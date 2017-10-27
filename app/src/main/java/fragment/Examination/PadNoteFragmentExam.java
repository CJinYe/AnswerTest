package fragment.Examination;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class PadNoteFragmentExam extends PadNoteFragmentBase {
    @Override
    public void updateDb() {
        String[] answers = mAnswer.answer.split(mAnswerSpilt);
        mAnswer.selectedAnswer = "";
        if (answers.length > 0) {
            for (int i = 0; i < answers.length; i++) {
                if (answers[i].contains(mAnswerPicTag) || checkStr(answers[i])) {
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
                }
            }
        } else {
            if (pathMap.get(0) != null) {
                mAnswer.selectedAnswer = pathMap.get(0);
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
        return mSpUtils.getBoolean(Constants.isExplain, false);
    }

    private boolean checkStr(String str) {
        Pattern pattern = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
}
