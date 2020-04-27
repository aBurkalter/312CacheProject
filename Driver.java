import java.io.*;
import java.util.*;

public class Driver {

	public static void main(String[] args) {
		RAM memory = new RAM(); //initializing the RAM using a custom class

		//opening up the input.txt file, replace later
		Scanner infile = null;
		try {
			infile = new Scanner(new FileReader("./src/input.txt")); // args[0], as the file is inital command line input
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
			System.exit(0);
		}
		
		//copying the contents of input.txt to the RAM
		String line;
		while(infile.hasNextLine()) {
			line = infile.nextLine();
			memory.write(line);
		}
		
		//printing the contents of the RAM
		memory.printContents();
	}

}
