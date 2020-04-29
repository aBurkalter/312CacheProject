// File: Cache.java
// Author(s): Austin Burkhalter, Ivan Carrasco
// Date: 04/27/2020
// Section: 510 
// E-mail: falloutdays@tamu.edu, ivancarrasco@tamu.edu 
// Description:
// Represents a Cache, with all of the user-inputted fields.

//import java.io.*;
import java.util.*;

public class Cache {
    // Fields don't need accessors as they are immutable after inital input.
    // Fields don't need getters as they are only accessed from within Cache class.
    //private final int m = 8; // number of bits in memory address

    private int C; // cache size in bytes; must be between 8-256
    private int B; // number of bytes per block
    private int E; // number of lines per set; must be 1, 2. or 4
    private int S; // number of sets; equivalent to C / (B * E)
    
    private int replacementPolicy; // either 1 or 2; 1 = random replacement, 2 = least recently used
    private int writeHitPolicy;    // either 1 or 2; 1 = write-through, 2 = write-back
    private int writeMissPolicy;   // either 1 or 2; 1 = write-allocate, 2 = no-write-allocate

    private Scanner in = new Scanner(System.in);

    // constructor which takes in and validates input
    Cache() {
        System.out.println("configure the cache:");

        System.out.print("cache size (between 8 and 256): ");
        C = in.nextInt();
        while (C < 8 || C > 256) {
        	System.out.println("try again");
        	C = in.nextInt();
        }
             
        System.out.print("data block size (in bytes): ");
        B = in.nextInt();

        System.out.print("associativity (1, 2, or 4): ");
        E = in.nextInt();
        while (!(E == 1 || E == 2 || E == 4)) {
        	System.out.println("try again");
        	E = in.nextInt();
        }
        
        System.out.print("replacement policy (RR=1, LRU=2): ");
        replacementPolicy = in.nextInt();
        while (!(replacementPolicy == 1 || replacementPolicy == 2 || replacementPolicy == 3)) {
        	System.out.println("try again");
        	replacementPolicy = in.nextInt();
        }
        
        System.out.print("write hit policy (through=1, back=2): ");
        writeHitPolicy = in.nextInt();
        while (!(writeHitPolicy == 1 || writeHitPolicy == 2)) {
        	System.out.println("try again");
        	writeHitPolicy = in.nextInt();
        }
        
        System.out.print("write miss policy (write=1 no write=2): ");
        writeMissPolicy = in.nextInt();
        while (!(writeMissPolicy == 1 || writeMissPolicy == 2)) {
        	System.out.println("try again");
        	writeMissPolicy = in.nextInt();
        }
        
        S = C / (B * E);
        
        System.out.println("cache successfully configured!\n");
    }

    public String getRP(int r) {
    	if(r == 1) {
    		return "random replacement";
    	}else if(r == 2) {
    		return "least recently used";
    	}else {}
    	 	return "invalid replacement policy";
    }
    
    public String getHP(int h) {
    	if(h == 1) {
    		return "write-through";
    	}else if(h == 2) {
    		return "write-back";
    	}else {}
    	 	return "invalid replacement policy";
    }
    
    public String getMP(int m) {
    	if(m == 1) {
    		return "write-allocate";
    	}else if(m == 2) {
    		return "no write-allocate";
    	}else {}
    	 	return "invalid replacement policy";
    }
    
    public void printConfiguration() {
    	System.out.println("Cache size: " + C);
    	System.out.println("Data block size: " + B);
    	System.out.println("Associativity: " + E);
    	System.out.println("Number of sets: " + S);
    	System.out.println("Replacement Policy: " + getRP(replacementPolicy));
    	System.out.println("Write Hit Politc: " + getHP(writeHitPolicy));
    	System.out.println("Write Miss Policy: " + getMP(writeMissPolicy));
    }
        
    public void cacheRead(String addr) {}
    public void cacheWrite(String addr, String data) {}
    public void cacheFlush() {}
    public void cacheView() {}
    public void cacheDump() {}
    
}