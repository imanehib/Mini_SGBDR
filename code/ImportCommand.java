package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ImportCommand {
    private String relationName; // Nom de la relation cible
    private String csvFileName; // Nom du fichier CSV source

    public ImportCommand(String command) {
        parseCommand(command);
    }

    /**
     * Parse la commande d'importation pour extraire le nom de la relation et le nom
     * du fichier CSV.
     *
     * @param command La commande d'importation.
     */
    private void parseCommand(String command) {
        String[] commandParts = command.split(" ");
        if (commandParts.length != 4 || !commandParts[0].equalsIgnoreCase("IMPORT")
                || !commandParts[2].equalsIgnoreCase("INTO")) {
            throw new IllegalArgumentException("Commande d'importation incorrecte.");
        }

        this.relationName = commandParts[1];
        this.csvFileName = commandParts[3];
    }

    public void execute() throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                Record record = createRecordWithValues(values);

                FileManager.getInstance().insertRecordIntoTable(record);
            }
        } catch (IOException e) {
            throw new IOException("Erreur lors de la lecture du fichier CSV.", e);
        }
    }

    private Record createRecordWithValues(String[] values) throws Exception {
        TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(relationName);
        if (tableInfo == null) {
            throw new IllegalArgumentException("Table non trouvée : " + relationName);
        }

        if (tableInfo.getNbrColumns() != values.length) {
            throw new IllegalArgumentException(
                    "Le nombre de valeurs fournies ne correspond pas au nombre de colonnes dans la table");
        }

        Record record = new Record(tableInfo);
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            ColInfo.ColType colType = tableInfo.getColumnInfo().get(i).getType();
            switch (colType) {
                case INT:
                    record.addValue(Integer.parseInt(value));
                    System.out.println("Added INT value: " + value);
                    break;
                case FLOAT:
                    record.addValue(Float.parseFloat(value));
                    System.out.println("Added FLOAT value: " + value);
                    break;
                case STRING:
                    record.addValue(value);
                    System.out.println("Added STRING value: " + value);
                    break;
                case VARSTRING:
                    record.addValue(value);
                    System.out.println("Added VARSTRING value: " + value);
                    break;
                default:
                    throw new IllegalArgumentException("Type de colonne non supporté : " + colType);
            }
        }

        return record;
    }
}
