#!/bin/bash
#******************************************************************************
#
#  NAME:       passw.sh
#  PURPOSE:    script to create encrypted password
#
#  CREATED:    August 2015 Asar Khan
#
#  See source control for revision history
#
#******************************************************************************/

APP_DIR=`dirname $0`/..
if [ -d $APP_DIR ]; then
  cd $APP_DIR
else
  echo "ERROR: Directory $APP_DIR does not exist"
  exit 1
fi

export ETLTOOL_KEY=mxtools

JAVA_HOME=/opt/jdk1.8.0_131
APP_JAR=jar/etl-tool-1.0.0.jar
APP_NAME=etl-tool

CLASS_NAME=com.inservio.tools.PasswordTool
JVM_ARGS="-Xmx64m -Xmx512m"
#Declare the CLASSPATH
#
CLASSPATH="${APP_JAR}:config:etl"

for j in `find ./lib -type f | sed -e 's/^\.\///'`
do
  CLASSPATH=${CLASSPATH}:$j
done

################################################################
## main
################################################################

echo -----
echo INFO: Using JAVA_HOME: $JAVA_HOME
echo INFO: Using CLASSPATH: $CLASSPATH
echo INFO: Using JVM_ARGS: $JVM_ARGS
echo -----

$JAVA_HOME/bin/java $JVM_ARGS -cp $CLASSPATH $CLASS_NAME $@
