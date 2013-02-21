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

import java.awt.Point;

/**
 * Directions on the Field.
 * 
 * @author Tim Wiederhake
 */
public enum Direction {
    /** North. */
    UP(0, -1),

    /** East. */
    RIGHT(1, 0),

    /** South. */
    DOWN(0, 1),

    /** West. */
    LEFT(-1, 0);

    public static Direction getRandom() {
        return values()[Parameter.getNextRandomInt(values().length)];
    }

    public static Direction left(final Direction d) {
        switch (d) {
            case UP:
                return LEFT;
            case LEFT:
                return DOWN;
            case DOWN:
                return RIGHT;
            case RIGHT:
                return UP;
            default:
                throw new RuntimeException();
        }
    }

    public static Direction right(final Direction d) {
        switch (d) {
            case UP:
                return RIGHT;
            case RIGHT:
                return DOWN;
            case DOWN:
                return LEFT;
            case LEFT:
                return UP;
            default:
                throw new RuntimeException();
        }
    }

    private final int x, y;

    private Direction(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public Point getCoordinate(final Point origin) {
        return new Point(origin.x + x, origin.y + y);
    }

}
