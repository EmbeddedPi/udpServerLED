udpServerLED
======

A status indicator plugin for a Minecraft server. The minecraft server can be any machine running a java based Minecraft server such as Bukkit or Spigot. Status messages are sent from the server using the udpClientLED plugin that needs to be paired with this software.

This application displays the status of a Minecraft server on external LEDs using Raspberry Pi GPIO pins.

Server is online (GPIO18) => Red

LAN players present (GPIO23) => Yellow

Internet players present (GPIO24) => Green


Usage
=====
This repository is a Maven project based on source code built in Eclipse. If you just want to build it without delving into the code then 
just download the completed plugin .jar from the latest release.

TBC
[https://github.com/EmbeddedPi/udpServerLED/releases](https://github.com/EmbeddedPi/udpServerLED/releases)


Current status
==============
Fully working and tested on Glowstone and Spigot (for the minecraft server) and RaspberryPi (for this part)


Hardware
========
Schematic and physical hardware are the same as my previous all-in-one piLED project which can be seen on the project site.

[http://embeddedpi.github.io/piLED/](http://embeddedpi.github.io/piLED/)
