@echo off
chcp 65001
cd /d "%~dp0"

if "" equ "%~1" (
  echo 请将文件直接拖放到"%~nx0"文件上
  pause
  exit
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
echo schtasks /run /tn "UACSkip\%~n1" > %~n1.bat

pause
