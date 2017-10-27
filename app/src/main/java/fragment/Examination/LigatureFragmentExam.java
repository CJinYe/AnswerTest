package fragment.Examination;

import bean.QuestionDB;
import conf.Constants;
import fragment.base.LigatureFragmentBase;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-25 11:01
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class LigatureFragmentExam extends LigatureFragmentBase {
    @Override
    public void updateDb() {
        if (mDbUtil.queryAnswer(mAnswer.id) != null) {
            mDbUtil.updateAnswer(mAnswer.id, lineSelector);
        } else {
            mDbUtil.insertAnswerData(mAnswer.id, lineSelector);
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
