#!/bin/bash
USER=james
IP=172.19.12.81
ZIPFILE='find_girls.tar.gz'
FOLDER=find_girls

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
echo $password | sudo -S rm -rf /webapp/$FOLDER
echo $password | sudo -S mv $FOLDER /webapp/
echo $password | sudo -S service unicorn.filegirls upgrade
exit

eeooff

echo done!
