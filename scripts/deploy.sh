#!/usr/bin/env bash

echo 'Copy files'

scp -i ~/.ssh/id_rsa \
    ../target/coursework-0.0.1-SNAPSHOT.jar \
    root@109.68.214.48:~/coursework

echo 'Restart server'

ssh -i ~/.ssh/id_rsa root@109.68.214.48 << EOF
pgrep java | xargs kill -9
nohup java -jar ~/coursework/coursework-0.0.1-SNAPSHOT.jar > log.txt &
EOF

echo 'Finish'