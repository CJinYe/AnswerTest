package fragment;

import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.icox.exercises.R;

import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import utils.MyTagHandler;
import utils.SpannedUtil;

import static utils.ImageGetterInstanceUtil.getImageGetterInstance;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 15:28
 * @des ${纯文本作答题}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class AnswerTextFragment extends BaseFragment {


    @InjectView(R.id.fragment_answer_tv_question)
    TextView mMvQuestion;
    @InjectView(R.id.fragment_answer_edt_answer)
    EditText mEdtAnswer;
    @InjectView(R.id.fragment_filling_mv_result)
    TextView mMvResult;
    private String TAG = "AnswerFragmentExam";

    private View mView;
    private QuestionDB mQuestionDB;

    @Override
    public View initBaseView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.fragment_answer_text, container, false);
        ButterKnife.inject(this, mView);
        initView();
        return mView;
    }


    private void initView() {
        mMvQuestion.setMovementMethod(LinkMovementMethod.getInstance());
        mMvResult.setMovementMethod(LinkMovementMethod.getInstance());
        if (mAnswer != null) {
            Spanned spanned = Html.fromHtml(mAnswer.subject.replaceAll("\n", "<br>"),
                    getImageGetterInstance(getActivity()), new MyTagHandler(getActivity()));
            SpannedUtil.spannedUtil(spanned);
            mMvQuestion.setText(spanned);

            Spanned spannedResult = Html.fromHtml(mAnswer.analysis,
                    getImageGetterInstance(getActivity()), new MyTagHandler(getActivity()));
            SpannedUtil.spannedUtil(spannedResult);
            mMvResult.setText(spanned);

            mQuestionDB = mDbUtil.queryAnswer(mAnswer.id);
        }
        if (mQuestionDB != null) {
            mAnswer.selectedAnswer = mQuestionDB.userAnswer;
            if (mQuestionDB.userAnswer != null) {
                mEdtAnswer.setText(mQuestionDB.userAnswer);
            }
        }

        if (mIsExplain) {
            mMvResult.setVisibility(View.VISIBLE);
            mEdtAnswer.setEnabled(false);
        } else {
            mMvResult.setVisibility(View.GONE);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isVisibleToUser && isExist) {
            updateDb();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        updateDb();
        ButterKnife.reset(this);
    }

    private void updateDb(){
        String text = mEdtAnswer.getText().toString().trim();
        if (mQuestionDB!=null){
            mDbUtil.insertAnswerData(mAnswer.id,text);
        }else {
            mDbUtil.updateAnswer(mAnswer.id,text);
        }
    }

}