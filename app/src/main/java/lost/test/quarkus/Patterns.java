package lost.test.quarkus;

public class Patterns {
    /**
     * 中文或英文开头，中文或英文或数字结尾，中间允许空格或 '-' 或 '_'
     */
    public static final String name =
            "^[\u4E00-\u9FA5A-Za-z]([\u4E00-\u9FA5A-Za-z0-9_ \\-]*[\u4E00-\u9FA5A-Za-z0-9])?$";
}
