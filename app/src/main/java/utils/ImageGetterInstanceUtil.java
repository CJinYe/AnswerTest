package utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.icox.exercises.R;
import com.icox.synfile.info.Ebagbook;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import conf.Constants;
import db.DBConstants;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-4-26 10:13
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class ImageGetterInstanceUtil {
    /**
     * ImageGetter用于text图文混排
     *
     * @return
     */
    public static Html.ImageGetter getImageGetterInstance(final Context context) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager()
                .getDefaultDisplay().getMetrics(mDisplayMetrics);
        final int screenWidth = mDisplayMetrics.widthPixels; // 屏幕宽（像素，如：480px）
        final int screenHeight = mDisplayMetrics.heightPixels; // 屏幕高（像素，
        Html.ImageGetter imgGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable d = null;
                if (source.contains("input_")) {
                    d = context.getResources().getDrawable(R.drawable.edttext_bg);
                    String width = source.substring(source.lastIndexOf("_", source.length()));

                } else {
                    Ebagbook ebagbook = Ebagbook.newInstance(DBConstants.ExamPath);
                    source = ebagbook.getExtractFile(context, source);
                    d = Drawable.createFromPath(source);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    Bitmap bitmap = BitmapFactory.decodeFile(source, options); // 此时返回的bitmap为null

                    if (d != null) {
                        int width;
                        int height = options.outHeight;
                        if (options.outWidth > screenWidth) {
                            width = screenWidth - 100;
                        } else {
                            if (screenWidth > screenHeight) {
                                width = options.outWidth;
                                //                                float ratioWidth = screenWidth / 1280f;
                                float ratioWidth = screenWidth / 900f;
                                width = Math.round(width * ratioWidth);
                                float ratioHeight = (float) width / options.outWidth;
                                height = Math.round(height * ratioHeight);
                            } else {
                                width = options.outWidth;
                                //                                float ratioWidth = screenWidth / 800f;
                                float ratioWidth = screenWidth / 600f;
                                width = Math.round(width * ratioWidth);
                                float ratioHeight = (float) width / options.outWidth;
                                height = Math.round(height * ratioHeight);
                            }
                        }
                        if (width >= screenWidth) {
                            width = width - 50;
                        }

                        Constants.log("width = " + width, -1);
                        Constants.log("height = " + height, 1);
                        d.setBounds(0, 0, width, height);
                    }
                }

                return d;
            }
        };
        return imgGetter;
    }

    /**
     *  ImageGetter用于text图文混排
     * @param context
     * @param mtextView
     * @param answer    该题目的答案
     * @return
     */
    public static Html.ImageGetter getImageGetterEdtText(final Context context, final TextView mtextView, final String answer) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager()
                .getDefaultDisplay().getRealMetrics(mDisplayMetrics);
        final int screenWidth = mDisplayMetrics.widthPixels; // 屏幕宽（像素，如：480px）
        final int screenHeight = mDisplayMetrics.heightPixels; // 屏幕高

        /**
         * 返回图片的数据
         */
        Html.ImageGetter imgGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable d = null;
                if (source.contains("input_") || source.contains("note_")) {

                    //这里是打开一张透明的图片
                    d = context.getResources().getDrawable(R.drawable.edttext_bg);
                    //得到该占位图的长度,比如之前的数字4
                    int size = Integer.valueOf(source.substring(source.lastIndexOf("_") + 1, source.length()));
                    //这里的note我是自己定义的另一个标签,可忽略
                    if (source.contains("note_"))
                        size = 1;

                    int width;
                    String[] answers = answer.split("\\|\\|");

                    float ratio = (float) 5 / size;
                    if (ratio < 1)
                        size = (int) (size * ratio);

                    if (size > 15)
                        size = 15;

                    if (size == 1) {
                        width = (int) (mtextView.getTextSize() * size + mtextView.getTextSize() / 2);
                    } else {
                        width = (int) (mtextView.getTextSize() * size);
                        if (source.contains("input_")) {
                            int sizeWidth = Integer.valueOf(source.substring(source.lastIndexOf("_") + 1, source.length()));
                            if (answers.length > 0) {
                                int mun = Integer.valueOf(source.substring(source.lastIndexOf("input_"), source.lastIndexOf("_")).replaceAll("input_", ""));
                                if (!checkChina(answers[mun - 1])) {
                                    width = (int) (mtextView.getTextSize() / 1.5 * sizeWidth);
                                }
                            } else {
                                if (!checkChina(answer)) {
                                    width = (int) (mtextView.getTextSize() / 1.5 * sizeWidth);
                                }
                            }
                        }
                    }
                    //                    int height = (int) (mtextView.getTextSize() * 1.5);
                    int height = (int) (mtextView.getTextSize());

                    if (screenWidth > screenHeight) {
                        float ratioWidth = screenWidth / 1280f;
                        width = Math.round(width * ratioWidth);
                    } else {
                        float ratioWidth = screenWidth / 800f;
                        width = Math.round(width * ratioWidth);
                    }

                    //设置该占位图的宽高
                    d.setBounds(0, 0, width, height);

                } else if (source.contains("sup_") || source.contains("sub_")) {
                } else {
                    Ebagbook ebagbook = Ebagbook.newInstance(DBConstants.ExamPath);
                    source = ebagbook.getExtractFile(context, source);
                    d = Drawable.createFromPath(source);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    Bitmap bitmap = BitmapFactory.decodeFile(source, options); // 此时返回的bitmap为null

                    if (d != null) {
                        int width;
                        int height = options.outHeight;
                        if (options.outWidth > screenWidth) {
                            width = screenWidth - 100;
                        } else {
                            if (screenWidth > screenHeight) {
                                width = options.outWidth;
                                //                                float ratioWidth = screenWidth / 1280f;
                                float ratioWidth = screenWidth / 900f;
                                width = Math.round(width * ratioWidth);
                                float ratioHeight = (float) width / options.outWidth;
                                height = Math.round(height * ratioHeight);
                            } else {
                                width = options.outWidth;
                                //                                float ratioWidth = screenWidth / 800f;
                                float ratioWidth = screenWidth / 600f;
                                width = Math.round(width * ratioWidth);
                                float ratioHeight = (float) width / options.outWidth;
                                height = Math.round(height * ratioHeight);
                            }
                        }
                        if (width >= screenWidth) {
                            width = width - 50;
                        }
                        d.setBounds(0, 0, width, height);
                    }
                }

                return d;
            }
        };
        return imgGetter;
    }

    /**
     * ImageGetter用于text图文混排
     *
     * @return
     */
    public static Html.ImageGetter getImageGetterEdtText(final Context context, final TextView mtextView, final String answer, final int sizes) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager()
                .getDefaultDisplay().getMetrics(mDisplayMetrics);
        final int screenWidth = mDisplayMetrics.widthPixels; // 屏幕宽（像素，如：480px）
        final int screenHeight = mDisplayMetrics.heightPixels; // 屏幕高（像素，
        Html.ImageGetter imgGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable d = null;
                if (source.contains("input_") || source.contains("note_")) {

                    d = context.getResources().getDrawable(R.drawable.edttext_bg);
                    int size = sizes;
                    if (source.contains("note_"))
                        size = 1;

                    int width;
                    String[] answers = answer.split("\\|\\|");

                    float ratio = (float) 5 / size;
                    if (ratio < 1)
                        size = (int) (size * ratio);

                    if (size > 15)
                        size = 15;

                    if (size == 1) {
                        width = (int) (mtextView.getTextSize() * size + mtextView.getTextSize() / 2);
                    } else {
                        width = (int) (mtextView.getTextSize() * size);
                        //                        if (source.contains("input_")) {
                        //                            int sizeWidth = Integer.valueOf(source.substring(source.lastIndexOf("_") + 1, source.length()));
                        //                            if (answers.length > 0) {
                        //                                int mun = Integer.valueOf(source.substring(source.lastIndexOf("input_"), source.lastIndexOf("_")).replaceAll("input_", ""));
                        //                                if (!checkChina(answers[mun - 1])) {
                        //                                    width = (int) (mtextView.getTextSize() / 1.5 * sizeWidth);
                        //                                }
                        //                            } else {
                        //                                if (!checkChina(answer)) {
                        //                                    width = (int) (mtextView.getTextSize() / 1.5 * sizeWidth);
                        //                                }
                        //                            }
                        //                        }
                    }
                    //                    int height = (int) (mtextView.getTextSize() * 1.5);
                    int height = (int) (mtextView.getTextSize());
                    Log.e("width1", "width = " + screenWidth + "     he = " + screenHeight);
                    if (screenWidth > screenHeight) {
                        float ratioWidth = screenWidth / 1280f;
                        width = Math.round(width * ratioWidth);
                    } else {
                        float ratioWidth = screenWidth / 800f;
                        width = Math.round(width * ratioWidth);
                    }

                    d.setBounds(0, 0, width, height);

                } else if (source.contains("sup_") || source.contains("sub_")) {
                } else {
                    Ebagbook ebagbook = Ebagbook.newInstance(DBConstants.ExamPath);
                    source = ebagbook.getExtractFile(context, source);
                    d = Drawable.createFromPath(source);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    Bitmap bitmap = BitmapFactory.decodeFile(source, options); // 此时返回的bitmap为null

                    if (d != null) {
                        int width;
                        int height = options.outHeight;
                        if (options.outWidth > screenWidth) {
                            width = screenWidth - 100;
                        } else {
                            if (screenWidth > screenHeight) {
                                width = options.outWidth;
                                //                                float ratioWidth = screenWidth / 1280f;
                                float ratioWidth = screenWidth / 900f;
                                width = Math.round(width * ratioWidth);
                                float ratioHeight = (float) width / options.outWidth;
                                height = Math.round(height * ratioHeight);
                            } else {
                                width = options.outWidth;
                                //                                float ratioWidth = screenWidth / 800f;
                                float ratioWidth = screenWidth / 600f;
                                width = Math.round(width * ratioWidth);
                                float ratioHeight = (float) width / options.outWidth;
                                height = Math.round(height * ratioHeight);
                            }
                        }
                        if (width >= screenWidth) {
                            width = width - 50;
                        }
                        d.setBounds(0, 0, width, height);
                    }
                }

                return d;
            }
        };
        return imgGetter;
    }

    /**
     * 判断字符串是够含有汉字
     *
     * @param str
     * @return
     */
    private static boolean checkChina(String str) {
        String china = "[\u4e00-\u9fa5]";
        Pattern pattern = Pattern.compile(china);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

}
