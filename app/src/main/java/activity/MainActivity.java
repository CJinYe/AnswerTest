package activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.icox.exercises.R;
import com.icox.synfile.info.Ebagbook;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import adapter.MainPagerFragmentAdapter;
import bean.ExercisesBean;
import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import conf.Constants;
import db.DBConstants;
import db.DBUtil;
import dialog.SubmitDialog;
import dialog.UploadAnswerDialog;
import listener.BaseFragmentListener;
import utils.SpUtils;
import utils.XmlPullUtil;
import view.NoPreloadViewPager;
import view.SlideLeftRightButton;

import static conf.Constants.isResetAnswer;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    //public class MainActivity extends BaseAppActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity111";
    //    @InjectView(R.id.main_cm_time)
    TextView mMainCmTime;
    @InjectView(R.id.main_tv_pager_number)
    TextView mMainTvPagerNumber;
    @InjectView(R.id.main_viewpager)
    NoPreloadViewPager mMainViewPager;
    @InjectView(R.id.main_btn_up)
    ImageButton mBtnUp;
    @InjectView(R.id.main_btn_down)
    ImageButton mBtnDowm;
    @InjectView(R.id.main_tv_answer_card)
    ImageButton mMainTvAnswerCard;
    @InjectView(R.id.main_ll_btn)
    LinearLayout mMainLlBtn;
    @InjectView(R.id.main_tv_no_question)
    TextView mMainTvNoQuestion;
    @InjectView(R.id.main_tv_score)
    TextView mMainTvScore;
    @InjectView(R.id.main_fragment_ll)
    LinearLayout mMainFragmentLl;
    @InjectView(R.id.main_ib_draft)
    ImageButton mMainIbDraft;
    @InjectView(R.id.main_relativeLayout)
    RelativeLayout mMainRelativeLayout;
    @InjectView(R.id.main_tv_button_reset)
    ImageButton mMainTvButtonReset;
    @InjectView(R.id.main_tv_button_go_back)
    ImageButton mMainTvButtonGoBack;
    @InjectView(R.id.main_tv_button_exit)
    ImageButton mMainTvButtonExit;
    @InjectView(R.id.main_tv_button_add_error)
    ImageButton mMainTvButtonAddError;
    @InjectView(R.id.main_slide_button)
    SlideLeftRightButton mMainSlideButton;
    private DBUtil mDbUtil;
    private SpUtils mSpUtils;
    public static String mFilePath, mPathName;
    private List<ExercisesBean> mExercisesBeanList;
    public static Ebagbook mEbagbook;
    private InputStream mIsAnswer;
    private MainPagerFragmentAdapter mPagerFragmentAdapter;
    //作答的时间
    private long answerTime = 0;
    private InputMethodManager mImm;

    private int TIME_TYPE = 0;
    private int TIME_TYPE_PULUS_MINUS = 100;
    private int TIME_TYPE_START = 1;
    private int TIME_TYPE_STOP = -1;
    private int TIME_TYPE_PLUS = 100;
    private int TIME_TYPE_MINUS = -100;
    private SimpleDateFormat mFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mDbUtil = new DBUtil(this);
        mSpUtils = new SpUtils(this);
        mMainCmTime = (TextView) findViewById(R.id.main_cm_time);
        initIntentData();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int current = mMainViewPager.getCurrentItem();
        mMainViewPager.setCurrentItem(0);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mMainCmTime = (TextView) findViewById(R.id.main_cm_time);
        //检验答案
        if (mSpUtils.getBoolean(Constants.isExplain, false)) {
            mMainTvScore.setVisibility(View.VISIBLE);
            int scores = 0;
            int score = 0;
            for (int i = 0; i < mExercisesBeanList.size(); i++) {
                ExercisesBean ex = mExercisesBeanList.get(i);
                QuestionDB db = mDbUtil.queryAnswer(ex.id);
                String scoreStr = ex.score.replaceAll("分", "");
                if (db != null && db.userAnswer != null) {

                    if (db.userAnswer.equalsIgnoreCase(ex.answer)) {
                        score = score + Integer.parseInt(scoreStr);
                    }
                }
                scores = scores + Integer.parseInt(scoreStr);
            }
            mMainTvScore.setText("总分:" + scores + ",得分:" + score);
            mMainTvScore.setVisibility(View.GONE);
        }

        if (mExercisesBeanList != null && mExercisesBeanList.size() > 0) {
            mPagerFragmentAdapter = new MainPagerFragmentAdapter(getSupportFragmentManager(), mExercisesBeanList);
            mMainViewPager.setAdapter(mPagerFragmentAdapter);
            mMainViewPager.setCurrentItem(current);
            mMainTvPagerNumber.setText(mMainViewPager.getCurrentItem() + 1 + "/" + (mMainViewPager.getAdapter().getCount() - 1));
        }

        initListener();
        String hms = mFormatter.format(answerTime);
        mMainCmTime.setText(hms);
        super.onConfigurationChanged(newConfig);


    }

    Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (answerTime < 0 && TIME_TYPE != TIME_TYPE_STOP) {
                TIME_TYPE = TIME_TYPE_STOP;
                Toast.makeText(MainActivity.this, "答题时间已到,已自动交卷", Toast.LENGTH_LONG).show();
                mSpUtils.putBoolean(Constants.isExplain, true);
                mMainViewPager.setCurrentItem(0);
                initView();
                String hms = mFormatter.format(0);
                mMainCmTime.setText(hms);
                return;
            }

            if (TIME_TYPE_PULUS_MINUS == TIME_TYPE_MINUS) {
                if (TIME_TYPE != TIME_TYPE_STOP) {
                    answerTime -= 1000;
                }
            } else {
                if (TIME_TYPE != TIME_TYPE_STOP) {
                    answerTime += 1000;
                }
            }

            String hms = mFormatter.format(answerTime);
            mMainCmTime.setText(hms);

            if (mHandler != null)
                mHandler.postDelayed(this, 1000);

        }
    };

    /**
     * 查看是否有保存答案 有的话是否要导入
     */
    private void initAnswer() {
        List<QuestionDB> listAnswer = mDbUtil.inquireAllAnswer();
        if (listAnswer != null && listAnswer.size() > 0) {
            UploadAnswerDialog uploadAnswerDialog = new UploadAnswerDialog(this) {
                @Override
                public void sure() {
                    Constants.isUploadAnswer = true;
                }

                @Override
                public void clickCancel() {
                    deleteDbUpdateUI();
                }
            };
            uploadAnswerDialog.show();

            uploadAnswerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    TIME_TYPE = TIME_TYPE_START;
                }
            });
        } else {
            TIME_TYPE = TIME_TYPE_START;
        }

    }

    private void refreshView() {
        try {
            initData();
            initView();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MainActivity111", " IOException = " + e);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.e("MainActivity111", " XmlPullParserException = " + e);
            Toast.makeText(this, "题目异常无法解析！", Toast.LENGTH_SHORT).show();
        }
        initListener();
    }


    private void initIntentData() {
        String[] name = {
                "math/lianxi241.xml",
                "math/lianxi242.xml",
                "math/lianxi243.xml",
                "math/lianxi244.xml",
                "math/lianxi245.xml",
                "math/lianxi246.xml",
                "math/lianxi247.xml",
                "math/lianxi248.xml",
                "math/lianxi251.xml",
                "math/lianxi252.xml",
                "math/lianxi261.xml",
                "math/lianxi262.xml",
                "math/lianxi263.xml",
                "math/lianxi264.xml",
        };
        mFilePath = getIntent().getStringExtra("FilePath");
        mPathName = getIntent().getStringExtra("PathName");
        //                mFilePath = "/storage/emulated/0/icoxedu/[数学]初中沪科版新时代数学九年级下册(2014版).jc";
        //        mPathName =  name[(int) (Math.random()*name.length)];

        //        mFilePath = "/storage/emulated/0/icoxedu/2011年广东省初中毕业学业考试化学试题.jc";
        //                mFilePath = Environment.getExternalStorageDirectory() + "/icoxedu/2011年广东省初中毕业学业考试化学试题.jc";
        //                mPathName = "test.xml";

        if (mFilePath == null || mPathName == null) {
            Toast.makeText(this, "答题文件无法加载,请联系开发人员,抱歉！(文件为空)", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mEbagbook = Ebagbook.newInstance(mFilePath);
        mIsAnswer = mEbagbook.getInputStreamByName(mPathName);

        Log.i("examintaion", "mEbagbook   " + mEbagbook);
        Log.i("examintaion", "mIsAnswer   " + mIsAnswer);

        if (mEbagbook == null || mIsAnswer == null) {
            Toast.makeText(this, "答题文件无法加载,请联系开发人员,抱歉！(数据读取错误)", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        DBConstants.ExamintaionName = mEbagbook.getEtextbook().getName();
        DBConstants.UnitName = mPathName;

        refreshView();
        initAnswer();
        updateErrorUi();
    }

    private void initListener() {
        mBtnDowm.setOnClickListener(this);
        mBtnUp.setOnClickListener(this);
        mMainIbDraft.setOnClickListener(this);
        mMainTvAnswerCard.setOnClickListener(this);
        mMainTvButtonReset.setOnClickListener(this);
        mMainTvButtonGoBack.setOnClickListener(this);
        mMainTvButtonExit.setOnClickListener(this);
        mMainTvButtonAddError.setOnClickListener(this);
        mMainViewPager.setOnPageChangeListener(new NoPreloadViewPager.OnPageChangeListener() {
            public boolean flag;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        flag = false;
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:

                        if (isSHowKeyboard(MainActivity.this)) {
                            if (mImm != null) {
                                mImm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                                        0);
                            }
                        }

                        if (mMainViewPager.getCurrentItem() == mMainViewPager.getAdapter()
                                .getCount() - 2) {
                            //最后一题
                            mMainTvButtonAddError.setVisibility(View.VISIBLE);
                            mBtnUp.setVisibility(View.VISIBLE);
                            mMainTvPagerNumber.setVisibility(View.VISIBLE);
                            mBtnDowm.setVisibility(View.VISIBLE);
                            mBtnDowm.setImageResource(R.drawable.selector_main_button_down_card);
                            updateErrorUi();
                        } else if (mMainViewPager.getCurrentItem() == mMainViewPager.getAdapter()
                                .getCount() - 1) {
                            //答题卡页面
                            mBtnUp.setVisibility(View.GONE);
                            mMainTvButtonAddError.setVisibility(View.GONE);
                            mMainTvPagerNumber.setVisibility(View.GONE);
                            mBtnDowm.setVisibility(View.VISIBLE);
                            mBtnDowm.setImageResource(R.drawable.button_analysis);
                        } else {
                            mMainTvPagerNumber.setVisibility(View.VISIBLE);
                            mBtnUp.setVisibility(View.VISIBLE);
                            mMainTvButtonAddError.setVisibility(View.VISIBLE);
                            mBtnDowm.setVisibility(View.VISIBLE);
                            mBtnDowm.setImageResource(R.drawable.selector_main_button_down);
                            updateErrorUi();
                        }
                        mMainTvPagerNumber.setText(mMainViewPager.getCurrentItem() + 1 + "/" + (mMainViewPager.getAdapter().getCount() - 1));
                        flag = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (mMainViewPager.getCurrentItem() == 0) {
                            if (mSpUtils.getBoolean(Constants.isExplain, false)) {
                                TIME_TYPE = TIME_TYPE_STOP;
                            }

                        }
                        flag = true;
                        break;
                }
            }
        });

        //重置操作
        mMainSlideButton.setSlideListener(new SlideLeftRightButton.SlideListener() {
            @Override
            public void LeftSlide() {
                deleteDbUpdateUI();
                mMainSlideButton.setVisibility(View.GONE);
                mMainSlideButton.resetView();
                mBtnUp.setVisibility(View.VISIBLE);
                mBtnDowm.setVisibility(View.VISIBLE);
                mMainTvButtonAddError.setVisibility(View.VISIBLE);
                mBtnDowm.setImageResource(R.drawable.selector_main_button_down);
            }

            @Override
            public void RightSlide() {
                mMainSlideButton.setVisibility(View.GONE);
                mMainSlideButton.resetView();
            }
        });

    }

    private void initView() {
        //检验答案
        if (mSpUtils.getBoolean(Constants.isExplain, false)) {
            mMainTvScore.setVisibility(View.VISIBLE);
            int scores = 0;
            int score = 0;
            for (int i = 0; i < mExercisesBeanList.size(); i++) {
                ExercisesBean ex = mExercisesBeanList.get(i);
                QuestionDB db = mDbUtil.queryAnswer(ex.id);
                String scoreStr = ex.score.replaceAll("分", "");
                if (db != null && db.userAnswer != null) {
                    if (db.userAnswer.equalsIgnoreCase(ex.answer)) {
                        score = score + Integer.parseInt(scoreStr);
                    }
                }
                scores = scores + Integer.parseInt(scoreStr);
            }
            mMainTvScore.setText("总分:" + scores + ",得`分:" + score);
            mMainTvScore.setVisibility(View.GONE);
        }

        if (mExercisesBeanList == null && mExercisesBeanList.size() < 1) {
            mMainTvPagerNumber.setVisibility(View.GONE);
            mMainCmTime.setVisibility(View.GONE);
            mMainTvAnswerCard.setVisibility(View.GONE);
            mMainLlBtn.setVisibility(View.GONE);
            mMainTvNoQuestion.setVisibility(View.VISIBLE);
        } else {
            mMainTvPagerNumber.setVisibility(View.VISIBLE);
            mMainCmTime.setVisibility(View.VISIBLE);
            mMainTvAnswerCard.setVisibility(View.VISIBLE);
            mMainLlBtn.setVisibility(View.VISIBLE);
            mMainTvNoQuestion.setVisibility(View.GONE);
            mPagerFragmentAdapter = new MainPagerFragmentAdapter(getSupportFragmentManager(), mExercisesBeanList);
            mMainViewPager.setAdapter(mPagerFragmentAdapter);
            mMainTvPagerNumber.setText(mMainViewPager.getCurrentItem() + 1 + "/" + (mMainViewPager.getAdapter().getCount() - 1));
        }

    }

    private void initData() throws IOException, XmlPullParserException {

        mExercisesBeanList = XmlPullUtil.readXML(mIsAnswer, "UTF-8");

        //        InputStream isAnswer = getAssets().open("test.xml");
        //        mExercisesBeanList = XmlPullUtil.readTestXML(isAnswer, "UTF-8");
        //        DBConstants.ExamintaionName = mExercisesBeanList.get(0).name;
        //        DBConstants.UnitName = mPathName;

        for (int i = 0; i < mExercisesBeanList.size(); i++) {
            ExercisesBean Exercise = mExercisesBeanList.get(i);
            Exercise.examPath = mFilePath;
            if (Exercise.sound != null) {
                String soundPath = mEbagbook.getExtractFile(this, mPathName.substring(0, mPathName.lastIndexOf("/") + 1)
                        + Exercise.sound);
                Exercise.soundPath = soundPath;
            }

            if (Exercise.h5 != null) {
                Exercise.subject = Exercise.h5 + "\n" + Exercise.subject;
            }
            if (Exercise.h4 != null) {
                Exercise.subject = Exercise.h4 + "\n" + Exercise.subject;
            }
            if (Exercise.h3 != null) {
                Exercise.subject = Exercise.h3 + "\n" + Exercise.subject;
            }
            if (Exercise.h2 != null) {
                Exercise.subject = Exercise.h2 + "\n" + Exercise.subject;
            }
            if (Exercise.h1 != null) {
                Exercise.subject = Exercise.h1 + "\n" + Exercise.subject;
            }
            if (Exercise.alysis1 != null) {
                Exercise.analysis = Exercise.analysis + "\n" + Exercise.alysis1;
            }

            Exercise.subject = Exercise.subject.replaceAll("<font color=\"#0000ff\">", "") + " ";
            Exercise.subject = Exercise.subject.replaceAll("<@mark>", "") + " ";
            Log.i("subject", "" + Exercise.subject);
        }

        //这里想要只保留分秒可以写成"mm:ss"
        mFormatter = new SimpleDateFormat("H:mm:ss");
        mFormatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        mHandler.postDelayed(mRunnable, 1000);
        TIME_TYPE_PULUS_MINUS = answerTime > 0 ? TIME_TYPE_MINUS : TIME_TYPE_PLUS;

        //计时器
        if (answerTime != 0) {
            //获得已作答的时间
            long submitTime = mDbUtil.queryAnswerTime() / 1000;
            if (submitTime > 0) {
                answerTime = answerTime - (answerTime - submitTime);
            }
        } else {
            answerTime = mDbUtil.queryAnswerTime();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_up:
                mMainViewPager.setCurrentItem(mMainViewPager.getCurrentItem() - 1);
                mMainTvPagerNumber.setText(mMainViewPager.getCurrentItem() + 1 + "/" + (mMainViewPager.getAdapter().getCount() - 1));
                break;
            case R.id.main_btn_down:
                if (mMainViewPager.getCurrentItem() == mMainViewPager.getAdapter().getCount() - 1) {
                    //答题卡页面
                    //答题卡点击进入答题解析系统
                    int answer = mExercisesBeanList.size();
                    for (int i = 0; i < mExercisesBeanList.size(); i++) {
                        ExercisesBean ex = mExercisesBeanList.get(i);
                        QuestionDB db = mDbUtil.queryAnswer(ex.id);
                        if (db != null) {
                            if (db.userAnswer != null && !TextUtils.isEmpty(db.userAnswer)) {
                                //填空题.判断是否有答案
                                if (ex.type == 3 || ex.type == 31) {
                                    String[] strings = db.userAnswer.split("\\|\\|");
                                    if (strings.length > 0) {
                                        boolean isNullAnswer = true;
                                        for (int j = 0; j < strings.length; j++) {
                                            if (!strings[j].contains("图图图") && !TextUtils.isEmpty(strings[j].trim())) {
                                                isNullAnswer = false;
                                            }
                                        }
                                        if (!isNullAnswer) {
                                            answer = answer - 1;
                                        }
                                    }
                                } else {
                                    answer = answer - 1;
                                }
                            }
                        }

                    }

                    submitDialog(answer);

                } else {
                    updateErrorUi();
                }
                mMainViewPager.setCurrentItem(mMainViewPager.getCurrentItem() + 1);
                mMainTvPagerNumber.setText(mMainViewPager.getCurrentItem() + 1 + "/" + (mMainViewPager.getAdapter().getCount() - 1));
                break;
            case R.id.main_tv_answer_card:
                mMainViewPager.setCurrentItem(mMainViewPager.getAdapter().getCount() - 1);
                break;
            case R.id.main_ib_draft:
                Intent intent = new Intent(MainActivity.this, DraftDialogActivity.class);
                startActivity(intent);
                break;
            case R.id.main_tv_button_reset://重置
                //                resetDialog();
                mMainSlideButton.setVisibility(mMainSlideButton.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.main_tv_button_go_back://返回
                //                moveTaskToBack(true);
                Intent intent1 = new Intent(MainActivity.this, ErrorExamMainActivity.class);
                startActivity(intent1);
                break;
            case R.id.main_tv_button_exit://退出
                goBackDialog();
                //                finish();
                break;
            case R.id.main_tv_button_add_error://加入错题库

                int id = mMainViewPager.getCurrentItem();
                if (id != mMainViewPager.getAdapter().getCount() - 1 && mExercisesBeanList.size() > id) {
                    if (mBaseFragmentListener != null)
                        mBaseFragmentListener.baseFragmentListener();
                    id = id + 1;
                    if (mDbUtil.queryIsError(id)) {
                        mDbUtil.removeErrorExamination(id);
                        mMainTvButtonAddError.setImageResource(R.drawable.title_button_add_error_normal);
                    } else {
                        mDbUtil.addErrorExamination(id, mExercisesBeanList.get(id - 1));
                        mMainTvButtonAddError.setImageResource(R.drawable.title_button_add_error_pressed);
                    }
                }
                break;
        }
    }

    private void submitDialog(int answer) {
        SubmitDialog submitDialog = new SubmitDialog(this, answer) {
            @Override
            protected void setPagerCurrent() {
                mSpUtils.putBoolean(Constants.isExplain, true);
                mMainViewPager.setCurrentItem(0);
                initView();
            }
        };
        submitDialog.show();
    }

    private BaseFragmentListener mBaseFragmentListener;

    public void setBaseListener(BaseFragmentListener listener) {
        mBaseFragmentListener = listener;
    }

    @Override
    protected void onDestroy() {
        if (mSpUtils != null) {
            mSpUtils.putBoolean(Constants.isExplain, false);
        }

        if (mFilePath != null || mPathName != null) {
            if (mDbUtil.queryExaminationData()) {
                mDbUtil.updateAnswerTime(answerTime + "", mMainCmTime.getText().toString());
            } else {
                mDbUtil.insertExaminationData(answerTime + "", mMainCmTime.getText().toString());
            }
        }

        mHandler = null;
        mRunnable = null;

        super.onDestroy();
    }

    public NoPreloadViewPager getMainViewPager() {
        return mMainViewPager;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            goBackDialog();
            return false;
        } else {
            return false;
        }
    }

    private void goBackDialog() {
        UploadAnswerDialog dialog = new UploadAnswerDialog(this) {
            @Override
            public void sure() {
                finish();
            }

            @Override
            public void clickCancel() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDbUtil.resetData();
                    }
                }, 500);
                finish();
            }
        };
        dialog.show();
        dialog.setTitle("是否要保存你的答案？");
    }

    /**
     * 更新是否加入了错题库UI
     */
    private void updateErrorUi() {
        int id = mMainViewPager.getCurrentItem() + 1;
        if (mDbUtil.queryIsError(id)) {
            mMainTvButtonAddError.setImageResource(R.drawable.title_button_add_error_pressed);
        } else {
            mMainTvButtonAddError.setImageResource(R.drawable.title_button_add_error_normal);
        }
    }

    private void resetDialog() {
        UploadAnswerDialog dialog = new UploadAnswerDialog(this) {
            @Override
            public void sure() {
                deleteDbUpdateUI();
            }

            @Override
            public void clickCancel() {
            }
        };
        dialog.show();
        dialog.setTitle("确定要重置答卷？");
    }

    /**
     * 删除数据库,更新UI
     */
    private void deleteDbUpdateUI() {
        if (mSpUtils != null && mSpUtils.getBoolean(Constants.isExplain, false)) {
            mSpUtils.putBoolean(Constants.isExplain, false);
            TIME_TYPE = TIME_TYPE_START;
            //            mHandler.postDelayed(mRunnable, 1000);
        }
        mDbUtil.resetData();
        isResetAnswer = true;
        if (mExercisesBeanList != null) {
            for (ExercisesBean bean : mExercisesBeanList) {
                bean.selectedAnswer = null;
            }
            mPagerFragmentAdapter = new MainPagerFragmentAdapter(getSupportFragmentManager(), mExercisesBeanList);
            mMainViewPager.setAdapter(mPagerFragmentAdapter);
        }
        isResetAnswer = false;
    }

    /**
     * 判断软键盘是否弹出
     */
    public boolean isSHowKeyboard(Context context) {
        View view = this.getWindow().peekDecorView();
        if (mImm != null && mImm.hideSoftInputFromWindow(view.getWindowToken(), 0)) {
            mImm.showSoftInput(view, 0);
            return true;
            //软键盘已弹出
        } else {
            return false;
            //软键盘未弹出
        }
    }


    public void initWindow() {
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
}
