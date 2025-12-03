#!/bin/bash

_now=$(date +"%Y%m%d_%H%M%S")
_file="/home/ftpshared/DatabaseBackups/M_PEP_$_now.sql"
echo "Initiating backup $_file"
mysqldump -uadmin -h155.135.0.35 -ptiger@pbc pep > /home/ftpshared/DatabaseBackups/M_PEP_$_now.sql
mysqldump -uadmin -h155.135.0.35 -ptiger@pbc peplogs > /home/ftpshared/DatabaseBackups/M_PEPLOGS_$_now.sql
mysqldump -uadmin -h155.135.0.35 -ptiger@pbc ksml > /home/ftpshared/DatabaseBackups/M_KSML_$_now.sql

cd /home/ftpshared/DatabaseBackups
tar -cvzf M_PEP_$_now.tar.gz M_PEP_$_now.sql
tar -cvzf M_PEPLOGS_$_now.tar.gz M_PEPLOGS_$_now.sql
tar -cvzf M_KSML_$_now.tar.gz M_KSML_$_now.sql

rm -f M_PEP_$_now.sql
rm -f M_PEPLOGS_$_now.sql
rm -f M_KSML_$_now.sql

/bin/cp *.tar.gz /backup
rm -rf *.tar.gz
