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

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 * {@link JPanel} displaying the simulation's settings.
 * 
 * @author Tim Wiederhake
 */
public class JSettingsPane extends JPanel implements Scrollable {
    /** Not meant to be serialized. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new JSettingsPane.
     * 
     * @param mutable change read-only parameters anyway
     */
    public JSettingsPane(final boolean mutable) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (final Parameter p : Parameter.values()) {
            add(new JParameterPane(p, mutable));
        }
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public final Dimension getPreferredScrollableViewportSize() {
        return null;
    }

    @Override
    public final int getScrollableBlockIncrement(
        final Rectangle visibleRect,
        final int orientation,
        final int direction)
    {
        return getComponent(0).getHeight();
    }

    @Override
    public final boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public final boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public final int getScrollableUnitIncrement(
        final Rectangle visibleRect,
        final int orientation,
        final int direction)
    {
        return getComponent(0).getHeight();
    }
}
