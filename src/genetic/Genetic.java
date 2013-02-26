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
import genetic.data.Field;
import genetic.data.Parameter;
import genetic.gui.Gui;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.ImageIcon;

public final class Genetic {
    public static final HashMap<Command, ImageIcon> COMMAND_ICONS =
        new HashMap<Command, ImageIcon>();

    /** Location of the file for the --help text. */
    private static final String USAGE_FILE = "/USAGE.txt";

    /** Location of the file containing the full --license text. */
    private static final String LICENSE_FILE = "/LICENSE.txt";

    private static final double MS_PER_SECOND = 1000.0;

    public static ImageIcon loadIcon(final String path) {
        return new ImageIcon(Genetic.class.getResource(path));
    }

    public static void main(final String[] args) {
        parseArgs(args);

        /* load icons */
        for (final Command c : Command.values()) {
            final ImageIcon image = loadIcon("/genetic/res/"
                + c.toString()
                + ".png");
            COMMAND_ICONS.put(c, image);
        }

        /* load & prepare (in constructor) */
        final Field field = new Field();

        /* show the gui */
        final Gui guiFrame = new Gui(field);
        EventQueue.invokeLater(guiFrame);

        /* start the simulation */
        while (true) {
            try {
                final double sleepTime =
                    MS_PER_SECOND / Parameter.SIMULATION_SPEED.getValue();
                Thread.sleep((int) sleepTime);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }

            field.tick();
        }
    }

    /**
     * Prints out the given (bundled) resource onto the given PrintStream. Does
     * not close <code>out</code>.
     * 
     * @param resource The resource to print
     * @param out Destination of the copy
     */
    private static void printFile(final String resource, final PrintStream out)
    {
        try {
            final InputStream in = Genetic.class.getResourceAsStream(resource);

            int readByte;

            while ((readByte = in.read()) != -1) {
                out.write(readByte);
            }

            in.close();
            /* Don't close out */
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Parses and removes the next argument from the given Iterator.
     * 
     * @param argIter the Iterator instance to use.
     */
    private static void parseNext(final Iterator<String> argIter) {
        final String flag = argIter.next();

        if ("--license".equals(flag)) {
            printFile(LICENSE_FILE, System.out);
            System.exit(0);
        } else if ("--help".equals(flag)) {
            printFile(USAGE_FILE, System.out);
            System.exit(0);
        } else {
            throw new IllegalArgumentException("Unrecognized option: " + flag);
        }
    }

    /**
     * Parses the given command-line arguments. If an error is encountered, or
     * there is any other reason that the program should stop now,
     * <code>System.exit(1)</code> is called.
     * 
     * @param args arguments
     */
    private static void parseArgs(final String[] args) {
        final Iterator<String> argIter = Arrays.asList(args).iterator();

        while (argIter.hasNext()) {
            try {
                parseNext(argIter);
            } catch (IllegalArgumentException e) {
                printFile(USAGE_FILE, System.err);
                System.err.println();
                System.err.println("Couldn't parse the command"
                    + " line arguments:");
                System.err.println(e.getLocalizedMessage());
                System.exit(1);
            } catch (Exception e) {
                printFile(USAGE_FILE, System.err);
                System.err.println();
                System.err.println("Couldn't parse the command"
                    + " line arguments:");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    /**
     * Not meant to be instantiated.
     */
    private Genetic() {
    }
}
