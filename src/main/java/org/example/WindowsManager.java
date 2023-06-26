package org.example;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class WindowsManager {
    public static List<WinDef.HWND> getWindowsToArrange() {
        List<WinDef.HWND> windowList = new ArrayList<>();
        List<WinDef.HWND> windowsToArrange = new ArrayList<>();

        User32.INSTANCE.EnumWindows((hwnd, data) -> {
            if (User32.INSTANCE.IsWindowVisible(hwnd)) {
                windowList.add(hwnd);
            }
            return true;
        }, null);

        // Wyświetlanie informacji o procesach okien
        for (WinDef.HWND hwnd : windowList) {
            IntByReference processIdRef = new IntByReference();
            User32.INSTANCE.GetWindowThreadProcessId(hwnd, processIdRef);
            int processId = processIdRef.getValue();

            WinNT.HANDLE processHandle = Kernel32.INSTANCE.OpenProcess(Kernel32.PROCESS_QUERY_INFORMATION | Kernel32.PROCESS_VM_READ, false, processId);
            if (processHandle != null) {
                String processName = getProcessName(processHandle);
                Kernel32.INSTANCE.CloseHandle(processHandle);
                if (processName != null && processName.equals("_XenoxMT2.exe")) {
                    windowsToArrange.add(hwnd);
                    System.out.println("Okno: " + hwnd + ", Proces: " + processName);
                }
            }
        }
        return windowsToArrange;
    }


    private static String getProcessName(WinNT.HANDLE processHandle) {
        char[] buffer = new char[1024];
        Psapi.INSTANCE.GetProcessImageFileName(processHandle, buffer, buffer.length);
        String imagePath = Native.toString(buffer);

        // Wyodrębnianie nazwy procesu
        int lastBackslashIndex = imagePath.lastIndexOf("\\");
        if (lastBackslashIndex >= 0 && lastBackslashIndex < imagePath.length() - 1) {
            return imagePath.substring(lastBackslashIndex + 1);
        }

        return null;
    }

    public static void arrangeWindows(List<WinDef.HWND> windows, Robot robot) throws InterruptedException {
        // Config
        int windowWidth = 640;
        int windowHeight = 480;
        int MonitorX = 0;
        int MonitorY = 0;
        int windowCount = 0;
        for (WinDef.HWND hwnd : windows) {
            User32.INSTANCE.SetForegroundWindow(hwnd);
            int x = 0, y = 0;

            if (windowCount <= 4) {
                // Okna na lewym monitorze (pierwszy rząd)
                x = MonitorX + windowCount * windowWidth;
                y = MonitorY;
            } else if (windowCount <= 9) {
                x = MonitorX + (windowCount - 5) * windowWidth;
                y = MonitorY + windowHeight;
            } else if (windowCount <= 14) {
                // Okna na lewym monitorze (drugi rząd)
                x = MonitorX + (windowCount-10) * windowWidth;
                y = MonitorY + 2 * windowHeight;
            }
            else if (windowCount <= 19) {
                // Okna na lewym monitorze (drugi rząd)
                x = MonitorX + (windowCount-15) * windowWidth;
                y = MonitorY + 3 * windowHeight;
            }


            User32.INSTANCE.SetWindowPos(hwnd, null, x, y, 0, 0, User32.SWP_NOSIZE | User32.SWP_SHOWWINDOW);
            Thread.sleep(200);
            robot.keyPress(KeyEvent.VK_ENTER);
            Thread.sleep(300);
            robot.keyRelease(KeyEvent.VK_ENTER);

            windowCount++;
        }
    }

}
