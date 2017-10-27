package dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.icox.exercises.R;

import java.io.File;

import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import db.DBUtil;
import listener.WriteDialogListener;
import view.NoteView.TuyaView;

public class WritePadDialogCopy extends Dialog implements View.OnClickListener {
    @InjectView(R.id.dialog_write_tablet_view)
    FrameLayout mDialogWriteTabletView;
    @InjectView(R.id.dialog_write_but_ok)
    Button mDialogWriteButOk;
    @InjectView(R.id.dialog_write_but_clear)
    Button mDialogWriteButClear;
    @InjectView(R.id.dialog_write_but_repeal)
    Button mDialogWriteButRepeal;
    @InjectView(R.id.dialog_write_but_recover)
    Button mDialogWriteButRecover;
    @InjectView(R.id.dialog_write_but_cancel)
    Button mDialogWriteButCancel;
    @InjectView(R.id.dialog_write_btn_read)
    Button mDialogWriteBtnRead;
    @InjectView(R.id.dialog_write_btn_back)
    Button mDialogWriteBtnBack;
    private Context mContext;
    private WriteDialogListener mWriteDialogListener;
    private TuyaView mPaintView;
    private FrameLayout mFrameLayout;
    private Button mBtnOK, mBtnClear, mBtnCancel;
    private String mImgPath;
    private final DBUtil mDbUtil;
    private int idAnswer;

    public WritePadDialogCopy(Context context,
                              String imgPath, int idAnswer, WriteDialogListener writeDialogListener) {
        super(context);
        mDbUtil = new DBUtil(context);
        this.mImgPath = imgPath;
        this.mContext = context;
        this.idAnswer = idAnswer;
        this.mWriteDialogListener = writeDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //无标题
        setContentView(R.layout.dialog_write_answer);

        ButterKnife.inject(this, this);

        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int screenWidth = mDisplayMetrics.widthPixels;
        int screenHeight = mDisplayMetrics.heightPixels;
        if (TextUtils.isEmpty(mImgPath)) {
            // 获取屏幕尺寸
            mPaintView = new TuyaView(mContext, screenWidth, screenHeight);
        } else {
            // 获取屏幕尺寸
            Bitmap bitmapc = BitmapFactory.decodeFile(mImgPath);
            mPaintView = new TuyaView(mContext, bitmapc, screenWidth, screenHeight);
        }
        mDialogWriteTabletView.addView(mPaintView);
        mPaintView.requestFocus();

        mDialogWriteBtnBack.setOnClickListener(this);
        mDialogWriteBtnRead.setOnClickListener(this);
        mDialogWriteButOk.setOnClickListener(this);
        mDialogWriteButClear.setOnClickListener(this);
        mDialogWriteButRepeal.setOnClickListener(this);
        mDialogWriteButRecover.setOnClickListener(this);
        mDialogWriteButCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_write_but_ok://提交
                if (!TextUtils.isEmpty(mImgPath)) {
                    File file = new File(mImgPath);
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                }
                String path = mPaintView.saveToSDCard("","");
                mWriteDialogListener.onPaintDone(path);
                QuestionDB mQuestionDB = mDbUtil.queryAnswer(idAnswer);
                if (mQuestionDB != null) {
                    mDbUtil.updateAnswer(idAnswer, path);
                } else {
                    mDbUtil.insertAnswerData(idAnswer, path);
                }
                dismiss();
                break;
            case R.id.dialog_write_but_clear://清屏
                mPaintView.redo();
                break;
            case R.id.dialog_write_but_repeal://撤销
                mPaintView.undo();
                break;
            case R.id.dialog_write_but_recover://恢复
                mPaintView.recover();
                break;
            case R.id.dialog_write_but_cancel://取消
                cancel();
                break;
            case R.id.dialog_write_btn_read://红色
                mPaintView.selectPaintColor(0);
                break;
            case R.id.dialog_write_btn_back://黑色
                mPaintView.selectPaintColor(4);
                break;

        }
    }
}