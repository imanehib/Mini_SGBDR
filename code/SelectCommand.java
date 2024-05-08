package Main;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import java.util.stream.Collectors;

public class SelectCommand {
    private String tableName;
    private FileManager LefileManager;
    private ArrayList<SelectCondition> conditions;
    private String[] commande;

    public SelectCommand(String saisie) {
        conditions = new ArrayList<>();
        parse(saisie);
    }
    // Méthode pour analyser la commande

    private void parse(String c) {
        commande = c.split("WHERE");
        String[] tmp = commande[0].split(" ");
        this.tableName = tmp[3];
        if (commande.length > 1) {
            parseConditions(commande[1].trim());
        }
    }

    // Méthode pour analyser les conditions de la commande
    private void parseConditions(String conditionsStr) {
        String[] conditionsSplit = conditionsStr.split(" AND ");
        if (conditionsSplit.length > 20) { // Vérifier si la longueur dépasse 20
            System.out.println("Erreur! Votre commande contient plus de 20 critères.");
            System.exit(1); // Quitter le programme avec un code d'erreur
        } else {
            for (String conditionStr : conditionsSplit) {
                conditions.add(parseCondition(conditionStr.trim()));
            }
        }
    }

    // Méthode pour analyser une condition individuelle
    private SelectCondition parseCondition(String conditionStr) {
        String[] parts = conditionStr.split("=|<|>|<=|>=|<>");
        String columnName = parts[0].trim();
        String value = parts[1].trim();
        String operator = conditionStr.substring(columnName.length(), conditionStr.length() - value.length()).trim();

        return new SelectCondition(columnName, operator, value);
    }

    // Méthode pour exécuter la commande SELECT
    public void execute() throws IOException {
        TableInfo tableInfo = DatabaseInfo.getInstance().GetTableInfo(tableName);

        if (tableInfo == null) {
            System.out.println("Table \"" + tableName + "\" n'existe pas.");
            return;
        }

        displayTableInfo(tableInfo);

        System.out.println("Chargement des records de \"" + tableName + "\"...");
        List<Record> records = LefileManager.getAllRecords(tableInfo);

        System.out.println("*************Etape de verification des records***************");
        VerificationRecords(records);

        System.out.println("*************Affichage des records***************");
        afficherRecords(records);

        System.out.println("Total records = " + records.size());
    }

    // Méthode pour afficher les informations de la table
    private void displayTableInfo(TableInfo tableInfo) {
        System.out.println("*************Information Table:*****************");
        System.out.println("Nom de la Table : " + tableInfo.getTableName());
        System.out.println("Nombres de Colonnes: " + tableInfo.getNbrColumns());

        List<ColInfo> columnInfoList = tableInfo.getColumnInfo();
        System.out.println("***************Information Colonnes:****************");
        for (ColInfo colInfo : columnInfoList) {
            System.out.println(" Nom de la Colonne : " + colInfo.getName());
            System.out.println(" Type de la Colonne: " + colInfo.getType());
            // Ajoutez d'autres informations sur la colonne au besoin
        }

        System.out.println("Header Page ID: " + tableInfo.getHeaderPageId());
    }

    // Méthode pour filtrer les enregistrements en fonction des conditions
    private void VerificationRecords(List<Record> records) {
        records = records.stream().filter(record -> {
            System.out.println("Verification du record: " + record);
            boolean satisfies = satisfiesConditions(record);
            System.out.println("Satisfait les conditions ? " + satisfies);
            return satisfies;
        }).collect(Collectors.toList());
    }
    // Méthode pour afficher les enregistrements

    private void afficherRecords(List<Record> records) {
        for (Record record : records) {
            List<String> values = record.getRecValues();
            for (String value : values) {
                System.out.print(value + " ; ");
            }
        }
    }

    // Méthode pour obtenir l'index de la colonne
    private int getIndexOfColumn(String columnName, Record record) {
        List<String> columnNames = record.getTableInfo().getColumnInfo().stream()
                .map(ColInfo::getName)
                .collect(Collectors.toList());

        return columnNames.indexOf(columnName);
    }

    // Méthode pour vérifier si les conditions sont satisfaites pour un
    // enregistrement
    private boolean satisfiesConditions(Record record) {
        List<String> recordValues = record.getRecValues();

        for (SelectCondition condition : conditions) {
            int columnIndex = getIndexOfColumn(condition.getColumnName(), record);
            if (columnIndex != -1 && !condition.evaluateCondition(recordValues.get(columnIndex))) {
                return false;
            }
        }

        return true;
    }
}
