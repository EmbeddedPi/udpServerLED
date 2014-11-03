piBell
======
A status indicator plugin for a RaspberryPi Minecraft server

This project is named piBell as it was originally intended to act as a virtual doorbell for a RaspberryPi powered Minecraft server by playing sounds through an external speaker when players join or leave the server. The focus has changed somewhat to have LEDs showing the follow status items instead.

Server is online (GPIO18) 
LAN players present (GPIO23) 
Internet players present (GPIO24)
Sound may still be implemented later but for now I can't think of a suitable new name to change it to.

Current status

Detection of player type code implemented
LED control code implemented 
Stripboard prototype working

Future enhancements

Include schematic for breadboard (although it is just 330Ohm resistors and LEDs to each GPIO pin)
A scrolling player list displayed on an external LCD

