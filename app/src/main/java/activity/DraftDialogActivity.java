package activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.icox.exercises.R;

import conf.BitmapConf;
import conf.Constants;
import view.NoteView.NoteView;
import view.NoteView.TuyaView;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-4-26 14:29
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class DraftDialogActivity extends Activity implements View.OnClickListener {
    private TuyaView mTuyaView;
    private NoteView mNoteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.dialog_draft);
        initView();
    }

    private void initView() {
        ImageButton ibClear = (ImageButton) findViewById(R.id.dialog_draft_clear);
        ImageButton ibRepeal = (ImageButton) findViewById(R.id.dialog_draft_repeal);
        ImageButton ibExit = (ImageButton) findViewById(R.id.dialog_draft_exit);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.dialog_draft_frame);
//        mTuyaView = (TuyaView) findViewById(R.id.dialog_draft_tuya);
//        mTuyaView.setBackgroundColor(Color.parseColor("#50000000"));
//        mTuyaView.selectPaintColor(Color.BLUE);
//        mTuyaView.setVisibility(View.GONE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        mNoteView = new NoteView(this,
                BitmapConf.createNullBitmap(width,height),
                BitmapConf.createSaveBitmap(width,height));
        frameLayout.addView(mNoteView);
        mNoteView.setBackgroundColor(Constants.NoteViewBackground);
        mNoteView.backgroundColor = Constants.NoteViewBackground;
        mNoteView.selectPaintColor(Color.WHITE);
        ibClear.setOnClickListener(this);
        ibRepeal.setOnClickListener(this);
        ibExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_draft_clear:
                mNoteView.redo();
                break;
            case R.id.dialog_draft_repeal:
                mNoteView.undo();
                break;
            case R.id.dialog_draft_exit:
                finish();
                break;

            default:
                break;
        }
    }
}
