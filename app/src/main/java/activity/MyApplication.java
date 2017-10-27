package activity;

import android.content.Context;

import com.icox.synfile.SynFileApplication;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-3-8 11:16
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class MyApplication extends SynFileApplication {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
