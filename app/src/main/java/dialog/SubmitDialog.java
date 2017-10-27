package dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.icox.exercises.R;

import utils.SpUtils;


/**
 * Created by icoxyf03 on 2016/12/19
 * 密码验证的验证框
 */

public abstract class SubmitDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private int mAnswer;
    private final SpUtils mSpUtils;

    public SubmitDialog(@NonNull Context context, int answer) {
        super(context, R.style.CustomDialog);
        mSpUtils = new SpUtils(context);
        this.mContext = context;
        this.mAnswer = answer;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyCompat();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.dialog_redo);
        initView();//初始化自定的布局/控件
    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.dialog_redo_tv_title);
        ImageButton cancel = (ImageButton) findViewById(R.id.dialog_redo_btn_cancel);
        ImageButton ensure = (ImageButton) findViewById(R.id.dialog_redo_btn_sure);

        cancel.setOnClickListener(this);
        ensure.setOnClickListener(this);

        if (mAnswer > 0) {
            title.setText("你还有 " + mAnswer + " 题没有作答！\n是否结束答题？");
        } else {
            title.setText("你已经全部作答！是否结束答题？");
        }
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_redo_btn_sure:
                setPagerCurrent();
                dismiss();
                break;
            case R.id.dialog_redo_btn_cancel:
                dismiss();
                break;
        }
    }

    protected abstract void setPagerCurrent();


}
