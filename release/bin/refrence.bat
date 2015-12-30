@echo off

set base=%~1

set clspath=
setlocal enabledelayedexpansion

for %%j in (%base%lib\*.jar) do (
	set clspath=!clspath!%%j;
)

setlocal disabledelayedexpansion
set clspath=%clspath:~0,-1%