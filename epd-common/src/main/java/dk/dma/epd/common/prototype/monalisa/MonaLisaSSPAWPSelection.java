/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.epd.common.prototype.monalisa;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import dk.dma.epd.common.prototype.model.route.Route;


public class MonaLisaSSPAWPSelection extends JDialog implements ActionListener,
        ListSelectionListener, TableModelListener, MouseListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTable routeTable;
    private MonaLisaSSPASelectionTableModel routesTableModel;
    private ListSelectionModel routeSelectionModel;
    List<Boolean> selectedWp;
    Route route;
    JButton okButton;
    JButton cancelButton;
    MonaLisaSSPAOptionsDialogCommon parent;

    public MonaLisaSSPAWPSelection(MonaLisaSSPAOptionsDialogCommon parent, Route route, List<Boolean> selectedWp) {
        super(parent, "Waypoint Selection", true);
        
        this.parent = parent;
        this.route = route;
        this.selectedWp = selectedWp;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
//        setResizable(false);
        
//        setBounds(100, 100, 450, 300);
        setSize(450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        routeTable = new JTable();
        routesTableModel = new MonaLisaSSPASelectionTableModel(route, selectedWp);
        routesTableModel.addTableModelListener(this);
        routeTable.setShowHorizontalLines(false);
        routeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        routeSelectionModel = routeTable.getSelectionModel();
        routeSelectionModel.addListSelectionListener(this);
        routeTable.setSelectionModel(routeSelectionModel);
        routeTable.addMouseListener(this);

        getContentPane().add(contentPanel, BorderLayout.CENTER);
        {
            JScrollPane scrollPane = new JScrollPane(routeTable);
            scrollPane
                    .setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane
                    .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            contentPanel.add(scrollPane);
            
            scrollPane.setPreferredSize(new Dimension(400, 200));
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                okButton = new JButton("OK");
                okButton.addActionListener(this);
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
            {
                cancelButton = new JButton("Reset");
                cancelButton.addActionListener(this);
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
            }
        }
        
        routeTable.setModel(routesTableModel);
        for (int i = 0; i < 1; i++) {
            if (i == 1) {
                routeTable.getColumnModel().getColumn(i).setPreferredWidth(10);
            } else {
                routeTable.getColumnModel().getColumn(i).setPreferredWidth(290);
            }
        }
        
        updateTable();
    }

    
    public void updateTable() {
        int selectedRow = routeTable.getSelectedRow();
        // Update routeTable
        routesTableModel.fireTableDataChanged();
        // routeTable.doLayout();
        if (selectedRow >= 0 && selectedRow < routeTable.getRowCount()) {
            routeSelectionModel.setSelectionInterval(selectedRow, selectedRow);
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void tableChanged(TableModelEvent arg0) {
        if (arg0.getColumn() == 1) {
//            System.out.println("Changed inclusion");
//            selectedWp.
            // Visibility has changed
//            routeManager
//                    .notifyListeners(RoutesUpdateEvent.ROUTE_VISIBILITY_CHANGED);
        }
        
    }

    @Override
    public void valueChanged(ListSelectionEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == cancelButton) {
            parent.resetSelected();
            this.dispose();
        }
        
        if (arg0.getSource() == okButton) {
            parent.updateSelected();
            this.dispose();
            
//            System.out.println("Time to update?");
//            for (int i = 0; i < selectedWp.size(); i++) {
//                System.out.println(i + " : " + selectedWp.get(i));
//            }
//            parent.printSelected();
        }
        
    }

}
