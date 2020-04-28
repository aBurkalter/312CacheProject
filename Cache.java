// File: Cache.java
// Author(s): Austin Burkhalter, Ivan Carrasco
// Date: 04/27/2020
// Section: 510 
// E-mail: falloutdays@tamu.edu, ivancarrasco@tamu.edu 
// Description:
// Represents a Cache, with all of the user-inputted fields.

import java.io.*;
import java.util.*;

public class Cache {
    // Fields don't need accessors as they are immutable after inital input.
    // Fields don't need getters as they are only accessed from within Cache class.
    private final int m = 8; // number of bits in memory address

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

        System.out.print("cache size: ");
        C = in.nextInt();
        //if (C < 8 || C > 256)
            // figure out how to throw exception
        
        System.out.print("data block size: ");
        B = in.nextInt();

        System.out.print("associativity: ");
        E = in.nextInt();
        //if (!(E == 1 || E == 2 || E == 4))
            // figure out how to throw exception
        
        System.out.print("replacement policy: ");
        replacementPolicy = in.nextInt();
        //if (!(replacementPolicy == 1 || replacementPolicy == 2 || replacementPolicy == 3))
            // figure out how to throw exception
        
        System.out.print("write hit policy: ");
        writeHitPolicy = in.nextInt();
        //if (!(writeHitPolicy == 1 || writeHitPolicy == 2))
            // figure out how to throw exception
        
        System.out.print("write miss policy: ");
        writeMissPolicy = in.nextInt();
        //if (!(writeMissPolicy == 1 || writeMissPolicy == 2))
            // figure out how to throw exception
        
        System.out.println("\ncache successfully configured!");
    }


    public static void main(String[] args) {
        System.out.println("Cache Tests");
    }
}