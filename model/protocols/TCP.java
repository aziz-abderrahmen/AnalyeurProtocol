package model.protocols;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class TCP extends Protocol {

	private int srcport;
	private int dstport;
	private int seqnum;
	private String acknum;
	private String dataoffset;
	private String reserved;
	private char urg;
	private char ack;
	private char psh;
	private char rst;
	private char syn;
	private char fin;
	private String window;
	private String checksum;
	private String urgpointer;
	private String options;
	private String data;
	
    public TCP(List<String> data) {
        super(data, 4 * Integer.parseInt(data.get(13).charAt(0)+""), 0); // A changer
        srcport = Integer.parseInt((data.get(0) + data.get(1)),16);
        dstport = Integer.parseInt((data.get(2) + data.get(3)),16);
        seqnum = Integer.parseInt((data.get(4) + data.get(5) + data.get(6) + data.get(7)),16);
    }
    
   /* private void initFlagsAndFragmentOffset() {
        String binaryValue = new BigInteger(data.get(6) + data.get(7), 16).toString(2);

        // Ajout des chiffres significatifs si besoin
        binaryValue = String.format("%16s", binaryValue).replace(" ", "0");

        flagR = binaryValue.charAt(0) + "";
        flagDF = binaryValue.charAt(1) + "";
        flagMF = binaryValue.charAt(2) + "";

        // Transforme le reste des binaires en hexa
        fragmentOffset = new BigInteger(binaryValue.substring(3), 2).toString(16);
    }*/
    
    public String toString() {
    	return "\nTransmission Control Protocol , Src Port: " + srcport  + ", Dst Port:" + dstport + " { \n" 
    				+ "\tSource Port: " + srcport  + "\n"
    				+ "\tDestination Port:" + dstport + "\n"
    				+ "\tSequence Number: " + seqnum + "\n"
    				+ "\t[Next Sequence Number: " + (seqnum+1) + "]\n"+
    				
    			
                '}';
    }
}
