/*
 * GeneticCode - A simple evolving code / automaton sandbox.
 * 
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

package genetic.stat;

import genetic.data.Field;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * A single destination of collected data points. Also contains filtering /
 * formatting information, parsed from the <code>pattern</code> argument in the
 * constructor.
 * 
 * @author Ben Wiederhake
 */
public final class StatisticsFile {
    private final DataOutputStream out;

    private final List<LineElement> elements;

    private final String file;

    /**
     * Whether <code>out.closed()</code> already has been called. Note that
     * during shutdown, multiple hooks might be calling close() simultaneously.
     */
    private boolean outClosed;

    public StatisticsFile(final String file, final String pattern)
        throws IOException
    {
        this.file = file;
        out = new DataOutputStream(new BufferedOutputStream(
            new FileOutputStream(file)));
        elements = LineElement.parse(pattern);
    }

    public static class DataPoint {
        private final int step, population, populationDifference, food;

        public DataPoint(final DataPoint lastData, final Field field) {
            population = field.getEntities().size();
            food = field.getGrass().size();
            step = field.getStep();

            if (lastData == null) {
                populationDifference = population;
            } else {
                populationDifference = population - lastData.population;
            }
        }

        public final int getStep() {
            return step;
        }

        public final int getPopulation() {
            return population;
        }

        public final int getPopulationDifference() {
            return populationDifference;
        }

        public final int getFood() {
            return food;
        }
    }

    public void writeOut(final DataPoint dataPoint) {
        try {
            for (final LineElement lel : elements) {
                lel.writeOut(dataPoint, out);
            }
        } catch (final IOException e) {
            System.out.println("Error while writing datapoint #"
                + dataPoint.getStep() + " to file " + file + ":\n"
                + e.getLocalizedMessage());
        }
    }

    public void flush() {
        try {
            out.flush();
        } catch (final IOException e) {
            System.out.println("Error while flushing file " + file + ":\n"
                + e.getLocalizedMessage());
        }
    }

    public synchronized void close() {
        if (outClosed) {
            /* If the call fails, don't attempt again. */
            outClosed = true;

            try {
                out.close();
            } catch (final IOException e) {
                System.out.println("Error while closing file " + file + ":\n"
                    + e.getLocalizedMessage());
            }
        }
    }
}
