@echo off
set "TASKNAME=YanRongManager25566"
echo [UNINSTALL] Removing startup task: %TASKNAME%
schtasks /delete /tn "%TASKNAME%" /f
if %errorlevel%==0 (
  echo [DONE] Service removed.
) else (
  echo [INFO] Task not found or already removed.
)
pause
