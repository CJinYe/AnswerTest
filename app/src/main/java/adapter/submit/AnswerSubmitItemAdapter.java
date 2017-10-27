package adapter.submit;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.icox.exercises.R;

import java.util.List;

import bean.ExercisesBean;
import bean.QuestionDB;
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
public class AnswerSubmitItemAdapter extends BaseSubmitFragmentAdapter {
    private List<ExercisesBean> mChoiceList;
    private ViewHolder mHolder;
    private final DBUtil mDbUtil;

    public AnswerSubmitItemAdapter(Context context, List<ExercisesBean> list) {
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
        mHolder = holder;
        ExercisesBean choice = mChoiceList.get(position);
        QuestionDB db = mDbUtil.queryAnswer(choice.id);
        if (choice.id < 10) {
            holder.tvId.setText("  " + choice.id + "  ");
        }else {
            holder.tvId.setText(" " + choice.id + " ");
        }
        if ( db == null || db.userAnswer == null
                || TextUtils.isEmpty(db.userAnswer)
                ) {
            holder.item.setBackgroundResource(R.drawable.shape_submit_item_normal);
            holder.tvId.setTextColor(Color.BLACK);
        } else {
            holder.item.setBackgroundResource(R.drawable.shape_submit_item_selector);
            holder.tvId.setTextColor(Color.BLUE);
        }
    }
}
