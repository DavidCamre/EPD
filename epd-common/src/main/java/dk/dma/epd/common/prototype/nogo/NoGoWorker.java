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
package dk.dma.epd.common.prototype.nogo;

import java.util.Date;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.dma.enav.model.geometry.Position;
import dk.dma.epd.common.prototype.communication.webservice.ShoreServiceException;
import dk.dma.epd.common.prototype.shoreservice.ShoreServicesCommon;
import dk.frv.enav.common.xml.nogo.response.NogoResponse;
import dk.frv.enav.common.xml.nogoslices.response.NogoResponseSlices;

public class NoGoWorker extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(NoGoWorker.class);

    private NogoHandlerCommon nogoHandler;
    private ShoreServicesCommon shoreServices;
    int id;
    double draught;
    Position northWestPoint;
    Position southEastPoint;
    Date validFrom;
    Date validTo;
    int slices;

    public NoGoWorker(NogoHandlerCommon nogoHandler, ShoreServicesCommon shoreCommon, int id, int slices) {
        this.nogoHandler = nogoHandler;
        shoreServices = shoreCommon;
        this.id = id;
        this.slices = slices;
    }

    public void setValues(double draught, Position northWestPoint, Position southEastPoint, DateTime startDate, DateTime endDate) {

        this.draught = draught;
        this.northWestPoint = northWestPoint;
        this.southEastPoint = southEastPoint;
        this.validFrom = new Date(startDate.getMillis());
        this.validTo = new Date(endDate.getMillis());

    }

    @Override
    public void run() {

        LOG.info("NoGo Worker has started its request");

        if (shoreServices == null) {
            nogoHandler.noNetworkConnection();
            // Send fault message

            return;
        }

        try {

            if (slices == 1) {

                NogoResponse nogoResponse = shoreServices.nogoPoll(draught, northWestPoint, southEastPoint, validFrom, validTo);

                // Check the nogoresponse stuff

                if (nogoResponse == null || nogoResponse.getPolygons() == null) {
                    nogoHandler.nogoTimedOut();
                    return;
                }

                // Store results

                nogoHandler.nogoRequestCompleted(nogoResponse);
            } else {
                NogoResponseSlices nogoResponse = shoreServices.nogoPoll(draught, northWestPoint, southEastPoint, validFrom,
                        validTo, slices);

                // Check the nogoresponse stuff

                if (nogoResponse == null || nogoResponse.getResponses() == null) {
                    nogoHandler.nogoTimedOut();
                    nogoHandler.setNoGoRequestCompleted();
                    return;
                }

                // Store the responses
                nogoHandler.nogoRequestCompleted(nogoResponse);

                LOG.info("NoGo Worker has completed its request");

            }
        } catch (ShoreServiceException e) {
            // TODO Auto-generated catch block
            nogoHandler.noNetworkConnection();
            LOG.error("Failed to get NoGo from shore: " + e.getMessage());

        }
        // Perform the thing
        nogoHandler.setNoGoRequestCompleted();
    }

}
