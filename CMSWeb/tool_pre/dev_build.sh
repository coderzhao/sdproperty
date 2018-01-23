#!/bin/bash
SHELLPATH=$(cd `dirname $0`; pwd)

cd $SHELLPATH/..
mvn clean package -Ppre

cd $SHELLPATH
./dev_hide_expect_upload.sh
#sshpass -p root321% ssh root -p22 -l n-tech-xxj<dev_hide_restarttomcat.sh