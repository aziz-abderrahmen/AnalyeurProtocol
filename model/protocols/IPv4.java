package model.protocols;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * version 4 bits - headerLength 4 bits - ToS 8 bits - TotalLength 16 bits
 * Identifier 16 bits - flagR 1 bit - flagDF 1 bit - flagMF 1 bit - Fragment offset 13 bits
 * TTL 8 bits - Protocol 8 bits - Header Checksum 16 bits
 * Source IP address 32 bits
 * Destination IP address 32 bits
 * Option + padding (0 - 40 bits d'options)
 * Data / Payload ? bits
 **/


public class IPv4 extends Protocol {
    private static final int version = 4;
    private int headerLength;
    private String typeOfService;
    private String totalLength;
    private String identifier;
    private String flags;
    private String flagR;
    private String flagDF;
    private String flagMF;
    private String fragmentOffset;
    private int TTL;
    private String protocol;
    private String checksum;
    private String srcIP;
    private String dstIP;
    private List<String> options;
    private int optionlength;
    private List<String> routeRecord = new ArrayList<>();
    private int pointer;
    
    
    //Pour avoir le pseudo ent�te pour TCP 
    
    private List<String> pseudoHeader;
    private String srcIPHex;
    private String dstIPHex;


    public IPv4(List<String> data) {
        super(data, 4 * Integer.parseInt(data.get(0).charAt(1)+"",16), 0);
        headerLength = Integer.parseInt(data.get(0).charAt(1)+"",16);
        typeOfService = data.get(1);
        totalLength = data.get(2) + data.get(3);
        identifier = data.get(4) + data.get(5);
        flags = data.get(6);
        initFlagsAndFragmentOffset();
        TTL = Integer.parseInt(data.get(8),16);
        protocol = data.get(9);
        checksum = data.get(10) + data.get(11);
        srcIP = data.subList(12, 16).stream()
                .map(s -> String.valueOf(Integer.parseInt(s, 16)))
                .collect(Collectors.joining("."));
        dstIP = data.subList(16, 20).stream()
                .map(s -> String.valueOf(Integer.parseInt(s, 16)))
                .collect(Collectors.joining("."));
        int taille = Integer.parseInt(data.get(0).charAt(1)+"",16) * 4;
        if(taille>20) initOptions(data.subList(20, taille));
        pseudoHeader = data.subList(12, 20);
    }

    private void initFlagsAndFragmentOffset() {
        String binaryValue = new BigInteger(data.get(6) + data.get(7), 16).toString(2);

        // Ajout des chiffres significatifs si besoin
        binaryValue = String.format("%16s", binaryValue).replace(" ", "0");

        flagR = binaryValue.charAt(0) + "";
        flagDF = binaryValue.charAt(1) + "";
        flagMF = binaryValue.charAt(2) + "";

        // Transforme le reste des binaires en hexa
        fragmentOffset = new BigInteger(binaryValue.substring(3), 2).toString(16);
    }

    private void initOptions(List<String> options){
    	switch(options.get(0)) {
    		case("00"):
    			break;
    		case("07"):
    			optionlength = Integer.parseInt(options.get(1),16);
    			pointer = Integer.parseInt(options.get(2),16);
    			//System.out.println("le pointer vaut" + pointer + "et optionslength vaut: " +optionlength);
    			for(int i = 3; i<=optionlength-4; i+=4) {
    				routeRecord.add(options.subList(i, i+4).stream()
    		                .map(s -> String.valueOf(Integer.parseInt(s, 16)))
    		                .collect(Collectors.joining(".")));
    			}
    			//System.out.println(" et routerecord size: "+routeRecord.size());
    	}
    }
    
    public String getSrcIPHex() {
    	return srcIPHex;
    }
    
    public String getDstIPHex() {
    	return dstIPHex;
    }
    
    public List<String> getPseudoHeader(){
    	return pseudoHeader;
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
        header.addAll(data.subList(0, 20));
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
    
    @Override
    public String toString() {
    	return "\nInternet Protocol Version 4, srcIP=" + srcIP  + ", dstIP=" + dstIP + " { \n" 
    				+ "\t0100 .... = Version: 4\n"
    				+ "\t.... "+ String.format("%4s",Integer.toBinaryString(headerLength)).replace(' ', '0') + " Header Length: "+ headerLength*4 + " bytes (" + headerLength + ")\n"
    				+ "\tType of service: 0x" + typeOfService +"\n"
    				+ "\tFlags : 0x" + flags + "\n "
    					+ toStringFlags()
    				+ "\tFragment Offset: "+ fragmentOffset + "\n"
    				+ "\tTime to Live:" + TTL + "\n"
    				+ "\tProtocol: TCP (" + protocol +")\n"
    				+ "\tHeader checksum: 0x" + checksum + checkChecksum() + "\n" 
    				+ "\tSource Address: " + srcIP  + "\n"
    				+ "\tDestination Address: " + dstIP + "\n" 
    				+ toStringRouteRecord()
                +'}';
    }
    
    private String toStringFlags() {
    	String not = "Not ";
    	String res = "\t\t" + flagR + "... .... = Reserved bit : ";
    	if(flagR.equals("0")) res += not;
    	res+= "set\n";
    	
    	res+= "\t\t" + "." + flagDF + ".. .... = Don't fragment : ";
    	if(flagDF.equals("0")) res += not;
    	res+= "set\n";
    	
    	res+= "\t\t"+ ".." + flagMF + ". .... = More fragment : ";
    	if(flagMF.equals("0")) res += not;
    	res+= "set\n";
    	
    	return res;
    }
    
    private String toStringRouteRecord() {
    	String res = "";
    	Boolean isNext = true;
		if(routeRecord.size()==0) return res;
		int length = 4 * Integer.parseInt(data.get(0).charAt(1)+"",16) - 20;
		res+= "\tOptions: (" + length + "bytes), Record Route\n";
		res+= "\t\tIP Option - Record Route (" + optionlength + "bytes)\n";
		res+= "\t\t\tType: 7\n"
			+ "\t\t\tLength: " + optionlength + "\n"
			+ "\t\t\tPointer: " + pointer +"\n";
		for(int i = 0; i < routeRecord.size(); i++){
			if(routeRecord.get(i).equals("0.0.0.0") && isNext) {
				res+="\t\t\tEmpty Route: 0.0.0.0 <- (next)\n";
				isNext = false;
			}
			else if(routeRecord.get(i).equals("0.0.0.0")){
				res+="\t\t\tEmpty Route: 0.0.0.0\n";
			}
			else res+= "\t\t\tRecorded Route: " + routeRecord.get(i) + "\n";
			
		}
    	return res+= "\t\tIP Option - End of Options List (EOL)\n"
				   + "\t\t\tType: 0\n";
    }
}
