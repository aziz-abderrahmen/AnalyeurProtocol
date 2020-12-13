package models;

import java.util.List;

import models.protocols.Protocol;

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
			protocol = new Ethernet();
			sublayer = new Layer("ipv4", protocol.getPayload());
			break;
		case "ipv4":
			protocol = new Ipv4();
			sublayer = new Layer("TCP", protocol.getPayload());
		case "TCP":
			protocol = new TCP();
			sublayer = new Layer(getHTTPVersion(),protocol.getPayload());
			break;
		case "HTTP/1.0":
		case "HTTP/1.1":
			protocol = new HTTP();
			sublayer = null;
			break;
		}
		
	}
}
