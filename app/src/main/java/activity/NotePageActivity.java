package activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import conf.BitmapConf;
import conf.Constants;
import db.DBUtil;
import dialog.ColorPickerDialog;
import dialog.GoBackDialog;
import dialog.RedoDialog;
import dialog.SaveDialog;
import utils.DateTimeUtil;
import view.NoteView.NoteView;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-3-30 9:30
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class NotePageActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "NoteActvitit";
    @InjectView(R.id.note_write_tablet_view)
    FrameLayout mNoteViewPager;
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
    @InjectView(R.id.activity_main)
    RelativeLayout mActivityMain;
    @InjectView(R.id.note_write_iv_eraser)
    public ImageView mNoteWriteIvEraser;
    @InjectView(R.id.note_write_but_eraser)
    LinearLayout mNoteWriteButEraser;
    @InjectView(R.id.note_write_but_go_back)
    LinearLayout mNoteWriteButGoBack;
    @InjectView(R.id.note_write_edt_text)
    EditText mNoteWriteEdtText;
    @InjectView(R.id.note_write_but_show)
    ImageButton mNoteButtonShow;
    @InjectView(R.id.note_write_but_hide)
    LinearLayout mNoteWriteButHide;
    @InjectView(R.id.boom_button_menus)
    LinearLayout mBoomButtonMenus;

    private String mImgPath;
    private String mText;
    private String mBookLocation;


    private String mFilePath;
    private InputMethodManager mImm;
    private int idAnswer;
    private DBUtil mDbUtil;
    private NoteView mPaintView;
    private int mTag;
    private RelativeLayout.LayoutParams mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initWindow();
        setContentView(R.layout.activity_note);
        ButterKnife.inject(this);

        mImgPath = getIntent().getStringExtra("ImgPath");
        String filePath = getIntent().getStringExtra("FilePath");
        mFilePath = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf(".jc"));
        //        mFilePath = "测试测试";
        idAnswer = getIntent().getIntExtra("idAnswer", 0);
        mTag = getIntent().getIntExtra("tag", -1);
        mDbUtil = new DBUtil(this);
        initCanvas();

    }

    private void initWindow() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    private void initCanvas() {

        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);

        int screenWidth = mDisplayMetrics.widthPixels;
        int screenHeight = mDisplayMetrics.heightPixels;

        if (TextUtils.isEmpty(mImgPath)) {
            // 新建画布
            mPaintView = new NoteView(this, BitmapConf.createNullBitmap(screenWidth, screenHeight),
                    BitmapConf.createSaveBitmap(screenWidth, screenHeight));
            //            mPaintView.setBackgroundColor(Constants.NoteViewBackground);
            mPaintView.setBackgroundColor(Constants.NoteViewBackground);
            mPaintView.backgroundColor = Constants.NoteViewBackground;
            //            mNoteViewPager.setBackgroundResource(R.drawable.draft_background);
            // TODO: 2017-6-15 这里可以设置保存的背景颜色
        } else {
            // 在原有的图片
            Bitmap bitmapc = BitmapFactory.decodeFile(mImgPath);
            if (bitmapc == null) {
                // 新建画布
                mPaintView = new NoteView(this, BitmapConf.createNullBitmap(screenWidth, screenHeight),
                        BitmapConf.createSaveBitmap(screenWidth, screenHeight));
                mPaintView.setBackgroundColor(Constants.NoteViewBackground);
                mPaintView.backgroundColor = Constants.NoteViewBackground;
            } else {
                mPaintView = new NoteView(this,
                        BitmapConf.createNullBitmap(screenWidth, screenHeight),
                        BitmapConf.createSaveBitmap(screenWidth, screenHeight),
                        bitmapc);
            }
        }
        mNoteViewPager.addView(mPaintView);
        mPaintView.requestFocus();

        //                mNoteWriteButCancel.setOnClickListener(this);
        //                mNoteWriteButRecover.setOnClickListener(this);
        mNoteWriteButRepeal.setOnClickListener(this);
        mNoteWriteButClear.setOnClickListener(this);
        mNoteWriteButType.setOnClickListener(this);
        mNoteWriteButOk.setOnClickListener(this);
        mNoteWriteButSize.setOnClickListener(this);
        mNoteWriteButEraser.setOnClickListener(this);
        mNoteWriteButGoBack.setOnClickListener(this);
        mNoteButtonShow.setOnClickListener(this);
        mNoteWriteButHide.setOnClickListener(this);

        ViewGroup.MarginLayoutParams marginLayoutParams =
                new ViewGroup.MarginLayoutParams(mNoteWriteIvEraser.getLayoutParams());
        mParams = new RelativeLayout.LayoutParams(marginLayoutParams);
        setEraserListener();
    }

    private void setEraserListener() {

        mPaintView.setOnTouchEraserListener(new NoteView.onTouchEraser() {
            @Override
            public void onTouchEraserListener(float x, float y) {
                int x1 = (int) x;
                int y1 = (int) y;
                int width = mNoteWriteIvEraser.getWidth() / 2;
                int height = (int) (mNoteWriteIvEraser.getHeight() / 1.8);
                mParams.setMargins(x1 - width, y1 - height, width - x1 - width,
                        height - y1 - height);
                mNoteWriteIvEraser.setLayoutParams(mParams);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.note_write_but_size://大小
                ColorPickerDialog dialog = new ColorPickerDialog(
                        NotePageActivity.this
                        , mPaintView);
                dialog.show();
                //
                //                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                //                    @Override
                //                    public void onDismiss(DialogInterface dialog) {
                //                        tuyaView.setBackgroundColor(tuyaView.backgroundColor);
                //                    }
                //                });
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
            case R.id.note_write_but_eraser://橡皮擦
                clickEraser(mPaintView, mNoteWriteIvEraser);
                break;
            case R.id.note_write_but_go_back://退出
                goBack();
                break;
            case R.id.note_write_but_show://显示
                mNoteButtonShow.setVisibility(View.GONE);
                mBoomButtonMenus.setVisibility(View.VISIBLE);
                break;
            case R.id.note_write_but_hide://隐藏
                mNoteButtonShow.setVisibility(View.VISIBLE);
                mBoomButtonMenus.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * 点击退出
     */
    public void goBack() {
        GoBackDialog goBackDialog = new GoBackDialog(this) {
            @Override
            public void sure() {
                finish();
            }
        };
        goBackDialog.show();
    }

    /**
     * 点击橡皮擦
     *
     * @param tuyaViewPage 涂鸦View
     * @param view         橡皮擦
     */
    public void clickEraser(NoteView tuyaViewPage, ImageView view) {
        tuyaViewPage.selectEraser();
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
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
                initWindow();
            }
        });
    }

    private void clickSave() {
        final SaveDialog saveDialog = new SaveDialog(this) {
            @Override
            public void sure() {
                if (!TextUtils.isEmpty(mImgPath)) {
                    File file = new File(mImgPath);
                    if (file.isFile() && file.exists()) {
                        file.delete();
                    }
                }
                String time = DateTimeUtil.getCurrentTime();
                String path = null;
                try {
                    path = mPaintView.saveToSDCard(time, mFilePath + "第" + idAnswer + "题");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(NotePageActivity.this, "请检查你的存储设备 : " + e, Toast.LENGTH_LONG).show();
                    return;
                }

                QuestionDB mQuestionDB = mDbUtil.queryAnswer(idAnswer);

                EvenBusAnswerBean eventBusAnswerBean = new EvenBusAnswerBean();
                if (mTag != -1) {
                    path = Constants.PadNoteAnswerPathTag + path;
                    eventBusAnswerBean.tag = mTag;
                } else {
                    if (mQuestionDB != null) {
                        mDbUtil.updateAnswer(idAnswer, path);
                    } else {
                        mDbUtil.insertAnswerData(idAnswer, path);
                    }
                }

                eventBusAnswerBean.path = path;
                EventBus.getDefault().post(eventBusAnswerBean);

                finish();
            }
        };


        saveDialog.show();
        saveDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                initWindow();
            }
        });

    }


    long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 1500)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                //                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
