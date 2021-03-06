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
package dk.dma.epd.common;

import dk.dma.epd.common.prototype.EPD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JOptionPane;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Exception handler for uncaught exceptions. 
 */
public class ExceptionHandler implements UncaughtExceptionHandler {
    
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);
    
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOG.error("Uncaught exception from thread " + t.getName(), e);
        int result = JOptionPane.showConfirmDialog(
                null, 
                "An error has occured! If the problem persists please contact an administrator.\nRestart application?", 
                "Error occured",
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.ERROR_MESSAGE);
        boolean restart = result == JOptionPane.YES_OPTION;
        EPD.getInstance().setRestart(restart);
        System.exit(restart ? 2 : 0);
    }

}
