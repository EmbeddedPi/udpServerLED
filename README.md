piLED
=====
A status indicator plugin for a RaspberryPi Minecraft server

This project is intended to act as a virtual doorbell for a RaspberryPi powered Minecraft server by showing status LEDs to indicate whether the server is running or players are logged in.

Server is online (GPIO18, red) 
LAN players present (GPIO23, yellow) 
Internet players present (GPIO24, green)


Current status

Detection of player type code implemented
LED control code implemented 
Stripboard prototype working

Future enhancements

Include schematic for breadboard (although it is just 330Ohm resistors and LEDs to each GPIO pin)
Sound may still be implemented later.
