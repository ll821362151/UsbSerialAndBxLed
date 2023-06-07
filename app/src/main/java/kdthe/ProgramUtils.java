package kdthe;

import java.awt.Font;

import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreenProfile;
import onbon.bx06.Bx6GScreenRS;
import onbon.bx06.area.page.TextBxPage;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.series.Bx6E;
import onbon.bx06.utils.TextBinary;

/**
 * @author liulang
 * @date 2023-06-01 10:48
 * @company 湖南科大天河通信股份有限公司
 * @description
 **/

public class ProgramUtils {
    private static String TAG = ProgramUtils.class.getSimpleName();
    private static Bx6GScreenRS gScreenRS;


    public static void send() {
        gScreenRS = new Bx6GScreenRS("program01", new Bx6E());
        boolean isConnect = gScreenRS.connect("/dev/ttyS3", Bx6GScreenRS.BaudRate.RATE_57600);
        if (!isConnect) {
            android.util.Log.d(TAG, "send: 连接失败");
            return;
        }
        gScreenRS.turnOff();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gScreenRS.turnOn();
        gScreenRS.deleteAllDynamic();
        gScreenRS.deletePrograms();
        Bx6GScreenProfile profile = gScreenRS.getProfile();
        ProgramBxFile programBxFile = new ProgramBxFile("P000", profile);
        programBxFile.setFrameShow(false);
        onbon.bx06.area.TextCaptionBxArea bxArea = new onbon.bx06.area.TextCaptionBxArea(0, 0, 128, 80, profile);
//        TextBxPage bxPage = new TextBxPage("P000 - This is the first program!");
//        bxPage.setFont(new Font("@宋体", Font.PLAIN, 12));
//        bxPage.setNewLineNewPage(true);
//        bxPage.setVerticalAlignment(TextBinary.Alignment.FAR);
//        bxPage.setHorizontalAlignment(TextBinary.Alignment.CENTER);
//        bxArea.addPage(bxPage);
        TextBinary textBinary = new TextBinary(128, 80);
        textBinary.verticalMoving("科大天河通信股份有限公司", TextBinary.Alignment.CENTER, 80);
        TextBxPage bxPage2 = new TextBxPage("科大天河通信股份有限公司!");
        bxPage2.setFont(new Font("宋体", Font.PLAIN, 12));
        bxPage2.setNewLineNewPage(true);
        bxPage2.setVerticalAlignment(TextBinary.Alignment.CENTER);
        bxPage2.setHorizontalAlignment(TextBinary.Alignment.CENTER);
        bxArea.addPage(bxPage2);
        try {
            programBxFile.addArea(bxArea);
            boolean isSuccess = gScreenRS.writeProgram(programBxFile);
            android.util.Log.d(TAG, isSuccess ? "send: 发送节目成功" : "send: 发送节目失败");
        } catch (Bx6GException e) {
            throw new RuntimeException(e);
        }
    }

}
