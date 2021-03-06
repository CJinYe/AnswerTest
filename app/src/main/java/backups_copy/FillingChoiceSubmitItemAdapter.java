package backups_copy;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.icox.exercises.R;

import java.util.List;

import adapter.submit.BaseSubmitFragmentAdapter;
import bean.QuestionDB;
import bean.QuestionFilling;
import db.DBUtil;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-25 14:52
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class FillingChoiceSubmitItemAdapter extends BaseSubmitFragmentAdapter {
    private List<QuestionFilling> mChoiceList;
    private final DBUtil mDbUtil;

    public FillingChoiceSubmitItemAdapter(Context context, List<QuestionFilling> list) {
        super(context);
        mChoiceList = list;
        mDbUtil = new DBUtil(context);
    }

    @Override
    protected int setCount() {
        return mChoiceList.size();
    }

    @Override
    public void initView(ViewHolder holder, int position) {
        QuestionFilling choice = mChoiceList.get(position);
        holder.tvId.setText("  "+choice.idFilling + "  ");
        if (mDbUtil != null) {
            QuestionDB q = mDbUtil.queryAnswer(choice.idFilling);
            if (q != null) {
                String[] answers = q.userAnswer.split(":");
                for (int i = 0; i < answers.length; i++) {
                    String answer = answers[i].trim();
                    if (TextUtils.isEmpty(answer)){
                        holder.item.setBackgroundResource(R.drawable.shape_submit_item_normal);
                        holder.tvId.setTextColor(Color.BLACK);
                    }else {
                        holder.item.setBackgroundResource(R.drawable.shape_submit_item_selector);
                        holder.tvId.setTextColor(Color.BLUE);
                    }
                }
            }
        }
//        if (choice.selectedAnswer == null || TextUtils.isEmpty(choice.selectedAnswer)) {
//            holder.item.setBackgroundResource(R.drawable.shape_submit_item_normal);
//            holder.tv.setTextColor(Color.BLACK);
//        } else {
//            holder.item.setBackgroundResource(R.drawable.shape_submit_item_selector);
//            holder.tv.setTextColor(Color.BLUE);
//        }
    }
}
