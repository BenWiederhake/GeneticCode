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

/**
 * GeneticCode main gui frame.
 * 
 * @author Tim Wiederhake
 */
public class GuiFrame extends JFrame implements Runnable {
    /** Not meant to be serialized. */
    private static final long serialVersionUID = 1L;

    /** Default inter-element spacing. */
    public static final int INSET = 5;

    /** Displays the field. */
    private final JFieldPane fieldPane;

    /** Displays the settings. */
    private final JSettingsPane settingsPane;

    /** Displays the status. */
    private final JStatusPane statusPane;

    /**
     * Create a new GuiFrame.
     * 
     * @param field simulation field to display
     */
    public GuiFrame(final Field field) {
        super("Genetic Code");

        this.settingsPane = new JSettingsPane(field, false);
        this.fieldPane = new JFieldPane(field);
        this.statusPane = new JStatusPane(field, this);

        final JScrollPane scrollField = new JScrollPane(
            fieldPane,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        final JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.add(settingsPane, BorderLayout.WEST);
        contentPane.add(scrollField, BorderLayout.CENTER);
        contentPane.add(statusPane, BorderLayout.SOUTH);

        setContentPane(contentPane);
        setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
