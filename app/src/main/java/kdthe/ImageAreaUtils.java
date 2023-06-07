package kdthe;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;

import com.liulang.testandroid11.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import onbon.bx06.Bx6GException;
import onbon.bx06.Bx6GScreenProfile;
import onbon.bx06.Bx6GScreenRS;
import onbon.bx06.area.page.ImageFileBxPage;
import onbon.bx06.file.ProgramBxFile;
import onbon.bx06.series.Bx6E;

/**
 * @author liulang
 * @date 2023-06-01 15:12
 * @description
 **/

public class ImageAreaUtils {
    private static final String TAG = ImageAreaUtils.class.getSimpleName();
    private static Bx6GScreenRS screen;

    public static void send(Context context) {
        if (screen == null) {
            screen = new Bx6GScreenRS("Screen1", new Bx6E());
        }
        // connect to the screen
        // 连接控制器  ttyS1  ttyS2  ttyS3  ttyS4  ttyS9
        boolean isConnect = screen.connect("/dev/ttyS3", Bx6GScreenRS.BaudRate.RATE_57600);
        if (isConnect) {
            System.out.println("连接成功");
        }
        screen.deletePrograms();
        screen.deleteAllDynamic();
        Bx6GScreenProfile profile = screen.getProfile();
        ProgramBxFile programBxFile = new ProgramBxFile("P000", profile);
        programBxFile.setFrameShow(false);
        programBxFile.setProgramTimeSpan(60);
        ArrayList<String> filePaths = Text2ImageUtils.text2Image("这里所说血清钠作为水合作用的替代指标，血清钠的浓度越高，代表体液水平越低，喝的水越少。健康人群的血清钠水平应该在135-146毫摩尔/升。");
        onbon.bx06.area.TextCaptionBxArea bxArea = new onbon.bx06.area.TextCaptionBxArea(0, 0, 128, 80, profile);
        int len = filePaths.size();
        for (int i = 0; i < len; i++) {
            android.util.Log.d(TAG, "filePath:" + filePaths.get(i));
            ImageFileBxPage imageFileBxPage = new ImageFileBxPage(filePaths.get(i));
            bxArea.addPage(imageFileBxPage);
        }
        programBxFile.addArea(bxArea);
        try {
            boolean isSuccess = screen.writeProgram(programBxFile);
            android.util.Log.d(TAG, isSuccess ? "send: 发送节目成功" : "send: 发送节目失败");
//            screen.disconnect();
        } catch (Bx6GException e) {
            throw new RuntimeException(e);
        }
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


    /**
     * 直接下发图片，图片的尺寸要与LED的宽高相对应
     * demo 里面有一张test02.png 图片；因我的LED板是竖着安装，如何是文字转图片需要旋转角度，图片根据实际情况来定
     */
    public static String image(Context context) {
        // 获取drawable图片
        android.graphics.drawable.Drawable drawable = context.getResources().getDrawable(R.drawable.ic_launcher, null);
        // 将Drawable转换为Bitmap
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        // 创建文件  替换成您想要的文件名和格式
        File file = new File(Environment.getExternalStorageDirectory(), "test.png");

        try {
            FileOutputStream fos = new FileOutputStream(file);
            // 将Bitmap保存为JPEG格式到文件
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
