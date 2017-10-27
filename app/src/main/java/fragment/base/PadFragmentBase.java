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
 * @des 填空题
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class PadFragmentBase extends BaseFragment {

    @InjectView(R.id.fragment_choice_mv_question)
    public MtextView mFragmentChoiceMvQuestion;
    @InjectView(R.id.fragment_choice_edt)
    public EditText mFragmentChoiceEdt;
    @InjectView(R.id.fragment_choice_mv_result)
    public TextView mFragmentChoiceMvResult;
    @InjectView(R.id.fragment_choice_but_sound)
    public ImageButton mFragmentChoiceButSound;
    @InjectView(R.id.fragment_choice_rl_big)
    public RelativeLayout mFragmentChoiceRlBig;
    public View mView;
    public QuestionDB mQuestionDB;
    public MediaPlayer mediaPlayer;

    private String TAG = "PadFragment";
    public SpannableString mSubJectSb;
    public ArrayList<EditText> mEditTextList;
    public String[] mAnswers;

    @Override
    public View initBaseView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_pad, container, false);
        ButterKnife.inject(this, mView);
        initView();
        if (mIsExplain) {
            initExplainationView();
        }
        initListener();
        return mView;
    }

    private void initListener() {
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

    /**
     * 答案匹配
     */
    public abstract void initExplainationView();


    private void initView() {

        if (mAnswer.soundPath == null) {
            mFragmentChoiceButSound.setVisibility(View.GONE);
        } else {
            mFragmentChoiceButSound.setVisibility(View.VISIBLE);
        }

        //把图片标签前后的空格去掉
        String subject = mAnswer.subject;
        subject = subject.replaceAll("  <img src", "<img src");
        subject = subject.replaceAll(">  ", ">");
        subject = subject.replaceAll("</font>", "");
        subject = subject.replaceAll(" \n", "\n");
        subject = subject.replaceAll("\n ", "\n");
        subject = subject.replaceAll("\\( ", "(");
        subject = subject.replaceAll("<p>", "");
        subject = subject.replaceAll("<nbsp>", "");
        subject = subject.replaceAll("<br>", "\n");

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
        mFragmentChoiceMvQuestion.setTextSize(30);
        mFragmentChoiceMvQuestion.setTextColor(Color.BLACK);

        //用Html方法去适配得到标签作处理
        String htmlString = toString.replaceAll("<font color=\"#0000ff\">", "");
        htmlString = htmlString.replaceAll("\n", "-");
        Html.fromHtml(htmlString, getImageGetterEdtText(getActivity(), mFragmentChoiceMvQuestion, mAnswer.answer),
                new MTagHandler());

        mFragmentChoiceMvQuestion.setMText(mSubJectSb);
        mFragmentChoiceMvQuestion.setEdtText(mEditTextList);
        //刷新控件,重走一遍onDraw
        mFragmentChoiceMvQuestion.invalidate();

        //答题解析,有图片的时候文字居中方法
        //mAnswer.analysis.replaceAll("\n", "<br>") 因为是html显示,所以把\n转化成br
        Spanned spannedResult = processSpanned(mAnswer.analysis);
        mFragmentChoiceMvResult.setText(spannedResult);
        mFragmentChoiceMvResult.setMovementMethod(LinkMovementMethod.getInstance());
        mFragmentChoiceMvResult.setVisibility(View.GONE);

        mQuestionDB = getAnswerData();
        if (mQuestionDB != null && mQuestionDB.userAnswer != null) {
            mAnswer.selectedAnswer = mQuestionDB.userAnswer;
            mAnswers = mAnswer.selectedAnswer.split("\\|\\|");
            for (int i = 0; i < mAnswers.length; i++) {
                if (mEditTextList.size() > i) {
                    mEditTextList.get(i).setText(mAnswers[i]);
                }
            }
        }

        //最后一个edt的软键盘设置为完成按钮
        if (mEditTextList != null && mEditTextList.size() > 0) {
            mEditTextList.get(mEditTextList.size() - 1).setImeOptions(EditorInfo.IME_ACTION_DONE);
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

                //图片
                mSubJectSb.setSpan(images[0], len - 2, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (imgURL.contains("input_")) {
                    EditText editText = new EditText(getActivity());
                    int size = Integer.valueOf(imgURL.substring(imgURL.lastIndexOf("_") + 1, imgURL.length()));
                    //将输入框保存到集合里
                    mEditTextList.add(editText);
                    editText.setTextColor(Color.BLACK);
                    //限定输入框能输入的字符数量
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(size)});
                    editText.setSingleLine();
                    editText.setTextSize(25);
                    editText.setPadding(2, 2, 2, 2);
                    editText.setGravity(Gravity.CENTER);
                    editText.setHeight((int) (mFragmentChoiceMvQuestion.getTextSize() * 1.5));
                    //设置虚拟键盘最下面的按钮是下一个
                    editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    //将输入框加入到布局容器
                    mFragmentChoiceRlBig.addView(editText);
                }

                // 使图片可点击并监听点击事件
                //                output.setSpan(new MyTagHandler.ClickableImage(mContext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //        if (!Constants.isResetAnswer)
        updateDb();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        ButterKnife.reset(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isVisibleToUser && isExist) {
            updateDb();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public abstract void updateDb();

    public abstract QuestionDB getAnswerData();
}
