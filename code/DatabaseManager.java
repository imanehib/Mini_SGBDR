package Main;

import java.io.IOException;

public class DatabaseManager {

        // Instance unique de DatabaseManager
        private static DatabaseManager instance;

        // Méthode publique pour obtenir l'instance unique de DatabaseManager
        public static synchronized DatabaseManager getInstance() {
            if (instance == null) {
                instance = new DatabaseManager();
            }
            return instance;
        }

        public void init() {

            // Appel à la méthode init de DatabaseInfo
            DatabaseInfo.getInstance().Init();

            // Appel à la méthode init de BufferManager
            BufferManager.getInstance().Init();

            /*  Appel à la méthode init de DiskManager (si elle existe)
            if (DiskManager.getInstance() != null) {
                DiskManager.getInstance().Init();
            }*/
            
        }

        public void finish() throws IOException {
        
            DatabaseInfo.getInstance().Finish();
            BufferManager.getInstance().flushAll(); 
        }

        public void processCommand(String command) throws IOException {
        
            if (command.startsWith("CREATE TABLE")) {
                CreateTableCommand createTableCommand = new CreateTableCommand(command);
                createTableCommand.execute();
            } else if (command.equals("RESETDB")) {
                ResetDBCommand resetDBCommand = new ResetDBCommand(command);
                resetDBCommand.execute();
                System.out.println("Reset Reussi !!");
            } else if (command.startsWith("INSERT INTO")) {
                InsertCommand insertCommand = new InsertCommand(command);
                insertCommand.execute();
            } else {
                System.out.println("Commande non reconnue : " + command);
            }

            /* 
            switch (command.toLowerCase()) {

            case "CREATE TABLE":
                
            CreateTableCommand createTableCommand = new CreateTableCommand(command);
            createTableCommand.execute();
            
                break;
            case "RESETDB":
            ResetDBCommand resetDBCommand = new ResetDBCommand(command);
            resetDBCommand.execute();
                
                break;


            default:
                System.out.println("Commande non reconnue : " + command);
                break;
        } */
    }
}
