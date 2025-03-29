import java.util.HashMap;
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
    private ArrayList<String> instructions;
    private HashMap<Ingredient, IngredientSize> ingredients;

    public String getTitle() {return title;}
    
    public void setTitle(String title) {this.title = title;}

    public String getDescription() {return description;}
    
    public void setDescription(String description) {this.description = description;}

    public BufferedImage getImage() {return image;}

    public ArrayList<String> getInstructions() {return instructions;}
    
    public void setInstructions(ArrayList<String> instructions) {this.instructions = instructions;}

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
