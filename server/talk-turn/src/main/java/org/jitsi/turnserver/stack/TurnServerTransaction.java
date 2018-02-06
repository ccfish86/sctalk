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

import java.io.IOException;

import org.ice4j.StunException;
import org.ice4j.TransportAddress;
import org.ice4j.message.Response;
import org.ice4j.stack.StunServerTransaction;
import org.ice4j.stack.StunStack;
import org.ice4j.stack.TransactionID;

/**
 * The class represents the TURN server Transaction in turnserver. It is just an inheritance of
 * StunServerTransaction.
 * 
 * @author Aakash Garg
 *
 */
public class TurnServerTransaction extends StunServerTransaction {

    /**
     * {@inheritDoc}
     */
    public TurnServerTransaction(StunStack stackCallback, TransactionID tranID,
            TransportAddress localListeningAddress, TransportAddress requestSource) {
        super(stackCallback, tranID, localListeningAddress, requestSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void retransmitResponse()
            throws StunException, IOException, IllegalArgumentException {
        super.retransmitResponse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Response getResponse() {
        return super.getResponse();
    }

}
