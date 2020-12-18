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
	private Protocol protocol;
	private Layer sublayer;
	
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
			sublayer = new Layer("TCP", protocol.getPayload());
			break;
		case "TCP":
			protocol = new TCP(data);
			sublayer = new Layer(getHTTPVersion(),protocol.getPayload());
			break;
		case "HTTP/1.0":
		case "HTTP/1.1":
			protocol = new HTTP(data, name);
			sublayer = null;
			break;
		}
		
	}

	private String getHTTPVersion() {
        return "HTTP/1.0";
    }

    public Protocol getProtocol() {
        return protocol;
    }

    /*
    @Override
    public String toString() {
        int cpt = 0;
        String res = "";
        for (int i = 0; i < data.size(); i++) {
            res += data.get(i) + " ";
            cpt ++;
            if (cpt == 16) {
                res += "\n";
                cpt = 0;
            }
        }
        return res;
    }*/
    
    public String toString() {
    	StringBuilder res = new StringBuilder();
		if (protocol instanceof Ethernet)
			res.append(((Ethernet)protocol).toString());
		
		if (protocol instanceof IPv4)
			res.append(((IPv4)protocol).toString());
			
		if (protocol instanceof TCP)
			res.append(((TCP)protocol).toString());
			
		if (protocol instanceof HTTP)
			res.append(((HTTP)protocol).toString());
		
    	if(sublayer!=null)
		return res.append(sublayer.toString()).toString();
    	else return res.toString();
    }

}
