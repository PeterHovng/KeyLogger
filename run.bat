@echo off
echo Running KeyLogger...

rem Check if compiled
if not exist "bin\Keylogger.class" (
    echo Project not compiled yet. Building...
    call build.bat
    if %ERRORLEVEL% NEQ 0 exit /b 1
)

rem Run KeyLogger
java -cp ".;bin;libs/*" Keylogger