package fragment.Examination;

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
public class AnswerTextFragmentExam extends BaseAnswerTextFragment {
    @Override
    public void updateDb() {
        String text = mEdtAnswer.getText().toString().trim();
        if (getAnswerData()==null){
            long mun = mDbUtil.insertAnswerData(mAnswer.id,text);
        }else {
            int mun = mDbUtil.updateAnswer(mAnswer.id,text);
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
