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
	
	//prints the contents of the memory and the corresponding line numbers
	public void printContents() {
		for (int i = 0; i < 256; i++) {
			System.out.println(i + "\t" + contents.get(i));
		}
	}
	
}
