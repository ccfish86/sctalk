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
package org.jitsi.turnserver.turnClient;

import java.net.InetAddress;

import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.stack.StunStack;
import org.ice4j.stunclient.NetworkConfigurationDiscoveryProcess;
import org.ice4j.stunclient.StunDiscoveryReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The class would be acting as a StunClient. The client runs on UDP initiates displays network
 * configuration <br>
 * Usage from command prompt <br>
 * 
 * java org.ice4j.stunclient.StunCient stunserver.org stunPort <br>
 * (Displays the network configuration and port mapping using stunserver.org as stunserver and
 * stunPort as server's stun port<br>
 * * java org.ice4j.stunclient.StunCient N <br>
 * (chooses server from array of { "stunserver.org", "stun.softjoys.com", "stun.voiparound.com",
 * "stun.voipbuster.com", "stun.voipstunt.com", "stun.voxgratia.org", "stun.ekiga.net",
 * "stun.ideasip.com", "stun.iptel.org", "stun.rixtelecom.se" })
 * 
 * @author Aakash
 * 
 */

public class StunClient {
    
    private static Logger logger = LoggerFactory.getLogger(StunClient.class);

    /**
     * @param args
     */
    public static StunDiscoveryReport getReport(String... args) throws Exception {
        TransportAddress localAddr = null;
        TransportAddress serverAddr = null;
        Transport protocol = Transport.UDP;
        int serverUdpPort = 3478;
        String[] server = {"stunserver.org", "stun.softjoys.com", "stun.voiparound.com",
                "stun.voipbuster.com", "stun.voipstunt.com", "stun.voxgratia.org", "stun.ekiga.net",
                "stun.ideasip.com", "stun.iptel.org", "stun.rixtelecom.se"};

        if (args.length == 4) {
            // uses args[0] and args[1] as server name and port and args[2] and
            // args[3] as client ip and port
            localAddr =
                    new TransportAddress(args[2], Integer.valueOf(args[3]).intValue(), protocol);
            serverUdpPort = Integer.valueOf(args[1]).intValue();
            serverAddr = new TransportAddress(args[0], serverUdpPort, protocol);
        } else if (args.length == 2) {
            // uses args as server name and port
            localAddr = new TransportAddress(InetAddress.getLocalHost(), 5678, protocol);
            logger.debug("Sending request on " + args[0] + ":" + args[1]);
            serverAddr =
                    new TransportAddress(args[0], Integer.valueOf(args[1]).intValue(), protocol);
        } else if (args.length == 1) {
            // chooses a server indexed by N
            localAddr = new TransportAddress(InetAddress.getLocalHost(), 5678, protocol);
            serverAddr = new TransportAddress(server[Integer.parseInt(args[0])], serverUdpPort,
                    protocol);
            // serverAddr = new
            // TransportAddress(InetAddress.getLocalHost(),serverUdpPort,protocol);
            logger.debug(
                    "Stun Server - " + server[Integer.parseInt(args[0])] + ":" + serverUdpPort);
        } else {
            // runs server running on same computer
            localAddr = new TransportAddress(InetAddress.getLocalHost(), 5678, protocol);
            // serverAddr = new TransportAddress(
            // server[0],serverUdpPort,protocol);
            serverAddr = new TransportAddress(InetAddress.getLocalHost(), serverUdpPort, protocol);
        }

        NetworkConfigurationDiscoveryProcess addressDiscovery =
                new NetworkConfigurationDiscoveryProcess(new StunStack(), localAddr, serverAddr);

        addressDiscovery.start();
        StunDiscoveryReport report = addressDiscovery.determineAddress();
        addressDiscovery.shutDown();
        return report;
    }

    public static void main(String... args) throws Exception {
        StunDiscoveryReport report = StunClient.getReport(args);
        logger.debug("StunDiscoveryReport {}", report);
    }

}
