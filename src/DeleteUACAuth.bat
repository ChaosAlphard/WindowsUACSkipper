@echo off
chcp 65001
cd /d "%~dp0"

:: 检查管理员权限
net session >nul 2>&1
if %errorlevel% neq 0 (
  echo 开始查询已授权程序，要删除程序的授权需要以管理员权限执行该文件
  schtasks /query /tn UACSkip\
  echo.
  pause
  exit
)

:loop
cls

echo 开始查询已授权程序
schtasks /query /tn UACSkip\
echo.
echo 1: 删除程序的授权
echo.
echo 2: 重新查询已授权程序
echo.
echo 0: 退出
echo.
choice /c 120 /n /m 请输入数字来选择需要执行的操作：

if "%errorlevel%" equ "1" (
  goto delete
)
if "%errorlevel%" equ "2" (
  goto loop
)
if "%errorlevel%" equ "0" (
  exit
)

exit

:delete
set /p taskname=请输入需要删除的任务名：
echo 开始删除任务 "UACSkip\%taskname%"
:: 删除计划任务
schtasks /delete /tn "UACSkip\%taskname%"

pause
goto loop
