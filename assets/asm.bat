@ECHO OFF&PUSHD %~DP0 &TITLE 启动ASM.JAR
Rd "%WinDir%\system32\test_permissions" >nul 2>nul
Md "%WinDir%\System32\test_permissions" 2>nul||(Echo 请使用右键管理员身份运行！&&Pause >nul&&Exit)
Rd "%WinDir%\System32\test_permissions" 2>NUL
SetLocal EnableDelayedExpansion
mode con cols=60 lines=16
color 2F
:Menu
Cls
@echo -----------------------------------------------------------

@echo    子文欢迎您的使用

@echo 请按顺序输入字母，以便正常启动asm.jar

@echo    1、  输入1会自动启动asm.jar

@echo    2、  输入其他字符：输入无效，请重新输入

@echo    欢迎来到子文空间 2015-04-04 创建

@echo -----------------------------------------------------------

@ echo.
set /p xj= 输入数字按回车：
if /i "%xj%"=="1" Goto Start
@ echo.
echo      输入无效，请重新输入
ping -n 2 127.1>nul
goto menu
:Start
@ echo.
echo 　　　正在进入D盘..请稍等..
(E:)
@ echo.
echo 　　　正在进入asm.jar文件夹..
(cd E:/sdk/platform-tools)
@ echo.
echo 　　　正在运行asm.jar..
(java -jar asm.jar)
goto :Menu