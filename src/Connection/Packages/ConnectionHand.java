package Connection.Packages;

import java.io.Serializable;

/**
 * 本封装用于存储手机的基本信息（型号等）。
 * App端连接PC端时将此封装传输给PC端，作为握手信息。
 *
 * @param phoneName 手机名
 * @param phoneIp   手机IP地址
 * @param appPort   App端口号
 * @param fileTypes App端将要上传的文件类型及其总数（封装类）
 */
public record ConnectionHand(String phoneName, String phoneIp, String appPort,
                             TypeInfo[] fileTypes) implements Serializable {
}
