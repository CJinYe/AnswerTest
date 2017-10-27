package fragment.Examination;

import android.util.Log;

import bean.QuestionDB;
import conf.Constants;
import fragment.base.MoreChoiceFragmentBase;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-25 11:06
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class MoreChoiceFragmentExam extends MoreChoiceFragmentBase {
    @Override
    public void updateDb() {
        if (mQuestionDB == null) {
            mDbUtil.insertAnswerData(mAnswer.id, mAnswer.selectedAnswer);
            mQuestionDB = mDbUtil.queryAnswer(mAnswer.id);
        } else {
            mDbUtil.updateAnswer(mAnswer.id, mAnswer.selectedAnswer);
        }

    }

    @Override
    public QuestionDB getAnswerData() {
        return mDbUtil.queryAnswer(mAnswer.id);
    }

    @Override
    public boolean getSpUtilIsExplain() {
        return mSpUtils.getBoolean(Constants.isExplain,false);
    }
}
