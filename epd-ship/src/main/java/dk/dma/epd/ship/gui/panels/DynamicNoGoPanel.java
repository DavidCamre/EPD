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
package dk.dma.epd.ship.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * GPS panel in sensor panel
 */
public class DynamicNoGoPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel nogoTitleLabel = new JLabel("Dynamic NoGo");
    private JLabel statusTitleLabel = new JLabel("Status");
    private JLabel statusLabel = new JLabel("N/A");
    private final JLabel statLabel2 = new JLabel("N/A");
    private final JLabel statLabel1 = new JLabel("N/A");
    private final JLabel statLabel3 = new JLabel("N/A");
    private final JLabel statLabel6 = new JLabel("N/A");
    private final JLabel lblNewLabel = new JLabel("Valid From");
    private final JLabel lblNewLabel_1 = new JLabel("Valid to");
    private final JLabel lblNewLabel_2 = new JLabel("Own Draught");
    private final JLabel statLabel7 = new JLabel("N/A");
    private final JLabel navWarning1 = new JLabel("Do not use this for");
    private final JLabel navWarning2 = new JLabel("navigational purposes");
    private final JLabel lblNewLabel_3 = new JLabel("Target Draught");
    private final JLabel statLabel4 = new JLabel("N/A");
    private final JLabel statLabel5 = new JLabel("N/A");

    public DynamicNoGoPanel() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 10, 10 };
        gridBagLayout.rowHeights = new int[] { 20, 16, 15, 0, 0, 0, 0, 0, 0, 0,
                0, 10 };
        gridBagLayout.columnWeights = new double[] { 1.0, 1.0 };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        setLayout(gridBagLayout);
        nogoTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nogoTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        GridBagConstraints gbc_nogoTitleLabel = new GridBagConstraints();
        gbc_nogoTitleLabel.anchor = GridBagConstraints.NORTH;
        gbc_nogoTitleLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_nogoTitleLabel.insets = new Insets(0, 0, 5, 0);
        gbc_nogoTitleLabel.gridwidth = 2;
        gbc_nogoTitleLabel.gridx = 0;
        gbc_nogoTitleLabel.gridy = 0;
        add(nogoTitleLabel, gbc_nogoTitleLabel);

        statusTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_statusTitleLabel = new GridBagConstraints();
        gbc_statusTitleLabel.anchor = GridBagConstraints.NORTHWEST;
        gbc_statusTitleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_statusTitleLabel.gridx = 0;
        gbc_statusTitleLabel.gridy = 1;
        add(statusTitleLabel, gbc_statusTitleLabel);

        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_statusLabel = new GridBagConstraints();
        gbc_statusLabel.anchor = GridBagConstraints.NORTHWEST;
        gbc_statusLabel.insets = new Insets(0, 0, 5, 0);
        gbc_statusLabel.gridx = 1;
        gbc_statusLabel.gridy = 1;
        add(statusLabel, gbc_statusLabel);

        lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 2;
        add(lblNewLabel, gbc_lblNewLabel);

        statLabel1.setHorizontalAlignment(SwingConstants.LEFT);
        statLabel1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_statLabel1 = new GridBagConstraints();
        gbc_statLabel1.anchor = GridBagConstraints.NORTHWEST;
        gbc_statLabel1.insets = new Insets(0, 0, 5, 0);
        gbc_statLabel1.gridx = 1;
        gbc_statLabel1.gridy = 2;
        add(statLabel1, gbc_statLabel1);

        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 0;
        gbc_lblNewLabel_1.gridy = 3;
        add(lblNewLabel_1, gbc_lblNewLabel_1);
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel_1.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        statLabel2.setHorizontalAlignment(SwingConstants.LEFT);
        statLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_statLabel2 = new GridBagConstraints();
        gbc_statLabel2.anchor = GridBagConstraints.NORTHWEST;
        gbc_statLabel2.insets = new Insets(0, 0, 5, 0);
        gbc_statLabel2.gridx = 1;
        gbc_statLabel2.gridy = 3;
        add(statLabel2, gbc_statLabel2);

        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
        gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_2.gridx = 0;
        gbc_lblNewLabel_2.gridy = 4;
        add(lblNewLabel_2, gbc_lblNewLabel_2);
        lblNewLabel_2.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel_2.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        statLabel3.setHorizontalAlignment(SwingConstants.LEFT);
        statLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_statLabel3 = new GridBagConstraints();
        gbc_statLabel3.anchor = GridBagConstraints.NORTHWEST;
        gbc_statLabel3.insets = new Insets(0, 0, 5, 0);
        gbc_statLabel3.gridx = 1;
        gbc_statLabel3.gridy = 4;
        add(statLabel3, gbc_statLabel3);

        GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
        gbc_lblNewLabel_3.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
        gbc_lblNewLabel_3.gridx = 0;
        gbc_lblNewLabel_3.gridy = 5;
        add(lblNewLabel_3, gbc_lblNewLabel_3);
        lblNewLabel_3.setHorizontalAlignment(SwingConstants.LEFT);
        lblNewLabel_3.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        GridBagConstraints gbc_statLabel4 = new GridBagConstraints();
        gbc_statLabel4.anchor = GridBagConstraints.NORTHWEST;
        gbc_statLabel4.insets = new Insets(0, 0, 5, 0);
        gbc_statLabel4.gridx = 1;
        gbc_statLabel4.gridy = 5;
        add(statLabel4, gbc_statLabel4);
        statLabel4.setHorizontalAlignment(SwingConstants.LEFT);
        statLabel4.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        GridBagConstraints gbc_statLabel5 = new GridBagConstraints();
        gbc_statLabel5.anchor = GridBagConstraints.NORTH;
        gbc_statLabel5.gridwidth = 2;
        gbc_statLabel5.insets = new Insets(0, 0, 5, 5);
        gbc_statLabel5.gridx = 0;
        gbc_statLabel5.gridy = 6;
        add(statLabel5, gbc_statLabel5);
        statLabel5.setHorizontalAlignment(SwingConstants.LEFT);
        statLabel5.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        
        statLabel6.setHorizontalAlignment(SwingConstants.LEFT);
        statLabel6.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_statLabel6 = new GridBagConstraints();
        gbc_statLabel6.gridwidth = 2;
        gbc_statLabel6.insets = new Insets(0, 0, 5, 0);
        gbc_statLabel6.anchor = GridBagConstraints.NORTH;
        gbc_statLabel6.gridx = 0;
        gbc_statLabel6.gridy = 7;
        add(statLabel6, gbc_statLabel6);

        statLabel7.setHorizontalAlignment(SwingConstants.LEFT);
        statLabel7.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_statLabel7 = new GridBagConstraints();
        gbc_statLabel7.gridwidth = 2;
        gbc_statLabel7.insets = new Insets(0, 0, 5, 0);
        gbc_statLabel7.gridx = 0;
        gbc_statLabel7.gridy = 8;
        add(statLabel7, gbc_statLabel7);
        navWarning1.setForeground(Color.RED);

        navWarning1.setHorizontalAlignment(SwingConstants.LEFT);
        navWarning1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        GridBagConstraints gbc_navWarning1 = new GridBagConstraints();
        gbc_navWarning1.gridwidth = 2;
        gbc_navWarning1.anchor = GridBagConstraints.NORTH;
        gbc_navWarning1.insets = new Insets(0, 0, 5, 0);
        gbc_navWarning1.gridx = 0;
        gbc_navWarning1.gridy = 9;
        add(navWarning1, gbc_navWarning1);

        GridBagConstraints gbc_navWarning2 = new GridBagConstraints();
        gbc_navWarning2.anchor = GridBagConstraints.NORTH;
        gbc_navWarning2.gridwidth = 2;
        gbc_navWarning2.gridx = 0;
        gbc_navWarning2.gridy = 10;
        add(navWarning2, gbc_navWarning2);
        navWarning2.setForeground(Color.RED);

        navWarning2.setHorizontalAlignment(SwingConstants.LEFT);
        navWarning2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    public JLabel getStatusTitleLabel() {
        return statusTitleLabel;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public JLabel getStatLabel2() {
        return statLabel2;
    }

    public JLabel getStatLabel1() {
        return statLabel1;
    }

    public JLabel getStatLabel3() {
        return statLabel3;
    }

    public JLabel getStatLabel4() {
        return statLabel4;
    }

    public JLabel getNavWarning1() {
        return navWarning1;
    }

    public JLabel getNavWarning2() {
        return navWarning2;
    }

    public JLabel getStatLabel5() {
        return statLabel5;
    }

    public JLabel getStatLabel6() {
        return statLabel6;
    }

    public JLabel getStatLabel7() {
        return statLabel7;
    }


    
    
    
}
