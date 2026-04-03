package Connection.Packages.PhotoPackage;

import java.io.Serializable;

/**
 * 用于封装图片数据的Package。此类可以被序列化，通过Socket传输。
 * 此封装包含的信息有：
 * 文件数据
 * 文件路径
 * 照片尺寸
 *
 * @param photoData 文件内容数据
 * @param filePath  存储图片信息 文件路径（\Storage\emulated\0\为根目录）
 * @param photoType 文件类型
 * @param fileSize  文件大小（单位：MB）
 * @param photoSize 图片尺寸
 */
public record PhotoPackage(byte[] photoData, String filePath, int fileSize, PhotoTypes photoType,
                           PhotoSize photoSize) implements Serializable {
    //……//

    /**
     * 初始化封装，需要指定图片的数据和文件名
     * 此构造用于存储和传输缩略图。
     *
     * @param photoData 图片数据
     * @param filePath  文件名
     * @param fileSize  文件大小（单位：MB）
     * @param photoType 文件格式（常量）
     * @param photoSize 原图的尺寸信息
     */
    public PhotoPackage {
    }

    // 暂时不用了。发送原图和发送其他文件一样，直接使用文件流了。
//    /**
//     * 初始化封装，仅需指定图片数据。
//     * 此构造用于存储和传输原图。
//     * @param photoData 图片数据
//     */
//    public PhotoPackage(byte[] photoData) {
//        photoSize = null;
//        photoType = null;
//        fileSize = -1;
//        this.photoData = photoData;
//    }


    /**
     * 获取图片的文件路径。
     *
     * @return 文件路径
     */
    @Override
    public String filePath() {
        return filePath;
    }

    /**
     * 获取文件名（包含后缀名）。
     *
     * @return 文件名
     */
    @Override
    public String toString() {
        return filePath.substring(filePath.lastIndexOf("\\"));
    }

    /**
     * 获取图片的数据。
     *
     * @return 图片数据
     */
    @Override
    public byte[] photoData() {
        return photoData;
    }

    /**
     * 获取图片的尺寸信息。
     *
     * @return 储存信息的封装类（包含宽Width和高Height）
     */
    @Override
    public PhotoSize photoSize() {
        return photoSize;
    }

    /**
     * 获取文件的格式类型。
     *
     * @return 图片的文件格式（一个常量）
     */
    @Override
    public PhotoTypes photoType() {
        return photoType;
    }

    /**
     * 获取文件的大小信息。
     *
     * @return 文件大小（单位：MB）
     */
    public int fileSize() {
        return fileSize;
    }
}
