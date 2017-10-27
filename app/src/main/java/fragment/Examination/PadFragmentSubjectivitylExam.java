package fragment.Examination;

import bean.QuestionDB;
import conf.Constants;
import fragment.base.PadFragmentBase;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-25 11:13
 * @des ${主观填空题 ,没有标准答案}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class PadFragmentSubjectivitylExam extends PadFragmentBase {
    @Override
    public void initExplainationView() {

    }

    @Override
    public void updateDb() {
        mAnswer.selectedAnswer = "";
        for (int i = 0; i < mEditTextList.size(); i++) {
            String answer = mEditTextList.get(i).getText().toString().trim();
            //            if (TextUtils.isEmpty(answer))
            //                answer = "null";
            if (i == mEditTextList.size() - 1) {
                mAnswer.selectedAnswer += answer;
            } else {
                mAnswer.selectedAnswer += answer + "||";
            }
        }

        if (mAnswer.selectedAnswer != null) {
            String answer = mAnswer.selectedAnswer.replaceAll("：", ":");
            QuestionDB questionDB = getAnswerData();
            if (questionDB == null) {
                mDbUtil.insertAnswerData(mAnswer.id, answer);
            } else {
                mDbUtil.updateAnswer(mAnswer.id, answer);
            }
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
