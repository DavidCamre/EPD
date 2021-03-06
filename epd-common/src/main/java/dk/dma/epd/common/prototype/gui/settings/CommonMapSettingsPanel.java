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
package dk.dma.epd.common.prototype.gui.settings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;

import com.bbn.openmap.proj.coords.LatLonPoint;

import dk.dma.epd.common.prototype.gui.settings.ISettingsListener.Type;
import dk.dma.epd.common.prototype.settings.MapSettings;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;

/**
 * 
 * @author adamduehansen
 * 
 */
public class CommonMapSettingsPanel extends BaseSettingsPanel implements
        ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel generalMapSettings;
    private JPanel wmsSettings;
    private JTextField textFieldWMSURL;
    private MapSettings settings;
    private JSpinner spinnerDefaultMapScale;
    private JSpinner spinnerMaximumScale;
    private JSpinner spinnerLatitude;
    private JSpinner spinnerLongitude;
    private JLabel lblWmsUrl;
    private JLabel lblenterTheUrl;
    private JCheckBox chckbxUseEnc;
    private JCheckBox chckbxUseWms;
    private JSpinner spinnerShallowContour;
    private JSpinner spinnerSafetyDepth;
    private JSpinner spinnerSafetyContour;
    private JSpinner spinnerDeepContour;
    private JComboBox<String> comboBoxColorProfile;
    private JCheckBox chckbxSimplePointSymbols;
    private JCheckBox chckbxShallowPattern;
    private JCheckBox chckbxShowText;
    private JCheckBox chckbxTwoShades;
    private JCheckBox chckbxPlainAreas;
    private JButton btnAdvancedOptions;

    private boolean s57SettingsChanged;

    /**
     * Constructs a new CommonMapSettingsPanel object.
     */
    public CommonMapSettingsPanel() {
        super("Map", new ImageIcon(
                CommonMapSettingsPanel.class
                        .getResource("/images/settings/map.png")));
        setLayout(null);

        /************** General settings ***************/

        generalMapSettings = new JPanel();
        generalMapSettings.setBounds(6, 6, 438, 160);
        generalMapSettings.setLayout(null);
        generalMapSettings.setBorder(new TitledBorder(null, "General",
                TitledBorder.LEADING, TitledBorder.TOP, null, null));

        // General settings panel components.
        spinnerDefaultMapScale = new JSpinner(new SpinnerNumberModel(new Float(
                0), null, null, new Float(1)));
        spinnerDefaultMapScale.setBounds(16, 20, 75, 20);
        generalMapSettings.add(spinnerDefaultMapScale);

        JLabel lblDefaultMapScale = new JLabel("Default map scale");
        lblDefaultMapScale.setBounds(103, 22, 113, 16);
        generalMapSettings.add(lblDefaultMapScale);

        spinnerMaximumScale = new JSpinner(new SpinnerNumberModel(
                new Integer(0), null, null, new Integer(1)));
        spinnerMaximumScale.setBounds(16, 45, 75, 20);
        generalMapSettings.add(spinnerMaximumScale);

        JLabel lblMaximumScale = new JLabel("Maximum scale");
        lblMaximumScale.setBounds(103, 47, 98, 16);
        generalMapSettings.add(lblMaximumScale);

        JLabel lblDefaultMapCenter = new JLabel("Default map center");
        lblDefaultMapCenter.setBounds(16, 70, 120, 16);
        generalMapSettings.add(lblDefaultMapCenter);

        JLabel lblLatitude = new JLabel("Latitude:");
        lblLatitude.setBounds(16, 95, 55, 16);
        generalMapSettings.add(lblLatitude);

        spinnerLatitude = new JSpinner(new SpinnerNumberModel(new Double(0),
                null, null, new Double(1)));
        spinnerLatitude.setBounds(83, 93, 75, 20);
        generalMapSettings.add(spinnerLatitude);

        JLabel lblLongitude = new JLabel("Longitude:");
        lblLongitude.setBounds(190, 93, 75, 16);
        generalMapSettings.add(lblLongitude);

        spinnerLongitude = new JSpinner(new SpinnerNumberModel(new Double(0),
                null, null, new Double(1)));
        spinnerLongitude.setBounds(245, 91, 75, 20);
        generalMapSettings.add(spinnerLongitude);

        this.add(generalMapSettings);

        chckbxUseEnc = new JCheckBox("use ENC");
        chckbxUseEnc.setBounds(16, 123, 84, 20);
        generalMapSettings.add(chckbxUseEnc);

        /************** WMS settings ***************/

        wmsSettings = new JPanel();
        wmsSettings.setBounds(6, 409, 438, 155);
        wmsSettings.setLayout(null);
        wmsSettings.setBorder(new TitledBorder(null, "WMS Settings",
                TitledBorder.LEADING, TitledBorder.TOP, null, null));

        chckbxUseWms = new JCheckBox("Use WMS");
        chckbxUseWms.setBounds(16, 20, 88, 23);
        wmsSettings.add(chckbxUseWms);

        lblWmsUrl = new JLabel("WMS URL:");
        lblWmsUrl.setBounds(16, 50, 61, 16);
        wmsSettings.add(lblWmsUrl);

        textFieldWMSURL = new JTextField();
        textFieldWMSURL.setBounds(16, 75, 405, 20);
        wmsSettings.add(textFieldWMSURL);
        textFieldWMSURL.setColumns(10);

        this.add(wmsSettings);

        lblenterTheUrl = new JLabel(
                "<html>Enter the URL to the WMS service you wish to use, <br>enter everything except BBOX and height/width options.</html>");
        lblenterTheUrl.setBounds(16, 100, 405, 37);
        wmsSettings.add(lblenterTheUrl);

        /************** S52 settings ***************/

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new TitledBorder(null, "S52 Layer",
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(6, 178, 438, 219);
        add(panel);

        spinnerShallowContour = new JSpinner();
        spinnerShallowContour.setBounds(16, 20, 75, 20);
        panel.add(spinnerShallowContour);

        spinnerSafetyDepth = new JSpinner();
        spinnerSafetyDepth.setBounds(16, 45, 75, 20);
        panel.add(spinnerSafetyDepth);

        spinnerSafetyContour = new JSpinner();
        spinnerSafetyContour.setBounds(16, 70, 75, 20);
        panel.add(spinnerSafetyContour);

        spinnerDeepContour = new JSpinner();
        spinnerDeepContour.setBounds(16, 95, 75, 20);
        panel.add(spinnerDeepContour);

        JLabel label = new JLabel("Shallow contour");
        label.setBounds(103, 22, 101, 16);
        panel.add(label);

        JLabel label_1 = new JLabel("Safety depth");
        label_1.setBounds(103, 47, 78, 16);
        panel.add(label_1);

        JLabel label_2 = new JLabel("Safety contour");
        label_2.setBounds(103, 72, 91, 16);
        panel.add(label_2);

        JLabel label_3 = new JLabel("Deep contour");
        label_3.setBounds(103, 97, 91, 16);
        panel.add(label_3);

        String[] colorModes = { "Day", "Dusk", "Night" };
        comboBoxColorProfile = new JComboBox<String>(colorModes);
        comboBoxColorProfile.setBounds(206, 19, 75, 20);
        panel.add(comboBoxColorProfile);

        JLabel label_4 = new JLabel("Color profile");
        label_4.setBounds(293, 20, 91, 16);
        panel.add(label_4);

        chckbxShowText = new JCheckBox("Show text");
        chckbxShowText.setBounds(16, 125, 128, 23);
        panel.add(chckbxShowText);

        chckbxShallowPattern = new JCheckBox("Shallow pattern");
        chckbxShallowPattern.setBounds(16, 150, 142, 23);
        panel.add(chckbxShallowPattern);

        chckbxSimplePointSymbols = new JCheckBox("Simple point symbols");
        chckbxSimplePointSymbols.setBounds(16, 175, 168, 23);
        panel.add(chckbxSimplePointSymbols);

        chckbxTwoShades = new JCheckBox("Two shades");
        chckbxTwoShades.setBounds(220, 150, 106, 23);
        panel.add(chckbxTwoShades);

        chckbxPlainAreas = new JCheckBox("Plain areas");
        chckbxPlainAreas.setBounds(220, 125, 106, 23);
        panel.add(chckbxPlainAreas);

        btnAdvancedOptions = new JButton("Advanced Options");
        btnAdvancedOptions.setBounds(220, 175, 107, 20);

        btnAdvancedOptions.addActionListener(this);

        // new ActionListener() {
        // public void actionPerformed(ActionEvent arg0) {
        // new AdvancedSettingsWindow(self);
        //
        // }
        // });

        panel.add(btnAdvancedOptions);
    }

    /**
     * 
     * @return The General settings panel.
     */
    public JPanel getGeneralPanel() {
        return this.generalMapSettings;
    }

    /**
     * 
     * @return The WMS settings panel.
     */
    public JPanel getWMSPanel() {
        return this.wmsSettings;
    }

    /**
     * 
     * @return The WMS URL JLabel Component.
     */
    public JLabel getLblWMSURL() {
        return this.lblWmsUrl;
    }

    public JTextField getTextFieldWMSURL() {
        return this.textFieldWMSURL;
    }

    @Override
    protected boolean checkSettingsChanged() {
        return changed(this.settings.getScale(),
                this.spinnerDefaultMapScale.getValue())
                || changed(this.settings.getMaxScale(),
                        this.spinnerMaximumScale.getValue())
                || changed(this.settings.getCenter().getLatitude(),
                        this.spinnerLatitude.getValue())
                || changed(this.settings.getCenter().getLongitude(),
                        this.spinnerLongitude.getValue())
                || changed(this.settings.isUseEnc(),
                        this.chckbxUseEnc.isSelected())
                ||

                // Check for changes in S52 layer settings
                changed(this.settings.getS52ShallowContour(),
                        this.spinnerShallowContour.getValue())
                || changed(this.settings.getS52SafetyDepth(),
                        this.spinnerSafetyDepth.getValue())
                || changed(this.settings.getS52SafetyContour(),
                        this.spinnerSafetyContour.getValue())
                || changed(this.settings.getS52DeepContour(),
                        this.spinnerDeepContour.getValue())
                || changed(this.settings.isS52ShowText(),
                        this.chckbxShowText.isSelected())
                || changed(this.settings.isS52ShallowPattern(),
                        this.chckbxShallowPattern.isSelected())
                || changed(this.settings.isUseSimplePointSymbols(),
                        this.chckbxSimplePointSymbols.isSelected())
                || changed(this.settings.isUsePlainAreas(),
                        this.chckbxPlainAreas.isSelected())
                || changed(this.settings.isS52TwoShades(),
                        this.chckbxTwoShades.isSelected())
                || changed(this.settings.getColor(), this.comboBoxColorProfile
                        .getSelectedItem().toString())
                ||

                // Changes in WMS settings.
                changed(this.settings.isUseWms(),
                        this.chckbxUseWms.isSelected())
                || changed(this.settings.getWmsQuery(),
                        this.textFieldWMSURL.getText())

                || s57SettingsChanged;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkNeedsRestart() {
        return changed(this.settings.isUseEnc(), this.chckbxUseEnc.isSelected())
                || changed(this.settings.isUseWms(),
                        this.chckbxUseWms.isSelected() || s57SettingsChanged);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doLoadSettings() {
        this.settings = this.getSettings().getMapSettings();

        // Load settings for generel panel.
        this.spinnerDefaultMapScale.setValue(this.settings.getScale());
        this.spinnerMaximumScale.setValue(this.settings.getMaxScale());
        Float latitude = this.settings.getCenter().getLatitude();
        Float longitude = this.settings.getCenter().getLongitude();
        this.spinnerLatitude.setValue(latitude.doubleValue());
        this.spinnerLongitude.setValue(longitude.doubleValue());
        this.chckbxUseEnc.setSelected(this.settings.isUseEnc());

        // Load s52 layer settings
        this.spinnerShallowContour.setValue(this.settings
                .getS52ShallowContour());
        this.spinnerSafetyDepth.setValue(this.settings.getS52SafetyDepth());
        this.spinnerSafetyContour.setValue(this.settings.getS52SafetyContour());
        this.spinnerDeepContour.setValue(this.settings.getS52DeepContour());
        this.chckbxShowText.setSelected(settings.isS52ShowText());
        this.chckbxShallowPattern.setSelected(settings.isS52ShallowPattern());
        this.chckbxSimplePointSymbols.setSelected(settings
                .isUseSimplePointSymbols());
        this.chckbxPlainAreas.setSelected(settings.isUsePlainAreas());
        this.chckbxTwoShades.setSelected(settings.isS52TwoShades());
        this.comboBoxColorProfile.setSelectedItem(settings.getColor());

        // Load settings for WMS.
        this.chckbxUseWms.setSelected(this.settings.isUseWms());
        this.textFieldWMSURL.setText(settings.getWmsQuery());
        this.btnAdvancedOptions.setEnabled(this.settings.isUseEnc());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doSaveSettings() {

        // Save generel panel.
        this.settings.setScale((Float) spinnerDefaultMapScale.getValue());
        this.settings
                .setMaxScale((Integer) this.spinnerMaximumScale.getValue());
        LatLonPoint center = new LatLonPoint.Double(
                (Double) this.spinnerLatitude.getValue(),
                (Double) this.spinnerLongitude.getValue());
        this.settings.setCenter(center);
        this.settings.setUseEnc(this.chckbxUseEnc.isSelected());

        // Save s52 layer settings
        this.settings.setS52ShallowContour((Integer) spinnerShallowContour
                .getValue());
        this.settings.setS52SafetyDepth((Integer) this.spinnerSafetyDepth
                .getValue());
        this.settings.setS52SafetyContour((Integer) this.spinnerSafetyContour
                .getValue());
        this.settings.setS52DeepContour((Integer) this.spinnerDeepContour
                .getValue());
        this.settings.setS52ShowText(this.chckbxShowText.isSelected());
        this.settings.setS52ShallowPattern(this.chckbxShallowPattern
                .isSelected());
        this.settings.setUseSimplePointSymbols(this.chckbxSimplePointSymbols
                .isSelected());
        this.settings.setUsePlainAreas(this.chckbxPlainAreas.isSelected());
        this.settings.setS52TwoShades(this.chckbxTwoShades.isSelected());
        this.settings.setColor(comboBoxColorProfile.getSelectedItem()
                .toString());

        // Checks the WMS settings
        checkWmsSettings();

        // Save settings for WMS.
        this.settings.setUseWms(this.chckbxUseWms.isSelected());
        this.settings.setWmsQuery(textFieldWMSURL.getText());
    }

    /**
     * Checks that the WMS query is a valid URL if WMS is turned on
     */
    private void checkWmsSettings() {
        if (chckbxUseWms.isSelected()
                && (!settings.isUseWms() || changed(settings.getWmsQuery(),
                        textFieldWMSURL.getText()))) {

            boolean validUrl = textFieldWMSURL.getText().length() > 0;
            if (validUrl) {
                try {
                    URL url = new URL(textFieldWMSURL.getText());
                    url.openConnection().connect();
                } catch (MalformedURLException e) {
                    // the URL is not in a valid form
                    validUrl = false;
                } catch (IOException e) {
                    // This does not make it an invalid URL, though...
                }
            }
            if (!validUrl) {
                JOptionPane
                        .showMessageDialog(
                                this,
                                "The specified WMS URL is not valid.\nWMS will be turned off.",
                                "Invalid WMS URL", JOptionPane.WARNING_MESSAGE);
                chckbxUseWms.setSelected(false);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fireSettingsChanged() {
        fireSettingsChanged(Type.MAP);
    }

    public void s57MapSettingsChanged() {
        s57SettingsChanged = true;
        fireSettingsChanged(Type.MAP);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdvancedOptions) {
            new AdvancedSettingsWindow(this);
        }
    }
}
