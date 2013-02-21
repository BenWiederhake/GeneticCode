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

package genetic;

import genetic.data.Command;
import genetic.data.Direction;
import genetic.data.Entity;
import genetic.data.Field;
import genetic.data.Parameter;
import genetic.data.Program;
import genetic.gui.GuiFrame;

import java.awt.EventQueue;
import java.util.HashMap;

import javax.swing.ImageIcon;

public final class Genetic {
    public static final HashMap<Command, ImageIcon> COMMAND_ICONS =
        new HashMap<Command, ImageIcon>();

    public static void main(final String[] args) {
        final Field field = new Field();

        /* load icons */
        for (final Command c : Command.values()) {
            final ImageIcon image = new ImageIcon(
                Genetic.class.getResource("/genetic/res/"
                    + c.toString()
                    + ".png"));
            COMMAND_ICONS.put(c, image);
        }

        /* place initial food */
        final int foodCount =
            (Parameter.FIELD_WIDTH.getValue()
                * Parameter.FIELD_HEIGHT.getValue()
                * Parameter.INITIAL_FOOD.getValue()) / Parameter.PERCENT;

        for (int i = 0; i < foodCount; ++i) {
            field.addRandomGrass();
        }

        /* place initial walls */
        final int wallCount =
            (Parameter.FIELD_WIDTH.getValue()
                * Parameter.FIELD_HEIGHT.getValue()
                * Parameter.INITIAL_WALL.getValue()) / Parameter.PERCENT;

        for (int i = 0; i < wallCount; ++i) {
            field.addRandomWall();
        }

        /* place initial population */
        for (int i = 0; i < Parameter.INITIAL_POPULATION.getValue(); ++i) {
            field.addEntity(new Entity(
                100,
                new Program(Command.MOVE),
                field.getRandomValidPoint(),
                Direction.getRandom()));
        }

        /* show the gui */
        final GuiFrame guiFrame = new GuiFrame(field);
        EventQueue.invokeLater(guiFrame);

        /* start the simulation */
        while (true) {
            try {
                final double sleepTime =
                    1000.0 / Parameter.SIMULATION_SPEED.getValue();
                Thread.sleep((int) sleepTime);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }

            field.tick();
        }
    }

    /**
     * Not meant to be instanciated.
     */
    private Genetic() {
    }
}
