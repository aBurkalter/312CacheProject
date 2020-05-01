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

    private int C; // cache size in bytes; must be between 8-256
    private int B; // number of bytes per block
    private int E; // number of lines per set; must be 1, 2. or 4
    private int S; // number of sets; equivalent to C / (B * E)
    
    private int t; //tag bits
    private int s; //set bits
    private int b; //block bits
    private final int m = 8; // number of bits in memory address
    
    private int replacementPolicy; // either 1 or 2; 1 = random replacement, 2 = least recently used
    private int writeHitPolicy;    // either 1 or 2; 1 = write-through, 2 = write-back
    private int writeMissPolicy;   // either 1 or 2; 1 = write-allocate, 2 = no-write-allocate

    
//    private String[][][] cache; //[tag][set][2+block] (first two Strings in line are V and D bit
    private Byte[][][] cache;
    
    private Scanner in = new Scanner(System.in);

    // constructor which takes in and validates input
    public Cache() {
    	
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
        b = (int) (Math.log(B) / Math.log(2));
        s = (int) (Math.log(S) / Math.log(2));
        t = m - s - b;
//        int T = (int) Math.pow(2, t);
//        cache = new String[S][E][B+2];
        cache = new Byte[S][E][B+3];
        
        
        for (int i = 0; i < S; i++) {
        	for (int j = 0; j < E; j++) {
        		for (int k = 0; k < (B+3); k++) {
        			cache[i][j][k] = new Byte("00");
        		}
        		cache[i][j][0].setKey("0");
        		cache[i][j][1].setKey("0");
        		cache[i][j][2].setKey("0");
        	}
        }
        
        System.out.println("cache successfully configured!");
//        System.out.println("t bits: " + t + ", s bits: " + s + ", b bits: " + b);
        System.out.println("\n");
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
    
    public String hexKey(char in) {
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
    
    public String hexToBinary(String in) {
    	String A = hexKey(in.charAt(2));
    	String B = hexKey(in.charAt(3));
    	return A + B;
    }
    
    public int binaryToDecimal(String in) {
    	int out = 0;
    	int temp = 0;
    	for (int i = 0; i < in.length(); i++) {
    		temp = Character.getNumericValue(in.charAt(i));
    		out += temp * Math.pow(2, in.length() - i - 1);
    	}
    	return out;
    }
    
    public String decimalToHex(int in) {
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
    
    public String binaryToHex(String in) {
    	int L = in.length();
    	if (L > 4) {
    		return binaryToHex(in.substring(0, L-5)) + binaryToHex(in.substring(L - 4, L));
    	}
    	
    	int value = binaryToDecimal(in);
    	return decimalToHex(value);
    }
    
    public int randomReplace(Byte[][][] in, int set, int block) {
    	int maxIndex = in[1].length - 1;
    	Random r = new Random();
//    	return r.nextInt((max - min) + 1) + min;
    	return r.nextInt((maxIndex) + 1);
//    	return r.nextInt(maxIndex);
    }
    
    public int lruReplace(Byte[][][] in) {
    	return -1;
    }
    
    public int findLine(Byte[][][] in, int set, int block) {
    	System.out.println("Finding line");
    	System.out.println("in[1].length = " + in[1].length);
    	for (int i = 0; i < in[1].length; i++) {
    		if (in[set][i][2].getKey().equals("0")) {
//    			in[set][1][block].setKey("1");
    			System.out.println("Found empty line at " + i);
    			return i;
    		}
    	}
    	if (replacementPolicy == 1) { //Random replacement
    		System.out.println("Returning random line");
    		return randomReplace(in, set, block);
    	} else if (replacementPolicy == 2) { //Least Recently Used
    		System.out.println("Returning least recently used line");
    		return lruReplace(in);
    	} else {
    		System.out.println("Replacement Policy has illegal value.");
    		System.exit(0);
    	}
    	System.out.println("Returning -1");
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
    	
    	System.out.println("tagIndex = " + tagIndex);
    	
    	Byte value = cache[setDecimal][tagIndex][blockIndex];
//    	cache[setDecimal][tagIndex][2].setKey(tagHex);
    	String hitString = "no";
    	int eviction_line = 0; //replace depending on miss policy i think
    	String ram_address = inHex;
    	boolean hit = true;
//    	boolean hit;
    	
    	
    	if (cache[setDecimal][tagIndex][2].getKey().equals("0")) {
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
    	} else {
//    		cache[setDecimal][tagIndex][2].setKey(tagHex);
    	}
    	
    	System.out.println("set:" + setDecimal);
    	System.out.println("tag:" + tagHex);
    	System.out.println("hit:" + hitString);
    	System.out.println("eviction_line:" + eviction_line);
    	System.out.println("ram_address:" + ram_address);//
    	System.out.println("data:" + value.getKey() + "\n");
    }
    
    public void cacheWrite(String addr, String data) {}
    
    public void cacheFlush() {
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
    }
    
    public void cacheView() {
    	int lengthX = cache.length;
    	int lengthY = cache[1].length;
    	int lengthZ = cache[0][1].length;
    	
    	System.out.println(lengthX + " " + lengthY + " " + lengthZ + "\n");
    	
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
    		System.out.print("\n");
    	}
    	System.out.print("\n");
    }
    
    public void cacheDump() {}
    
}