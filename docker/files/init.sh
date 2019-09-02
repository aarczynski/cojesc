#!/usr/bin/env bash

echo $GOOGLE_API_KEY > /app/google-api-key.json

exec $@
