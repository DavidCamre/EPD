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
package dk.dma.epd.ship.service;

import dk.dma.enav.model.fal.FALReport;
import dk.dma.epd.common.prototype.enavcloud.FALReportingService.FALReportMessage;
import dk.dma.epd.common.prototype.enavcloud.FALReportingService.FALReportReply;
import dk.dma.epd.common.prototype.enavcloud.TODO;
import dk.dma.epd.common.prototype.service.FALHandlerCommon;
import dk.dma.epd.common.util.Util;
import dk.dma.epd.ship.fal.FALManager;
import net.maritimecloud.net.mms.MmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Ship specific intended route service implementation.
 * <p>
 * Listens for changes to the active route and broadcasts it. Also broadcasts the route periodically.
 * <p>
 * Improvements:
 * <ul>
 * <li>Use a worker pool rather than spawning a new thread for each broadcast.</li>
 * </ul>
 */
@SuppressWarnings("unused")
public class FALHandler extends FALHandlerCommon implements Runnable {

    /**
     * Protocols needed for VOCT Communication - may be further split or combined in future
     */
    private boolean running;
    private FALManager falManager;

    private List<TODO.ServiceEndpoint<FALReportMessage, FALReportReply>> falRecievers = new ArrayList<>();

    private static final Logger LOG = LoggerFactory.getLogger(FALHandler.class);

    /**
     * Constructor
     */
    public FALHandler() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cloudConnected(MmsClient connection) {
        super.cloudConnected(connection);
        //
        // // Register for RapidResponse
        // try {
        // getMmsClient().serviceRegister(FALReportingService.INIT,
        // new InvocationCallback<FALReportingService.FALReportMessage, FALReportingService.FALReportReply>() {
        // public void process(FALReportMessage message,
        // InvocationCallback.Context<FALReportingService.FALReportReply> context) {
        //
        // MaritimeId caller = context.getCaller();
        // long mmsi = MaritimeCloudUtils.toMmsi(context.getCaller());
        // //
        // // voctInvitations.put(message.getId(), mmsi);
        // // cloudStatus.markCloudReception();
        // //
        // // voctContextRapidResponse = context;
        // //
        // // voctManager.handleSARDataPackage(message);
        //
        // context.complete(new FALReportReply(message.getId(), EPDShip.getInstance().getOwnShipHandler()
        // .getMmsi(), System.currentTimeMillis()));
        // }
        // }).awaitRegistered(4, TimeUnit.SECONDS);
        //
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        //
        // Start thread to find recievers of FAL reports
        running = true;
        new Thread(this).start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cloudDisconnected() {
        running = false;
    }

    /**
     * Main thread run method. Broadcasts the intended route
     */
    public void run() {
        while (running) {
            Util.sleep(5000L);
            fetchSTCCList();
        }

    }

    /**
     * Fetches the list of Shore Centers that can recieve a FAL report
     */
    private void fetchSTCCList() {
        try {
// TODO: Maritime Cloud 0.2 re-factoring
//            falRecievers = MaritimeCloudUtils.findSTCCServices(getMmsClient().serviceLocate(FALReportingService.INIT)
//                    .nearest(Integer.MAX_VALUE).get());
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

    public void sendFALReport(long id, String message, FALReport falReport) {
        falReport.setSentDate(new Date());
        FALReportMessage falMessage = new FALReportMessage();
        falMessage.setFalReport(falReport);
        falMessage.setSentDate(new Date());

        // TODO: Maritime Cloud 0.2 re-factoring
        //boolean toSend = sendMaritimeCloudMessage(new MmsiId((int) id), falMessage, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findAndInit(Object obj) {
        super.findAndInit(obj);

        if (obj instanceof FALManager) {
            falManager = (FALManager) obj;
            // voctManager.addListener(this);
        }

    }

    /**
     * @return the falRecievers
     */
    public List<TODO.ServiceEndpoint<FALReportMessage, FALReportReply>> getFalRecievers() {
        return falRecievers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void findAndUndo(Object obj) {
        // if (obj instanceof RouteManager) {
        // routeManager.removeListener(this);
        // routeManager = null;
        // }
        super.findAndUndo(obj);
    }
}
