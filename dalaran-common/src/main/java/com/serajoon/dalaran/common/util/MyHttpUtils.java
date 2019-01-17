package com.serajoon.dalaran.common.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public abstract class MyHttpUtils {

    public static boolean isHostConnectable(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port),100);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
