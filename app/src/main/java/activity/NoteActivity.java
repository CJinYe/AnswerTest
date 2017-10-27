package activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.icox.exercises.R;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import bean.EvenBusAnswerBean;
import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import db.DBUtil;
import dialog.RedoDialog;
import dialog.SaveDialog;
import utils.DateTimeUtil;
import view.NoteView.NoteSurfaceView;
import view.NoteView.TuyaView;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-3-30 9:30
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class NoteActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.note_write_tablet_view)
    FrameLayout mNoteWriteTabletView;
//    @InjectView(R.id.note_sanilBar)
//    SnailBar mNoteSanilBar;
    @InjectView(R.id.note_write_but_ok)
    LinearLayout mNoteWriteButOk;
    @InjectView(R.id.note_write_but_clear)
    LinearLayout mNoteWriteButClear;
    @InjectView(R.id.note_write_but_repeal)
    LinearLayout mNoteWriteButRepeal;
    @InjectView(R.id.note_write_but_size)
    LinearLayout mNoteWriteButSize;
    @InjectView(R.id.note_write_but_type)
    LinearLayout mNoteWriteButType;
    @InjectView(R.id.boom_buttons_menus)
    LinearLayout mBoomButtonsMenus;
    @InjectView(R.id.note_write_edt_text)
    EditText mNoteWriteEdtText;
    @InjectView(R.id.activity_main)
    RelativeLayout mActivityMain;
    @InjectView(R.id.note_write_iv_eraser)
    public ImageView mNoteWriteIvEraser;
    private TuyaView mPaintView;
    private String mImgPath;
    private String mText;
    private NoteSurfaceView mNoteSurface;
    private boolean isType = false;
    private int[] colors = new int[]{
            Color.parseColor("#2b2b2b"),
            Color.parseColor("#ff5858"),
            Color.parseColor("#fefc38"),
            Color.parseColor("#58c6ff"),
            Color.parseColor("#76ff6f"),
            Color.parseColor("#ff7895"),
            Color.parseColor("#ff00f0"),
            Color.parseColor("#fc7e00"),

            Color.parseColor("#00ff7895"),
    };


    private String[] colorsName = new String[]{
            "黑色",
            "红色",
            "黄色",
            "蓝色",
            "绿色",
            "粉红色",
            "紫色",
            "橙色",
            "透明",
    };
    private DBUtil mDbUtil;
    private int idAnswer;
    private String mFilePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_note);
        ButterKnife.inject(this);
        mImgPath = getIntent().getStringExtra("ImgPath");
        String filePath = getIntent().getStringExtra("FilePath");
        mFilePath = filePath.substring(filePath.lastIndexOf("/")+1,filePath.lastIndexOf(".jc"));
        idAnswer = getIntent().getIntExtra("idAnswer", 0);
        mDbUtil = new DBUtil(this);
        initCanvas();



    }

    int lastX, lastY;
    int originX, originY;


    private void initCanvas() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int screenWidth = mDisplayMetrics.widthPixels;
        int screenHeight = mDisplayMetrics.heightPixels;
        //        GameViewSurface gameViewSurface = new GameViewSurface(this,screenWidth,screenHeight);
        if (TextUtils.isEmpty(mImgPath)) {
            // 获取屏幕尺寸
            mPaintView = new TuyaView(this, screenWidth, screenHeight);
            mNoteSurface = new NoteSurfaceView(this, screenWidth, screenHeight);
        } else {
            // 获取屏幕尺寸
            Bitmap bitmapc = BitmapFactory.decodeFile(mImgPath);
            //            mNoteSurface = new NoteSurfaceView(this, bitmapc, screenWidth, screenHeight);
            mPaintView = new TuyaView(this, bitmapc, screenWidth, screenHeight);
        }
        mNoteWriteTabletView.addView(mPaintView);
        mPaintView.requestFocus();

        if (mText != null && !TextUtils.isEmpty(mText)) {
            mNoteWriteEdtText.setVisibility(View.VISIBLE);
            mNoteWriteEdtText.setEnabled(false);
        }

        mNoteWriteButRepeal.setOnClickListener(this);
        mNoteWriteButClear.setOnClickListener(this);
        mNoteWriteButType.setOnClickListener(this);
        mNoteWriteButOk.setOnClickListener(this);
        mNoteWriteButSize.setOnClickListener(this);

        initSizeBar(screenWidth, screenHeight);
    }

    /**
     * 初始化画笔选择大小的选择器
     *
     * @param screenWidth
     * @param screenHeight
     */
    private void initSizeBar(int screenWidth, int screenHeight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) (screenWidth / 1.5),
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if (screenWidth <= 900) {
            params.weight = (float) (screenWidth / 1.1);
        } else if (screenWidth <= 2000) {
            params.weight = (float) (screenWidth / 1.6);
        } else if (screenWidth <= 3000) {
            params.weight = (float) (screenWidth / 2.2);
        } else if (screenWidth <= 4000) {
            params.weight = (float) (screenWidth / 3.5);
        } else {
            params.weight = (float) (screenWidth / 5);
        }

        params.weight = screenWidth / 2;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER_HORIZONTAL;
//        mNoteSanilBar.setLayoutParams(params);
//
//        mNoteSanilBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mPaintView.selectPaintSize(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //            case R.id.note_tapBarMenu://开关
            //                break;
            //                    case R.id.note_write_but_recover://恢复
            //                        mPaintView.recover();
            //                        break;
            //                    case R.id.note_write_but_cancel://橡皮擦
            //                        mPaintView.selectEraser();
            //                        break;

            case R.id.note_write_but_size://大小
//                mNoteSanilBar.setVisibility(
//                        mNoteSanilBar.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
//                );
                break;
            case R.id.note_write_but_ok://提交
                clickSave();
                break;
            case R.id.note_write_but_clear://清屏
                clickClear();
                break;
            case R.id.note_write_but_repeal://撤销
                mPaintView.undo();
                break;
            case R.id.note_write_but_type://文字
                isType = !isType;
                Toast.makeText(this, "" + isType, Toast.LENGTH_SHORT).show();
                if (isType) {
                    mNoteWriteEdtText.setVisibility(View.VISIBLE);
                    mNoteWriteEdtText.setEnabled(true);
                } else {
                    if (!TextUtils.isEmpty(mNoteWriteEdtText.getText().toString())) {
                        mNoteWriteEdtText.setVisibility(View.VISIBLE);
                        mNoteWriteEdtText.setEnabled(false);
                    } else {
                        mNoteWriteEdtText.setVisibility(View.GONE);
                    }
                }
                break;

            default:
                break;
        }
    }

    private void clickClear() {
        RedoDialog redoDialog = new RedoDialog(this) {
            @Override
            public void sure() {
                mPaintView.redo();
            }
        };
        redoDialog.show();
        redoDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //                initWindow();
            }
        });
    }

    private void clickSave() {
        SaveDialog saveDialog = new SaveDialog(this) {
            @Override
            public void sure() {
                if (!TextUtils.isEmpty(mImgPath)) {
                    File file = new File(mImgPath);
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                }
                String time = DateTimeUtil.getCurrentTime();
                String path = mPaintView.saveToSDCard(time,mFilePath+"第"+idAnswer+"题");

                QuestionDB mQuestionDB = mDbUtil.queryAnswer(idAnswer);
                if (mQuestionDB != null) {
                    mDbUtil.updateAnswer(idAnswer, path);
                } else {
                    mDbUtil.insertAnswerData(idAnswer, path);
                }

                EvenBusAnswerBean eventBusAnswerBean = new EvenBusAnswerBean();
                eventBusAnswerBean.path = path;
                EventBus.getDefault().post(eventBusAnswerBean);

                finish();
            }
        };
        saveDialog.show();
        saveDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //                initWindow();
            }
        });
    }

}
