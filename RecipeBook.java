import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.FileWriter;

class RecipeBook {
  public static void main(String[] args) {
    //Recipes
    ArrayList<Recipe> recipes = new ArrayList<>();

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
      //Create new recipe
      recipes.add(new Recipe());
      Recipe recipe = recipes.get(recipes.size()-1);
      HashMap<String, IngredientSize> ingredients = new HashMap<>();

      JFrame frame = new JFrame("Recipe");
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setSize(500, 700);

      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      panel.setBorder(new EmptyBorder(new Insets(20, 30, 30, 30))); // Adjusted padding

      // Editable Title
      JTextField title = new JTextField("Title");
      title.setFont(new Font("Times New Roman", Font.BOLD, 25));
      title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
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

      // Image Placeholder
      JLabel image = new JLabel("Image Placeholder", SwingConstants.CENTER);
      image.setPreferredSize(new Dimension(400, 200));
      image.setBorder(BorderFactory.createLineBorder(Color.BLACK));

      // Editable description
      JTextArea description = new JTextArea("Description");
      description.setLineWrap(true);
      description.setWrapStyleWord(true);
      description.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      description.setPreferredSize(new Dimension(450, 200));
      description.addFocusListener(new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
          if(description.getText().equals("Description")) {
            description.setText("");
          }
        }
        public void focusLost(FocusEvent e) {
          if(description.getText().isEmpty()) {
            description.setText("Description");
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

      addIngredientButton.addActionListener(event -> {
          String ingredient = ingredientInput.getText().trim();
          String size = sizeInput.getText().trim();
          Pattern p = Pattern.compile("\\d+");
          Matcher m = p.matcher(size);

          int numerator = 0;
          int denominator = 1;
          String units = "";

          if (m.find()) {numerator = Integer.parseInt(m.group());}
          System.out.println(numerator);
          if (m.find()) {denominator = Integer.parseInt(m.group());}

          p = Pattern.compile("[a-zA-z]+");
          m = p.matcher(size);
          if (m.find()) {units = m.group();}

          if (!ingredient.isEmpty() && !ingredient.equals("Ingredient") && !size.isEmpty() && numerator != 0) {
            Fraction amount = new Fraction(numerator, denominator);
            ingredients.put(ingredient, new IngredientSize(amount, units));

            ingredientsModel.addElement("- " + size + " " + ingredient);
            ingredientInput.setText("");
            sizeInput.setText("");
          }
      });

      // Editable Instructions
      JTextArea instructions = new JTextArea("Instructions");
      instructions.setLineWrap(true);
      instructions.setWrapStyleWord(true);
      instructions.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      instructions.setPreferredSize(new Dimension(450, 200));
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

      JButton addSaveButton = new JButton("Save");
      addSaveButton.addActionListener(event1 -> {
        recipe.setTitle(title.getText());
        recipe.setDescription(description.getText());
        recipe.setIngredients(ingredients);
        recipe.setInstructions(instructions.getText());

        String fileName = "example.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
          writer.write(recipe.toString());
        } catch (IOException ex) {
          System.err.println(ex.getMessage());
        }
      });

      JPanel inputRow = new JPanel();
      inputRow.setLayout(new FlowLayout(FlowLayout.LEFT));
      inputRow.add(ingredientInput);
      inputRow.add(sizeInput);

      panel.add(title);
      panel.add(image);
      panel.add(description);
      panel.add(ingredientsPane);
      panel.add(inputRow);
      panel.add(addIngredientButton);
      panel.add(instructions);
      panel.add(addSaveButton);

      frame.add(panel);
      frame.setVisible(true);

    });

    // Add buttons to panel
    buttonPanel.add(button1);
    buttonPanel.add(button2);

    // Add button panel to main frame
    mainFrame.add(buttonPanel, BorderLayout.CENTER);

    // Make the frame visible
    mainFrame.setVisible(true);
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
