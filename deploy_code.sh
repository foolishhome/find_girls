#!/bin/bash
USER=james
IP=172.19.12.81
ZIPFILE='webroot.tar.gz'
FOLDER='webroot'

echo rezip $ZIPFILE
if [ -f $ZIPFILE ]; then
	rm $ZIPFILE
fi
tar -zcvf $ZIPFILE $FOLDER

echo transmit $ZIPFILE
scp $ZIPFILE $USER@$IP:/tmp/

echo exe script in server, input root password
read password

ssh $USER@$IP > /dev/null 2>&1 << eeooff

cd /tmp
rm -rf $FOLDER
tar -zxvf $ZIPFILE
echo $password | sudo -S rm -rf /webapp/find_girls/$FOLDER
echo $password | sudo -S mkdir /webapp
echo $password | sudo -S mkdir /webapp/find_girls
echo $password | sudo -S mv $FOLDER /webapp/find_girls/
cd /webapp/find_girls/$FOLDER
echo $password | sudo -S bundle install
echo $password | sudo -S bundle exec rake db:migrate RACK_ENV=production
echo $password | sudo -S service unicorn.findgirls upgrade
exit

eeooff

echo done!
