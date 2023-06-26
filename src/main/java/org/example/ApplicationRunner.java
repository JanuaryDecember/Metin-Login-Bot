package org.example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.List;

import static org.example.Account.loadAccountFromFile;
import static org.example.RobotAndTerminalUtils.*;
import static org.example.WindowsManager.arrangeWindows;
import static org.example.WindowsManager.getWindowsToArrange;

public class ApplicationRunner {
    public static void editConfig(String filePath) {
        File configFile = new File(filePath);
        File tempFile = new File(filePath + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(configFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                   switch (line) {
                        case "WIDTH":
                            line = "WIDTH\t\t\t\t640";
                            break;
                        case "HEIGHT":
                            line = "HEIGHT\t\t\t\t480";
                            break;
                        case "BPP":
                            line = "BPP\t\t\t\t\t16";
                            break;
                        case "ALWAYS_VIEW_NAME":
                            line = "ALWAYS_VIEW_NAME\t\t0";
                            break;
                        case "VIEW_CHAT":
                            line = "VIEW_CHAT\t\t\t0";
                            break;
                        case "ENABLE_EFFECTS":
                            line = "ENABLE_EFFECTS\t\t0";
                            break;
                        case "ENABLE_QUESTS":
                            line = "ENABLE_QUESTS\t\t0";
                            break;
                        case "SHADOW_LEVEL":
                            line = "SHADOW_LEVEL\t\t0";
                            break;
                        default:
                            break;
                }

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace the original file with the modified temporary file
        if (configFile.delete()) {
            tempFile.renameTo(configFile);
        } else {
            System.err.println("Failed to edit the configuration file.");
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException, AWTException {
        int repeatCount = 20, accountIterator = 0;

        List<Account> accountList = loadAccountFromFile("C:\\konta.txt", repeatCount);
        Robot robot = new Robot();

        String directoryPath = "C:\\XenoXMT2 - Klient";
        String fileName = "_XenoxMT2.exe";

        killProcessesByName("_XenoxMT2.exe");
        editConfig(directoryPath+"\\metin2.cfg");

        for (int i = 0; i < repeatCount; i++) {

            // Zmiana bieżącego katalogu
            File directory = new File(directoryPath);
            if (!directory.exists() || !directory.isDirectory()) {
                System.out.println("Podany katalog nie istnieje.");
                return;
            }
            System.setProperty("user.dir", directory.getAbsolutePath());

            // Uruchomienie aplikacji jako administrator bez wyświetlania okna cmd.exe
            boolean success = runAsAdministrator(fileName, directory.getAbsolutePath());

            if (success) {
                // Poczekaj na załadowanie aplikacji
                Thread.sleep(2000);

                // Wpisanie loginu, hasła i PINu
                typeString(robot, accountList.get(accountIterator).getLogin());

                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);

                typeString(robot, accountList.get(accountIterator).getPassword());

                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);

                typeString(robot, accountList.get(accountIterator).getPin());
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                accountIterator++;

            } else {
                System.out.println("Nie udało się uruchomić aplikacji jako administrator.");
            }
        }
        Thread.sleep(3000);
        arrangeWindows(getWindowsToArrange(), robot);
    }
}
