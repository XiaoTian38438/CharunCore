@echo off
setlocal
set SERVER=C:\Users\tian_\Desktop\mc-server-core1.21.11
set PLUGIN=%~dp0
set OUT=%PLUGIN%out

if not exist "%OUT%" mkdir "%OUT%"

rem 确保服务器已编译（插件 API 位于 target/classes）
cd /d "%SERVER%"
call mvn -o compile -DskipTests -q
if errorlevel 1 (
  echo [ERROR] 主服务器编译失败，请先修复主项目
  exit /b 1
)

rem 编译插件（依赖服务器 target/classes 中的插件 API）
cd /d "%PLUGIN%"
javac -cp "%SERVER%\target\classes" -d "%OUT%" MyPlugin.java
if errorlevel 1 (
  echo [ERROR] 插件编译失败
  exit /b 1
)

rem 打包 jar（含 plugin.yml）
if not exist "%SERVER%\plugins" mkdir "%SERVER%\plugins"
jar cf "%SERVER%\plugins\MyPlugin.jar" -C "%OUT%" . -C "%PLUGIN%" plugin.yml
echo [OK] 已生成 %SERVER%\plugins\MyPlugin.jar
endlocal
