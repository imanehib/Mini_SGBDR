package Main;

public class SelectCondition implements Comparable<SelectCondition> {
    private String columnName;
    private String operator;
    private String value;
    private final String[] validOperators = { "=", "<", ">", "<=", ">=", "<>" };

    public SelectCondition(String columnName, String operator, String value) {
        this.columnName = columnName;
        setOperator(operator);
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        if (isValidOperator(operator)) {
            this.operator = operator;
        } else {
            throw new IllegalArgumentException("Opérateur invalide : " + operator);
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return columnName + " " + operator + " " + value;
    }

    @Override
    public int compareTo(SelectCondition other) {
        // Comparaison des opérateurs pour l'ordre alphabétique
        return this.operator.compareTo(other.operator);
    }

    public boolean evaluateCondition(Object recordValue) {
        if (!isTypeCompatible(recordValue)) {
            throw new IllegalArgumentException("Type incompatible pour l'évaluation de la condition.");
        }

        String stringValue = convertToString(recordValue);

        switch (operator) {
            case "=":
                return stringValue.equals(value);
            case "<":
                return stringValue.compareTo(value) < 0;
            case ">":
                return stringValue.compareTo(value) > 0;
            case "<=":
                return stringValue.compareTo(value) <= 0;
            case ">=":
                return stringValue.compareTo(value) >= 0;
            case "<>":
                return !stringValue.equals(value);
            default:
                throw new IllegalArgumentException("Opérateur non géré : " + operator);
        }
    }

    private boolean isTypeCompatible(Object recordValue) {
        return recordValue instanceof Comparable;
    }

    private String convertToString(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else {
            return String.valueOf(value);
        }
    }

    private boolean isValidOperator(String op) {
        for (String validOp : validOperators) {
            if (validOp.equals(op)) {
                return true;
            }
        }
        return false;
    }
}
