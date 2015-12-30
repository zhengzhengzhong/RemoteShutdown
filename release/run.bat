@echo off

set base=%~dp0

rem call %base%bin\refrence.bat %base%

set clspath=
setlocal enabledelayedexpansion

for %%j in (%base%lib\*.jar) do (
	set clspath=!clspath!%%j;
)

setlocal disabledelayedexpansion
set clspath=%clspath:~0,-1%

set _MAINCLASS=cn.letterme.tools.shutdown.Application

java -Dtools.root="%base%\" -Dlogback.configurationFile=%base%conf\logback.xml -classpath %clspath% %_MAINCLASS%