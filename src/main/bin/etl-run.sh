#!/bin/bash
#******************************************************************************
#
#  NAME:       etl-run.sh
#  PURPOSE:    script to run etl tool
#
#  CREATED:    August 2015 Asar Khan
#
#  See source control for revision history
#
#******************************************************************************/

set -o pipefail

usage(){
  echo "Usage: $0 /template=[template name] [/property-name=property-value]"
}

###############################################################
# Parameters are start, stop restart and status
###############################################################

APP_DIR=`dirname $0`/..
if [ -d $APP_DIR ]; then
  cd $APP_DIR
else
  echo "ERROR: Directory $APP_DIR does not exist"
  exit 1
fi

#Needs JDK7
#
JAVA_HOME=$HOME/apps/java/64bit/jdk1.7.0_51
APP_JAR=`ls jar/etl-tool*.jar`
APP_NAME=etl-tool

LOG_DIR=/var/app/murex3/logs/musi/apps/pvhub
LOG_FILE=$LOG_DIR/${APP_NAME}.log
PID_FILE=`dirname $0`/${APP_NAME}.pid

CLASS_NAME=com.inservio.tools.etl.EtlTool
JVM_ARGS="-XX:MaxPermSize=256m -Xmx64m -Xmx512m -Dlog4j.configurationFile=log4j2-file.xml -DLOG_DIR=$LOG_DIR -DPVH_MODULE=$APP_NAME -Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager"
#Declare the CLASSPATH
#
CLASSPATH="${APP_JAR}:config:etl"

for j in `find ./lib -type f | sed -e 's/^\.\///'`
do
  CLASSPATH=${CLASSPATH}:$j
done

###############################################################
# main
###############################################################

echo "INFO: Using JAVA_HOME: $JAVA_HOME"
echo "INFO: Using CLASSPATH: $CLASSPATH"
echo "INFO: Using JVM_ARGS: $JVM_ARGS"
echo "INFO: Using LOG_FILE: $LOG_FILE"
${JAVA_HOME}/bin/java -DLOG_FILE=${LOG_FILE} ${JVM_ARGS} -cp $CLASSPATH ${CLASS_NAME} $@
