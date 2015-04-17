#!/bin/bash
shopt -s expand_aliases
alias sudo='sudo env PATH=$PATH $@'

sudo bundle install
echo "sudo bundle install     r=$?"
sudo bundle exec  rake db:migrate RAILS_ENV=production
echo "sudo rake db:migrate RAILS_ENV=production             r=$?"
#sudo bundle exec  rake db:seed RAILS_ENV=production
#echo "rake db:seed RAILS_ENV=production              r=$?"
#sudo rake assets:precompile RAILS_ENV=production
#echo "rake assets:precompile RAILS_ENV=production           r=$?"

sudo service unicorn.findgirls upgrade
