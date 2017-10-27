package fragment.ErrorExam;

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
public class LigatureFragmentError extends LigatureFragmentBase {
    @Override
    public void updateDb() {
        mDbUtil.updateErrorAnswer(mAnswer.indexID, lineSelector);
    }

    @Override
    public QuestionDB getAnswerData() {
        return mDbUtil.queryErrorAnswer(mAnswer.indexID);
    }

    @Override
    public boolean getSpUtilIsExplain() {
        return mSpUtils.getBoolean(Constants.isErrorExplain,false);
    }
}