package fragment.ErrorExam;

import android.widget.EditText;

import bean.QuestionDB;
import conf.Constants;
import fragment.base.PadNoteFragmentBase;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-25 11:17
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class PadNoteFragmentError extends PadNoteFragmentBase {
    @Override
    public void updateDb() {
        String[] answers = mAnswer.answer.split(mAnswerSpilt);
        mAnswer.selectedAnswer = "";
        int currentEditIndex = 0;
        for (int i = 0; i < answers.length; i++) {
            if (answers[i].contains(mAnswerPicTag)) {
                String path = "图图图";
                if (pathMap.get(i) != null) {
                    path = pathMap.get(i);
                }
                if (i == answers.length - 1) {
                    mAnswer.selectedAnswer += path;
                } else {
                    mAnswer.selectedAnswer += (path + "||");
                }
            } else if (mViews.get(i) instanceof EditText) {
                EditText editText = (EditText) mViews.get(i);
                String answer = editText.getText().toString().trim();
                if (i == answer.length() - 1) {
                    mAnswer.selectedAnswer += answer;
                } else {
                    mAnswer.selectedAnswer += (answer + "||");
                }
                currentEditIndex++;
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
