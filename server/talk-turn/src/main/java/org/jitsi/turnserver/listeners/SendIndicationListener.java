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

import org.ice4j.StunException;
import org.ice4j.TransportAddress;
import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.DataAttribute;
import org.ice4j.attribute.XorPeerAddressAttribute;
import org.ice4j.message.Indication;
import org.ice4j.message.Message;
import org.ice4j.stack.RawMessage;
import org.jitsi.turnserver.IndicationListener;
import org.jitsi.turnserver.stack.Allocation;
import org.jitsi.turnserver.stack.TurnStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle the incoming Send indications.
 * 
 * @author Aakash Garg
 * 
 */
public class SendIndicationListener extends IndicationListener {
    /**
     * The <tt>Logger</tt> used by the <tt>SendIndicationListener</tt> class and its instances for
     * logging output.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(SendIndicationListener.class.getName());

    /**
     * parametrised constructor.
     * 
     * @param turnStack the turnStack to set for this class.
     */
    public SendIndicationListener(TurnStack turnStack) {
        super(turnStack);
    }

    /**
     * Handles the incoming send indication.
     * 
     * @param ind the indication to handle.
     * @param alloc the allocation associated with message.
     */
    @Override
    public void handleIndication(Indication ind, Allocation alloc) {
        if (ind.getMessageType() == Message.SEND_INDICATION) {
            logger.trace("Received a Send Indication message.");
            byte[] tran = ind.getTransactionID();
            XorPeerAddressAttribute xorPeerAddress =
                    (XorPeerAddressAttribute) ind.getAttribute(Attribute.XOR_PEER_ADDRESS);
            xorPeerAddress.setAddress(xorPeerAddress.getAddress(), tran);
            DataAttribute data = (DataAttribute) ind.getAttribute(Attribute.DATA);
            TransportAddress peerAddr = xorPeerAddress.getAddress();
            if (alloc != null && alloc.isPermitted(peerAddr)) {
                RawMessage udpMessage = RawMessage.build(data.getData(), data.getDataLength(),
                        peerAddr, alloc.getRelayAddress());
                try {
                    this.getTurnStack().sendUdpMessage(udpMessage, peerAddr,
                            alloc.getRelayAddress());
                    logger.trace("Sent SendIndiaction to " + peerAddr + " from "
                            + alloc.getRelayAddress());
                } catch (StunException e) {
                    logger.warn("Unable to send message.");
                }
            }
            // else silently ignore the indication.
        }
    }

}
