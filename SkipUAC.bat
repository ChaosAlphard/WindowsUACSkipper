@echo off
chcp 65001
cd /d "%~dp0"

if "" equ "%~1" (
  goto welcome
)

if 0 equ %~z1 (
  echo 获取到的文件大小为0
  pause
  exit
)
:: 检查管理员权限
net session >nul 2>&1
if %errorlevel% neq 0 (
  echo 需要管理员权限执行
  powershell Start-Process "%~nx0" -Verb RunAs -ArgumentList "\"%~1\""
  exit
)
:: 创建计划任务
schtasks /create /tn "UACSkip\%~n1" /tr "%~1" /rl highest /sc once /sd 2000/01/01 /st 00:00
:: 创建启动方式
echo schtasks /run /tn "UACSkip\%~n1" >%~n1.skipuac.bat

pause
exit

:welcome
cls
echo 欢迎使用Windows UAC Skipper
echo.
echo 1: 创建免UAC快捷启动方式
echo.
echo 2: 仅查询已授权的免UAC启动程序
echo.
echo 3: 查询/删除已授权的免UAC启动程序(需要管理员权限)
echo.
echo 0: 退出
echo.
choice /c 1230 /n /m 请输入数字来选择需要执行的操作:
if "%errorlevel%" equ "1" (
  echo 请将想要免UAC启动的程序执行文件直接拖放到"%~nx0"文件上
  pause
  goto welcome
)
if "%errorlevel%" equ "2" (
  powershell Start-Process "src/DeleteUACAuth.bat"
  exit
)
if "%errorlevel%" equ "3" (
  powershell Start-Process "src/DeleteUACAuth.bat" -Verb RunAs
  exit
)
if "%errorlevel%" equ "0" (
  exit
)
exit
