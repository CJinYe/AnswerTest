package fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.Log;
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
import java.util.Locale;

import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import utils.MyTagHandler;
import view.MtextView;

import static utils.ImageGetterInstanceUtil.getImageGetterEdtText;
import static utils.ImageGetterInstanceUtil.getImageGetterInstance;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 14:10
 * @des ${多选题}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class MoreChoiceFragment extends BaseFragment implements View.OnClickListener {

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
    RelativeLayout mFragmentChoiceLlBig;
    private View mView;
    private QuestionDB mQuestionDB;
    private String TAG = "MoreChoiceFragment";
    private SpannableStringBuilder mSubJectSb;
    private ArrayList<EditText> mEditTextList;

    @Override
    public View initBaseView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_answer_copy, container, false);
        ButterKnife.inject(this, mView);
        initView();
        if (mIsExplain) {
            initExplainView();
        }
        initListener();
        return mView;
    }

    private void initExplainView() {
        mChoiceMvResult.setVisibility(View.VISIBLE);
        if (mQuestionDB != null) {
            char[] answerChars = mAnswer.answer.toCharArray();
            for (char aChar : answerChars) {
                String str = String.valueOf(aChar);
                switch (str) {
                    case "A":
                        initExplainError(mChoiceTvA, mChoiceLlA, mChoiceTvA2, false);
                        break;
                    case "B":
                        initExplainError(mChoiceTvB, mChoiceLlB, mChoiceTvB2, false);
                        break;
                    case "C":
                        initExplainError(mChoiceTvC, mChoiceLlC, mChoiceTvC2, false);
                        break;
                    case "D":
                        initExplainError(mChoiceTvD, mChoiceLlD, mChoiceTvD2, false);
                        break;
                }
            }

            char[] chars = mQuestionDB.userAnswer.toCharArray();
            boolean error = false;
            for (char aChar : chars) {
                String str = String.valueOf(aChar);
                error = mAnswer.answer.contains(str);
                switch (str) {
                    case "A":
                        initExplainError(mChoiceTvA, mChoiceLlA, mChoiceTvA2, error);
                        break;
                    case "B":
                        initExplainError(mChoiceTvB, mChoiceLlB, mChoiceTvB2, error);
                        break;
                    case "C":
                        initExplainError(mChoiceTvC, mChoiceLlC, mChoiceTvC2, error);
                        break;
                    case "D":
                        initExplainError(mChoiceTvD, mChoiceLlD, mChoiceTvD2, error);
                        break;
                }
            }
        }

        //        char[] chars = mAnswer.selectedAnswer.toCharArray();
        //        for (int i = 0; i < chars.length; i++) {
        //            String str = String.valueOf(chars[i]);
        //            if ("A".equals(str)) {
        //                initExplain(mChoiceTvA, mChoiceLlA, mChoiceTvA2);
        //            } else if ("B".equals(str)) {
        //                initExplain(mChoiceTvB, mChoiceLlB, mChoiceTvB2);
        //            } else if ("C".equals(str)) {
        //                initExplain(mChoiceTvC, mChoiceLlC, mChoiceTvC2);
        //            } else if ("D".equals(str)) {
        //                initExplain(mChoiceTvD, mChoiceLlD, mChoiceTvD2);
        //            }
        //        }
    }

    private void initListener() {
        mChoiceLlA.setOnClickListener(this);
        mChoiceLlC.setOnClickListener(this);
        mChoiceLlB.setOnClickListener(this);
        mChoiceLlD.setOnClickListener(this);
    }

    private void initView() {
        mAnswer.subject = mAnswer.subject.replaceAll("．", ".");
        String[] subjects = mAnswer.subject.split("\n");

        for (int i = 0; i < subjects.length; i++) {
            String answer = subjects[i];
            if (i >= 2 && answer.contains("A.")) {
                mChoiceLlA.setVisibility(View.VISIBLE);
                String answerA = answer.substring(2, answer.length());
                mChoiceMvAAnswer.setText(Html.fromHtml(answerA, getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));
            }

            if (i >= 2 && answer.contains("B.")) {
                mChoiceLlB.setVisibility(View.VISIBLE);
                String answerB = answer.substring(2, answer.length());
                mChoiceMvBAnswer.setText(Html.fromHtml(answerB, getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));
            }

            if (i >= 2 && answer.contains("C.")) {
                mChoiceLlC.setVisibility(View.VISIBLE);
                String answerC = answer.substring(2, answer.length());
                mChoiceMvCAnswer.setText(Html.fromHtml(answerC, getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));
            }

            if (i >= 2 && answer.contains("D.")) {
                mChoiceLlD.setVisibility(View.VISIBLE);
                String answerD = answer.substring(2, answer.length());
                mChoiceMvDAnswer.setText(Html.fromHtml(answerD, getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));
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

        //        mChoiceMvQuestion.setText(Html.fromHtml(subject.replaceAll("\n", "<br>"),
        //                getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));

        mChoiceMvResult.setText(Html.fromHtml(mAnswer.analysis.replaceAll("\n", "<br>"),
                getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));
        mChoiceMvQuestion.setMovementMethod(LinkMovementMethod.getInstance());
        mChoiceMvAAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        mChoiceMvBAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        mChoiceMvCAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        mChoiceMvDAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        mChoiceMvResult.setMovementMethod(LinkMovementMethod.getInstance());

        mChoiceMvResult.setVisibility(View.GONE);

        mQuestionDB = mDbUtil.queryAnswer(mAnswer.id);
        if (mQuestionDB != null) {
            char[] chars = mQuestionDB.userAnswer.toCharArray();
            for (char aChar : chars) {
                String str = String.valueOf(aChar);
                if ("A".equals(str)) {
                    updataSelector(mChoiceTvA, mChoiceLlA, mChoiceTvA2);
                } else if ("B".equals(str)) {
                    updataSelector(mChoiceTvB, mChoiceLlB, mChoiceTvB2);
                } else if ("C".equals(str)) {
                    updataSelector(mChoiceTvC, mChoiceLlC, mChoiceTvC2);
                } else if ("D".equals(str)) {
                    updataSelector(mChoiceTvD, mChoiceLlD, mChoiceTvD2);
                }
            }
            mAnswer.selectedAnswer = mQuestionDB.userAnswer;

            if (!mEditTextList.isEmpty()) {
                mEditTextList.get(0).setText(mAnswer.selectedAnswer);
            }
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
                    mFragmentChoiceLlBig.addView(editText);

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
                    if (mAnswer.selectedAnswer != null && mAnswer.selectedAnswer.contains("A")) {
                        //设置为不选中
                        mAnswer.selectedAnswer = mAnswer.selectedAnswer.replaceAll("A", "");
                        updataNormal(mChoiceTvA, mChoiceLlA, mChoiceTvA2);
                    } else {
                        if (mAnswer.selectedAnswer == null) {
                            mAnswer.selectedAnswer = "A";
                        } else {
                            mAnswer.selectedAnswer += "A";

                        }
                        updataSelector(mChoiceTvA, mChoiceLlA, mChoiceTvA2);
                    }
                    updataDB();
                }
                break;
            case R.id.fragment_choice_ll_B:
                if (mIsExplain) {
                    return;
                } else {
                    if (mAnswer.selectedAnswer != null && mAnswer.selectedAnswer.contains("B")) {
                        //设置为不选中
                        mAnswer.selectedAnswer = mAnswer.selectedAnswer.replaceAll("B", "");
                        updataNormal(mChoiceTvB, mChoiceLlB, mChoiceTvB2);
                    } else {
                        if (mAnswer.selectedAnswer == null) {
                            mAnswer.selectedAnswer = "B";
                        } else {
                            mAnswer.selectedAnswer += "B";
                        }
                        updataSelector(mChoiceTvB, mChoiceLlB, mChoiceTvB2);
                    }
                    updataDB();
                }
                break;
            case R.id.fragment_choice_ll_C:
                if (mIsExplain) {
                    return;
                } else {
                    if (mAnswer.selectedAnswer != null && mAnswer.selectedAnswer.contains("C")) {
                        //设置为不选中
                        mAnswer.selectedAnswer = mAnswer.selectedAnswer.replaceAll("C", "");
                        updataNormal(mChoiceTvC, mChoiceLlC, mChoiceTvC2);
                    } else {
                        if (mAnswer.selectedAnswer == null) {
                            mAnswer.selectedAnswer = "C";
                        } else {
                            mAnswer.selectedAnswer += "C";
                        }
                        updataSelector(mChoiceTvC, mChoiceLlC, mChoiceTvC2);
                    }
                    updataDB();
                }
                break;
            case R.id.fragment_choice_ll_D:
                if (mIsExplain) {
                    return;
                } else {
                    if (mAnswer.selectedAnswer != null && mAnswer.selectedAnswer.contains("D")) {
                        //设置为不选中
                        mAnswer.selectedAnswer = mAnswer.selectedAnswer.replaceAll("D", "");
                        updataNormal(mChoiceTvD, mChoiceLlD, mChoiceTvD2);
                    } else {
                        if (mAnswer.selectedAnswer == null) {
                            mAnswer.selectedAnswer = "D";
                        } else {
                            mAnswer.selectedAnswer += "D";
                        }
                        updataSelector(mChoiceTvD, mChoiceLlD, mChoiceTvD2);
                    }
                    updataDB();
                }
                break;
        }

        if (!mEditTextList.isEmpty()) {
            mEditTextList.get(0).setText(mAnswer.selectedAnswer);
        }
    }

    /**
     * 选中时的视图
     *
     * @param choiceTvA
     * @param choiceLlA
     * @param choiceTvA2
     */
    private void updataSelector(TextView choiceTvA, LinearLayout choiceLlA, TextView choiceTvA2) {
        ColorStateList ChoiceABCDSelector = getResources().getColorStateList(R.color.ChoiceABCDSelector);
        ColorStateList BackgroundABCDNormal = getResources().getColorStateList(R.color.BackgroundABCDNormal);
        choiceLlA.setBackgroundResource(R.drawable.shape_answer_selector);
        choiceTvA.setBackgroundResource(R.color.BackgroundABCDSelector);
        choiceTvA.setTextColor(ChoiceABCDSelector);
        choiceTvA2.setText("|");
        choiceTvA2.setTextColor(BackgroundABCDNormal);
        choiceTvA2.setBackgroundResource(R.color.BackgroundABCDSelector);
    }

    /**
     * 默认时的视图
     *
     * @param choiceTvA
     * @param choiceLlA
     * @param choiceTvA2
     */
    private void updataNormal(TextView choiceTvA, LinearLayout choiceLlA, TextView choiceTvA2) {
        ColorStateList ChoiceABCDNormal = getResources().getColorStateList(R.color.ChoiceABCDNormal);
        ColorStateList BackgroundNormal = getResources().getColorStateList(R.color.BackgroundNormal);
        choiceLlA.setBackgroundResource(R.drawable.shape_answer_normal);
        choiceTvA.setBackgroundResource(R.color.BackgroundABCDNormal);
        choiceTvA.setTextColor(ChoiceABCDNormal);
        choiceTvA2.setText("|");
        choiceTvA2.setTextColor(BackgroundNormal);
        choiceTvA2.setBackgroundResource(R.color.BackgroundABCDNormal);
    }


    private void updataDB() {
        QuestionDB questionDB = mDbUtil.queryAnswer(mAnswer.id);
        if (questionDB == null) {
            mDbUtil.insertAnswerData(mAnswer.id, mAnswer.selectedAnswer);
        } else {
            mDbUtil.updateAnswer(mAnswer.id, mAnswer.selectedAnswer);
        }
    }


    /**
     * 匹配用户答题跟答案
     *
     * @param choiceTvA
     * @param choiceLlA
     * @param choiceTvA2
     * @param a
     */
    private void initExplainError(TextView choiceTvA, LinearLayout choiceLlA, TextView choiceTvA2, boolean a) {
        ColorStateList ChoiceABCDSelector = getResources().getColorStateList(R.color.ChoiceABCDSelector);
        ColorStateList BackgroundABCDNormal = getResources().getColorStateList(R.color.BackgroundABCDNormal);


        //        if (mAnswer.answer.contains(a)) {
        //        if (mQuestionDB.userAnswer.equals(mAnswer.answer)) {
        if (a) {
            Log.d(TAG, mQuestionDB.userAnswer + "   " + mAnswer.answer);
            choiceLlA.setBackgroundResource(R.drawable.shape_answer_right);
            choiceTvA.setBackgroundResource(R.color.BackgroundABCDSelector);
            choiceTvA.setTextColor(ChoiceABCDSelector);
            choiceTvA2.setTextColor(BackgroundABCDNormal);
            choiceTvA2.setBackgroundResource(R.color.BackgroundABCDSelector);
            choiceTvA.setText("　√  ");
        } else {
            choiceLlA.setBackgroundResource(R.drawable.shape_answer_explain_error);
            choiceTvA.setBackgroundResource(R.color.BackgroundABCDSelectorError);
            choiceTvA.setTextColor(ChoiceABCDSelector);
            choiceTvA2.setTextColor(BackgroundABCDNormal);
            choiceTvA2.setBackgroundResource(R.color.BackgroundABCDSelectorError);
            choiceTvA.setText("　X  ");
        }

    }

    /**
     * 显示正确答案
     *
     * @param choiceTvA
     * @param choiceLlA
     * @param choiceTvA2
     */
    private void initExplain(TextView choiceTvA, LinearLayout choiceLlA, TextView choiceTvA2) {
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
