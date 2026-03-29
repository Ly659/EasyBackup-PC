package Connection.Packages.Command;

import java.io.Serializable;

public class Command implements Serializable {
    private Commands command;       // 命令
    private String filePath;              // 要发送或删除文件的路径
}
