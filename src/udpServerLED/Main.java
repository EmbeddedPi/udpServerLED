package udpServerLED;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Main {
		
	private static final String gpioPath="/sys/class/gpio";
	private static final String exportPath= gpioPath + "/export";
	private static final String unexportPath= gpioPath + "/unexport";
	private static final String devicePath= gpioPath + "/gpio%d";
	private static final String directionPath= devicePath + "/direction";
	private static final String valuePath= devicePath + "/value";
	private static final String gpioOut = "out";
	private static final String gpioOn = "1";
	private static final String gpioOff = "0";
	private static final int[] gpioChannel = {18,23,24};

public Main () throws SocketException {		
	// Open file handles for GPIO unexport and export
	try {
		FileWriter unexportFile = new FileWriter(unexportPath);
		FileWriter exportFile = new FileWriter(exportPath);
				
		// Initialise GPIO settings
		for (Integer channel : gpioChannel) {
			File exportFileCheck = new File(getDevicePath(channel));
			if (exportFileCheck.exists()) {
				unexportFile.write(channel.toString());
				unexportFile.flush();	
			}
			// Set port for use
			exportFile.write(channel.toString());
			exportFile.flush();
			//Set direction file
			FileWriter directionFile = new FileWriter(getDirectionPath(channel));
			directionFile.write(gpioOut);
			directionFile.flush();
			directionFile.close();
		}
							
		unexportFile.close();
		exportFile.close();
	}
	catch (Exception exception) {
		exception.printStackTrace();
	}
	
	// Open socket for communication
	DatagramSocket serverSocket = new DatagramSocket(9876);
    byte[] receiveData = new byte[1024];
    byte[] sendData = new byte[1024];
    while(true){
    	try {
    		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String( receivePacket.getData());
            System.out.println("Received: " + sentence);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            System.out.println("Got this from " + IPAddress + " @ port " + port);
            String returnSentence = "Received " + sentence;
            sendData = returnSentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
            updateLED();
    		}
        	catch (IOException exception) {
                 exception.printStackTrace();
                 serverSocket.close();
        	}
    	}
	}

    // Variable setting for device path
    private static String getDevicePath(int pinNumber) {
 	   return String.format(devicePath, pinNumber);
    }
    
    // Variable setting for direction path
    private static String getDirectionPath(int pinNumber) {
 	   return String.format(directionPath, pinNumber);
    }

    // Variable setting for value path
    private static String getValuePath(int pinNumber) {
 	   return String.format(valuePath, pinNumber);
    }

    // Update player LED status
    private void updateLED() {
    		writeLED (gpioChannel[1], gpioOn);
    		writeLED (gpioChannel[1], gpioOff);
    		writeLED (gpioChannel[2], gpioOn); 		
    		writeLED (gpioChannel[2], gpioOff);    			
    }
    
    // LED IO 
    private void writeLED (int channel, String status) {
    	try {
     		FileWriter commandFile = new FileWriter(getValuePath(channel));
     		commandFile.write(status);
     		commandFile.flush();
     		commandFile.close();
        }
        catch (Exception exception) {
        	exception.printStackTrace();
        }
    }
}

