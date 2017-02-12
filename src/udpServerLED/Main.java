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

public static void main (String args[]) throws SocketException {		
	// Open file handles for GPIO unexport and export
	System.out.println("udpServer is initialising GPIO pins");
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
	catch (Exception e) {
		e.printStackTrace();
	}
	
	// Open socket for communication
	DatagramSocket serverSocket = new DatagramSocket(9876);
    byte[] sendData = new byte[32];
    String returnSentence;
    while(true){
    	try {
    		System.out.println("udpServer is preparing to receive packets");
    		byte[] receiveData = new byte[14];
    		System.out.println("Receiving packet at piLED, length =" + receiveData.length);
    		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    		serverSocket.receive(receivePacket);
            String sentence = new String( receivePacket.getData());
            sentence = sentence.replaceAll("[^\\p{Print}]", "");
            System.out.println("Received: " + sentence);
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            System.out.println("Got this from " + IPAddress + " @ port " + port);        
            boolean validInput = checkInput(sentence);
            System.out.println("Valid input is  " + validInput); 
            if (validInput){
            	if (sentence.equals("Oggy_Oggy_Oggy")) {
            		returnSentence = "Oi_Oi_Oi";
            	} else {
            		returnSentence = "Server got '" + sentence + "' Valid";
            	}
            } else {
            	returnSentence = "Server got '" + sentence + "' Invalid";
            }
			sendData = returnSentence.getBytes();
			System.out.println("Sending packet from piLED, length =" + sendData.length);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
    		}
        	catch (IOException exception) {
                 exception.printStackTrace();
                 serverSocket.close();
        	}
    	}
	}

	private static Boolean checkInput (String sentence) {
		System.out.println("checkInput received " + sentence + ".");
		switch (sentence) {
		
		case "Red_On": 	writeLED (gpioChannel[0], gpioOn); 
						return true;
						
		case "Red_Off": 	writeLED (gpioChannel[0], gpioOff); 
						return true;	
			
		case "Amber_On": 	writeLED (gpioChannel[1], gpioOn); 
						return true;	
			
		case "Amber_Off": 	writeLED (gpioChannel[1], gpioOff); 
						return true;
			
		case "Green_On": 	writeLED (gpioChannel[2], gpioOn); 
						return true;	
					
		case "Green_Off": 	writeLED (gpioChannel[2], gpioOff); 
						return true;
	
		case "All_On":		writeLED (gpioChannel[0], gpioOn);
							writeLED (gpioChannel[1], gpioOn);
							writeLED (gpioChannel[2], gpioOn);	
						return true;
						
		case "All_Off":		writeLED (gpioChannel[0], gpioOff);
							writeLED (gpioChannel[1], gpioOff);
							writeLED (gpioChannel[2], gpioOff);	
						return true;
		case "Oggy_Oggy_Oggy": 
						return true;			
		// TODO Look at an exception for this as causes timeout in udpClient				
		case "Funky_Disco": funkyDisco();
						return true;
		
		default:		return false ;
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
    
//Test code
    private static void funkyDisco() {
    	int pause = 150;
    	for (int i=0; i<5; i++) {
    		try {
    		writeLED (gpioChannel[0], gpioOn);
        	Thread.sleep(pause);
        	writeLED (gpioChannel[0], gpioOff);
        	writeLED (gpioChannel[1], gpioOn);
    		Thread.sleep(pause);
    		writeLED (gpioChannel[1], gpioOff);
    		writeLED (gpioChannel[2], gpioOn); 
    		Thread.sleep(pause);	
    		writeLED (gpioChannel[2], gpioOff); 
    		writeLED (gpioChannel[1], gpioOn);
    		Thread.sleep(pause);
    		writeLED (gpioChannel[1], gpioOff);
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}   			
    }
   
	// LED IO 
    private static void writeLED (int channel, String status) {
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