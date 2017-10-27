package fragment.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.icox.exercises.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import activity.MainActivity;
import activity.NotePageActivity;
import bean.EvenBusAnswerBean;
import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import fragment.BaseFragment;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 15:28
 * @des ${作答题}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class AnswerFragmentBase extends BaseFragment {

    @InjectView(R.id.fragment_answer_mv_question)
    TextView mMvQuestion;
    @InjectView(R.id.fragment_answer_mv_result)
    TextView mMvResult;
    @InjectView(R.id.fragment_answer_bt)
    ImageButton mFragmentAnswerBt;
    @InjectView(R.id.fragment_answer_iv_answer)
    ImageView mFragmentAnswerIvAnswer;

    private String TAG = "AnswerFragmentExam";

    private View mView;
    private String mImgPath;
    private QuestionDB mQuestionDB;

    @Override
    public View initBaseView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_answer, container, false);
        ButterKnife.inject(this, mView);
        EventBus.getDefault().register(this);
        initView();
        initListener();
        return mView;
    }

    private void initListener() {
        mFragmentAnswerBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQuestionDB != null) {
                    if (!TextUtils.isEmpty(mQuestionDB.userAnswer)) {
                        mImgPath = mQuestionDB.userAnswer;
                        mAnswer.selectedAnswer = mImgPath;
                    }
                }
                Intent intent = new Intent(getActivity(), NotePageActivity.class);
                intent.putExtra("ImgPath", mImgPath);
                intent.putExtra("idAnswer", mAnswer.id);
                intent.putExtra("FilePath", MainActivity.mFilePath);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        mMvQuestion.setMovementMethod(LinkMovementMethod.getInstance());
        mMvResult.setMovementMethod(LinkMovementMethod.getInstance());
        if (mAnswer != null) {
            Spanned spanned = processSpanned(mAnswer.subject);
            mMvQuestion.setText(spanned);

            Spanned spannedResult = processSpanned(mAnswer.analysis);
            mMvResult.setText(spannedResult);

            mQuestionDB = getAnswerData();
        }
        if (mQuestionDB != null && mQuestionDB.userAnswer != null && !TextUtils.isEmpty(mQuestionDB.userAnswer)) {
            mAnswer.selectedAnswer = mQuestionDB.userAnswer;
            mFragmentAnswerBt.setImageResource(R.drawable.button_answer_note_pressed);
        } else {
            mFragmentAnswerBt.setImageResource(R.drawable.button_answer_note_normal);
        }

        if (mIsExplain) {
            mFragmentAnswerBt.setVisibility(View.GONE);
            mMvResult.setVisibility(View.VISIBLE);
            mFragmentAnswerIvAnswer.setVisibility(View.VISIBLE);
            if (mQuestionDB != null && mQuestionDB.userAnswer != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(mQuestionDB.userAnswer);
                mFragmentAnswerIvAnswer.setImageBitmap(bitmap);
            }
        } else {
            mFragmentAnswerBt.setVisibility(View.VISIBLE);
            mMvResult.setVisibility(View.GONE);
            mFragmentAnswerIvAnswer.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取用户的答案
     *
     * @return
     */
    public abstract QuestionDB getAnswerData();

    @Subscribe
    public void onMessage(final EvenBusAnswerBean busAnswerBean) {
        mImgPath = busAnswerBean.path;
        mFragmentAnswerBt.setImageResource(R.drawable.button_answer_note_pressed);
    }
}
