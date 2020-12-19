package model.protocols;

import java.util.List;

public abstract class Protocol {
	protected final int headerLength; // In bytes
    protected final int footerLength; // In bytes
    private final List<String> payload; // charge utile
    protected final List<String> data;

    public Protocol(List<String> data, int headerLength, int footerLength) {
    	//System.out.println(data.size() + " headerlength = " + headerLength);
        this.data = data;
        this.headerLength = headerLength;
        this.footerLength = footerLength;
        this.payload = data.subList(headerLength, data.size() - footerLength);
    }

    public List<String> getPayload() {
        return payload;
    }
}


