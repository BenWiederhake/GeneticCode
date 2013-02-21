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
import genetic.data.Parameter;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class JSettingsPane extends JPanel implements Observer {
    private static final long serialVersionUID = 1L;

    public static final int PREFERRED_WIDTH = 300;

    private final Field field;

    private final JLabel stepLabel;

    private final JProgramStatTable programStatTable;

    public JSettingsPane(final Field field, final boolean mutable) {
        this.field = field;
        this.stepLabel = new JLabel("0");
        this.programStatTable = new JProgramStatTable(field);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final JPanel stepPanel = new JPanel();
        stepPanel.add(stepLabel);
        stepPanel.setBorder(new TitledBorder(
            null,
            "Steps",
            TitledBorder.LEADING,
            TitledBorder.TOP,
            null,
            null));

        final JScrollPane scrollTable = new JScrollPane(programStatTable);
        scrollTable.setPreferredSize(new Dimension(
            Integer.MIN_VALUE,
            PREFERRED_WIDTH));

        if (!mutable) {
            add(stepPanel);
        }

        add(Box.createVerticalGlue());
        for (final Parameter p : Parameter.values()) {
            add(new JParameterPane(p, mutable));
        }

        if (!mutable) {
            add(scrollTable);
        }

        if (field != null) {
            field.addObserver(this);
        }
    }

    @Override
    public final void update(final Observable o, final Object arg) {
        stepLabel.setText(String.valueOf(field.getStep()));
    }
}
