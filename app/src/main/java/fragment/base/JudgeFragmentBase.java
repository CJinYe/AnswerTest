package fragment.base;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
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

import java.io.IOException;
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
 * @des ${判断题}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class JudgeFragmentBase extends BaseFragment implements View.OnClickListener {

    @InjectView(R.id.fragment_judge_tv_question)
    MtextView mFragmentJudgeTvQuestion;
    @InjectView(R.id.fragment_judge_ll_correct)
    LinearLayout mFragmentJudgeTvCorrect;
    @InjectView(R.id.fragment_judge_ll_error)
    LinearLayout mFragmentJudgeTvError;
    @InjectView(R.id.fragment_judge_mv_result)
    TextView mFragmentJudgeMvResult;
    @InjectView(R.id.fragment_judge_rl_big)
    RelativeLayout mFragmentJudgeRlBig;
    @InjectView(R.id.fragment_judge_radio_correct)
    RadioButton mRadioCorrect;
    @InjectView(R.id.fragment_judge_tv_correct)
    TextView mTvCorrect;
    @InjectView(R.id.fragment_judge_radio_error)
    RadioButton mRadioError;
    @InjectView(R.id.fragment_judge_tv_error)
    TextView mTvError;
    @InjectView(R.id.fragment_judge_but_sound)
    ImageButton mButSound;
    private View mView;
    public QuestionDB mQuestionDB;
    private SpannableString mSubJectSb;
    private ArrayList<EditText> mEditTextList;
    private MediaPlayer mMediaPlayer;

    @Override
    public View initBaseView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_judg, container, false);
        ButterKnife.inject(this, mView);
        initView();
        if (mIsExplain) {
            initExplainView();
        }
        initListener();
        return mView;
    }

    private void initExplainView() {
        mFragmentJudgeMvResult.setVisibility(View.VISIBLE);
        if (mQuestionDB != null && !TextUtils.isEmpty(mQuestionDB.userAnswer)) {
            if (!mQuestionDB.userAnswer.equals(mAnswer.answer)) {
                //答错了
                initExplain(mQuestionDB.userAnswer, false);
            } else {
                //答对了
                initExplain(mQuestionDB.userAnswer, true);
            }
        }
    }

    private void initListener() {
        mFragmentJudgeTvError.setOnClickListener(this);
        mFragmentJudgeTvCorrect.setOnClickListener(this);
        mRadioError.setOnClickListener(this);
        mRadioCorrect.setOnClickListener(this);
        mButSound.setOnClickListener(this);
    }

    private void initView() {
        if (mAnswer.sound == null)
            mButSound.setVisibility(View.GONE);
        else
            mButSound.setVisibility(View.VISIBLE);

        String subject = mAnswer.subject;
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
        mFragmentJudgeTvQuestion.setTextSize(30);
        mFragmentJudgeTvQuestion.setTextColor(Color.BLACK);

        //用Html方法去适配得到标签作处理
        String htmlString = toString.replaceAll("<font color=\"#0000ff\">", "");
        htmlString = htmlString.replaceAll("\n", "-");
        Html.fromHtml(htmlString, getImageGetterEdtText(getActivity(), mFragmentJudgeTvQuestion, mAnswer.answer, 2),
                new MTagHandler());


        mFragmentJudgeTvQuestion.setMText(mSubJectSb);
        mFragmentJudgeTvQuestion.setEdtText(mEditTextList);
        mFragmentJudgeTvQuestion.invalidate();

        mFragmentJudgeMvResult.setMovementMethod(LinkMovementMethod.getInstance());
        Spanned spannedResult = processSpanned(mAnswer.analysis);
        mFragmentJudgeMvResult.setText(spannedResult);
        mFragmentJudgeMvResult.setVisibility(View.GONE);


        mQuestionDB = getAnswerData();
        if (mQuestionDB != null && mQuestionDB.userAnswer != null) {
            mAnswer.selectedAnswer = mQuestionDB.userAnswer;
            if (mQuestionDB.userAnswer.equals("0")) {//选择错误
                updateRadioButton(mRadioError);
                if (mEditTextList.size() > 0)
                    mEditTextList.get(0).setText("错误");
            } else if (mQuestionDB.userAnswer.equals("1")) {//选择正确
                updateRadioButton(mRadioCorrect);
                if (mEditTextList.size() > 0)
                    mEditTextList.get(0).setText("正确");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
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

                mSubJectSb.setSpan(images[0], len - 2, len, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
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
                    editText.setHeight((int) (mFragmentJudgeTvQuestion.getTextSize() * 1.5));
                    mFragmentJudgeRlBig.addView(editText);

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
            case R.id.fragment_judge_radio_error:
            case R.id.fragment_judge_ll_error:
                if (mIsExplain) {
                    return;
                } else {
                    updateRadioButton(mRadioError);
                    if (mEditTextList.size() > 0)
                        mEditTextList.get(0).setText("错误");
                    mAnswer.selectedAnswer = "0";
                    updateDb();
                }
                break;
            case R.id.fragment_judge_radio_correct:
            case R.id.fragment_judge_ll_correct:
                if (mIsExplain) {
                    return;
                } else {
                    updateRadioButton(mRadioCorrect);
                    mAnswer.selectedAnswer = "1";
                    if (mEditTextList.size() > 0)
                        mEditTextList.get(0).setText("正确");
                    updateDb();
                }
                break;
            case R.id.fragment_judge_but_sound:
                    if (mMediaPlayer == null)
                        mMediaPlayer = createLocalMp3();

                    mMediaPlayer.seekTo(0);
                    mMediaPlayer.start();
                break;
        }
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

    private void updateRadioButton(RadioButton radioButton) {
        mRadioError.setChecked(false);
        mRadioCorrect.setChecked(false);
        mRadioCorrect.setButtonDrawable(R.drawable.radio_button_normal);
        mRadioError.setButtonDrawable(R.drawable.radio_button_normal);
        radioButton.setButtonDrawable(R.drawable.radio_button_checked);
        radioButton.setChecked(true);
    }

    private void initExplain(String answer, boolean isCorrect) {
        if (answer.equals("0")) {
            if (isCorrect) {
                mTvError.setTextColor(getResources().getColor(R.color.AnswerGreen));
                mRadioError.setButtonDrawable(R.drawable.radio_button_green);
                mFragmentJudgeMvResult.setTextColor(getResources().getColor(R.color.AnswerGreen));
            } else {
                mTvCorrect.setTextColor(getResources().getColor(R.color.AnswerRed));
                mRadioCorrect.setButtonDrawable(R.drawable.radio_button_red);
                mFragmentJudgeMvResult.setTextColor(getResources().getColor(R.color.AnswerRed));
            }
        } else {
            if (isCorrect) {
                mTvCorrect.setTextColor(getResources().getColor(R.color.AnswerGreen));
                mRadioCorrect.setButtonDrawable(R.drawable.radio_button_green);
                mFragmentJudgeMvResult.setTextColor(getResources().getColor(R.color.AnswerGreen));
            } else {
                mTvCorrect.setTextColor(getResources().getColor(R.color.AnswerRed));
                mRadioCorrect.setButtonDrawable(R.drawable.radio_button_red);
                mFragmentJudgeMvResult.setTextColor(getResources().getColor(R.color.AnswerRed));
            }
        }
    }

    public abstract void updateDb();

    public abstract QuestionDB getAnswerData();


    private void updataDB() {
        QuestionDB questionDB = mDbUtil.queryAnswer(mAnswer.id);
        if (questionDB == null) {
            mDbUtil.insertAnswerData(mAnswer.id, mAnswer.selectedAnswer);
        } else {
            mDbUtil.updateAnswer(mAnswer.id, mAnswer.selectedAnswer);
        }
    }
}
