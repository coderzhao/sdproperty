#!/usr/bin/expect -f

#date is 2011-11-14
set password xu_caveman

#upload remote host
spawn scp -P5022 -r ./../target/sdproperty.war root@182.18.26.42:/usr/local/tomcat9/webapps/
set timeout 5
expect {
"yes/no" {send "yes\r";exp_continue}
}
expect "root@182.18.26.42's password:"
set timeout 5
send "$password\r"
set timeout 220
send "exit\r"
expect eof