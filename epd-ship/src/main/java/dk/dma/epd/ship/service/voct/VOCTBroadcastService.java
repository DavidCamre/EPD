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
package dk.dma.epd.ship.service.voct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.enav.model.voyage.Route;
import dk.dma.epd.common.prototype.enavcloud.EnavRouteBroadcast;
import dk.dma.epd.common.prototype.model.route.IRoutesUpdateListener;
import dk.dma.epd.common.prototype.model.route.RoutesUpdateEvent;
import dk.dma.epd.common.prototype.sensor.nmea.IPntListener;
import dk.dma.epd.common.prototype.sensor.nmea.PntMessage;
import dk.dma.epd.ship.service.EnavService;
import dk.dma.epd.ship.service.EnavServiceHandler;
import dk.dma.epd.ship.service.intendedroute.ActiveRouteProvider;

/**
 * Intended route service implementation
 */
public class VOCTBroadcastService extends EnavService implements
        IRoutesUpdateListener, IPntListener {

    private static final Logger LOG = LoggerFactory
            .getLogger(VOCTBroadcastService.class);

    /**
     * The current active route provider
     */
    private final ActiveRouteProvider provider;
    private final VOCTManager voctManager;

    public VOCTBroadcastService(EnavServiceHandler enavServiceHandler,
            ActiveRouteProvider provider, VOCTManager voctManager) {
        super(enavServiceHandler);
        this.provider = provider;
        this.voctManager = voctManager;
    }

    
    
    /**
     * Broadcast intended route
     */
    // @ScheduleWithFixedDelay(10000)
    public void broadcastVOCTMessage() {
        System.out.println("BROADCAST INTENDED ROUTE");

        // Get active route from provider
        LOG.info("Get active route");

        provider.getActiveRoute();
        
        LOG.info("Got active route");
        
     // Make intended route message
        EnavRouteBroadcast message = new EnavRouteBroadcast();
        
        if (provider.getActiveRoute() != null){
            
            message.setIntendedRoute(provider.getActiveRoute().getFullRouteData());
            
        }else{
            System.out.println("Active route is null!");
            message.setIntendedRoute(new Route());
        }
        
        
        
        
        // send message
        LOG.info("Sending VOCT Broadcast");
//        try {
//            enavServiceHandler.sendMessage(message);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        
    }

    
    
    /**
     * Handle event of active route change
     */
    @Override
    public void routesChanged(RoutesUpdateEvent e) {
        if (e != null) {
            if (e.is(RoutesUpdateEvent.ACTIVE_ROUTE_UPDATE,
                    RoutesUpdateEvent.ACTIVE_ROUTE_FINISHED,
                    RoutesUpdateEvent.ROUTE_ACTIVATED,
                    RoutesUpdateEvent.ROUTE_DEACTIVATED)) {
                
                //Check if the active route is the same as the sar search pattern
                
                broadcastVOCTMessage();
            }
        }
    }



    @Override
    public void receive(PntMessage pntMessage) {
        // TODO Auto-generated method stub
        
    }
}