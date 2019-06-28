@echo off
rem #******************************************************************************
rem #
rem #  NAME:       etl-run.bat
rem #  PURPOSE:    script to run etl tool
rem #
rem #  CREATED:    August 2015 Asar Khan
rem #
rem #  See source control for revision history
rem #
rem #******************************************************************************/

setlocal

cd ..

SET JAVA_HOME=C:\appl\Java\jdk1.7.0_51
set APP_JAR=jar\etl-tool-1.0.0.jar
set APP_NAME=etl-tool

set LOG_DIR=logs
set LOG_FILE=%LOG_DIR%\%APP_NAME%.log

set CLASS_NAME=com.inservio.tools.etl.EtlTool
set JVM_ARGS=-Xmx64m -Xmx512m -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager -Dlog4j.configurationFile=log4j2-file.xml -DLOG_DIR=%LOG_DIR% -DLOG_FILE=%LOG_FILE%
set LIBS="commons-jexl-1.2-SNAPSHOT.jar;jasypt-1.9.2.jar;jtds-1.3.1.jar;log4j-api-2.3.jar;log4j-core-2.3.jar;log4j-jul-2.3.jar;scriptella-core-1.2-SNAPSHOT.jar;scriptella-drivers-1.2-SNAPSHOT.jar;scriptella-tools-1.2-SNAPSHOT.jar"
set CLASSPATH="%APP_JAR%;config;etl;lib\*"

rem ################################################################
rem ## main
rem ################################################################

title %~n0 ETL-TOOL

echo -----
echo INFO: Using JAVA_HOME: %JAVA_HOME%
echo INFO: Using CLASSPATH: %CLASSPATH%
echo INFO: Using JVM_ARGS: %JVM_ARGS%
echo INFO: Using LOG_FILE: %LOG_FILE%
echo -----
ech0.

%JAVA_HOME%\bin\java %JVM_ARGS% -cp %CLASSPATH% %CLASS_NAME%  "%1" "%2" "%3" "%4" "%5" "%6"

title Command Prompt
endlocal
