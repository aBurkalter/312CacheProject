// File: cachesimulator.java
// Author(s): Austin Burkhalter, Ivan Carrasco
// Date: 04/27/2020
// Section: 510 
// E-mail: falloutdays@tamu.edu, ivancarrasco@tamu.edu 
// Description:
// The driver of the application

import java.io.*;
import java.util.*;

public class cachesimulator {

	public static void main(String[] args) {
        RAM memory = new RAM(); // creating the RAM using a custom class
        Cache cache = new Cache(); // creating the cache using a custom class
        memory.fillRAM();       // fill RAM with values from .txt file
        //memory.printContents(); // output contents of RAM
	}

}