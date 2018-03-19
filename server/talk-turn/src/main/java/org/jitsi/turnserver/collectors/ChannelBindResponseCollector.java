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

package org.jitsi.turnserver.collectors;

import org.ice4j.ResponseCollector;
import org.ice4j.StunResponseEvent;
import org.ice4j.StunTimeoutEvent;
import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.ErrorCodeAttribute;
import org.ice4j.message.Message;
import org.ice4j.stack.StunStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class that would be handling and responding to incoming ChannelBind response.
 * 
 * @author Aakash Garg
 */
public class ChannelBindResponseCollector implements ResponseCollector {

    /**
     * The <tt>Logger</tt> used by the <tt>ChannelBindresponseCollector</tt> class and its instances
     * for logging output.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(ChannelBindResponseCollector.class.getName());

    private final StunStack stunStack;

    /**
     * Creates a new ChannelBindresponseCollector
     * 
     * @param turnStack
     */
    public ChannelBindResponseCollector(StunStack stunStack) {
        this.stunStack = stunStack;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ice4j.ResponseCollector#processResponse(org.ice4j.StunResponseEvent)
     */
    @Override
    public void processResponse(StunResponseEvent evt) {

        logger.trace("Received response {}", evt);

        Message message = evt.getMessage();
        if (message.getMessageType() == Message.CHANNELBIND_ERROR_RESPONSE) {
            ErrorCodeAttribute errorCodeAttribute =
                    (ErrorCodeAttribute) message.getAttribute(Attribute.ERROR_CODE);
            switch (errorCodeAttribute.getErrorCode()) {
                case ErrorCodeAttribute.BAD_REQUEST:
                    // code for bad response error
                    break;
                case ErrorCodeAttribute.INSUFFICIENT_CAPACITY:
                    // logic for processing insufficient capacity error code
                    break;
            }
        } else if (message.getMessageType() == Message.CHANNELBIND_RESPONSE) {
            // logic for processing the success response.
        } else {
            return;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ice4j.ResponseCollector#processTimeout(org.ice4j.StunTimeoutEvent)
     */
    @Override
    public void processTimeout(StunTimeoutEvent event) {
        // TODO Auto-generated method stub

    }

}
