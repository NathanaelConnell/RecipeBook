package com.RecipeBook;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class RecipeBook {
  //Recipes
  private static final ArrayList<Recipe> recipes;

    static {
        try {
            recipes = RecipeRepo.load();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
        Recipe recipe = new Recipe();
        recipeTemplate(recipe);
      });

      // Add buttons to panel
      buttonPanel.add(button1);
      buttonPanel.add(button2);

      // Add button panel to main frame
      mainFrame.add(buttonPanel, BorderLayout.CENTER);

      // Make the frame visible
      mainFrame.setVisible(true);
  }

  // Create/Edit Recipe
  private static void recipeTemplate(Recipe recipe) {
    JFrame frame = new JFrame("Recipe");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setSize(500, 700);
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(new EmptyBorder(new Insets(20, 30, 30, 30))); // Adjusted padding
    panel.setBackground(new Color(145, 210, 212));

    // Editable Title
    String titlePlaceholder = "Title";
    JTextField title = new JTextField(recipe.getTitle().isEmpty() ? titlePlaceholder : recipe.getTitle());
    title.setFont(new Font("Times New Roman", Font.BOLD, 25));
    title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    title.setBackground(new Color(248, 245, 170));
    title.setBorder(BorderFactory.createLineBorder(new Color(241, 9, 125)));
    title.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if(title.getText().equals(titlePlaceholder)) {title.setText("");}
      }
      public void focusLost(FocusEvent e) {
        if (title.getText().isEmpty()) {title.setText(titlePlaceholder);}
        else {recipe.setTitle(title.getText());}
      }
    });

    //Drop Down Menu for type
    String[] types = {"-- Select recipe type --", "Entree", "Appetizer", "Dessert", "Drink", "Other"};
    JComboBox<String> typeList = new JComboBox<>(types);
    typeList.setSelectedItem(recipe.getType().isEmpty() ? types[0] : recipe.getType());

    //Disable selecting the placeholder after opening
    typeList.addActionListener(e -> {
      if(typeList.getItemAt(0).equals(types[0])) {
        typeList.removeItemAt(0);}
        recipe.setType(Objects.requireNonNull(typeList.getSelectedItem()).toString());
    });

    //Editable Image
    JLabel imageLabel = new JLabel();
    imageLabel.setIcon(new ImageIcon(recipe.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
    imageLabel.setPreferredSize(new Dimension(300, 300));
    imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    JButton importImage = new JButton("Upload Image");
    importImage.addActionListener(e1 -> uploadImage(recipe, imageLabel));

    //Editable description
    String descriptionPlaceholder = "Description (optional)";
    JTextArea description = new JTextArea(recipe.getDescription().isEmpty() ? descriptionPlaceholder : recipe.getDescription());
    description.setLineWrap(true);
    description.setWrapStyleWord(true);
    description.setBackground(new Color(248, 245, 170));
    description.setBorder(BorderFactory.createLineBorder(new Color(23, 103, 106)));
    description.setPreferredSize(new Dimension(450, 200));
    description.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if(description.getText().equals(descriptionPlaceholder)) {description.setText("");}
      }
      public void focusLost(FocusEvent e) {
        if(description.getText().isEmpty()) {description.setText(descriptionPlaceholder);}
        else{recipe.setDescription(description.getText());}
      }
    });

    // Editable Ingredients List
    DefaultListModel<String> ingredientsModel = new DefaultListModel<>();
    JList<String> ingredientsList = new JList<>(ingredientsModel);
    JScrollPane ingredientsPane = new JScrollPane(ingredientsList);
    ingredientsPane.setPreferredSize(new Dimension(300, 150));
    for(String ingredient : recipe.getIngredients().keySet()) {
      IngredientSize size = recipe.getIngredients().get(ingredient);
      ingredientsModel.addElement("- " + size + " " + ingredient);
    }

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

    // Allow you to remove ingredients
    JButton removeIngredientButton = new JButton("Remove");
    removeIngredientButton.setBackground(new Color(23, 103, 106));
    removeIngredientButton.setForeground(new Color(145,210,212));
    removeIngredientButton.setVisible(false);
    ingredientsList.addListSelectionListener( e ->
            removeIngredientButton.setVisible(!ingredientsList.isSelectionEmpty()));

    removeIngredientButton.addActionListener(e -> {
      removeIngredient(recipe, ingredientsModel, ingredientsList);
      removeIngredientButton.setVisible(false);
    });


    // Editable Instructions
    String instructionsPlaceholder = "Instructions";
    JTextArea instructions = new JTextArea(recipe.getInstructions().isEmpty() ? instructionsPlaceholder : recipe.getInstructions());
    instructions.setLineWrap(true);
    instructions.setWrapStyleWord(true);
    instructions.setBorder(BorderFactory.createLineBorder(new Color(23, 103, 106)));
    instructions.setPreferredSize(new Dimension(450, 200));
    instructions.setBackground(new Color(248, 245, 170));
    instructions.addFocusListener(new FocusListener() {
      @Override
      public void focusGained(FocusEvent e) {
        if(instructions.getText().equals(instructionsPlaceholder)) {instructions.setText("");}
      }
      public void focusLost(FocusEvent e) {
        if(instructions.getText().isEmpty()) {instructions.setText(instructionsPlaceholder);}
        else {recipe.setInstructions(instructions.getText());}
      }
    });

    JButton saveButton = new JButton("Save");
    saveButton.setBackground(new Color(23, 103, 106));
    saveButton.setForeground(new Color(145,210,212));
    saveButton.addActionListener(e -> {
      try {
        save(recipe);
        frame.dispose();
        viewRecipe(recipe);
      } catch (IOException | SQLException ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    });
           
    JPanel imageRow = new JPanel();
    imageRow.setLayout(new FlowLayout(FlowLayout.LEFT));
    imageRow.add(importImage);
    imageRow.add(imageLabel);

    JPanel inputRow = new JPanel();
    inputRow.setLayout(new FlowLayout(FlowLayout.LEFT));
    inputRow.add(ingredientInput);
    inputRow.add(sizeInput);
    inputRow.add(addIngredientButton);
    inputRow.add(removeIngredientButton);

    panel.add(title);
    panel.add(typeList);
    panel.add(imageRow);
    panel.add(description);
    panel.add(ingredientsPane);
    panel.add(inputRow);
    panel.add(instructions);
    panel.add(saveButton);

    frame.add(panel);
    frame.setVisible(true);
  }

  // Upload a recipe image
  private static void uploadImage(Recipe recipe, JLabel imageLabel) {
    BufferedImage image;
    JFileChooser fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG files", "png");
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

  // Add an ingredient to a recipe
  private static void addIngredient(Recipe recipe, JTextField ingredientInput, JTextField sizeInput, DefaultListModel<String> ingredientsModel) {
    StringBuilder ingredient = new StringBuilder();
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
    while (m.find()) {
        ingredient.append(m.group());}

    if ((!ingredient.toString().isEmpty()) && !ingredient.toString().equals("Ingredient") && !size.isEmpty() && numerator != 0) {
      Fraction amount = new Fraction(numerator, denominator);
      recipe.addIngredient(ingredient.toString(), new IngredientSize(amount, units));

      ingredientsModel.addElement("- " + size + " " + ingredient);
      ingredientInput.setText("Ingredient");
      sizeInput.setText("Quantity");
    }
    else {
      JOptionPane.showMessageDialog(null, "Invalid ingredient");
    }
  }

  // Remove an ingredient from a recipe
  private static void removeIngredient(Recipe recipe, DefaultListModel<String> ingredientsModel, JList<String> ingredientsList) {
    int selectedIndex = ingredientsList.getSelectedIndex();
    String key = recipe.getIngredients().keySet().toArray(new String[0])[selectedIndex];
    ingredientsModel.remove(selectedIndex);
    recipe.removeIngredient(key);
  }

  // Save Recipe
  private static void save(Recipe recipe) throws IOException, SQLException {
    boolean invalidRecipe = false;
    String error = "Invalid recipe! Please update the following fields: ";
    if(recipe.getTitle().isEmpty()) {
      error += "\nTitle";
      invalidRecipe = true;
    }
    if(recipe.getType().isEmpty()) {
      error += "\nType";
      invalidRecipe = true;
    }
    if(recipe.getIngredients().isEmpty()) {
      error += "\nIngredients";
      invalidRecipe = true;
    }
    if(recipe.getInstructions().isEmpty()) {
      error += "\nInstructions";
      invalidRecipe = true;
    }
    if(invalidRecipe) {
      throw new IOException(error);
    }
    try {
      RecipeRepo.save(recipe);
      recipes.add(recipe);
    }
    catch (SQLException e) {
      throw new SQLException(error);
    }
  }

  // Look at a recipe
  private static void viewRecipe(Recipe recipe) {
    // Create a new frame to display the saved recipe
    JFrame outputFrame = new JFrame("Saved Recipe");
    outputFrame.setSize(900, 400);
    outputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    JPanel outputPanel = new JPanel();
    outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.LINE_AXIS));

    JTextArea outputArea = new JTextArea(recipe.toString());
    outputArea.setEditable(false);
    outputArea.setLineWrap(true);
    outputArea.setWrapStyleWord(true);
    outputArea.setFont(new Font("Serif", Font.PLAIN, 16));
    outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    outputArea.setBackground(new Color(145, 210, 212));

    JLabel imageLabel = new JLabel();
    imageLabel.setIcon(new ImageIcon(recipe.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
    imageLabel.setPreferredSize(new Dimension(300, 300));
    imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

    JScrollPane scrollPane = new JScrollPane(outputArea);
    scrollPane.setPreferredSize(new Dimension(600, 300));

    // Export Grocery List Button
    JButton exportButton = new JButton("Export Grocery List");
    exportButton.addActionListener(e -> ExportGroceryList(recipe));

    //Edit Recipe Button
    JButton editButton = new JButton("Edit Recipe");
    editButton.addActionListener(e -> {
      recipeTemplate(recipe);
      outputFrame.dispose();
    });

    JButton deleteButton = new JButton("Delete Recipe");
    deleteButton.addActionListener(e -> {
      deleteRecipe(recipe);
      outputFrame.dispose();
    });

    outputPanel.add(scrollPane);
    outputPanel.add(imageLabel);

    // Panel for buttons (added to bottom)
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.setPreferredSize(new Dimension(900, 100));
    buttonPanel.add(exportButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);

    outputFrame.add(buttonPanel, BorderLayout.SOUTH);
    outputFrame.add(outputPanel);
    outputFrame.setVisible(true);
  }

  // Delete Recipe
  private static void deleteRecipe(Recipe recipe) {
    int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this recipe?", "Delete Recipe",JOptionPane.YES_NO_OPTION);
    if(result == JOptionPane.YES_OPTION) {
      File folder = new File("Grocery Lists");
      if(folder.exists()) {
        File groceryList = new File(folder, recipe.getTitle() + ".txt");
        if(groceryList.exists() && !groceryList.delete()) {
          JOptionPane.showMessageDialog(null, "Failed to delete grocery list file located at: \n" + groceryList.getAbsolutePath());
        }
      }
      try {
        RecipeRepo.remove(recipe);
        recipes.remove(recipe);
        JOptionPane.showMessageDialog(null, "Recipe deleted successfully!");
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Failed to delete recipe!");
      }

    }
  }

  // Method to open the "Look For Recipes" frame with a border layout
  private static void openRecipesFrame() {
    JFrame recipesFrame = new JFrame("Recipes");
    recipesFrame.setSize(500, 400);
    recipesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    recipesFrame.setLayout(new BorderLayout());

    // Panel for recipe titles
    JPanel recipeListPanel = new JPanel();
    recipeListPanel.setLayout(new BoxLayout(recipeListPanel, BoxLayout.Y_AXIS));
    recipesFrame.add(new JScrollPane(recipeListPanel), BorderLayout.CENTER);

    // Top panel with title
    JLabel titleLabel = new JLabel("Look for Recipes", SwingConstants.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    recipesFrame.add(titleLabel, BorderLayout.NORTH);

    // Left panel with filter buttons
    JPanel filterPanel = new JPanel();
    filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
    String[] filters = { "All", "Entree", "Appetizer", "Dessert", "Drink", "Other"};
    filterRecipe(filters[0], recipeListPanel);
    for (String filter : filters) {
      JButton btn = getFilterBtn(filter, recipeListPanel);
      filterPanel.add(btn);
      filterPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
    }

    recipesFrame.add(filterPanel, BorderLayout.WEST);
    recipesFrame.setVisible(true);
  }

  // Generate buttons for filters
  private static JButton getFilterBtn(String filter, JPanel recipeListPanel) {
    JButton btn = new JButton(filter);
    btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    btn.addActionListener(e -> filterRecipe(filter, recipeListPanel));
    return btn;
  }

  // Filter the recipes
  private static void filterRecipe(String filter, JPanel recipeListPanel) {
    recipeListPanel.removeAll();
    for(Recipe recipe : recipes) {
      if(filter.equals("All") || filter.equals(recipe.getType())) {
        JButton recipeButton = new JButton(recipe.getTitle());
        recipeButton.setHorizontalAlignment(SwingConstants.LEFT);
        recipeButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, recipeButton.getPreferredSize().height));

        recipeButton.addActionListener(ev -> viewRecipe(recipe));
        recipeListPanel.add(recipeButton);
      }
    }
    recipeListPanel.revalidate(); //Recalculates the layout of the panel
    recipeListPanel.repaint();  //Updates details
  }

  // Export Grocery List to txt file
  private static void ExportGroceryList(Recipe recipe) {
    File folder = new File("Grocery Lists");
    if (!folder.exists()) {
      if (!folder.mkdir()) {
        JOptionPane.showMessageDialog(null, "Grocery list could not be created");
      }
    }
    else {
      try{
        File groceryList = new File(folder, recipe.getTitle() + ".txt");
        FileWriter fileWriter = new FileWriter(groceryList);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(recipe.getTitle() + " Grocery List: \n" + recipe.printIngredients());
        bufferedWriter.close();

        JOptionPane.showMessageDialog(null, "The grocery list was successfully exported to: \n" + groceryList.getAbsolutePath());
      }
      catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Grocery list could not be created");
      }
    }
  }
}
