package fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
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
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icox.exercises.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import activity.MainActivity;
import activity.NotePageActivity;
import adapter.PadNoteAnswerAdapter;
import bean.EvenBusAnswerBean;
import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import conf.Constants;
import utils.MyTagHandler;
import utils.SpannedUtil;
import view.MtextViewNote;

import static utils.ImageGetterInstanceUtil.getImageGetterEdtText;
import static utils.ImageGetterInstanceUtil.getImageGetterInstance;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 14:10
 * @des 填空题 Type=3-1 文字填空加涂鸦填空
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class PadNoteFragment extends BaseFragment implements View.OnClickListener {

    @InjectView(R.id.fragment_choice_mv_question)
    MtextViewNote mFragmentChoiceMvQuestion;
    @InjectView(R.id.fragment_choice_mv_result)
    TextView mFragmentChoiceMvResult;
    @InjectView(R.id.fragment_choice_but_sound)
    ImageButton mFragmentChoiceButSound;
    @InjectView(R.id.fragment_choice_rl_big)
    RelativeLayout mFragmentChoiceRlBig;
    @InjectView(R.id.fragment_pad_note_ll_answer)
    ListView mPadNoteLlAnswer;
    @InjectView(R.id.fragment_pad_note_line)
    TextView mFragmentPadNoteLine;
    private View mView;
    private QuestionDB mQuestionDB;
    private MediaPlayer mediaPlayer;

    private String TAG = "PadFragment";
    private SpannableString mSubJectSb;
    private ArrayList<EditText> mEditTextList;
    private String[] mAnswers;
    private ArrayList<ImageView> mImageViewList;
    private ArrayList<View> mViews;
    private Map<Integer, String> pathMap = new HashMap<>();

    //答案标签的分隔符
    private String mAnswerSpilt = "\\|\\|";
    //子答案标签的分隔符
    private String mAnswerMoreSpilt = "_";
    //答案是图片的标签
    private String mAnswerPicTag = "图图图";

    @Override
    public View initBaseView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_pad_note, container, false);
        ButterKnife.inject(this, mView);
        EventBus.getDefault().register(this);
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
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
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
            //            String path =  MainActivity.mEbagbook.getExtractFile(getActivity(),mAnswer.soundPath);
            mediaPlayer.setDataSource(mAnswer.soundPath);
            mediaPlayer.prepare();
            //            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    private void initExplainationView() {
        mFragmentChoiceMvResult.setVisibility(View.VISIBLE);
        mPadNoteLlAnswer.setVisibility(View.VISIBLE);
        mFragmentPadNoteLine.setVisibility(View.VISIBLE);
        QuestionDB questionDB = mDbUtil.queryAnswer(mAnswer.id);

        Map<Integer, String> answerMap = new HashMap<>();

        String[] answer = mAnswer.answer.split(mAnswerSpilt);
        for (int i = 0; i < answer.length; i++) {
            if (!answer[i].contains(mAnswerPicTag)) {
                if (mAnswers != null && mAnswers.length > i) {
                    String[] answerChilde = answer[i].split(mAnswerMoreSpilt);
                    if (answerChilde.length > 0) {//如果答案有多个选项
                        boolean isYesAnswer = false;
                        for (int j = 0; j < answerChilde.length; j++) {
                            if (answerChilde[j].equals(mAnswers[i])) {
                                isYesAnswer = true;
                            }
                        }
                        if (!isYesAnswer) {
                            mViews.get(i).setBackgroundResource(R.drawable.shape_answer_explain_error);
                        } else {
                            answerMap.put(i, mAnswers[i]);
                        }
                    } else if (!answer[i].equals(mAnswers[i])) {
                        mViews.get(i).setBackgroundResource(R.drawable.shape_answer_explain_error);
                    }
                } else {
                    mViews.get(i).setBackgroundResource(R.drawable.shape_answer_explain_error);
                }
            }
        }

        Map<Integer, String> errorMap = new HashMap<>();
        //如果几个空的答案排序不一的,如果两个以上相同的,则只有一个是对的
        if (answerMap.size() > 1) {
            Iterator<Map.Entry<Integer, String>> iterator = answerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> entry = iterator.next();
                int key = entry.getKey();
                String value = entry.getValue();

                Iterator<Map.Entry<Integer, String>> iterator2 = answerMap.entrySet().iterator();
                while (iterator2.hasNext()) {
                    Map.Entry<Integer, String> entry2 = iterator2.next();
                    int key2 = entry2.getKey();
                    String value2 = entry2.getValue();
                    //已经存起来的相同答案,如果有则不做判断
                    String error = errorMap.get(key);
                    if (error == null && !value2.equals(error) && key != key2) {
                        if (value.equals(value2)) {
                            mViews.get(key2).setBackgroundResource(R.drawable.shape_answer_explain_error);
                            //相同的匹配起来,到时候不在做判断
                            errorMap.put(key2, value2);
                        }
                    }
                }
            }
        }


        for (View view : mViews) {
            view.setFocusable(false);
        }

        mPadNoteLlAnswer.setVisibility(View.VISIBLE);
        PadNoteAnswerAdapter adapter = new PadNoteAnswerAdapter(getActivity(), mAnswers, answer);
        mPadNoteLlAnswer.setAdapter(adapter);

    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {

        //如果音频地址不为空,则为听力题
        if (mAnswer.soundPath == null) {
            mFragmentChoiceButSound.setVisibility(View.GONE);
        } else {
            mFragmentChoiceButSound.setVisibility(View.VISIBLE);
        }

        //把图片标签前后的空格去掉
        String subject = mAnswer.subject;
        subject = subject.replaceAll("  <img src", "<img src");
        subject = subject.replaceAll(">  ", ">");

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

        mViews = new ArrayList<>();
        mEditTextList = new ArrayList<>();
        //设置题目的大小颜色
        mFragmentChoiceMvQuestion.setTextSize(30);
        mFragmentChoiceMvQuestion.setTextColor(Color.BLACK);

        //用Html方法去适配得到标签作处理
        String subjectHtml = toString.replaceAll("<font color=\"#0000ff\">", "") + " ";
        Html.fromHtml(subjectHtml, getImageGetterEdtText(getActivity(), mFragmentChoiceMvQuestion, mAnswer.answer),
                new MTagHandler());

        mFragmentChoiceMvQuestion.setMText(mSubJectSb);
        mFragmentChoiceMvQuestion.setView(mViews);

        mFragmentChoiceMvQuestion.invalidate();

        //答题解析,有图片的时候文字居中方法
        Spanned spannedResult = Html.fromHtml(mAnswer.analysis,
                getImageGetterInstance(getActivity()), new MyTagHandler(getActivity()));
        SpannedUtil.spannedUtil(spannedResult);
        mFragmentChoiceMvResult.setText(spannedResult);
        mFragmentChoiceMvResult.setMovementMethod(LinkMovementMethod.getInstance());
        mFragmentChoiceMvResult.setVisibility(View.GONE);

        String[] answers = mAnswer.answer.split(mAnswerSpilt);
        for (int i = 0; i < answers.length; i++) {
            pathMap.put(i, null);
        }

        mQuestionDB = mDbUtil.queryAnswer(mAnswer.id);
        if (mQuestionDB != null) {
            mAnswer.selectedAnswer = mQuestionDB.userAnswer;
            mAnswers = mAnswer.selectedAnswer.split(mAnswerSpilt);
            for (int i = 0; i < mAnswers.length; i++) {
                if (mViews.get(i) instanceof EditText) {
                    EditText editText = (EditText) mViews.get(i);
                    editText.setText(mAnswers[i]);
                }
                if (mAnswers[i].contains(Constants.PadNoteAnswerPathTag)) {
                    pathMap.put(i, mAnswers[i]);
                    mViews.get(i).setBackgroundResource(R.drawable.button_answer_note_pressed);
                }
            }
        }

        for (int i = 0; i < mViews.size(); i++) {
            View view = mViews.get(i);
            if (view instanceof ImageView) {
                view.setOnClickListener(this);
                view.setTag(i);
            }
        }

        //最后一个edt的软键盘设置为完成按钮
        if (mEditTextList != null && mEditTextList.size() > 0) {
            mEditTextList.get(mEditTextList.size() - 1).setImeOptions(EditorInfo.IME_ACTION_DONE);
        }

    }

    @Override
    public void onClick(View v) {

        if (mIsExplain)
            return;

        int tag = (int) v.getTag();
        String imgPath = null;
        if (pathMap.size() > tag && pathMap.get(tag) != null) {
            imgPath = pathMap.get(tag).replaceAll(Constants.PadNoteAnswerPathTag, "");
        } else if (mAnswers != null) {
            for (int i = 0; i < mAnswers.length; i++) {
                if (mAnswers[tag].contains(Constants.PadNoteAnswerPathTag)) {
                    imgPath = mAnswers[tag].replaceAll(Constants.PadNoteAnswerPathTag, "");
                }
            }
        }

        Intent intent = new Intent(getActivity(), NotePageActivity.class);
        intent.putExtra("ImgPath", imgPath);
        intent.putExtra("idAnswer", mAnswer.id);
        intent.putExtra("tag", tag);
        intent.putExtra("FilePath", MainActivity.mFilePath);
        startActivity(intent);
    }

    @Subscribe
    public void onMessage(final EvenBusAnswerBean busAnswerBean) {
        pathMap.put(busAnswerBean.tag, busAnswerBean.path);
        mViews.get(busAnswerBean.tag).setBackgroundResource(R.drawable.button_answer_note_pressed);
        updataDB();
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
                    Log.e("sup","start = "+start);
                    Log.i("sup","last = "+last);
                    SuperscriptSpan span = new SuperscriptSpan();
                    mSubJectSb.setSpan(span, start, last, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    return;
                }

                if (imgURL.contains("sub_")) {//下标
                    int start = Integer.valueOf(imgURL.substring(imgURL.indexOf("_") + 1, imgURL.lastIndexOf("_")));
                    int last = Integer.valueOf(imgURL.substring(imgURL.lastIndexOf("_") + 1, imgURL.length()));
                    Log.e("sup","start = "+start);
                    Log.i("sup","last = "+last);
                    SubscriptSpan span = new SubscriptSpan();
                    mSubJectSb.setSpan(span, start, last, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    return;
                }

                //图片
                mSubJectSb.setSpan(images[0], len - 2, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                if (imgURL.contains("input_")) {
                    EditText editText = new EditText(getActivity());
                    int size = Integer.valueOf(imgURL.substring(imgURL.lastIndexOf("_") + 1, imgURL.length()));
                    editText.setTextColor(Color.BLACK);
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(size)});
                    editText.setSingleLine();
                    editText.setTextSize(25);
                    editText.setPadding(2, 2, 2, 2);
                    editText.setGravity(Gravity.CENTER);
                    editText.setHeight((int) (mFragmentChoiceMvQuestion.getTextSize() * 1.5));
                    editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    mFragmentChoiceRlBig.addView(editText);
                    mViews.add(editText);
                    mEditTextList.add(editText);

                } else if (imgURL.contains("note_")) {
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setImageResource(R.drawable.button_answer_note_normal);
                    imageView.setMaxHeight((int) (mFragmentChoiceMvQuestion.getTextSize() * 1.5));
                    mFragmentChoiceRlBig.addView(imageView);
                    mViews.add(imageView);
                }

                // 使图片可点击并监听点击事件
                //                output.setSpan(new MyTagHandler.ClickableImage(mContext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        updataDB();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isVisibleToUser && isExist) {
            updataDB();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onStop() {
        super.onStop();
        //        updataDB();
    }

    private void updataDB() {
        QuestionDB questionDB = mDbUtil.queryAnswer(mAnswer.id);
        String[] answers = mAnswer.answer.split(mAnswerSpilt);
        mAnswer.selectedAnswer = "";
        int currentEditIndex = 0;
        for (int i = 0; i < answers.length; i++) {
            if (answers[i].contains(mAnswerPicTag)) {
                String path = "图图图";
                if (pathMap.get(i) != null) {
                    path = pathMap.get(i);
                }
                if (i == answers.length - 1) {
                    mAnswer.selectedAnswer += path;
                } else {
                    mAnswer.selectedAnswer += (path + "||");
                }
            } else if (mViews.get(i) instanceof EditText) {
                EditText editText = (EditText) mViews.get(i);
                String answer = editText.getText().toString().trim();
                if (i == answer.length() - 1) {
                    mAnswer.selectedAnswer += answer;
                } else {
                    mAnswer.selectedAnswer += (answer + "||");
                }
                currentEditIndex++;
            }
        }

        if (mAnswer.selectedAnswer != null) {
            String answer = mAnswer.selectedAnswer.replaceAll("：", ":");
            if (questionDB == null) {
                mDbUtil.insertAnswerData(mAnswer.id, answer);
            } else {
                mDbUtil.updateAnswer(mAnswer.id, answer);
            }
        }
    }

}
