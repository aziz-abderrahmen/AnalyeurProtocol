package model.protocols;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TCP extends Protocol {

	private int srcport;
	private int dstport;
	private long seqnum;
	private long acknum;
	private int headerlength;
	private String flags;
	private String reserved;
	private char urg;
	private char ack;
	private char psh;
	private char rst;
	private char syn;
	private char fin;
	private int window;
	private String checksum;
	private int urgpointer;
	private String options;
	private int optionslength = 0;
	private List<String> tcpData = new ArrayList<>();
	
	//Pour avoir le pseudo ent�te
	
	private List<String> pseudoHeader = new ArrayList<String>();
	private String srcIPHex;
    private String dstIPHex;
	
    public TCP(List<String> data, List<String> pseudoHeader) {
        super(data, 4 * Integer.parseInt(data.get(12).charAt(0)+""), 0); 
        srcport = Integer.parseInt((data.get(0) + data.get(1)),16);
        dstport = Integer.parseInt((data.get(2) + data.get(3)),16);
        seqnum = Long.parseLong((data.get(4) + data.get(5) + data.get(6) + data.get(7)),16);
        acknum = Long.parseLong((data.get(8) + data.get(9) + data.get(10) + data.get(11)),16);
        headerlength = Integer.parseInt(data.get(12).charAt(0)+"");
        reserved = "0000 00";
        initFlags(data);
        window = Integer.parseInt(data.get(14) + data.get(15),16);
        checksum = data.get(16) + data.get(17);
        urgpointer = Integer.parseInt((data.get(18) + data.get(19)),16);
        tcpData.addAll(data.subList(20 + optionslength, data.size()));
        initPseudoHeader(data, pseudoHeader);
    }
    
    private void initFlags(List<String> data) {
    	int tmp = Integer.parseInt((data.get(12).charAt(1) + data.get(13)),16);

        // Ajout des chiffres significatifs si besoin
        flags = String.format("%12s", Integer.toBinaryString(tmp)).replace(' ', '0');
        //System.out.println(flags);
        reserved = flags.substring(0,6);
        urg = flags.charAt(6);
        ack = flags.charAt(7);
        psh = flags.charAt(8);
        rst = flags.charAt(9);
        syn = flags.charAt(10);
        fin = flags.charAt(11);
        
        //On remet flags en hexadecimal
        flags = String.format("%3s",new BigInteger(flags, 2).toString(16)).replace(' ', '0');
    }
    
    private void initPseudoHeader(List<String> data, List<String> ps) {
    	this.pseudoHeader.addAll(ps);
    	pseudoHeader.add("00");
    	pseudoHeader.add("06"); //code hexadecimal de TCP
    	pseudoHeader.add(data.get(0));
    	pseudoHeader.add(data.get(1));
    	pseudoHeader.add(data.get(2));
    	pseudoHeader.add(data.get(3));
    	pseudoHeader.add(data.get(16));
    	pseudoHeader.add(data.get(17));
    	pseudoHeader.addAll(tcpData);
    	String length = String.valueOf(tcpData.size() + 8);
    	String l = String.format("%4s",new BigInteger(length).toString(16)).replace(' ', '0');
    	//System.out.println("La taille vaut:" + l.substring(0, 2));
    	pseudoHeader.add(l.substring(0, 2));
    	pseudoHeader.add(l.substring(2, 4));
    	pseudoHeader.add(l.substring(0, 2));
    	pseudoHeader.add(l.substring(2, 4));
    }
    
    private String checkChecksum() {
        /** Addition des hexas de l'entête
         * Si résultat supérieur à 16 bits :
         * On additionne les 16 bits "high" avec les 16 bits "low" (high et low comme en archi)
         * res = (somme & 0xFFFF) + (somme >> 16)
         * pas d'erreurs si res = 0xFFFF
         */
    	if (checksum.equals("0000")) return " [validation disabled]";
        List<String> header = new ArrayList<>();
        //On y ajoute le pseudo en-t�te
        header.addAll(pseudoHeader);
        
        // On crée la liste d'hexas de 2 octets
        List<String> hexs = new ArrayList<>();
        
        
        for(int i = 0; i < header.size(); i+=2) {
            hexs.add(header.get(i) + header.get(i + 1));
        }

        // On fait la somme
        int sum = 0;
        for (String hex : hexs) {
            sum += Integer.parseInt(hex, 16);
        }
        
        

        // On somme les bits de poids forts et faibles
        if (sum > 0xFFFF) {
            sum = (sum >> 16) + (sum & 0xFFFF) ;
        }
        if(sum == 0xFFFF) return " [Correct]";
        else return " [Incorrect]";
    }
    
    
    public String toString() {
    	return "\nTransmission Control Protocol , Src Port: " + srcport  + ", Dst Port:" + dstport + " { \n" 
    				+ "\tSource Port: " + srcport  + "\n"
    				+ "\tDestination Port:" + dstport + "\n"
    				+ "\tSequence Number: " + seqnum + "\n"
    				+ "\t[Next Sequence Number: " + (seqnum+1) + "]\n"
    				+ "\tAcknowledgment Number: " + acknum + "\n"
    				+ "\t" + String.format("%4s", Integer.toBinaryString(headerlength)).replace(' ', '0') + " .... = Header Length : " + (headerlength*4) + " bytes (" + headerlength +")\n" 
    				+ "\tFlags: 0x" + flags + "\n"
    				+ toStringFlags()
    				+ "\tWindow: " + window + "\n"
    				+ "\tChecksum: 0x" + checksum + checkChecksum() + "\n"
    				+ "\tUrgent pointer: " + urgpointer + "\n"
    				+ "\tTCP payload (" + getPayload().size() + " bytes)\n"
                +'}';
    }
    
    private String toStringFlags() {
    	String not = "Not ";
    	String res = "\t\t" + reserved + ".. .... = Reserved : ";
    	if(reserved.equals("0")) res += not;
    	res+= "set\n";
    	
    	res+= "\t\t" + ".... .." + urg + ". .... = Urgent : ";
    	if(urg == '0') res += not;
    	res+= "set\n";
    	
    	res+= "\t\t"+ ".... ..." + ack + " .... = Acknowledgment : ";
    	if(ack == '0') res += not;
    	res+= "set\n";
    	

    	res+= "\t\t"+ ".... .... " + psh + "... = Push : ";
    	if(psh == '0') res += not;
    	res+= "set\n";
    	
    	res+= "\t\t"+ ".... .... ." + rst + ".. = Reset : ";
    	if(rst == '0') res += not;
    	res+= "set\n";
    	
    	res+= "\t\t"+ ".... .... .." + syn + ". = Syn : ";
    	if(syn == '0') res += not;
    	res+= "set\n";
    	
    	res+= "\t\t"+ ".... .... ..." + fin + " = Fin : ";
    	if(fin == '0') res += not;
    	res+= "set\n";
    	
    	return res;
    }
    
}
