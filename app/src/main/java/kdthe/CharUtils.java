package kdthe;

/**
 * @author liulang
 * @date 2023-06-02 14:57
 * @company 湖南科大天河通信股份有限公司
 * @description
 **/

public class CharUtils {
    public static String toFullWidth(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 0x21 && c <= 0x7E) {
                // 字母和数字转换为全角
                c = (char) (c + 0xFEE0);
            } else if (c == 0x20) {
                // 空格转换为全角空格
                c = (char) 0x3000;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
