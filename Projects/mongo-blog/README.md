# Essential IDEA Configurations

## Module Import
This mini project is a module of the root repository
(that contains the folders `Short Questions`, `Projects`, and `Coding`).

If no run button shows up in the application class, the module needs to be added manually
by doing **Import module from external model — Maven** in **Project Structure**.

Also, add this project in the **Maven** tool window if it is not there.

## Build and Run

From IDEA's **Run / Debug Configurations**, set the **working directory** for `MongoPing.java`
and `MongoBlogApplication.java` to be `$MODULE_WORKING_DIR$` (or the absolute path of `mongo-blog`).
This ensures they are always launched from `mong-blog`, not the root repository.
If they are launched from the root repository, they will fail to run because `mongo-blog/.env`
(needed by `MongoPing.java` and `applicaton.properties`)
cannot be resolved.

Adjusting **Run / Debug Configurations** may be needed
when switching between the root repository and `mong-blog`.

# MongoDB Atlas Connection

In `mongo-blog/.env` (create one if it doesn't exist), set the following according to MongoDB Atlas.
```
MONGO_USER=
MONGO_PWD=
MONGO_CLUSTER=
MONGO_DBNAME=
```

# Quick Connection Test

Version number for the dependency `mongodb-driver-sync` is omitted on purpose,
because the version (e.g., `5.3.0`) stated in the [MongoDB tutorial](https://www.mongodb.com/docs/drivers/java/sync/current/getting-started/#add-mongodb-as-a-dependency)
may not be compatible and result in build failure.

For a quick connection test, run `MongoPing.java`.
