import java.io.*;
import javax.swing.JFileChooser;

public Analyseur{
    // Lire le fichier hex

    public static BufferedReader setupFileStream(){
            File fileDir = null;
            BufferedReader in = null;


            JFileChooser file = new JFileChooser();
            file.setCurrentDirectory(new java.io.File("."));
            file.setFileSelectionMode(JFileChooser.FILES_ONLY);
            file.setDialogTitle("Choisir le fichier hex.");
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
    public static void main(String[] args) {
        System.out.println("Rien");
    }
}
