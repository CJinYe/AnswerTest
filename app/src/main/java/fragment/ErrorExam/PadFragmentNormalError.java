package fragment.ErrorExam;

import bean.QuestionDB;
import conf.Constants;
import fragment.base.PadFragmentNormalBase;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-25 11:13
 * @des ${客观填空题 ,标准答案}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class PadFragmentNormalError extends PadFragmentNormalBase {

    @Override
    public void updateDb() {
        mAnswer.selectedAnswer = "";
        for (int i = 0; i < mEditTextList.size(); i++) {
            String answer = mEditTextList.get(i).getText().toString().trim();
            if (i == mEditTextList.size() - 1) {
                mAnswer.selectedAnswer += answer;
            } else {
                mAnswer.selectedAnswer += answer + "||";
            }
        }

        if (mAnswer.selectedAnswer != null) {
            String answer = mAnswer.selectedAnswer.replaceAll("：", ":");
            mDbUtil.updateErrorAnswer(mAnswer.indexID, answer);
        }
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
