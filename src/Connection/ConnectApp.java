package Connection;

import Connection.Packages.ConnectionHand;
import Connection.Packages.PhotoPackage.PhotoPackage;
import Connection.Packages.TypeInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 用于PC端到App端的Socket连接，以及
 * 实现逻辑：
 * 1、初始化对象，指定端口号，调用waitConnect()方法等待连接；
 * 2、App端点击建立连接；
 * 3、App端选择要发送的文件类型，发送握手包ConnectHand到PC端；
 * 4、App端发送所有要发送的文件Package给PC端。
 * 注：一切外部类不负责异常处理和多线程操作。捕获异常和处理多线程并发需要由调用方（主程序）实现。
 */
public class ConnectApp {
    private final ServerSocket serverSocket;
    private Socket socket;
    private InputStream inputStream;
    private ObjectInputStream objInputStream;

    /**
     *
     * @param serverPort 指定PC端的端口号。
     * @throws IOException 指定非法的端口号（合法范围为0~65535）时，抛出此异常。
     */
    public ConnectApp(int serverPort) throws IOException {
        // 初始化用于等待App连接的socket
        serverSocket = new ServerSocket(serverPort);
    }


    /**
     * 等待App端的连接请求。
     */
    public void waitConnect() throws IOException, ClassNotFoundException {
        // 等待App端连接
        socket = serverSocket.accept();
        // 连接成功后，获取数据输入流
        inputStream = socket.getInputStream();
        // 读取App端发送的握手包
        readConnectionHand();
        // 握手成功后，读取真正的文件数据
        readPackages();
    }

    private void readConnectionHand() throws IOException, ClassNotFoundException {
        // 读取App端发送的ConectionHand握手封装
        objInputStream = new ObjectInputStream(inputStream);
        Object obj = objInputStream.readObject();

        if (obj instanceof ConnectionHand) {
            for (TypeInfo info: ((ConnectionHand) obj).fileTypes()) {

            }
        }
    }

    private void readPackages() throws IOException, ClassNotFoundException {
        while (true) {
            // 读取App发送的文件封装
            Object obj = objInputStream.readObject();

            // 判断文件类型，执行对应操作
            if (obj instanceof PhotoPackage) {

            }   // 此处预留，以后添加更多文件类型
        }
    }
}
