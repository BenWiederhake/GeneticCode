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

public class Entity {
    /** Program this entity follows. */
    private final Program program;

    /** Location on the field. */
    private Point position;

    /** Heading. */
    private Direction direction;

    /** Energy. */
    private int health;

    public Entity(
        final int health,
        final Program program,
        final Point position,
        final Direction direction)
    {
        this.health = health;
        this.program = program;
        this.position = position;
        this.direction = direction;
    }

    /**
     * Returns this entity's direction.
     * 
     * @return this entity's direction
     */
    public final Direction getDirection() {
        return direction;
    }

    /**
     * Returns this entity's energy.
     * 
     * @return this entity's energy
     */
    public final int getHealth() {
        return health;
    }

    /**
     * Returns this entity's position.
     * 
     * @return this entity's position
     */
    public final Point getPosition() {
        return position;
    }

    /**
     * Returns this entity's program.
     * 
     * @return this entity's program
     */
    public final Program getProgram() {
        return program;
    }

    /**
     * Rotate this entity to the left. This is always possible.
     */
    public final void left() {
        direction = Direction.left(direction);
    }

    /**
     * Make a single step forward if possible. This might not always be the
     * case.
     * 
     * @param f field to move on
     */
    public final void move(final Field f) {
        final Point goal =
            f.sanitizeCoordinates(direction.getCoordinate(position));

        if (!f.isWalkable(goal)) {
            return;
        }

        if (f.isGrass(goal)) {
            health += Parameter.HEALTH_PER_FOOD.getValue();
            f.removeGrass(goal);
        }

        position = goal;
    }

    public final Entity replicate(final Field field) {
        final Point newPosition = field.getRandomValidPoint();
        final Direction newDirection = Direction.getRandom();
        final Program newProgram = program.mutate();
        health /= 2;

        return new Entity(health, newProgram, newPosition, newDirection);
    }

    /**
     * Rotate this entity to the right. This is always possible.
     */
    public final void right() {
        direction = Direction.right(direction);
    }

    public final void step(final Field field) {
        program.execute(field, this);
        if (field.isGrass(position)) {
            health += Parameter.HEALTH_PER_FOOD.getValue();
            field.removeGrass(position);
        }
        health -= Parameter.HEALTH_PER_STEP.getValue();
    }
}
