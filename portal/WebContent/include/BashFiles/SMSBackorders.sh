#!/bin/bash

export CLASSPATH=${CLASSPATH}:/usr/share/apache-tomcat-7.0.40/webapps/portal/WEB-INF/classes
export CLASSPATH=${CLASSPATH}:/usr/share/apache-tomcat-7.0.40/lib/servlet-api.jar
export CLASSPATH=${CLASSPATH}:/usr/share/apache-tomcat-7.0.40/lib/sapjco3.jar
export CLASSPATH=${CLASSPATH}:/usr/share/apache-tomcat-7.0.40/webapps/portal/WEB-INF/lib/mysql-connector-java-5.1.23-bin.jar
export CLASSPATH=${CLASSPATH}:/usr/share/apache-tomcat-7.0.40/webapps/portal/WEB-INF/lib/commons-lang3-3.1.jar

/usr/java/jdk1.7.0_79/bin/java com.pbc.sms.S110BackorderStatsAmir
/usr/java/jdk1.7.0_79/bin/java com.pbc.sms.S110BackorderStatsSD3
/usr/java/jdk1.7.0_79/bin/java com.pbc.sms.S110BackorderStatsSaqib




