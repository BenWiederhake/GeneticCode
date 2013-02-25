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

import genetic.Genetic;
import genetic.data.Command;
import genetic.data.Entity;
import genetic.data.Field;
import genetic.data.Program;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/**
 * A {@link JTable} displaying the simulation's entity statistics.
 * 
 * @author Tim Wiederhake
 */
public class JProgramStatTable
extends JTable
implements TableCellRenderer, TableModel, Observer,
Comparator<Entry<Program, Integer>>
{
    /** Not meant to be serialized. */
    private static final long serialVersionUID = 1L;

    /** Height of a row. */
    private static final int ROWHEIGHT = 20;

    /** Width of first column. */
    private static final int COLUMNWIDTH = 50;

    /** Default size. */
    private static final Dimension DEFAULT_SIZE = new Dimension(300, 150);

    /** TableModelListeners. */
    private final Vector<TableModelListener> listener;

    /** Entity statistics. */
    private Vector<Entry<Program, Integer>> data;

    /** Simulation field. */
    private final Field field;

    /**
     * Create a new JProgramStatTable.
     * 
     * @param field simulation field
     */
    public JProgramStatTable(final Field field) {
        this.listener = new Vector<TableModelListener>();
        this.data = new Vector<Map.Entry<Program, Integer>>();
        this.field = field;

        if (field == null) {
            return;
        }

        field.addObserver(this);

        setModel(this);
        setDefaultRenderer(Program.class, this);
        setRowHeight(ROWHEIGHT);

        /* Why do we have to set MaxWidth as well? */
        getColumnModel().getColumn(0).setPreferredWidth(COLUMNWIDTH);
        getColumnModel().getColumn(0).setMaxWidth(COLUMNWIDTH * 2);
    }

    @Override
    public final void addTableModelListener(final TableModelListener l) {
        listener.add(l);
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public final int compare(
        final Entry<Program, Integer> o1,
        final Entry<Program, Integer> o2)
    {
        return o2.getValue().compareTo(o1.getValue());
    }

    @Override
    public final Class<?> getColumnClass(final int columnIndex) {
        if (columnIndex == 0) {
            return Integer.class;
        } else {
            return Program.class;
        }
    }

    @Override
    public final int getColumnCount() {
        return 2;
    }

    @Override
    public final String getColumnName(final int columnIndex) {
        if (columnIndex == 0) {
            return "#";
        } else {
            return "Program";
        }
    }

    @Override
    public final Dimension getPreferredScrollableViewportSize() {
        return DEFAULT_SIZE;
    }

    @Override
    public final int getRowCount() {
        return data.size();
    }

    @Override
    public final Component getTableCellRendererComponent(
        final JTable table,
        final Object value,
        final boolean isSelected,
        final boolean hasFocus,
        final int row,
        final int column)
    {
        if (value instanceof Program) {
            final Program program = (Program) value;
            final JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            for (final Command c : program) {
                panel.add(new JLabel(Genetic.COMMAND_ICONS.get(c)));
            }

            return panel;
        } else {
            return new JLabel(String.valueOf(value));
        }
    }

    @Override
    public final Object getValueAt(final int rowIndex, final int columnIndex) {
        final Vector<Entry<Program, Integer>> entries = data;

        if (rowIndex >= entries.size()) {
            return null;
        }

        final Entry<Program, Integer> entry = entries.get(rowIndex);

        if (columnIndex == 0) {
            return entry.getValue();
        } else {
            return entry.getKey();
        }
    }

    @Override
    public final boolean isCellEditable(
        final int rowIndex,
        final int columnIndex)
    {
        return false;
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

    @Override
    public final void update(final Observable o, final Object arg) {
        final HashMap<Program, Integer> unsorted =
            new HashMap<Program, Integer>();

        final Vector<Entity> entities = new Vector<Entity>(field.getEntities());
        for (final Entity entity : entities) {
            final Program program = entity.getProgram();
            int amount = 1;
            if (unsorted.containsKey(program)) {
                amount += unsorted.get(program);
            }
            unsorted.put(program, amount);
        }

        final Vector<Entry<Program, Integer>> sorted =
            new Vector<Map.Entry<Program, Integer>>(unsorted.entrySet());
        Collections.sort(sorted, this);

        data = sorted;
        for (final TableModelListener l : listener) {
            l.tableChanged(new TableModelEvent(this));
        }
    }
}
