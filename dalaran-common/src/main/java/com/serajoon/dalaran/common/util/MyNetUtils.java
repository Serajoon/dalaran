package com.serajoon.dalaran.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

@Slf4j
public abstract class MyNetUtils {

    /**
     * 判断一个指定ip和端口的服务是否可用
     *
     * @param host ip
     * @param port prot
     * @return
     */
    public static boolean isIpAndPortReachable(String host, int port) {
        boolean result = false;
        if (!isPing(host)) {
            return false;
        }
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port), 100);
            result = socket.isConnected();
        } catch (IOException e) {
            log.error("{}:{} is not connectable", host, port);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ping一个ip地址
     *
     * @param ip ip地址
     * @return true:ping通 false:ping失败
     */
    public static boolean isPing(String ip) {
        boolean status = false;
        if (ip != null) {
            try {
                status = InetAddress.getByName(ip).isReachable(200);
            } catch (IOException ignored) {
            }
        }
        return status;
    }
}
