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

package genetic;

import genetic.data.Command;
import genetic.data.Field;
import genetic.data.Parameter;
import genetic.gui.Gui;
import genetic.stat.Statistics;

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

    /**
     * "Global" Statistics object that keeps track of who wants which kind of
     * statistic about what.
     */
    /* Static initialization, but it's an empty constructor */
    private static final Statistics STATISTICS = new Statistics();

    private static boolean displayGui = true;

    private static boolean goFast = false;

    /**
     * When stepLimit is &gt;= 0, then the step number stepLimit+1 won't be
     * reached, because the simulation terminates the VM before that.
     */
    private static int stepLimit = -1;
    
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

        /* Load & prepare (in constructor) */
        final Field field = new Field();
        STATISTICS.update(field);

        /* Show the GUI */
        if (displayGui) {
            final Gui guiFrame = new Gui(field);
            EventQueue.invokeLater(guiFrame);
        }

        /* Start the simulation */
        while (stepLimit < 0 || field.getStep() < stepLimit) {
            if (!goFast) {
                try {
                    final double sleepTime =
                        MS_PER_SECOND / Parameter.SIMULATION_SPEED.getValue();
                    Thread.sleep((int) sleepTime);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }

            field.tick();
            STATISTICS.update(field);
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
        } else if ("--set".equals(flag)) {
            final String paramName = poll(argIter, "--set", 0, 2);
            final int value =
                parseInt(poll(argIter, "--set", 1, 2), "--set", 2);
            /*
             * valueOf() throws IllegalArgumentException if paramName is
             * unknown, and never returns null.
             */
            final Parameter p = Parameter.valueOf(paramName);
            p.setValue(value);
        } else if ("--seed".equals(flag)) {
            Parameter.setRNGSeed(parseLong(
                poll(argIter, "--seed", 0, 1),
                "--seed",
                1));
        } else if (flag.equals("--statistics")) {
            final String pattern = poll(argIter, "--statistics", 0, 2);
            final String filename = poll(argIter, "--statistics", 1, 2);
            try {
                STATISTICS.addStatisticsFile(filename, pattern);
            } catch (IOException e) {
                throw new IllegalArgumentException("Couldn't parse"
                    + " \"--statistics "
                    + pattern
                    + " "
                    + filename
                    + "\" because:\n"
                    + e.getMessage());
            }
        } else if (flag.equals("--automate-for")) {
            /* --automate-for implies --no-gui --go-fast --exit-after */
            displayGui = false;
            goFast = true;
            stepLimit = parseInt(
                poll(argIter, "--automate-for", 0, 1),
                "--automate-for",
                1);
        } else if ("--no-gui".equals(flag)) {
            displayGui = false;
        } else if ("--go-fast".equals(flag)) {
            goFast = true;
        } else if ("--exit-after".equals(flag)) {
            stepLimit = parseInt(
                poll(argIter, "--exit-after", 0, 1),
                "--exit-after",
                1);
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
     * Tries to interpret <code>what</code> as an integer and returns it. If
     * something goes wrong, it throws an exception with information about the
     * error.
     * 
     * @param what the string to parse as an integer
     * @param flag the flag whose argument we are currently parsing. Only used
     *            for error detail information.
     * @param argNo the number of this flag-argument. Only used for error detail
     *            information.
     * @return the represented integer
     * @throws IllegalArgumentException if <code>what</code> did not represent a
     *             valid integer.
     */
    private static int parseInt(
        final String what,
        final String flag,
        final int argNo)
    {
        try {
            return Integer.parseInt(what);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Couldn't parse argument #"
                + argNo
                + " to "
                + flag
                + ":\nExpected integer value, found \""
                + what
                + "\" instead ("
                + e.getLocalizedMessage()
                + ").");
        }
    }

    /**
     * Tries to interpret <code>what</code> as an long and returns it. If
     * something goes wrong, it throws an exception with information about the
     * error.
     * 
     * @param what the string to parse as an long
     * @param flag the flag whose argument we are currently parsing. Only used
     *            for error detail information.
     * @param argNo the number of this flag-argument. Only used for error detail
     *            information.
     * @return the represented long
     * @throws IllegalArgumentException if <code>what</code> did not represent a
     *             valid long.
     */
    private static long parseLong(
        final String what,
        final String flag,
        final int argNo)
    {
        try {
            return Long.parseLong(what);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Couldn't parse argument #"
                + argNo
                + " to "
                + flag
                + ":\nExpected integer value, found \""
                + what
                + "\" instead ("
                + e.getLocalizedMessage()
                + ").");
        }
    }

    /**
     * Polls another flag-argument for the current <code>flag</code> from the
     * given <code>argIter</code> and returns it. If there are not enough
     * commandline-arguments, it throws an exception with information about the
     * error.
     * 
     * @param argIter the {@link Iterator} over commandline-arguments
     * @param flag the flag whose argument we are currently parsing. Only used
     *            for error detail information.
     * @param found the amount of flag-arguments previously found. Only used for
     *            error detail information.
     * @param expected the total amount of flag-arguments this flag requires.
     *            Only used for error detail information.
     * @return the next flag-argument, if there is one.
     * @throws IllegalArgumentException if there is no next argument
     */
    private static String poll(
        final Iterator<String> argIter,
        final String flag,
        final int found,
        final int expected)
    {
        if (argIter.hasNext()) {
            return argIter.next();
        } else if (expected > 1) {
            throw new IllegalArgumentException("Missing argument to "
                + flag
                + " (expected "
                + expected
                + " arguments, found "
                + found
                + ")");
        } else /* if (expected == 1) */{
            throw new IllegalArgumentException(
                "Missing argument to " + flag);
        }
    }

    /**
     * Not meant to be instantiated.
     */
    private Genetic() {
    }
}
