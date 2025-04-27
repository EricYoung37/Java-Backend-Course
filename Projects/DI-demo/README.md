# Essential IDEA Configurations

## Module Import
This mini project is a module of the root repository
(that contains the folders `Short Questions`, `Projects`, and `Coding`).

If no run button shows up in the application class, the module needs to be added manually
by doing **Import module from external model — Maven** in **Project Structure**.

Also, add this project in the **Maven** tool window if it is not there.

## Build and Run

From IDEA's **Run / Debug Configurations**, set the **working directory** for `DiDemoApplication.java` to be `$MODULE_WORKING_DIR$` (or the absolute path of `DI-demo`).
This ensures they are always launched from `DI-demo`, not the root repository.

Adjusting **Run / Debug Configurations** may be needed
when switching between the root repository and `DI-demo`.
