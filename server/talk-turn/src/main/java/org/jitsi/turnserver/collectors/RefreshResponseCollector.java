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
import org.ice4j.message.Message;
import org.ice4j.stack.StunStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class that would be handling and responding to incoming Refresh responses.
 * 
 * @author Aakash Garg
 */
public class RefreshResponseCollector implements ResponseCollector {
    /**
     * The <tt>Logger</tt> used by the <tt>RefreshresponseCollector</tt> class and its instances for
     * logging output.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(RefreshResponseCollector.class.getName());

    private final StunStack stunStack;

    /**
     * Creates a new RefreshResponseCollector
     * 
     * @param turnStack
     */
    public RefreshResponseCollector(StunStack stunStack) {
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
        if (message.getMessageType() == Message.REFRESH_ERROR_RESPONSE) {
            // delete allocation
        } else if (message.getMessageType() == Message.REFRESH_RESPONSE) {
            // update allocation
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

    }

}
