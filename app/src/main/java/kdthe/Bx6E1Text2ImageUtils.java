package kdthe;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author liulang
 * @date 2023-06-01 16:31
 * @company 湖南科大天河通信股份有限公司
 * @description
 **/

public class Bx6E1Text2ImageUtils {
    public static String text2Image(String text) {
        int width = 80; // 矩形宽度
        int height = 128; // 矩形高度
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
            text = "Hello, World! This is a long text that needs to wrap automatically.";
        }
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(android.graphics.Color.WHITE); // 设置文字颜色
        textPaint.setTextSize(16); // 设置文字大小
        // 自动换行绘制文字
        StaticLayout staticLayout = new StaticLayout(text, textPaint, width, android.text.Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        canvas.save();
        // 计算文字在矩形中的y坐标居中显示
        canvas.translate(0, (height - staticLayout.getHeight()) / 2.f);
        staticLayout.draw(canvas);
        canvas.restore();
        // 创建一个Matrix对象进行旋转
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        // 逆时针旋转90度
        matrix.postRotate(-90);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        // 保存Bitmap为图片文件 // 替换成您想要保存的文件名和格式
        File file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            // 将Bitmap保存为JPEG格式到文件
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file.getAbsolutePath();
    }
}
