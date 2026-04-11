package Connection;

import Connection.Packages.Command.SendPackageEnded;
import Connection.Packages.ConnectionHand;
import Connection.Packages.PhotoPackage.PhotoPackage;
import Connection.Packages.TypeInfo;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 用于PC端到App端的Socket连接，以及相关数据的网络读取和写入磁盘。
 * 实现逻辑：
 * 1、初始化对象，指定端口号，调用waitConnect()方法等待连接；
 * 2、App端点击建立连接；
 * 3、App端选择要发送的文件类型，发送握手包ConnectHand到PC端；
 * 4、PC端选择要备份的文件；
 * 5、App端发送所有要发送的文件给PC端。
 * 注：一切外部类不负责异常处理和多线程操作。捕获异常和处理多线程并发需要由调用方（主程序）实现。
 */
public class ConnectApp {
    private final ServerSocket serverSocket;
    private InputStream inputStream;
    // private ObjectInputStream objInputStream;
    private NewObjInputStream objInputStream;       // 使用修改过的ObjectInputStream

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
     * 等待App端的连接请求，与App端建立连接。
     * 注：此方法会阻塞执行并等待，直到建立连接。
     * @throws IOException 等待连接时出现IO异常，例如Socket被意外关闭。
     * @throws ClassNotFoundException 反序列化封装类时，找不到指定的类。
     */
    public void waitConnect() throws IOException, ClassNotFoundException {
        // 等待App端连接
        Socket socket = serverSocket.accept();
        // 连接成功后，获取数据输入流
        inputStream = socket.getInputStream();
        // 读取App端发送的握手包
        readConnectionHand();
        // 握手成功后，读取真正的文件数据
        readPackages();

        // 最后关闭所有socket
        objInputStream.close();
        inputStream.close();
        socket.close();
        serverSocket.close();
    }

    /**
     * 读取App端发送的握手请求封装类。这个类包含手机名称等握手信息。
     * 注：此方法会阻塞直到握手封装类接收并解析完毕。
     * 需要先成功建立连接，再接收握手信息。
     * @throws IOException 读取握手封装时发生IO异常
     * @throws ClassNotFoundException 找不到握手封装类的类型，或类型不正确。
     */
    private void readConnectionHand() throws IOException, ClassNotFoundException {
        // 读取App端发送的ConectionHand握手封装
        objInputStream = new NewObjInputStream(inputStream, "Connection", "Connection");
        Object obj = objInputStream.readObject();

        // 读取握手信息，写入XML
        if (obj instanceof ConnectionHand) {
            // 在内存中创建新XML文件
            Document xmlFile = DocumentHelper.createDocument();

            // 初始化xml文件，创建根元素和两个子节点
            Element root = xmlFile.addElement("HandInformation");

            Element phoneInfo = root.addElement("Phone");
            Element fileInfo = root.addElement("Files");

            // 1、写入手机信息到XML
            Element phoneName = phoneInfo.addElement("name");
            phoneName.setText(((ConnectionHand) obj).phoneName());      // 手机名称
            Element phoneIp = phoneInfo.addElement("ip");
            phoneIp.setText(((ConnectionHand) obj).phoneIp());          // 手机IP地址
            Element phonePort = phoneInfo.addElement("port");
            phonePort.setText(((ConnectionHand) obj).appPort());        // 手机端应用端口号

            // 2、写入要备份的文件信息到XML
            for (TypeInfo info: ((ConnectionHand) obj).fileTypes()) {
                // 表示一种文件类型的节点
                Element file = fileInfo.addElement("file");
                file.addAttribute("name", info.fileType().toString());      // 标识文件类型

                Element fileNum = file.addElement("num");
                fileNum.setText(String.valueOf(info.fileNumber()));            // 文件总数
            }

            // 3、保存更改到文件
            Writer writer = new OutputStreamWriter(new FileOutputStream("src\\Connection\\Temp\\HandInfo.xml"));
            xmlFile.write(writer);
            writer.close();         // 关闭文件流。非常重要，否则文件为空
        }
    }

    /**
     * 此方法用于在连接和握手完毕后，读取发送的文件信息封装。
     * @throws IOException 读取时发生IO异常。
     * @throws ClassNotFoundException 找不到对应的类。
     */
    private void readPackages() throws IOException, ClassNotFoundException {
        while (true) {
            // 读取App发送的文件封装
            Object obj = objInputStream.readObject();
            // 如果此文件封装是Command，则表示App所有文件都已经发送完毕，可以退出循环
            if (obj instanceof SendPackageEnded) {
                return;
            }

            // 初始化xml文件
            Document xmlFile = DocumentHelper.createDocument();
            Element root = xmlFile.addElement("FileInfo");

            // 判断文件类型，执行对应操作
            if (obj instanceof PhotoPackage photoPackage) {

                // /////////////// 写入文件本体 /////////////// //
                // 指定唯一的临时文件名（时间戳_hash值）
                File thumbFile = new File("src\\Connection\\Temp\\" + System.currentTimeMillis() + "_" + Integer.toHexString(obj.hashCode()));

                if (thumbFile.isFile()) if (thumbFile.delete()) System.out.println("已删除冲突的临时文件");       // 若文件已存在，则先删除已存在的文件
                OutputStream thumbOutput = new FileOutputStream(thumbFile);
                thumbOutput.write(photoPackage.photoData());
                thumbOutput.close();

                // /////////////// 写入XML文件 /////////////// //
                // 写入文件信息
                Element fileName = root.addElement("name");
                fileName.setText(photoPackage.toString());               // 文件名
                Element filePath = root.addElement("path");
                filePath.setText(photoPackage.filePath());                  // 源文件路径
                Element fileSize = root.addElement("size");
                fileSize.setText(String.valueOf(photoPackage.fileSize()));  // 文件大小（MB）
                Element fileType = root.addElement("type");
                fileType.setText(photoPackage.photoType().toString());      // 文件类型（后缀名）

                // 图片内部信息（分辨率等）
                Element photo = root.addElement("photo");
                Element photoWidth = photo.addElement("width");
                photoWidth.setText(String.valueOf(photoPackage.photoSize().width()));
                Element photoHeight = photo.addElement("height");
                photoHeight.setText(String.valueOf(photoPackage.photoSize().height()));

                // 保存XML文件
                Writer writer = new OutputStreamWriter(new FileOutputStream("src\\Connection\\Temp\\" + obj + ".xml"));
                xmlFile.write(writer);
                writer.close();

            }   // 此处预留，以后添加更多文件类型
        }
    }
}
