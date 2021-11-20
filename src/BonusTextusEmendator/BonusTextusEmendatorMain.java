// Declare package.
package BonusTextusEmendator;

// Main dependencies.
import javax.swing.JFrame;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;

import javax.swing.JOptionPane;

import javax.swing.ImageIcon;

import java.awt.Container;
import java.awt.GridLayout;

import java.awt.Color;
import java.awt.Font;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;

import java.io.FileWriter;
import java.io.PrintWriter;

import java.util.Scanner;

// Main class.
public class BonusTextusEmendatorMain extends JFrame {
    // Widgets.
    final private JTextArea Text = new JTextArea();
    final private JTextField Command = new JTextField();
    final private JButton ExecuteCommand = new JButton("Execute command.");

    // Class variables.
    private String Title = null;
    private Container Content = null;

    // Constructor.
    public BonusTextusEmendatorMain(String WindowIcon, String WindowTitle, int[] WindowXY, int[] WindowSize) {
        // Set title.
        super(WindowTitle);

        // Setup window.
        this.setBounds(WindowXY[0], WindowXY[1], WindowSize[0], WindowSize[1]);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set icon.
        ImageIcon Icon = new ImageIcon(WindowIcon);
        this.setIconImage(Icon.getImage());

        // Update title var;
        Title = WindowTitle;

        // Update content var.
        Content = this.getContentPane();

        // Update window grid.
        GridLayout Layout = new GridLayout(0, 1, 0, 1);
        Content.setLayout(Layout);

        // Set app background color.
        Content.setBackground(Color.BLACK);

        // Declare default app font.
        Font AppFont = new Font("Consolas", Font.PLAIN, 14);

        // Stylize widgets.
        Text.setBackground(Color.BLACK);
        Text.setForeground(Color.WHITE);
        Text.setFont(AppFont);

        Command.setBackground(Color.BLACK);
        Command.setForeground(Color.WHITE);
        Command.setFont(AppFont);

        ExecuteCommand.setBackground(Color.BLACK);
        ExecuteCommand.setForeground(Color.WHITE);
        ExecuteCommand.setFont(AppFont);

        // Set line wrap in text.
        Text.setLineWrap(true);

        // Add action listener to button, if say easier, just connecting function to button.
        ExecuteCommand.addActionListener(new ExecuteCommandFunction());

        // Place all widgets on window.
        PlaceWidgets();

        // Set window visible.
        SetWindowVisible();

        // Welcome message in console.
        System.out.println("Welcome to application!");
    }

    // Set window visible function.
    public void SetWindowVisible() {
        this.setVisible(true);
    }

    // Place widgets.
    public void PlaceWidgets() {
        Content.add(Text);
        Content.add(Command);
        Content.add(ExecuteCommand);
    }

    // Create message.
    public void Message(String Text, int Type) {
        JOptionPane.showMessageDialog(null, Text, Title, Type, null);
    }

    // Read file.
    public String ReadFile(String FilePath) {
        try {
            String Newline = "\n";
            String Empty = "";

            String Text = "";
            File Path = new File(FilePath);
            Scanner ScannerObject = new Scanner(Path);

            while (true) {
                if (!ScannerObject.hasNextLine()) {
                    ScannerObject.close();

                    return Text;
                } else if (ScannerObject.hasNextLine()) {
                    Text += (ScannerObject.nextLine() + (ScannerObject.hasNextLine() ? Newline : Empty));
                }
            }
        }

        catch (Exception Error) {
            Message("Some things went wrong while reading a file...", JOptionPane.ERROR_MESSAGE);
            Message(String.format("Exception: \"%s\"", Error.getMessage()), JOptionPane.ERROR_MESSAGE);

            return Text.getText();
        }
    }

    // Write a file.
    public void WriteFile(String FilePath) {
        String TextToWrite = Text.getText();
        File Path = new File(FilePath);

        if (!Path.exists()) {
            try {
                Path.createNewFile();
            }

            catch (Exception Error) {
                Message("Unknown error while creating new file.", JOptionPane.ERROR_MESSAGE);
                Message(String.format("Exception: \"%s\"", Error.getMessage()), JOptionPane.ERROR_MESSAGE);
            }
        }

        try {
            FileWriter FWriter = new FileWriter(FilePath);
            PrintWriter PWriter = new PrintWriter(FWriter);

            PWriter.print(TextToWrite);
            PWriter.close();
        }

        catch (Exception Error) {
            Message("Unknown error while writing file.", JOptionPane.ERROR_MESSAGE);
            Message(String.format("Exception: \"%s\"", Error.getMessage()), JOptionPane.ERROR_MESSAGE);
        }
    }

    // Function to connect to button.
    class ExecuteCommandFunction implements ActionListener {
        public void actionPerformed(ActionEvent Event) {
            String UserCommand = Command.getText();
            String[] UserCommandParsed = UserCommand.split(" : ");
            String CommandToHandle = null;

            try {
                CommandToHandle = UserCommandParsed[0];
            }

            catch (Exception Error) {
                Message("Error while parsing CommandToHandle.", JOptionPane.ERROR_MESSAGE);
                Message(String.format("Exception: \"%s\"", Error.getMessage()), JOptionPane.ERROR_MESSAGE);
            }

            switch (CommandToHandle) {
                case "@r":
                    try {
                        String FirstArgument = UserCommandParsed[1];
                        Text.setText(ReadFile(FirstArgument));
                    }

                    catch (Exception Error) {
                        Message("Unknown error while reading file.", JOptionPane.ERROR_MESSAGE);
                        Message(String.format("Exception: \"%s\"", Error.getMessage()), JOptionPane.ERROR_MESSAGE);
                    }

                    break;

                case "@w":
                    try {
                        String FirstArgument = UserCommandParsed[1];
                        WriteFile(FirstArgument);
                    }

                    catch (Exception Error) {
                        Message("Unknown error while writing file.", JOptionPane.ERROR_MESSAGE);
                        Message(String.format("Exception: \"%s\"", Error.getMessage()), JOptionPane.ERROR_MESSAGE);
                    }

                    break;

                case "@":
                    Message("Missed command name.", JOptionPane.ERROR_MESSAGE);

                    break;

                case "!ex":
                    System.exit(0);
                    break;

                default:
                    Message("Unknown command.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
