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

import genetic.stat.StatisticsFile.DataPoint;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public abstract class LineElement {
    public abstract void writeOut(final DataPoint dataPoint,
        final DataOutputStream out) throws IOException;

    /**
     * Lookup table for all the "special", escaped elements, since they don't
     * take arguments.
     */
    /*
     * I didn't even know that new "LineElement['z' + 1]" was possible - but
     * it's elegant, I guess
     */
    private static final LineElement[] SPECIAL_ELEMENTS =
        new LineElement['z' + 1];

    private static final Charset UTF8 = Charset.forName("UTF-8");

    static {
        SPECIAL_ELEMENTS['P'] = new LineElement() {
            @Override
            public void writeOut(
                final DataPoint dataPoint,
                final DataOutputStream out)
            throws IOException
            {
                out.write(String.valueOf(dataPoint.getPopulation()).getBytes(
                    UTF8));
            }
        };
        SPECIAL_ELEMENTS['p'] = new LineElement() {
            @Override
            public void writeOut(
                final DataPoint dataPoint,
                final DataOutputStream out)
            throws IOException
            {
                out.writeInt(dataPoint.getPopulation());
            }
        };
        SPECIAL_ELEMENTS['D'] = new LineElement() {
            @Override
            public void writeOut(
                final DataPoint dataPoint,
                final DataOutputStream out)
            throws IOException
            {
                out.write(String
                    .valueOf(dataPoint.getPopulationDifference())
                    .getBytes(UTF8));
            }
        };
        SPECIAL_ELEMENTS['d'] = new LineElement() {
            @Override
            public void writeOut(
                final DataPoint dataPoint,
                final DataOutputStream out)
            throws IOException
            {
                out.writeInt(dataPoint.getPopulationDifference());
            }
        };
        SPECIAL_ELEMENTS['G'] = new LineElement() {
            @Override
            public void writeOut(
                final DataPoint dataPoint,
                final DataOutputStream out)
            throws IOException
            {
                out.write(String.valueOf(dataPoint.getStep()).getBytes(UTF8));
            }
        };
        SPECIAL_ELEMENTS['g'] = new LineElement() {
            @Override
            public void writeOut(
                final DataPoint dataPoint,
                final DataOutputStream out)
            throws IOException
            {
                out.writeInt(dataPoint.getStep());
            }
        };
        SPECIAL_ELEMENTS['F'] = new LineElement() {
            @Override
            public void writeOut(
                final DataPoint dataPoint,
                final DataOutputStream out)
            throws IOException
            {
                out.write(String.valueOf(dataPoint.getFood()).getBytes(UTF8));
            }
        };
        SPECIAL_ELEMENTS['f'] = new LineElement() {
            @Override
            public void writeOut(
                final DataPoint dataPoint,
                final DataOutputStream out)
            throws IOException
            {
                out.writeInt(dataPoint.getFood());
            }
        };
        SPECIAL_ELEMENTS['%'] = new LineElement() {
            @Override
            public void writeOut(
                final DataPoint dataPoint,
                final DataOutputStream out)
            throws IOException
            {
                out.writeByte('%');
            }
        };
        SPECIAL_ELEMENTS['r'] = new LineElement() {
            @Override
            public void writeOut(
                final DataPoint dataPoint,
                final DataOutputStream out)
            throws IOException
            {
                out.writeByte('\n'); // We want CR, always.
            }
        };
        SPECIAL_ELEMENTS['n'] = new LineElement() {
            @Override
            public void writeOut(
                final DataPoint dataPoint,
                final DataOutputStream out)
            throws IOException
            {
                out.writeByte('\r'); // We want LF, always.
            }
        };
    }

    private static final class Literal extends LineElement {
        private final byte[] literal;

        public Literal(final String literal) {
            super();
            this.literal = literal.getBytes(UTF8);
        }

        @Override
        public void writeOut(final DataPoint dataPoint,
            final DataOutputStream out) throws IOException
        {
            out.write(literal);
        }
    }

    public static final List<LineElement> parse(final String pattern)
    throws IOException
    {
        ArrayList<LineElement> list = new ArrayList<LineElement>();

        int firstNew = 0;
        int escapeIndex = 0;
        while ((escapeIndex = pattern.indexOf('%', firstNew)) != -1) {
            if (firstNew < escapeIndex) {
                list.add(new Literal(pattern.substring(firstNew, escapeIndex)));
            }
            firstNew = escapeIndex + 2;
            if (escapeIndex + 1 < pattern.length()) {
                final char c = pattern.charAt(escapeIndex + 1);
                final LineElement element = SPECIAL_ELEMENTS[c];
                if (element == null) {
                    throw new IOException("Unknown escape code " + ((int) c)
                        + " (character \"" + c + "\") in pattern \""
                        + pattern + "\". Start with --help to see a list"
                        + " of supported escape codes.");
                }
                list.add(element);
            } else {
                throw new IOException("Pattern must not end with escape"
                    + " character (%).\nUse %% to insert a percentage"
                    + " sign, or start with --help to see a full list"
                    + " of supported escape codes.");
            }
        }
        /* Add rest of pattern */
        if (firstNew < pattern.length()) {
            list.add(new Literal(pattern.substring(
                firstNew, pattern.length())));
        }

        return list;
    }
}
