import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;

class Recipe {
    private int id;
    private String title;
    private String type;
    private String description;
    private BufferedImage image;
    private String instructions;
    private final HashMap<String, IngredientSize> ingredients = new HashMap<>();

    Recipe() {
        id = 0;
        title = "";
        type = "";
        description = "";
        instructions = "";
        setDefaultImage();
    }

    public Recipe(String title, String type, String description, BufferedImage image, String instructions, HashMap<String, IngredientSize> ingredients) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.image = image;
        this.instructions = instructions;
        this.ingredients.putAll(ingredients);
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public String getDescription() {return description;}
    
    public void setDescription (String description) {this.description = description;}

    public BufferedImage getImage() {return image;}

    public void setImage(BufferedImage image) {this.image = image;}

    public String getInstructions() {return instructions;}
    
    public void setInstructions(String instructions) {this.instructions = instructions;}

    public HashMap<String, IngredientSize> getIngredients() {return ingredients;}

    public void addIngredient(String ingredient, IngredientSize size) {ingredients.put(ingredient, size);}

    public void removeIngredient(String ingredient) {ingredients.remove(ingredient);}

    public String printIngredients() {
        var output = new StringBuilder();
        for(String ingredient: ingredients.keySet()) {
            output.append("\n").append(ingredients.get(ingredient)).append(" ").append(ingredient);
        }
        return output.toString();
    }

    public String toString() {
        return "Title: " + title + " ("+ type + ")\n\nDescription: \n" + description + "\n\nIngredients: "
                + printIngredients() + "\n\nInstructions: \n" + instructions;
    }

    private void setDefaultImage() {
        image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics(); // Use Graphics2D for better control

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, 300, 300);

        g.setColor(Color.BLACK);
        Font font = g.getFont(); // Use the default font or set your own with g.setFont(...)
        FontMetrics metrics = g.getFontMetrics(font);

        String text = "No Image Provided";
        int x = (300 - metrics.stringWidth(text)) / 2;
        int y = (300 - metrics.getHeight()) / 2 + metrics.getAscent();

        g.drawString(text, x, y);
        g.dispose();
    }
}
