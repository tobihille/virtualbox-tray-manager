# Goal

Create an easy-to-use manager for all already existing VirtualBox VMs residing in taskbar notification area

#Requirements

VirtualBox >= 5.0 (I assume the api is backwards compatible)
vboxjxpcom.jar - you get it inside the VirtualBox SDK
* Download it from https://www.virtualbox.org/wiki/Downloads, unzip the file and copy it from sdk/bindings/xpcom/java/ to the applications jar directory

# How to Run

change to the application-directory
run "java -cp lib/vboxjxpcom.jar:VirtualBoxTrayManager.jar virtualboxtraymanager.VirtualBoxTrayManager"

# Setup

On first Startup you will be asked to enter all Settings, if they are not provided the application will not run correctly.
To claryfy things:
* Xpcom Service Executable refers on Linux to /usr/lib/virtualbox/VBoxSVC
* VirtualBox install directory refers to your Install directory, on Linux usually /usr/lib/virtualbox
* Library Path refers to the applications directory, the directory you placed the .jar-Files in
* Backup save directory is the directory where your VMs are exported to if you choose backup. Please be sure you have write permissions and enough diskspace available.
