package fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
 * @des ${判断题}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class JudgeFragment extends BaseFragment implements View.OnClickListener {

    @InjectView(R.id.fragment_judge_tv_question)
    MtextView mFragmentJudgeTvQuestion;
    @InjectView(R.id.fragment_judge_tv_correct)
    TextView mFragmentJudgeTvCorrect;
    @InjectView(R.id.fragment_judge_tv_error)
    TextView mFragmentJudgeTvError;
    @InjectView(R.id.fragment_judge_mv_result)
    TextView mFragmentJudgeMvResult;
    @InjectView(R.id.fragment_judge_rl_big)
    RelativeLayout mFragmentJudgeRlBig;
    private View mView;
    private QuestionDB mQuestionDB;
    private SpannableStringBuilder mSubJectSb;
    private ArrayList<EditText> mEditTextList;


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
                if (mQuestionDB.userAnswer.equals("0")) {
                    mFragmentJudgeTvError.setBackgroundResource(R.drawable.shape_answer_explain_error);
                } else if (mQuestionDB.userAnswer.equals("1")) {
                    mFragmentJudgeTvCorrect.setBackgroundResource(R.drawable.shape_answer_explain_error);
                }
            }
        }
    }

    private void initListener() {
        mFragmentJudgeTvError.setOnClickListener(this);
        mFragmentJudgeTvCorrect.setOnClickListener(this);
    }

    private void initView() {

        mSubJectSb = new SpannableStringBuilder(mAnswer.subject.replaceAll("<[^>]+>", "") + "                 ");

        mEditTextList = new ArrayList<>();
        mFragmentJudgeTvQuestion.setTextSize(30);
        mFragmentJudgeTvQuestion.setTextColor(Color.BLACK);

        String subjectHtml = mAnswer.subject.replaceAll("<font color=\"#0000ff\">", "") + " ";
        Html.fromHtml(subjectHtml, getImageGetterEdtText(getActivity(), mFragmentJudgeTvQuestion, mAnswer.answer),
                new MTagHandler());

        mFragmentJudgeTvQuestion.setMText(mSubJectSb);
        mFragmentJudgeTvQuestion.setEdtText(mEditTextList);
        mFragmentJudgeTvQuestion.invalidate();


//        mFragmentJudgeTvQuestion.setMovementMethod(LinkMovementMethod.getInstance());
//        mFragmentJudgeTvQuestion.setText(Html.fromHtml(mAnswer.subject,
//                getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));

        mFragmentJudgeMvResult.setMovementMethod(LinkMovementMethod.getInstance());
        mFragmentJudgeMvResult.setText(Html.fromHtml(mAnswer.analysis,
                getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));
        mFragmentJudgeMvResult.setVisibility(View.GONE);


        mQuestionDB = mDbUtil.queryAnswer(mAnswer.id);
        if (mQuestionDB != null) {
            mAnswer.selectedAnswer = mQuestionDB.userAnswer;
            if (mQuestionDB.userAnswer.equals("0")) {//选择错误
                mFragmentJudgeTvError.setBackgroundResource(R.drawable.shape_answer_selector);
                mFragmentJudgeTvCorrect.setBackgroundResource(R.drawable.shape_answer_normal);
                mEditTextList.get(0).setText("错误");
            } else if (mQuestionDB.userAnswer.equals("1")) {//选择正确
                mFragmentJudgeTvError.setBackgroundResource(R.drawable.shape_answer_normal);
                mFragmentJudgeTvCorrect.setBackgroundResource(R.drawable.shape_answer_selector);
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
            case R.id.fragment_judge_tv_error:
                if (mIsExplain) {
                    return;
                } else {
                    mFragmentJudgeTvCorrect.setBackgroundResource(R.drawable.shape_answer_normal);
                    mFragmentJudgeTvError.setBackgroundResource(R.drawable.shape_answer_selector);
                    mEditTextList.get(0).setText("错误");
                    mAnswer.selectedAnswer = "0";
                    updataDB();
                }
                break;
            case R.id.fragment_judge_tv_correct:
                if (mIsExplain) {
                    return;
                } else {
                    mFragmentJudgeTvCorrect.setBackgroundResource(R.drawable.shape_answer_selector);
                    mFragmentJudgeTvError.setBackgroundResource(R.drawable.shape_answer_normal);
                    mAnswer.selectedAnswer = "1";
                    mEditTextList.get(0).setText("正确");
                    updataDB();
                }
                break;
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
}
