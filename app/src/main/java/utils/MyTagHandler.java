package utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;

import org.xml.sax.XMLReader;

import java.util.Locale;

import activity.MainActivity;
import dialog.PicDiaLog;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-4-26 10:09
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class MyTagHandler implements Html.TagHandler {

    private final Context mContext;

    public MyTagHandler(Context context) {
        mContext = context;
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

        // 处理标签<img>
        if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
            // 获取长度
            int len = output.length();
            // 获取图片地址
            ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
            String imgURL = images[0].getSource();
            // 使图片可点击并监听点击事件
            output.setSpan(new MyTagHandler.ClickableImage(mContext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private class ClickableImage extends ClickableSpan {

        private String url;
        private Context context;

        public ClickableImage(Context context, String url) {
            this.context = context;
            if (MainActivity.mEbagbook != null) {
                this.url = MainActivity.mEbagbook.getExtractFile(context, url);
            }
        }

        @Override
        public void onClick(View widget) {
            // 进行图片点击之后的处理
            Drawable d = Drawable.createFromPath(url);
            PicDiaLog picDiaLog = new PicDiaLog(context, d);
            picDiaLog.show();
        }
    }
}
