package model.protocols;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class TCP extends Protocol {

	private int srcport;
	private int dstport;
	private int seqnum;
	private int acknum;
	private int headerlength;
	private String flags;
	private String reserved;
	private String urg;
	private String ack;
	private String psh;
	private String rst;
	private String syn;
	private String fin;
	private String window;
	private String checksum;
	private String urgpointer;
	private String options;
	private String data;
	
    public TCP(List<String> data) {
        super(data, 4 * Integer.parseInt(data.get(12).charAt(0)+""), 0); // A changer
        srcport = Integer.parseInt((data.get(0) + data.get(1)),16);
        dstport = Integer.parseInt((data.get(2) + data.get(3)),16);
        seqnum = Integer.parseInt((data.get(4) + data.get(5) + data.get(6) + data.get(7)),16);
        acknum = Integer.parseInt((data.get(8) + data.get(9) + data.get(10) + data.get(11)),16);
        headerlength = Integer.parseInt(data.get(12).charAt(0)+"");
        reserved = "0000 00";
    }
    
    private void initFlagsAndFragmentOffset() {
        String binaryValue = new BigInteger(data.get(6) + data.get(7), 16).toString(2);

        // Ajout des chiffres significatifs si besoin
        binaryValue = String.format("%16s", binaryValue).replace(" ", "0");

        reserved = binaryValue.charAt(0) + "";
        flagDF = binaryValue.charAt(1) + "";
        flagMF = binaryValue.charAt(2) + "";

        
    }
    
    public String toString() {
    	return "\nTransmission Control Protocol , Src Port: " + srcport  + ", Dst Port:" + dstport + " { \n" 
    				+ "\tSource Port: " + srcport  + "\n"
    				+ "\tDestination Port:" + dstport + "\n"
    				+ "\tSequence Number: " + seqnum + "\n"
    				+ "\t[Next Sequence Number: " + (seqnum+1) + "]\n"
    				+ "\tAcknowledgment Number: " + acknum + "\n"
    				+ "\t" + String.format("%4s", Integer.toBinaryString(headerlength)).replace(' ', '0') + " .... = Header Length : " + (headerlength*4) + " bytes (" + headerlength +")\n" 
    				+ "\tFlags: " + flags + "\n"
    				+ toStringFlags()
                +'}';
    }
    
    private String toStringFlags() {
    	String not = "Not ";
    	String res = "\t\t" + reserved + ".. .... = Reserved : ";
    	if(reserved.equals("0")) res += not;
    	res+= "set\n";
    	
    	res+= "\t\t" + ".... .." + urg + ". .... = Urgent : ";
    	if(urg.equals("0")) res += not;
    	res+= "set\n";
    	
    	res+= "\t\t"+ ".... ..." + ack + " .... = Acknowledgment : ";
    	if(ack.equals("0")) res += not;
    	res+= "set\n";
    	

    	res+= "\t\t"+ ".... .... " + psh + "... = Push : ";
    	if(psh.equals("0")) res += not;
    	res+= "set\n";
    	
    	res+= "\t\t"+ ".... .... ." + rst + ".. = Reset : ";
    	if(rst.equals("0")) res += not;
    	res+= "set\n";
    	
    	res+= "\t\t"+ ".... .... .." + syn + ". = Syn : ";
    	if(syn.equals("0")) res += not;
    	res+= "set\n";
    	
    	res+= "\t\t"+ ".... .... ..." + fin + " = Fin : ";
    	if(fin.equals("0")) res += not;
    	res+= "set\n";
    	
    	return res;
    }
    
}
