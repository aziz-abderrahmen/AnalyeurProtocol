package model.protocols;

import java.util.List;
import java.util.stream.Collectors;

/**
 * srcMAC (6 bytes) - destMAC (6 bytes) - type (2 bytes) - data (46 - 1500 bytes) - CRC (2 bytes)
 **/
public class Ethernet extends Protocol {
    private final String srcMac;
    private final String dstMac;
    private final String type;
    private final String crc;

    public Ethernet(List<String> data) {
        super(data, 14, 0);
        dstMac = data.subList(0, 6).stream()
                .map(s -> String.valueOf(s))
                .collect(Collectors.joining(":"));

        srcMac = data.subList(6, 12).stream()
                .map(s -> String.valueOf(s))
                .collect(Collectors.joining(":"));

        type = String.join("", data.subList(12, 14));

        crc = String.join("", data.subList(data.size() - footerLength, data.size()));
    }

    @Override
    public String toString() {
        return "Ethernet II, Src: " + srcMac + ", Dst: " + dstMac + " {\n"
                +"\tDestination: " + srcMac + "\n" 
                +"\tSource: " + dstMac + "\n"
                +"\tType: IPv4 (0x" + type + ")\n"
                +'}';
    }
  
}
