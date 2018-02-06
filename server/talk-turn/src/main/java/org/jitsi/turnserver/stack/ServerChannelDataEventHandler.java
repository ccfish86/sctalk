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

package org.jitsi.turnserver.stack;

import java.util.Arrays;

import org.ice4j.ChannelDataMessageEvent;
import org.ice4j.StunException;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.message.ChannelData;
import org.ice4j.stack.ChannelDataEventHandler;
import org.ice4j.stack.RawMessage;
import org.ice4j.stack.StunStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle incoming ChannelData messages coming from Client to Server. It first finds if
 * there is a ChannelBind installed for the peer. If yes it then sends the UDP message to peer. If
 * no it then silently ignores the message.
 * 
 * @author Aakash Garg
 */
public class ServerChannelDataEventHandler implements ChannelDataEventHandler {

    /**
     * The <tt>Logger</tt> used by the <tt>ServerChannelDataEventHandler</tt> class and its
     * instances for logging output.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(ServerChannelDataEventHandler.class.getName());

    /**
     * The turnStack to call.
     */
    private TurnStack turnStack;

    /**
     * Default Constructor.
     */
    public ServerChannelDataEventHandler() {}

    /**
     * Parametrized constructor.
     * 
     * @param turnStack the turnStack to set for this class.
     */
    public ServerChannelDataEventHandler(StunStack turnStack) {
        if (turnStack instanceof TurnStack) {
            this.turnStack = (TurnStack) turnStack;
        } else {
            throw new IllegalArgumentException("This is not a TurnStack!");
        }
    }

    /**
     * Sets the TurnStack for this class.
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

        ChannelData channelData = evt.getChannelDataMessage();
        char channelNo = channelData.getChannelNumber();
        byte[] data = channelData.getData();
        logger.trace("Received a ChannelData message for " + (int) channelNo + " , message : "
                + Arrays.toString(data));

        TransportAddress clientAddress = evt.getRemoteAddress();
        TransportAddress serverAddress = evt.getLocalAddress();
        Transport transport = Transport.UDP;
        FiveTuple fiveTuple = new FiveTuple(clientAddress, serverAddress, transport);

        Allocation allocation = this.turnStack.getServerAllocation(fiveTuple);

        if (allocation == null) {
            logger.trace("allocation not found.");
        } else if (!allocation.containsChannel(channelNo)) {
            logger.trace("ChannelNo " + (int) channelNo + " not found in Allocation!");
            return;
        }
        TransportAddress destAddr = allocation.getPeerAddr(channelNo);
        if (destAddr != null) {
            RawMessage message =
                    RawMessage.build(data, data.length, destAddr, allocation.getClientAddress());
            try {
                logger.trace("Dispatching a UDP message to " + destAddr + ", data: "
                        + Arrays.toString(message.getBytes()));
                this.turnStack.sendUdpMessage(message, destAddr, allocation.getRelayAddress());
            } catch (StunException e) {
                logger.trace(e.getMessage());
            }
        } else {
            logger.trace("Peer address not found for channel " + (int) channelNo);
        }

    }

}
