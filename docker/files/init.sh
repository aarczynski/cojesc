#!/usr/bin/env bash

sed -i s#@FACEBOOK-APP-ID@#$FACEBOOK_APP_ID# /app/application.yaml
sed -i s#@FACEBOOK-APP-SECRET@#$FACEBOOK_APP_SECRET# /app/application.yaml
echo $GOOGLE_API_KEY > /app/google-api-key.json

exec $@
