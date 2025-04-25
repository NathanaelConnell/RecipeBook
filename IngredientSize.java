class IngredientSize {
    private Fraction amount;
    private String unit;

    public IngredientSize(Fraction amount, String unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public Fraction getAmount() {return amount;}

    public void setAmount(Fraction amount) {this.amount = amount;}

    public String getUnit() {return unit;}

    public void setUnit(String unit) {this.unit = unit;}

    public String toString() {return amount + " " + unit;}
}
