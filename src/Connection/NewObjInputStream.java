/**
 * 重写ObjectInput类的resolveClass方法，解决反序列化由于包名不一致而失败的问题。
 *
 */

package Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class NewObjInputStream extends ObjectInputStream {
    private final String oldPackagePath;
    private final String newPackagePath;

    /**
     *
     * @param in 像原版ObjectInputStream那样，需要传入InputStream对象
     * @param oldPackageName 旧的Package路径
     * @param newPackageName 要替换成的新Package路径
     * @throws IOException 详见原版。
     */
    public NewObjInputStream(InputStream in, String oldPackageName, String newPackageName) throws IOException {
        super(in);
        this.oldPackagePath = oldPackageName;
        this.newPackagePath = newPackageName;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {
        String OldClassPath = osc.getName();

        // 替换类的名称
        String NewClassPath = OldClassPath.replace(oldPackagePath, newPackagePath);
        try {
            // 返回映射完成后的类
            return Class.forName(NewClassPath);
        } catch (ClassNotFoundException e) {
            // 若映射失败，则返回原始类，不做修改
            return super.resolveClass(osc);
        }

    }
}
