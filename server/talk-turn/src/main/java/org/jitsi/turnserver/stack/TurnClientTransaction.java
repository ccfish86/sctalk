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

import org.ice4j.ResponseCollector;
import org.ice4j.StunMessageEvent;
import org.ice4j.TransportAddress;
import org.ice4j.message.Request;
import org.ice4j.stack.StunClientTransaction;
import org.ice4j.stack.StunStack;
import org.ice4j.stack.TransactionID;

/**
 * {@inheritDoc}
 */
public class TurnClientTransaction extends StunClientTransaction {

    /**
     * {@inheritDoc}
     */
    public TurnClientTransaction(StunStack stackCallback, Request request,
            TransportAddress requestDestination, TransportAddress localAddress,
            ResponseCollector responseCollector, TransactionID transactionID) {
        super(stackCallback, request, requestDestination, localAddress, responseCollector,
                transactionID);
    }

    /**
     * /** {@inheritDoc}
     */
    public TurnClientTransaction(StunStack stackCallback, Request request,
            TransportAddress requestDestination, TransportAddress localAddress,
            ResponseCollector responseCollector) {
        super(stackCallback, request, requestDestination, localAddress, responseCollector);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void handleResponse(StunMessageEvent evt) {
        super.handleResponse(evt);
    }

}
