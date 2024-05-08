package Main;

import java.util.ArrayList;
import java.util.List;

public class InsertCommand {

    private String tableName;
    private List<String> values;

    public InsertCommand(String command) {
        parseCommand(command);
    }

    private void parseCommand(String command) {
        try {
            String[] cmd = command.split(" ");
            if (cmd.length < 5 || !cmd[1].equalsIgnoreCase("INTO") || !cmd[3].equalsIgnoreCase("VALUES")) {
                throw new IllegalArgumentException("Format incorrect pour la commande INSERT");
            }

            this.tableName = cmd[2];
            this.values = new ArrayList<>();

            // Trouver l'indice du début des valeurs
            int startIndex = command.indexOf("(");
            int endIndex = command.lastIndexOf(")");

            // Extraire les valeurs entre les indices startIndex et endIndex inclus
            String valuesPart = command.substring(startIndex + 1, endIndex);

            // Diviser les valeurs par des virgules en prenant en compte les espaces après
            // les virgules
            String[] valuesArray = valuesPart.split(",\\s*");

            for (String value : valuesArray) {
                // Ajouter les valeurs à la liste
                String trimmedValue = value.trim();
                // Gérer les valeurs entourées de guillemets simples ou doubles
                if ((trimmedValue.startsWith("'") && trimmedValue.endsWith("'")) ||
                        (trimmedValue.startsWith("\"") && trimmedValue.endsWith("\""))) {
                    // Retirer les guillemets et ajouter la valeur
                    trimmedValue = trimmedValue.substring(1, trimmedValue.length() - 1);
                }
                values.add(trimmedValue);
            }

        } catch (Exception e) {
            System.out.println("Erreur lors du parsing de la commande INSERT : " + e.getMessage());
        }
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getValues() {
        return values;
    }

    public void execute() {
        try {
            if (!DatabaseInfo.getInstance().tableExists(tableName)) {
                System.out.println("La table '" + tableName + "' n'existe pas.");
                return;
            }

            TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(tableName);
            List<ColInfo> colInfos = tableInfo.getColumnInfo();

            if (values.size() != colInfos.size()) {
                System.out.println(values.size());
                System.out.println(colInfos.size());

                throw new IllegalArgumentException("Le nombre de valeurs ne correspond pas au nombre de colonnes.");
            }

            List<ColInfo.ColType> colTypes = new ArrayList<>();

            for (int i = 0; i < colInfos.size(); i++) {
                colTypes.add(colInfos.get(i).getType());
            }

            List<Object> typedValues = new ArrayList<>();

            for (int i = 0; i < values.size(); i++) {
                Object typedValue = parseTypedValue(values.get(i), colTypes.get(i));
                typedValues.add(typedValue);
            }

            Record record = new Record(tableInfo);

            for (Object typedValue : typedValues) {
                record.addValue(String.valueOf(typedValue));
            }

            FileManager.getInstance().insertRecordIntoTable(record);

            System.out.println("Record inséré avec succès dans la table '" + tableName + "'.");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'exécution de la commande INSERT : " + e.getMessage());
        }
    }

    private Object parseTypedValue(String value, ColInfo.ColType colType) {
        // Implémentez la logique de conversion de la valeur en fonction du type de
        // colonne
        // Pour les types INT, FLOAT, STRING, VARSTRING, utilisez Integer.parseInt,
        // Float.parseFloat, etc.
        // Assurez-vous de gérer les exceptions en cas de conversion échouée.
        // Retournez la valeur convertie.

        // Exemple de conversion pour le type INT :
        if (colType == ColInfo.ColType.INT) {
            return Integer.parseInt(value);
        }
        // Ajoutez des cas pour les autres types.

        return null; // Retournez null si le type n'est pas géré (à adapter selon vos besoins).
    }

    /*
     * public static void main(String[] args) {
     * // Test de la commande INSERT
     * String command = "INSERT INTO TestTable VALUES (1, 'John', 42.5)";
     * InsertCommand insertCommand = new InsertCommand(command);
     * 
     * // Affichage des informations de la commande
     * System.out.println("Table Name: " + insertCommand.getTableName());
     * System.out.println("Values: " + insertCommand.getValues());
     * 
     * // Exécution de la commande
     * insertCommand.execute();
     * }
     */

}
