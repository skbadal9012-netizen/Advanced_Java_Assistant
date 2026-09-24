package com.assistant.ui;

import com.assistant.model.ParsedCommand;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Function;

public class AssistantUI extends JFrame {
    private JTextPane chatPane;
    private JTextField inputField;
    private JButton sendButton;
    private JButton settingsButton;
    private Function<String, String> commandHandler;
    private StringBuilder chatHtml;

    private final Color BG_DARK = new Color(18, 18, 18);
    private final Color SURFACE_DARK = new Color(30, 30, 30);
    private final Color TEXT_LIGHT = new Color(232, 236, 245);
    private final Color ACCENT_COLOR = new Color(0, 172, 193);

    public AssistantUI(Function<String, String> commandHandler) {
        this.commandHandler = commandHandler;
        this.chatHtml = new StringBuilder();

        setTitle("Alexa");
        setSize(550, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        setupStyles();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_DARK);

        // Top Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(SURFACE_DARK);
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Alexa");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_LIGHT);

        settingsButton = new JButton("⚙ Settings");
        settingsButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        settingsButton.setBackground(new Color(45, 45, 45));
        settingsButton.setForeground(TEXT_LIGHT);
        settingsButton.setFocusPainted(false);
        settingsButton.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        settingsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        settingsButton.addActionListener(e -> showSettingsDialog());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(settingsButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Chat Pane
        chatPane = new JTextPane();
        chatPane.setContentType("text/html");
        chatPane.setEditable(false);
        chatPane.setBackground(BG_DARK);
        
        JScrollPane scrollPane = new JScrollPane(chatPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_DARK);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Input Panel
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(SURFACE_DARK);
        inputPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBackground(new Color(45, 45, 45));
        inputField.setForeground(TEXT_LIGHT);
        inputField.setCaretColor(TEXT_LIGHT);
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    processUserInput();
                }
            }
        });

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(ACCENT_COLOR);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> processUserInput());

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        // Initial Greeting
        appendMessage("Alexa", "Hello! I am Alexa, your PC Assistant. Type commands to control your PC or ask questions.");
    }

    private void setupStyles() {
        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body { font-family: 'Segoe UI', sans-serif; color: #E8ECF5; font-size: 14px; margin: 10px; background-color: #121212; }");
        styleSheet.addRule(".msg-user { background-color: #00695C; color: #FFFFFF; padding: 10px 14px; border-radius: 12px; margin: 8px 0; max-width: 80%; float: right; clear: both; }");
        styleSheet.addRule(".msg-ai { background-color: #303030; color: #E8ECF5; padding: 10px 14px; border-radius: 12px; margin: 8px 0; max-width: 80%; float: left; clear: both; }");
        styleSheet.addRule(".sender { font-weight: bold; font-size: 11px; margin-bottom: 3px; color: #B0BEC5; }");
        chatPane.setEditorKit(kit);
    }

    private void appendMessage(String sender, String message) {
        String cssClass = sender.equalsIgnoreCase("You") ? "msg-user" : "msg-ai";
        chatHtml.append("<div class='").append(cssClass).append("'>");
        chatHtml.append("<div class='sender'>").append(sender).append("</div>");
        chatHtml.append("<div>").append(escapeHtml(message)).append("</div>");
        chatHtml.append("</div>");
        
        chatPane.setText("<html><body>" + chatHtml.toString() + "</body></html>");
        chatPane.setCaretPosition(chatPane.getDocument().getLength());
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;");
    }

    private void processUserInput() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;

        appendMessage("You", text);
        inputField.setText("");

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                try {
                    return commandHandler.apply(text);
                } catch (Exception e) {
                    return "Error executing command: " + e.getMessage();
                }
            }

            @Override
            protected void done() {
                try {
                    String response = get();
                    appendMessage("Alexa", response);
                } catch (Exception e) {
                    appendMessage("Alexa", "Error: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void showSettingsDialog() {
        JOptionPane.showMessageDialog(this, 
            "Alexa Settings\nVoice Control: Enabled\nPC Controller: Active", 
            "Settings", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
