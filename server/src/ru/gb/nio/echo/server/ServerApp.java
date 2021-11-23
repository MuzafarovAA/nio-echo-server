package ru.gb.nio.echo.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerApp {

    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9000;
    public static final int BUFFER = 256;

    public static void main(String[] args) throws IOException {
        new ServerApp().start();
    }

    private void start() throws IOException {
        Selector selector = Selector.open();
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        SocketAddress socketAddress = new InetSocketAddress(HOSTNAME, PORT);
        serverSocketChannel.bind(socketAddress);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server has been started.");

        while (true) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    SocketChannel client = serverSocketChannel.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("Client has been connected.");
                }
                if (selectionKey.isReadable()) {
                    SocketChannel client = (SocketChannel) selectionKey.channel();
                    client.read(byteBuffer);
                    String message = new String(byteBuffer.array());
                    System.out.println("Received from client: " + message);
                    byteBuffer.flip();
                    client.write(byteBuffer);
                    byteBuffer.clear();
                }
                iterator.remove();
            }

        }
    }

}
