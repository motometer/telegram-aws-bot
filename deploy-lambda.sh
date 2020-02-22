#!/bin/bash

./gradlew clean build -x check > /dev/null
aws lambda update-function-code --function-name motometer-bot-test --zip-file 'fileb://build/distributions/telegram-aws-bot-0.0.1-SNAPSHOT.zip'
echo "Lambda uploaded successfully"
