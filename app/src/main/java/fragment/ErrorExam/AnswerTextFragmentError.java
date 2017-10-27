package fragment.ErrorExam;

import bean.QuestionDB;
import conf.Constants;
import fragment.base.BaseAnswerTextFragment;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-25 10:56
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class AnswerTextFragmentError extends BaseAnswerTextFragment {
    @Override
    public void updateDb() {
        String text = mEdtAnswer.getText().toString().trim();
        mDbUtil.updateErrorAnswer(mAnswer.indexID, text);
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
