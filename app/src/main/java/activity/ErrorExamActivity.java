package activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import adapter.ErrorPagerFragmentAdapter;
import bean.ExercisesBean;
import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import conf.Constants;
import db.DBUtil;
import dialog.SubmitDialog;
import dialog.UploadAnswerDialog;
import listener.BaseFragmentListener;
import utils.SpUtils;
import view.NoPreloadViewPager;
import view.SlideLeftRightButton;

import static conf.Constants.isResetAnswer;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-25 11:27
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class ErrorExamActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity111";
    @InjectView(R.id.main_cm_time)
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
    private boolean isLastPager;
    private DBUtil mDbUtil;
    private SpUtils mSpUtils;
    public static String mFilePath, mPathName;
    private List<ExercisesBean> mExercisesBeanList;
    public static Ebagbook mEbagbook;
    private ErrorPagerFragmentAdapter mPagerFragmentAdapter;
    //作答的时间
    private long answerTime = 0;
    private InputMethodManager mImm;
    private int TIME_TYPE = 0;
    private int TIME_TYPE_PULUS_MINUS = 100;
    private int TIME_TYPE_START = 1;
    private int TIME_TYPE_STOP = -1;
    private SimpleDateFormat mFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_error_exam);
        ButterKnife.inject(this);
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mDbUtil = new DBUtil(this);
        mSpUtils = new SpUtils(this);

        refreshView();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int current = mMainViewPager.getCurrentItem();
        mMainViewPager.setCurrentItem(0);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        //检验答案
        if (mSpUtils.getBoolean(Constants.isErrorExplain, false)) {
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
            mPagerFragmentAdapter = new ErrorPagerFragmentAdapter(getSupportFragmentManager(), mExercisesBeanList);
            mMainViewPager.setAdapter(mPagerFragmentAdapter);
            mMainViewPager.setCurrentItem(current);
            mMainTvPagerNumber.setText(mMainViewPager.getCurrentItem() + 1 + "/" + (mMainViewPager.getAdapter().getCount() - 1));
        }

        initListener();
        String hms = mFormatter.format(answerTime);
        mMainCmTime.setText(hms);
    }

    Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (answerTime < 0 && TIME_TYPE != TIME_TYPE_STOP) {
                TIME_TYPE = TIME_TYPE_STOP;
                Toast.makeText(ErrorExamActivity.this, "答题时间已到,已自动交卷", Toast.LENGTH_LONG).show();
                mSpUtils.putBoolean(Constants.isExplain, true);
                mMainViewPager.setCurrentItem(0);
                initView();
                String hms = mFormatter.format(0);
                mMainCmTime.setText(hms);
                return;
            }

            answerTime += 1000;


            String hms = mFormatter.format(answerTime);
            mMainCmTime.setText(hms);

            if (mHandler != null)
                mHandler.postDelayed(this, 1000);

        }
    };

    private void refreshView() {
        mExercisesBeanList = (List<ExercisesBean>) getIntent().getSerializableExtra("ErrorList");
        initData();
        initView();
        initListener();
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

                        if (isSHowKeyboard(ErrorExamActivity.this)) {
                            if (mImm != null) {
                                mImm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                            }
                        }

                        if (mMainViewPager.getCurrentItem() == mMainViewPager.getAdapter()
                                .getCount() - 2) {
                            //最后一题
                            mBtnUp.setVisibility(View.VISIBLE);
                            mMainTvPagerNumber.setVisibility(View.VISIBLE);
                            mBtnDowm.setVisibility(View.VISIBLE);
                            mBtnDowm.setImageResource(R.drawable.selector_main_button_down_card);
                            isLastPager = true;
                        } else if (mMainViewPager.getCurrentItem() == mMainViewPager.getAdapter()
                                .getCount() - 1) {
                            //答题卡页面
                            mBtnUp.setVisibility(View.GONE);
                            mMainTvPagerNumber.setVisibility(View.GONE);
                            mBtnDowm.setVisibility(View.VISIBLE);
                            mBtnDowm.setImageResource(R.drawable.button_analysis);
                            isLastPager = true;
                        } else {
                            mMainTvPagerNumber.setVisibility(View.VISIBLE);
                            mBtnUp.setVisibility(View.VISIBLE);
                            mBtnDowm.setVisibility(View.VISIBLE);
                            mBtnDowm.setImageResource(R.drawable.selector_main_button_down);
                        }
                        mMainTvPagerNumber.setText(mMainViewPager.getCurrentItem() + 1 + "/" + (mMainViewPager.getAdapter().getCount() - 1));
                        flag = true;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (mMainViewPager.getCurrentItem() == 0) {
                            if (mSpUtils.getBoolean(Constants.isErrorExplain, false)) {
                                TIME_TYPE = TIME_TYPE_STOP;
                            }
                        }
                        flag = true;
                        break;
                }
            }
        });

        mMainSlideButton.setSlideListener(new SlideLeftRightButton.SlideListener() {
            @Override
            public void LeftSlide() {
                deleteDbUpdateUI();
                mMainSlideButton.setVisibility(View.GONE);
                mMainSlideButton.resetView();
                mBtnUp.setVisibility(View.VISIBLE);
                mBtnDowm.setVisibility(View.VISIBLE);
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
        if (mSpUtils.getBoolean(Constants.isErrorExplain, false)) {
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

        mMainTvPagerNumber.setVisibility(View.VISIBLE);
        mMainCmTime.setVisibility(View.VISIBLE);
        mMainTvAnswerCard.setVisibility(View.VISIBLE);
        mMainLlBtn.setVisibility(View.VISIBLE);
        mPagerFragmentAdapter = new ErrorPagerFragmentAdapter(getSupportFragmentManager(), mExercisesBeanList);
        mMainViewPager.setAdapter(mPagerFragmentAdapter);
        mMainTvPagerNumber.setText(mMainViewPager.getCurrentItem() + 1 + "/" + (mMainViewPager.getAdapter().getCount() - 1));

    }

    private void initData() {

        for (int i = 0; i < mExercisesBeanList.size(); i++) {
            ExercisesBean Exercise = mExercisesBeanList.get(i);
            if (Exercise.sound != null) {
                mEbagbook = Ebagbook.newInstance(Exercise.examPath);
                String soundPath = mEbagbook.getExtractFile(this, Exercise.unit.substring(0, Exercise.unit.lastIndexOf("/") + 1)
                        + Exercise.sound);
                Exercise.soundPath = soundPath;
            }
        }

        mFormatter = new SimpleDateFormat("H:mm:ss");
        mFormatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        mHandler.postDelayed(mRunnable, 1000);
        TIME_TYPE = TIME_TYPE_START;
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
                    //                    List<QuestionDB> listAnswer = mDbUtil.inquireAllAnswer();
                    int answer = mExercisesBeanList.size();
                    for (int i = 0; i < mExercisesBeanList.size(); i++) {
                        ExercisesBean ex = mExercisesBeanList.get(i);
                        QuestionDB db = mDbUtil.queryErrorAnswer(ex.id);
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


                    SubmitDialog submitDialog = new SubmitDialog(this, answer) {
                        @Override
                        protected void setPagerCurrent() {
                            mSpUtils.putBoolean(Constants.isErrorExplain, true);
                            mMainViewPager.setCurrentItem(0);
                            initView();
                        }
                    };
                    submitDialog.show();
                }
                mMainViewPager.setCurrentItem(mMainViewPager.getCurrentItem() + 1);
                mMainTvPagerNumber.setText(mMainViewPager.getCurrentItem() + 1 + "/" + (mMainViewPager.getAdapter().getCount() - 1));
                break;
            case R.id.main_tv_answer_card:
                mMainViewPager.setCurrentItem(mMainViewPager.getAdapter().getCount() - 1);
                break;
            case R.id.main_ib_draft:
                Intent intent = new Intent(ErrorExamActivity.this, DraftDialogActivity.class);
                startActivity(intent);
                break;
            case R.id.main_tv_button_reset://重置
                //                resetDialog();
                mMainSlideButton.setVisibility(mMainSlideButton.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.main_tv_button_go_back://返回
                moveTaskToBack(true);
                break;
            case R.id.main_tv_button_exit://退出
                //                goBackDialog();
                finish();
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

    private BaseFragmentListener mBaseFragmentListener;

    public void setBaseListener(BaseFragmentListener listener) {
        mBaseFragmentListener = listener;
    }

    @Override
    protected void onDestroy() {
        if (mSpUtils != null) {
            mSpUtils.putBoolean(Constants.isErrorExplain, false);
        }

        super.onDestroy();
    }

    public NoPreloadViewPager getMainViewPager() {
        return mMainViewPager;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
        //            goBackDialog();
        //            return false;
        //        } else {
        //            return false;
        //        }
        mHandler = null;
        mRunnable = null;
        return super.onKeyDown(keyCode, event);
    }

    private void goBackDialog() {
        UploadAnswerDialog dialog = new UploadAnswerDialog(this) {
            @Override
            public void sure() {
                finish();
            }

            @Override
            public void clickCancel() {
                isLastPager = true;
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
        if (mSpUtils != null && mSpUtils.getBoolean(Constants.isErrorExplain, false)) {
            mSpUtils.putBoolean(Constants.isErrorExplain, false);
            TIME_TYPE = TIME_TYPE_START;
        }
//        mDbUtil.resetData();
        isResetAnswer = true;
        if (mExercisesBeanList != null) {
            for (ExercisesBean bean : mExercisesBeanList) {
                bean.selectedAnswer = null;
                mDbUtil.updateErrorAnswer(bean.id,null);
            }
            mPagerFragmentAdapter = new ErrorPagerFragmentAdapter(getSupportFragmentManager(), mExercisesBeanList);
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
