/*
 * GeneticCode - A simple evolving code / automaton sandbox.
 * 
 * Copyright (c) 2013, Tim Wiederhake
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package genetic.gui;

import genetic.Genetic;
import genetic.data.Command;
import genetic.data.Field;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * This class represents the action executed on pressing the "about" button.
 * 
 * @author Tim Wiederhake
 */
class ActionAboutButton extends JDialog implements ActionListener {
    /** Not meant to be serialized. */
    private static final long serialVersionUID = 1L;

    /** Default dialog size. */
    private static final Dimension DEFAULT_SIZE = new Dimension(500, 300);

    /** Dialog visibility. */
    private boolean visible = false;

    /**
     * Create a new ActionAboutButton.
     * 
     * @param parentFrame parent frame
     */
    public ActionAboutButton(final JFrame parentFrame) {
        super(parentFrame);

        final InputStream input = ActionAboutButton.class.getResourceAsStream(
            "/genetic/res/license.html");
        final String text;
        {
            final Scanner scanner = new Scanner(input);
            scanner.useDelimiter("\\Z");
            text = scanner.next();
            scanner.close();
        }
        final JEditorPane editorPane = new JEditorPane("text/html", text);
        editorPane.setEditable(false);
        editorPane.setOpaque(false);

        final JButton closeButton = new JButton("Close");
        closeButton.addActionListener(this);

        final JPanel buttonPane = new JPanel();
        buttonPane.add(closeButton);

        add(new JScrollPane(editorPane), BorderLayout.CENTER);
        add(buttonPane, BorderLayout.SOUTH);

        setTitle("About GeneticCode");
        setSize(DEFAULT_SIZE);
        setLocationRelativeTo(parentFrame);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        visible = !visible;
        setVisible(visible);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}


/**
 * This class represents the action executed on pressing the "help" button.
 * 
 * @author user
 * 
 */
class ActionHelpButton extends JDialog implements ActionListener {
    /** Not meant to be serialized. */
    private static final long serialVersionUID = 1L;

    /** Default dialog size. */
    private static final Dimension DEFAULT_SIZE = new Dimension(500, 300);

    /** Dialog visibility. */
    private boolean visible = false;

    /** Holds the conent of this dialog. */
    private final JPanel contentPane;

    /**
     * Create a new ActionHelpButton.
     * 
     * @param parentFrame parent frame
     */
    public ActionHelpButton(final JFrame parentFrame) {
        super(parentFrame);
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBorder(new EmptyBorder(
            Gui.GAP,
            Gui.GAP,
            Gui.GAP,
            Gui.GAP));

        addDesc(Command.DOUBLEMOVE, "Move two steps forward");
        addDesc(Command.MOVE, "Move one step forward");
        addDesc(Command.LEFT, "Turn left");
        addDesc(Command.RIGHT, "Turn right");
        addDesc(
            Command.IFENTITY,
            "Skips the next command if the entity faces another entity");
        addDesc(
            Command.IFFOOD,
            "Skips the next command if the entity faces food");
        addDesc(
            Command.IFWALL,
            "Skips the next command if the entity faces a wall");
        addDesc(Command.SKIP, "Skip one command");
        addDesc(Command.SKIP2, "Skip two commands");
        addDesc(Command.SLEEP, "Do nothing");
        addDesc("Ctrl-Q, Ctrl-W = Close the program immediately.");

        final JButton closeButton = new JButton("Close");
        closeButton.addActionListener(this);

        final JPanel buttonPane = new JPanel();
        buttonPane.add(closeButton);

        add(new JScrollPane(contentPane), BorderLayout.CENTER);
        add(buttonPane, BorderLayout.SOUTH);
        setTitle("Icon reference");
        setSize(DEFAULT_SIZE);
        setLocationRelativeTo(parentFrame);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        visible = !visible;
        setVisible(visible);
    }

    /**
     * Add description for a {@link Command}.
     * 
     * @param c command to describe
     * @param text description
     */
    private void addDesc(final Command c, final String text) {
        final JLabel label = new JLabel(
            "= " + text,
            Genetic.COMMAND_ICONS.get(c),
            JLabel.LEFT);
        contentPane.add(label);
        contentPane.add(Box.createVerticalStrut(Gui.GAP));
    }

    /**
     * Add custom description text (e.g. for a keybinding)
     * 
     * @param text description
     */
    private void addDesc(final String text) {
        final JLabel label = new JLabel(
            text,
            JLabel.LEFT);
        contentPane.add(label);
        contentPane.add(Box.createVerticalStrut(Gui.GAP));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}

/**
 * {@link JPanel} displaying the simulation's status.
 * 
 * @author Tim Wiederhake
 */
public class JStatusPane extends JPanel implements Observer {
    /** Not meant to be serialized. */
    private static final long serialVersionUID = 1L;

    /** Static width of textfields in characters. */
    private static final int TEXTFIELD_WIDTH = 6;

    /** Simulation field. */
    private final Field field;

    /** Display the simulation steps. */
    private final JTextField stepField;

    /** Display the simulation's total population. */
    private final JTextField populationField;

    /**
     * Create a new JStatusPane.
     * 
     * @param field simulation field
     * @param parentFrame parent frame
     */
    public JStatusPane(final Field field, final JFrame parentFrame) {
        this.stepField = new JTextField(TEXTFIELD_WIDTH);
        this.populationField = new JTextField(TEXTFIELD_WIDTH);
        this.field = field;
        if (field != null) {
            field.addObserver(this);
            update(null, null);
        }

        stepField.setEditable(false);
        stepField.setHorizontalAlignment(SwingConstants.RIGHT);

        populationField.setEditable(false);
        populationField.setHorizontalAlignment(SwingConstants.RIGHT);

        final JLabel stepLabel = new JLabel("Steps: ");
        final JLabel populationLabel = new JLabel("Population: ");

        stepLabel.setLabelFor(stepField);
        populationLabel.setLabelFor(populationField);

        final JButton helpButton = new JButton(
            "Help",
            Genetic.loadIcon("/genetic/res/questionmark.png"));
        final JButton aboutButton = new JButton(
            "About",
            Genetic.loadIcon("/genetic/res/businesscard.png"));

        helpButton.addActionListener(new ActionHelpButton(parentFrame));
        helpButton.setFocusPainted(false);
        aboutButton.addActionListener(new ActionAboutButton(parentFrame));
        aboutButton.setFocusPainted(false);

        final JPanel labelPane = new JPanel();
        labelPane.add(stepLabel);
        labelPane.add(stepField);
        labelPane.add(Box.createHorizontalStrut(Gui.GAP * 2));
        labelPane.add(populationLabel);
        labelPane.add(populationField);

        final JPanel buttonPane = new JPanel();
        buttonPane.add(helpButton);
        buttonPane.add(Box.createHorizontalStrut(Gui.GAP * 2));
        buttonPane.add(aboutButton);

        setLayout(new BorderLayout());
        add(labelPane, BorderLayout.WEST);
        add(Box.createGlue(), BorderLayout.CENTER);
        add(buttonPane, BorderLayout.EAST);
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public final void update(final Observable o, final Object arg) {
        stepField.setText(String.valueOf(field.getStep()));
        populationField.setText(String.valueOf(field.getEntities().size()));
    }
}
