package fragment.base;

import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.icox.exercises.R;

import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import fragment.BaseFragment;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 15:28
 * @des ${纯文本作答题}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class BaseAnswerTextFragment extends BaseFragment {


    @InjectView(R.id.fragment_answer_tv_question)
    TextView mMvQuestion;
    @InjectView(R.id.fragment_answer_edt_answer)
    public EditText mEdtAnswer;
    @InjectView(R.id.fragment_filling_mv_result)
    TextView mMvResult;

    private View mView;
    public QuestionDB mQuestionDB;

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
            Spanned spanned = processSpanned(mAnswer.subject);
            mMvQuestion.setText(spanned);
            Log.i("tests","subject = "+mAnswer.subject);
            Log.i("tests","spanned = "+spanned);

            Spanned spannedResult = processSpanned(mAnswer.analysis);
            mMvResult.setText(spannedResult);

            mQuestionDB = getAnswerData();
        }
        if (mQuestionDB != null) {
            mAnswer.selectedAnswer = mQuestionDB.userAnswer;
            if ( mAnswer.selectedAnswer != null) {
                mEdtAnswer.setText( mAnswer.selectedAnswer);
            }
        }
            Log.i("mytest","mAnswer.id = "+mAnswer.id+" ,  mAnswer = "+mAnswer.selectedAnswer);

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

    /**
     * 把答案保存到数据库
     */
    public abstract void updateDb();

    /**
     * 查询已经作答的答案
     * @return 答案bean
     */
    public abstract QuestionDB getAnswerData();

//    private void updateDb(){
//        String text = mEdtAnswer.getText().toString().trim();
//        if (mQuestionDB!=null){
//            mDbUtil.insertAnswerData(mAnswer.id,text);
//        }else {
//            mDbUtil.updateAnswer(mAnswer.id,text);
//        }
//    }

}