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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

public class Program implements Iterable<Command> {
    public static final Program EMPTY = new Program();

    private final Vector<Command> commands;

    private int current;

    private Program() {
        this(Command.MOVE);
    }

    public Program(final Command... commands) {
        this(new Vector<Command>(Arrays.asList(commands)));
    }

    private Program(final Vector<Command> commands) {
        this.commands = commands;
        this.current = 0;

        if (commands.isEmpty()) {
            this.commands.add(Command.SLEEP);
        }
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        return commands.equals(((Program) obj).commands);
    }

    public final void execute(final Field field, final Entity entity) {
        commands.get(current).execute(field, entity);
        current = (current + 1) % commands.size();
    }

    @Override
    public final int hashCode() {
        return commands.hashCode();
    }

    @Override
    public Iterator<Command> iterator() {
        return commands.iterator();
    }

    public final Program mutate() {
        final int mutationRate = Parameter.MUTATION_RATE.getValue();
        if (Parameter.getNextRandomInt(Parameter.PERCENT) >= mutationRate) {
            return new Program(commands);
        }

        /* mutation! */
        final Vector<Command> newCommands = new Vector<Command>(commands);
        final Command c = Command.getRandom();
        final int length = newCommands.size();

        switch (Parameter.getNextRandomInt(1 + 1 + 1)) {
            case 0: /* delete random instruction */
                newCommands.remove(Parameter.getNextRandomInt(length));
                break;

            case 1: /* insert random instruction */
                newCommands.add(Parameter.getNextRandomInt(length + 1), c);
                break;

            case 2: /* change random instruction */
                newCommands.set(Parameter.getNextRandomInt(length), c);
                break;

            default:
                throw new RuntimeException();
        }

        return new Program(newCommands);
    }
}
