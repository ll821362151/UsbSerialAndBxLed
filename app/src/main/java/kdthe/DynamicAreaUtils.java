package kdthe;

import java.util.ArrayList;

import onbon.bx06.Bx6GScreen;
import onbon.bx06.Bx6GScreenProfile;
import onbon.bx06.Bx6GScreenRS;
import onbon.bx06.area.DynamicBxArea;
import onbon.bx06.area.page.ImageFileBxPage;
import onbon.bx06.cmd.dyn.DynamicBxAreaRule;
import onbon.bx06.message.global.ACK;
import onbon.bx06.series.Bx6E;

/**
 * @author liulang
 * @date 2023-06-01 10:36
 * @company 湖南科大天河通信股份有限公司
 * @description
 **/

public class DynamicAreaUtils {
    private final static String TAG = DynamicAreaUtils.class.getSimpleName();
    private static Bx6GScreenRS screen;
    private static boolean isConnect;

    public static void send(String text) {
        if (text.isEmpty()) {
            text = "LED屏幕不同显示效果测试！";
        }
        // Create screen object, we create a bx05 screen using serial port
        // 创建 screen 对象，这里创建的是一个五代控制器的串口控制器对象
        Bx6E bx6E = new Bx6E();
        boolean isReverse = bx6E.isReverseFontData();
        System.out.println("isReverse:" + isReverse);
        if (screen == null) {
            screen = new Bx6GScreenRS("Screen1", bx6E);
        }
        // connect to the screen
        // 连接控制器  ttyS1  ttyS2  ttyS3  ttyS4  ttyS9
        if (!screen.isConnected()) {
            isConnect = screen.connect("/dev/ttyS4", Bx6GScreenRS.BaudRate.RATE_57600);
        }
        if (isConnect) {
            System.out.println("连接成功");
        }
        screen.deletePrograms();
        Bx6GScreenProfile profile = screen.getProfile();
        profile.isReverseFontData();
        // 创建动态区
        // BX-6E BX-6EX系列支持4个动态区，BX-6Q系列支持32个动态区
        DynamicBxAreaRule rule = new DynamicBxAreaRule();
        // 设定动态区ID ，此处ID为0 ，多个动态区ID不能相同
        rule.setId(0);
        // 设定异步节目停止播放，仅播放动态区
        // 0:与异步节目一起播放
        // 1:异步节目 停止播放，仅播放动态区
        // 2:当播放完节目编号坐高的异步节目后播放该动态区
        rule.setImmediatePlay((byte) 1);
        // 设定动态区循环播放
        // 0:循环显示
        // 1:显示完成后静止显示最后一页数据
        // 2:循环显示，超过设定时间后数据仍未更新时不再显示
        // 3:循环显示，超过设定时间后数据仍未更新时显示Logo信息
        // 4:循环显示，显示完成最后一页后就不再显示
        rule.setRunMode((byte) 0);
        DynamicBxArea area = new DynamicBxArea(0, 0, 128, 80, profile);
        ArrayList<String> filePaths = Text2ImageUtils.text2Image(text);
        int len = filePaths.size();
        for (int i = 0; i < len; i++) {
            ImageFileBxPage imageFileBxPage = new ImageFileBxPage(filePaths.get(i));
            imageFileBxPage.setStayTime(5000);
            area.addPage(imageFileBxPage);
        }
        Bx6GScreen.Result<ACK> result = screen.writeDynamic(rule, area);
        if (result.isOK()) {
            android.util.Log.d(TAG, "send success");
        }
        // disconnect
        // 断开连接
//        screen.disconnect();
    }

    public static void turnOn() {
        if (screen == null) {
            return;
        }
        screen.turnOn();
    }

    public static void turnOff() {
        if (screen == null) {
            return;
        }
        screen.turnOff();
    }
}
