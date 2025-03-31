import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Recipe.java;
import Ingredient.java;


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

        // Editable description
        JTextArea description = new JTextArea("description:");
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        description.setPreferredSize(new Dimension(450, 200));

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
        panel.add(description);
        panel.add(ingredientsPane);
        panel.add(ingredientInput);
        panel.add(addIngredientButton);
        panel.add(instructions);

        frame.add(panel);
        frame.setVisible(true);


        JButton addSaveButton = new JButton("Save");
        addSaveButton.addActionListener(e -> {

            String textTitle = title.getText();
            String textDescription = description.getText();
            //ArrayList<String> instructionsArrayList = instructions.getText();
            // We currently have instructions as an array list inside of the class but that's 
            //not how we set up inside of the template so we need to discuss and choose which one we're doing
            //I don't know how to do this HashMap stuff I'm going to try and discuss
            //it with the person who made the recipe. Java file

            setTitle(textTitle);
            setDescription(String description);
            setIngredients(HashMap<Ingredient, IngredientSize> ingredients);
            //setInstructions(instructionsArrayList);
        });
    }
}
