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

package genetic.gui;

import genetic.data.Entity;
import genetic.data.Program;

import java.util.HashMap;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

// TODO remove this class, it's ugly.
public class ProgramStatistic implements TableModel {
    private final HashMap<Program, Integer> data;

    private final Vector<TableModelListener> listener;

    public ProgramStatistic() {
        data = new HashMap<Program, Integer>();
        listener = new Vector<TableModelListener>();
    }

    public final void addEntity(final Entity e) {
        final Program p = e.getProgram();

        int amount = 1;
        if (data.containsKey(p)) {
            amount += data.get(p);
        }

        data.put(p, amount);
    }

    @Override
    public final void addTableModelListener(final TableModelListener l) {
        listener.add(l);
    }

    @Override
    public final Class<?> getColumnClass(final int columnIndex) {
        if (columnIndex == 0) {
            return String.class;
        } else {
            return Integer.class;
        }
    }

    @Override
    public final int getColumnCount() {
        return 2;
    }

    @Override
    public final String getColumnName(final int columnIndex) {
        if (columnIndex == 0) {
            return "Program";
        } else {
            return "#";
        }
    }

    @Override
    public final int getRowCount() {
        return data.size();
    }

    @Override
    public final Object getValueAt(final int rowIndex, final int columnIndex) {
        final Object key = data.keySet().toArray()[rowIndex];
        if (columnIndex == 0) {
            return key;
        } else {
            return data.get(key);
        }
    }

    @Override
    public final boolean isCellEditable(
        final int rowIndex,
        final int columnIndex)
    {
        return false;
    }

    public final void removeEntity(final Entity e) {
        final Program p = e.getProgram();
        final int amount = data.get(p) - 1;
        if (amount > 0) {
            data.put(p, amount);
        } else {
            data.remove(p);
        }
    }

    @Override
    public final void removeTableModelListener(final TableModelListener l) {
        listener.remove(l);
    }

    @Override
    public final void setValueAt(
        final Object aValue,
        final int rowIndex,
        final int columnIndex)
    {
    }

    public final void update() {
        for (final TableModelListener l : listener) {
            l.tableChanged(new TableModelEvent(this));
        }
    }
}
