_now=$(date +"%Y%m%d_%H%M%S")
_file="/home/ftpshared/DatabaseBackups/APP_$_now.sql"
echo "Initiating backup $_file"
cd /usr/share/apache-tomcat-7.0.61/webapps/
zip -r /backup/APP_$_now.zip *
