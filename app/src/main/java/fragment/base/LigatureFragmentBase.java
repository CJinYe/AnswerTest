package fragment.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icox.exercises.R;

import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import fragment.BaseFragment;
import utils.MyTagHandler;
import view.LigatureView;

import static utils.ImageGetterInstanceUtil.getImageGetterInstance;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 15:28
 * @des ${连线题}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class LigatureFragmentBase extends BaseFragment implements View.OnClickListener {

    @InjectView(R.id.fragment_ligature_ll_left)
    LinearLayout mFragmentLigatureLlLeft;
    @InjectView(R.id.fragment_ligature_ligature)
    LigatureView mFragmentLigatureLigature;
    @InjectView(R.id.fragment_ligature_ll_right)
    LinearLayout mFragmentLigatureLlRight;
    @InjectView(R.id.fragment_ligature_mv_result)
    TextView mFragmentLigatureMvResult;
    @InjectView(R.id.fragment_ligature_tv_title)
    TextView mFragmentLigatureTvTitle;
    @InjectView(R.id.fragment_ligature_answer_ll_left)
    LinearLayout mAnswerLlLeft;
    @InjectView(R.id.fragment_ligature_answer_ligature)
    LigatureView mAnswerLigature;
    @InjectView(R.id.fragment_ligature_answer_ll_right)
    LinearLayout mAnswerLlRight;
    @InjectView(R.id.fragment_ligature_answer_main)
    LinearLayout mAnswerMain;
    private View mView;
    private String TAG = "LigatureFragment";
    public QuestionDB mQuestionDB;
    int[] arr = {-1, -1};
    int[] arrAnswer = {-1, -1};
    private TextView beforTextview;
    public String lineSelector;

    @Override
    public View initBaseView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_ligature, container, false);
        ButterKnife.inject(this, mView);
        initView();
        initAnswer();
        initListener();
        return mView;
    }

    private void initAnswer() {

        if (mIsExplain)
            mAnswerMain.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mQuestionDB != null && mQuestionDB.userAnswer != null) {
                    Log.e("Ligature","userAnswer = "+mQuestionDB.userAnswer);
                    String[] answers = mQuestionDB.userAnswer.split(":");
                    for (String answer1 : answers) {
                        String[] answer = answer1.split("-");
                        for (String anAnswer : answer) {
                            setValues(Integer.parseInt(anAnswer));
                            setLine();
                        }
                    }
                }
                //                String[] modelAnswer = "0-7:2-1:4-3:6-5".split(":");
                if (mIsExplain) {
                    String[] modelAnswer = mAnswer.answer.split("\\|\\|");
                    for (String aModelAnswer : modelAnswer) {
                        String[] answer = aModelAnswer.split("-");
                        for (String anAnswer : answer) {
                            setAnswerValues(Integer.parseInt(anAnswer));
                            setAnswerLine();
                        }
                    }
                }
            }
        }, 300);


    }


    private void setViewLine(String[] answers) {
        for (String answer1 : answers) {
            String[] answer = answer1.split("-");
            for (String anAnswer : answer) {
                setValues(Integer.parseInt(anAnswer));
                setLine();
            }
        }
    }

    private void initListener() {
    }

    private void initView() {

        mQuestionDB = getAnswerData();

        String[] subjects = mAnswer.subject.split("\n");
        mFragmentLigatureTvTitle.setText(subjects[0]);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        String[] lefts = subjects[1].split(",");
        String[] rights = subjects[2].split(",");
        //设置两边问题的数量
        mFragmentLigatureLigature.getQuestionCount(lefts.length + rights.length);
        mAnswerLigature.getQuestionCount(lefts.length + rights.length);

        for (int i = 0; i < lefts.length; i++) {
            params.setMargins(10, 10, 5, 10);
            TextView tvLeft = createTextView(lefts[i], i + i);
            TextView tvAnswer = createTextView(lefts[i], i + i);
            //添加到布局
            mFragmentLigatureLlLeft.addView(tvLeft, params);
            mAnswerLlLeft.addView(tvAnswer, params);
            tvLeft.setOnClickListener(this);

            //            tvLeft.setMovementMethod(LinkMovementMethod.getInstance());
        }

        for (int i = 0; i < rights.length; i++) {
            params.setMargins(5, 10, 10, 10);
            TextView tvRight = createTextView(rights[i], i + i + 1);
            TextView tvAnswerRight = createTextView(rights[i], i + i + 1);
            //添加到布局
            mFragmentLigatureLlRight.addView(tvRight, params);
            mAnswerLlRight.addView(tvAnswerRight, params);

            tvRight.setOnClickListener(this);

            //            tvRight.setMovementMethod(LinkMovementMethod.getInstance());
        }


        mFragmentLigatureMvResult.setText(Html.fromHtml(mAnswer.analysis,
                getImageGetterInstance(getActivity()),
                new MyTagHandler(getActivity())));

        Spanned spannedResult = processSpanned(mAnswer.analysis);
        mFragmentLigatureMvResult.setText(spannedResult);

        if (mIsExplain) {
            mFragmentLigatureMvResult.setVisibility(View.VISIBLE);
        } else {
            mFragmentLigatureMvResult.setVisibility(View.GONE);
        }
    }

    private TextView createTextView(String text, int i) {
        TextView tvLeft = new TextView(getActivity());
        tvLeft.setGravity(Gravity.CENTER);
        tvLeft.setTextSize(30);
        tvLeft.setPadding(2, 2, 2, 2);
        tvLeft.setTextColor(Color.BLACK);
        tvLeft.setId(i);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            tvLeft.setElegantTextHeight(true);
        }
        //设置文本
        Spanned spanned = processSpanned(text);
        tvLeft.setText(spanned);
        return tvLeft;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void setLine() {
        if (arr[0] != -1 & arr[1] != -1) {
            if (mFragmentLigatureLigature != null) {
                mFragmentLigatureLigature.setLine(arr[0], arr[1]);
            }
            if (arr[0] % 2 == 0 && arr[1] % 2 == 0) {//同一侧
                // i为偶数
            } else if (arr[0] % 2 != 0 && arr[1] % 2 != 0) {//同一侧
                // i为奇数
            } else {
                if (TextUtils.isEmpty(lineSelector)) {
                    lineSelector = arr[0] + "-" + arr[1];
                } else if (!lineSelector.contains(arr[0] + "-" + arr[1]) && !lineSelector.contains(arr[1] + "-" + arr[0])) {
                    lineSelector = lineSelector + ":" + arr[0] + "-" + arr[1];
                }
                updateDb();
            }
            arr[0] = -1;
            arr[1] = -1;
        }
    }

    private void setAnswerLine() {
        if (arrAnswer[0] != -1 & arrAnswer[1] != -1) {
            if (mAnswerLigature != null) {
                mAnswerLigature.setLine(arrAnswer[0], arrAnswer[1]);
            }
            arrAnswer[0] = -1;
            arrAnswer[1] = -1;
        }

    }

    private void setTextBackGround(TextView tv) {
        if (arr[0] == -1 && arr[1] == -1) {
            //还原
            beforTextview.setBackgroundResource(R.drawable.btn_login);
            tv.setBackgroundResource(R.drawable.btn_login);
        } else {
            beforTextview = tv;
        }

    }

    private void setValues(int values) {

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == -1) {
                arr[i] = values;
                return;
            }
        }
    }

    private void setAnswerValues(int values) {

        for (int i = 0; i < arrAnswer.length; i++) {
            if (arrAnswer[i] == -1) {
                arrAnswer[i] = values;
                return;
            }
        }
    }

    public abstract void updateDb();

    public abstract QuestionDB getAnswerData();

    @Override
    public void onClick(View v) {
        if (!mIsExplain) {
            setValues(v.getId());
            setLine();
            v.setBackgroundResource(R.color.ChoiceABCDNormal);
            setTextBackGround((TextView) v);
        }
    }

}
