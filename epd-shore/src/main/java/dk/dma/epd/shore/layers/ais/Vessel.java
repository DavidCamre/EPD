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
package dk.dma.epd.shore.layers.ais;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import com.bbn.openmap.omGraphics.OMCircle;
import com.bbn.openmap.omGraphics.OMGraphicConstants;
import com.bbn.openmap.omGraphics.OMLine;
import com.bbn.openmap.omGraphics.OMText;
import com.bbn.openmap.proj.Length;
import com.bbn.openmap.proj.coords.LatLonPoint;

import dk.dma.epd.common.prototype.ais.AisTarget;
import dk.dma.epd.common.prototype.ais.VesselPositionData;
import dk.dma.epd.common.prototype.ais.VesselStaticData;
import dk.dma.epd.common.prototype.ais.VesselTarget;
import dk.dma.epd.common.prototype.layers.ais.PastTrackGraphic;
import dk.dma.epd.common.prototype.layers.ais.TargetGraphic;
import dk.dma.epd.common.prototype.settings.AisSettings;
import dk.dma.epd.common.prototype.settings.NavSettings;
import dk.dma.epd.common.text.Formatter;

/**
 * Vessel class that maintains all the components in a vessel
 *
 * @author Claes N. Ladefoged, claesnl@gmail.com
 *
 */
public class Vessel extends TargetGraphic {
    private static final long serialVersionUID = 1L;
    private VesselLayer vessel;
    private OMCircle vesCirc;
    private HeadingLayer heading;
    private String vesselHeading = "N/A";
    private Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 11);;
    private OMText callSign;
    private String vesselCallSign = "N/A";
    private OMText nameMMSI;
    private String vesselName = "N/A";
    private long MMSI;
    private double lat;
    private double lon;
    private double sog;
    private double cogR;
    private double trueHeading;
    private OMLine speedVector;
    private LatLonPoint startPos;
    private LatLonPoint endPos;
    public static final float STROKE_WIDTH = 1.5f;
    private Color shipColor = new Color(78, 78, 78);
    private String vesselDest = "N/A";
    private String vesselEta = "N/A";
    private String vesselShiptype = "N/A";
    private VesselTarget vesselTarget;
    private PastTrackGraphic pastTrackGraphic = new PastTrackGraphic();

    /**
     * Vessel initialization with icon, circle, heading, speedvector, callsign
     * and name/mmsi.
     *
     * @param MMSI
     *            Key of vessel
     * @param staticImages
     */
    public Vessel(long MMSI) {
        super();
        this.MMSI = MMSI;

        // Vessel layer
        vessel = new VesselLayer(MMSI, this);

        // Vessel circle layer
        vesCirc = new OMCircle(0, 0, 0.01);
        vesCirc.setFillPaint(shipColor);

        // Heading layer
        heading = new HeadingLayer(MMSI, new int[] { 0, 0 }, new int[] { 0, -30 });
        heading.setFillPaint(new Color(0, 0, 0));

        // Speed vector layer
        speedVector = new OMLine(0, 0, 0, 0, OMGraphicConstants.LINETYPE_STRAIGHT);
        speedVector.setStroke(new BasicStroke(STROKE_WIDTH, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f,
                new float[] { 10.0f, 8.0f }, 0.0f));
        speedVector.setLinePaint(new Color(255, 0, 0));

        // Call sign layer
        callSign = new OMText(0, 0, 0, 0, "", font, OMText.JUSTIFY_CENTER);

        // MSI / Name layer
        nameMMSI = new OMText(0, 0, 0, 0, Long.toString(MMSI), font, OMText.JUSTIFY_CENTER);

        this.add(vessel);
        this.add(vesCirc);
        this.add(heading);
        this.add(speedVector);
        this.add(callSign);
        this.add(nameMMSI);
        this.add(pastTrackGraphic);
    }

    /**
     * Updates the graphics according to the current target
     * 
     * @param aisTarget
     * @param aisSettings
     * @param navSettings
     * @param mapScale
     */
    @Override
    public void update(AisTarget aisTarget, AisSettings aisSettings, NavSettings navSettings, float mapScale) {
        vesselTarget = (VesselTarget)aisTarget;
        VesselPositionData location = vesselTarget.getPositionData();
        VesselStaticData staticData = vesselTarget.getStaticData();
        
        double trueHeading = location.getTrueHeading();
        if (trueHeading == 511) {
            trueHeading = location.getCog();
        }
        
        double lat = location.getPos().getLatitude();
        double lon = location.getPos().getLongitude();
        double sog = location.getSog();
        double cogR = Math.toRadians(location.getCog());
        
        vessel.setLocation(lat, lon);
        vessel.setHeading(trueHeading);

        heading.setLocation(lat, lon, OMGraphicConstants.DECIMAL_DEGREES, Math.toRadians(trueHeading));
        vesselHeading = Integer.toString((int) Math.round(trueHeading)) + "°";

        vesselName = "ID:" + this.MMSI;
        if (staticData != null) {
            vessel.setImageIcon(staticData.getShipType().toString());
            callSign.setData("Call Sign: " + staticData.getCallsign());
            vesselCallSign = staticData.getTrimmedCallsign();
            vesselName = staticData.getTrimmedName();
            vesselDest = staticData.getDestination();
            vesselEta = Long.toString(staticData.getEta());
            vesselShiptype = staticData.getShipType().toString();

        }
        nameMMSI.setData(vesselName);

        if (this.lat != lat || this.lon != lon || this.sog != sog || this.cogR != cogR
                || this.trueHeading != trueHeading) {
            this.lat = lat;
            this.lon = lon;
            this.sog = sog;
            this.cogR = cogR;
            this.trueHeading = trueHeading;

            vesCirc.setLatLon(lat, lon);

            callSign.setLat(lat);
            callSign.setLon(lon);
            if (trueHeading > 90 && trueHeading < 270) {
                callSign.setY(-25);
            } else {
                callSign.setY(35);
            }

            double[] speedLL = new double[4];
            speedLL[0] = (float) lat;
            speedLL[1] = (float) lon;
            startPos = new LatLonPoint.Double(lat, lon);
            float length = (float) Length.NM.toRadians(6.0 * (sog / 60.0));
            endPos = startPos.getPoint(length, cogR);
            speedLL[2] = endPos.getLatitude();
            speedLL[3] = endPos.getLongitude();
            speedVector.setLL(speedLL);

            nameMMSI.setLat(lat);
            nameMMSI.setLon(lon);
            if (trueHeading > 90 && trueHeading < 270) {
                nameMMSI.setY(-10);
            } else {
                nameMMSI.setY(20);
            }
        }
        
        
        // Past track graphics
        pastTrackGraphic.update(vesselTarget);

        // Scale for text-labels
        boolean b1 = mapScale < 750000;
        showHeading(b1);
        showSpeedVector(b1);
        showCallSign(false);
        showName(b1);
        // Scale for ship icons
        boolean b2 = mapScale < 1500000;
        showVesselIcon(b2);
        showVesselCirc(!b2);
    }
    
    /**
     * Toggle visibility of vessel icon on map
     *
     * @param b
     *            Boolean that tells if layer should be shown or not
     */
    public void showVesselIcon(boolean b) {
        vessel.setVisible(b);
    }

    /**
     * Toggle visibility of vessel circle on map
     *
     * @param b
     *            Boolean that tells if layer should be shown or not
     */
    public void showVesselCirc(boolean b) {
        vesCirc.setVisible(b);
    }

    /**
     * Toggle visibility of heading vector on map
     *
     * @param b
     *            Boolean that tells if layer should be shown or not
     */
    public void showHeading(boolean b) {
        heading.setVisible(b);
    }

    /**
     * Toggle visibility of speed vector on map
     *
     * @param b
     *            Boolean that tells if layer should be shown or not
     */
    public void showSpeedVector(boolean b) {
        speedVector.setVisible(b);
    }

    /**
     * Toggle visibility of call sign label on map
     *
     * @param b
     *            Boolean that tells if layer should be shown or not
     */
    public void showCallSign(boolean b) {
        callSign.setVisible(b);
    }

    /**
     * Toggle visibility of name label on map
     *
     * @param b
     *            Boolean that tells if layer should be shown or not
     */
    public void showName(boolean b) {
        nameMMSI.setVisible(b);
    }

    public long getMMSI() {
        return this.MMSI;
    }

    public String getName() {
        if (vesselName != null && vesselName.startsWith("ID")) {
            return "N/A";
        } else {
            return vesselName;
        }
    }

    public String getHeading() {
        return vesselHeading;
    }

    public String getCallSign() {
        return vesselCallSign;
    }

    public String getLat() {
        return Formatter.latToPrintable(lat);
    }

    public String getLon() {
        return Formatter.lonToPrintable(lon);
    }

    public String getSog() {
        return Integer.toString((int) Math.round(sog)) + " kn";
    }

    public String getEta() {
        return vesselEta;
    }

    public String getDest() {
        return vesselDest;
    }

    public String getShipType() {
        return vesselShiptype;
    }

    public VesselTarget getVesselTarget() {
        return vesselTarget;
    }


    @Override
    public void render(Graphics gr) {
        Graphics2D image = (Graphics2D) gr;
        image.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.render(image);
    }

}
