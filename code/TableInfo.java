package Main;

import java.util.ArrayList;
import java.util.List;

public class TableInfo {

    String tableName;
    int nbrColumns;
    ArrayList<ColInfo> columnInfo;
    PageId headerPageId; // Nouvelle variable membre

    public TableInfo(String tableName, int nbrColumns, List<String> colNames, List<String> colTypes,
            PageId headerPageId) {
        this.tableName = tableName;
        this.nbrColumns = nbrColumns;
        this.columnInfo = new ArrayList<>();
        this.headerPageId = headerPageId; // Initialisation de la nouvelle variable

        /*
         * Vérification que le nombre de colonnes et la longueur des listes
         * // correspondent
         * if (nbrColumns == colNames.size() && nbrColumns == colTypes.size()) {
         * for (int i = 0; i < nbrColumns; i++) {
         * ColInfo colInfo = new ColInfo(colNames.get(i), colTypes.get(i));
         * this.columnInfo.add(colInfo);
         * }
         * } else {
         * throw new
         * IllegalArgumentException("Le nombre de colonnes ne correspond pas à la longueur des listes."
         * );
         * }
         */
    }

    public TableInfo(String tableName, List<ColInfo> columnInfo, PageId headerPageId) {
        this.tableName = tableName;
        this.nbrColumns = columnInfo.size();
        this.columnInfo = new ArrayList<>(columnInfo);
        this.headerPageId = headerPageId;
    }

    public List<ColInfo> getColumnInfo() {
        return columnInfo;
    }

    public PageId getHeaderPageId() {
        return new PageId(-1, 0);
    }

    public String getTableName() {
        return tableName;
    }

    public int getNbrColumns() {
        return nbrColumns;
    }

}
