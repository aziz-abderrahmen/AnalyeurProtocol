package model.protocols;

import java.util.List;
import java.util.stream.Collectors;

/**
 * srcMAC (6 bytes) - destMAC (6 bytes) - type (2 bytes) - data (46 - 1500 bytes) - CRC (2 bytes)
 **/
public class Ethernet extends Protocol {
    private final String srcMac;
    private final String destMac;
    private final String type;
    private final String crc;

    public Ethernet(List<String> data) {
        super(data, 14, 0);
        srcMac = data.subList(0, 6).stream()
                .map(s -> String.valueOf(s))
                .collect(Collectors.joining(":"));

        destMac = data.subList(6, 12).stream()
                .map(s -> String.valueOf(Integer.parseInt(s, 16)))
                .collect(Collectors.joining(":"));

        type = String.join("", data.subList(12, 14));

        crc = String.join("", data.subList(data.size() - footerLength, data.size()));
    }

    @Override
    public String toString() {
        return "Ethernet{" +
                "srcMac='" + srcMac + '\'' +
                ", destMac='" + destMac + '\'' +
                ", type='0x" + type + '\'' + getType()+
                ", crc='0x" + crc + '\'' +
                '}';
    }
    
    public String getType() {
    	String res = "";
    	switch(type) {
    	case "0800":
    		res += " (Datagramme IP)";
    		break;
    	}
    	return res;
    }
}
