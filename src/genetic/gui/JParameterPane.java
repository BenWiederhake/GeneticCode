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

import genetic.data.Parameter;

import java.awt.BorderLayout;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * {@link JPanel} displaying the simulation's {@link Parameter}.
 * 
 * @author Tim Wiederhake
 */
public class JParameterPane extends JPanel implements ChangeListener {
    /** Not meant to be serialized. */
    private static final long serialVersionUID = 1L;

    /** The parameter this Panel represents. */
    private final Parameter parameter;

    /** Label displaying the current value. */
    private final JLabel label;

    /** Slider to adjust this parameter's value. */
    private final JSlider slider;

    /**
     * Create a new JParameterPane.
     * 
     * @param parameter this pane's parameter
     */
    public JParameterPane(final Parameter parameter) {
        this(parameter, false);
    }

    /**
     * Create a new JParameterPane that might be allowed to adjust an parameter
     * even though it is not mutable. TODO This method should be removed.
     * 
     * @param parameter this pane's parameter
     * @param adjust if this pane is allowed to change that parameter
     */
    public JParameterPane(final Parameter parameter, final boolean adjust) {
        this.parameter = parameter;

        setLayout(new BorderLayout(GuiFrame.INSET, 0));

        setBorder(new TitledBorder(
            null,
            parameter.getTitle(),
            TitledBorder.LEADING,
            TitledBorder.TOP,
            null,
            null));

        final Hashtable<Integer, JLabel> labelTable =
            new Hashtable<Integer, JLabel>();

        labelTable.put(
            parameter.getMinimum(),
            new JLabel(String.valueOf(parameter.getMinimum())));
        labelTable.put(
            parameter.getMaximum(),
            new JLabel(String.valueOf(parameter.getMaximum())));

        slider = new JSlider(parameter);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setPaintLabels(true);
        slider.setMinorTickSpacing(1);
        slider.setLabelTable(labelTable);

        if (parameter.isMutable() || adjust) {
            slider.addChangeListener(this);
        } else {
            slider.setEnabled(false);
        }

        label = new JLabel();
        label.setText(String.valueOf(parameter.getValue()));

        add(slider, BorderLayout.CENTER);
        add(label, BorderLayout.LINE_END);
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public final void stateChanged(final ChangeEvent e) {
        final int value = slider.getValue();
        parameter.setValue(value);
        label.setText(String.valueOf(value));
    }
}
