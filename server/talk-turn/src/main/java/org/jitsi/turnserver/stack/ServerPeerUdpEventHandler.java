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

import org.ice4j.PeerUdpMessageEvent;
import org.ice4j.StunException;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.message.ChannelData;
import org.ice4j.message.Indication;
import org.ice4j.message.MessageFactory;
import org.ice4j.stack.PeerUdpMessageEventHandler;
import org.ice4j.stack.RawMessage;
import org.ice4j.stack.StunStack;
import org.ice4j.stack.TransactionID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle UDP messages coming from Peer. The class first checks if there is a non-expired
 * ChannelBind for the peer if yes it then sends a ChannelData message to Client. If no it then
 * finds if there is a non-expired permission if yes then it sends a DataIndicatio to Client. All
 * the mesages sent to client here are from the address on which the allocation request was received
 * or the serverAddress of fiveTuple of corresponding Allocation.
 * 
 * @author Aakash Garg
 */
public class ServerPeerUdpEventHandler implements PeerUdpMessageEventHandler {
    /**
     * The <tt>Logger</tt> used by the <tt>PeerUdpMessageEventHandler</tt> class and its instances
     * for logging output.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(ServerPeerUdpEventHandler.class.getName());

    /**
     * The turnStack to call.
     */
    private TurnStack turnStack;

    /**
     * Default constructor.
     */
    public ServerPeerUdpEventHandler() {}

    /**
     * Parametrized constructor.
     * 
     * @param turnStack the turnStack to set for this class.
     */
    public ServerPeerUdpEventHandler(StunStack turnStack) {
        if (turnStack instanceof TurnStack) {
            this.turnStack = (TurnStack) turnStack;
        } else {
            throw new IllegalArgumentException("This is not a TurnStack!");
        }
    }

    public void setTurnStack(TurnStack turnStack) {
        this.turnStack = turnStack;
    }

    /**
     * Handles the PeerUdpMessageEvent.
     * 
     * @param evt the PeerUdpMessageEvent to handle/process.
     */
    @Override
    public void handleMessageEvent(PeerUdpMessageEvent evt) {

        logger.trace("Received Peer UdP message message {}", evt);

        byte[] data = evt.getBytes();
        TransportAddress localAddress = evt.getLocalAddress();
        TransportAddress remoteAddress = evt.getRemoteAddress();
        logger.debug(
                "Received a UDP message on: " + localAddress + ", data: " + byteArrayToHex(data));
        Allocation allocation = this.turnStack.getServerAllocation(localAddress);
        if (remoteAddress.getTransport() == Transport.TCP) {
            FiveTuple fiveTuple = new FiveTuple(remoteAddress, localAddress, Transport.TCP);
            logger.debug("Send message request from " + fiveTuple);
            if (allocation == null) // came from client
            {
                // get client allocation
                logger.debug("Message came from TCP Client");
                int connectionId = this.turnStack.getConnectionIdForDataConn(fiveTuple);
                logger.debug("Connection Id extracted for " + fiveTuple + " is " + connectionId);
                allocation = this.turnStack.getAllocationFromConnectionId(connectionId);
                logger.debug("Allocation extracted is " + allocation + " for client-" + fiveTuple);
                FiveTuple peerTuple = allocation.getPeerTCPConnection(connectionId);
                TransportAddress peerAddress = peerTuple.getClientTransportAddress();
                TransportAddress relayAddress = peerTuple.getServerTransportAddress();
                RawMessage rawMessage =
                        RawMessage.build(data, data.length, peerAddress, relayAddress);
                try {
                    logger.debug("Relaying data to peer-" + peerAddress + " from " + remoteAddress
                            + " data-");
                    this.turnStack.sendUdpMessage(rawMessage, peerAddress, relayAddress);
                } catch (StunException e) {
                    logger.warn(
                            "Unable to relay message to peer-" + peerAddress + " from client-"
                                    + remoteAddress + " message-" + Arrays.toString(data));
                }

            } else {
                // else came from peer
                logger.debug("Message came from TCP peer.");
                int connectionId = this.turnStack.getConnectionIdForPeer(fiveTuple);
                if (!allocation.isPermitted(remoteAddress)) {
                    logger.debug("No permission installed for peer-" + remoteAddress);
                    return;
                } else {
                    TransportAddress dataConn =
                            allocation.getDataConnection(connectionId).getClientTransportAddress();
                    if (dataConn != null) {
                        RawMessage rawMessage = RawMessage.build(data, data.length, dataConn,
                                allocation.getServerAddress());
                        try {
                            logger.debug("Relaying data to client-" + dataConn + " from peer-"
                                    + remoteAddress);
                            this.turnStack.sendUdpMessage(rawMessage, dataConn,
                                    allocation.getServerAddress());
                        } catch (StunException e) {
                            logger.warn(
                                    "Unable to relay message to client-" + dataConn + " from peer-"
                                            + remoteAddress + " message-" + Arrays.toString(data));
                        }
                    } else {
                        logger.debug("No data connection found for peer-" + remoteAddress);
                    }
                }
            }
        } else if (allocation != null && allocation.getChannel(remoteAddress) != 0x1000) {
            char channelNo = allocation.getChannel(remoteAddress);
            ChannelData channelData = new ChannelData();
            channelData.setChannelNumber(channelNo);
            channelData.setData(data);
            try {
                logger.debug("Sending a ChannelData message " + channelData + " from "
                        + allocation.getServerAddress() + " to " + allocation.getClientAddress());

                this.turnStack.sendChannelData(channelData, allocation.getClientAddress(),
                        allocation.getServerAddress());
            } catch (StunException ex) {
                logger.trace(ex.getMessage());
            }
        } else if (allocation != null && allocation.isPermitted(remoteAddress)) {
            TransactionID tranID = TransactionID.createNewTransactionID();
            Indication dataInd =
                    MessageFactory.createDataIndication(remoteAddress, data, tranID.getBytes());
            try {
                logger.debug("Sending a ChannelData message " + dataInd + " from "
                        + allocation.getServerAddress() + " to " + allocation.getClientAddress());

                this.turnStack.sendIndication(dataInd, allocation.getClientAddress(),
                        allocation.getServerAddress());
            } catch (StunException e) {
                logger.trace(e.getMessage());
            }
        } else {
            logger.debug("unable to find allocation and the message is not on TCP.");
        }
    }

    private String byteArrayToHex(byte[] data) {
        String arrayToHex = "";
        for (int i = 0; i < data.length; i++) {
            arrayToHex += String.format("%02X, ", data[i]);
        }
        return arrayToHex;
    }

}
