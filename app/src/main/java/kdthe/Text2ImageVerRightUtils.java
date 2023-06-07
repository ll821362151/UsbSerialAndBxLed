package kdthe;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author liulang
 * @date 2023-06-02 10:48
 * @description
 **/

public class Text2ImageVerRightUtils {
    public static ArrayList<String> text2Image(String text) {
        text = CharUtils.toFullWidth(text);
        // 矩形宽度
        int width = 128;
        // 矩形高度
        int height = 80;
        // 每页最大行数，假设每行高度为16像素
        int maxLinesPerPage = height / 16;
        // 存储文件路径的数组集合
        ArrayList<String> filePaths = new ArrayList<>();
        // 计算总页数
        TextPaint textPaint = new TextPaint();
        // 设置文字颜色
        textPaint.setColor(android.graphics.Color.WHITE);
        // 设置文字大小
        textPaint.setTextSize(16);
        StaticLayout staticLayout = new StaticLayout(text, textPaint, width, android.text.Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        int lineCount = staticLayout.getLineCount();
        int totalPages = (int) Math.ceil((double) lineCount / maxLinesPerPage);
        // 分页绘制并保存图片
        for (int page = 0; page < totalPages; page++) {
            // 创建一个空的Bitmap对象
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            // 创建一个Canvas对象，用于在Bitmap上绘制
            Canvas canvas = new Canvas(bitmap);
            // 绘制矩形
            Paint paint = new Paint();
            // 设置矩形颜色
            paint.setColor(android.graphics.Color.BLACK);
            canvas.drawRect(0, 0, width, height, paint);
            // 计算当前页需要绘制的起始行和结束行
            int startLine = page * maxLinesPerPage;
            int endLine = Math.min(startLine + maxLinesPerPage, lineCount);
            // 自动换行绘制文字 // 每行的高度
            int lineHeight = height / maxLinesPerPage;
            //画一个矩形
            Rect rect = new Rect(0, 0, width, lineHeight);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            //为基线到字体上边框的距离,即上图中的top
            float top = fontMetrics.top;
            //为基线到字体下边框的距离,即上图中的bottom
            float bottom = fontMetrics.bottom;
            //基线中间点的y轴计算公式
            int startY = (int) (rect.centerY() - top / 2 - bottom / 2);
            for (int i = startLine; i < endLine; i++) {
                int startX = (int) (rect.centerY() - top / 2 - bottom / 2);
                int lineStart = staticLayout.getLineStart(i);
                int lineEnd = staticLayout.getLineEnd(i);
                String lineText = text.substring(lineStart, lineEnd);
                char[] chars = lineText.toCharArray();
                for (char character : chars) {
                    canvas.save();
                    canvas.rotate(-90, startX, startY);
                    canvas.drawText(String.valueOf(character), startX, startY, textPaint);
                    canvas.restore();
                    startX += 16;
                }
                // 每行增加16像素的间距
                startY += 16;
            }
            // 创建一个Matrix对象进行旋转
            android.graphics.Matrix matrix = new android.graphics.Matrix();
            // 逆时针旋转90度
//            matrix.postRotate(-90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            // 保存Bitmap为图片文件
            File file = new File(Environment.getExternalStorageDirectory(), "image" + page + ".jpg");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                // 将Bitmap保存为JPEG格式到文件
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                // 将文件路径添加到集合中
                filePaths.add(file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        return filePaths;
    }
}