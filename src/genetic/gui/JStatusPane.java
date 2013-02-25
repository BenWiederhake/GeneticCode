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

import java.awt.Dimension;
import java.awt.GridLayout;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * This class represents the action executed on pressing the "about" button.
 * 
 * @author Tim Wiederhake
 */
class ActionAboutButton extends JDialog implements ActionListener {
    /** Not meant to be serialized. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new ActionAboutButton.
     * 
     * @param guiFrame parent frame
     */
    public ActionAboutButton(final GuiFrame guiFrame) {
        super(guiFrame);
        setTitle("About GeneticCode");
        setSize(450, 300);
        setLocationRelativeTo(guiFrame);

        final InputStream input = ActionAboutButton.class.getResourceAsStream(
            "/genetic/res/license.html");
        final String text = new Scanner(input).useDelimiter("\\Z").next();
        final JEditorPane editorPane = new JEditorPane("text/html", text);
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        add(new JScrollPane(editorPane));
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        setVisible(true);
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

    /**
     * Create a new ActionHelpButton.
     * 
     * @param guiFrame parent frame
     */
    public ActionHelpButton(final GuiFrame guiFrame) {
        super(guiFrame);
        setTitle("Icon reference");
        setSize(450, 300);
        setLocationRelativeTo(guiFrame);
        setLayout(new GridLayout(0, 1));

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
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        setVisible(true);
    }

    /**
     * Add description for a {@link Command}.
     * 
     * @param c command to describe
     * @param s text
     */
    private void addDesc(final Command c, final String s) {
        final JLabel label = new JLabel(
            s,
            Genetic.COMMAND_ICONS.get(c),
            JLabel.HORIZONTAL);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        add(label);
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
     * @param guiFrame parent frame
     */
    public JStatusPane(final Field field, final GuiFrame guiFrame) {
        this.field = field;
        this.stepField = new JTextField(6);
        this.populationField = new JTextField(6);

        final JLabel stepLabel = new JLabel("Steps: ");
        final JLabel populationLabel = new JLabel("Population: ");

        stepLabel.setLabelFor(stepField);
        stepField.setEditable(false);
        // TODO remove set[Minimum|Maximum|Preferred]Size() calls.
        stepField.setMaximumSize(new Dimension(50, Integer.MAX_VALUE));
        stepField.setHorizontalAlignment(SwingConstants.RIGHT);

        populationLabel.setLabelFor(populationField);
        populationField.setEditable(false);
        // TODO remove set[Minimum|Maximum|Preferred]Size() calls.
        populationField.setMaximumSize(new Dimension(50, Integer.MAX_VALUE));
        populationField.setHorizontalAlignment(SwingConstants.RIGHT);

        final JButton helpButton = new JButton(
            "Help",
            Genetic.loadIcon("/genetic/res/questionmark.png"));
        final JButton aboutButton = new JButton(
            "About",
            Genetic.loadIcon("/genetic/res/businesscard.png"));

        helpButton.addActionListener(new ActionHelpButton(guiFrame));
        helpButton.setFocusPainted(false);
        aboutButton.addActionListener(new ActionAboutButton(guiFrame));
        aboutButton.setFocusPainted(false);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(stepLabel);
        add(stepField);
        add(Box.createHorizontalStrut(GuiFrame.INSET));
        add(populationLabel);
        add(populationField);
        add(Box.createHorizontalGlue());
        add(helpButton);
        add(Box.createHorizontalStrut(GuiFrame.INSET));
        add(aboutButton);

        if (field != null) {
            field.addObserver(this);
            update(null, null);
        }
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
