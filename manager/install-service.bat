@echo off
cd /d "%~dp0"
set "TASKNAME=YanRongManager25566"
set "BAT=%~dp0start25566.bat"
echo [INSTALL] Creating startup task: %TASKNAME%
schtasks /create /tn "%TASKNAME%" /tr "\"%BAT%\"" /sc onlogon /rl highest /f
if %errorlevel%==0 (
  echo [DONE] Service installed. The manager will auto-start on user logon.
) else (
  echo [FAIL] Install failed (see above).
)
pause
