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
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.ice4j.Transport;
import org.ice4j.TransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An interactive UDP peer client to send messages.
 * 
 * @author Aakash Garg
 *
 */
public class InteractiveUdpPeer {

    private static DatagramSocket sock;

    private static int serverPort = 15000;
    // private static int serverPort = 49152;

    private static int clientPort = 11000;
    
    private static Logger logger = LoggerFactory.getLogger(InteractiveUdpPeer.class);

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String[] temp = {InetAddress.getLocalHost().toString(), "" + clientPort};
        if (args.length == 0) {
            args = temp;
        }
        TransportAddress serverAddr = null;
        TransportAddress clientAddr = null;
        if (args.length == 2) {
            serverAddr = new TransportAddress(args[0], Integer.parseInt(args[1]), Transport.UDP);
            clientAddr = new TransportAddress(InetAddress.getLocalHost(), 12000, Transport.UDP);
        } else if (args.length == 4) {
            serverAddr = new TransportAddress(args[0], Integer.parseInt(args[1]), Transport.UDP);
            serverPort = Integer.parseInt(args[1]);
            clientAddr = new TransportAddress(args[2], Integer.parseInt(args[3]), Transport.UDP);
            clientPort = Integer.parseInt(args[3]);
        } else {
            throw new IllegalArgumentException("Please enter valid arguments.");
        }

        sock = new DatagramSocket(clientPort, InetAddress.getLocalHost());
        /*
         * Thread recThread = getRecThread(); recThread.start();
         */
        /*
         * FileReader fr = new FileReader( "D:\\Eclipse\\turnserver\\samoleInputFile.txt");
         */ // BufferedReader br = new BufferedReader(fr);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        logger.debug("Start typing message.");
        while ((line = br.readLine()) != null) {
            byte[] data = line.getBytes();
            DatagramPacket pkt =
                    new DatagramPacket(data, data.length, InetAddress.getLocalHost(), serverPort);
            sock.send(pkt);
            logger.debug("Sent : " + line);

            byte[] receiveData = new byte[1024];
            DatagramPacket recPkt = new DatagramPacket(receiveData, receiveData.length);
            sock.receive(recPkt);
            logger.debug("Recv : " + getString(recPkt));
        }
    }

    public static Thread getRecThread() {
        Runnable recRun = new Runnable() {
            @Override
            public void run() {
                byte[] receiveData = new byte[1024];
                DatagramPacket recPkt = new DatagramPacket(receiveData, receiveData.length);
                logger.debug("Waiting for receiving data on "
                        + sock.getLocalAddress().toString() + ":" + sock.getLocalPort());
                try {
                    sock.receive(recPkt);
                    logger.debug("Received Data : " + getString(recPkt));
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        };
        Thread thread = new Thread(recRun);
        return thread;
    }

    public static String getString(DatagramPacket recPkt) {
        byte[] data = recPkt.getData();
        int len = recPkt.getLength();
        byte[] recData = new byte[len];
        for (int i = 0; i < len; i++) {
            recData[i] = data[i];
        }
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }

    }
}
