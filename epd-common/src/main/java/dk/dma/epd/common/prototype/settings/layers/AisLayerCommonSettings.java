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
package dk.dma.epd.common.prototype.settings.layers;

import java.util.Properties;

import com.bbn.openmap.util.PropUtils;

import dk.dma.epd.common.prototype.layers.ais.AisLayerCommon;
import dk.dma.epd.common.prototype.settings.ObservedSettings;

/**
 * Maintains settings relevant to an {@link AisLayerCommon} or any of its
 * subclasses. This class inherits from {@link ObservedSettings} allowing
 * clients to register for notifications of changes to any setting maintained by
 * this class.
 * 
 * @param <OBSERVER>
 *            The type of the observers observing the
 *            {@code AisLayerCommonSettings} for changes.
 * @author Janus Varmarken
 */
public abstract class AisLayerCommonSettings<OBSERVER extends IAisLayerCommonSettingsObserver>
        extends LayerSettings<OBSERVER> {

    /**
     * The setting key for the "show all AIS names" setting.
     */
    private static final String KEY_SHOW_ALL_AIS_NAMES = "showAllAisNameLabels";

    /**
     * The setting key for the "show all past tracks" setting.
     */
    private static final String KEY_SHOW_ALL_PAST_TRACKS = "showAllPastTracks";

    /**
     * The setting key for the setting that specifies how often the layer should
     * repaint itself.
     */
    private static final String KEY_LAYER_REDRAW_INTERVAL = "layerRedrawInterval";

    /**
     * Specifies if all AIS name labels should be shown.
     */
    private boolean showAllAisNameLabels = true;

    /**
     * Specifies if all past tracks should be shown.
     */
    private boolean showAllPastTracks;

    /**
     * Setting specifying how often the layer should repaint itself.
     */
    private int layerRedrawInterval = 5;

    /**
     * Get the value of the setting specifying if all AIS name labels should be
     * shown.
     * 
     * @return {@code true} if all AIS name labels should be shown,
     *         {@code false} if all AIS name labels should be hidden.
     */
    public boolean isShowAllAisNameLabels() {
        try {
            this.settingLock.readLock().lock();
            return this.showAllAisNameLabels;
        } finally {
            this.settingLock.readLock().unlock();
        }
    }

    /**
     * Changes the setting specifying if all AIS name labels should be shown.
     * The registered observers are notified of this change.
     * 
     * @param show
     *            {@code true} to show all AIS name labels, {@code false} to
     *            hide all AIS name labels.
     */
    public void setShowAllAisNameLabels(boolean show) {
        this.settingLock.writeLock().lock();
        boolean oldVal = this.showAllAisNameLabels;
        this.showAllAisNameLabels = show;
        // Notify observers of change to this setting
        for (OBSERVER obs : this.observers) {
            obs.showAllAisNameLabelsChanged(oldVal, this.showAllAisNameLabels);
        }
        this.settingLock.writeLock().unlock();
    }

    /**
     * Get the value of the setting specifying if all past tracks should be
     * shown.
     * 
     * @return {@code true} if all past tracks should be shown, {@code false} if
     *         all past tracks should be hidden.
     */
    public boolean isShowAllPastTracks() {
        try {
            this.settingLock.readLock().lock();
            return this.showAllPastTracks;
        } finally {
            this.settingLock.readLock().unlock();
        }
    }

    /**
     * Changes the setting specifying if all past tracks should be shown. The
     * registered observers are notified of this change.
     * 
     * @param show
     *            {@code true} if all past tracks should be shown, {@code false}
     *            if all past tracks should be hidden.
     */
    public void setShowAllPastTracks(boolean show) {
        this.settingLock.writeLock().lock();
        boolean oldVal = this.showAllPastTracks;
        this.showAllPastTracks = show;
        // Notify observers of change to this setting
        for (OBSERVER obs : this.observers) {
            obs.showAllPastTracksChanged(oldVal, this.showAllPastTracks);
        }
        this.settingLock.writeLock().unlock();
    }

    /**
     * Get the value of the setting specifying how often the associated AIS
     * layer(s) should repaint itself/themselves.
     * 
     * @return The number of seconds between each repaint.
     */
    public int getLayerRedrawInterval() {
        try {
            this.settingLock.readLock().lock();
            return this.layerRedrawInterval;
        } finally {
            this.settingLock.readLock().unlock();
        }
    }

    /**
     * Changes the setting specifying how often the associated AIS layer(s)
     * should repaint itself/themselves.
     * 
     * @param seconds
     *            The number of seconds between each repaint.
     * @throws IllegalArgumentException
     *             if {@code seconds} is less than 1.
     */
    public void setLayerRedrawInterval(int seconds) {
        // Sanity check setting value
        if (seconds < 1) {
            throw new IllegalArgumentException(
                    "A redraw interval below 1 second is not allowed.");
        }
        this.settingLock.writeLock().lock();
        int oldVal = this.layerRedrawInterval;
        this.layerRedrawInterval = seconds;
        for (OBSERVER obs : this.observers) {
            obs.layerRedrawIntervalChanged(oldVal, this.layerRedrawInterval);
        }
        this.settingLock.writeLock().unlock();
    }

    @Override
    protected void onLoadSuccess(Properties settings) {
        /*
         * We acquire the lock here even though the individual setters acquire
         * the lock themselves too. This is to ensure that all settings are
         * loaded as a single batch.
         */
        this.settingLock.writeLock().lock();
        this.setShowAllAisNameLabels(PropUtils.booleanFromProperties(settings,
                KEY_SHOW_ALL_AIS_NAMES, this.isShowAllAisNameLabels()));
        this.setShowAllPastTracks(PropUtils.booleanFromProperties(settings,
                KEY_SHOW_ALL_PAST_TRACKS, this.isShowAllPastTracks()));
        this.setLayerRedrawInterval(PropUtils.intFromProperties(settings,
                KEY_LAYER_REDRAW_INTERVAL, this.getLayerRedrawInterval()));
        /*
         * TODO init other settings variables based on the provided Properties
         * instance...
         */

        // Release the lock.
        this.settingLock.writeLock().unlock();
    }

    @Override
    protected Properties onSaveSettings() {
        /*
         * We acquire the lock here even though the individual getters acquire
         * the lock themselves too. This is to ensure that the saved settings
         * will be a snapshot of the entire set of settings values.
         */
        this.settingLock.readLock().lock();
        Properties savedVars = new Properties();
        savedVars.setProperty(KEY_SHOW_ALL_AIS_NAMES,
                Boolean.toString(this.isShowAllAisNameLabels()));
        savedVars.setProperty(KEY_SHOW_ALL_PAST_TRACKS,
                Boolean.toString(this.isShowAllPastTracks()));
        savedVars.setProperty(KEY_LAYER_REDRAW_INTERVAL,
                Integer.toString(this.getLayerRedrawInterval()));
        /*
         * TODO store other settings variables based on field values...
         */

        // Release the lock.
        this.settingLock.readLock().unlock();
        return savedVars;
    }
}
