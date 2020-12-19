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


    public IPv4(List<String> data) {
        super(data, 4 * Integer.parseInt(data.get(0).charAt(1)+"",16), 0);
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

        initOptions();
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

    private void initOptions(){

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
    				+ "\tFlags : 0x" + flags + "\n "
    					+ toStringFlags()
    				+ "\tFragment Offset: "+ fragmentOffset + "\n"
    				+ "\tTime to Live:" + TTL + "\n"
    				+ "\tProtocol:" + protocol +"\n"
    				+ "\tHeader checksum: 0x" + checksum + checkChecksum() + "\n" 
    				+ "\tSource Address: " + srcIP  + "\n"
    				+ "\tDestination Address: " + dstIP + "\n" 
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
}
