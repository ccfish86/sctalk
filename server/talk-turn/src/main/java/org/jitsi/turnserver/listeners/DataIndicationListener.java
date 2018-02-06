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

import java.io.UnsupportedEncodingException;

import org.ice4j.TransportAddress;
import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.DataAttribute;
import org.ice4j.attribute.XorPeerAddressAttribute;
import org.ice4j.message.Indication;
import org.ice4j.message.Message;
import org.jitsi.turnserver.IndicationListener;
import org.jitsi.turnserver.stack.Allocation;
import org.jitsi.turnserver.stack.TurnStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle the incoming Data indications.
 * 
 * @author Aakash Garg
 * 
 */
public class DataIndicationListener extends IndicationListener {
    /**
     * The <tt>Logger</tt> used by the <tt>DataIndicationListener</tt> class and its instances for
     * logging output.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(DataIndicationListener.class.getName());

    /**
     * parametrised constructor.
     * 
     * @param turnStack the turnStack to set for this class.
     */
    public DataIndicationListener(TurnStack turnStack) {
        super(turnStack);
    }

    /**
     * Handles the incoming data indication.
     * 
     * @param ind the indication to handle.
     * @param alloc the allocation associated with message.
     */
    @Override
    public void handleIndication(Indication ind, Allocation alloc) {
        if (ind.getMessageType() == Message.DATA_INDICATION) {
            logger.trace("Received a Data Indication message.");
            byte[] tran = ind.getTransactionID();

            XorPeerAddressAttribute xorPeerAddress =
                    (XorPeerAddressAttribute) ind.getAttribute(Attribute.XOR_PEER_ADDRESS);
            xorPeerAddress.setAddress(xorPeerAddress.getAddress(), tran);
            DataAttribute data = (DataAttribute) ind.getAttribute(Attribute.DATA);

            TransportAddress peerAddr = xorPeerAddress.getAddress();
            try {
                String line = new String(data.getData(), "UTF-8");
                // System.out.println(line);
                logger.trace("Data Indiaction message from  " + peerAddr + " is " + line);
                /*
                 * System.out.println("Received a Data indiction from " + peerAddr + ", message : "
                 * + line);
                 */ } catch (UnsupportedEncodingException e) {
                logger.warn("Unable to convert to String!");
            }
        }
    }

}
