import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics;

class Recipe {
    private String title;
    private String type;
    private String description;
    private BufferedImage image;
    private String instructions;
    private final HashMap<String, IngredientSize> ingredients = new HashMap<>();

    Recipe() {
        title = "Title";
        type = "";
        description = "Description (optional)";
        instructions = "Instructions";
    
        try {
            image = ImageIO.read(new File("Default_Image.png"));
        } catch (IOException e) {
            System.err.println("Default image could not be loaded. Using blank image.");
            image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB); // Or whatever size you need
            Graphics g = image.getGraphics();
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, 100, 100);
            g.setColor(Color.BLACK);
            g.drawString("No Image", 10, 50); // Optional: label it
            g.dispose();
        }
    }

    public Recipe(String title, String type, String description, BufferedImage image, String instructions, HashMap<String, IngredientSize> ingredients) {
        //this.id = id; need to add later for database
        this.title = title;
        this.type = type;
        this.description = description;
        this.image = image;
        this.instructions = instructions;
        this.ingredients.putAll(ingredients);
    }

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public String getDescription() {return description;}
    
    public void setDescription (String description) {this.description = description;}

    public BufferedImage getImage() {return image;}

    public String getInstructions() {return instructions;}
    
    public void setInstructions(String instructions) {this.instructions = instructions;}

    public HashMap<String, IngredientSize> getIngredients() {return ingredients;}

    public void addIngredient(String ingredient, IngredientSize size) {ingredients.put(ingredient, size);}

    public void removeIngredient(String ingredient) {ingredients.remove(ingredient);}

    public void setImage(BufferedImage image) {this.image = image;}

    public String printIngredients() {
        var output = new StringBuilder();
        for(String ingredient: ingredients.keySet()) {
            output.append("\n").append(ingredients.get(ingredient)).append(" ").append(ingredient);
        }
        return output.toString();
    }

    public String toString() {
        return "Title: " + title + "\nDescription" + description + "\nIngredients: "
                + printIngredients() + "\nInstructions: " + instructions;
    }
}
