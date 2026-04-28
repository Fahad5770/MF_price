_now=$(date +"%Y%m%d_%H%M%S")
cd /home/ftpshared/WorkflowAttachments
zip -r /backup/ATT_$_now.zip *
