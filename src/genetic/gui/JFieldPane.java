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

import genetic.data.Entity;
import genetic.data.Field;
import genetic.data.Parameter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

/**
 * {@link JPanel} displaying the simulation's {@link Field}.
 * 
 * @author Tim Wiederhake
 */
public class JFieldPane extends JPanel implements Observer {
    /** Not meant to be serialized. */
    private static final long serialVersionUID = 1L;

    /** Simulation field. */
    private final Field field;

    /** Last known display scale. */
    private int scale;

    /**
     * Create a new JFieldPane.
     * 
     * @param field simulation field
     */
    public JFieldPane(final Field field) {
        this.field = field;
        this.scale = Parameter.FIELD_SCALE.getValue();

        setOpaque(true);
        if (field != null) {
            field.addObserver(this);
        }
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Draw an single point correctly scaled on the current {@link Graphics2D}
     * object.
     * 
     * @param g2 current graphics object
     * @param p point to draw
     */
    private void drawRectangle(final Graphics2D g2, final Point p) {
        g2.fillRect(1 + (p.x * scale), 1 + (p.y * scale), scale - 1, scale - 1);
    }

    @Override
    public final Dimension getPreferredSize() {
        final int width = Parameter.FIELD_WIDTH.getValue() * scale;
        final int height = Parameter.FIELD_HEIGHT.getValue() * scale;

        return new Dimension(
            width + 1 + (GuiFrame.GAP * 2),
            height + 1 + (GuiFrame.GAP * 2));
    }

    @Override
    public final void paint(final Graphics g) {
        final int currentScale = Parameter.FIELD_SCALE.getValue();

        /* if the scale changed, revalidate bevore repainting. */
        if (scale != currentScale) {
            scale = currentScale;
            revalidate();
            repaint();
            return;
        }

        super.paint(g);
        final Graphics2D g2 = (Graphics2D) g;
        final int fieldWidth = Parameter.FIELD_WIDTH.getValue();
        final int fieldHeight = Parameter.FIELD_HEIGHT.getValue();

        final int widthpx = fieldWidth * scale;
        final int heightpx = fieldHeight * scale;

        g2.translate(GuiFrame.GAP, GuiFrame.GAP);

        /* grid */
        g2.setColor(Color.BLACK);
        for (int i = 0; i < (fieldWidth + 1); ++i) {
            final int ipx = i * scale;
            g2.drawLine(ipx, 0, ipx, heightpx);
        }

        for (int i = 0; i < (fieldHeight + 1); ++i) {
            final int ipx = i * scale;
            g2.drawLine(0, ipx, widthpx, ipx);
        }

        if (field == null) {
            return;
        }

        /* grass */
        g2.setColor(Color.GREEN);
        for (final Point p : field.getGrass()) {
            drawRectangle(g2, p);
        }

        /* wall */
        g2.setColor(Color.GRAY);
        for (final Point p : field.getWall()) {
            drawRectangle(g2, p);
        }

        /* entites */
        g2.setColor(Color.RED);
        for (final Entity e : field.getEntities()) {
            drawRectangle(g2, e.getPosition());
        }
    }

    @Override
    public final void update(final Observable o, final Object arg) {
        repaint();
    }
}
