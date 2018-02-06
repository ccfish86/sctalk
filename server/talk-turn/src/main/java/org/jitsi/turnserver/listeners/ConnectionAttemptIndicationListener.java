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

package org.jitsi.turnserver.listeners;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.ice4j.StunException;
import org.ice4j.StunMessageEvent;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.ConnectionIdAttribute;
import org.ice4j.attribute.XorPeerAddressAttribute;
import org.ice4j.message.Indication;
import org.ice4j.message.Message;
import org.ice4j.message.MessageFactory;
import org.ice4j.message.Request;
import org.ice4j.socket.IceTcpSocketWrapper;
import org.ice4j.stack.RawMessage;
import org.ice4j.stack.TransactionID;
import org.ice4j.stunclient.BlockingRequestSender;
import org.jitsi.turnserver.IndicationListener;
import org.jitsi.turnserver.stack.Allocation;
import org.jitsi.turnserver.stack.TurnStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle events when a Connection Attempt Indication is received on CLient Side.
 * 
 * @author Aakash Garg
 * 
 */
public class ConnectionAttemptIndicationListener extends IndicationListener {
    /**
     * The <tt>Logger</tt> used by the <tt>DataIndicationListener</tt> class and its instances for
     * logging output.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(ConnectionAttemptIndicationListener.class.getName());

    /**
     * The request sender to use to send request to Turn server.
     */
    private BlockingRequestSender requestSender;

    /**
     * Constructor to create a ConnectionAttemptIndicationListener with specified turnStack to use
     * the requestSender will be null and a new {@link BlockingRequestSender} will be created with
     * new TCP socket to send request to server.
     * 
     * @param turnStack the turnStack to use for processing.
     */
    public ConnectionAttemptIndicationListener(TurnStack turnStack) {
        super(turnStack);
    }

    /**
     * Constructor to create a ConnectionAttemptIndicationListener with specified turnStack to use.
     * 
     * @param turnStack the turnStack to use for processing.
     * @param requestSender the requestSender to use to send request to server.
     */
    public ConnectionAttemptIndicationListener(TurnStack turnStack,
            BlockingRequestSender requestSender) {
        super(turnStack);
        this.requestSender = requestSender;
    }

    /**
     * The method to handle the incoming ConnectionAttempt Indications from Turn Server.
     */
    @Override
    public void handleIndication(Indication ind, Allocation alloc) {
        if (ind.getMessageType() == Message.CONNECTION_ATTEMPT_INDICATION) {
            logger.trace("Received a connection attempt indication.");
            byte[] tranId = ind.getTransactionID();
            ConnectionIdAttribute connectionId =
                    (ConnectionIdAttribute) ind.getAttribute(Attribute.CONNECTION_ID);
            XorPeerAddressAttribute peerAddress =
                    (XorPeerAddressAttribute) ind.getAttribute(Attribute.XOR_PEER_ADDRESS);
            peerAddress.setAddress(peerAddress.getAddress(), tranId);
            logger.trace("Received a Connection Attempt Indication with connectionId-"
                    + connectionId.getConnectionIdValue() + ", for peerAddress-"
                    + peerAddress.getAddress());
            Socket socket;
            try {
                socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 3478);
                IceTcpSocketWrapper sockWrapper = new IceTcpSocketWrapper(socket);
                this.getTurnStack().addSocket(sockWrapper);
                TransportAddress localAddr = new TransportAddress(sockWrapper.getLocalAddress(),
                        sockWrapper.getLocalPort(), Transport.TCP);
                logger.trace("New Local TCP socket chosen - " + localAddr);
                TransportAddress serverAddress =
                        new TransportAddress(InetAddress.getLocalHost(), 3478, Transport.TCP);
                StunMessageEvent evt = null;
                try {
                    Request connectionBindRequest = MessageFactory
                            .createConnectionBindRequest(connectionId.getConnectionIdValue());
                    TransactionID tranID = TransactionID.createNewTransactionID();
                    connectionBindRequest.setTransactionID(tranID.getBytes());
                    if (this.requestSender == null) {
                        logger.trace("Setting RequestSender");
                        this.requestSender =
                                new BlockingRequestSender(this.getTurnStack(), localAddr);
                    }
                    logger.trace("Sending ConnectionBind Request to " + serverAddress);
                    evt = requestSender.sendRequestAndWaitForResponse(connectionBindRequest,
                            serverAddress);
                    if (evt != null) {
                        Message msg = evt.getMessage();
                        if (msg.getMessageType() == Message.CONNECTION_BIND_SUCCESS_RESPONSE) {
                            logger.trace("Received a ConnectionBind Sucess Response.");
                            String myMessage = "Aakash Garg";
                            RawMessage rawMessage = RawMessage.build(myMessage.getBytes(),
                                    myMessage.length(), serverAddress, localAddr);
                            try {
                                logger.trace("--------------- Thread will now sleep for 20 sec.");
                                Thread.sleep(20 * 1000);
                            } catch (InterruptedException e) {
                                logger.error("Unable to stop thread");
                            }
                            logger.trace(">>>>>>>>>>>> Sending a Test message : ");
                            byte[] data = myMessage.getBytes();
                            for (int i = 0; i < data.length; i++) {
                                logger.debug(String.format("%02X, ", data[i]));
                            }
                            this.getTurnStack().sendUdpMessage(rawMessage, serverAddress,
                                    requestSender.getLocalAddress());
                        } else {
                            logger.trace("Received a ConnectionBind error Response - "
                                    + msg.getAttribute(Attribute.ERROR_CODE));
                        }
                    } else {
                        logger.debug("No response received to ConnectionBind request");
                    }
                } catch (StunException e) {
                    logger.trace("Unable to send ConnectionBind request");
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
