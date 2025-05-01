# Essential IDEA Configurations

## Module Import
This mini project is a module of the root repository
(that contains the folders `Short Questions`, `Projects`, and `Coding`).

If no run button shows up in the application class, the module needs to be added manually
by doing **Import module from external model — Maven** in **Project Structure**.

Also, add this project in the **Maven** tool window if it is not there.

## Build and Run

From IDEA's **Run / Debug Configurations**, set the **working directory** `HttpsDemoApplication.java`
to be `$MODULE_WORKING_DIR$` (or the absolute path of `https-demo`).
This ensures they are always launched from `https-demo`, not the root repository.
If they are launched from the root repository, they will fail to run because `https-demo/.env`
(needed by `applicaton.properties`)
cannot be resolved.

Adjusting **Run / Debug Configurations** may be needed
when switching between the root repository and `https-demo`.


# Important Environment Variables

In `https-demo/.env` (create one if it doesn't exist), set the following variables:
using the values used for the `keytool` command.
```
KEY_STORE=
STORE_PWD=
KEY_PWD=
KEY_ALIAS=
```


# Generate a Self-signed Certificate (`.jks`)

Run
```shell
# In https-demo/
$ . generate-cert.sh
```
By default, the `.jks` file is generated in the same directory as `generate-cert.sh`.

Or type and run the command below.
```shell
keytool -genkeypair \
  -alias <KEY_ALIAS> \
  -keyalg RSA \
  -keysize 2048 \
  -storetype JKS \
  -keystore <KEY_ALIAS> \
  -validity 365 \
  -storepass <STORE_PWD> \
  -keypass <KEY_PWD> \
  -dname "CN=localhost, OU=Dev, O=Example, L=City, S=State, C=US" \
  -ext "SAN=DNS:localhost,DNS:127.0.0.1"
```
Supply values for `<KEY_ALIAS>`, `<KEY_STORE>`, `<STORE_PWD>`, `<KEY_PWD>`.


# Export the Certificate

Run
```shell
# In https-demo/
$ . export-cert.sh
```

Or type and run the command below.
```shell
keytool -exportcert \
  -alias <KEY_ALIAS> \
  -keystore <KEY_ALIAS> \
  -rfc \
  -file <CERT_NAME>.crt \
  -storepass <STORE_PWD>
```

Supply `<CERT_NAME>` as well.

By default, the `.crt` file is generated in the same directory as `export-cert.sh`.


# Add the Certificate into Postman

In **Settings | Certificates**, turn on **CA certificates**,
and select the generated `.crt` file for the **PEM file**.