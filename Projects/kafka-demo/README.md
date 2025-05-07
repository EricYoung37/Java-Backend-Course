# Essential IDEA Configurations

## Module Import
This mini project is a module of the root repository
(that contains the folders `Short Questions`, `Projects`, and `Coding`).

If no run button shows up in the application class, the module needs to be added manually
by doing **Import module from external model — Maven** in **Project Structure**.

Also, add this project in the **Maven** tool window if it is not there.

## Build and Run

From IDEA's **Run / Debug Configurations**, set the **working directory** for `KafkaDemoApplication.java` to be `$MODULE_WORKING_DIR$` (or the absolute path of `kafka-demo`).
This ensures they are always launched from `kafka-demo`, not the root repository.

Adjusting **Run / Debug Configurations** may be needed
when switching between the root repository and `kafka-demo`.


# Brokers

Build images and run containers:
```shell
# kafka-demo/
$ docker compose up
```

Stops containers and removes containers, networks, volumes, and images:
```shell
# kafka-demo/
$ docker compose down
```

## Kafka UI
```
localhost:8080
```

# Producers & Consumers

Run `kafka-demo/KafkaDemoApplication.java`.

## Send Messages

In Postman,
```http request
# 1. Publish to Topics Related to Message Delivery Guarantee
POST localhost:8088/publish?key=msgKey&message=msgContent


# 2. Test Message Delivery Guarantee

# Test At-Least-Once Guarantee
POST localhost:8088/test-at-least-once?key=msg2&message=MayDuplicateButNoLoss2

# Test At-Most-Once Guarantee
POST localhost:8088/test-at-most-once?key=msg11&message=MayMissButNoDuplicate11

# Test Exactly-Once Guarantee
POST localhost:8088/test-exactly-once?key=msg1&message=NoLossNoDuplicate


# 3. Test Custom Logic

# Modulo Partitioning for Numeric Key
POST localhost:8088/test-custom-partition?key=6&message=GoesToPartition0

# Hashing for Non-Numeric Key
POST localhost:8088/test-custom-partition?key=six&message=MayNotBePartition0
```
