package fragment;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import conf.Constants;
import utils.MyTagHandler;
import view.MtextView;

import static utils.ImageGetterInstanceUtil.getImageGetterEdtText;
import static utils.ImageGetterInstanceUtil.getImageGetterInstance;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 14:10
 * @des 填空题
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class PadFragment extends BaseFragment {

    @InjectView(R.id.fragment_choice_mv_question)
    MtextView mFragmentChoiceMvQuestion;
    @InjectView(R.id.fragment_choice_mv_result)
    TextView mFragmentChoiceMvResult;
    @InjectView(R.id.fragment_choice_but_sound)
    ImageButton mFragmentChoiceButSound;
    @InjectView(R.id.fragment_choice_rl_big)
    RelativeLayout mFragmentChoiceRlBig;
    private View mView;
    private QuestionDB mQuestionDB;
    private MediaPlayer mediaPlayer;

    private String TAG = "PadFragment";
    private SpannableStringBuilder mSubJectSb;
    private ArrayList<EditText> mEditTextList;
    private String[] mAnswers;

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
                //当播放完音频资源时，会触发onCompletion事件，可以在该事件中释放音频资源，
                //以便其他应用程序可以使用该资源:
                //                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                //                    @Override
                //                    public void onCompletion(MediaPlayer mp) {
                //                        mp.release();//释放音频资源
                //                    }
                //                });
                //                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                //                                    @Override
                //                                    public void onPrepared(MediaPlayer mp) {
                //                                            mp.start();
                //                                        }
                //                                });
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
        /**
         * 创建音频文件的方法：
         * 1、播放资源目录的文件：MediaPlayer.create(MainActivity.this,R.raw.beatit);//播放res/raw 资源目录下的MP3文件
         * 2:播放sdcard卡的文件：mediaPlayer=new MediaPlayer();
         *   mediaPlayer.setDataSource("/sdcard/beatit.mp3");//前提是sdcard卡要先导入音频文件
         */
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
    private void initExplainationView() {
        mFragmentChoiceMvResult.setVisibility(View.VISIBLE);
        QuestionDB questionDB = mDbUtil.queryAnswer(mAnswer.id);
        Map<Integer, String> answerMap = new HashMap<>();
        String[] answer = mAnswer.answer.split("\\|\\|");
        for (int i = 0; i < answer.length; i++) {
            if (mAnswers != null && mAnswers.length > i) {
                String[] answerChilde = answer[i].split("_");
                if (answerChilde.length > 0) {//如果答案有多个选项
                    boolean isYesAnswer = false;
                    for (int j = 0; j < answerChilde.length; j++) {
                        if (answerChilde[j].equals(mAnswers[i])) {
                            isYesAnswer = true;
                        }
                    }
                    if (!isYesAnswer) {
//                        mEditTextList.get(i).setBackgroundResource(R.drawable.shape_answer_explain_error);
                        mEditTextList.get(i).setTextColor(Color.RED);
                    } else {
                        //把多空不规则排序的答案存起来,做相同判断
                        answerMap.put(i, mAnswers[i]);
                    }

                } else if (!answer[i].equals(mAnswers[i])) {
                        mEditTextList.get(i).setTextColor(Color.RED);
//                    mEditTextList.get(i).setBackgroundResource(R.drawable.shape_answer_explain_error);
                }
            } else {
                        mEditTextList.get(i).setTextColor(Color.RED);
//                mEditTextList.get(i).setBackgroundResource(R.drawable.shape_answer_explain_error);
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
                    Map.Entry<Integer, String> entry1 = iterator2.next();
                    int key2 = entry1.getKey();
                    String value2 = entry1.getValue();
                    //已经存起来的相同答案,如果有则不做判断
                    String error = errorMap.get(key);
                    if (error == null && !value2.equals(error) && key != key2) {
                        if (value.equals(value2)) {
                            mEditTextList.get(key2).setBackgroundResource(R.drawable.shape_answer_explain_error);
//                            mEditTextList.get(key2).setTextColor(Color.RED);
                            //错的匹配起来,到时候不在做判断
                            errorMap.put(key2, value2);
                        }
                    }
                }
            }
        }

        for (EditText editText : mEditTextList) {
            editText.setFocusable(false);
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {

        if (mAnswer.soundPath == null) {
            mFragmentChoiceButSound.setVisibility(View.GONE);
        } else {
            mFragmentChoiceButSound.setVisibility(View.VISIBLE);
        }


        //        mFragmentChoiceMvQuestion.setText(Html.fromHtml(mAnswer.subject.replace("\n", "<br>"),
        //                getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));

        String subject = mAnswer.subject;
        mSubJectSb = new SpannableStringBuilder(subject.replaceAll("<[^>]+>", "") + "                 ");

        mEditTextList = new ArrayList<>();
        mFragmentChoiceMvQuestion.setTextSize(30);
        mFragmentChoiceMvQuestion.setTextColor(Color.BLACK);

        String subjectHtml = subject.replaceAll("<font color=\"#0000ff\">", "") + " ";
        Html.fromHtml(subjectHtml, getImageGetterEdtText(getActivity(), mFragmentChoiceMvQuestion, mAnswer.answer),
                new MTagHandler());

        mFragmentChoiceMvQuestion.setMText(mSubJectSb);
        mFragmentChoiceMvQuestion.setEdtText(mEditTextList);
        mFragmentChoiceMvQuestion.invalidate();

        mFragmentChoiceMvResult.setText(Html.fromHtml(mAnswer.analysis,
                getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));

        mFragmentChoiceMvQuestion.setMovementMethod(LinkMovementMethod.getInstance());
        mFragmentChoiceMvResult.setMovementMethod(LinkMovementMethod.getInstance());

        mFragmentChoiceMvResult.setVisibility(View.GONE);

        mQuestionDB = mDbUtil.queryAnswer(mAnswer.id);
        if (mQuestionDB != null && mAnswer.selectedAnswer!=null) {
            mAnswer.selectedAnswer = mQuestionDB.userAnswer;
            mAnswers = mAnswer.selectedAnswer.split("\\|\\|");
            for (int i = 0; i < mAnswers.length; i++) {
                if (mEditTextList.size() > i) {
                    //                    if (mAnswers.equals("null"))
                    //                        return;
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
                mSubJectSb.setSpan("(　", len - 2, len - 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                mSubJectSb.setSpan(images[0], len - 1, len - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                mSubJectSb.setSpan("　)", len, len, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                if (imgURL.contains("input_")) {
                    EditText editText = new EditText(getActivity());
                    int size = Integer.valueOf(imgURL.substring(imgURL.lastIndexOf("_") + 1, imgURL.length()));
                    mEditTextList.add(editText);
                    //                    editText.setWidth((int) (mChoiceMvQuestion.getTextSize() * 3*size));
                    editText.setTextColor(Color.BLACK);
                    //                    editText.setBackgroundResource(R.drawable.shape_answer_selector);
                    //                    editText.setMaxLines(size);
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(size)});
                    editText.setSingleLine();
                    editText.setTextSize(25);
                    editText.setPadding(2, 2, 2, 2);
                    editText.setGravity(Gravity.CENTER);
                    editText.setHeight((int) (mFragmentChoiceMvQuestion.getTextSize() * 1.2));
                    editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
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
        if (!Constants.isResetAnswer)
            updataDB();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        ButterKnife.reset(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isVisibleToUser && isExist) {
            updataDB();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    private void updataDB() {
        QuestionDB questionDB = mDbUtil.queryAnswer(mAnswer.id);
        mAnswer.selectedAnswer = "";
        for (int i = 0; i < mEditTextList.size(); i++) {
            String answer = mEditTextList.get(i).getText().toString().trim();
            //            if (TextUtils.isEmpty(answer))
            //                answer = "null";
            if (i == mEditTextList.size() - 1) {
                mAnswer.selectedAnswer += answer;
            } else {
                mAnswer.selectedAnswer += answer + "||";
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


    class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //            mAnswer.selectedAnswer = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
