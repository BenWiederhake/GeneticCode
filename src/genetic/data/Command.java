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

/**
 * A single Command that gets aggregated to complex programs.
 * 
 * @author Tim Wiederhake
 */
public enum Command {
    /** Move two steps forward. */
    DOUBLEMOVE {
        @Override
        public void execute(final Field field, final Entity entity) {
            entity.move(field);
            entity.move(field);
        }
    },

    /** Move one step foward. */
    MOVE {
        @Override
        public void execute(final Field field, final Entity entity) {
            entity.move(field);
        }
    },

    /** Turn left. */
    LEFT {
        @Override
        public void execute(final Field field, final Entity entity) {
            entity.left();
        }
    },

    /** Turn right. */
    RIGHT {
        @Override
        public void execute(final Field field, final Entity entity) {
            entity.right();
        }
    },

    /** Do nothing. */
    SLEEP {
        @Override
        public void execute(final Field field, final Entity entity) {
        }
    };

    /**
     * Returns a random Command.
     * 
     * @return a random Command
     */
    public static Command getRandom() {
        return values()[Parameter.getNextRandomInt(values().length)];
    }

    /**
     * Execute this command for the given entity on the given field.
     * 
     * @param field {@link Field}
     * @param entity {@link Entity}
     */
    public abstract void execute(Field field, Entity entity);
}
