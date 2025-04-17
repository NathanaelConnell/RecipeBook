import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.FileWriter;

class RecipeBook {
  //Recipes
  static ArrayList<Recipe> recipes = new ArrayList<>();

  public static void main(String[] args) {
    // Main frame
    JFrame mainFrame = new JFrame("Main Panel");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setSize(300, 200);
    mainFrame.setLayout(new BorderLayout());

    // Panel for buttons
    JPanel buttonPanel = new JPanel(new FlowLayout());

    // Create buttons
    JButton button1 = new JButton("Look For Recipes");
    JButton button2 = new JButton("Create Recipe");

    // Add action listeners to buttons
    button1.addActionListener(e -> openRecipesFrame());

    button2.addActionListener(e -> {
      Recipe recipe;
      try {
        recipe = new Recipe();
        recipeTemplate(recipe);
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(null, "Recipe could not be created");
      }
    });

    // Add buttons to panel
    buttonPanel.add(button1);
    buttonPanel.add(button2);

    // Add button panel to main frame
    mainFrame.add(buttonPanel, BorderLayout.CENTER);

    // Make the frame visible
    mainFrame.setVisible(true);
  }

  private static void recipeTemplate(Recipe recipe) {
    JFrame frame = new JFrame("Recipe");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(500, 700);
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(new EmptyBorder(new Insets(20, 30, 30, 30))); // Adjusted padding
    panel.setBackground(new Color(145, 210, 212));

    // Editable Title
    JTextField title = new JTextField(recipe.getTitle());
    title.setFont(new Font("Times New Roman", Font.BOLD, 25));
    title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    title.setBackground(new Color(248, 245, 170));
    title.setBorder(BorderFactory.createLineBorder(new Color(241, 9, 125)));
    title.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if(title.getText().equals("Title")) {
          title.setText("");
        }
      }
      public void focusLost(FocusEvent e) {
        if(title.getText().isEmpty()) {
          title.setText("Title");
        }
      }
    });

    //Editable Image
    JLabel imageLabel = new JLabel();
    imageLabel.setIcon(new ImageIcon(recipe.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
    imageLabel.setPreferredSize(new Dimension(300, 300));
    imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    JButton importImage = new JButton("Upload Image");
    importImage.addActionListener(e1 -> uploadImage(recipe, imageLabel));

    // Editable description
    JTextArea description = new JTextArea(recipe.getDescription());
    description.setLineWrap(true);
    description.setWrapStyleWord(true);
    description.setBackground(new Color(248, 245, 170));
    description.setBorder(BorderFactory.createLineBorder(new Color(23, 103, 106)));
    description.setPreferredSize(new Dimension(450, 200));
    description.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if(description.getText().equals("Description (optional)")) {
          description.setText("");
        }
      }
      public void focusLost(FocusEvent e) {
        if(description.getText().isEmpty()) {
          description.setText("Description (optional)");
        }
      }
    });

    // Editable Ingredients List
    DefaultListModel<String> ingredientsModel = new DefaultListModel<>();
    JList<String> ingredientsList = new JList<>(ingredientsModel);
    JScrollPane ingredientsPane = new JScrollPane(ingredientsList);
    ingredientsPane.setPreferredSize(new Dimension(300, 150));

    // Text field and button to add new ingredients
    JTextField ingredientInput = new JTextField();
    ingredientInput.setPreferredSize(new Dimension(100, 30));
    JTextField sizeInput = new JTextField();
    sizeInput.setPreferredSize(new Dimension(100, 30));
    JButton addIngredientButton = new JButton("Add Ingredient");
    addIngredientButton.setBackground(new Color(23, 103, 106));
    addIngredientButton.setForeground(new Color(145,210,212));
    ingredientInput.setText("Ingredient");
    sizeInput.setText("Quantity");
    ingredientInput.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if(ingredientInput.getText().equals("Ingredient")) {
          ingredientInput.setText("");
        }
      }
      public void focusLost(FocusEvent e) {
        if(ingredientInput.getText().isEmpty()) {
          ingredientInput.setText("Ingredient");
        }
      }
    });
    sizeInput.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if(sizeInput.getText().equals("Quantity")) {
          sizeInput.setText("");
        }
      }
      public void focusLost(FocusEvent e) {
        if(sizeInput.getText().isEmpty()) {
          sizeInput.setText("Quantity");
        }
      }
    });
    addIngredientButton.addActionListener(event ->
            addIngredient(recipe, ingredientInput, sizeInput, ingredientsModel));

    // Editable Instructions
    JTextArea instructions = new JTextArea(recipe.getInstructions());
    instructions.setLineWrap(true);
    instructions.setWrapStyleWord(true);
    instructions.setBorder(BorderFactory.createLineBorder(new Color(23, 103, 106)));
    instructions.setPreferredSize(new Dimension(450, 200));
    instructions.setBackground(new Color(248, 245, 170));
    instructions.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if(instructions.getText().equals("Instructions")) {
          instructions.setText("");
        }
      }
      public void focusLost(FocusEvent e) {
        if(instructions.getText().isEmpty()) {
          instructions.setText("Instructions");
        }
      }
    });

    JButton saveButton = new JButton("Save");
    saveButton.setBackground(new Color(23, 103, 106));
    saveButton.setForeground(new Color(145,210,212));
    saveButton.addActionListener(e ->
            save(recipe, title.getText(), description.getText(), instructions.getText(), frame));

    JPanel imageRow = new JPanel();
    imageRow.setLayout(new FlowLayout(FlowLayout.LEFT));
    imageRow.add(importImage);
    imageRow.add(imageLabel);

    JPanel inputRow = new JPanel();
    inputRow.setLayout(new FlowLayout(FlowLayout.LEFT));
    inputRow.add(ingredientInput);
    inputRow.add(sizeInput);
    inputRow.add(addIngredientButton);

    panel.add(title);
    panel.add(imageRow);
    panel.add(description);
    panel.add(ingredientsPane);
    panel.add(inputRow);
    panel.add(instructions);
    panel.add(saveButton);

    frame.add(panel);
    frame.setVisible(true);
  }

  private static void uploadImage(Recipe recipe, JLabel imageLabel) {
    BufferedImage image;
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
          image = selectedImage.getSubimage(Math.max((selectedImage.getWidth() - cropLength) / 2, 0),
                  Math.max((selectedImage.getHeight() - cropLength) / 2, 0),
                  cropLength, cropLength);
          recipe.setImage(image);
          ImageIcon icon = new ImageIcon(image.getScaledInstance(300, 300, Image.SCALE_SMOOTH));
          imageLabel.setIcon(icon);
          imageLabel.setText("");

        }
        else {
          JOptionPane.showMessageDialog(null, "Invalid image format");
        }
      } catch (IOException ex1) {
        JOptionPane.showMessageDialog(null, "Error uploading image: " + ex1.getMessage());
      }
    }
  }

  private static void addIngredient(Recipe recipe, JTextField ingredientInput, JTextField sizeInput, DefaultListModel<String> ingredientsModel) {
    String ingredient = "";
    String size =sizeInput.getText().trim();
    Pattern p = Pattern.compile("\\d+");
    Matcher m = p.matcher(size);

    int numerator = 0;
    int denominator = 1;
    String units = "";

    if (m.find()) {numerator = Integer.parseInt(m.group());}
    if (m.find()) {denominator = Integer.parseInt(m.group());}

    p = Pattern.compile("[a-zA-z ]+");
    m = p.matcher(size);
    if (m.find()) {units = m.group();}

    p = Pattern.compile("[a-zA-z' -]+");
    m = p.matcher(ingredientInput.getText().trim());
    while (m.find()) {ingredient += m.group();}

    if (!ingredient.isEmpty() && !ingredient.equals("Ingredient") && !size.isEmpty() && numerator != 0) {
      Fraction amount = new Fraction(numerator, denominator);
      recipe.addIngredient(ingredient, new IngredientSize(amount, units));

      ingredientsModel.addElement("- " + size + " " + ingredient);
      ingredientInput.setText("");
      sizeInput.setText("");
    }
    else {
      JOptionPane.showMessageDialog(null, "Invalid ingredient");
    }
  }

  private static void save(Recipe recipe, String title, String description, String instructions, JFrame frame) {
    boolean invalidRecipe = false;
    String error = "Invalid recipe! Please update the following fields: ";
    if(title.equals("Title")) {
      error += "\nTitle";
      invalidRecipe = true;
    }
    if(recipe.getIngredients().isEmpty()) {
      error += "\nIngredients";
      invalidRecipe = true;
    }
    if(instructions.equals("Instructions")) {
      error += "\nInstructions";
      invalidRecipe = true;
    }
    if(invalidRecipe) {
      JOptionPane.showMessageDialog(null, error);
    }
    else {
      recipe.setTitle(title);
      recipe.setDescription(description);
      recipe.setInstructions(instructions);
      recipes.add(recipe);

      //Temporary test file to make sure data is saving
      String fileName = "example.txt";
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
        writer.write(recipe.toString());
      } catch (IOException ex) {
        System.err.println(ex.getMessage());
      }
      frame.dispose();
    }
  }

  // Method to open the "Look For Recipes" frame with a border layout
  private static void openRecipesFrame() {
    JFrame recipesFrame = new JFrame("Recipes");
    recipesFrame.setSize(500, 400);
    recipesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    recipesFrame.setLayout(new BorderLayout());

    // Top panel with title
    JLabel titleLabel = new JLabel("Look for Recipes", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    recipesFrame.add(titleLabel, BorderLayout.NORTH);

    // Left panel with filter buttons
    JPanel filterPanel = new JPanel();
    filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
    String[] filters = { "All", "Entree", "Appetizer", "Desert", "Drinks" };
    for (String filter : filters) {
      JButton btn = new JButton(filter);
      btn.setAlignmentX(Component.CENTER_ALIGNMENT);
      btn.addActionListener(e -> System.out.println("Filter selected: " + filter));
      filterPanel.add(btn);
      filterPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
    }
    recipesFrame.add(filterPanel, BorderLayout.WEST);

    // Export Grocery List Button
    JButton exportButton = new JButton("Export Grocery List");
    exportButton.addActionListener(e -> {
        ExportGroceryListFrame(); // Call the new method
    });

    // Panel for buttons (added to bottom)
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(exportButton);
    recipesFrame.add(buttonPanel, BorderLayout.SOUTH); // Add button at the bottom

    // Main panel in the center for displaying recipe titles
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.DARK_GRAY, 2),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)));

    JLabel placeholder = new JLabel("Recipe Titles Will Appear Here", SwingConstants.CENTER);
    mainPanel.add(placeholder, BorderLayout.CENTER);
    recipesFrame.add(mainPanel, BorderLayout.CENTER);

    recipesFrame.setVisible(true);
  }

  // Original frame for "Create Recipe" button remains unchanged
  private static void openFrame(String title, Color bgColor) {
    JFrame frame = new JFrame(title);
    frame.setSize(300, 200);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.getContentPane().setBackground(bgColor);
    frame.setVisible(true);
  }

  // Frame for ExportGroceryList
  private static void ExportGroceryListFrame() {
    DefaultListModel<String> ingredients = new DefaultListModel<>();
    ingredients.addElement("Milk");
    ingredients.addElement("Eggs");
    ingredients.addElement("Bread");
    ingredients.addElement("Cheese");

    //new ExportGroceryList(ingredients); // Open the ExportGroceryList frame
  }
}
