/*
 * TurnServer, the OpenSource Java Solution for TURN protocol. Maintained by the Jitsi community
 * (http://jitsi.org).
 *
 * Copyright @ 2015 Atlassian Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.jitsi.turnserver.turnClient;

import java.io.UnsupportedEncodingException;

import org.ice4j.ChannelDataMessageEvent;
import org.ice4j.message.ChannelData;
import org.ice4j.stack.ChannelDataEventHandler;
import org.ice4j.stack.StunStack;
import org.jitsi.turnserver.stack.TurnStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the incoming ChannelData message for Client from Server.
 * 
 * @author Aakash Garg
 * 
 */
public class ClientChannelDataEventHandler implements ChannelDataEventHandler {

    /**
     * The <tt>Logger</tt> used by the <tt>ServerChannelDataEventHandler</tt> class and its
     * instances for logging output.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(ClientChannelDataEventHandler.class.getName());

    /**
     * The turnStack to call.
     */
    private TurnStack turnStack;

    /**
     * Default constructor.
     */
    public ClientChannelDataEventHandler() {}

    /**
     * parametrised contructor.
     * 
     * @param turnStack the turnStack for this class.
     */
    public ClientChannelDataEventHandler(StunStack turnStack) {
        if (turnStack instanceof TurnStack) {
            this.turnStack = (TurnStack) turnStack;
        } else {
            throw new IllegalArgumentException("This is not a TurnStack!");
        }
    }

    /**
     * Sets the turnStack for this class.
     * 
     * @param turnStack the turnStack to set for this class.
     */
    public void setTurnStack(TurnStack turnStack) {
        this.turnStack = turnStack;
    }

    /**
     * Handles the ChannelDataMessageEvent.
     * 
     * @param evt the ChannelDataMessageEvent to handle/process.
     */
    @Override
    public void handleMessageEvent(ChannelDataMessageEvent evt) {

        logger.trace("Received ChannelData message " + evt);

        ChannelData channelData = evt.getChannelDataMessage();
        char channelNo = channelData.getChannelNumber();
        byte[] data = channelData.getData();
        try {
            String line = new String(data, "UTF-8");
            logger.debug(line);
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unable to get back String.");
        }
        /**
         * logger.debug("Received a ChannelData message for " + (int) channelNo + " , message
         * : " + Arrays.toString(data));
         **/
    }
}
