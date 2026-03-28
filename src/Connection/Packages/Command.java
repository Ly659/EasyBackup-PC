package Connection.Packages;

/**
 * 储存所有的命令。以下是常用的命令：
 * RemoveFile：删除手机上指定的文件
 * SendFile：将手机上的指定文件发送给电脑
 */
enum Commands {
    RemoveFile, SendFile
}

public class Command {
    private Commands command;       // 命令

}
