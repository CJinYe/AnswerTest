package fragment.base;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icox.exercises.R;

import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.Locale;

import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import fragment.BaseFragment;
import view.MtextView;

import static utils.ImageGetterInstanceUtil.getImageGetterEdtText;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 14:10
 * @des ${多选题}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class MoreChoiceFragmentBase extends BaseFragment implements View.OnClickListener {

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
    @InjectView(R.id.fragment_choice_ll_big)
    RelativeLayout mFragmentChoiceLlBig;
    @InjectView(R.id.fragment_choice_rb_A)
    RadioButton mFragmentChoiceRbA;
    @InjectView(R.id.fragment_choice_rb_B)
    RadioButton mFragmentChoiceRbB;
    @InjectView(R.id.fragment_choice_rb_C)
    RadioButton mFragmentChoiceRbC;
    @InjectView(R.id.fragment_choice_rb_D)
    RadioButton mFragmentChoiceRbD;
    @InjectView(R.id.fragment_choice_but_sound)
    ImageButton mFragmentChoiceButSound;
    @InjectView(R.id.fragment_choice_rb_E)
    RadioButton mFragmentChoiceRbE;
    @InjectView(R.id.fragment_choice_mv_E_answer)
    TextView mChoiceMvEAnswer;
    @InjectView(R.id.fragment_choice_ll_E)
    LinearLayout mChoiceLlE;
    @InjectView(R.id.fragment_choice_rb_F)
    RadioButton mFragmentChoiceRbF;
    @InjectView(R.id.fragment_choice_mv_F_answer)
    TextView mChoiceMvFAnswer;
    @InjectView(R.id.fragment_choice_ll_F)
    LinearLayout mChoiceLlF;
    @InjectView(R.id.fragment_choice_rb_G)
    RadioButton mFragmentChoiceRbG;
    @InjectView(R.id.fragment_choice_mv_G_answer)
    TextView mChoiceMvGAnswer;
    @InjectView(R.id.fragment_choice_ll_G)
    LinearLayout mChoiceLlG;
    @InjectView(R.id.fragment_choice_rb_H)
    RadioButton mFragmentChoiceRbH;
    @InjectView(R.id.fragment_choice_mv_H_answer)
    TextView mChoiceMvHAnswer;
    @InjectView(R.id.fragment_choice_ll_H)
    LinearLayout mChoiceLlH;
    private View mView;
    public QuestionDB mQuestionDB;
    private String TAG = "MoreChoiceFragment";
    private SpannableString mSubJectSb;
    private ArrayList<EditText> mEditTextList;

    @Override
    public View initBaseView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_choice_more, container, false);
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
            //所有答案默认为选错
            //            char[] answerChars = mAnswer.answer.toCharArray();
            //            for (char aChar : answerChars) {
            //                String str = String.valueOf(aChar);
            //                switch (str) {
            //                    case "A":
            //                        initExplainError(mFragmentChoiceRbA,mChoiceMvAAnswer,false);
            //                        break;
            //                    case "B":
            //                        initExplainError(mFragmentChoiceRbB,mChoiceMvBAnswer,false);
            //                        break;
            //                    case "C":
            //                        initExplainError(mFragmentChoiceRbC,mChoiceMvCAnswer,false);
            //                        break;
            //                    case "D":
            //                        initExplainError(mFragmentChoiceRbD,mChoiceMvDAnswer,false);
            //                        break;
            //                }
            //            }

            char[] chars = mQuestionDB.userAnswer.toCharArray();
            boolean error = false;
            for (char aChar : chars) {
                String str = String.valueOf(aChar);
                error = mAnswer.answer.contains(str);
                switch (str) {
                    case "A":
                        initExplainError(mFragmentChoiceRbA, mChoiceMvAAnswer, error);
                        break;
                    case "B":
                        initExplainError(mFragmentChoiceRbB, mChoiceMvBAnswer, error);
                        break;
                    case "C":
                        initExplainError(mFragmentChoiceRbC, mChoiceMvCAnswer, error);
                        break;
                    case "D":
                        initExplainError(mFragmentChoiceRbD, mChoiceMvDAnswer, error);
                        break;
                    case "E":
                        initExplainError(mFragmentChoiceRbE, mChoiceMvEAnswer, error);
                        break;
                    case "F":
                        initExplainError(mFragmentChoiceRbF, mChoiceMvFAnswer, error);
                        break;
                    case "G":
                        initExplainError(mFragmentChoiceRbG, mChoiceMvGAnswer, error);
                        break;
                    case "H":
                        initExplainError(mFragmentChoiceRbH, mChoiceMvHAnswer, error);
                        break;
                }
            }
        }

    }

    private void initListener() {
        mChoiceLlA.setOnClickListener(this);
        mChoiceLlC.setOnClickListener(this);
        mChoiceLlB.setOnClickListener(this);
        mChoiceLlD.setOnClickListener(this);
        mChoiceLlE.setOnClickListener(this);
        mChoiceLlF.setOnClickListener(this);
        mChoiceLlG.setOnClickListener(this);
        mChoiceLlH.setOnClickListener(this);
        mFragmentChoiceRbA.setOnClickListener(this);
        mFragmentChoiceRbB.setOnClickListener(this);
        mFragmentChoiceRbC.setOnClickListener(this);
        mFragmentChoiceRbD.setOnClickListener(this);
        mFragmentChoiceRbE.setOnClickListener(this);
        mFragmentChoiceRbF.setOnClickListener(this);
        mFragmentChoiceRbG.setOnClickListener(this);
        mFragmentChoiceRbH.setOnClickListener(this);
    }

    private void initChoiceButton(LinearLayout layout, TextView tv, String answer) {
        layout.setVisibility(View.VISIBLE);
        Spanned spanned = processSpanned(answer);
        tv.setText(spanned);
    }

    private void initView() {

        if (mAnswer.soundPath == null) {
            mFragmentChoiceButSound.setVisibility(View.GONE);
        } else {
            mFragmentChoiceButSound.setVisibility(View.VISIBLE);
        }

        mAnswer.subject = mAnswer.subject.replaceAll("．", ".");
        String[] subjects = mAnswer.subject.split("\n");

        for (int i = 0; i < subjects.length; i++) {
            String answer = subjects[i];
            if (i >= 2 && answer.contains("A.")) {
                initChoiceButton(mChoiceLlA, mChoiceMvAAnswer, answer);
            }

            if (i >= 2 && answer.contains("B.")) {
                initChoiceButton(mChoiceLlB, mChoiceMvBAnswer, answer);
            }

            if (i >= 2 && answer.contains("C.")) {
                initChoiceButton(mChoiceLlC, mChoiceMvCAnswer, answer);
            }

            if (i >= 2 && answer.contains("D.")) {
                initChoiceButton(mChoiceLlD, mChoiceMvDAnswer, answer);
            }
            if (i >= 2 && answer.contains("E.")) {
                initChoiceButton(mChoiceLlE, mChoiceMvEAnswer, answer);
            }
            if (i >= 2 && answer.contains("F.")) {
                initChoiceButton(mChoiceLlF, mChoiceMvFAnswer, answer);
            }
            if (i >= 2 && answer.contains("G.")) {
                initChoiceButton(mChoiceLlG, mChoiceMvGAnswer, answer);
            }
            if (i >= 2 && answer.contains("H.")) {
                initChoiceButton(mChoiceLlH, mChoiceMvHAnswer, answer);
            }

        }
        String subject = mAnswer.subject;
        if (mAnswer.subject.contains("A.")) {
            subject = mAnswer.subject.substring(0, mAnswer.subject.indexOf("A."));
        }

        //把图片标签前后的空格去掉
        subject = subject.replaceAll("  <img src", "<img src");
        subject = subject.replaceAll(">  ", ">");
        subject = subject.replaceAll("</font>", "");
        subject = subject.replaceAll(" \n", "\n");
        subject = subject.replaceAll("\n ", "\n");
        subject = subject.replaceAll("\\( ", "(");
        subject = subject.replaceAll("<p>", "");
        subject = subject.replaceAll("<nbsp>", "");
        subject = subject.replaceAll("input_1_\\d", "input_1_4");

        //动态在图片标签前面加两个空格来作为图片插入时替换的无用字符
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < subject.length(); i++) {
            char cha = subject.charAt(i);
            String chaStr = String.valueOf(cha);
            if (chaStr.equals("<")) {
                sb.append("  ");
            }
            sb.append(subject.charAt(i));
        }

        //去掉多余的空格
        String toString = sb.toString().replaceAll("  </", "</");
        toString = toString.replaceAll("  <img src=\"sup_", "<img src=\"sup_");
        toString = toString.replaceAll("  <img src=\"sub_", "<img src=\"sub_");

        //把标签全部去除
        mSubJectSb = new SpannableString(toString.replaceAll("<[^>]+>", ""));

        mEditTextList = new ArrayList<>();
        //设置题目的大小颜色
        mChoiceMvQuestion.setTextSize(30);
        mChoiceMvQuestion.setTextColor(Color.BLACK);

        //用Html方法去适配得到标签作处理
        String htmlString = toString.replaceAll("<font color=\"#0000ff\">", "");
        htmlString = htmlString.replaceAll("\n", "-");
        Html.fromHtml(htmlString, getImageGetterEdtText(getActivity(), mChoiceMvQuestion, mAnswer.answer),
                new MTagHandler());

        mChoiceMvQuestion.setMText(mSubJectSb);
        mChoiceMvQuestion.setEdtText(mEditTextList);
        mChoiceMvQuestion.invalidate();

        //        mChoiceMvQuestion.setText(Html.fromHtml(subject.replaceAll("\n", "<br>"),
        //                getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));

        Spanned spannedResult = processSpanned(mAnswer.analysis);
        mChoiceMvResult.setText(spannedResult);

        //        mChoiceMvQuestion.setMovementMethod(LinkMovementMethod.getInstance());
        //        mChoiceMvAAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        //        mChoiceMvBAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        //        mChoiceMvCAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        //        mChoiceMvDAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        mChoiceMvResult.setMovementMethod(LinkMovementMethod.getInstance());

        mChoiceMvResult.setVisibility(View.GONE);

        mQuestionDB = getAnswerData();
        if (mQuestionDB != null) {
            char[] chars = mQuestionDB.userAnswer.toCharArray();
            for (char aChar : chars) {
                String str = String.valueOf(aChar);
                if ("A".equals(str)) {
                    mFragmentChoiceRbA.setChecked(true);
                    mFragmentChoiceRbA.setButtonDrawable(R.drawable.radio_button_checked);
                } else if ("B".equals(str)) {
                    mFragmentChoiceRbB.setChecked(true);
                    mFragmentChoiceRbB.setButtonDrawable(R.drawable.radio_button_checked);
                } else if ("C".equals(str)) {
                    mFragmentChoiceRbC.setChecked(true);
                    mFragmentChoiceRbC.setButtonDrawable(R.drawable.radio_button_checked);
                } else if ("D".equals(str)) {
                    mFragmentChoiceRbD.setChecked(true);
                    mFragmentChoiceRbD.setButtonDrawable(R.drawable.radio_button_checked);
                } else if ("E".equals(str)) {
                    mFragmentChoiceRbE.setChecked(true);
                    mFragmentChoiceRbE.setButtonDrawable(R.drawable.radio_button_checked);
                } else if ("F".equals(str)) {
                    mFragmentChoiceRbF.setChecked(true);
                    mFragmentChoiceRbF.setButtonDrawable(R.drawable.radio_button_checked);
                } else if ("G".equals(str)) {
                    mFragmentChoiceRbG.setChecked(true);
                    mFragmentChoiceRbG.setButtonDrawable(R.drawable.radio_button_checked);
                } else if ("H".equals(str)) {
                    mFragmentChoiceRbH.setChecked(true);
                    mFragmentChoiceRbH.setButtonDrawable(R.drawable.radio_button_checked);
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

                if (imgURL.contains("sup_")) {//上标
                    int start = Integer.valueOf(imgURL.substring(imgURL.indexOf("_") + 1, imgURL.lastIndexOf("_")));
                    int last = Integer.valueOf(imgURL.substring(imgURL.lastIndexOf("_") + 1, imgURL.length()));
                    SuperscriptSpan span = new SuperscriptSpan();
                    mSubJectSb.setSpan(span, start, last, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    return;
                }

                if (imgURL.contains("sub_")) {//下标
                    int start = Integer.valueOf(imgURL.substring(imgURL.indexOf("_") + 1, imgURL.lastIndexOf("_")));
                    int last = Integer.valueOf(imgURL.substring(imgURL.lastIndexOf("_") + 1, imgURL.length()));
                    SubscriptSpan span = new SubscriptSpan();
                    mSubJectSb.setSpan(span, start, last, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    return;
                }

                mSubJectSb.setSpan(images[0], len - 2, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (imgURL.contains("input_")) {
                    EditText editText = new EditText(getActivity());
                    int size = Integer.valueOf(imgURL.substring(imgURL.length() - 1, imgURL.length()));
                    mEditTextList.add(editText);
                    //                    editText.setWidth((int) (mChoiceMvQuestion.getTextSize() * 3*size));
                    editText.setTextColor(Color.BLACK);
                    //                    editText.setBackgroundResource(R.drawable.shape_answer_selector);
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
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

    private void clickProcess(String text, RadioButton radioButton) {
        if (!mIsExplain) {
            if (mAnswer.selectedAnswer != null && mAnswer.selectedAnswer.contains(text)) {
                //设置为不选中
                mAnswer.selectedAnswer = mAnswer.selectedAnswer.replaceAll(text, "");
                radioButton.setButtonDrawable(R.drawable.radio_button_normal);
            } else {
                if (mAnswer.selectedAnswer == null) {
                    mAnswer.selectedAnswer = text;
                } else {
                    mAnswer.selectedAnswer += text;
                }
                radioButton.setButtonDrawable(R.drawable.radio_button_checked);
            }

            updateDb();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_choice_ll_A:
            case R.id.fragment_choice_rb_A:
                clickProcess("A", mFragmentChoiceRbA);
                break;
            case R.id.fragment_choice_ll_B:
            case R.id.fragment_choice_rb_B:
                clickProcess("B", mFragmentChoiceRbB);
                //                if (!mIsExplain) {
                //                    if (mAnswer.selectedAnswer != null && mAnswer.selectedAnswer.contains("B")) {
                //                        //设置为不选中
                //                        mAnswer.selectedAnswer = mAnswer.selectedAnswer.replaceAll("B", "");
                //                        mFragmentChoiceRbB.setButtonDrawable(R.drawable.radio_button_normal);
                //                    } else {
                //                        if (mAnswer.selectedAnswer == null) {
                //                            mAnswer.selectedAnswer = "B";
                //                        } else {
                //                            mAnswer.selectedAnswer += "B";
                //                        }
                //                        mFragmentChoiceRbB.setButtonDrawable(R.drawable.radio_button_checked);
                //                    }
                //                    updateDb();
                //                }
                break;
            case R.id.fragment_choice_ll_C:
            case R.id.fragment_choice_rb_C:
                clickProcess("C", mFragmentChoiceRbC);
                break;
            case R.id.fragment_choice_ll_D:
            case R.id.fragment_choice_rb_D:
                clickProcess("D", mFragmentChoiceRbD);
                break;
            case R.id.fragment_choice_ll_E:
            case R.id.fragment_choice_rb_E:
                clickProcess("E", mFragmentChoiceRbE);
                break;
            case R.id.fragment_choice_ll_F:
            case R.id.fragment_choice_rb_F:
                clickProcess("F", mFragmentChoiceRbF);
                break;
            case R.id.fragment_choice_ll_G:
            case R.id.fragment_choice_rb_G:
                clickProcess("G", mFragmentChoiceRbG);
                break;
            case R.id.fragment_choice_ll_H:
            case R.id.fragment_choice_rb_H:
                clickProcess("H", mFragmentChoiceRbH);
                break;
        }

        if (!mEditTextList.isEmpty()) {
            mEditTextList.get(0).setText(mAnswer.selectedAnswer);
        }
    }

    private void updateRadioButton(RadioButton radioButton, boolean isChecked) {
        if (isChecked) {
            radioButton.setButtonDrawable(R.drawable.radio_button_checked);
        } else {
            radioButton.setButtonDrawable(R.drawable.radio_button_normal);
        }
    }


    private void initExplainError(RadioButton radioButton, TextView textView, boolean error) {
        if (error) {
            textView.setTextColor(getResources().getColor(R.color.AnswerGreen));
            mChoiceMvResult.setTextColor(getResources().getColor(R.color.AnswerGreen));
            radioButton.setButtonDrawable(R.drawable.radio_button_green);
        } else {
            textView.setTextColor(getResources().getColor(R.color.AnswerRed));
            mChoiceMvResult.setTextColor(getResources().getColor(R.color.AnswerRed));
            radioButton.setButtonDrawable(R.drawable.radio_button_red);
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
            mChoiceMvResult.setTextColor(Color.RED);
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

    public abstract void updateDb();

    public abstract QuestionDB getAnswerData();

}
