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
import genetic.stat.StatisticsFile.DataPoint;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Collection of all "statistics destinations" ({@link StatisticsFile}). Cares
 * about whether any data has to be collected, and that cleanup (flushing,
 * closing) happens before shutdown.
 * 
 * @author Ben Wiederhake
 */
public class Statistics extends Thread {
    private final List<StatisticsFile> statisticFiles =
        new LinkedList<StatisticsFile>();

    /**
     * Store last dataPoint (or <code>null</code>) to be able to accumulate
     * data, if desired.
     */
    private DataPoint dataPoint = null;

    /**
     * Whether the shutdown hook already is registered. Shutdown-hooks are
     * registered lazily, because they cause impose a delay between desire to
     * shutdown and the JVM halting.
     */
    private boolean hookRegistered = false;

    public final
    void
    addStatisticsFile(final String file, final String pattern)
    throws IOException
    {
        statisticFiles.add(new StatisticsFile(file, pattern));
    }

    public void update(final Field field) {
        /* Don't collect statistics if not required */
        if (statisticFiles.isEmpty()) {
            return;
        }
        if (!hookRegistered) {
            hookRegistered = true;
            Runtime.getRuntime().addShutdownHook(this);
        }
        dataPoint = new DataPoint(dataPoint, field);
        for (StatisticsFile statFile : statisticFiles) {
            statFile.writeOut(dataPoint);
        }
    }

    @Override
    public final void run() {
        /* When used as shutdown hook */
        close();
    }

    public final void flush() {
        for (StatisticsFile statFile : statisticFiles) {
            statFile.flush();
        }
    }

    public final void close() {
        for (StatisticsFile statFile : statisticFiles) {
            statFile.close();
        }
    }
}
