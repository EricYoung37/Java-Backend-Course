# Essential IDEA Configuration

## Module Import
This mini project is a module of the root repository
(that has the folders `Short Questions`, `Projects`, and `Coding`).

If no run button shows up in the application class, the module needs to be added manually
by doing **Import module from external model — Maven** in **Project Structure**.

Also, add this project in the **Maven** tool window if it is not there.

## Build and Run

From IDEA's **Run / Debug Configurations**, set the **working directory** for `MongoPing.java`
and `MongoBlogApplication.java` to be `$MODULE_WORKING_DIR$`.
This ensures they are always launched from `mong-blog`, not `hw-repo`.
If they are launched from `hw-repo`, they will fail to run because `.env`
(needed by `MongoPing.java` and `applicaton.properties`)
cannot be resolved.


# MongoDB Driver

Version number for the dependency `mongodb-driver-sync` is omitted on purpose,
because the version (e.g., `5.3.0`) stated in the [MongoDB tutorial](https://www.mongodb.com/docs/drivers/java/sync/current/getting-started/#add-mongodb-as-a-dependency)
may not be compatible and result in build failure.

For a quick connection test, run `MongoPing.java`.
