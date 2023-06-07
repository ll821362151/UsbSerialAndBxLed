package com.hoho.android.usbserial.util;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CommProByte {

    private static void writeCurrTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS");
        System.out.println(dateFormat.format(new Date(System
                .currentTimeMillis())));
    }

    /**
     * int转为4字节byte[]
     *
     * @param
     * @return
     */
    public static byte[] int2Bytes(int n) {
        byte[] data = new byte[4];
        String str = getStrLenRange(Integer.toHexString(n), 8);
        for (int i = 0; i < 4; i++) {
            data[i] = to16Byte(str.substring(i * 2, i * 2 + 2));
        }
        return data;
    }

    /**
     * 字节数组转换为长整型long
     *
     * @param data 字节数组，注意长度不能超过8位，超过8位后8位为有效字节
     * @return
     */
    public static long bytes2Long(byte[] data) {
        byte[] ldata = new byte[8];
        if (data.length <= 8) {
            for (int i = 0; i < 8 - data.length; i++) {
                ldata[i] = 0x00;
            }
            for (int i = 0; i < data.length; i++) {
                ldata[8 - data.length + i] = data[i];
            }
        } else {
            for (int i = 0; i < 8; i++) {
                ldata[i] = data[data.length - 8 + i];
            }
        }
        return ((long) (ldata[0] & 0xFF) << 56)
                + ((long) (ldata[1] & 0xFF) << 48)
                + ((long) (ldata[2] & 0xFF) << 40)
                + ((long) (ldata[3] & 0xFF) << 32)
                + ((long) (ldata[4] & 0xFF) << 24)
                + ((long) (ldata[5] & 0xFF) << 16)
                + ((long) (ldata[6] & 0xFF) << 8) + (long) (ldata[7] & 0xFF);
    }

    /**
     * 字节数组转换为长整型long
     *
     * @param b0 第1个字节（从高位到低位）
     * @param b0 第2个字节（从高位到低位）
     * @param b0 第3个字节（从高位到低位）
     * @param b0 第4个字节（从高位到低位）
     * @return
     */
    public static long bytes2Long(byte b0, byte b1, byte b2, byte b3) {
        return ((long) (b0 & 0xFF) << 24) + ((long) (b1 & 0xFF) << 16)
                + ((long) (b2 & 0xFF) << 8) + (long) (b3 & 0xFF);
    }

    /**
     * 字节数组转换为长整型long字符串
     *
     * @param data 字节数组，注意长度不能超过8位，超过8位后8位为有效字节
     * @return
     */
    public static String bytes2LongStr(byte[] data) {
        return Long.toString(bytes2Long(data));
    }

    /**
     * 字节数组转换为长整型long字符串
     *
     * @param b0 第1个字节（从高位到低位）
     * @param b0 第2个字节（从高位到低位）
     * @param b0 第3个字节（从高位到低位）
     * @param b0 第4个字节（从高位到低位）
     * @return
     */
    public static String bytes2LongStr(byte b0, byte b1, byte b2, byte b3) {
        return Long.toString(bytes2Long(b0, b1, b2, b3));
    }

    /**
     * 字节数组转换为整型int
     *
     * @param data 字节数组，注意长度不能超过4位，超过4位后4位为有效字节
     * @return
     */
    public static int bytes2Int(byte[] data) {
        byte[] ldata = new byte[4];
        if (data.length <= 4) {
            for (int i = 0; i < 4 - data.length; i++) {
                ldata[i] = 0x00;
            }
            for (int i = 0; i < data.length; i++) {
                ldata[4 - data.length + i] = data[i];
            }
        } else {
            for (int i = 0; i < 4; i++) {
                ldata[i] = data[data.length - 4 + i];
            }
        }
        return ((ldata[0] & 0xFF) << 24) + ((ldata[1] & 0xFF) << 16)
                + ((ldata[2] & 0xFF) << 8) + (ldata[3] & 0xFF);
    }

    /**
     * 字节数组转换为长整型int
     *
     * @param b0 第1个字节（从高位到低位）
     * @param b0 第2个字节（从高位到低位）
     * @param b0 第3个字节（从高位到低位）
     * @param b0 第4个字节（从高位到低位）
     * @return
     */
    public static int bytes2Int(byte b0, byte b1, byte b2, byte b3) {
        return ((b0 & 0xFF) << 24) + ((b1 & 0xFF) << 16) + ((b2 & 0xFF) << 8)
                + (b3 & 0xFF);
    }

    /**
     * 字节数组转换为长整型int
     *
     * @param b0 第1个字节（从高位到低位）
     * @param b0 第2个字节（从高位到低位）
     * @param b0 第3个字节（从高位到低位）
     * @return
     */
    public static int bytes2Int(byte b0, byte b1, byte b2) {
        return ((b0 & 0xFF) << 16) + ((b1 & 0xFF) << 8) + (b2 & 0xFF);
    }

    /**
     * 字节数组转换为长整型int
     *
     * @param b0 第1个字节（从高位到低位）
     * @param b0 第2个字节（从高位到低位）
     * @return
     */
    public static int bytes2Int(byte b0, byte b1) {
        return ((b0 & 0xFF) << 8) + (b1 & 0xFF);
    }

    /**
     * 字节数组转换为长整型int
     *
     * @param b 字节
     * @return
     */
    public static int bytes2Int(byte b) {
        return b & 0xFF;
    }

    /**
     * 字节数组转换为整型int字符串
     *
     * @param data 字节数组，注意长度不能超过4位，超过4位后4位为有效字节
     * @return
     */
    public static String bytes2IntStr(byte[] data) {
        return Integer.toString(bytes2Int(data));
    }

    /**
     * 字节数组转换为长整型int字符串
     *
     * @param b0 第1个字节（从高位到低位）
     * @param b0 第2个字节（从高位到低位）
     * @param b0 第3个字节（从高位到低位）
     * @param b0 第4个字节（从高位到低位）
     * @return
     */
    public static String bytes2IntStr(byte b0, byte b1, byte b2, byte b3) {
        return Integer.toString(bytes2Int(b0, b1, b2, b3));
    }

    /**
     * 字节数组转换为长整型int字符串
     *
     * @param b0 第1个字节（从高位到低位）
     * @param b0 第2个字节（从高位到低位）
     * @param b0 第3个字节（从高位到低位）
     * @return
     */
    public static String bytes2IntStr(byte b0, byte b1, byte b2) {
        return Integer.toString(bytes2Int(b0, b1, b2));
    }

    /**
     * 字节数组转换为长整型int字符串
     *
     * @param b0 第1个字节（从高位到低位）
     * @param b0 第2个字节（从高位到低位）
     * @return
     */
    public static String bytes2IntStr(byte b0, byte b1) {
        return Integer.toString(bytes2Int(b0, b1));
    }

    /**
     * 字节数组转换为长整型int字符串
     *
     * @param b 字节
     * @return
     */
    public static String bytes2IntStr(byte b) {
        return Integer.toString(bytes2Int(b));
    }

    /**
     * 长度为0的byte数组
     */
    public static byte[] NullBytes = new byte[0];

    /**
     * 长度为0的ListBytes
     */
    public static List<byte[]> NullListBytes = new ArrayList<byte[]>();

    /**
     * 文字转 Unicode 编码字节数组
     *
     * @param text 文字
     * @return 字节数组
     */
    public static byte[] text2Bytes(String text) {
        List<Byte> data = new ArrayList<Byte>();
        try {
            for (int i = 0; i < text.length(); i++) {
                byte[] b;
                b = text.substring(i, i + 1).getBytes("UTF-16BE");
                data.add((byte) (0xFF & b[1]));
                data.add((byte) (0xFF & b[0]));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string2Bytes2(bytes2String(data));
    }

    /**
     * ASCII字符串转byte数组
     *
     * @param ascii ASCII字符串
     * @return
     */
    public static byte[] ascii2Bytes(String ascii) {
        byte[] data = new byte[ascii.length()];
        char[] chars = ascii.toCharArray();
        for (int i = 0; i < ascii.length(); i++) {
            data[i] = (byte) (chars[i] & 0xFF);
        }
        return data;
    }

    /**
     * byte数组转ASCII
     *
     * @param data byte数组
     * @return
     */
    public static String bytes2Ascii(byte[] data) {
        StringBuilder str = new StringBuilder();
        for (byte datum : data) {
            str.append((char) datum);
        }
        return str.toString();
    }

    /**
     * Unicode编码字节数组转文字
     *
     * @param data Unicode编码字节数组
     * @return 文字
     */
    public static String bytes2Text(byte[] data) {
        String[] arr = bytes2String(data).split(" ");
        StringBuilder ostr = new StringBuilder();
        for (int i = 0; i < arr.length / 2; i++) {
            ostr.append((char) Integer.decode("0x" + arr[i * 2 + 1] + arr[i * 2])
                    .intValue());
        }
        return ostr.toString();
    }

    /**
     * 返回限定长度的字符串，不够长度在前面补0
     *
     * @param str  字符串
     * @param iLen 限定长度
     * @return 限定长度的字符串
     */
    public static String getStrLenRange(String str, int iLen) {
        int n = str.length();
        if (n < iLen) {
            for (int i = 0; i < iLen - n; i++) {
                str = "0" + str;
            }
        }
        return str;
    }

    /**
     * 返回满足长度的地址
     *
     * @param strAddress 终端地址
     * @return 满足长度的地址
     */
    public static String getFullAddress(String strAddress) {
        return getStrLenRange(strAddress, 6);
    }

    /**
     * Hex字符串转换为Byte
     *
     * @param strHex Hex字符串
     * @return Byte
     */
    public static byte to16Byte(String strHex) {
        if (strHex.length() > 2) {
            strHex = strHex.substring(0, 2);
        }
        if (strHex.matches("[0123456789ABCDEFabcdef]{1,2}")) {
            return (byte) Integer.decode("0x" + strHex).intValue();
        }
        return 0x00;
    }

    /**
     * Byte转换为Hex字符串
     *
     * @param b Byte
     * @return Hex字符串
     */
    public static String to16String(byte b) {
        return getStrLenRange(Integer.toHexString(b & 0xFF).toUpperCase(), 2);
    }

    /**
     * 从字符串转换为byte数组
     *
     * @param str 字符串，16进制字符，两个一组，用空格隔开，如（00 11 22 33 44 55 66）
     * @return byte数组
     */
    public static List<Byte> string2Bytes(String str) {
        List<Byte> bData = new ArrayList<Byte>();
        if (str.length() > 0
                && (str.trim() + " ")
                .matches("^([0123456789ABCDEFabcdef][0123456789ABCDEFabcdef] )+$")) {
            String[] strArr = str.trim().split(" ");
            for (String s : strArr) {
                bData.add(to16Byte(s));
            }
        }
        return bData;
    }

    /**
     * 从字符串转换为byte数组
     *
     * @param str 字符串，16进制字符，两个一组，不用空格隔开，如（00112233445566）
     * @return byte数组
     */
    public static List<Byte> string2Bytes_UnNull(String str) {
        List<Byte> bData = new ArrayList<Byte>();
        if (str.length() > 0
                && (str.trim())
                .matches("^([0123456789ABCDEFabcdef][0123456789ABCDEFabcdef])+$")) {
            for (int i = 0; i < str.length() / 2; i++) {
                bData.add(to16Byte(str.substring(i * 2, i * 2 + 2)));
            }
        }
        return bData;
    }

    /**
     * 从字符串转换为byte数组
     *
     * @param str 字符串，16进制字符，两个一组，用空格隔开，如（00 11 22 33 44 55 66）
     * @return byte数组
     */
    public static byte[] string2Bytes2(String str) {
        String[] strArr = str.trim().split(" ");
        byte[] bData = new byte[strArr.length];
        if (str.length() > 0
                && (str.trim() + " ")
                .matches("^([0123456789ABCDEFabcdef][0123456789ABCDEFabcdef] )+$")) {
            for (int i = 0; i < strArr.length; i++) {
                bData[i] = to16Byte(strArr[i]);
            }
        }
        return bData;
    }

    /**
     * 从字符串转换为byte数组
     *
     * @param str 字符串，16进制字符，两个一组，不用空格隔开，如（00112233445566）
     * @return byte数组
     */
    public static byte[] string2Bytes2_UnNull(String str) {
        if (str.length() > 0
                && (str.trim())
                .matches("^([0123456789ABCDEFabcdef][0123456789ABCDEFabcdef])+$")) {
            byte[] bData = new byte[str.length() / 2];
            for (int i = 0; i < str.length() / 2; i++) {
                bData[i] = to16Byte(str.substring(i * 2, i * 2 + 2));
            }
            return bData;
        }
        return NullBytes;
    }

    /**
     * 从byte数组转换为字符串
     *
     * @param bData
     * @return 字符串，16进制字符，两个一组，用空格隔开，如（00 11 22 33 44 55 66）
     */
    public static String bytes2String(byte[] bData) {
        if (bData.length > 0) {
            StringBuilder str = new StringBuilder();
            for (byte b : bData) {
                str.append(to16String(b));
                str.append(" ");
            }
            return str.toString().trim();
        }
        return "";
    }

    /**
     * 从byte数组转换为字符串
     *
     * @param bData
     * @return 字符串，16进制字符，两个一组，不用空格隔开，如（00112233445566）
     */
    public static String bytes2String_UnNull(byte[] bData) {
        if (bData.length > 0) {
            StringBuilder str = new StringBuilder();
            for (byte b : bData) {
                str.append(to16String(b));
            }
            return str.toString().trim();
        }
        return "";
    }

    /**
     * 从byte数组转换为字符串
     *
     * @param bData
     * @return 字符串，16进制字符，两个一组，用空格隔开，如（00 11 22 33 44 55 66）
     */
    public static String bytes2String(List<Byte> bData) {
        if (bData.size() > 0) {
            StringBuilder str = new StringBuilder();
            for (byte b : bData) {
                str.append(to16String(b));
                str.append(" ");
            }
            return str.toString().trim();
        }
        return "";
    }

    /**
     * 从byte数组转换为字符串
     *
     * @param bData
     * @return 字符串，16进制字符，两个一组，不用空格隔开，如（00 11 22 33 44 55 66）
     */
    public static String bytes2String_UnNull(List<Byte> bData) {
        if (bData.size() > 0) {
            StringBuilder str = new StringBuilder();
            for (byte b : bData) {
                str.append(to16String(b));
            }
            return str.toString().trim();
        }
        return "";
    }

    /**
     * 转换byte[]为List<Byte>
     *
     * @param bData byte[]
     * @return List<Byte>
     */
    public static List<Byte> bytes2List(byte[] bData) {

        List<Byte> retData = new ArrayList<Byte>();
        for (byte b : bData) {
            retData.add(b);
        }
        return retData;
    }

    /**
     * 转换List<Byte>为byte[]
     *
     * @param bData List<Byte>
     * @return byte[]
     */
    public static byte[] list2Bytes(List<Byte> bData) {
        byte[] send = new byte[bData.size()];
        for (int i = 0; i < send.length; i++) {
            send[i] = bData.get(i);
        }
        return send;
    }

    /**
     * 把byte[]添加到List
     *
     * @param bData
     * @return
     */
    public static List<Byte> listAddRange(List<Byte> lists, byte[] bData) {
        for (byte b : bData) {
            lists.add(b);
        }
        return lists;
    }

    /**
     * List byte[]合并为一个byte[]
     *
     * @param data List byte[]
     * @return
     */
    public static byte[] ListBytes2Bytes(List<byte[]> data) {
        int s = 0;
        for (byte[] d : data) {
            s += d.length;
        }
        byte[] res = new byte[s];
        int i = 0;
        for (byte[] bs : data) {
            for (byte b : bs) {
                res[i] = b;
                i++;
            }
        }
        return res;
    }


    /**
     * 返回+0x55的16进制Byte
     *
     * @param bValue 16进制字节
     * @return +0x55的16进制Byte
     */
    public static byte toAdd0x55(byte bValue) {
        return (byte) ((bValue + 0x55) & 0xFF);
    }

    /**
     * 返回-0x55的16进制Byte
     *
     * @param bValue 16进制字节
     * @return -0x55的16进制Byte
     */
    public static byte toSub0x55(byte bValue) {
        if (bValue >= 0x55) {
            return (byte) (bValue - 0x55);
        }
        return (byte) (0x100 + bValue - 0x55);
    }

    /**
     * 获取CRC效验和（密钥专用，不计后一位）
     *
     * @param bData 数据
     * @return 效验码
     */
    public static byte getCRC8_Key(byte[] bData) {
        int iSum = 0;
        for (int i = 0; i < bData.length - 1; i++) {
            iSum = (iSum + bData[i]) & 0xFF;
        }
        return (byte) iSum;
    }

    /**
     * 获取CRC效验和（DLT645规约，不计算后两位）
     *
     * @param bData 数据
     * @return 效验码
     */
    public static byte getCRC8_DLT645(byte[] bData) {
        int iSum = 0;
        for (int i = 0; i < bData.length - 2; i++) {
            iSum = (iSum + bData[i]) & 0xFF;
        }
        return (byte) iSum;
    }

    /**
     * 获取CRC效验和（全部计算）
     *
     * @param bData 数据
     * @return 效验码
     */
    public static byte getCRC8(byte[] bData) {
        int iSum = 0;
        for (byte b : bData) {
            iSum = (iSum + b) & 0xFF;
        }
        return (byte) iSum;
    }


    /**
     * 判断终端地址是否正确
     *
     * @param address 终端地址
     * @return 终端地址是否正确
     */
    public static boolean checkAddress(String address) {
        return address.matches("^[0123456789ABCDEFabcdef]{1,6}$");
    }

    /**
     * 判断数据域标识是否正确
     *
     * @param mark 数据域标识
     * @return 数据域标识是否正确
     */
    public static boolean checkMark(String mark) {
        return mark.matches("^[0123456789ABCDEFabcdef]{4}$");
    }

    /**
     * 判断密码是否正确
     *
     * @param pwd 密码
     * @return
     */
    public static boolean checkPwd(String pwd) {
        return pwd.matches("^[0123456789ABCDEFabcdef]{6}$");
    }


    /**
     * 两个字节转换为整数
     *
     * @param b1 前一位字节
     * @param b2 后一位字节
     * @return 整数
     */
    public static int twoByte2Int(byte b1, byte b2) {
        return (b2 & 0xFF) | (b1 & 0xFF) << 8;
    }


    /**
     * 单个byte[]转换为List byte[]
     *
     * @param bData 单个byte[]
     * @return
     */
    public static List<byte[]> bytes2ListBytes(byte[] bData) {
        List<byte[]> bSend = new ArrayList<byte[]>();
        bSend.add(bData);
        return bSend;
    }

    /**
     * 返回只包含第一条指令的List
     *
     * @param data
     * @return
     */
    public static List<byte[]> GetListFirst(List<byte[]> data) {
        return bytes2ListBytes(data.get(0));
    }

    /**
     * 增加的4字节长度前缀
     *
     * @param bData 发送数据
     * @return
     */
    public static byte[] sendPrefixByte(byte[] bData) {
        byte[] bSend = new byte[bData.length + 4];
        String str = getStrLenRange(Integer.toHexString(bData.length), 8);
        for (int i = 0; i < 4; i++) {
            bSend[i] = to16Byte(str.substring(i * 2, i * 2 + 2));
        }
        for (int i = 4; i < bSend.length; i++) {
            bSend[i] = bData[i - 4];
        }
        return bSend;
    }




    /**
     * 获得纯文字字符命令提交JSON
     *
     * @param submitkey  提交Key
     * @param submitdata 提交内容
     * @return
     */
    public static String getSdStr(String submitkey, String submitdata) {
        return "{\"submitkey\":\"" + submitkey + "\",\"submitdata\":\""
                + inJsonStr(submitdata) + "\"}";
    }

    /**
     * 内置对象传入转换
     *
     * @param json Json字符串
     * @return
     */
    public static String inJsonStr(String json) {
        json = json.replace("{", "-|hkh_l|-");
        json = json.replace("}", "-|hkh_r|-");
        json = json.replace("[", "-|zkh_l|-");
        json = json.replace("]", "-|zkh_r|-");
        json = json.replace("\"", "-|syh|-");
        json = json.replace(":", "-|mh|-");
        json = json.replace(",", "-|dh|-");
        return json;
    }

    /**
     * 内置对象传出转换
     *
     * @param json Json字符串
     * @return
     */
    public static String outJsonStr(String json) {
        json = json.replace("-|hkh_l|-", "{");
        json = json.replace("-|hkh_r|-", "}");
        json = json.replace("-|zkh_l|-", "[");
        json = json.replace("-|zkh_r|-", "]");
        json = json.replace("-|syh|-", "\"");
        json = json.replace("-|mh|-", ":");
        json = json.replace("-|dh|-", ",");
        return json;
    }

    /**
     * 传入Json的值转换
     *
     * @param str Json值
     * @return
     */
    public static String inJsonValue(String str) {
        str = str.replace("=!hkh_l!=", "");
        str = str.replace("=!hkh_r!=", "");
        str = str.replace("=!zkh_l!=", "");
        str = str.replace("=!zkh_r!=", "");
        str = str.replace("=!syh!=", "");
        str = str.replace("=!mh!=", "");
        str = str.replace("=!dh!=", "");
        str = str.replace("{", "=!hkh_l!=");
        str = str.replace("}", "=!hkh_r!=");
        str = str.replace("[", "=!zkh_l!=");
        str = str.replace("]", "=!zkh_r!=");
        str = str.replace("\"", "=!syh!=");
        str = str.replace(":", "=!mh!=");
        str = str.replace(",", "=!dh!=");
        str = str.replace("\\", "=!fxg!=");
        str = str.replace("\n", "=!hhf!=");
        str = str.replace("\r", "");
        str = str.replace("\t", "=!tgj!=");
        return str;
    }

    /**
     * 传出Json的值转换
     *
     * @param str Json值
     * @return
     */
    public static String outJsonValue(String str) {
        str = str.replace("=!hkh_l!=", "{");
        str = str.replace("=!hkh_r!=", "}");
        str = str.replace("=!zkh_l!=", "[");
        str = str.replace("=!zkh_r!=", "]");
        str = str.replace("=!syh!=", "\"");
        str = str.replace("=!mh!=", ":");
        str = str.replace("=!dh!=", ",");
        str = str.replace("=!fxg!=", "\\");
        str = str.replace("=!hhf!=", "\r\n");
        str = str.replace("=!tgj!=", "\t");
        return str;
    }

    /**
     * 获取消息ID（6字节，日期时间对应的时间戳毫秒数值HEX码）
     *
     * @return
     */
    public static byte[] getMsgId() {
        byte[] id = new byte[6];
        String strMill = getStrLenRange(
                Long.toHexString(System.currentTimeMillis()), 12);
        id[0] = to16Byte(strMill.substring(0, 2));
        id[1] = to16Byte(strMill.substring(2, 4));
        id[2] = to16Byte(strMill.substring(4, 6));
        id[3] = to16Byte(strMill.substring(6, 8));
        id[4] = to16Byte(strMill.substring(8, 10));
        id[5] = to16Byte(strMill.substring(10, 12));
        return id;
    }

    /**
     * 从消息ID还原为日期时间
     *
     * @param id 消息ID
     * @return
     */
    public static Calendar getMsgIdToCalendar(byte[] id) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(bytes2String_UnNull(id), 16));
        return cal;
    }
}