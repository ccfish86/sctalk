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
 * The class that would be handling and responding to incoming Connect response.
 * 
 * @author Aakash Garg
 */
public class ConnectResponseCollector implements ResponseCollector {

    /**
     * The <tt>Logger</tt> used by the <tt>ConnectresponseCollector</tt> class and its instances for
     * logging output.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(ConnectResponseCollector.class.getName());

    private final StunStack stunStack;

    /**
     * Creates a new ConnectresponseCollector
     * 
     * @param turnStack
     */
    public ConnectResponseCollector(StunStack stunStack) {
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
        if (message.getMessageType() == Message.CONNECT_ERROR_RESPONSE) {
            ErrorCodeAttribute errorCodeAttribute =
                    (ErrorCodeAttribute) message.getAttribute(Attribute.ERROR_CODE);
            switch (errorCodeAttribute.getErrorCode()) {
                case ErrorCodeAttribute.ALLOCATION_MISMATCH:
                    // code for bad response error
                    break;
                case ErrorCodeAttribute.CONNECTION_ALREADY_EXISTS:
                    // code for processing connection already exists error
                    break;
                case ErrorCodeAttribute.FORBIDDEN:
                    // code for processing forbidden error code
                    break;
                case ErrorCodeAttribute.CONNECTION_TIMEOUT_OR_FAILURE:
                    // code for processing connection timeout or failure error code.
                    break;
            }
        } else if (message.getMessageType() == Message.CONNECT_RESPONSE) {
            // code for doing processing of Connect success response
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
