package Connection.Packages;

/**
 * 存储每种文件的类型和总数。
 * @param fileType 文件类型（常量）
 * @param fileNumber 该类型的文件总数
 */
public record TypeInfo(Types fileType, int fileNumber) {
}
