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

import org.ice4j.StunMessageEvent;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.message.Message;
import org.ice4j.message.MessageFactory;
import org.ice4j.message.Response;
import org.ice4j.stack.RequestListener;
import org.ice4j.stack.StunStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class that would be handling and responding to incoming requests that are validated and sends
 * a SUCCESS response
 * 
 * @author Aakash Garg
 */
public class BindingRequestListener implements RequestListener {
    /**
     * The <tt>Logger</tt> used by the <tt>BindingRequestListener</tt> class and its instances for
     * logging output.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(BindingRequestListener.class.getName());

    private final StunStack stunStack;

    /**
     * The indicator which determines whether this <tt>ValidatedrequestListener</tt> is currently
     * started.
     */
    private boolean started = false;

    /**
     * Creates a new BindingRequestListener
     * 
     * @param turnStack
     */
    public BindingRequestListener(StunStack stunStack) {
        this.stunStack = stunStack;
    }

    @Override
    public void processRequest(StunMessageEvent evt) throws IllegalArgumentException {

        Message message = evt.getMessage();

        if (message.getMessageType() == Message.BINDING_REQUEST) {
            logger.trace("Received a Binding Request from " + evt.getRemoteAddress());
            TransportAddress mappedAddress = evt.getRemoteAddress();
            // Response response =
            // MessageFactory.createBindingResponse(request,mappedAddress);
            TransportAddress sourceAddress = evt.getLocalAddress();
            TransportAddress changedAddress =
                    new TransportAddress("stunserver.org", 3489, Transport.UDP);
            Response response = MessageFactory.create3489BindingResponse(mappedAddress,
                    sourceAddress, changedAddress);

            try {
                stunStack.sendResponse(evt.getTransactionID().getBytes(), response,
                        evt.getLocalAddress(), evt.getRemoteAddress());
                logger.trace("Binding Response Sent.");
            } catch (Exception e) {
                logger.warn("Failed to send " + response + " through " + evt.getLocalAddress(), e);
                // try to trigger a 500 response although if this one failed,
                throw new RuntimeException("Failed to send a response", e);
            }
        }
    }

    /**
     * Starts this <tt>BindingRequestListener</tt>. If it is not currently running, does nothing.
     */
    public void start() {
        if (!started) {
            stunStack.addRequestListener(this);
            started = true;
        }
    }

    /**
     * Stops this <tt>ValidatedRequestListenerr</tt>. A stopped <tt>ValidatedRequestListenerr</tt>
     * can be restarted by calling {@link #start()} on it.
     */
    public void stop() {
        stunStack.removeRequestListener(this);
        started = false;
    }
}
