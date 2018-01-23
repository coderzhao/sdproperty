#!/usr/bin/expect -f

#date is 2011-11-14
set password root321%

#upload remote host
spawn scp -P22 -r ./../target/sdproperty.war root@1747785do9.imwork.net:/entai/server/apache-tomcat-9.0.0.M22/webapps/
set timeout 5
expect {
"yes/no" {send "yes\r";exp_continue}
}
expect "root@1747785do9.imwork.net's password:"
set timeout 5
send "$password\r"
set timeout 120
send "exit\r"
expect eof