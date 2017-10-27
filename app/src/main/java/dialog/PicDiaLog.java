package dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.icox.exercises.R;


/**
 * Created by icoxyf03 on 2016/12/19
 * 密码验证的验证框
 */

public class PicDiaLog extends Dialog  {

    private Context mContext;
    private EditText mPwdEt;
    private String mPwd;
    private View mToastRoot;
    private TextView mToastText;
    private Drawable d;

    public PicDiaLog(@NonNull Context context, Drawable d) {
        super(context, R.style.DialogTransparent);
        mContext = context;
        this.d = d;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyCompat();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pwd);
        initView();//初始化自定的布局/控件
    }

    protected void applyCompat(){
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Window window = this.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }
    }


    private void initView() {
        ImageView mIv = (ImageView) findViewById(R.id.dialog_iv);
        mIv.setImageDrawable(d);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dismiss();
        return super.onTouchEvent(event);
    }
}
