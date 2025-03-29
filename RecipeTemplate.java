import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

class RecipeTemplate {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Recipe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(new Insets(20, 30, 30, 30))); // Adjusted padding

        // Editable Title
        JTextField title = new JTextField("Title");
        title.setFont(new Font("Times New Roman", Font.BOLD, 25));
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

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

        addIngredientButton.addActionListener(e -> {
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
}
