package org.ruxlsr.view;

import javax.swing.*;
import java.io.OutputStream;

public class JTextAreaOutputStream extends OutputStream {
    private final JTextArea textArea;

    public JTextAreaOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) {
        textArea.append(String.valueOf((char) b));
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}