@echo off
cd /d "%~dp0"

set "NODE_EXE=%USERPROFILE%\.workbuddy\binaries\node\versions\22.22.2\node.exe"
if not exist "%NODE_EXE%" set "NODE_EXE=node"

if not exist "%NODE_EXE%" (
  echo [ERROR] Node.js not found. Install Node.js or fix NODE_EXE path in this script.
  pause
  exit /b 1
)

if exist "25566.pid" (
  set /p OLD_PID=<25566.pid
  for /f "tokens=5" %%a in ('netstat -ano ^| findstr :25566 ^| findstr LISTENING') do (
    if "%%a"=="%OLD_PID%" (
      echo [INFO] 25566 already running with PID %OLD_PID%. Aborting to avoid duplicate instance.
      pause
      exit /b 0
    )
  )
  del /q 25566.pid >nul 2>&1
)

echo [START] Manager panel starting on port 25566 in foreground mode.
echo         Open  http://localhost:25566  in your browser after it binds.
echo         Default admin account: admin / admin  - change it in config.json
echo         Close this window or run stop25566.bat to stop.
echo.

"%NODE_EXE%" "%~dp0server.js"

echo [STOPPED] Manager process exited.
pause
