import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI {
    public static void main(String[] args) {
        // Main frame
        JFrame mainFrame = new JFrame("Main Panel");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(300, 200);
        mainFrame.setLayout(new BorderLayout());

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create buttons
        JButton button1 = new JButton("Look For Recipes");
        JButton button2 = new JButton("Create Recipe");

        // Add action listeners to buttons
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFrame("Recipes", Color.CYAN);
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFrame("Create Recipe", Color.ORANGE);
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

    private static void openFrame(String title, Color bgColor) {
        JFrame frame = new JFrame(title);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(bgColor);
        frame.setVisible(true);
    }
}
