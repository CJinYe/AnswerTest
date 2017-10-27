package fragment.base;

import android.view.View;
import android.widget.EditText;

import com.icox.exercises.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import bean.QuestionDB;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 14:10
 * @des 填空题
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class PadFragmentNormalBase extends PadFragmentBase {

    /**
     * 答案匹配
     */
    @Override
    public void initExplainationView() {
        mFragmentChoiceEdt.setFocusable(false);
        mFragmentChoiceMvResult.setVisibility(View.VISIBLE);
        Map<Integer, String> answerMap = new HashMap<>();
        String[] answer = mAnswer.answer.split("\\|\\|");
        for (int i = 0; i < answer.length; i++) {
            if (mAnswers != null && mAnswers.length > i) {
                String[] answerChilde = answer[i].split("_");
                //如果答案有多个选项
                if (answerChilde.length > 1) {
                    boolean isYesAnswer = false;
                    for (int j = 0; j < answerChilde.length; j++) {
                        if (answerChilde[j].equals(mAnswers[i])) {
                            isYesAnswer = true;
                        }
                    }
                    if (!isYesAnswer) {
                        //                        mEditTextList.get(i).setTextColor(Color.RED);
                        mEditTextList.get(i).setBackgroundResource(R.drawable.shape_answer_explain_error);
                        mFragmentChoiceMvResult.setTextColor(getResources().getColor(R.color.AnswerRed));
                    } else {
                        //把多空不规则排序的答案存起来,做相同判断
                        answerMap.put(i, mAnswers[i]);
                    }

                } else if (!answer[i].equals(mAnswers[i])) {
                    //                        mEditTextList.get(i).setTextColor(Color.RED);
                    mFragmentChoiceMvResult.setTextColor(getResources().getColor(R.color.AnswerRed));
                    mEditTextList.get(i).setBackgroundResource(R.drawable.shape_answer_explain_error);
                }
            } else {
                mFragmentChoiceMvResult.setTextColor(getResources().getColor(R.color.AnswerRed));
                //                        mEditTextList.get(i).setTextColor(Color.RED);
                mEditTextList.get(i).setBackgroundResource(R.drawable.shape_answer_explain_error);
            }
        }

        Map<Integer, String> errorMap = new HashMap<>();

        //如果几个空的答案排序不一的,如果两个以上相同的,则只有一个是对的
        if (answerMap.size() > 1) {
            Iterator<Map.Entry<Integer, String>> iterator = answerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> entry = iterator.next();
                int key = entry.getKey();
                String value = entry.getValue();

                Iterator<Map.Entry<Integer, String>> iterator2 = answerMap.entrySet().iterator();
                while (iterator2.hasNext()) {
                    Map.Entry<Integer, String> entry1 = iterator2.next();
                    int key2 = entry1.getKey();
                    String value2 = entry1.getValue();
                    //已经存起来的相同答案,如果有则不做判断
                    String error = errorMap.get(key);
                    if (error == null && !value2.equals(error) && key != key2) {
                        if (value.equals(value2)) {
                            mEditTextList.get(key2).setBackgroundResource(R.drawable.shape_answer_explain_error);
                            mFragmentChoiceMvResult.setTextColor(getResources().getColor(R.color.AnswerRed));
                            //                        mEditTextList.get(key2).setTextColor(Color.RED);
                            //错的匹配起来,到时候不在做判断
                            errorMap.put(key2, value2);
                        }
                    }
                }
            }
        }

        for (EditText editText : mEditTextList) {
            editText.setFocusable(false);
        }
    }

    public abstract void updateDb();

    public abstract QuestionDB getAnswerData();
}
