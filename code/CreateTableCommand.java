package Main;

import java.util.ArrayList;
import java.util.List;

public class CreateTableCommand {

    private String tableName;
    private int numColumns;
    private List<String> columnNames;
    private List<String> columnTypes;

    public CreateTableCommand(String command) {
        parseCommand(command);
    }

    private void parseColumnInfo(String colName, String colType) {
        int size = 0;
        if (colType.toUpperCase().startsWith("STRING") || colType.toUpperCase().startsWith("VARSTRING")) {
            // Extraire la taille de la colonne STRING ou VARSTRING
            int openParenIndex = colType.indexOf("(");
            int closeParenIndex = colType.indexOf(")");

            if (openParenIndex != -1 && closeParenIndex != -1 && closeParenIndex > openParenIndex) {
                String sizeStr = colType.substring(openParenIndex + 1, closeParenIndex);
                try {
                    size = Integer.parseInt(sizeStr);
                    System.out.println("La taille de la colonne : " + sizeStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Taille incorrecte pour la colonne " + colType + ": " + sizeStr);
                }
            } else {
                throw new IllegalArgumentException("Taille manquante ou incorrecte pour la colonne " + colType);
            }
        }

        ColInfo colInfo;
        if (colType.toUpperCase().startsWith("STRING") || colType.toUpperCase().startsWith("VARSTRING")) {
            colInfo = new ColInfo(colName, ColInfo.ColType.STRING, size);
        } else {
            colInfo = new ColInfo(colName, ColInfo.ColType.valueOf(colType.toUpperCase()));
        }

        columnNames.add(colName);
        columnTypes.add(colType);
    }

    private void parseCommand(String command) {
        try {
            String[] cmd = command.split(" ");
            if (cmd.length < 4) {
                throw new IllegalArgumentException("Commande CREATE TABLE incomplète");
            }
            this.tableName = cmd[2];
            this.columnNames = new ArrayList<>();
            this.columnTypes = new ArrayList<>();

            // Trouver l'indice du début des colonnes
            int startIndex = command.indexOf("(");
            int endIndex = command.lastIndexOf(")");

            // Extraire les colonnes entre les indices startIndex et endIndex inclus
            String columnsPart = command.substring(startIndex + 1, endIndex);

            // Diviser les colonnes par des virgules
            String[] columns = columnsPart.split(",");

            for (String column : columns) {
                String[] colInfo = column.split(":");
                if (colInfo.length != 2) {
                    throw new IllegalArgumentException("Format incorrect pour la définition des colonnes");
                }

                String colName = colInfo[0].trim();
                System.out.println("Nom de la colonne : " + colName);
                String colType = colInfo[1].trim();
                System.out.println("Type de la colonne : " + colType);

                parseColumnInfo(colName, colType);
            }

            this.numColumns = columnNames.size();

        } catch (Exception e) {
            System.out.println("Erreur lors du parsing de la commande : " + e.getMessage());
        }
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<String> getColumnTypes() {
        return columnTypes;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void execute() {
        try {

            if (DatabaseInfo.getInstance().tableExists(getTableName())) {
                System.out.println("La table '" + getTableName() + "' existe déjà.");
                return;
            }
            // Appel de createNewHeaderPage du FileManager
            PageId pageId = FileManager.getInstance().createNewHeaderPage();

            // Création de TableInfo en utilisant les données « communes »
            List<ColInfo> colInfos = new ArrayList<>();

            if (getColumnNames() != null && getColumnTypes() != null) {
                for (int i = 0; i < getNumColumns(); i++) {
                    String colName = getColumnNames().get(i);
                    String colType = getColumnTypes().get(i);

                    parseColumnInfo(colName, colType);
                }
            } else {
                throw new IllegalArgumentException("Les noms de colonnes ou les types de colonnes sont null.");
            }

            TableInfo tableInfo = new TableInfo(getTableName(), colInfos, pageId);

            // Rajouter la TableInfo au DatabaseInfo avec la méthode qui convient
            DatabaseInfo.getInstance().AddTableInfo(tableInfo);

            System.out.println("La table " + getTableName() + " a été créée avec succès!");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'exécution de la commande : " + e.getMessage());
        }
    }
}
