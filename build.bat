@echo off
rem Compila y empaqueta inputfile-java.jar (clases + fuentes) en dist\.
setlocal
cd /d "%~dp0"
if exist out rmdir /s /q out
if exist dist rmdir /s /q dist
mkdir out
mkdir dist
javac --release 11 -encoding UTF-8 -d out src\com\sha2cs\inputfile\InputFile.java || exit /b 1
xcopy /e /i /q src out >nul
jar --create --file dist\inputfile-java.jar -C out .
rmdir /s /q out
echo Generado: dist\inputfile-java.jar
