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

	//initializer
	public RAM() {
		contents = new ArrayList<String>();
	}

	//pushes lines to the back of the memory, used when initializing the memory
	public void write(String in) {
		contents.add(in);
    }
    
    public void fillRAM(String in) {
        //opening up the input.txt file
		Scanner infile = null;
		try {
			infile = new Scanner(new FileReader(in));
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

	public String get(int in) {
		return contents.get(in);
	}

	public void set(int addr, String data) {
		contents.set(addr, data);
	}
	
	public void memoryView() {
		System.out.println("memory_size:" + contents.size());
		System.out.println("memory_content:");
		System.out.println("Address:Data");
		for(int i = 0; i < contents.size(); i++) {
			if (i % 8 == 0) {
				System.out.print("0x" + Integer.toHexString(i) + ":" + contents.get(i) + " ");
			} else if ((i+1) % 8 == 0) {
				System.out.println(contents.get(i));
			} else {
				System.out.print(contents.get(i) + " ");
			}
		}

	}

	public void memoryDump(){
		try {
			FileWriter ramWrite = new FileWriter("ram.txt");
			for(int i = 0; i < contents.size(); i++) {
				ramWrite.write(contents.get(i) + "\n");
			}
			ramWrite.close();
		}
		catch(IOException e) {
			System.out.println("ram.txt not found");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
}