package Connection.Packages.PhotoPackage;

import java.io.Serializable;

/**
 * 此类用于封装图片尺寸。此对象一经创建，其图片尺寸数据就无法再修改。
 */
public record PhotoSize(int width, int height) implements Serializable {
    /**
     * 初始化封装类。
     *
     * @param width  图片宽度（像素）
     * @param height 图片高度（像素）
     */
    public PhotoSize {
    }
}
