@echo off
echo Building KeyLogger Project...

rem Create bin directory if not exists
if not exist "bin" mkdir bin

rem Compile Java files
javac -encoding UTF-8 -cp ".;libs/*" -d bin src/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ Build successful!
    echo.
    echo To run KeyLogger:
    echo   java -cp ".;bin;libs/*" Keylogger
    echo.
    echo To test trap methods:
    echo   java -cp ".;bin;libs/*" TrapFileDemo
) else (
    echo.
    echo ❌ Build failed!
    exit /b 1
)

pause