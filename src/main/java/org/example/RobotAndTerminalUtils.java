package org.example;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.Map;

public class RobotAndTerminalUtils {
    private static final Map<Character, Integer> converter = Map.ofEntries(
            new AbstractMap.SimpleEntry<>('0', 48),
            new AbstractMap.SimpleEntry<>('1', 49),
            new AbstractMap.SimpleEntry<>('2', 50),
            new AbstractMap.SimpleEntry<>('3', 51),
            new AbstractMap.SimpleEntry<>('4', 52),
            new AbstractMap.SimpleEntry<>('5', 53),
            new AbstractMap.SimpleEntry<>('6', 54),
            new AbstractMap.SimpleEntry<>('7', 55),
            new AbstractMap.SimpleEntry<>('8', 56),
            new AbstractMap.SimpleEntry<>('9', 57),
            new AbstractMap.SimpleEntry<>('a', 65),
            new AbstractMap.SimpleEntry<>('b', 66),
            new AbstractMap.SimpleEntry<>('c', 67),
            new AbstractMap.SimpleEntry<>('d', 68),
            new AbstractMap.SimpleEntry<>('e', 69),
            new AbstractMap.SimpleEntry<>('f', 70),
            new AbstractMap.SimpleEntry<>('g', 71),
            new AbstractMap.SimpleEntry<>('h', 72),
            new AbstractMap.SimpleEntry<>('i', 73),
            new AbstractMap.SimpleEntry<>('j', 74),
            new AbstractMap.SimpleEntry<>('k', 75),
            new AbstractMap.SimpleEntry<>('l', 76),
            new AbstractMap.SimpleEntry<>('m', 77),
            new AbstractMap.SimpleEntry<>('n', 78),
            new AbstractMap.SimpleEntry<>('o', 79),
            new AbstractMap.SimpleEntry<>('p', 80),
            new AbstractMap.SimpleEntry<>('q', 81),
            new AbstractMap.SimpleEntry<>('r', 82),
            new AbstractMap.SimpleEntry<>('s', 83),
            new AbstractMap.SimpleEntry<>('t', 84),
            new AbstractMap.SimpleEntry<>('u', 85),
            new AbstractMap.SimpleEntry<>('v', 86),
            new AbstractMap.SimpleEntry<>('w', 87),
            new AbstractMap.SimpleEntry<>('x', 88),
            new AbstractMap.SimpleEntry<>('y', 89),
            new AbstractMap.SimpleEntry<>('z', 90)
    );
    public static void typeString(Robot robot, String text) {

        for (int i = 0; i < text.length(); i++) {
            // Ustawienie kodu klawisza na odpowiadający wartości znaku
            robot.keyPress(converter.get(text.charAt(i)));
            robot.keyRelease(converter.get(text.charAt(i)));
        }
        System.out.println(text);
    }
    public static void killProcessesByName(String processName) {
        String os = System.getProperty("os.name").toLowerCase();

        try {
            Process process;
            if (os.contains("win")) {
                process = Runtime.getRuntime().exec("taskkill /F /IM " + processName);
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                process = Runtime.getRuntime().exec("pkill " + processName);
            } else {
                throw new UnsupportedOperationException("Unsupported operating system: " + os);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Processes with name '" + processName + "' have been terminated.");
            } else {
                System.out.println("Failed to terminate processes with name '" + processName + "'.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static boolean runAsAdministrator(String fileName, String workingDirectory) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            try {
                // Tworzenie pliku vbscript
                String vbscriptContent = "Set objShell = CreateObject(\"Wscript.Shell\")\n"
                        + "objShell.Run \"cmd /c cd /d " + workingDirectory + " && " + fileName + "\", 0, false";
                Path vbscriptFile = Files.createTempFile("runasadmin", ".vbs");
                Files.write(vbscriptFile, vbscriptContent.getBytes(), StandardOpenOption.CREATE);

                // Uruchamianie vbscript jako administrator bez wyświetlania okna cmd.exe
                ProcessBuilder processBuilder = new ProcessBuilder("wscript.exe", vbscriptFile.toAbsolutePath().toString());
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                process.waitFor();

                // Usuwanie pliku vbscript
                Files.deleteIfExists(vbscriptFile);

                return true;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
