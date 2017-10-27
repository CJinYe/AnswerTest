package fragment;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icox.exercises.R;

import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import utils.MyTagHandler;
import view.MtextView;

import static android.text.Html.fromHtml;
import static utils.ImageGetterInstanceUtil.getImageGetterEdtText;
import static utils.ImageGetterInstanceUtil.getImageGetterInstance;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 14:10
 * @des 选择题
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class ChoiceFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "ChoiceFragment";
    @InjectView(R.id.fragment_choice_mv_question)
    MtextView mChoiceMvQuestion;
    @InjectView(R.id.fragment_choice_mv_A_answer)
    TextView mChoiceMvAAnswer;
    @InjectView(R.id.fragment_choice_ll_A)
    LinearLayout mChoiceLlA;
    @InjectView(R.id.fragment_choice_mv_B_answer)
    TextView mChoiceMvBAnswer;
    @InjectView(R.id.fragment_choice_ll_B)
    LinearLayout mChoiceLlB;
    @InjectView(R.id.fragment_choice_mv_C_answer)
    TextView mChoiceMvCAnswer;
    @InjectView(R.id.fragment_choice_ll_C)
    LinearLayout mChoiceLlC;
    @InjectView(R.id.fragment_choice_mv_D_answer)
    TextView mChoiceMvDAnswer;
    @InjectView(R.id.fragment_choice_ll_D)
    LinearLayout mChoiceLlD;
    @InjectView(R.id.fragment_choice_mv_result)
    TextView mChoiceMvResult;
    @InjectView(R.id.fragment_choice_Tv_A)
    TextView mChoiceTvA;
    @InjectView(R.id.fragment_choice_Tv_A2)
    TextView mChoiceTvA2;
    @InjectView(R.id.fragment_choice_Tv_B)
    TextView mChoiceTvB;
    @InjectView(R.id.fragment_choice_Tv_B2)
    TextView mChoiceTvB2;
    @InjectView(R.id.fragment_choice_Tv_C)
    TextView mChoiceTvC;
    @InjectView(R.id.fragment_choice_Tv_C2)
    TextView mChoiceTvC2;
    @InjectView(R.id.fragment_choice_Tv_D)
    TextView mChoiceTvD;
    @InjectView(R.id.fragment_choice_Tv_D2)
    TextView mChoiceTvD2;
    @InjectView(R.id.fragment_choice_ll_big)
    RelativeLayout mChoiceLlBig;
    private View mView;
    private QuestionDB mQuestionDB;

    private SpannableStringBuilder mSubJectSb;
    private List<EditText> mEditTextList;

    @Override
    public View initBaseView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_answer_copy, container, false);
        ButterKnife.inject(this, mView);
        initView();
        if (mIsExplain) {
            initExplainationView();
        }
        initListener();

        return mView;
    }

    private void initExplainationView() {
        mChoiceMvResult.setVisibility(View.VISIBLE);
        QuestionDB questionDB = mDbUtil.queryAnswer(mAnswer.id);
        if (questionDB != null && questionDB.userAnswer != null) {
            mAnswer.selectedAnswer = questionDB.userAnswer;
            switch (questionDB.userAnswer) {
                case "A":
                    initExplainationBackgroundError(mChoiceLlA, mChoiceTvA, mChoiceTvA2);
                    break;
                case "B":
                    initExplainationBackgroundError(mChoiceLlB, mChoiceTvB, mChoiceTvB2);
                    break;
                case "C":
                    initExplainationBackgroundError(mChoiceLlC, mChoiceTvC, mChoiceTvC2);
                    break;
                case "D":
                    initExplainationBackgroundError(mChoiceLlD, mChoiceTvD, mChoiceTvD2);
                    break;
            }
        }

    }

    private void initListener() {
        mChoiceLlA.setOnClickListener(this);
        mChoiceLlC.setOnClickListener(this);
        mChoiceLlB.setOnClickListener(this);
        mChoiceLlD.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {
        mAnswer.subject = mAnswer.subject.replaceAll("．", ".");
        String[] subjects = mAnswer.subject.split("\n");

        for (int i = 0; i < subjects.length; i++) {
            String answer = subjects[i];
            if (i >= 1 && answer.contains("A.")) {
                mChoiceLlA.setVisibility(View.VISIBLE);
                String answerA = answer.substring(2, answer.length());
                mChoiceMvAAnswer.setText(fromHtml(answerA, getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));
            }

            if (i >= 2 && answer.contains("B.")) {
                mChoiceLlB.setVisibility(View.VISIBLE);
                String answerB = answer.substring(2, answer.length());
                mChoiceMvBAnswer.setText(fromHtml(answerB, getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));
            }

            if (i >= 2 && answer.contains("C.")) {
                mChoiceLlC.setVisibility(View.VISIBLE);
                String answerC = answer.substring(2, answer.length());
                mChoiceMvCAnswer.setText(fromHtml(answerC, getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));
            }

            if (i >= 2 && answer.contains("D.")) {
                mChoiceLlD.setVisibility(View.VISIBLE);
                String answerD = answer.substring(2, answer.length());
                mChoiceMvDAnswer.setText(fromHtml(answerD, getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));
            }

        }
        String subject = mAnswer.subject.substring(0, mAnswer.subject.indexOf("A."));
        mSubJectSb = new SpannableStringBuilder(subject.replaceAll("<[^>]+>", "") + "                 ");

        mEditTextList = new ArrayList<>();
        mChoiceMvQuestion.setTextSize(30);
        mChoiceMvQuestion.setTextColor(Color.BLACK);

        String subjectHtml = subject.replaceAll("<font color=\"#0000ff\">", "") + " ";
        Html.fromHtml(subjectHtml, getImageGetterEdtText(getActivity(), mChoiceMvQuestion, mAnswer.answer),
                new MTagHandler());

        mChoiceMvQuestion.setMText(mSubJectSb);
        mChoiceMvQuestion.setEdtText(mEditTextList);
        mChoiceMvQuestion.invalidate();

        mChoiceMvResult.setText(Html.fromHtml(mAnswer.analysis,
                getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));

        //点击图片放大
        mChoiceMvQuestion.setMovementMethod(LinkMovementMethod.getInstance());
        mChoiceMvAAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        mChoiceMvBAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        mChoiceMvCAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        mChoiceMvDAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        mChoiceMvResult.setMovementMethod(LinkMovementMethod.getInstance());

        mChoiceMvResult.setVisibility(View.GONE);

        mQuestionDB = mDbUtil.queryAnswer(mAnswer.id);
        if (mQuestionDB != null && mQuestionDB.userAnswer != null) {
            mAnswer.selectedAnswer = mQuestionDB.userAnswer;
            switch (mQuestionDB.userAnswer) {
                case "A":
                    updateBackgroundABCD(mChoiceLlA, mChoiceTvA, mChoiceTvA2);
                    break;
                case "B":
                    updateBackgroundABCD(mChoiceLlB, mChoiceTvB, mChoiceTvB2);
                    break;
                case "C":
                    updateBackgroundABCD(mChoiceLlC, mChoiceTvC, mChoiceTvC2);
                    break;
                case "D":
                    updateBackgroundABCD(mChoiceLlD, mChoiceTvD, mChoiceTvD2);
                    break;
            }
            setEdtText(mAnswer.selectedAnswer);
        }
    }

    class MTagHandler implements Html.TagHandler {

        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
            // 处理标签<img>
            if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
                // 获取长度
                int len = output.length();
                // 获取图片地址
                ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
                String imgURL = images[0].getSource();

                mSubJectSb.setSpan("(　", len - 2, len - 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                mSubJectSb.setSpan(images[0], len - 1, len - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                mSubJectSb.setSpan("　)", len, len, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                if (imgURL.contains("input_")) {
                    EditText editText = new EditText(getActivity());
                    int size = Integer.valueOf(imgURL.substring(imgURL.length() - 1, imgURL.length()));
                    mEditTextList.add(editText);
                    //                    editText.setWidth((int) (mChoiceMvQuestion.getTextSize() * 3*size));
                    editText.setTextColor(Color.BLACK);
                    //                    editText.setBackgroundResource(R.drawable.shape_answer_selector);
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(size)});
                    editText.setSingleLine();
                    editText.setTextSize(25);
                    editText.setFocusable(false);
                    editText.setPadding(5, 5, 5, 5);
                    editText.setGravity(Gravity.CENTER);
                    editText.setHeight((int) (mChoiceMvQuestion.getTextSize() * 1.5));
                    mChoiceLlBig.addView(editText);

                }

                // 使图片可点击并监听点击事件
                //                output.setSpan(new MyTagHandler.ClickableImage(mContext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_choice_ll_A:
                if (mIsExplain) {
                    return;
                } else {
                    updateBackgroundABCD(mChoiceLlA, mChoiceTvA, mChoiceTvA2);
                    mAnswer.selectedAnswer = "A";
                    setEdtText("A");
                    updataDB();
                }
                break;
            case R.id.fragment_choice_ll_B:
                if (mIsExplain) {
                    return;
                } else {
                    updateBackgroundABCD(mChoiceLlB, mChoiceTvB, mChoiceTvB2);
                    mAnswer.selectedAnswer = "B";
                    setEdtText("B");
                    updataDB();
                }
                break;
            case R.id.fragment_choice_ll_C:
                if (mIsExplain) {
                    return;
                } else {
                    updateBackgroundABCD(mChoiceLlC, mChoiceTvC, mChoiceTvC2);
                    mAnswer.selectedAnswer = "C";
                    setEdtText("C");
                    updataDB();
                }
                break;
            case R.id.fragment_choice_ll_D:
                if (mIsExplain) {
                    return;
                } else {
                    updateBackgroundABCD(mChoiceLlD, mChoiceTvD, mChoiceTvD2);
                    mAnswer.selectedAnswer = "D";
                    setEdtText("D");
                    updataDB();
                }
                break;
        }
    }

    /**
     * 设置EdtText文本,如果课件没有写input_标签,防止出错
     *
     * @param text
     */
    private void setEdtText(String text) {
        try {
            mEditTextList.get(0).setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updataDB() {
        QuestionDB questionDB = mDbUtil.queryAnswer(mAnswer.id);
        if (questionDB == null) {
            mDbUtil.insertAnswerData(mAnswer.id, mAnswer.selectedAnswer);
        } else {
            mDbUtil.updateAnswer(mAnswer.id, mAnswer.selectedAnswer);
        }
    }

    private void updateBackgroundABCD(LinearLayout choiceLlA, TextView choiceTvA, TextView choiceTvA2) {
        mChoiceLlA.setBackgroundResource(R.drawable.shape_answer_normal);
        mChoiceLlB.setBackgroundResource(R.drawable.shape_answer_normal);
        mChoiceLlC.setBackgroundResource(R.drawable.shape_answer_normal);
        mChoiceLlD.setBackgroundResource(R.drawable.shape_answer_normal);
        choiceLlA.setBackgroundResource(R.drawable.shape_answer_selector);
    }

    private void initExplainationBackgroundError(LinearLayout choiceLlA, TextView choiceTvA, TextView choiceTvA2) {
        mChoiceLlA.setBackgroundResource(R.drawable.shape_answer_normal);
        mChoiceLlB.setBackgroundResource(R.drawable.shape_answer_normal);
        mChoiceLlC.setBackgroundResource(R.drawable.shape_answer_normal);
        mChoiceLlD.setBackgroundResource(R.drawable.shape_answer_normal);

        choiceLlA.setBackgroundResource(R.drawable.shape_answer_selector);

        if (mQuestionDB.userAnswer.equals(mAnswer.answer)) {
            choiceLlA.setBackgroundResource(R.drawable.shape_answer_right);
            choiceTvA.setText("　√  ");
        } else {
            choiceLlA.setBackgroundResource(R.drawable.shape_answer_explain_error);
            choiceTvA.setText("　X  ");
        }
    }


    private void initExplainationBackground(LinearLayout choiceLlA, TextView choiceTvA, TextView choiceTvA2) {
        ColorStateList ChoiceABCDSelector = getResources().getColorStateList(R.color.ChoiceABCDSelector);
        ColorStateList BackgroundABCDNormal = getResources().getColorStateList(R.color.BackgroundABCDNormal);
        choiceLlA.setBackgroundResource(R.drawable.shape_answer_selector);
        choiceTvA.setBackgroundResource(R.color.BackgroundABCDSelector);
        choiceTvA.setTextColor(ChoiceABCDSelector);
        choiceTvA2.setTextColor(BackgroundABCDNormal);
        choiceTvA2.setBackgroundResource(R.color.BackgroundABCDSelector);
        choiceTvA.setText("　√  ");
    }

}
