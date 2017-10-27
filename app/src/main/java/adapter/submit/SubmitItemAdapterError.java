package adapter.submit;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import com.icox.exercises.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import bean.ExercisesBean;
import bean.QuestionDB;
import conf.Constants;
import db.DBUtil;
import utils.SpUtils;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-25 14:52
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class SubmitItemAdapterError extends BaseSubmitFragmentAdapter {
    private List<ExercisesBean> mChoiceList;
    private final DBUtil mDbUtil;
    private ViewHolder mHolder;
    private boolean mIsExplain;

    public SubmitItemAdapterError(Context context, List<ExercisesBean> list) {
        super(context);
        mChoiceList = list;
        mDbUtil = new DBUtil(context);
        SpUtils spUtils = new SpUtils(context);
        mIsExplain = spUtils.getBoolean(Constants.isErrorExplain, false);
    }

    @Override
    protected int setCount() {
        return mChoiceList.size();
    }

    @Override
    public void initView(ViewHolder holder, int position) {
        mHolder = holder;
        boolean isAnswer = false;
        ExercisesBean choice = mChoiceList.get(position);
        QuestionDB db = mDbUtil.queryErrorAnswer(choice.indexID);
        holder.tvId.setText("" + (position+1));
        if (choice.selectedAnswer == null
                || TextUtils.isEmpty(choice.selectedAnswer) || db == null || db.userAnswer == null
                || TextUtils.isEmpty(db.userAnswer)
                ) {
            holder.item.setBackgroundResource(R.drawable.submit_bg_normal);
            holder.tvId.setTextColor(Color.BLACK);
        } else {
            holder.item.setBackgroundResource(R.drawable.submit_bg_selector);
            holder.tvId.setTextColor(Color.BLACK);
            isAnswer = true;
        }

        switch (choice.type) {
            case 0://选择题
                holder.tvTitle.setText("选择题");
                break;
            case 1://多选题
                holder.tvTitle.setText("多选题");
                break;
            case 2://判断题
                holder.tvTitle.setText("判断题");
                break;
            case 3://填空题
            case 31://图文填空题
                holder.tvTitle.setText("填空题");

                if (choice.selectedAnswer != null) {
                    String[] strings = choice.selectedAnswer.split("\\|\\|");
                    if (strings.length < 1) {
                        holder.item.setBackgroundResource(R.drawable.submit_bg_normal);
                        holder.tvId.setTextColor(Color.BLACK);
                    } else {
                        boolean isContainsAnswer = false;
                        for (int i = 0; i < strings.length; i++) {
                            if (!strings[i].contains("图图图") && !TextUtils.isEmpty(strings[i].trim())) {
                                isContainsAnswer = true;
                            }
                        }

                        if (!isContainsAnswer) {
                            holder.item.setBackgroundResource(R.drawable.submit_bg_normal);
                            holder.tvId.setTextColor(Color.BLACK);
                        }

                    }
                }

                break;
            case 4://简答题
            case 41://简答题
            case 102:
                holder.tvTitle.setText("简答题");
                break;
            case 101://连线题
                holder.tvTitle.setText("连线题");
                break;

            default:
                break;
        }

        if (db != null && db.userAnswer != null && !TextUtils.isEmpty(db.userAnswer.trim()) && mIsExplain) {
            switch (choice.type) {
                case 0://选择题
                    if (!choice.answer.equals(db.userAnswer)) {
                        holder.item.setBackgroundResource(R.drawable.submit_bg_error);
                    }
                    break;
                case 1://多选题
                    char[] userAnswers = choice.selectedAnswer.toCharArray();
                    for (int i = 0; i < userAnswers.length; i++) {
                        if (!choice.answer.contains(String.valueOf(userAnswers[i]))) {
                            holder.item.setBackgroundResource(R.drawable.submit_bg_error);
                        }
                    }
                    break;
                case 2://判断题
                    if (isAnswer && !choice.answer.equals(choice.selectedAnswer)) {
                        holder.item.setBackgroundResource(R.drawable.submit_bg_error);
                    }
                    break;
                case 3://填空题
                case 31://图文填空题
                    mateAnswer(choice, holder.item);
                    break;
                default:
                    break;
            }
        }


    }


    private void mateAnswer(ExercisesBean choice, RelativeLayout item) {
        Map<Integer, String> answerMap = new HashMap<>();
        String[] answer = choice.answer.split("\\|\\|");
        String[] userAnswer = choice.selectedAnswer.split("\\|\\|");
        for (int i = 0; i < answer.length; i++) {
            if (userAnswer != null && userAnswer.length > i && !TextUtils.isEmpty(userAnswer[i].trim())) {
                String[] answerChilde = answer[i].split("_");
                if (answerChilde.length > 1) {//如果答案有多个选项
                    boolean isYesAnswer = false;
                    for (int j = 0; j < answerChilde.length; j++) {
                        if (answerChilde[j].equals(userAnswer[i])) {
                            isYesAnswer = true;
                        }
                    }
                    if (!isYesAnswer) {
                        //错误
                        item.setBackgroundResource(R.drawable.submit_bg_error);
                    } else {
                        //把多空不规则排序的答案存起来,做相同判断
                        answerMap.put(i, userAnswer[i]);
                    }

                } else if (!answer[i].equals(userAnswer[i])) {
                    //错误
                    item.setBackgroundResource(R.drawable.submit_bg_error);
                }
            } else {
                //错误
                //                item.setBackgroundResource(R.drawable.submit_bg_error);
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
                            //错误
                            item.setBackgroundResource(R.drawable.submit_bg_error);
                            //错的匹配起来,到时候不在做判断
                            errorMap.put(key2, value2);
                        }
                    }
                }
            }
        }
    }

}
