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
package dk.dma.epd.ship.gui.setuptabs;

import static dk.dma.epd.common.prototype.settings.SensorSettings.SensorConnectionType.FILE;
import static dk.dma.epd.common.prototype.settings.SensorSettings.SensorConnectionType.SERIAL;
import static dk.dma.epd.common.prototype.settings.SensorSettings.SensorConnectionType.TCP;
import static dk.dma.epd.common.prototype.settings.SensorSettings.SensorConnectionType.UDP;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dk.dma.epd.common.prototype.gui.settings.BaseSettingsPanel;
import dk.dma.epd.common.prototype.gui.settings.ISettingsListener.Type;
import dk.dma.epd.common.prototype.settings.SensorSettings;
import dk.dma.epd.common.prototype.settings.SensorSettings.PntSourceSetting;
import dk.dma.epd.common.prototype.settings.SensorSettings.SensorConnectionType;
import dk.dma.epd.ship.EPDShip;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.ImageIcon;

/**
 * 
 * @author adamduehansen
 *
 */
public class ShipSensorSettingsPanel extends BaseSettingsPanel implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JTextField textFieldGpsFilename;
    private JTextField textFieldGpsHostOrSerialPort;
    private JTextField textFieldMsPntFileName;
    private JTextField textFieldMsPntHostOrSerialPort;
    private JComboBox<PntSourceSetting> comboBoxPntSource;
    private JComboBox<SensorConnectionType> comboBoxGPSConnectionType;
    private JComboBox<SensorConnectionType> comboBoxMsPntConnectionType;
    private SensorSettings settings;
    private JSpinner spinnerGpsPort;
    private JTextField textFieldAisFilename;
    private JTextField textFieldAisHostOrSerialPort;
    private JComboBox<SensorConnectionType> comboBoxAisConnectionType;
    private JPanel aisConnectionPanel;
    private JPanel gpsConnectionPanel;
    private JPanel msPntConnectionPanel;
    private JSpinner spinnerAisPort;
    private JSpinner spinnerMsPntPort;
    private JCheckBox chckbxStartTransponder;
    private JSpinner spinnerAisSensorRange;
    private JSpinner spinnerMsPntHal;
    private JCheckBox cbMsPntTrueHeadingFromCog;

    /**
     * Constructs a new ShipSensorSettingsPanel object.
     */
    public ShipSensorSettingsPanel() {
        super("Sensor", new ImageIcon(ShipSensorSettingsPanel.class.getResource
                ("/images/settingspanels/sensor.png")));
        this.setLayout(null);
        
        
        /************** PNT Source ***************/

        JLabel lblPntSource = new JLabel("PNT Source");
        lblPntSource.setBounds(16, 20, 71, 16);
        this.add(lblPntSource);
        
        this.comboBoxPntSource = new JComboBox<PntSourceSetting>(
                new DefaultComboBoxModel<>(PntSourceSetting.values()));
        this.comboBoxPntSource.addActionListener(this);
        this.comboBoxPntSource.setBounds(99, 19, 180, 20);
        this.add(this.comboBoxPntSource);
        
        
        /************** AIS Connection ***************/
        
        this.aisConnectionPanel = new JPanel();
        this.aisConnectionPanel.setBounds(6, 48, 438, 136);
        this.aisConnectionPanel.setLayout(null);
        this.aisConnectionPanel.setBorder(new TitledBorder(
                null, "AIS Connection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        
        // AIS connection panel components.
        JLabel lblAisConnectionType = new JLabel("Connection Type");
        lblAisConnectionType.setBounds(16, 22, 106, 16);
        this.aisConnectionPanel.add(lblAisConnectionType);
        
        this.comboBoxAisConnectionType = new JComboBox<SensorConnectionType>(
                new DefaultComboBoxModel<>(SensorConnectionType.values()));
        this.comboBoxAisConnectionType.addActionListener(this);
        this.comboBoxAisConnectionType.setBounds(134, 21, 139, 20);
        this.aisConnectionPanel.add(this.comboBoxAisConnectionType);
        
        this.textFieldAisHostOrSerialPort = new JTextField();
        this.textFieldAisHostOrSerialPort.setColumns(10);
        this.textFieldAisHostOrSerialPort.setBounds(134, 46, 139, 20);
        this.aisConnectionPanel.add(this.textFieldAisHostOrSerialPort);
        
        JLabel lblAisHostOrSerialPort = new JLabel("Host or serial port");
        lblAisHostOrSerialPort.setBounds(16, 47, 110, 16);
        this.aisConnectionPanel.add(lblAisHostOrSerialPort);
        
        JLabel lblAisfileName = new JLabel("AIS-file name");
        lblAisfileName.setBounds(16, 73, 89, 16);
        this.aisConnectionPanel.add(lblAisfileName);
        
        this.textFieldAisFilename = new JTextField();
        this.textFieldAisFilename.setColumns(10);
        this.textFieldAisFilename.setBounds(134, 71, 139, 20);
        this.aisConnectionPanel.add(this.textFieldAisFilename);
        
        this.spinnerAisPort = new JSpinner();
        this.spinnerAisPort.setEditor(new NumberEditor(this.spinnerAisPort, "#"));
        this.spinnerAisPort.setBounds(134, 96, 139, 20);
        this.aisConnectionPanel.add(this.spinnerAisPort);
        
        JLabel lblAisPort = new JLabel("Port");
        lblAisPort.setBounds(16, 98, 61, 16);
        this.aisConnectionPanel.add(lblAisPort);
        
        this.add(this.aisConnectionPanel);
        
        
        /************** GPS Connection ***************/

        this.gpsConnectionPanel = new JPanel();
        this.gpsConnectionPanel.setBounds(6, 196, 438, 134);
        this.gpsConnectionPanel.setLayout(null);
        this.gpsConnectionPanel.setBorder(new TitledBorder(
                null, "GPS Connection", TitledBorder.LEADING, TitledBorder.TOP, null, null));

        // GPS connection panel components 
        JLabel lblGpsConnectionType = new JLabel("Connection Type");
        lblGpsConnectionType.setBounds(16, 20, 106, 16);
        this.gpsConnectionPanel.add(lblGpsConnectionType);
        
        this.comboBoxGPSConnectionType = new JComboBox<SensorConnectionType>(
                new DefaultComboBoxModel<>(SensorConnectionType.values()));
        this.comboBoxGPSConnectionType.addActionListener(this);
        this.comboBoxGPSConnectionType.setBounds(134, 19, 139, 20);
        this.gpsConnectionPanel.add(this.comboBoxGPSConnectionType);
        
        JLabel lblGpsHostOrSerialPort = new JLabel("Host or serial port");
        lblGpsHostOrSerialPort.setBounds(16, 45, 110, 16);
        this.gpsConnectionPanel.add(lblGpsHostOrSerialPort);
        
        JLabel lblGpsfileName = new JLabel("GPS-file name");
        lblGpsfileName.setBounds(16, 71, 89, 16);
        this.gpsConnectionPanel.add(lblGpsfileName);
        
        this.textFieldGpsFilename = new JTextField();
        this.textFieldGpsFilename.setBounds(134, 69, 139, 20);
        this.gpsConnectionPanel.add(this.textFieldGpsFilename);
        this.textFieldGpsFilename.setColumns(10);
        
        JLabel lblGpsPort = new JLabel("Port");
        lblGpsPort.setBounds(16, 96, 61, 16);
        this.gpsConnectionPanel.add(lblGpsPort);
        
        this.spinnerGpsPort = new JSpinner();
        this.spinnerGpsPort.setEditor(new NumberEditor(this.spinnerGpsPort, "#"));
        this.spinnerGpsPort.setBounds(134, 94, 139, 20);
        this.gpsConnectionPanel.add(this.spinnerGpsPort);
        
        this.textFieldGpsHostOrSerialPort = new JTextField();
        this.textFieldGpsHostOrSerialPort.setColumns(10);
        this.textFieldGpsHostOrSerialPort.setBounds(134, 44, 139, 20);
        this.gpsConnectionPanel.add(this.textFieldGpsHostOrSerialPort);
        
        this.add(gpsConnectionPanel);
        
        
        /************** Multi-source PNT Connection ***************/

        this.msPntConnectionPanel = new JPanel();
        this.msPntConnectionPanel.setBounds(6, 342, 438, 145);
        this.msPntConnectionPanel.setLayout(null);
        this.msPntConnectionPanel.setBorder(new TitledBorder(
                null, "Multi-source PNT Connection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        
        JLabel lblPntSourceConnectionType = new JLabel("Connection Type");
        lblPntSourceConnectionType.setBounds(16, 20, 106, 16);
        this.msPntConnectionPanel.add(lblPntSourceConnectionType);
        
        this.comboBoxMsPntConnectionType = new JComboBox<SensorConnectionType>(
                new DefaultComboBoxModel<>(SensorConnectionType.values()));
        this.comboBoxMsPntConnectionType.addActionListener(this);
        this.comboBoxMsPntConnectionType.setBounds(135, 19, 139, 20);
        this.msPntConnectionPanel.add(this.comboBoxMsPntConnectionType);
        
        JLabel lblPntHostOrSerialPort = new JLabel("Host or serial port");
        lblPntHostOrSerialPort.setBounds(17, 45, 110, 16);
        this.msPntConnectionPanel.add(lblPntHostOrSerialPort);
        
        JLabel lblPntFileName = new JLabel("PNT-file name");
        lblPntFileName.setBounds(17, 70, 89, 16);
        this.msPntConnectionPanel.add(lblPntFileName);
        
        this.textFieldMsPntFileName = new JTextField();
        this.textFieldMsPntFileName.setColumns(10);
        this.textFieldMsPntFileName.setBounds(135, 69, 139, 20);
        this.msPntConnectionPanel.add(this.textFieldMsPntFileName);
        
        JLabel lblPntPort = new JLabel("Port");
        lblPntPort.setBounds(17, 95, 61, 16);
        this.msPntConnectionPanel.add(lblPntPort);
        
        this.spinnerMsPntPort = new JSpinner();
        this.spinnerMsPntPort.setEditor(new NumberEditor(this.spinnerMsPntPort, "#"));
        this.spinnerMsPntPort.setBounds(135, 94, 139, 20);
        this.msPntConnectionPanel.add(spinnerMsPntPort);
        
        this.textFieldMsPntHostOrSerialPort = new JTextField();
        this.textFieldMsPntHostOrSerialPort.setColumns(10);
        this.textFieldMsPntHostOrSerialPort.setBounds(135, 44, 139, 20);
        this.msPntConnectionPanel.add(this.textFieldMsPntHostOrSerialPort);

        JLabel lblMspPntHal = new JLabel("HAL");
        lblMspPntHal.setBounds(17, 118, 61, 16);
        this.msPntConnectionPanel.add(lblMspPntHal);

        this.spinnerMsPntHal = new JSpinner();
        this.spinnerMsPntHal.setEditor(new NumberEditor(this.spinnerMsPntHal, "#"));
        this.spinnerMsPntHal.setBounds(135, 118, 139, 20);
        this.msPntConnectionPanel.add(spinnerMsPntHal);

        cbMsPntTrueHeadingFromCog = new JCheckBox("True heading = COG");
        this.cbMsPntTrueHeadingFromCog.setBounds(300, 118, 160, 20);
        this.msPntConnectionPanel.add(cbMsPntTrueHeadingFromCog);

        this.add(this.msPntConnectionPanel);

        
        /************** Transponder Panel ***************/
        
        JPanel transponderPanel = new JPanel();
        transponderPanel.setBounds(6, 499, 438, 85);
        transponderPanel.setLayout(null);
        transponderPanel.setBorder(new TitledBorder(null, "Transponder", TitledBorder.LEADING, 
                TitledBorder.TOP, null, null));
        
        this.chckbxStartTransponder = new JCheckBox("Start virtuel tranponder on startup");
        this.chckbxStartTransponder.setBounds(16, 20, 247, 20);
        transponderPanel.add(this.chckbxStartTransponder);
        
        this.spinnerAisSensorRange = new JSpinner(
                new SpinnerNumberModel(new Double(0), null, null, new Double(1)));
        this.spinnerAisSensorRange.setBounds(16, 45, 75, 20);
        transponderPanel.add(this.spinnerAisSensorRange);
        
        JLabel lblAisSensorRange = new JLabel("AIS sensor range");
        lblAisSensorRange.setBounds(103, 47, 105, 16);
        transponderPanel.add(lblAisSensorRange);
        
        this.add(transponderPanel);
        
        // Update UI based on the current selection.
        this.updateUIState();
    }
    
    /**
     * Updates the enabled state of UI components based on the current selection.
     */
    private void updateUIState() {
        
        // Get the current selected PNT source.
        PntSourceSetting pntSrc = (PntSourceSetting) this.comboBoxPntSource.getSelectedItem();
        
        // Set enabled state of AIS connection components.
        boolean aisEnabled = pntSrc == PntSourceSetting.AUTO || pntSrc == PntSourceSetting.AIS;
        Object aisConType  = this.comboBoxAisConnectionType.getSelectedItem();
        this.aisConnectionPanel.setEnabled(aisEnabled);
        this.setEnabled(aisConnectionPanel, aisEnabled, JLabel.class);
        this.comboBoxAisConnectionType.setEnabled(aisEnabled);
        this.textFieldAisHostOrSerialPort.setEnabled(aisEnabled && (aisConType == TCP || aisConType == SERIAL));
        this.spinnerAisPort.setEnabled(aisEnabled && (aisConType == TCP || aisConType == UDP));
        this.textFieldAisFilename.setEnabled(aisEnabled && aisConType == FILE);
        
        // Set enabled state of GPS connection components.
        boolean gpsEnabled = pntSrc == PntSourceSetting.AUTO || pntSrc == PntSourceSetting.GPS;
        Object gpsConType = this.comboBoxGPSConnectionType.getSelectedItem();
        this.gpsConnectionPanel.setEnabled(gpsEnabled);
        this.setEnabled(this.gpsConnectionPanel, gpsEnabled, JLabel.class);
        this.comboBoxGPSConnectionType.setEnabled(gpsEnabled);
        this.textFieldGpsHostOrSerialPort.setEnabled(gpsEnabled && (gpsConType == TCP || gpsConType == SERIAL));
        this.spinnerGpsPort.setEnabled(gpsEnabled && (gpsConType == TCP || gpsConType == UDP));
        this.textFieldGpsFilename.setEnabled(gpsConType == FILE);
        
        // Set enabled state of Multi-source PNT connection components
        boolean msPntEnabled = pntSrc == PntSourceSetting.AUTO || pntSrc == PntSourceSetting.MSPNT;
        Object msPntConType = this.comboBoxMsPntConnectionType.getSelectedItem();
        this.msPntConnectionPanel.setEnabled(msPntEnabled);
        this.setEnabled(this.msPntConnectionPanel, msPntEnabled, JLabel.class);
        this.comboBoxMsPntConnectionType.setEnabled(msPntEnabled);
        this.textFieldMsPntHostOrSerialPort.setEnabled(msPntEnabled && (msPntConType == TCP || msPntConType == SERIAL));
        this.spinnerMsPntPort.setEnabled(msPntEnabled && (msPntConType == TCP || msPntConType == UDP));
        this.textFieldMsPntFileName.setEnabled(msPntConType == FILE);
        this.spinnerMsPntHal.setEnabled(msPntEnabled);
        this.cbMsPntTrueHeadingFromCog.setEnabled(msPntEnabled);
    }
    
    /**
     * Sets the enabled state of child components of the given types
     * @param container the container whose child components to update
     * @param enabled the enabled state of the child components
     * @param types the types of child component to update
     */
    private void setEnabled(Container container, boolean enabled, Class<?>... types) {
        
        for(Component c : container.getComponents()) {
            if (types.length == 0) {
                c.setEnabled(enabled);
                continue;
            }
            
            for (Class<?> type : types) {
                if (c.getClass().isAssignableFrom(type)) {
                    c.setEnabled(enabled);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkSettingsChanged() {
        
        return 
                // Check for changes made in PNT source settings.
                changed(this.settings.getPntSource(), comboBoxPntSource.getSelectedItem()) ||

                // Check for changes made in AIS connection settings.
                changed(this.settings.getAisConnectionType(), this.comboBoxAisConnectionType.getSelectedItem()) ||
                changed(this.settings.getAisHostOrSerialPort(), this.textFieldAisHostOrSerialPort.getText()) ||
                changed(this.settings.getAisFilename(), this.textFieldAisFilename.getText()) ||
                changed(this.settings.getAisTcpOrUdpPort(), this.spinnerAisPort.getValue()) ||
                
                // Check for changes made in GPS connection changes.
                changed(this.settings.getGpsConnectionType(), this.comboBoxGPSConnectionType.getSelectedItem()) ||
                changed(this.settings.getGpsHostOrSerialPort(), this.textFieldGpsHostOrSerialPort.getText()) ||
                changed(this.settings.getGpsFilename(), this.textFieldGpsFilename.getText()) ||
                changed(this.settings.getGpsTcpOrUdpPort(), this.spinnerGpsPort.getValue()) ||
                
                // Check for changes made in MS PNT connection settings.
                changed(this.settings.getMsPntConnectionType(), this.comboBoxMsPntConnectionType.getSelectedItem()) ||
                changed(this.settings.getMsPntHostOrSerialPort(), this.textFieldMsPntHostOrSerialPort.getText()) ||
                changed(this.settings.getMsPntFilename(), this.textFieldMsPntFileName.getText()) ||
                changed(this.settings.getMsPntTcpOrUdpPort(), this.spinnerMsPntPort.getValue()) ||
                changed(this.settings.getMsPntHal(), this.spinnerMsPntHal.getValue()) ||
                changed(this.settings.isMsPntTrueHeadingFromCog(), this.cbMsPntTrueHeadingFromCog.isSelected()) ||

                // Check for changes made in transponder settings.
                changed(this.settings.isStartTransponder(), this.chckbxStartTransponder.isSelected()) ||
                changed(this.settings.getAisSensorRange(), this.spinnerAisSensorRange.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doLoadSettings() {
        
        // Get the settings for EPDShip sensor settings.
        this.settings = EPDShip.getInstance().getSettings().getSensorSettings();
        
        // Load the PNT source settings.
        this.comboBoxPntSource.setSelectedItem(this.settings.getPntSource());
        
        // Load the AIS connection settings.
        this.comboBoxAisConnectionType.setSelectedItem(this.settings.getAisConnectionType());
        this.textFieldAisHostOrSerialPort.setText(this.settings.getAisHostOrSerialPort());
        this.textFieldAisFilename.setText(this.settings.getAisFilename());
        this.spinnerAisPort.setValue(this.settings.getAisTcpOrUdpPort()); 
        
        // Load the GPS connection settings.
        this.comboBoxGPSConnectionType.setSelectedItem(this.settings.getGpsConnectionType());
        this.textFieldGpsHostOrSerialPort.setText(this.settings.getGpsHostOrSerialPort());
        this.textFieldGpsFilename.setText(this.settings.getGpsFilename());
        this.spinnerGpsPort.setValue(this.settings.getGpsTcpOrUdpPort());
        
        // Load the Multi-source PNT Connection settings.
        this.comboBoxMsPntConnectionType.setSelectedItem(this.settings.getMsPntConnectionType());
        this.textFieldMsPntHostOrSerialPort.setText(this.settings.getMsPntHostOrSerialPort());
        this.textFieldMsPntFileName.setText(this.settings.getMsPntFilename());
        this.spinnerMsPntPort.setValue(this.settings.getMsPntTcpOrUdpPort());
        this.spinnerMsPntHal.setValue(this.settings.getMsPntHal());
        this.cbMsPntTrueHeadingFromCog.setSelected(settings.isMsPntTrueHeadingFromCog());

        // Load the transponder settings.
        this.chckbxStartTransponder.setSelected(this.settings.isStartTransponder());
        this.spinnerAisSensorRange.setValue(this.settings.getAisSensorRange());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void doSaveSettings() {
        // Saves the AIS Connection settings
        this.settings.setAisConnectionType((SensorConnectionType) this.comboBoxAisConnectionType.getSelectedItem());
        this.settings.setAisHostOrSerialPort(this.textFieldAisHostOrSerialPort.getText());
        this.settings.setAisFilename(this.textFieldAisFilename.getText());
        this.settings.setAisTcpOrUdpPort((Integer) this.spinnerAisPort.getValue());
        
        // Saves the GPS Connection settings
        this.settings.setGpsConnectionType((SensorConnectionType) this.comboBoxGPSConnectionType.getSelectedItem());
        this.settings.setGpsHostOrSerialPort(this.textFieldGpsHostOrSerialPort.getText());
        this.settings.setGpsFilename(this.textFieldGpsFilename.getText());
        this.settings.setGpsTcpOrUdpPort((Integer) this.spinnerGpsPort.getValue());
        
        // Saves the Multi-source PNT Connection settings
        this.settings.setMsPntConnectionType((SensorConnectionType) this.comboBoxMsPntConnectionType.getSelectedItem());
        this.settings.setMsPntHostOrSerialPort(this.textFieldMsPntHostOrSerialPort.getText());
        this.settings.setMsPntFilename(this.textFieldMsPntFileName.getText());
        this.settings.setMsPntTcpOrUdpPort((Integer) this.spinnerMsPntPort.getValue());
        this.settings.setMsPntHal((Integer) this.spinnerMsPntHal.getValue());
        this.settings.setMsPntTrueHeadingFromCog(cbMsPntTrueHeadingFromCog.isSelected());

        // Saves the PNT source settings
        this.settings.setPntSource((PntSourceSetting) this.comboBoxPntSource.getSelectedItem());
        
        // Saves the Transponder settings
        this.settings.setStartTransponder(this.chckbxStartTransponder.isSelected());        
        this.settings.setAisSensorRange((Double) this.spinnerAisSensorRange.getValue());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void fireSettingsChanged() {
        super.fireSettingsChanged(Type.SENSOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Update UI.
        this.updateUIState();
    }
}
