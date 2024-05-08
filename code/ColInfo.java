package Main;

public class ColInfo {
    
    private String name;
    private ColType colType;
    private int T ;

    public enum ColType {
        INT,
        FLOAT,
        STRING,
        VARSTRING
    }

    public ColInfo(String name, ColType colType, int T) {
        this.name = name;
        if (!isValidColType(colType)) {
            throw new IllegalArgumentException("Type de colonne invalide : " + colType);
        }
        this.colType = colType;
        this.T = T;
    }

    public ColInfo(String name, ColType colType) {
        this(name,colType,0);
    }

    public ColType getType() {
        return colType;
    }

    public String getName() {
        return name;
    }

    public int getT() {
        return T;
    }

    // Méthode pour valider le type de colonne
    private boolean isValidColType(ColType type) {
        return type == ColType.INT || type == ColType.FLOAT || type == ColType.STRING || type == ColType.VARSTRING;
    }

    public int getSize() {
        switch (colType) {
            case INT:
                return Integer.BYTES;
            case FLOAT:
                return Float.BYTES;
            case STRING:
            case VARSTRING:
            // Taille de la chaîne variable (taille stockée en tant qu'entier) + taille de la chaîne
            // Vous devez remplacer T par la valeur appropriée pour votre application
            return Integer.BYTES + T * Character.BYTES;
            default:
                throw new IllegalArgumentException("Type de colonne non géré : " + colType);
        }
    }

}
