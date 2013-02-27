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
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

/**
 * GeneticCode main gui.
 * 
 * @author Tim Wiederhake
 */
public class Gui implements Runnable, KeyEventDispatcher {
    /** Default space between elements. */
    public static final int GAP = 5;

    /** Simulation field. */
    private final Field field;

    /**
     * Create a new GuiFrame.
     * 
     * @param field simulation field to display
     */
    public Gui(final Field field) {
        this.field = field;
    }

    @Override
    public final boolean dispatchKeyEvent(final KeyEvent e) {
        /* Prevent accidental commands by requiring CTRL to be held */
        if (!e.isControlDown()) {
            /* We have not consumed the event */
            return false;
        }

        /* Only register each command ONCE */
        if (e.getID() != KeyEvent.KEY_RELEASED) {
            /* We have not consumed the event */
            return false;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_Q:
            case KeyEvent.VK_W:
                /*
                 * Using JFrame.EXIT_ON_CLOSE will result in exactly the same
                 * call. Since we are using that flag, we should halt the
                 * application the same way anything else would.
                 */
                System.exit(0);
                return true;
            default:
                /* We have not consumed the event */
                return false;
        }
    }

    @Override
    public final void run() {
        final JFrame frame = new JFrame("Genetic Code");

        final JFieldPane fieldPane = new JFieldPane(field);
        final JSettingsPane settingsPane = new JSettingsPane(false);
        final JProgramStatTable programStatTable = new JProgramStatTable(field);
        final JStatusPane statusPane = new JStatusPane(field, frame);

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

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(this);
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
