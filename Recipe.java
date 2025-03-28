import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

class Recipe {
    private String title;
    private String description;
    private BufferedImage image;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<String> instructions;

    public Recipe (String title, String description, BufferedImage image, 
    ArrayList<Ingredient> ingredients, ArrayList<String> instructions) {
        //Constructor
    }

    public String getTitle() {return title;}
    
    public void setTitle(String newTitle) {title = newTitle;}

    public String getDescription() {return description;}
    
    public void setDescription() (String newDescription) {description = newDescription;}

    public String getImage() {return image;}
    
    public void setImage() (String newImage) {image = newImage;}

    public String getInstructions() {return instructions;}
    
    public void setInstructions() (String newInstructions) {instructions = newInstructions;}
    

    public void importImage() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedImage selectedImage = ImageIO.read(selectedFile);
                if (selectedImage != null) {
                    int cropLength = Math.min(selectedImage.getWidth(), selectedImage.getHeight());
                    image = selectedImage.getSubimage(Math.max((selectedImage.getWidth()-cropLength)/2, 0),
                            Math.max((selectedImage.getHeight()-cropLength)/2, 0),
                            cropLength, cropLength);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid image format.");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error uploading image: " + e.getMessage());
            }
        }
    }
}
