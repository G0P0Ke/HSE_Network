# Backend part of social network HSE-Apple
## coder: Andreev Anton (GoPoKe)

### Launch 
 I hope, you've set up your VDS: install MySQL and launch server, 
 OpenJDK17, configure your ssh key
 Go to file "deploy.sh" and replace all elements in parentheses {}

> deploy.sh script 
```
#!/usr/bin/env bash

echo 'Copy files'

scp -i {~/.ssh/id_rsa\your ssh private key placement} \
    {../target/coursework-0.0.1-SNAPSHOT.jar\your jar placement} \
    {root\your user name}@{ipV4 of your VDS}:~/{directory for project}

echo 'Restart server'

ssh -i {~/.ssh/id_rsa\your ssh private key placement}{root\your user name}@{ipV4 of your VDS} << EOF
pgrep java | xargs kill -9
nohup java -jar ~/{directory for project}/{your jar's name} > {file name for writting logs} &
EOF

echo 'Finish'
```

After you should check the "application.properties" file 
and again replace all elements in parentheses {}
This file use for VDS deploy, your dev properties are in "application-dev.properties" file


> application.properties script

```
spring.datasource.url=jdbc:mysql://localhost:3306/{db_name}?allowPublicKeyRetrieval=true&useSSL=false&amp&serverTimezone=UTC
spring.datasource.username={user to access db}
spring.datasource.password={password for user}
// show code for creating migration scripts
spring.jpa.generate-ddl=false
// show sql request to db
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=validate

management.endpoints.web.exposure.include=*

spring.servlet.multipart.max-request-size=50MB
spring.servlet.multipart.max-file-size=50MB
spring.jackson.serialization.fail-on-empty-beans=false

// use https://yandex.ru/support/mail/mail-clients/others.html for configure your mail.host
spring.mail.host=smtp.yandex.ru
spring.mail.username={username}@yandex.ru
spring.mail.password={password for mailbox}
spring.mail.port=465
spring.mail.protocol=smtps
// true - if you want to see mail debug information
mail.debug=false

spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER
```

### Database architecture 
Check src/main/resources/db/migration/V1__Init_DB.sql 
&& src/main/resources/db/migration/V2__Add_role.



