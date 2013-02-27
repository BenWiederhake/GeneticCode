/*
 * GeneticCode - A simple evolving code / automaton sandbox.
 * 
 * Copyright (c) 2013, Tim Wiederhake
 * Copyright (c) 2013, Ben Wiederhake
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

package genetic.data;

import genetic.gui.JFieldPane;

import java.util.Random;
import java.util.Vector;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This simulation's parameter.
 * 
 * This class implements {@link BoundedRangeModel} for convenient access through
 * default java swing sliders etc.
 * 
 * @author Tim Wiederhake
 */
public enum Parameter implements BoundedRangeModel {
    /** Field width. */
    FIELD_WIDTH("Field width", 1, 200, 512, true),

    /** Field height. */
    FIELD_HEIGHT("Field height", 1, 200, 512, true),

    /** How much food there is on the field initially, in percent. */
    INITIAL_FOOD("Initial food on the field in percent", 0, 25, 100, true),

    /** How many walls are there initially, in percent. */
    INITIAL_WALL("Initial walls on the field in percent", 0, 1, 100, true),

    /** The amount of health the first entities will be initialized with */
    INITIAL_HEALTH("Initial health", 1, 100, 150, true),

    /** How big the start population is. */
    INITIAL_POPULATION("Initial population size", 1, 100, 1000, true),

    /** Scale to display the field. */
    FIELD_SCALE("Field scale", 1, 4, 20, true),

    /**
     * How wide the grid should be when displayed.
     * 
     * @see JFieldPane#paint(java.awt.Graphics)
     */
    GRID_GAP("Grid gap width", 0, 1, 3, true),

    /** Whether the grid should be rendered. 0 for "disabled" */
    GRID_VISIBILITY("Grid gap visibility", 0, 1, 1, true),

    /** Simulation speed in ticks per second. */
    SIMULATION_SPEED("Simulation speed in ticks per second", 1, 20, 200, true),

    /** Mutation rate in percent. */
    MUTATION_RATE("Mutation rate in percent", 0, 50, 100, true),

    /** Food regrowth rate in perdeka. */
    REGROWTH_RATE("Regrowth rate in food per 10 ticks", 0, 100, 200, true),

    /** Health per Food. */
    HEALTH_PER_FOOD("Health per Food", 0, 25, 100, true),

    /** Energy loss per step. */
    HEALTH_PER_STEP("Health loss per step", 0, 1, 10, true),

    /** Which energy level triggers reproduction. */
    REPRODUCTION_HP("Reproduction health", 1, 100, 200, true);

    /** *sigh*. */
    public static final int PERCENT = 100;

    /** Use only this random number generator! */
    private static final Random RANDOM = new Random();

    /**
     * @see Random#nextInt(int)
     * @param max see Random nextInt
     * @return see Random nextInt
     */
    public static final int getNextRandomInt(final int max) {
        return RANDOM.nextInt(max);
    }

    public static final void setRNGSeed(long seed) {
        RANDOM.setSeed(seed);
    }

    /** Pretty name. */
    private final String title;

    /** Minimum value. */
    private final int minValue;

    /** Maximum value. */
    private final int maxValue;

    /** Current value. */
    private int value;

    /** Avoid update bursts when dragging a slider. */
    private boolean valueIsAdjusting;

    /** If this parameter is changable during simulation. */
    private boolean mutable;

    /** ChangeListeners. */
    private final Vector<ChangeListener> changeListener;

    /**
     * Create a new Parameter.
     * 
     * @param title pretty name
     * @param minValue minimum value
     * @param value default value
     * @param maxValue maximum value
     * @param mutable changable during simulation or not
     */
    private Parameter(
        final String title,
        final int minValue,
        final int value,
        final int maxValue,
        final boolean mutable)
    {
        this.title = title;
        this.minValue = minValue;
        this.value = value;
        this.maxValue = maxValue;
        this.mutable = mutable;
        this.changeListener = new Vector<ChangeListener>();
    }

    @Override
    public void addChangeListener(final ChangeListener x) {
        changeListener.add(x);
    }

    @Override
    public int getExtent() {
        /* ignore */
        return 0;
    }

    /**
     * Returns the maximum value.
     * 
     * @return the maximum value
     */
    @Override
    public final int getMaximum() {
        return maxValue;
    }

    /**
     * Returns the minimum value.
     * 
     * @return the minimum value
     */
    @Override
    public final int getMinimum() {
        return minValue;
    }

    /**
     * Returns the name.
     * 
     * @return the name
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Returns the current value.
     * 
     * @return the current value
     */
    @Override
    public final int getValue() {
        return value;
    }

    @Override
    public boolean getValueIsAdjusting() {
        return valueIsAdjusting;
    }

    /**
     * Returns if this parameter can be changed during simulation.
     * 
     * @return if this parameter can be changed during simulation
     */
    public final boolean isMutable() {
        return mutable;
    }

    @Override
    public void removeChangeListener(final ChangeListener x) {
        changeListener.remove(x);
    }

    @Override
    public void setExtent(final int newExtent) {
        /* ignore */
    }

    @Override
    public void setMaximum(final int newMaximum) {
        /* ignore */
    }

    @Override
    public void setMinimum(final int newMinimum) {
        /* ignore */
    }

    @Override
    public void setRangeProperties(
        final int val,
        final int extent,
        final int min,
        final int max,
        final boolean adjusting)
    {
        setValueIsAdjusting(adjusting);
        setValue(val);
    }

    @Override
    public final void setValue(final int value) {
        if (!mutable) {
            return;
        }

        this.value = Math.min(maxValue, Math.max(minValue, value));

        for (final ChangeListener x : changeListener) {
            x.stateChanged(new ChangeEvent(this));
        }
    }

    @Override
    public void setValueIsAdjusting(final boolean b) {
        valueIsAdjusting = b;
    }
}
