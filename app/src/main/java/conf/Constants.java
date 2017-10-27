package conf;

import android.graphics.Color;
import android.util.Log;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-25 17:24
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class Constants {
    public static String isExplain = "Explain";
    public static String isErrorExplain = "ErrorExplain";
    /**
     * 是否导入已经作答的答案
     */
    public static boolean isUploadAnswer = false;
    /**
     * 是否导入已经作答的答案
     */
    public static boolean isResetAnswer = false;
    public static String PadNoteAnswerPathTag = "path=";
    public static int NoteViewBackground = Color.parseColor("#00000000");

    public static void log(String text, int type) {
        switch (type) {
            case -1:
                Log.e("mytest", "" + text);
                break;
            case 0:
                Log.d("mytest", "" + text);
                break;
            case 1:
                Log.i("mytest", "" + text);
                break;

            default:
                break;
        }
    }
}
