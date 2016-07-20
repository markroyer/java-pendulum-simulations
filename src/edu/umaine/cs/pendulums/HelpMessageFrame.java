/**
 * 
 */
package edu.umaine.cs.pendulums;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * A frame that contains a text explanation about how to use the user interface.
 * The text is loaded from the file "help.txt" in the same package.
 * 
 * @author Mark Royer
 * 
 */
public class HelpMessageFrame extends JFrame {

    /**
     * For serializability
     */
    private static final long serialVersionUID = 2018306336607038021L;

    /**
     * Create a new window that contains helpful information about how to use
     * the graphical interface.
     */
    public HelpMessageFrame() {
        super("Pendulum Simulation - Help");
        Dimension d = new Dimension(600, 300);
        this.setSize(d);
        this.setPreferredSize(d);
        this.setResizable(false);
        this.setAlwaysOnTop(true);

        this.setLayout(new GridLayout(1, 1));

        try {

            InputStream in = this.getClass().getResource("help.txt")
                    .openStream();

            String sep = System.getProperty("line.separator");

            BufferedReader buf = new BufferedReader(new InputStreamReader(in));

            StringBuffer str = new StringBuffer();

            while (buf.ready()) {
                str.append(buf.readLine() + sep);
            }

            JTextArea textArea = new JTextArea();
            textArea.setFont(new Font("Courier", Font.PLAIN, 12));
            textArea.setText(str.toString());
            textArea.setEditable(false);
            textArea.setCaretPosition(0);

            JScrollPane scrollPane = new JScrollPane(textArea);

            this.add(scrollPane);

            this.pack();
            this.validate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

}
