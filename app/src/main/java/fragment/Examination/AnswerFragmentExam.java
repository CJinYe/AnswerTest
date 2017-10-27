package fragment.Examination;

import bean.QuestionDB;
import conf.Constants;
import fragment.base.AnswerFragmentBase;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-25 10:55
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class AnswerFragmentExam extends AnswerFragmentBase {
    @Override
    public QuestionDB getAnswerData() {
        return mDbUtil.queryAnswer(mAnswer.id);
    }

    @Override
    public boolean getSpUtilIsExplain() {
        return mSpUtils.getBoolean(Constants.isExplain,false);
    }
}
