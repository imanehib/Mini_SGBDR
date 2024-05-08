package Main;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        //args[0] pourrait être null ou que l'index 0 n'est pas valide si args est vide.
        if (args.length > 0 && args[0] != null) {
            DBParams.DBPath = args[0];
        } else {
            // Définissez une valeur par défaut ou traitez l'absence de chemin de base de données
            //System.err.println("Avertissement : Aucun chemin de base de données fourni. Utilisation d'un chemin par défaut.");
            DBParams.DBPath = "C:\\Users\\NIS\\Documents\\projet-bdda-2023-main\\DB\\";
        }

        DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4;
        DBParams.frameCount = 2;


        
        //TP6:
        // Appel de la méthode Init du DatabaseManager
        DatabaseManager.getInstance().init();

        // Boucle de gestion des commandes
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Demander à l'utilisateur d'entrer une commande
            System.out.print("Entrez une commande (EXIT pour quitter) : ");
            String command = scanner.nextLine();

            // Vérifier si la commande est EXIT
            if (command.equalsIgnoreCase("EXIT")) {
                // Appel de Finish sur le DatabaseManager
                DatabaseManager.getInstance().finish();
                
                // Sortie de la boucle
                break;
            } else {
                // Passer la commande au DatabaseManager via ProcessCommand
                DatabaseManager.getInstance().processCommand(command);
            }
        }

        // Fermer le scanner (important pour éviter des fuites de ressources)
        scanner.close();
    }

}
