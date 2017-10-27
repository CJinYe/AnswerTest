package fragment.base;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icox.exercises.R;

import org.xml.sax.XMLReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import fragment.BaseFragment;
import utils.MyTagHandler;
import utils.SpannedUtil;
import view.MtextView;

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
public abstract class ChoiceFragmentBase extends BaseFragment implements View.OnClickListener {

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
    @InjectView(R.id.fragment_choice_ll_big)
    RelativeLayout mChoiceLlBig;
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
    TextView mFragmentChoiceMvEAnswer;
    @InjectView(R.id.fragment_choice_ll_E)
    LinearLayout mFragmentChoiceLlE;
    @InjectView(R.id.fragment_choice_rb_F)
    RadioButton mFragmentChoiceRbF;
    @InjectView(R.id.fragment_choice_mv_F_answer)
    TextView mFragmentChoiceMvFAnswer;
    @InjectView(R.id.fragment_choice_ll_F)
    LinearLayout mFragmentChoiceLlF;
    @InjectView(R.id.fragment_choice_rb_G)
    RadioButton mFragmentChoiceRbG;
    @InjectView(R.id.fragment_choice_mv_G_answer)
    TextView mFragmentChoiceMvGAnswer;
    @InjectView(R.id.fragment_choice_ll_G)
    LinearLayout mFragmentChoiceLlG;
    @InjectView(R.id.fragment_choice_rb_H)
    RadioButton mFragmentChoiceRbH;
    @InjectView(R.id.fragment_choice_mv_H_answer)
    TextView mFragmentChoiceMvHAnswer;
    @InjectView(R.id.fragment_choice_ll_H)
    LinearLayout mFragmentChoiceLlH;
    private View mView;
    private QuestionDB mQuestionDB;

    private SpannableString mSubJectSb;
    private List<EditText> mEditTextList;
    private MediaPlayer mediaPlayer;

    @Override
    public View initBaseView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_choice_base, container, false);
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
        if (mQuestionDB != null && mQuestionDB.userAnswer != null) {
            mAnswer.selectedAnswer = mQuestionDB.userAnswer;
            switch (mQuestionDB.userAnswer) {
                case "A":
                    initExplain(mFragmentChoiceRbA, mChoiceMvAAnswer, mQuestionDB.userAnswer);
                    break;
                case "B":
                    initExplain(mFragmentChoiceRbB, mChoiceMvBAnswer, mQuestionDB.userAnswer);
                    break;
                case "C":
                    initExplain(mFragmentChoiceRbC, mChoiceMvCAnswer, mQuestionDB.userAnswer);
                    break;
                case "D":
                    initExplain(mFragmentChoiceRbD, mChoiceMvDAnswer, mQuestionDB.userAnswer);
                    break;
                case "E":
                    initExplain(mFragmentChoiceRbE, mFragmentChoiceMvEAnswer, mQuestionDB.userAnswer);
                    break;
                case "F":
                    initExplain(mFragmentChoiceRbF, mFragmentChoiceMvFAnswer, mQuestionDB.userAnswer);
                    break;
                case "G":
                    initExplain(mFragmentChoiceRbG, mFragmentChoiceMvGAnswer, mQuestionDB.userAnswer);
                    break;
                case "H":
                    initExplain(mFragmentChoiceRbH, mFragmentChoiceMvHAnswer, mQuestionDB.userAnswer);
                    break;
            }
        }

    }

    private void initListener() {
        mChoiceLlA.setOnClickListener(this);
        mChoiceLlC.setOnClickListener(this);
        mChoiceLlB.setOnClickListener(this);
        mChoiceLlD.setOnClickListener(this);
        mFragmentChoiceLlE.setOnClickListener(this);
        mFragmentChoiceLlF.setOnClickListener(this);
        mFragmentChoiceLlG.setOnClickListener(this);
        mFragmentChoiceLlH.setOnClickListener(this);

        mFragmentChoiceRbA.setOnClickListener(this);
        mFragmentChoiceRbB.setOnClickListener(this);
        mFragmentChoiceRbC.setOnClickListener(this);
        mFragmentChoiceRbD.setOnClickListener(this);
        mFragmentChoiceRbE.setOnClickListener(this);
        mFragmentChoiceRbF.setOnClickListener(this);
        mFragmentChoiceRbG.setOnClickListener(this);
        mFragmentChoiceRbH.setOnClickListener(this);

        mFragmentChoiceButSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer == null) {
                    mediaPlayer = createLocalMp3();
                }
                //在播放音频资源之前，必须调用Prepare方法完成些准备工作
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
                //开始播放音频
            }
        });
    }

    /**
     * 创建本地MP3
     *
     * @return
     */
    public MediaPlayer createLocalMp3() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(mAnswer.soundPath);
            mediaPlayer.prepare();
            //            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    private void initChoiceButton(LinearLayout layout, TextView tv, String answer) {
        layout.setVisibility(View.VISIBLE);
        Spanned spanned = processSpanned(answer);
        tv.setText(spanned);
    }

    private void initView() {
        if (mAnswer == null)
            return;

        if (mAnswer.soundPath == null) {
            mFragmentChoiceButSound.setVisibility(View.GONE);
        } else {
            mFragmentChoiceButSound.setVisibility(View.VISIBLE);
        }

        mAnswer.subject = mAnswer.subject.replaceAll("．", ".");
        String[] subjects = mAnswer.subject.split("\n");

        for (int i = 0; i < subjects.length; i++) {
            String answer = subjects[i];
            if (i >= 1 && answer.contains("A.")) {
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
                //                mChoiceLlD.setVisibility(View.VISIBLE);
                //                Spanned spanned = processSpanned(answer);
                //                mChoiceMvDAnswer.setText(spanned);
            }

            if (i >= 2 && answer.contains("E.")) {
                initChoiceButton(mFragmentChoiceLlE, mFragmentChoiceMvEAnswer, answer);
            }

            if (i >= 2 && answer.contains("F.")) {
                initChoiceButton(mFragmentChoiceLlF, mFragmentChoiceMvFAnswer, answer);
            }
            if (i >= 2 && answer.contains("G.")) {
                initChoiceButton(mFragmentChoiceLlG, mFragmentChoiceMvGAnswer, answer);
            }
            if (i >= 2 && answer.contains("H.")) {
                initChoiceButton(mFragmentChoiceLlH, mFragmentChoiceMvHAnswer, answer);
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

        //答题解析,有图片的时候文字居中方法
        Spanned spannedResult = Html.fromHtml(mAnswer.analysis.replaceAll("\n", "<br>"),
                getImageGetterInstance(getActivity()), new MyTagHandler(getActivity()));
        SpannedUtil.spannedUtil(spannedResult);
        mChoiceMvResult.setText(spannedResult);

        //点击图片放大
        //        mChoiceMvQuestion.setMovementMethod(LinkMovementMethod.getInstance());
        //        mChoiceMvAAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        //        mChoiceMvBAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        //        mChoiceMvCAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        //        mChoiceMvDAnswer.setMovementMethod(LinkMovementMethod.getInstance());
        mChoiceMvResult.setMovementMethod(LinkMovementMethod.getInstance());

        mChoiceMvResult.setVisibility(View.GONE);

        mQuestionDB = getAnswerData();
        if (mQuestionDB != null && mQuestionDB.userAnswer != null) {
            mAnswer.selectedAnswer = mQuestionDB.userAnswer;

            switch (mQuestionDB.userAnswer) {
                case "A":
                    updateRadioButton(mFragmentChoiceRbA);
                    break;
                case "B":
                    updateRadioButton(mFragmentChoiceRbB);
                    break;
                case "C":
                    updateRadioButton(mFragmentChoiceRbC);
                    break;
                case "D":
                    updateRadioButton(mFragmentChoiceRbD);
                    break;
                case "E":
                    updateRadioButton(mFragmentChoiceRbE);
                    break;
                case "F":
                    updateRadioButton(mFragmentChoiceRbF);
                    break;
                case "G":
                    updateRadioButton(mFragmentChoiceRbG);
                    break;
                case "H":
                    updateRadioButton(mFragmentChoiceRbH);
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
                    editText.setTextColor(Color.BLACK);
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(size)});
                    editText.setSingleLine();
                    editText.setTextSize(25);
                    editText.setFocusable(false);
                    editText.setPadding(2, 2, 2, 2);
                    editText.setGravity(Gravity.CENTER);
                    editText.setHeight((int) (mChoiceMvQuestion.getTextSize() * 1.5));
                    editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
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
            case R.id.fragment_choice_rb_A:
            case R.id.fragment_choice_ll_A:
                if (!mIsExplain) {
                    updateRadioButton(mFragmentChoiceRbA);
                    mAnswer.selectedAnswer = "A";
                    setEdtText("A");
                    updateDb();
                }
                break;
            case R.id.fragment_choice_ll_B:
            case R.id.fragment_choice_rb_B:
                if (!mIsExplain) {
                    updateRadioButton(mFragmentChoiceRbB);
                    mAnswer.selectedAnswer = "B";
                    setEdtText("B");
                    updateDb();
                }
                break;
            case R.id.fragment_choice_ll_C:
            case R.id.fragment_choice_rb_C:
                if (!mIsExplain) {
                    updateRadioButton(mFragmentChoiceRbC);
                    mAnswer.selectedAnswer = "C";
                    setEdtText("C");
                    updateDb();
                }
                break;
            case R.id.fragment_choice_ll_D:
            case R.id.fragment_choice_rb_D:
                if (!mIsExplain) {
                    updateRadioButton(mFragmentChoiceRbD);
                    mAnswer.selectedAnswer = "D";
                    setEdtText("D");
                    updateDb();
                }
                break;
            case R.id.fragment_choice_ll_E:
            case R.id.fragment_choice_rb_E:
                if (!mIsExplain) {
                    updateRadioButton(mFragmentChoiceRbE);
                    mAnswer.selectedAnswer = "E";
                    setEdtText("E");
                    updateDb();
                }
                break;
            case R.id.fragment_choice_ll_F:
            case R.id.fragment_choice_rb_F:
                if (!mIsExplain) {
                    updateRadioButton(mFragmentChoiceRbF);
                    mAnswer.selectedAnswer = "F";
                    setEdtText("F");
                    updateDb();
                }
                break;
            case R.id.fragment_choice_ll_G:
            case R.id.fragment_choice_rb_G:
                if (!mIsExplain) {
                    updateRadioButton(mFragmentChoiceRbG);
                    mAnswer.selectedAnswer = "G";
                    setEdtText("G");
                    updateDb();
                }
                break;
            case R.id.fragment_choice_ll_H:
            case R.id.fragment_choice_rb_H:
                if (!mIsExplain) {
                    updateRadioButton(mFragmentChoiceRbH);
                    mAnswer.selectedAnswer = "H";
                    setEdtText("H");
                    updateDb();
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

    public abstract void updateDb();

    public abstract QuestionDB getAnswerData();

    private void updateRadioButton(RadioButton radioButton) {
        mFragmentChoiceRbA.setChecked(false);
        mFragmentChoiceRbB.setChecked(false);
        mFragmentChoiceRbC.setChecked(false);
        mFragmentChoiceRbD.setChecked(false);
        mFragmentChoiceRbE.setChecked(false);
        mFragmentChoiceRbF.setChecked(false);
        mFragmentChoiceRbG.setChecked(false);
        mFragmentChoiceRbH.setChecked(false);
        mFragmentChoiceRbA.setButtonDrawable(R.drawable.radio_button_normal);
        mFragmentChoiceRbB.setButtonDrawable(R.drawable.radio_button_normal);
        mFragmentChoiceRbC.setButtonDrawable(R.drawable.radio_button_normal);
        mFragmentChoiceRbD.setButtonDrawable(R.drawable.radio_button_normal);
        mFragmentChoiceRbE.setButtonDrawable(R.drawable.radio_button_normal);
        mFragmentChoiceRbF.setButtonDrawable(R.drawable.radio_button_normal);
        mFragmentChoiceRbG.setButtonDrawable(R.drawable.radio_button_normal);
        mFragmentChoiceRbH.setButtonDrawable(R.drawable.radio_button_normal);
        radioButton.setButtonDrawable(R.drawable.radio_button_checked);
        radioButton.setChecked(true);
    }

    private void initExplain(RadioButton radioButton, TextView textView, String str) {
        radioButton.setChecked(true);
        if (mAnswer.answer.equals(str)) {
            textView.setTextColor(getResources().getColor(R.color.AnswerGreen));
            radioButton.setButtonDrawable(R.drawable.radio_button_green);
            mChoiceMvResult.setTextColor(getResources().getColor(R.color.AnswerGreen));
        } else {
            textView.setTextColor(getResources().getColor(R.color.AnswerRed));
            radioButton.setButtonDrawable(R.drawable.radio_button_red);
            mChoiceMvResult.setTextColor(getResources().getColor(R.color.AnswerRed));
        }

    }

}
