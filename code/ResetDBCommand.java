package Main;

import java.io.File;

public class ResetDBCommand {



    public ResetDBCommand(String command) {
        parseCom();
    }

    private void parseCom() {
        //pas de parsing necessaire pour cette commande
    }

    public void execute() {
        clearDBFolder();
        BufferManager.getInstance().reset();
        DatabaseInfo.getInstance().reset();
    }

    private void clearDBFolder() {
        File dbFolder = new File("DB");
        if (dbFolder.exists()) {
            File[] files = dbFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        } else {
            dbFolder.mkdir();
        }
    }
}
