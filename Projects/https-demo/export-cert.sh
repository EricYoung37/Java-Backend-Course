#!/bin/bash
set -a
source .env
set +a

keytool -exportcert \
  -alias "$KEY_ALIAS" \
  -keystore "src/main/resources/$KEY_STORE" \
  -rfc \
  -file "src/main/resources/https-demo.crt" \
  -storepass "$STORE_PWD"
