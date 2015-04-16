#!/bin/bash
shopt -s expand_aliases
alias sudo='sudo env PATH=$PATH $@'

ID=`ps -ef | grep "resque" | grep -v "$0" | grep -v "grep" | awk '{print $2}'`
echo $ID

for id in $ID
do
   sudo kill -9 $id
   echo "killed $id"
done

if [ ! -d "/data/weblog/baozhao.yy.com/nginx" ]; then
 mkdir "/data/weblog/baozhao.yy.com/nginx"
 sudo chmod 777 "/data/weblog/baozhao.yy.com/nginx"
fi

if [ ! -d "/data/weblog/baozhao.yy.com/rails" ]; then
 mkdir "/data/weblog/baozhao.yy.com/rails"
 sudo chmod 777 "/data/weblog/baozhao.yy.com/rails"
fi

sudo mkdir tmp -p
sudo chmod 777 tmp

sudo bundle install
echo "sudo bundle install     r=$?"
sudo magic_encoding
echo "sudo magic_encoding          r=$?"
sudo bundle exec  rake db:migrate RAILS_ENV=production
echo "sudo rake db:migrate RAILS_ENV=production             r=$?"
sudo bundle exec  rake db:seed RAILS_ENV=production
echo "rake db:seed RAILS_ENV=production              r=$?"
#sudo rake assets:precompile RAILS_ENV=production
#echo "rake assets:precompile RAILS_ENV=production           r=$?"

sudo cp /data/gemfile/Gemfile.lock .

sudo mkdir /data/weblog/baozhao.yy.com/resque/ -p
sudo nohup bundle exec  rake resque:scheduler RAILS_ENV=production >> /data/weblog/baozhao.yy.com/resque/scheduler.log 2>&1 &
echo "nohup rake resque:scheduler RAILS_ENV=production  &          r=$?"
sudo nohup bundle exec  rake resque:work RAILS_ENV=production >> /data/weblog/baozhao.yy.com/resque/worker.log 2>&1 &
echo "rake resque:work RAILS_ENV=production &        r=$?"

sudo  /etc/init.d/unicorn.baozhao upgrade
