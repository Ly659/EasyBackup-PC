package Connection.Packages.PhotoPackage;

import java.io.Serializable;

/**
 * 用于封装图片数据的Package。此类可以被序列化，通过Socket传输。
 */
public class PhotoPackage implements Serializable {
    private final byte[] photoData;       // 文件内容数据

    // 存储图片信息
    private String fileName;        // 文件名
    private final PhotoTypes photoType;   // 文件类型
    private final PhotoSize photoSize;    // 图片尺寸
    //……//

    /**
     * 初始化封装，需要指定图片的数据和文件名
     * 此构造用于存储和传输缩略图。
     * @param photoData 图片数据
     * @param fileName 文件名
     * @param photoSize 原图的尺寸信息
     */
    public PhotoPackage(byte[] photoData, String fileName, PhotoTypes type, PhotoSize photoSize) {
        this.photoData = photoData;
        this.fileName = fileName;
        this.photoType = type;
        this.photoSize = photoSize;
    }

    /**
     * 初始化封装，仅需指定图片数据。
     * 此构造用于存储和传输原图。
     * @param photoData 图片数据
     */
    public PhotoPackage(byte[] photoData) {
        photoSize = null;
        photoType = null;
        this.photoData = photoData;
    }


    /**
     * 获取图片的文件名。
     * @return 文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 获取图片的数据。
     * @return 图片数据
     */
    public byte[] getPhotoData() {
        return photoData;
    }

    /**
     * 获取图片的尺寸信息。
     * @return 储存信息的封装类（包含宽Width和高Height）
     */
    public PhotoSize getPhotoSize() {
        return photoSize;
    }

    /**
     * 获取文件的格式类型。
     * @return 图片的文件格式（一个常量）
     */
    public PhotoTypes getPhotoType() {
        return photoType;
    }
}
