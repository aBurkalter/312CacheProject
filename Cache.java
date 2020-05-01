// File: Cache.java
// Author(s): Austin Burkhalter, Ivan Carrasco
// Date: 04/27/2020
// Section: 510 
// E-mail: falloutdays@tamu.edu, ivancarrasco@tamu.edu 
// Description:
// Represents a Cache, with all of the user-inputted fields.

//import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Cache {
	
    private int C; //cache size in bytes; must be between 8-256
    private int B; //number of bytes per block
    private int E; //number of lines per set; must be 1, 2. or 4
    private int S; //number of sets; equivalent to C / (B * E)
    
    private int t; //tag bits
    private int s; //set bits
    private int b; //block bits
    private final int m = 8; //number of bits in memory address
    
    private int replacementPolicy; //either 1 or 2; 1 = random replacement, 2 = least recently used
    private int writeHitPolicy;    //either 1 or 2; 1 = write-through, 2 = write-back
    private int writeMissPolicy;   //either 1 or 2; 1 = write-allocate, 2 = no-write-allocate
    private int cacheHits;
    private int cacheMisses;

    private Byte[][][] cache;
    
    private Scanner in = new Scanner(System.in);

    public Cache() throws InvalidInputException { //constructor which takes in and validates input
    	
    	cacheHits = 0;
    	cacheMisses = 0;
    	
        System.out.println("configure the cache:");

        System.out.print("cache size: ");
        C = in.nextInt();
        if (C < 8 || C > 256) {
        	throw new InvalidInputException("Cache size must be between 8-256 bytes.");
        }
             
        System.out.print("data block size: ");
        B = in.nextInt();

        System.out.print("associativity: ");
        E = in.nextInt();
        if (!(E == 1 || E == 2 || E == 4)) {
        	throw new InvalidInputException("Associativity must be 1, 2, or 4.");
        }
        
        System.out.print("replacement policy: ");
        replacementPolicy = in.nextInt();
        if (!(replacementPolicy == 1 || replacementPolicy == 2 || replacementPolicy == 3)) {
        	throw new InvalidInputException("The only valid inputs are 1, 2, or 3.");
        }
        
        System.out.print("write hit policy: ");
        writeHitPolicy = in.nextInt();
        if (!(writeHitPolicy == 1 || writeHitPolicy == 2)) {
        	throw new InvalidInputException("The only valid inputs are 1 or 2.");
        }
        
        System.out.print("write miss policy: ");
        writeMissPolicy = in.nextInt();
        if (!(writeMissPolicy == 1 || writeMissPolicy == 2)) {
        	throw new InvalidInputException("The only valid inputs are 1 or 2.");
        }
        
        S = C / (B * E);
        b = (int) (Math.log(B) / Math.log(2));
        s = (int) (Math.log(S) / Math.log(2));
        t = m - s - b;
//     int T = (int) Math.pow(2, t);
        
        cache = new Byte[S][E][B+3];
                
        //filling cache with zeros
        for (int i = 0; i < S; i++) {
        	for (int j = 0; j < E; j++) {
        		for (int k = 0; k < (B+3); k++) {
        			cache[i][j][k] = new Byte("00");
        		}
        		cache[i][j][0].setKey("0"); //initializing valid bits
        		cache[i][j][1].setKey("0"); //initializing dirty bits
        		cache[i][j][2].setKey("00"); //initializing tag bytes
        	}
        }
        
        System.out.print("cache successfully configured!\n");
    }

    public String getRP(int r) { //retrieves the string representation of the cache's replacement policy
    	if(r == 1) {
    		return "random replacement";
    	}else if(r == 2) {
    		return "least recently used";
    	}else {}
    	 	return "invalid replacement policy";
    }
    
    public String getHP(int h) { //retrieves the string representation of the cache's write-hit policy 
    	if(h == 1) {
    		return "write-through";
    	}else if(h == 2) {
    		return "write-back";
    	}else {}
    	 	return "invalid write-hit policy";
    }
    
    public String getMP(int m) { //retrieves the string representation of the cache's write-miss policy
    	if(m == 1) {
    		return "write-allocate";
    	}else if(m == 2) {
    		return "no write-allocate";
    	}else {}
    	 	return "invalid replacement policy";
    }
    
    public void printConfiguration() { //prints the cache's different parameters
    	System.out.println("Cache size: " + C);
    	System.out.println("Data block size: " + B);
    	System.out.println("Associativity: " + E);
    	System.out.println("Number of sets: " + S);
    	System.out.println("Replacement Policy: " + getRP(replacementPolicy));
    	System.out.println("Write Hit Politc: " + getHP(writeHitPolicy));
    	System.out.println("Write Miss Policy: " + getMP(writeMissPolicy));
    }
    
    public String hexKey(char in) { //used to convert from a hex character to it's binary value
    	if (in =='0') return "0000";
    	if (in =='1') return "0001";
    	if (in =='2') return "0010";
    	if (in =='3') return "0011";
    	if (in =='4') return "0100";
    	if (in =='5') return "0101";
    	if (in =='6') return "0110";
    	if (in =='7') return "0111";
    	if (in =='8') return "1000";
    	if (in =='9') return "1001";
    	if (in =='A') return "1010";
    	if (in =='B') return "1011";
    	if (in =='C') return "1100";
    	if (in =='D') return "1101";
    	if (in =='E') return "1110";
    	if (in =='F') return "1111";
    	else return "error in hexKey";
    }
    
    public String hexToBinary(String in) { //converts a byte in hex to an 8 bit address
    	String A = hexKey(in.charAt(2));
    	String B = hexKey(in.charAt(3));
    	return A + B;
    }
    
    public int binaryToDecimal(String in) { //converts a binary string of arbitrary length into its decimal value
    	int out = 0;
    	int temp = 0;
    	for (int i = 0; i < in.length(); i++) {
    		temp = Character.getNumericValue(in.charAt(i));
    		out += temp * Math.pow(2, in.length() - i - 1);
    	}
    	return out;
    }
    
    public String decimalToHex(int in) { //converts decimal values to their respective hex character value
    	if (in == 0) return "0";
    	if (in == 1) return "1";
    	if (in == 2) return "2";
    	if (in == 3) return "3";
    	if (in == 4) return "4";
    	if (in == 5) return "5";
    	if (in == 6) return "6";
    	if (in == 7) return "7";
    	if (in == 8) return "8";
    	if (in == 9) return "9";
    	if (in == 10) return "A";
    	if (in == 11) return "B";
    	if (in == 12) return "C";
    	if (in == 13) return "D";
    	if (in == 14) return "E";
    	if (in == 15) return "F";
    	else return "Error in decimalToHex";
    }
    
    public String binaryToHex(String in) { //converts a binary string of arbitrary length into its hex value
    	int L = in.length();
    	if (L > 4) {
    		return binaryToHex(in.substring(0, L-5)) + binaryToHex(in.substring(L - 4, L));
    	}
    	
    	int value = binaryToDecimal(in);
    	return decimalToHex(value);
    }
    
    public int randomReplace(Byte[][][] in, int set, int block) { //returns a random index between 0 and the number of lines in the set
    	int maxIndex = in[1].length;
    	Random r = new Random();
//    	return r.nextInt((max - min) + 1) + min;
    	return r.nextInt(maxIndex);
    }
    
    public int lruReplace(Byte[][][] in) {
    	return -1;
    }
    
    public int findLine(Byte[][][] in, int set, int block) { //finds the next eviction line
//    	System.out.println("Finding line");
//    	System.out.println("in[1].length = " + in[1].length);
    	for (int i = 0; i < in[1].length; i++) {
    		if ((in[set][i][2].getKey().equals("00")) && (in[set][i][0].getKey().equals("0"))) {
    			in[set][i][0].setKey("1");
//    			System.out.println("Found empty line at " + i);
    			return i;
    		}
    	}
    	if (replacementPolicy == 1) { //Random replacement
//    		System.out.println("Returning random line");
    		return randomReplace(in, set, block);
    	} else if (replacementPolicy == 2) { //Least Recently Used
//    		System.out.println("Returning least recently used line");
    		return lruReplace(in);
    	} else {
//    		System.out.println("Replacement Policy has illegal value.");
    		System.exit(0);
    	}
//    	System.out.println("Returning -1");
    	return -1;
    }
        
    public void cacheRead(String inHex, RAM memory) {
    	String inBinary = hexToBinary(inHex);
    	int inDecimal = binaryToDecimal(inBinary);
    	
    	String tagBinary = inBinary.substring(0, t);
    	String tagHex = binaryToHex(tagBinary);
//    	int tagDecimal = binaryToDecimal(tagBinary);
    	
    	String setBinary = inBinary.substring(t, t+s);
    	int setDecimal = binaryToDecimal(setBinary);
    	
    	String blockBinary = inBinary.substring(t+s);
    	int blockDecimal = binaryToDecimal(blockBinary);
    	int blockIndex = blockDecimal + 3;
    	
//    	int tagIndex = 0;
    	int tagIndex = findLine(cache,setDecimal,blockIndex);
    	for (int i = 0; i < cache[1].length; i++) {
    		if (cache[setDecimal][i][2].getKey().equals(tagHex)) {
    			tagIndex = i;
    		}
    	}
    	
//    	System.out.println("tagIndex = " + tagIndex);
    	
    	Byte value = cache[setDecimal][tagIndex][blockIndex];
//    	cache[setDecimal][tagIndex][2].setKey(tagHex);
    	String hitString = "no";
    	int eviction_line = 0; //replace depending on miss policy i think
    	String ram_address = inHex;
    	boolean hit = true;
//    	boolean hit;
    	
    	
    	if ((cache[setDecimal][tagIndex][2].getKey().equals("00")) && (cache[setDecimal][tagIndex][0].getKey().equals("0"))) {
    		hit = false;
//    		value.setKey(memory.get(inDecimal));
    		for (int i = 0; i < cache[1].length; i++) {
    			cache[setDecimal][tagIndex][i+3].setKey(memory.get(inDecimal + i));
    		}
    		cache[setDecimal][tagIndex][2].setKey(tagHex);
    		//find an eviction line
    	} else if (!(tagHex.equals(cache[setDecimal][tagIndex][2].getKey()))) {
    		hit = false;
    		eviction_line = tagIndex;
    		for (int i = 0; i < cache[1].length; i++) {
    			cache[setDecimal][tagIndex][i+3].setKey(memory.get(inDecimal + i));
    		}
    		cache[setDecimal][tagIndex][2].setKey(tagHex);
    	}
    	    	
    	if (hit) {
    		hitString = "yes";
    		eviction_line = -1;
    		ram_address = "-1";
    		cacheHits += 1;
    	} else {
//    		cache[setDecimal][tagIndex][2].setKey(tagHex);
    		cacheMisses += 1;
    	}
    	
    	System.out.println("set:" + setDecimal);
    	System.out.println("tag:" + tagHex);
    	System.out.println("hit:" + hitString);
    	System.out.println("eviction_line:" + eviction_line);
    	System.out.println("ram_address:" + ram_address);//
    	System.out.println("data:0x" + value.getKey() + "\n");
    }
    
    public void cacheWrite(String hexAddr, String data, RAM memory) {
		String binAddr = hexToBinary(hexAddr);
		int decAddr = binaryToDecimal(binAddr);
    	
		String binTag = binAddr.substring(0, t);
		String hexTag = binaryToHex(binTag);
		int decTag = binaryToDecimal(binTag);

		String binSet = binAddr.substring(t, t+s);
		int decSet = binaryToDecimal(binSet);

		String binBlock = binAddr.substring(t+s);
		int decBlock = binaryToDecimal(binBlock);
		int blockIndex = decBlock + 3;

		String writeHit = "no";
		int evictionLine = 0; //default value
		
		if (cache[decSet][decTag][0].getKey().equals("1")) { // if write-hit
			writeHit = "yes";
			// update cache regardless of whether write-through or write-back
			cache[decSet][decTag][blockIndex].setKey(data);
			if (writeHitPolicy == 1) { // write-through
				memory.set(decAddr, data);
			} else { // write-back
				cache[decSet][decTag][1] = new Byte("1");
			}
		} else { // write-miss
			if (writeMissPolicy == 1) { // write-allocate
				// fetch block from RAM and store in cache
				

			} else { // no-write-allocate
				cache[decSet][decTag][blockIndex] = new Byte(data); // just update RAM
			}
		}
		
		System.out.println("set:" + decSet);
		System.out.println("tag:" + hexTag);
		System.out.println("write_hit:" + writeHit);
		System.out.println("eviction_line:" + evictionLine);
		System.out.println("ram_address:" + hexAddr);
		System.out.println("data:" + data);
		System.out.println("dirty_bit:" + cache[decSet][decTag][1].getKey());

	}
    
    public void cacheFlush() { //clears the contents of the cache
    	for (int i = 0; i < cache.length; i++) {
        	for (int j = 0; j < cache[1].length; j++) {
        		for (int k = 0; k < cache[0][1].length; k++) {
        			cache[i][j][k].setKey("00");;
        		}
        		cache[i][j][0].setKey("0");
        		cache[i][j][1].setKey("0");
        		cache[i][j][2].setKey("0");
        	}
        }
    	System.out.print("cache_cleared\n");
    }
    
    public void cacheView() { //prints the contents of the cache
    	int lengthX = cache.length;
    	int lengthY = cache[1].length;
    	int lengthZ = cache[0][1].length;
    	
//    	System.out.println(lengthX + " " + lengthY + " " + lengthZ + "\n");
    	System.out.println("cache_size:" + C);
    	System.out.println("data_block_size:" + B);
    	System.out.println("associativity:" + E);
    	System.out.println("replacement_policy:" + getRP(replacementPolicy));
    	System.out.println("write_hit_policy:" + getHP(writeHitPolicy));
    	System.out.println("write_miss_policy:" + getMP(writeMissPolicy));
    	System.out.println("number_of_cache_hits:" + cacheHits);
    	System.out.println("number_of_cache_misses:" + cacheMisses);
    	System.out.println("cache_content:");
    	
    	for (int i = 0; i < lengthX; i++) {
    		for (int j = 0; j < lengthY; j++) {
    			for (int k = 0; k < lengthZ; k++) {
    				if (cache[i][j][k].getKey().equals("")) {
    					System.out.print("null\t");
    				} else {
    					System.out.print(cache[i][j][k].getKey() + "  ");
    				}
    			}
    			System.out.print("\n");
    		}
//    		System.out.print("\n");
    	}
//    	System.out.print("\n");
    }
    
    public void cacheDump() { //writes the contents of the cache to cache.txt
    	try {
			FileWriter cacheWrite = new FileWriter("cache.txt");
			for (int i = 0; i < cache.length; i++) {
	    		for (int j = 0; j < cache[1].length; j++) {
	    			for (int k = 3; k < cache[0][1].length; k++) {
	    				cacheWrite.write(cache[i][j][k].getKey() + " ");
	    				System.out.print(cache[i][j][k].getKey() + " ");
	    			}
	    			cacheWrite.write("\n");
	    			System.out.print("\n");
	    		}
	    	}
			cacheWrite.close();
		}
		catch(IOException e) {
			System.out.println("ram.txt not found");
			e.printStackTrace();
			System.exit(0);
		}
    }
    
}