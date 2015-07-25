# Goal

Create an easy-to-use manager for all already existing VirtualBox VMs residing in taskbar notification area

#Requirements

Java >= 7 (1.7)

VirtualBox >= 5.0 (I assume the api is backwards compatible)

vboxjxpcom.jar - you get it inside the VirtualBox SDK
* Download it from https://www.virtualbox.org/wiki/Downloads, unzip the file and copy it from sdk/bindings/xpcom/java/ to the applications jar directory

cron4j.jar - you get it from http://www.sauronsoftware.it/projects/cron4j/
* unzip it and copy the cron4j.jar to the applications jar directory

# How to Run

change to the application-directory
run "java -cp vboxjxpcom.jar:cron4j.jar:VirtualBoxTrayManager.jar virtualboxtraymanager.VirtualBoxTrayManager"

# Setup

On first Startup you will be asked to enter all Settings, if they are not provided the application will not run correctly.
To claryfy things:
* Xpcom Service Executable refers on Linux to /usr/lib/virtualbox/VBoxSVC
* VirtualBox install directory refers to your Install directory, on Linux usually /usr/lib/virtualbox
* Library Path refers to the applications directory, the directory you placed the .jar-Files in
* Backup save directory is the directory where your VMs are exported to if you choose backup. Please be sure you have write permissions and enough diskspace available.

# Scheduler

Enter in the grid the minute, hour etc. where you want to execute the task, then enter the mode and finally the VM you want to execute the command with.
On "OK" or close your changes will be saved and immediately added to the scheduler.
Add a new row via "+" and delete the marked row via "-"

# Problems

If you get exceptions on startup or during control of vms regarding vboxjxpcom missing in java.library.path you need to install the official package of VirtualBox, I had this issue when installing the package from openSuse because they omitted some files for unkown reasons. Uninstall and install from Oracle fixed it for me.
