package com.assistant.controller;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.imageio.ImageIO;

public class PCController {

    private Robot robot;

    public PCController() {
        try {
            this.robot = new Robot();
        } catch (Exception e) {
            System.err.println("Failed to initialize Java Robot: " + e.getMessage());
        }
    }

    public String launchApp(String appName) {
        if (appName == null || appName.trim().isEmpty()) {
            return "App name not specified.";
        }
        String cleanApp = appName.trim().toLowerCase(Locale.ROOT);
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);

        try {
            if (os.contains("win")) {
                switch (cleanApp) {
                    case "notepad":
                        Runtime.getRuntime().exec("notepad.exe");
                        break;
                    case "calculator":
                    case "calc":
                        Runtime.getRuntime().exec("calc.exe");
                        break;
                    case "cmd":
                    case "terminal":
                    case "command prompt":
                        Runtime.getRuntime().exec("cmd.exe /c start cmd");
                        break;
                    case "powershell":
                        Runtime.getRuntime().exec("powershell.exe");
                        break;
                    case "explorer":
                    case "file explorer":
                    case "files":
                        Runtime.getRuntime().exec("explorer.exe");
                        break;
                    case "chrome":
                        Runtime.getRuntime().exec("cmd.exe /c start chrome");
                        break;
                    case "edge":
                        Runtime.getRuntime().exec("cmd.exe /c start msedge");
                        break;
                    case "word":
                        Runtime.getRuntime().exec("cmd.exe /c start winword");
                        break;
                    case "excel":
                        Runtime.getRuntime().exec("cmd.exe /c start excel");
                        break;
                    case "mspaint":
                    case "paint":
                        Runtime.getRuntime().exec("mspaint.exe");
                        break;
                    default:
                        // Generic start on Windows
                        Runtime.getRuntime().exec("cmd.exe /c start " + appName);
                        break;
                }
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec(new String[]{"open", "-a", appName});
            } else { // Linux / Unix
                Runtime.getRuntime().exec(new String[]{"bash", "-c", appName + " &"});
            }
            return "Launching application: " + appName;
        } catch (Exception e) {
            return "Failed to launch application '" + appName + "': " + e.getMessage();
        }
    }

    public String typeText(String text) {
        if (text == null || text.isEmpty()) {
            return "No text provided to type.";
        }

        try {
            // Copy text to clipboard and simulate Ctrl+V / Cmd+V for maximum reliability with unicode & symbols
            StringSelection stringSelection = new StringSelection(text);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);

            if (robot == null) {
                robot = new Robot();
            }

            robot.delay(300); // short pause before typing

            String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
            int mask = os.contains("mac") ? KeyEvent.VK_META : KeyEvent.VK_CONTROL;

            robot.keyPress(mask);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(mask);

            return "Typed: \"" + text + "\"";
        } catch (Exception e) {
            return "Error typing text: " + e.getMessage();
        }
    }

    public String openWebsite(String url) {
        if (url == null || url.trim().isEmpty()) {
            return "No URL provided.";
        }
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
                return "Opened website: " + url;
            } else {
                String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
                if (os.contains("win")) {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                } else if (os.contains("mac")) {
                    Runtime.getRuntime().exec("open " + url);
                } else {
                    Runtime.getRuntime().exec("xdg-open " + url);
                }
                return "Opened website: " + url;
            }
        } catch (Exception e) {
            return "Failed to open website: " + e.getMessage();
        }
    }

    public String executeSystemCommand(String command) {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        try {
            switch (command.toLowerCase(Locale.ROOT)) {
                case "shutdown":
                    if (os.contains("win")) {
                        Runtime.getRuntime().exec("shutdown /s /t 60");
                        return "System will shutdown in 60 seconds. (Run 'shutdown /a' in CMD to cancel)";
                    } else if (os.contains("mac")) {
                        Runtime.getRuntime().exec("sudo shutdown -h +1");
                        return "Shutdown scheduled in 1 minute.";
                    } else {
                        Runtime.getRuntime().exec("shutdown -h +1");
                        return "Shutdown scheduled in 1 minute.";
                    }

                case "restart":
                    if (os.contains("win")) {
                        Runtime.getRuntime().exec("shutdown /r /t 60");
                        return "System will restart in 60 seconds.";
                    } else {
                        Runtime.getRuntime().exec("shutdown -r +1");
                        return "Restart scheduled in 1 minute.";
                    }

                case "lock":
                    if (os.contains("win")) {
                        Runtime.getRuntime().exec("rundll32.exe user32.dll,LockWorkStation");
                        return "Screen locked.";
                    } else if (os.contains("mac")) {
                        Runtime.getRuntime().exec("pmset displaysleepnow");
                        return "Screen locked.";
                    } else {
                        Runtime.getRuntime().exec("gnome-screensaver-command -l");
                        return "Screen locked.";
                    }

                case "sleep":
                    if (os.contains("win")) {
                        Runtime.getRuntime().exec("rundll32.exe powrprof.dll,SetSuspendState 0,1,0");
                        return "Putting PC to sleep.";
                    } else if (os.contains("mac")) {
                        Runtime.getRuntime().exec("pmset sleepnow");
                        return "Putting PC to sleep.";
                    } else {
                        Runtime.getRuntime().exec("systemctl suspend");
                        return "Putting PC to sleep.";
                    }

                default:
                    return "Unknown or unsupported system command: " + command;
            }
        } catch (Exception e) {
            return "Error executing system command '" + command + "': " + e.getMessage();
        }
    }

    public String takeScreenshot() {
        try {
            if (robot == null) {
                robot = new Robot();
            }
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle screenRect = new Rectangle(screenSize);
            BufferedImage image = robot.createScreenCapture(screenRect);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "screenshot_" + timestamp + ".png";
            File outputFile = new File(fileName);
            ImageIO.write(image, "png", outputFile);

            return "Screenshot saved to: " + outputFile.getAbsolutePath();
        } catch (Exception e) {
            return "Failed to capture screenshot: " + e.getMessage();
        }
    }
}
