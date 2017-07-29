package freegrowsoftware.growtracker.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {
    public static boolean saveImage(String imagePath) {
        return saveBitmap(500, 500, imagePath, 80);
    }

    public static boolean saveThumbnail(String imagePath) {
        return saveBitmap(200, 200, imagePath, 20);
    }

    public static boolean saveBitmap(int width, int height, String imagePath, int quality) {
        Bitmap b = BitmapFactory.decodeFile(imagePath);
        b = b.createScaledBitmap(b, width, height, false);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(imagePath);
            b.compress(Bitmap.CompressFormat.PNG, quality, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
