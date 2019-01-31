package com.serajoon.dalaran.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

@Slf4j
public abstract class MyNetUtils {

    /**
     * 判断一个指定ip和端口的服务是否可用
     * @param host ip
     * @param port prot
     * @return
     */
    public static boolean isIpAndPortReachable(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port),100);
        } catch (IOException e) {
            log.error("{}:{} is not connectable",host,port);
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
