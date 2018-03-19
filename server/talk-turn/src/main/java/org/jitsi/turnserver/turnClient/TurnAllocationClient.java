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

import org.ice4j.StunException;
import org.ice4j.StunMessageEvent;
import org.ice4j.StunResponseEvent;
import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.ice4j.attribute.Attribute;
import org.ice4j.attribute.AttributeFactory;
import org.ice4j.attribute.ChannelNumberAttribute;
import org.ice4j.attribute.RequestedTransportAttribute;
import org.ice4j.attribute.XorMappedAddressAttribute;
import org.ice4j.attribute.XorPeerAddressAttribute;
import org.ice4j.message.ChannelData;
import org.ice4j.message.Indication;
import org.ice4j.message.MessageFactory;
import org.ice4j.message.Request;
import org.ice4j.socket.IceUdpSocketWrapper;
import org.ice4j.socket.SafeCloseDatagramSocket;
import org.ice4j.stack.ChannelDataEventHandler;
import org.ice4j.stack.PeerUdpMessageEventHandler;
import org.ice4j.stack.TransactionID;
import org.ice4j.stunclient.BlockingRequestSender;
import org.jitsi.turnserver.collectors.AllocationResponseCollector;
import org.jitsi.turnserver.listeners.DataIndicationListener;
import org.jitsi.turnserver.stack.TurnStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to run Allocation Client.
 * 
 * @author Aakash Garg
 *
 */
public class TurnAllocationClient {
    private static BlockingRequestSender requestSender;
    private static IceUdpSocketWrapper sock;
    private static TurnStack turnStack;
    private static TransportAddress localAddress;
    private static TransportAddress serverAddress;
    private static boolean started;
    private static Logger logger = LoggerFactory.getLogger(TurnAllocationClient.class);

    /**
     * The instance that should be notified when an incoming UDP message has been processed and
     * ready for delivery
     */
    private PeerUdpMessageEventHandler peerUdpMessageEventHandler;

    /**
     * The instance that should be notified when an incoming ChannelData message has been processed
     * and ready for delivery
     */
    private ChannelDataEventHandler channelDataEventHandler;

    /**
     * @param args
     * @throws IOException
     * @throws StunException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, StunException, InterruptedException {
        String[] temp = {InetAddress.getLocalHost().getHostAddress(), "3478"};
        // String[] temp = {"176.31.40.85","3478"};
        args = temp;
        Transport protocol = Transport.UDP;

        // uses args as server name and port
        localAddress = new TransportAddress(InetAddress.getLocalHost(), 5678, protocol);
        serverAddress =
                new TransportAddress(args[0], Integer.valueOf(args[1]).intValue(), protocol);
        logger.debug("Client adress : " + localAddress);
        logger.debug("Server adress : " + serverAddress);
        start();
        StunMessageEvent evt = null;
        evt = sendAllocationRequest(localAddress, serverAddress);
        evt = sendCreatePermissionRequest(9999);
        evt = sendCreatePermissionRequest(9999);
        evt = sendCreatePermissionRequest(11000);

        TransportAddress peerAddr =
                new TransportAddress(InetAddress.getLocalHost(), 11000, protocol);

        evt = sendChannelBindRequest((char) 0x4000, peerAddr);
        sendChannelDataMessage();
        logger.debug("Starting interactive communication.");
        doInteractiveComm();
        /*
         * logger.debug("Thread will now sleep."); Thread.sleep(600*1000);
         */
        shutDown();
    }

    public static StunMessageEvent sendAllocationRequest(TransportAddress localAddr,
            TransportAddress serverAddress) throws IOException {
        Request request = MessageFactory.createAllocateRequest();

        RequestedTransportAttribute requestedTransportAttribute =
                AttributeFactory.createRequestedTransportAttribute(RequestedTransportAttribute.UDP);

        request.putAttribute(requestedTransportAttribute);

        StunMessageEvent evt = null;
        try {
            AllocationResponseCollector allocResCollec = new AllocationResponseCollector(turnStack);
            /*
             * turnStack.sendRequest(request, serverAddress, localAddress, allocResCollec);
             */ evt = requestSender.sendRequestAndWaitForResponse(request, serverAddress);
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
                new TransportAddress(serverAddress.getAddress(), peerPort, Transport.UDP);
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

    public static StunMessageEvent sendChannelBindRequest(char channelNo,
            TransportAddress peerAddress) throws IOException, StunException {
        logger.debug("ChannelBind request for : " + peerAddress + " on " + (int) channelNo);
        TransactionID tran = TransactionID.createNewTransactionID();
        Request request =
                MessageFactory.createChannelBindRequest(channelNo, peerAddress, tran.getBytes());
        char cNo = ((ChannelNumberAttribute) (request.getAttribute(Attribute.CHANNEL_NUMBER)))
                .getChannelNumber();
        TransportAddress pAddr =
                ((XorPeerAddressAttribute) (request.getAttribute(Attribute.XOR_PEER_ADDRESS)))
                        .getAddress();

        XorMappedAddressAttribute mappedAddr =
                AttributeFactory.createXorMappedAddressAttribute(localAddress, tran.getBytes());
        // mappedAddr.setAddress(mappedAddr.getAddress(), tran.getBytes());
        logger.debug(">" + mappedAddr.getAddress());
        request.putAttribute(mappedAddr);
        logger.debug("input mappedAddress : " + mappedAddr.getAddress());

        XorMappedAddressAttribute retMapAddr =
                (XorMappedAddressAttribute) (request.getAttribute(Attribute.XOR_MAPPED_ADDRESS));

        TransportAddress mAddr = (retMapAddr).getAddress();
        logger.debug("output mappedAddress : " + mAddr.getHostAddress());

        logger.debug("Retrived ChannelBind request is : " + pAddr + " on " + (int) cNo);

        StunMessageEvent evt = null;
        logger.debug("ChannelBind tran : " + tran);
        try {
            evt = requestSender.sendRequestAndWaitForResponse(request, serverAddress, tran);
        } catch (StunException ex) {
            // this shouldn't happen since we are the ones that created the
            // request
            logger.debug("Internal Error. Failed to encode a message");
            return null;
        }

        if (evt != null)
            logger.debug("ChannelBind TEST res=" + evt.getRemoteAddress().toString() + " - "
                    + evt.getRemoteAddress().getHostAddress());
        else
            logger.debug("NO RESPONSE received to ChannelBind TEST.");

        return evt;
    }

    public static void sendChannelDataMessage() throws StunException, IOException {
        byte[] message = {0xa, 0xb};
        ChannelData channelData = new ChannelData();
        channelData.setChannelNumber((char) 0x4000);
        channelData.setData(message);
        turnStack.sendChannelData(channelData, serverAddress, localAddress);
        logger.debug("ChannelData message sent.");
    }

    /**
     * Puts the discoverer into an operational state.
     * 
     * @throws IOException if we fail to bind.
     * @throws StunException if the stun4j stack fails start for some reason.
     */
    public static void start() throws IOException, StunException {
        ClientChannelDataEventHandler channelDataHandler = new ClientChannelDataEventHandler();
        turnStack = new TurnStack(null, channelDataHandler);
        channelDataHandler.setTurnStack(turnStack);
        sock = new IceUdpSocketWrapper(new SafeCloseDatagramSocket(localAddress));
        turnStack.addSocket(sock);

        DataIndicationListener dataIndListener = new DataIndicationListener(turnStack);
        dataIndListener.setLocalAddress(localAddress);
        dataIndListener.start();

        requestSender = new BlockingRequestSender(turnStack, localAddress);

        started = true;
    }


    public static void doInteractiveComm() throws IOException, StunException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        logger.debug("Started interaction start typing message");
        String line = br.readLine();
        logger.debug("My first message : " + line);
        while (line != null) {
            byte[] data = line.getBytes();
            TransactionID tran = TransactionID.createNewTransactionID();
            TransportAddress peerAddress =
                    new TransportAddress(InetAddress.getLocalHost(), 11000, Transport.UDP);
            Indication ind =
                    MessageFactory.createSendIndication(peerAddress, data, tran.getBytes());
            logger.debug("Trying to send message to server");
            turnStack.sendIndication(ind, serverAddress, localAddress);
            logger.debug("message sent");

            logger.debug("Type a new message : ");
            line = br.readLine();
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
