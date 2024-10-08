package server.network;

import common.responses.AbstractResponse;
import common.network.NetworkProvider;
import common.network.Serializer;
import server.managers.Logger;
import server.managers.RequestProcessor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.concurrent.*;

public class TCPServer implements NetworkProvider {
    private static final int BUFFER_SIZE = 4096;
    private final ByteBuffer buffer;
    private final int poolSize = 5;
    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(poolSize);
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    private static final String HOST = "localhost";
    private static final int PORT = 4728;

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private final RequestProcessor requestProcessor;
    private final Logger logger;

    public TCPServer(RequestProcessor requestProcessor, Logger logger) {
        this.requestProcessor = requestProcessor;
        this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.logger = logger;
    }

    @Override
    public void openConnection() throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);
        this.serverSocketChannel.bind(inetSocketAddress);
        this.selector = initSelector();
    }

    @Override
    public void run() {
        try {
            while (true) {
                selector.selectNow();
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = takeKey(selectedKeys);
                    handleKey(key);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SelectionKey takeKey(Iterator<SelectionKey> selectionKeyIterator) {
        SelectionKey key = selectionKeyIterator.next();
        selectionKeyIterator.remove();
        return key;
    }

    private Selector initSelector() throws IOException {
        Selector socketSelector = SelectorProvider.provider().openSelector();
        this.serverSocketChannel.register(socketSelector, SelectionKey.OP_ACCEPT);
        return socketSelector;
    }

    private void handleKey(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                accept(key);
            } else if (key.isReadable()) {
                read(key);
                key.cancel();
            } else if (key.isWritable()) {
                write(key);
                key.cancel();
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = ssc.accept();
        socketChannel.configureBlocking(false);
        System.out.println("Connected: " + socketChannel.getRemoteAddress());
        logger.log("Connected: " + socketChannel.getRemoteAddress());
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        this.buffer.clear();
        int bytesRead;
        try {
            bytesRead = socketChannel.read(this.buffer);
        } catch (IOException e) {
            key.cancel();
            socketChannel.close();
            return;
        }

        if (bytesRead == -1) {
            key.cancel();
            return;
        }
        this.buffer.flip();

        Runnable task = () -> {
            AbstractResponse abstractResponse = requestProcessor.handleRequest(buffer);
            System.out.println(abstractResponse);
            logger.log("Sent: " + abstractResponse.toString());

            try {
                socketChannel.register(selector, SelectionKey.OP_WRITE, abstractResponse);
            } catch (ClosedChannelException e) {
                throw new RuntimeException(e);
            }
        };

        this.fixedThreadPool.submit(task);
    }

    public void close() throws IOException {
        if (serverSocketChannel != null) {
            serverSocketChannel.close();
        }
    }

    private void write(SelectionKey key) {
        RecursiveAction writeAction = new RecursiveAction() {
            @Override
            protected void compute() {
                try {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    AbstractResponse abstractResponse = (AbstractResponse) key.attachment();

                    ByteBuffer writeBuffer = Serializer.serializeObject(abstractResponse);
                    writeBuffer.flip();
                    while (writeBuffer.hasRemaining()) {
                        socketChannel.write(writeBuffer);
                    }

                    socketChannel.register(selector, SelectionKey.OP_READ);
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        };
        forkJoinPool.execute(writeAction);
    }

}
