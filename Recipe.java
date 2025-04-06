import java.util.HashMap;
import java.awt.image.BufferedImage;

class Recipe {
    private String title;
    private String description;
    private BufferedImage image;
    private String instructions;
    private HashMap<String, IngredientSize> ingredients;

    public String getTitle() {return title;}
    
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    
    public void setDescription (String description) {this.description = description;}

    public BufferedImage getImage() {return image;}

    public String getInstructions() {return instructions;}
    
    public void setInstructions(String instructions) {this.instructions = instructions;}

    public HashMap<String, IngredientSize> getIngredients() {return ingredients;}

    public void setIngredients(HashMap<String, IngredientSize> ingredients) {this.ingredients = ingredients;}

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String toString() {
        StringBuilder output = new StringBuilder("Title: " + title + "\nDescription: " + description + "\nIngredients: ");
        for(String ingredient: ingredients.keySet()) {
            output.append("\n").append(ingredients.get(ingredient)).append(" ").append(ingredient);
        }
        output.append("\nInstructions: ").append(instructions);

        return output.toString();
    }
}
