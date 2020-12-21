package model.protocols;

import java.util.List;

public class HTTP extends Protocol {
    private String name;
    private String result;

    public HTTP(List<String> data, String name) {
        super(data, 0, 0);
        this.name = name;
	    String output = ""; 
	    for (int i = 0; i < data.size(); i++) {
	        output+= ((char) Integer.parseInt(data.get(i), 16));
	    }
	    result = output.toString();
	}

	public String toString(){
		return "\nHypertext Transfer Protocol {\n"+this.result + "}";
	}

    


}