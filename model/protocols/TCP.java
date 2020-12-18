package model.protocols;

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
        super(data, 20, 0); // A changer
        srcport = Integer.parseInt((data.get(0) + data.get(1)),16);
        dstport = Integer.parseInt((data.get(2) + data.get(3)),16);
        seqnum = Integer.parseInt(data.subList(4, 7).stream().collect(Collectors.joining()),16);

    }
    
    public String toString() {
    	return "\nTransmission Control Protocol , Src Port: " + srcport  + ", Dst Port:" + dstport + " { \n" 
    				+ "\tSource Port: " + srcport  + "\n"
    				+ "\tDestination Port:" + dstport + "\n"
    				+ "\tSequence Number: " + seqnum + "\n"
    				+ "\t[Next Sequence Number: " + (seqnum+1) + " ]\n"+
    			
                '}';
    }
}
