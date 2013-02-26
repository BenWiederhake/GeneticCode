/*
 * GeneticCode - A simple evolving code / automaton sandbox.
 * 
 * Copyright (c) 2013, Tim Wiederhake
 * Copyright (c) 2013, Ben Wiederhake
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
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
        addDesc(Genetic.loadIcon(JStatusPane.IMG_PAUSE),
            "Pause/Unpause Simulation (or Ctrl-P)");
        addDesc(Genetic.loadIcon(JStatusPane.IMG_RESET),
        "Reset Simulation (or Ctrl-R)");
        addDesc(Genetic.loadIcon(JStatusPane.IMG_GO_FAST),
            "Ignore simulation speed settings: " +
                "Go as fast as possible.");

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
        addDesc(Genetic.COMMAND_ICONS.get(c), text);
    }

    /**
     * Add description for anything with an ImageIcon.
     * 
     * @param icon accompanying icon describe
     * @param text description
     */
    private void addDesc(final ImageIcon icon, final String text) {
        final JLabel label = new JLabel(
            "= " + text,
            icon,
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

    /** Location of the "pause" icon */
    static final String IMG_PAUSE = "/genetic/res/pause.png";

    /** Location of the "go fast" icon */
    static final String IMG_GO_FAST = "/genetic/res/goFast.png";

    /** Location of the "reset" icon */
    static final String IMG_RESET = "/genetic/res/reset.png";

    /** Location of the "help" icon */
    static final String IMG_HELP = "/genetic/res/questionmark.png";

    /** Location of the "about" icon */
    static final String IMG_ABOUT = "/genetic/res/businesscard.png";

    /** Static width of textfields in characters. */
    private static final int TEXTFIELD_WIDTH = 6;

    /** Simulation field. */
    private final Field field;

    /** Display the simulation steps. */
    private final JTextField stepField;

    /** Display the simulation's total population. */
    private final JTextField populationField;

    /**
     * Display the simulation's total population difference to the last time we
     * could display it.
     */
    private final JTextField populationDiffField;

    /** The total population to the last time we could display it. */
    private int lastPopulation;

    /**
     * Create a new JStatusPane.
     * 
     * @param field simulation field
     * @param parentFrame parent frame
     */
    public JStatusPane(final Field field, final JFrame parentFrame) {
        if (field == null) {
            throw new NullPointerException();
        }

        this.field = field;
        this.stepField = new JTextField(TEXTFIELD_WIDTH);
        this.populationField = new JTextField(TEXTFIELD_WIDTH);
        this.lastPopulation = 0;
        this.populationDiffField = new JTextField(TEXTFIELD_WIDTH);

        stepField.setEditable(false);
        stepField.setHorizontalAlignment(SwingConstants.RIGHT);

        populationField.setEditable(false);
        populationField.setHorizontalAlignment(SwingConstants.RIGHT);

        populationDiffField.setEditable(false);
        populationDiffField.setHorizontalAlignment(SwingConstants.LEFT);

        field.addObserver(this);
        update(null, null);

        final JLabel stepLabel = new JLabel("Steps: ");
        final JLabel populationLabel = new JLabel("Population: ");

        stepLabel.setLabelFor(stepField);
        populationLabel.setLabelFor(populationField);

        final JToggleButton pauseButton = new JToggleButton(
            "Pause",
            Genetic.loadIcon(IMG_PAUSE),
            Genetic.isPaused());
        pauseButton.setFocusPainted(false);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Genetic.setPaused(pauseButton.isSelected());
            }
        });

        final JToggleButton goFastButton = new JToggleButton(
            "Go Fast",
            Genetic.loadIcon(IMG_GO_FAST),
            Genetic.isGoFast());
        goFastButton.setFocusPainted(false);
        goFastButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Genetic.setGoFast(goFastButton.isSelected());
            }
        });

        final JButton resetButton = new JButton(
            "Reset",
            Genetic.loadIcon(IMG_RESET));
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.reset();
            }
        });

        final JButton helpButton = new JButton(
            "Help",
            Genetic.loadIcon(IMG_HELP));
        helpButton.addActionListener(new ActionHelpButton(parentFrame));
        helpButton.setFocusPainted(false);

        final JButton aboutButton = new JButton(
            "About",
            Genetic.loadIcon(IMG_ABOUT));
        aboutButton.addActionListener(new ActionAboutButton(parentFrame));
        aboutButton.setFocusPainted(false);

        final JPanel labelPane = new JPanel();
        labelPane.add(stepLabel);
        labelPane.add(stepField);
        labelPane.add(Box.createHorizontalStrut(Gui.GAP * 2));
        labelPane.add(populationLabel);
        labelPane.add(populationField);
        labelPane.add(populationDiffField);

        final JPanel buttonPane = new JPanel();
        buttonPane.add(pauseButton);
        buttonPane.add(Box.createHorizontalStrut(Gui.GAP * 2));
        buttonPane.add(goFastButton);
        buttonPane.add(Box.createHorizontalStrut(Gui.GAP * 2));
        buttonPane.add(resetButton);
        buttonPane.add(Box.createHorizontalStrut(Gui.GAP * 2));
        buttonPane.add(helpButton);
        buttonPane.add(Box.createHorizontalStrut(Gui.GAP * 2));
        buttonPane.add(aboutButton);

        setLayout(new BorderLayout());
        add(labelPane, BorderLayout.WEST);
        add(buttonPane, BorderLayout.EAST);
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }


    @Override
    public final void update(final Observable o, final Object arg) {
        stepField.setText(String.valueOf(field.getStep()));
        final int newPopulation = field.getEntities().size();
        populationField.setText(String.valueOf(newPopulation));
        /* Display "0" when population stays same */
        final String sign = (newPopulation > lastPopulation) ? "+" : "";
        populationDiffField.setText("("
            + sign
            + (newPopulation - lastPopulation)
            + ")");
        lastPopulation = newPopulation;
    }
}
