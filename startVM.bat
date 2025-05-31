@echo off
title Ejecutando Maquina Virtual
echo.
echo Compilando...
timeout /t 1 >nul
echo Preparando entorno...
timeout /t 1 >nul
echo Iniciando ejecucion...
timeout /t 1 >nul
echo.
java -cp bin main.mainVM %*
echo.
echo Ejecucion finalizada.
echo.
pause > null