package fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iangclifton.android.floatlabel.FloatLabel;
import com.icox.exercises.R;

import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.MainActivity;
import bean.QuestionDB;
import bean.QuestionFilling;
import butterknife.ButterKnife;
import butterknife.InjectView;
import conf.Constants;
import db.DBUtil;
import dialog.PicDiaLog;
import utils.SpUtils;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 15:28
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class FillingFragment extends Fragment {

    @InjectView(R.id.fragment_answer_mv_question)
    TextView mFragmentAnswerMvQuestion;
//    @InjectView(R.id.fragment_filling_ll_answer)
    LinearLayout mFragmentFillingLlAnswer;
    @InjectView(R.id.fragment_filling_mv_result)
    TextView mFragmentFillingMvResult;
    private View mView;
    private QuestionFilling mAnswer;
    private List<String> urlList = new ArrayList<>();
    private DBUtil mDbUtil;
    private QuestionDB mQuestionDB;
    private boolean mIsExplain;
    private SparseArray<FloatLabel> editList = new SparseArray<>();

    private String[] mAnswers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_answer_text, container, false);
        ButterKnife.inject(this, mView);
        mDbUtil = new DBUtil(getActivity());
        SpUtils spUtils = new SpUtils(getActivity());
        mIsExplain = spUtils.getBoolean(Constants.isExplain, false);
        initView();
        initListener();
        return mView;
    }

    private void initListener() {

    }

    private void initView() {
//        mAnswer.subject.split("\n");
        //点击图片放大
//        mFragmentAnswerMvQuestion.setMovementMethod(LinkMovementMethod.getInstance());
//        mFragmentFillingMvResult.setMovementMethod(LinkMovementMethod.getInstance());
        mFragmentAnswerMvQuestion.setText(Html.fromHtml(mAnswer.questionFilling, getImageGetterInstance(), new MyTagHandler()));
        mFragmentFillingMvResult.setText(Html.fromHtml("答案解析:<br/>&#8195;&#8195;" + mAnswer.explainationFilling, getImageGetterInstance(), new MyTagHandler()));

        mQuestionDB = mDbUtil.queryAnswer(mAnswer.idFilling);


        String[] filling = mAnswer.answerFilling.split(":");
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < filling.length; i++) {
            if (editList.get(i) != null) {
                FloatLabel floatLabel = editList.get(i);
                floatLabel.setLabel("第" + (i + 1) + "空");
                floatLabel.getEditText().setHintTextColor(Color.BLUE);
                mFragmentFillingLlAnswer.addView(floatLabel, param);
            } else {
                FloatLabel floatLabel = new FloatLabel(getActivity());
                floatLabel.setLabel("第" + (i + 1) + "空");

                mFragmentFillingLlAnswer.addView(floatLabel, param);
                editList.put(i, floatLabel);
            }
        }

        if (mQuestionDB != null) {
//            QuestionDB q = mDbUtil.queryAnswer(mAnswer.idFilling);
//            if (q != null && editList != null) {
//                mAnswers = q.userAnswer.split(":");
//                for (int i = 0; i < mAnswers.length; i++) {
//                    String answer = mAnswers[i];
//                    //                        editList.get(i).setText(answer.toCharArray(), 0, answer.length());
//                    if (editList.get(i) != null) {
//                        editList.get(i).setText(answer.trim());
//                    }
//                }
//            }
        }

        if (mIsExplain) {
            mFragmentFillingMvResult.setVisibility(View.VISIBLE);
            for (int i = 0; i < editList.size(); i++) {
                editList.get(i).getEditText().setFocusable(false);
            }
        } else {
            mFragmentFillingMvResult.setVisibility(View.GONE);
            for (int i = 0; i < editList.size(); i++) {
                editList.get(i).getEditText().setFocusable(true);
            }
        }

    }


    public void initData(QuestionFilling answer) {
        mAnswer = answer;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        String answer = "";
        if (editList != null) {
            for (int i = 0; i < editList.size(); i++) {
                if (i == 0) {
                    answer = editList.get(i).getEditText().getText().toString() + " ";
                } else {
                    answer = answer + ":" + editList.get(i).getEditText().getText().toString() + " ";
                }
            }
        }
        if (!TextUtils.isEmpty(answer)) {
            if (mQuestionDB == null) {
                mDbUtil.insertAnswerData(mAnswer.idFilling, answer);
            } else {
                mDbUtil.updateAnswer(mAnswer.idFilling, answer);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mDbUtil != null) {
            if (mQuestionDB != null) {
                QuestionDB q = mDbUtil.queryAnswer(mAnswer.idFilling);
                if (q != null && editList != null &&q.userAnswer !=null) {
                    mAnswers = q.userAnswer.split(":");
                    for (int i = 0; i < mAnswers.length; i++) {
                        String answer = mAnswers[i];
                        if (editList.get(i) != null) {
                            editList.get(i).setText(answer.trim());
                        }
                    }
                }
            }
        }
        if (editList != null) {
            for (int i = 0; i < editList.size(); i++) {
                editList.get(i).setLabel("第" + (i + 1) + "空");
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
                    Log.d("queryAnswer", "setUserVisibleHint");
        if (!isVisibleToUser) {

        } else {

        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public class MyTagHandler implements Html.TagHandler {

        @Override
        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

            // 处理标签<img>
            if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
                // 获取长度
                int len = output.length();
                // 获取图片地址
                ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
                String imgURL = images[0].getSource();

                // 使图片可点击并监听点击事件
                output.setSpan(new ClickableImage(getActivity(), imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        private class ClickableImage extends ClickableSpan {

            private String url;
            private Context context;

            public ClickableImage(Context context, String url) {
                this.context = context;
                this.url = MainActivity.mEbagbook.getExtractFile(getActivity(), url);;
            }

            @Override
            public void onClick(View widget) {
                // 进行图片点击之后的处理
                Drawable d = Drawable.createFromPath(url);
                PicDiaLog picDiaLog = new PicDiaLog(getActivity(), d);
                picDiaLog.show();
            }
        }
    }


    /**
     * ImageGetter用于text图文混排
     *
     * @return
     */
    public Html.ImageGetter getImageGetterInstance() {
        Html.ImageGetter imgGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                Drawable d = null;
                int fontH = (int) (getResources().getDimension(
                        R.dimen.textSizeMedium) * 1.8);
                //                int id = Integer.parseInt(source);
                //                Drawable d = getResources().getDrawable(id);
                source = MainActivity.mEbagbook.getExtractFile(getActivity(), source);
                d = Drawable.createFromPath(source);

                if (d != null) {
                    int height = fontH;
                    int width = (int) ((float) d.getIntrinsicWidth() * 1.5 / (float) d
                            .getIntrinsicHeight()) * fontH;
                    //                int width =d.getMinimumWidth();
                    if (width == 0) {
                        width = d.getIntrinsicWidth();
                    }
                    d.setBounds(0, 0, width, height);
                }
                return d;
            }
        };
        return imgGetter;
    }


}
