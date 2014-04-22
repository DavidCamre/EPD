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
package dk.dma.epd.common.prototype.settings.handlers;

/**
 * Maintains settings for a METOC handler.
 * 
 * @author Janus Varmarken
 */
public class MetocHandlerCommonSettings<OBSERVER extends MetocHandlerCommonSettings.IObserver>
        extends HandlerSettings<OBSERVER> {

    /**
     * How long should METOC for route be considered valid. Unit: minutes.
     */
    private int metocTtl = 60;

    /**
     * The minimum interval between METOC polls for active route. 0 means never.
     * Unit: minutes.
     */
    private int activeRouteMetocPollInterval = 5;

    /**
     * The tolerance of how long we may drift from plan before METOC is
     * considered invalid. Unit: minutes.
     */
    private int metocTimeDiffTolerance = 15;

    /**
     * Get the setting that specifies how long (in minutes) METOC data for a
     * route is considered valid.
     * 
     * @return How long METOC data for a route is considered valid. Unit is
     *         minutes.
     */
    public int getMetocTtl() {
        try {
            this.settingLock.readLock().lock();
            return this.metocTtl;
        } finally {
            this.settingLock.readLock().unlock();
        }
    }

    /**
     * Changes the setting that specifies how long (in minutes) METOC data for a
     * route is considered valid.
     * 
     * @param metocTtl
     *            How long METOC data for a route is considered valid. Unit is
     *            minutes.
     */
    public void setMetocTtl(final int metocTtl) {
        try {
            this.settingLock.writeLock().lock();
            if (this.metocTtl == metocTtl) {
                // No change, no need to notify observers.
                return;
            }
            // There was a change, update and notify.
            this.metocTtl = metocTtl;
            for (OBSERVER obs : this.observers) {
                obs.metocTtlChanged(metocTtl);
            }
        } finally {
            this.settingLock.writeLock().unlock();
        }
    }

    /**
     * Get the setting that specifies the minimum interval between METOC polls
     * for an active route. 0 means never. Unit: minutes.
     * 
     * @return The minimum interval between METOC polls for an active route in
     *         minutes. 0 means never.
     */
    public int getActiveRouteMetocPollInterval() {
        try {
            this.settingLock.readLock().lock();
            return this.activeRouteMetocPollInterval;
        } finally {
            this.settingLock.readLock().unlock();
        }
    }

    /**
     * Changes the setting that specifies the minimum interval between METOC
     * polls for an active route. 0 means never. Unit: minutes.
     * 
     * @param activeRouteMetocPollInterval
     *            The minimum interval between METOC polls for an active route
     *            in minutes. 0 means never.
     */
    public void setActiveRouteMetocPollInterval(
            final int activeRouteMetocPollInterval) {
        try {
            this.settingLock.writeLock().lock();
            if (this.activeRouteMetocPollInterval == activeRouteMetocPollInterval) {
                // No change, no need to notify observers.
                return;
            }
            // There was a change, update and notify observers.
            this.activeRouteMetocPollInterval = activeRouteMetocPollInterval;
            for (OBSERVER obs : this.observers) {
                obs.activeRouteMetocPollIntervalChanged(activeRouteMetocPollInterval);
            }
        } finally {
            this.settingLock.writeLock().unlock();
        }
    }

    /**
     * Get the setting that specifies the tolerance of how long a vessel may
     * drift from plan before METOC is considered invalid. Unit: minutes.
     * 
     * @return The maximum number of minutes a vessel may drift from plan before
     *         METOC is considered invalid.
     */
    public int getMetocTimeDiffTolerance() {
        try {
            this.settingLock.readLock().lock();
            return this.metocTimeDiffTolerance;
        } finally {
            this.settingLock.readLock().unlock();
        }
    }

    /**
     * Changes the setting that specifies the tolerance of how long a vessel may
     * drift from plan before METOC is considered invalid. Unit: minutes.
     * 
     * @param metocTimeDiffTolerance
     *            The maximum number of minutes a vessel may drift from plan
     *            before METOC is considered invalid.
     */
    public void setMetocTimeDiffTolerance(final int metocTimeDiffTolerance) {
        try {
            this.settingLock.writeLock().lock();
            if (this.metocTimeDiffTolerance == metocTimeDiffTolerance) {
                // No change, no need to notify observers.
                return;
            }
            // There was a change, update and notify observers.
            this.metocTimeDiffTolerance = metocTimeDiffTolerance;
            for (OBSERVER obs : this.observers) {
                obs.metocTimeDiffToleranceChanged(metocTimeDiffTolerance);
            }
        } finally {
            this.settingLock.writeLock().unlock();
        }
    }

    /**
     * Interface for observing a {@link MetocHandlerCommonSettings} for changes.
     * 
     * @author Janus Varmarken
     */
    public interface IObserver extends HandlerSettings.IObserver {

        /**
         * Invoked when {@link MetocHandlerCommonSettings#getMetocTtl()} has
         * changed.
         * 
         * @param newMetocTtl
         *            The new METOC time to live in minutes. See
         *            {@link MetocHandlerCommonSettings#getMetocTtl()} for more
         *            details.
         */
        void metocTtlChanged(int newMetocTtl);

        /**
         * Invoked when
         * {@link MetocHandlerCommonSettings#getActiveRouteMetocPollInterval()}
         * has changed.
         * 
         * @param newInterval
         *            The new interval. See
         *            {@link MetocHandlerCommonSettings#getActiveRouteMetocPollInterval()}
         *            for more details.
         */
        void activeRouteMetocPollIntervalChanged(int newInterval);

        /**
         * Invoked when
         * {@link MetocHandlerCommonSettings#getMetocTimeDiffTolerance()} has
         * changed.
         * 
         * @param metocTimeDiffTolerance
         *            The new tolerance. See
         *            {@link MetocHandlerCommonSettings#getMetocTimeDiffTolerance()}
         *            for more details.
         */
        void metocTimeDiffToleranceChanged(int metocTimeDiffTolerance);
    }
}