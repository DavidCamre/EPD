/* Copyright (c) 2011 Danish Maritime Authority
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.dma.epd.common.prototype.layers;

import java.awt.event.MouseEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import com.bbn.openmap.MapBean;
import com.bbn.openmap.event.MapEventUtils;
import com.bbn.openmap.event.MapMouseListener;
import com.bbn.openmap.layer.OMGraphicHandlerLayer;
import com.bbn.openmap.omGraphics.OMGraphic;
import com.bbn.openmap.omGraphics.OMGraphicList;

import dk.dma.epd.common.prototype.EPD;
import dk.dma.epd.common.prototype.gui.MainFrameCommon;
import dk.dma.epd.common.prototype.gui.MapFrameCommon;
import dk.dma.epd.common.prototype.gui.MapMenuCommon;
import dk.dma.epd.common.prototype.gui.util.InfoPanel;

/**
 * Common EPD layer subclass that may be sub-classed by other layers.
 */
public abstract class GeneralLayerCommon extends OMGraphicHandlerLayer implements MapMouseListener {

    private static final long serialVersionUID = 1L;

    protected List<InfoPanel> infoPanels = new CopyOnWriteArrayList<>();
    
    protected OMGraphicList graphics = new OMGraphicList();
    protected MapBean mapBean;
    protected MainFrameCommon mainFrame;
    protected MapMenuCommon mapMenu;
    protected MapFrameCommon mapFrame;

    /**
     * Called when a bean is added to the bean context
     * @param obj the bean being added
     */
    @Override
    public void findAndInit(Object obj) {
        super.findAndInit(obj);
        
        if (obj instanceof MapBean) {
            mapBean = (MapBean) obj;
        } else if (obj instanceof MainFrameCommon) {
            mainFrame = (MainFrameCommon) obj;
            if (mainFrame.getGlassPane() != null) {
                // EPDShip case
                addInfoPanelsToGlassPane();
            }
        } else if (obj instanceof MapMenuCommon) {
            mapMenu = (MapMenuCommon) obj;
        } else if (obj instanceof MapFrameCommon) {
            mapFrame = (MapFrameCommon) obj;
            if (mapFrame.getGlassPane() != null) {
                // EPDShore case
                addInfoPanelsToGlassPane();
            }
        }
    }

    /**
     * Called when a bean is removed from the bean context
     * @param obj the bean being removed
     */
    @Override
    public void findAndUndo(Object obj) {
        // Important notice:
        // The mechanism for adding and removing beans has been used in 
        // a wrong way in epd-shore, which has multiple ChartPanels.
        // When the "global" beans are added to a new ChartPanel, they
        // will be removed from the other ChartPanels using findAndUndo.
        // Hence, we do not reset the references to mapBean and mainFrame
        
        super.findAndUndo(obj);
    }

    /**
     * Returns {@code this} as the {@linkplain MapMouseListener}
     * @return this
     */
    @Override
    public MapMouseListener getMapMouseListener() {
        return this;
    }

    /**
     * Returns the mouse mode service list
     * @return the mouse mode service list
     */
    @Override
    public String[] getMouseModeServiceList() {
        return  EPD.getInstance().getDefaultMouseModeServiceList();
    }

    
    /**
     * Provides default behavior for right-clicks by
     * showing the general menu.
     * 
     * @param evt the mouse event
     */
    @Override
    public boolean mouseClicked(MouseEvent evt) {
        if (evt.getButton() == MouseEvent.BUTTON3) {
            mapMenu.generalMenu(true);
            mapMenu.setVisible(true);

            if (mainFrame.getHeight() < evt.getYOnScreen() + mapMenu.getHeight()) {
                mapMenu.show(this, evt.getX() - 2, evt.getY() - mapMenu.getHeight());
            } else {
                mapMenu.show(this, evt.getX() - 2, evt.getY() - 2);
            }
            return true;
        }

        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mouseDragged(MouseEvent arg0) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void mouseMoved() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mouseMoved(MouseEvent arg0) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mousePressed(MouseEvent arg0) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mouseReleased(MouseEvent arg0) {
        return false;
    }

    /**
     * Returns the mouse selection tolerance
     * @return the mouse selection tolerance
     */
    public float getMouseSelectTolerance() {
        return EPD.getInstance().getSettings().getGuiSettings().getMouseSelectTolerance();
    }
    
    /**
     * Returns the first graphics element placed at the mouse event location
     * that matches any of the types passed along. 
     * 
     * @param evt the mouse event
     * @param types the possible types
     * @return the first matching graphics element
     */
    public final OMGraphic getSelectedGraphic(MouseEvent evt, Class<?>... types) {
        return MapEventUtils.getSelectedGraphic(graphics, evt, getMouseSelectTolerance(), types);
    }
    
    /**
     * Returns a reference to the main frame
     * @return a reference to the main frame
     */
    public MainFrameCommon getMainFrame() {
        return mainFrame;
    }   

    /**
     * Returns a reference to the map menu
     * @return a reference to the map menu
     */
    public MapMenuCommon getMapMenu() {
        return mapMenu;
    }   

    /**
     * Returns a reference to the map frame
     * @return a reference to the map frame
     */
    public MapFrameCommon getMapFrame() {
        return mapFrame;
    }

    /**
     * Returns a reference to the glass pane
     * @return a reference to the glass pane
     */
    public JPanel getGlassPanel() {
        return (mapFrame != null) ? mapFrame.getGlassPanel() : mainFrame.getGlassPanel();
    }    

    /**
     * Returns the list of information panels in this layer
     * @return the list of information panels
     */
    public List<InfoPanel> getInfoPanels() {
        return infoPanels;
    }

    /**
     * Registers the list of {@linkplain InfoPanel} panels.
     * <p>
     * These panels will automatically be added to the glass pane.
     * 
     * @param infoPanels the {@linkplain InfoPanel} panels to register
     */
    protected void registerInfoPanels(InfoPanel... infoPanels) {
        for (InfoPanel infoPanel : infoPanels) {
            this.infoPanels.add(infoPanel);
        }
    }
    
    /**
     * Called when a glass pane has been resolved.
     * Adds all info panels to the glass pane
     */
    private void addInfoPanelsToGlassPane() {
        for (InfoPanel infoPanel : infoPanels) {
            getGlassPanel().add(infoPanel);
        }
    }    
}
