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

    private static final Color[] ENTITY_SHADES = new Color[256];

    static {
        for (int i = 0; i < ENTITY_SHADES.length; i++) {
            ENTITY_SHADES[i] = new Color(i, 0, 0);
        }
    }

    /** Simulation field. */
    private final Field field;

    /** Last known display scale. */
    private int scale;

    /** Last known grid gap. */
    private int gridGap;

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
     * Draw an single cell correctly scaled and translated on the current
     * {@link Graphics2D} object.
     * 
     * @param g2 current graphics object
     * @param p point to draw
     */
    private void drawRectangle(final Graphics2D g2, final Point p) {
        g2.fillRect(
            gridGap + (p.x * scale), gridGap + (p.y * scale),
            scale - gridGap, scale - gridGap);
    }

    @Override
    public final Dimension getPreferredSize() {
        final int width = Parameter.FIELD_WIDTH.getValue() * scale;
        final int height = Parameter.FIELD_HEIGHT.getValue() * scale;

        return new Dimension(
            width + gridGap + (Gui.GAP * 2),
            height + gridGap + (Gui.GAP * 2));
    }

    /**
     * Paints the field by rendering the grid, then rendering grass, wall and
     * cell. To clarify the defined terms for the measurements:
     * 
     * <pre>
     * |--                  Whole field                    --|
     *       |--   scale   --|--   scale   --|--   scale   --|
     * |--G--|--  C  --|--G--|--  C  --|--G--|--  C  --|--G--|
     * |--   scale   --|--   scale   --|--   scale   --|
     * </pre>
     * 
     * Where G is the GRID_GAP, and C is the actual "paintable" width per cell.
     * Same rules apply for height. The GRID_GAP is not capped, but paint()
     * won't internally use higher values for it than FIELD_SCALE - 1. <br />
     * The two "scale" lines will always exhibit this useful behavior; plus is
     * commutative: G + C = C + G for all integer G, C
     */
    @Override
    public final void paint(final Graphics g) {
        final int currentScale = Parameter.FIELD_SCALE.getValue();
        /* Always leave at least 1 pixel of display */
        final int currentGridGap =
            Math.min(Parameter.GRID_GAP.getValue(), scale - 1);

        /*
         * If the size or something that affects it changed, revalidate before
         * repainting.
         */
        if (scale != currentScale || gridGap != currentGridGap) {
            scale = currentScale;
            gridGap = currentGridGap;
            revalidate();
            repaint();
            return;
        }

        super.paint(g);
        final Graphics2D g2 = (Graphics2D) g;
        final int fieldWidth = Parameter.FIELD_WIDTH.getValue();
        final int fieldHeight = Parameter.FIELD_HEIGHT.getValue();

        g2.translate(Gui.GAP, Gui.GAP);

        /* grid */
        if (Parameter.GRID_VISIBILITY.getValue() != 0 && gridGap > 0) {
            final int widthpx = fieldWidth * scale;
            final int heightpx = fieldHeight * scale;

            g2.setColor(Color.BLACK);
            for (int i = 0; i < (fieldWidth + 1); ++i) {
                final int ipx = i * scale;
                g2.fillRect(ipx, 0, gridGap, heightpx);
            }

            for (int i = 0; i < (fieldHeight + 1); ++i) {
                final int ipx = i * scale;
                g2.fillRect(0, ipx, widthpx, gridGap);
            }
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

        /* entities */
        final int lastReproductionHP = Parameter.REPRODUCTION_HP.getValue();
        for (final Entity e : field.getEntities()) {
            final int relativeHealth = (e.getHealth() *
                (ENTITY_SHADES.length - 1)) / lastReproductionHP;
            final int index = Math.min(ENTITY_SHADES.length - 1,
                Math.max(relativeHealth, 0));
            g2.setColor(ENTITY_SHADES[index]);
            drawRectangle(g2, e.getPosition());
        }
    }

    @Override
    public final void update(final Observable o, final Object arg) {
        repaint();
    }
}
