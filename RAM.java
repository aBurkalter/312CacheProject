// File: RAM.java
// Author(s): Austin Burkhalter, Ivan Carrasco
// Date: 04/27/2020
// Section: 510 
// E-mail: falloutdays@tamu.edu, ivancarrasco@tamu.edu 
// Description:
// Represents the RAM for the project

import java.io.*;
import java.util.*;

public class RAM {
	private ArrayList<String> contents; //the simulated memory

	//initializers, the second one can probably be deleted
	public RAM() {
		contents = new ArrayList<String>();
	}
	public RAM(ArrayList<String> contents) {
		this.contents = contents;
	}

	//pushes lines to the back of the memory, used when initializing the memory
	public void write(String in) {
		contents.add(in);
    }
    
    public void fillRAM() {
        //opening up the input.txt file, replace later
		Scanner infile = null;
		try {
			infile = new Scanner(new FileReader("input.txt")); // args[0], as the file is inital command line input
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
			System.exit(0);
		}
		
		//copying the contents of input.txt to the RAM
		String line;
		while(infile.hasNextLine()) {
			line = infile.nextLine();
			this.write(line);
		}
    }
	
	//prints the contents of the memory and the corresponding line numbers
	public void printContents() {
		for (int i = 0; i < 256; i++) {
			System.out.println(i + "\t" + contents.get(i));
		}
	}
	
}