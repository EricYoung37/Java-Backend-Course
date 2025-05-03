# Essential IDEA Configurations

## Module Import
This mini project is a module of the root repository
(that contains the folders `Short Questions`, `Projects`, and `Coding`).

If no run button shows up in the application class, the module needs to be added manually
by doing **Import module from external model — Maven** in **Project Structure**.

Also, add this project in the **Maven** tool window if it is not there.

## Build and Run

From IDEA's **Run / Debug Configurations**, set the **working directory** for `RedbookApplication.java`
to be `$MODULE_WORKING_DIR$` (or the absolute path of `redbook`).
This ensures they are always launched from `redbook`, not the root repository.
If they are launched from the root repository, they will fail to run because `redbook/.env`
(needed by MySQL connection in `application.properties`)
cannot be resolved.

Adjusting **Run / Debug Configurations** may be needed
when switching between the root repository and `redbook`.


## MySQL Connection

In `redbook/.env` (create one if it doesn't exist), set the following variables.
```
MYSQL_USER=
MYSQL_PWD=
MYSQL_PORT=
MYSQL_DB=
```