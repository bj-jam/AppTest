@ECHO OFF&PUSHD %~DP0 &TITLE ����ASM.JAR
Rd "%WinDir%\system32\test_permissions" >nul 2>nul
Md "%WinDir%\System32\test_permissions" 2>nul||(Echo ��ʹ���Ҽ�����Ա������У�&&Pause >nul&&Exit)
Rd "%WinDir%\System32\test_permissions" 2>NUL
SetLocal EnableDelayedExpansion
mode con cols=60 lines=16
color 2F
:Menu
Cls
@echo -----------------------------------------------------------

@echo    ���Ļ�ӭ����ʹ��

@echo �밴˳��������ĸ���Ա���������asm.jar

@echo    1��  ����1���Զ�����asm.jar

@echo    2��  ���������ַ���������Ч������������

@echo    ��ӭ�������Ŀռ� 2015-04-04 ����

@echo -----------------------------------------------------------

@ echo.
set /p xj= �������ְ��س���
if /i "%xj%"=="1" Goto Start
@ echo.
echo      ������Ч������������
ping -n 2 127.1>nul
goto menu
:Start
@ echo.
echo ���������ڽ���D��..���Ե�..
(E:)
@ echo.
echo ���������ڽ���asm.jar�ļ���..
(cd E:/sdk/platform-tools)
@ echo.
echo ��������������asm.jar..
(java -jar asm.jar)
goto :Menu