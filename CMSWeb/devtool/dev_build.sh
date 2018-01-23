#!/bin/bash
SHELLPATH=$(cd `dirname $0`; pwd)

cd $SHELLPATH/..
mvn clean package -Pprod

cd $SHELLPATH
./dev_hide_expect_upload.sh
#sshpass -p xu_caveman ssh 182.18.26.42 -p5022 -l root<dev_hide_restarttomcat.sh