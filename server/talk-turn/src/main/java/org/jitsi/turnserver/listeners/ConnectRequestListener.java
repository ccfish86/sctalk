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
import java.net.Socket;

import org.ice4j.StunMessageEvent;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.AttributeFactory;
import org.ice4j.attribute.ConnectionIdAttribute;
import org.ice4j.attribute.ErrorCodeAttribute;
import org.ice4j.attribute.XorPeerAddressAttribute;
import org.ice4j.message.Message;
import org.ice4j.message.MessageFactory;
import org.ice4j.message.Response;
import org.ice4j.socket.IceTcpSocketWrapper;
import org.ice4j.stack.RequestListener;
import org.ice4j.stack.StunStack;
import org.jitsi.turnserver.stack.Allocation;
import org.jitsi.turnserver.stack.FiveTuple;
import org.jitsi.turnserver.stack.TurnStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class that would be handling and responding to incoming Connect requests that are validated
 * and sends a success or error response
 * 
 * @author Aakash Garg
 */
public class ConnectRequestListener implements RequestListener {

    /**
     * The <tt>Logger</tt> used by the <tt>ConnectRequestListener</tt> class and its instances for
     * logging output.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(ConnectRequestListener.class.getName());

    private final TurnStack turnStack;

    /**
     * The indicator which determines whether this <tt>ValidatedrequestListener</tt> is currently
     * started.
     */
    private boolean started = false;

    /**
     * Creates a new ConnectRequestListener
     * 
     * @param turnStack
     */
    public ConnectRequestListener(StunStack stunStack) {
        this.turnStack = (TurnStack) stunStack;
    }

    @Override
    public void processRequest(StunMessageEvent evt) throws IllegalArgumentException {

        Message message = evt.getMessage();
        if (message.getMessageType() == Message.CONNECT_REQUEST) {
            logger.trace("Received Connect request " + evt);

            Character errorCode = null;
            XorPeerAddressAttribute peerAddress = null;
            FiveTuple fiveTuple = null;
            Response response = null;
            ConnectionIdAttribute connectionId = null;
            if (!message.containsAttribute(Attribute.XOR_PEER_ADDRESS)) {
                errorCode = ErrorCodeAttribute.BAD_REQUEST;
            } else {
                peerAddress =
                        (XorPeerAddressAttribute) message.getAttribute(Attribute.XOR_PEER_ADDRESS);
                peerAddress.setAddress(peerAddress.getAddress(), evt.getTransactionID().getBytes());
                logger.trace("Peer Address requested : " + peerAddress.getAddress());
                TransportAddress clientAddress = evt.getRemoteAddress();
                TransportAddress serverAddress = evt.getLocalAddress();
                Transport transport = evt.getLocalAddress().getTransport();
                fiveTuple = new FiveTuple(clientAddress, serverAddress, transport);
                Allocation allocation = this.turnStack.getServerAllocation(fiveTuple);
                if (allocation == null) {
                    errorCode = ErrorCodeAttribute.ALLOCATION_MISMATCH;
                } else if (!allocation.isPermitted(peerAddress.getAddress())) {
                    errorCode = ErrorCodeAttribute.FORBIDDEN;
                } else {
                    // code for processing the connect request.
                    connectionId = AttributeFactory.createConnectionIdAttribute();
                    logger.trace("Created ConnectionID : " + connectionId.getConnectionIdValue());
                    try {
                        Socket socket = new Socket(peerAddress.getAddress().getAddress(),
                                peerAddress.getAddress().getPort());
                        socket.setSoTimeout(30 * 1000);
                        IceTcpSocketWrapper iceSocket = new IceTcpSocketWrapper(socket);
                        this.turnStack.addSocket(iceSocket);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    /**
                     * TODO Create a new TCP connection to the peer from relay address. wait for
                     * at-least 30 seconds. If it fails then send 447 error code.
                     **/
                    this.turnStack.addUnAcknowlededConnectionId(connectionId.getConnectionIdValue(),
                            peerAddress.getAddress(), allocation);
                    logger.trace("Creating Connect Success Response.");
                    response = MessageFactory
                            .createConnectResponse(connectionId.getConnectionIdValue());

                }
            }
            if (errorCode != null) {
                response = MessageFactory.createConnectErrorResponse(errorCode);
                logger.trace("error Code : " + (int) errorCode + " on ConnectRequest");
            }
            try {
                logger.trace("Sending Connect Response");
                turnStack.sendResponse(evt.getTransactionID().getBytes(), response,
                        evt.getLocalAddress(), evt.getRemoteAddress());
            } catch (Exception e) {
                logger.warn("Failed to send " + response + " through " + evt.getLocalAddress(), e);
                // try to trigger a 500 response although if this one failed,
                throw new RuntimeException("Failed to send a response", e);
            }
        } else {
            return;
        }

    }

    /**
     * Starts this <tt>ConnectRequestListener</tt>. If it is not currently running, does nothing.
     */
    public void start() {
        if (!started) {
            turnStack.addRequestListener(this);
            started = true;
        }
    }

    /**
     * Stops this <tt>ConnectRequestListenerr</tt>. A stopped <tt>ConnectRequestListenerr</tt> can
     * be restarted by calling {@link #start()} on it.
     */
    public void stop() {
        turnStack.removeRequestListener(this);
        started = false;
    }

}
