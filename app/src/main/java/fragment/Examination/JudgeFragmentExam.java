package fragment.Examination;

import bean.QuestionDB;
import conf.Constants;
import fragment.base.JudgeFragmentBase;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-25 11:00
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class JudgeFragmentExam extends JudgeFragmentBase {
    @Override
    public void updateDb() {
        if (getAnswerData() == null) {
            mDbUtil.insertAnswerData(mAnswer.id, mAnswer.selectedAnswer);
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
