package conf;

import android.graphics.Bitmap;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-13 13:41
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class BitmapConf {
    public static Bitmap nullBitmap = null;
    public static Bitmap saveBitmap = null;
    public static Bitmap createNullBitmap(int width,int height){
        if (nullBitmap==null || nullBitmap.isRecycled()){
            nullBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_4444);
        }
        if (width != nullBitmap.getWidth()){
            nullBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_4444);
        }

        return nullBitmap;
    }
    public static Bitmap createSaveBitmap(int width,int height){
        if (saveBitmap==null || saveBitmap.isRecycled()){
            saveBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_4444);
        }
        if (width != saveBitmap.getWidth()){
            saveBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_4444);
        }
        return saveBitmap;
    }
}
