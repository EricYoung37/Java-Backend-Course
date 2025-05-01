#!/bin/bash
set -a
source .env
set +a

keytool -genkeypair \
  -alias "$KEY_ALIAS" \
  -keyalg RSA \
  -keysize 2048 \
  -storetype JKS \
  -keystore "src/main/resources/$KEY_STORE" \
  -validity 365 \
  -storepass "$STORE_PWD" \
  -keypass "$KEY_PWD" \
  -dname "CN=localhost, OU=Dev, O=HTTPS Demo, L=City, S=State, C=US" \
  -ext "SAN=DNS:localhost,DNS:127.0.0.1"
