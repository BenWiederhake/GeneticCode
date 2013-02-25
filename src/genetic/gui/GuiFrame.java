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

import genetic.data.Field;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

/**
 * GeneticCode main gui frame.
 * 
 * @author Tim Wiederhake
 */
public class GuiFrame extends JFrame implements Runnable {
    /** Not meant to be serialized. */
    private static final long serialVersionUID = 1L;

    /** Default space between elements. */
    public static final int GAP = 5;

    /** Displays the field. */
    private final JFieldPane fieldPane;

    /** Displays the settings. */
    private final JSettingsPane settingsPane;

    /** Displays the program statistics. */
    private final JProgramStatTable programStatTable;

    /** Displays the status. */
    private final JStatusPane statusPane;

    /**
     * Create a new GuiFrame.
     * 
     * @param field simulation field to display
     */
    public GuiFrame(final Field field) {
        super("Genetic Code");

        this.settingsPane = new JSettingsPane(false);
        this.fieldPane = new JFieldPane(field);
        this.statusPane = new JStatusPane(field, this);
        this.programStatTable = new JProgramStatTable(field);

        final JScrollPane scrollSettings = new JScrollPane(
            settingsPane,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final JScrollPane scrollTable = new JScrollPane(
            programStatTable,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final JScrollPane scrollField = new JScrollPane(
            fieldPane,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        final JSplitPane verticalSplit = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            true,
            scrollTable,
            scrollSettings
            );

        final JSplitPane horizontalSplit = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            true,
            verticalSplit,
            scrollField);

        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(GAP, GAP, GAP, GAP));
        panel.add(horizontalSplit, BorderLayout.CENTER);
        panel.add(statusPane, BorderLayout.SOUTH);

        setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        pack();
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public final void run() {
        setVisible(true);
    }
}
