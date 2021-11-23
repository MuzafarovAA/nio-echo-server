package ru.gb.nio.echo.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ClientApp {

    public static final String HOSTNAME = "localhost";
    public static final int PORT = 9000;

    public static void main(String[] args) throws IOException {
        new ClientApp().start();
    }

    private void start() throws IOException {
        ByteBuffer byteBuffer;
        SocketAddress socketAddress = new InetSocketAddress(HOSTNAME, PORT);
        SocketChannel client = SocketChannel.open(socketAddress);
        System.out.println("Connected to server.");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.next();
            byteBuffer = ByteBuffer.wrap(message.getBytes());
            client.write(byteBuffer);
            byteBuffer.flip();
            client.read(byteBuffer);
            message = new String(byteBuffer.array());
            System.out.println("Received from server: " + message);
            byteBuffer.clear();
        }
    }
}
