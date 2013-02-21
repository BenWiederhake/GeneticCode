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
import java.util.HashSet;
import java.util.Observable;
import java.util.Vector;

public class Field extends Observable {
    private final HashSet<Point> grass;

    private final HashSet<Point> wall;

    private final Vector<Entity> entities;

    private int timeUntilAddRandomGrass;

    private int step;

    public Field() {
        this.grass = new HashSet<Point>();
        this.wall = new HashSet<Point>();
        this.entities = new Vector<Entity>();
    }

    public final void addEntity(final Entity e) {
        entities.add(e);
    }

    public final void addRandomGrass() {
        grass.add(getRandomValidPoint());
    }

    public final Vector<Entity> getEntities() {
        return entities;
    }

    public final HashSet<Point> getGrass() {
        return grass;
    }

    public final Point getRandomValidPoint() {
        final int width = Parameter.FIELD_WIDTH.getValue();
        final int height = Parameter.FIELD_HEIGHT.getValue();
        final int x = Parameter.getNextRandomInt(width);
        final int y = Parameter.getNextRandomInt(height);
        return new Point(x, y);
    }

    public final int getStep() {
        return step;
    }

    public Point getTargetCoordinates(final Entity entity) {
        final Point origin = entity.getPosition();
        final Direction direction = entity.getDirection();
        return sanitizeCoordinates(direction.getCoordinate(origin));
    }

    public final HashSet<Point> getWall() {
        return wall;
    }

    public final boolean isGrass(final Point p) {
        return grass.contains(sanitizeCoordinates(p));
    }

    public final boolean isWalkable(final Point p) {
        final Point point = sanitizeCoordinates(p);

        if (point == null) {
            return false;
        }

        if (wall.contains(point)) {
            return false;
        }

        return true;
    }

    public final boolean removeGrass(final Point point) {
        return grass.remove(point);
    }

    /* wrap around corners etc.; returns null if illegal coordinate */
    public final Point sanitizeCoordinates(final Point p) {
        final int width = Parameter.FIELD_WIDTH.getValue();
        final int height = Parameter.FIELD_HEIGHT.getValue();
        if (p.x < 0) {
            return sanitizeCoordinates(new Point(p.x + width, p.y));
        }

        if (p.x >= width) {
            return sanitizeCoordinates(new Point(p.x - width, p.y));
        }

        if (p.y < 0) {
            return sanitizeCoordinates(new Point(p.x, p.y + height));
        }

        if (p.y >= height) {
            return sanitizeCoordinates(new Point(p.x, p.y - height));
        }

        return p;
    }

    public final void tick() {
        step += 1;

        if (--timeUntilAddRandomGrass < 0) {
            timeUntilAddRandomGrass = 10;
            for (int i = 0; i < Parameter.REGROWTH_RATE.getValue(); ++i) {
                addRandomGrass();
            }
        }

        final Vector<Entity> dead = new Vector<Entity>();
        final Vector<Entity> born = new Vector<Entity>();

        for (final Entity e : entities) {
            e.step(this);
            if (e.getHealth() <= 0) {
                dead.add(e);
            } else if (e.getHealth() > Parameter.REPRODUCTION_HP.getValue()) {
                born.add(e);
            }
        }

        entities.removeAll(dead);

        for (final Entity e : born) {
            addEntity(e.replicate(this));
        }

        setChanged();
        notifyObservers();
    }
}
