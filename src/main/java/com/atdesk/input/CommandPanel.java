package com.atdesk.input;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;

public class CommandPanel extends JPanel {
    private final JTextArea logArea;
    private final JTextField inputField;

    public CommandPanel(CommandProcessor processor) {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createTitledBorder("Controller Commands"));

        logArea = new JTextArea(8, 40);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        inputField = new JTextField();
        inputField.addActionListener(event -> {
            String input = inputField.getText();
            String response = processor.handleCommand(input);
            appendLine("> " + input);
            appendLine(response);
            inputField.setText("");
        });

        JPanel inputWrapper = new JPanel();
        inputWrapper.setLayout(new BoxLayout(inputWrapper, BoxLayout.Y_AXIS));
        inputWrapper.add(inputField);

        add(scrollPane, BorderLayout.CENTER);
        add(inputWrapper, BorderLayout.SOUTH);
    }

    private void appendLine(String line) {
        logArea.append(line + System.lineSeparator());
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}
