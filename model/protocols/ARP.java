package model.protocols;

import java.util.List;
import java.util.stream.Collectors;

public class ARP extends Protocol{

	private String hardwaretype;
	private String protocoltype;
	private String hardwaresize;
	private String protocolsize;
	private String opcode;
	private String srcMAC;
	private String dstMAC;
	private String srcIP;
	private String dstIP;
	
	public ARP(List<String> data, int headerLength, int footerLength) {
		super(data, 28 , 0);
		hardwaretype = data.get(0) + data.get(1);
		protocoltype = data.get(2) + data.get(3);
		hardwaresize = data.get(4);
		protocolsize = data.get(5);
		opcode = data.get(6) + data.get(7);
		srcMAC = data.subList(8, 13).stream()
                .map(s -> String.valueOf(s))
                .collect(Collectors.joining(":"));
		srcIP = data.subList(14, 17).stream()
                .map(s -> String.valueOf(Integer.parseInt(s, 16)))
                .collect(Collectors.joining("."));
		dstMAC = data.subList(18, 23).stream()
                .map(s -> String.valueOf(s))
                .collect(Collectors.joining(":"));
		srcIP = data.subList(24, 27).stream()
                .map(s -> String.valueOf(Integer.parseInt(s, 16)))
                .collect(Collectors.joining("."));
	}

	
	public String toString() {
    	return "Internet Protocol Version 4, srcIP='" + srcIP + '\'' + ", dstIP='" + dstIP + "' { \n\t" 
    			+ "Flags : " + flags + "\n \t \t" + flagR + " = Reserved bit : Not set \n" + 
    			
                '}';
    }
}
