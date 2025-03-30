import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class RecipeBook {
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
    button1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        openRecipesFrame();
      }
    });

    button2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
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
                
        //description
        JTextField describe = new JTextField("Description");
        describe.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        describe.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        // Image Placeholder
        JLabel image = new JLabel("Image Placeholder", SwingConstants.CENTER);
        image.setPreferredSize(new Dimension(400, 200));
        image.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
        // Editable Ingredients List
        DefaultListModel<String> ingredientsModel = new DefaultListModel<>();
        JList<String> ingredientsList = new JList<>(ingredientsModel);
        JScrollPane ingredientsPane = new JScrollPane(ingredientsList);
        ingredientsPane.setPreferredSize(new Dimension(300, 150));
            
        // Text field and button to add new ingredients
        JTextField ingredientInput = new JTextField();
        ingredientInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        JButton addIngredientButton = new JButton("Add Ingredient");
            
        addIngredientButton.addActionListener(event -> {
            String ingredient = ingredientInput.getText().trim();
            if (!ingredient.isEmpty()) {
                ingredientsModel.addElement("- " + ingredient);
                ingredientInput.setText("");
            }
        });
            
        // Editable Instructions
        JTextArea instructions = new JTextArea("Instructions:");
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        instructions.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        instructions.setPreferredSize(new Dimension(450, 200));
            
        panel.add(title);
        panel.add(image);
        panel.add(ingredientsPane);
        panel.add(ingredientInput);
        panel.add(addIngredientButton);
        panel.add(instructions);
            
        frame.add(panel);
        frame.setVisible(true);
                
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
      btn.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          System.out.println("Filter selected: " + filter);
        }
      });
      filterPanel.add(btn);
      filterPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
    }
    recipesFrame.add(filterPanel, BorderLayout.WEST);

    // Export Grocery List Button
    JButton exportButton = new JButton("Export Grocery List");
    exportButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ExportGroceryListFrame(); // Call the new method
        }
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

    new ExportGroceryList(ingredients); // Open the ExportGroceryList frame
  }
}
