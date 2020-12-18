
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.Layer;

public class Analyseur{
    // Lire le fichier hex

    public static BufferedReader setupFileStream(){
            File fileDir = null;
            BufferedReader in = null;


            JFileChooser file = new JFileChooser();
            file.setCurrentDirectory(new java.io.File("."));
            file.setFileSelectionMode(JFileChooser.FILES_ONLY);
            file.setDialogTitle("Choisir le fichier hex.");
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "FICHIER TXT", "txt");
                file.setFileFilter(filter);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                fileDir = file.getSelectedFile();

            } else {
                System.exit(0);
            }
            try {   
                in = new BufferedReader(new FileReader(fileDir));
            } catch (FileNotFoundException ex) {
                System.out.println("Fichier non trouvé");
                System.exit(0);
            }

            return in;
    }
    
    
    public static List<List<String>> readFile(BufferedReader br){
    	List<String> listhexa = new ArrayList<>();
    	int pos = 0;
    	List<List<String>> res = new ArrayList<>();
    	List<String> line = br.lines().collect(Collectors.toList());
    	for(int i = 0; i<line.size(); i++) {
    		List<String> listOfHex = Arrays.asList(line.get(i).split("\\s+"));
    		if(listOfHex.size()!= 0 && checkHex(listOfHex.get(0),0)) {
    			int offset = Integer.parseInt(listOfHex.get(0),16);
        		if(offset == 0 && listhexa.size()!=0) {
        			res.add(listhexa);
        			pos = 0;
        		}
        		if(offset == pos) {
        			for (int j = 1; j < listOfHex.size(); j++) {
        				if(checkHex(listOfHex.get(j),2)) {
        					listhexa.add(listOfHex.get(j));
        					pos++;
        				}
        			}
        		}
    		}
    		
    	}
    	res.add(listhexa);
    	return res;
    }
    
    private static boolean checkHex(String hexadecimal, int length) {
        if (hexadecimal.length() == length || length==0) {
            try {
                Integer.parseInt(hexadecimal, 16);
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
        return false;
    }
    
    public static void main(String[] args) {
    	List<List<String>> data = Analyseur.readFile(Analyseur.setupFileStream());
    	for(List<String> elem : data) {
    		for(String hexa : elem) {
    			System.out.print(hexa+" ");
    		}
    	}
    	Layer l = new Layer("Ethernet", data.get(0));
    	System.out.println(l);
        System.out.println("Rien");
    }
}
