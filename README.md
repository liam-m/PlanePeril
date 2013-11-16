bear_traffic_controller
=======================

Team FLR's Contribution to the University of York 2nd Year Software Engineering Project.

Wiki
----
FRL's SEPR Wiki page can be found [here](http://www-module.cs.york.ac.uk/sepr/wiki/index.php?title=FLR SEPR Wiki). Permissions are required.

Building
--------
To build a .jar (or .exe, .app or .sh) for the project, I recommend [JarSplice](http://ninjacave.com/jarsplice).
To use it, download, extract if necessary, and run JarSplice, having exported the project as a normal .jar in eclipse (or other IDE).
Under 'Add Jars', add the exported .jar, and all of the .jars in bear_traffic_controller/external.
Under 'Add Natives', select all of the files in the relevant folder for the OS you're using. This folder will be in bear_traffic_controller/external/natives.
For 'Main Class', type btc.Main and don't bother with any of the VM arguments.
