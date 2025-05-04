package com.RecipeBook;

class IngredientSize {
    private final Fraction amount;
    private final String unit;

    public IngredientSize(Fraction amount, String unit) {
        this.amount = amount;
        this.unit = unit;
    }

    public String toString() {return amount + " " + unit;}

    public static IngredientSize fromString(String amount) {
        String[] fractionElements = amount.trim().split("\\s+", 2);
        Fraction fractionAmount = Fraction.fromString(fractionElements[0]);
        String unit = fractionElements.length > 1 ? fractionElements[1] : "";

        return new IngredientSize(fractionAmount, unit);
    }
}
