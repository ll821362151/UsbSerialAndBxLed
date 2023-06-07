package kdthe;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author liulang
 * @date 2023-06-01 17:07
 * @description
 **/

public class Text2ImageCenterUtils {
    public static ArrayList<String> text2Image(String text) {
        // 存储文件路径的数组集合
        ArrayList<String> filePaths = new ArrayList<>();
        // 矩形宽度
        int width = 80;
        // 矩形高度
        int height = 128;

// 创建一个空的Bitmap对象
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

// 创建一个Canvas对象，用于在Bitmap上绘制
        Canvas canvas = new Canvas(bitmap);

// 绘制矩形
        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.BLACK); // 设置矩形颜色
        canvas.drawRect(0, 0, width, height, paint);
// 绘制文字
        if (text.isEmpty()) {
            text = "Hello, World!";
        }
        paint.setColor(android.graphics.Color.WHITE); // 设置文字颜色
        paint.setTextSize(20); // 设置文字大小
        float textWidth = paint.measureText(text); // 测量文字宽度
        float x = (width - textWidth) / 2; // 计算文字在矩形中的x坐标
        float y = height / 2; // 计算文字在矩形中的y坐标
        canvas.drawText(text, x, y, paint);

        android.graphics.Matrix matrix = new android.graphics.Matrix();
        // 逆时针旋转90度
        matrix.postRotate(-90);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
// 保存Bitmap为图片文件
        File file = new File(Environment.getExternalStorageDirectory(), "image.jpg"); // 替换成您想要保存的文件名和格式
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); // 将Bitmap保存为JPEG格式到文件
            fos.flush();
            fos.close();
            filePaths.add(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        return filePaths;
    }
}

