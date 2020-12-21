package model;

import java.util.List;

import model.protocols.Ethernet;
import model.protocols.HTTP;
import model.protocols.IPv4;
import model.protocols.Protocol;
import model.protocols.TCP;

public class Layer {
	private String name;
	private List<String> data;
	private List<String> pseudoHeader;
	private Protocol protocol;
	private Layer sublayer;
	
	public Layer(String name, List<String> data, List<String> pseudoHeader) {
		this.name = name;
		this.data = data;
		if (pseudoHeader!=null) this.pseudoHeader = pseudoHeader;
		init();
	}
	
	public Layer(String name, List<String> data) {
		this.name = name;
		this.data = data;
		init();
	}
	
	private void init() {
		switch(name) {
		case "Ethernet":
			protocol = new Ethernet(data);
			sublayer = new Layer("IPv4", protocol.getPayload());
			break;
		case "IPv4":
			protocol = new IPv4(data);
			sublayer = new Layer("TCP", protocol.getPayload(),((IPv4)protocol).getPseudoHeader());
			break;
		case "TCP":
			protocol = new TCP(data, pseudoHeader);
			if(protocol.getPayload()!=null)
			sublayer = new Layer("HTTP",protocol.getPayload());
			break;
		case "HTTP":
			protocol = new HTTP(data, name);
			sublayer = null;
			break;
		}
		
	}

    public Protocol getProtocol() {
        return protocol;
    }
    
    public String toString() {
    	String res = "";
		if (protocol instanceof Ethernet)
			res += (((Ethernet)protocol).toString());
		
		if (protocol instanceof IPv4)
			res+=(((IPv4)protocol).toString());
			
		if (protocol instanceof TCP)
			res+=(((TCP)protocol).toString());
			
		if (protocol instanceof HTTP)
			res+=(((HTTP)protocol).toString());
		
    	if(sublayer!=null)
		return res+=(sublayer.toString()).toString();
    	else return res.toString();
    }

}
