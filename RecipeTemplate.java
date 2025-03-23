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
        panel.setBorder(new EmptyBorder(new Insets(20, 75, 75, 75))); // Added padding

        JLabel title = new JLabel("Title");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setFont(new Font("Times New Roman", Font.BOLD, 25));

        JLabel image = new JLabel("Image Placeholder", SwingConstants.CENTER);
        image.setPreferredSize(new Dimension(400, 300));
        image.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Fake border to indicate image

        DefaultListModel<String> ingredientsModel = new DefaultListModel<>();
        JList<String> ingredients = new JList<>(ingredientsModel);
        ingredientsModel.addElement("- Ingredient 1");
        ingredientsModel.addElement("- Ingredient 2");
        JScrollPane ingredientsPane = new JScrollPane(ingredients);
        ingredientsPane.setPreferredSize(new Dimension(300, 200));

        JTextArea instructions = new JTextArea("Instructions:");
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        instructions.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        instructions.setPreferredSize(new Dimension(450, 200));

        panel.add(title);
        panel.add(image);
        panel.add(ingredientsPane);
        panel.add(instructions);

        frame.add(panel);
        frame.setVisible(true);
    }
}
