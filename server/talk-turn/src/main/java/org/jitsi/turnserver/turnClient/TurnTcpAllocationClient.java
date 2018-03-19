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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import org.ice4j.StunException;
import org.ice4j.StunMessageEvent;
import org.ice4j.StunResponseEvent;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.attribute.AttributeFactory;
import org.ice4j.attribute.RequestedTransportAttribute;
import org.ice4j.message.Indication;
import org.ice4j.message.MessageFactory;
import org.ice4j.message.Request;
import org.ice4j.socket.IceTcpSocketWrapper;
import org.ice4j.stack.PeerUdpMessageEventHandler;
import org.ice4j.stack.TransactionID;
import org.ice4j.stunclient.BlockingRequestSender;
import org.jitsi.turnserver.collectors.AllocationResponseCollector;
import org.jitsi.turnserver.listeners.ConnectionAttemptIndicationListener;
import org.jitsi.turnserver.stack.TurnStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to run Allocation Client over TCP.
 * 
 * @author Aakash Garg
 * 
 */
public class TurnTcpAllocationClient {
    private static BlockingRequestSender requestSender;

    private static IceTcpSocketWrapper sock;

    private static TurnStack turnStack;

    private static TransportAddress localAddress;

    private static TransportAddress serverAddress;

    private static boolean started;

    private static Socket tcpSocketToServer = null;
    
    private static Logger logger = LoggerFactory.getLogger(TurnTcpAllocationClient.class);

    /**
     * The instance that should be notified when an incoming TCP message has been processed and
     * ready for delivery
     */
    private PeerUdpMessageEventHandler peerUdpMessageEventHandler;

    /**
     * Puts the discoverer into an operational state.
     * 
     * @throws IOException if we fail to bind.
     * @throws StunException if the stun4j stack fails start for some reason.
     */
    public static void start(Transport protocol) throws IOException, StunException {
        sock = new IceTcpSocketWrapper(tcpSocketToServer);
        logger.debug("Adding an new TCP connection to : " + serverAddress.getHostAddress());

        localAddress = new TransportAddress(InetAddress.getLocalHost(),
                tcpSocketToServer.getLocalPort(), protocol);
        logger.debug("Client adress : " + localAddress);
        logger.debug("Server adress : " + serverAddress);

        ClientChannelDataEventHandler channelDataHandler = new ClientChannelDataEventHandler();
        turnStack = new TurnStack(null, channelDataHandler);
        channelDataHandler.setTurnStack(turnStack);

        turnStack.addSocket(sock);

        requestSender = new BlockingRequestSender(turnStack, localAddress);

        ConnectionAttemptIndicationListener connectionAttemptIndicationListener =
                new ConnectionAttemptIndicationListener(turnStack/* ,requestSender */);
        connectionAttemptIndicationListener.setLocalAddress(localAddress);
        connectionAttemptIndicationListener.start();

        started = true;
    }

    /**
     * @param args
     * @throws IOException
     * @throws StunException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, StunException, InterruptedException {
        String[] temp = {InetAddress.getLocalHost().toString(), "3478"};
        args = temp;
        Transport protocol = Transport.TCP;

        // uses args as server name and port
        serverAddress = new TransportAddress(InetAddress.getLocalHost(),
                Integer.valueOf(args[1]).intValue(), protocol);

        tcpSocketToServer = new Socket(serverAddress.getHostAddress(), 3478);
        logger.debug("Local port chosen : " + tcpSocketToServer.getLocalPort());

        start(protocol);
        StunMessageEvent evt = null;
        evt = sendAllocationRequest(localAddress, serverAddress);
        evt = sendCreatePermissionRequest(9999);
        // evt = sendCreatePermissionRequest(9999);
        // evt = sendCreatePermissionRequest(11000);
        // evt = sendConnectRequest(9999);

        TransportAddress peerAddr =
                new TransportAddress(InetAddress.getLocalHost(), 11000, protocol);

        Thread.sleep(600 * 1000);

        shutDown();
    }

    public static StunMessageEvent sendAllocationRequest(TransportAddress localAddr,
            TransportAddress serverAddress) throws IOException {
        Request request = MessageFactory.createAllocateRequest();

        RequestedTransportAttribute requestedTransportAttribute =
                AttributeFactory.createRequestedTransportAttribute(RequestedTransportAttribute.TCP);

        request.putAttribute(requestedTransportAttribute);
        logger.debug("Message type : " + (int) request.getMessageType());
        StunMessageEvent evt = null;
        try {
            AllocationResponseCollector allocResCollec = new AllocationResponseCollector(turnStack);
            /*
             * turnStack.sendRequest(request, serverAddress, localAddress, allocResCollec);
             */
            evt = requestSender.sendRequestAndWaitForResponse(request, serverAddress);
            allocResCollec.processResponse((StunResponseEvent) evt);
        } catch (Exception ex) {
            // this shouldn't happen since we are the ones that created the
            // request
            ex.printStackTrace();
            logger.debug("Internal Error. Failed to encode a message");
            return null;
        }

        if (evt != null)
            logger.debug("Allocation TEST res=" + (int) (evt.getMessage().getMessageType())
                    + " - " + evt.getRemoteAddress().getHostAddress());
        else
            logger.debug("NO RESPONSE received to Allocation TEST.");
        return evt;
    }

    public static StunMessageEvent sendCreatePermissionRequest(int peerPort)
            throws IOException, StunException {
        
        TransportAddress peerAddr =
                new TransportAddress(serverAddress.getAddress(), peerPort, Transport.TCP);
        TransactionID tran = TransactionID.createNewTransactionID();
        logger.debug("Create request for : " + peerAddr);
        Request request = MessageFactory.createCreatePermissionRequest(peerAddr, tran.getBytes());
        StunMessageEvent evt = null;
        logger.debug("Permission tran : " + tran);
        try {
            evt = requestSender.sendRequestAndWaitForResponse(request, serverAddress, tran);
        } catch (StunException ex) {
            // this shouldn't happen since we are the ones that created the
            // request
            logger.debug("Internal Error. Failed to encode a message");
            return null;
        }

        if (evt != null)
            logger.debug("Permission TEST res=" + (int) (evt.getMessage().getMessageType())
                    + " - " + evt.getRemoteAddress().getHostAddress());
        else
            logger.debug("NO RESPONSE received to Permission TEST.");

        return evt;
    }

    public static StunMessageEvent sendConnectRequest(int peerPort)
            throws IOException, StunException {

        TransportAddress peerAddr =
                new TransportAddress(serverAddress.getAddress(), peerPort, Transport.TCP);
        TransactionID tran = TransactionID.createNewTransactionID();
        logger.debug("Connect request for : " + peerAddr);
        Request request = MessageFactory.createConnectRequest(peerAddr, tran.getBytes());
        request.setTransactionID(tran.getBytes());
        StunMessageEvent evt = null;
        logger.debug("Connect Req tran : " + tran);
        try {
            evt = requestSender.sendRequestAndWaitForResponse(request, serverAddress, tran);
        } catch (StunException ex) {
            // this shouldn't happen since we are the ones that created the
            // request
            logger.debug("Internal Error. Failed to encode a message");
            return null;
        }

        if (evt != null)
            System.out
                    .println("Connect request TEST res=" + (int) (evt.getMessage().getMessageType())
                            + " - " + evt.getRemoteAddress().getHostAddress());
        else
            logger.debug("NO RESPONSE received to Connect Request TEST.");

        return evt;
    }

    public static void doInteractiveComm() throws IOException, StunException {
        logger.debug("---->Interactve Communication started<---------");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        while ((line = br.readLine()) != null) {
            byte[] data = line.getBytes();
            TransactionID tran = TransactionID.createNewTransactionID();
            TransportAddress peerAddress =
                    new TransportAddress(InetAddress.getLocalHost(), 11000, Transport.TCP);
            Indication ind =
                    MessageFactory.createSendIndication(peerAddress, data, tran.getBytes());
            turnStack.sendIndication(ind, serverAddress, localAddress);
        }
    }

    /**
     * Shuts down the underlying stack and prepares the object for garbage collection.
     */
    public static void shutDown() {
        turnStack.removeSocket(localAddress);
        sock.close();
        sock = null;

        localAddress = null;
        requestSender = null;

        started = false;
    }

}
