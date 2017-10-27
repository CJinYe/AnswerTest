package dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.icox.exercises.R;

import view.NoteView.TuyaView;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-4-26 11:07
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class DraftDialog extends Dialog implements View.OnClickListener {

    private TuyaView mTuyaView;

    public DraftDialog(Context context) {
        super(context, R.style.DialogTransparent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyCompat();
        setContentView(R.layout.dialog_draft);
        initView();//初始化自定的布局/控件
    }

    private void initView() {
        ImageButton ibClear = (ImageButton) findViewById(R.id.dialog_draft_clear);
        ImageButton ibRepeal = (ImageButton) findViewById(R.id.dialog_draft_repeal);
//        mTuyaView = (TuyaView) findViewById(R.id.dialog_draft_tuya);
//        mTuyaView.setBackgroundColor(Color.parseColor("#50000000"));
//        mTuyaView.selectPaintColor(Color.BLUE);
        ibClear.setOnClickListener(this);
        ibRepeal.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_draft_clear:
                mTuyaView.redo();
                break;
            case R.id.dialog_draft_repeal:
                mTuyaView.undo();
                break;

            default:
                break;
        }
    }
}
