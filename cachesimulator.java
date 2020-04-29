//import java.io.FileNotFoundException;
//import java.io.FileReader;

// File: cachesimulator.java
// Author(s): Austin Burkhalter, Ivan Carrasco
// Date: 04/27/2020
// Section: 510 
// E-mail: falloutdays@tamu.edu, ivancarrasco@tamu.edu 
// Description:
// The driver of the application

//import java.io.*;
//import java.util.*;
import java.util.Scanner;

public class cachesimulator {

	public static void main(String[] args) {
		
        RAM memory = new RAM(); // creating the RAM using a custom class
        memory.fillRAM(args[0]);       // fill RAM with values from .txt file
        //memory.printContents(); // output contents of RAM
        Cache cache = new Cache(); // creating the cache using a custom class
        //cache.printConfiguration();
        Scanner in = new Scanner(System.in);
        
        boolean continueRunning = true;
    	String command = "";
    	String[] line;
    	while(continueRunning) {
    		System.out.println("*** Cache simulator menu ***\n" +
    			"type one command:\n" +
    			"1. cache-read\r\n" + 
    			"2. cache-write\r\n" + 
    			"3. cache-flush\r\n" + 
    			"4. cache-view\r\n" + 
    			"5. memory-view\r\n" + 
    			"6. cache-dump\r\n" + 
    			"7. memory-dump\r\n" + 
    			"8. quit\r\n" + 
    			"****************************\r\n"
    		);
    		
    		command = in.nextLine();
    		line = command.split(" ");
    		if ((line[0].equals("cache-read")) || (line[0].equals("1"))) {
    			System.out.println("reading cache\n");
    			cache.cacheRead(line[1]); //input should be 0x18 format
    		}
    		else if ((line[0].equals("cache-write")) || (line[0].equals("2"))) {
    			System.out.println("writing cache\n");
    			cache.cacheWrite(line[1], line[2]); //input should be 0x18 format
    		}
    		else if ((line[0].equals("cache-flush")) || (line[0].equals("3"))) {
    			System.out.println("flushing cache\n");
    			cache.cacheFlush(); //clear the cache
    		}
    		else if ((line[0].equals("cache-view")) || (line[0].equals("4"))) {
    			System.out.println("viewing cache\n");
    			cache.cacheView(); //displays cache content and status
    		}
    		else if ((line[0].equals("memory-view")) || (line[0].equals("5"))) {
    			System.out.println("viewing memory\n");
    			memory.memoryView(); //display RAM content and status
    		}
    		else if ((line[0].equals("cache-dump")) || (line[0].equals("6"))) {
    			System.out.println("dumping cache\n");
    			cache.cacheDump(); //dump current state of cache in a cache.txt file
    		}
    		else if ((line[0].equals("memory-dump")) || (line[0].equals("7"))) {
    			System.out.println("dumping memory\n");
    			memory.memoryDump(); //dump the RAM content in a ram.txt file
    		}
    		else if((line[0].equals("quit")) || (line[0].equals("8"))) {
    			System.out.println("Exiting program\n");
    			continueRunning = false;
    		}
    		else {
    			System.out.println("Invalid command, try again\n"); 
    		}
    	}
    	
    	in.close();
    	
	}

}