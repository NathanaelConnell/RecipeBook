class IngredientSize {
    private final Fraction amount;
    private final String unit;

    public IngredientSize(Fraction amount, String unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public String toString() {return amount + " " + unit;}
}
