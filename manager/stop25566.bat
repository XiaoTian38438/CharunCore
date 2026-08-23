@echo off
cd /d "%~dp0"

if exist "25566.pid" (
  set /p PID=<25566.pid
  if defined PID (
    echo [STOP] Shutting down manager (PID %PID%)...
    taskkill /PID %PID% /F /T >nul 2>&1
    if exist "25566.pid" del /q 25566.pid >nul 2>&1
    echo [DONE] Manager stopped. 25565 also stopped if this panel started it.
    goto :eof
  )
)

echo [INFO] No pid file found, locating process by port 25566...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :25566 ^| findstr LISTENING') do (
  echo [STOP] Killing process on 25566, PID %%a...
  taskkill /PID %%a /F /T >nul 2>&1
)
echo [DONE]
pause
