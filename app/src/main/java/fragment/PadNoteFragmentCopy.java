package fragment;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icox.exercises.R;

import org.xml.sax.XMLReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import bean.ExercisesBean;
import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import conf.Constants;
import db.DBUtil;
import utils.MyTagHandler;
import utils.SpUtils;
import view.MtextView;

import static android.R.attr.path;
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
public class PadNoteFragmentCopy extends Fragment implements View.OnClickListener {

    @InjectView(R.id.fragment_choice_mv_question)
    MtextView mFragmentChoiceMvQuestion;
    @InjectView(R.id.fragment_choice_edt)
    EditText mFragmentChoiceEdt;
    @InjectView(R.id.fragment_choice_mv_result)
    TextView mFragmentChoiceMvResult;
    @InjectView(R.id.fragment_choice_but_sound)
    ImageButton mFragmentChoiceButSound;
    @InjectView(R.id.fragment_choice_rl_big)
    RelativeLayout mFragmentChoiceRlBig;
    private View mView;
    private ExercisesBean mChoice;
    private DBUtil mDbUtil;
    private QuestionDB mQuestionDB;
    private boolean mIsExplain;
    private MediaPlayer mediaPlayer;
    private boolean isExist;//当前页面是否已经创建

    private String TAG = "PadFragment";
    private SpannableStringBuilder mSubJectSb;
    private ArrayList<EditText> mEditTextList;
    private String[] mAnswers;
    private ArrayList<ImageView> mImageViewList;

    //答案标签的分隔符
    private String mAnswerSpilt = "||";
    //子答案标签的分隔符
    private String mAnswerMoreSpilt = "_";
    //答案是图片的标签
    private String mAnswerPicTag = "图图图";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_pad, container, false);
        ButterKnife.inject(this, mView);
        SpUtils spUtils = new SpUtils(getActivity());
        isExist = true;
        mDbUtil = new DBUtil(getActivity());
        initView();
        mIsExplain = spUtils.getBoolean(Constants.isExplain, false);
        if (mIsExplain) {
            initExplainationView();
        }
        initListener();
        return mView;
    }

    private void initListener() {
        mFragmentChoiceEdt.addTextChangedListener(new EditChangedListener());

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
            //            String path =  MainActivity.mEbagbook.getExtractFile(getActivity(),mChoice.soundPath);
            mediaPlayer.setDataSource(mChoice.soundPath);
            Log.d(TAG, " path = " + mChoice.soundPath + "    paht = " + path + "  ");
            mediaPlayer.prepare();
            //            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    private void initExplainationView() {
        mFragmentChoiceEdt.setFocusable(false);
        mFragmentChoiceMvResult.setVisibility(View.VISIBLE);
        QuestionDB questionDB = mDbUtil.queryAnswer(mChoice.id);
        String[] answer = mChoice.answer.split(mAnswerSpilt);
        for (int i = 0; i < answer.length; i++) {

            if (answer[i].contains(mAnswerPicTag)) {
                return;
            }

            if (mAnswers.length > i) {
                String[] answerChilde = answer[i].split(mAnswerMoreSpilt);
                if (answerChilde.length > 0) {//如果答案有多个选项
                    boolean isYesAnswer = false;
                    for (int j = 0; j < answerChilde.length; j++) {
                        if (answerChilde[j].equals(mAnswers[i])) {
                            isYesAnswer = true;
                        }
                    }
                    if (!isYesAnswer) {
                        mEditTextList.get(i).setBackgroundResource(R.drawable.shape_answer_explain_error);
                    }
                } else if (!answer[i].equals(mAnswers[i])) {
                    mEditTextList.get(i).setBackgroundResource(R.drawable.shape_answer_explain_error);
                }
            } else {
                mEditTextList.get(i).setBackgroundResource(R.drawable.shape_answer_explain_error);
            }
        }

        for (EditText editText : mEditTextList) {
            editText.setFocusable(false);
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initView() {

        //如果音频地址不为空,则为听力题
        if (mChoice.soundPath == null) {
            mFragmentChoiceButSound.setVisibility(View.GONE);
        } else {
            mFragmentChoiceButSound.setVisibility(View.VISIBLE);
        }


        //        mFragmentChoiceMvQuestion.setText(Html.fromHtml(mChoice.subject.replace("\n", "<br>"),
        //                getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));

        String subject = mChoice.subject;
        mSubJectSb = new SpannableStringBuilder(subject.replaceAll("<[^>]+>", "") + "                 ");

        mEditTextList = new ArrayList<>();
        mImageViewList = new ArrayList<>();
        mFragmentChoiceMvQuestion.setTextSize(30);
        mFragmentChoiceMvQuestion.setTextColor(Color.BLACK);

        String subjectHtml = subject.replaceAll("<font color=\"#0000ff\">", "") + " ";
        Html.fromHtml(subjectHtml, getImageGetterEdtText(getActivity(), mFragmentChoiceMvQuestion, mChoice.answer),
                new MTagHandler());

        mFragmentChoiceMvQuestion.setMText(mSubJectSb);
        mFragmentChoiceMvQuestion.setEdtText(mEditTextList);
        mFragmentChoiceMvQuestion.setImageView(mImageViewList);
        mFragmentChoiceMvQuestion.invalidate();

        mFragmentChoiceMvResult.setText(Html.fromHtml(mChoice.analysis,
                getImageGetterInstance(getActivity()), new MyTagHandler(getActivity())));

        mFragmentChoiceMvQuestion.setMovementMethod(LinkMovementMethod.getInstance());
        mFragmentChoiceMvResult.setMovementMethod(LinkMovementMethod.getInstance());

        mFragmentChoiceMvResult.setVisibility(View.GONE);

        mQuestionDB = mDbUtil.queryAnswer(mChoice.id);
        if (mQuestionDB != null) {
            mChoice.selectedAnswer = mQuestionDB.userAnswer;
            mAnswers = mChoice.selectedAnswer.split(mAnswerSpilt);
            int editTextSize = 0;
            for (int i = 0; i < mAnswers.length; i++) {
                if (mAnswers[i].contains(mAnswerPicTag)) {
                    return;
                }

                if (mEditTextList.size() > editTextSize) {
                    mEditTextList.get(i).setText(mAnswers[editTextSize]);
                    editTextSize++;
                }
            }
        }


        for (ImageView image : mImageViewList) {
            image.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Log.e("onClick", "点击");
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
                    editText.setTextColor(Color.BLACK);
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(size)});
                    editText.setSingleLine();
                    editText.setTextSize(25);
                    editText.setPadding(5, 5, 5, 5);
                    editText.setGravity(Gravity.CENTER);
                    editText.setHeight((int) (mFragmentChoiceMvQuestion.getTextSize() * 1.5));
                    mFragmentChoiceRlBig.addView(editText);

                } else if (imgURL.contains("note_")) {
                    ImageView imageView = new ImageView(getActivity());
                    mImageViewList.add(imageView);
                    imageView.setImageResource(R.drawable.button_note);
                    imageView.setMaxHeight((int) (mFragmentChoiceMvQuestion.getTextSize() * 1.5));
                    mFragmentChoiceRlBig.addView(imageView);
                }

                // 使图片可点击并监听点击事件
                //                output.setSpan(new MyTagHandler.ClickableImage(mContext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public void initData(ExercisesBean choice) {
        mChoice = choice;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        isExist = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        //        updataDB();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isVisibleToUser && isExist) {
            updataDB();
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                //                mediaPlayer.release();//释放资源
            }
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    private void updataDB() {
        QuestionDB questionDB = mDbUtil.queryAnswer(mChoice.id);
        //        mChoice.selectedAnswer = mFragmentChoiceEdt.getText().toString();
        //        mChoice.selectedAnswer = "";
        String[] answers = mChoice.answer.split(mAnswerSpilt);
        int currentEditIndex = 0;
        for (int i = 0; i < answers.length; i++) {
            if (answers[i].contains(mAnswerPicTag)) {
                mChoice.selectedAnswer += "图图图||";
                return;
            }

            //            if (i == 0) {
            //                mChoice.selectedAnswer = mEditTextList.get(currentEditIndex).getText().toString() + "||";
            //            } else if (i == mEditTextList.size() - 1) {
            //                mChoice.selectedAnswer += mEditTextList.get(currentEditIndex).getText().toString();
            //            } else {
            //                mChoice.selectedAnswer += mEditTextList.get(currentEditIndex).getText().toString() + "||";
            //            }
            if (mEditTextList.size() > currentEditIndex) {
                mChoice.selectedAnswer += mEditTextList.get(currentEditIndex).getText().toString() + "||";
                currentEditIndex++;
            }
        }

        if (mChoice.selectedAnswer != null) {
            String answer = mChoice.selectedAnswer.replaceAll("：", ":");
            if (questionDB == null) {
                mDbUtil.insertAnswerData(mChoice.id, answer);
            } else {
                mDbUtil.updateAnswer(mChoice.id, answer);
            }
        }
    }


    class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //            mChoice.selectedAnswer = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
