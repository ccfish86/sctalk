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

package org.jitsi.turnserver;

import org.ice4j.StunException;

/**
 * @author Aakash Garg
 * 
 */
public class TurnException extends StunException {

    /**
     * 
     */
    private static final long serialVersionUID = -8004612606830162094L;

    /**
     * 
     */
    public TurnException() {}

    /**
     * @param id
     */
    public TurnException(int id) {
        super(id);
    }

    /**
     * @param message
     */
    public TurnException(String message) {
        super(message);
    }

    /**
     * @param id
     * @param message
     */
    public TurnException(int id, String message) {
        super(id, message);
    }

    /**
     * @param id
     * @param message
     * @param cause
     */
    public TurnException(int id, String message, Throwable cause) {
        super(id, message, cause);
    }

    /**
     * @param message
     * @param cause
     */
    public TurnException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public TurnException(Throwable cause) {
        super(cause);
    }

}
