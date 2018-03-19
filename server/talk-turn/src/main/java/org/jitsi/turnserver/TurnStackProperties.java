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

import org.ice4j.StackProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class contains a number of property names and their default values that we use to configure
 * the behavior of the Turnserver stack.
 *
 * @author Aakash Garg.
 */
public class TurnStackProperties extends StackProperties {
    /**
     * Our class logger.
     */
    private static final Logger logger =
            LoggerFactory.getLogger(TurnStackProperties.class.getName());

    public static final String TURNSERVER_UDP_PORT = "org.jitsi.turnserver.udp_port";

    public static final int DEFAULT_TURNSERVER_UDP_PORT = 3478;

    public static final String TURNSERVER_MIN_PORT = "org.jitsi.turnserver.min_port";

    public static final int DEFAULT_TURNSERVER_MIN_PORT = 49152;

    public static final String TURNSERVER_MAX_PORT = "org.jitsi.turnserver.max_port";

    public static final int DEFAULT_TURNSERVER_MAX_PORT = 65535;

    public static final String ALLOCATION_LIFETIME = "org.jitsi.turnserver.allocation_lifetime";

    public static final int DEFAULT_ALLOCATION_LIFETIME = 10 * 60 * 1000;

    public static final String MAX_ALLOCATIONS = "org.jitsi.turnserver.max_allocations";

    public static final int DEFAULT_MAX_ALLOCATIONS = 50;

    public static final String CHANNELBIND_LIFETIME = "org.jitsi.turnserver.channelbind_lifetime";

    public static final int DEFAULT_CHANNELBIND_LIFETIME = 10 * 60 * 1000;

    public static final String MAX_PERMISSIONS_PER_ALLOCATION =
            "org.jitsi.turnserver.max_permissions_per_allocation";

    public static final int DEFAULT_MAX_PERMISSIONS_PER_ALLOCATION = 10;

    public static final String PERMISSION_LIFETIME = "org.jitsi.turnserver.permission_lifetime";

    public static final int DEFAULT_PERMISSION_LIFETIME = 300 * 1000;

    public static final String MAX_CHANNELBINDS_PER_ALLOCATION =
            "org.jitsi.turnserver.max_channelbinds_per_allocation";

    public static final int DEFAULT_MAX_CHANNELBINDS_PER_ALLOCATION = 10;

    public static final String REALM = "org.jitsi.turnserver.realm";

    public static final String DEFAULT_REALM = "org.jitsi.turnserver";

    public static final String DEFAULT_ACCOUNTS_FILE = "";

    public static final String ACCOUNTS_FILE = "";


}
