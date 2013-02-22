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

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class JSettingsPane extends JPanel {
    private static final long serialVersionUID = 1L;

    public static final int PREFERRED_WIDTH = 300;

    private final JProgramStatTable programStatTable;

    public JSettingsPane(final Field field, final boolean mutable) {
        this.programStatTable = new JProgramStatTable(field);

        final JScrollPane scrollTable = new JScrollPane(programStatTable);
        scrollTable.setPreferredSize(new Dimension(
            PREFERRED_WIDTH,
            PREFERRED_WIDTH / 2));
        scrollTable.setMaximumSize(new Dimension(
            PREFERRED_WIDTH,
            PREFERRED_WIDTH));

        final JPanel parameterPanel = new JPanel();
        parameterPanel.setLayout(
            new BoxLayout(parameterPanel, BoxLayout.Y_AXIS));

        for (final Parameter p : Parameter.values()) {
            parameterPanel.add(new JParameterPane(p, mutable));
        }

        final JScrollPane scrollSettings = new JScrollPane(
            parameterPanel,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollSettings.getVerticalScrollBar().setUnitIncrement(GuiFrame.INSET);
        scrollSettings.setPreferredSize(new Dimension(
            JSettingsPane.PREFERRED_WIDTH,
            0));


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(scrollSettings);
        if (!mutable) {
            add(scrollTable);
        }
    }
}
