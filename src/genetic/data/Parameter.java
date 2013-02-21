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

package genetic.data;

import java.util.Random;

public enum Parameter {
    /** Field width. */
    FIELD_WIDTH("Field width", 1, 100, 512, false),

    /** Field height. */
    FIELD_HEIGHT("Field height", 1, 100, 512, false),

    /** If the field wraps horizontally. */
    ENDLESS_X("Horizontal Wrapping", 0, 1, 1, false),

    /** If the field wraps vertically. */
    ENDLESS_Y("Vertical Wrapping", 0, 1, 1, false),

    /** How much food there is on the field initially, in percent. */
    INITIAL_FOOD("Initial food on the field in percent", 0, 25, 100, false),

    /** Scale to display the field. */
    FIELD_SCALE("Field scale", 1, 5, 20, true),

    /** Simulation speed in ticks per second. */
    SIMULATION_SPEED("Simulation speed in ticks per second", 1, 20, 100, true),

    /** Mutation rate in percent. */
    MUTATION_RATE("Mutation rate in percent", 0, 50, 100, true),

    /** Food regrowth rate in perdeka. */
    REGROWTH_RATE("Regrowth rate in food per 10 ticks", 0, 40, 100, true),

    /** Health per Food. */
    HEALTH_PER_FOOD("Health per Food", 0, 10, 100, true),

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

    /** Pretty name. */
    private final String title;

    /** Minimum value. */
    private final int minValue;

    /** Maximum value. */
    private final int maxValue;

    /** Current value. */
    private int value;

    /** If this parameter is changable during simulation. */
    private boolean mutable;

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
    }

    /**
     * Returns the maximum value.
     * 
     * @return the maximum value
     */
    public final int getMaxValue() {
        return maxValue;
    }

    /**
     * Returns the minimum value.
     * 
     * @return the minimum value
     */
    public final int getMinValue() {
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
    public final int getValue() {
        return value;
    }

    public final boolean isMutable() {
        return mutable;
    }

    public final void setValue(final int value) {
        if (mutable) {
            this.value = value;
        }
    }
}
