package backups_copy;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.icox.exercises.R;

import java.util.List;

import adapter.submit.BaseSubmitFragmentAdapter;
import bean.QuestionChoice;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-25 14:52
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class ChoiceSubmitItemAdapter extends BaseSubmitFragmentAdapter {
    private List<QuestionChoice> mChoiceList;

    public ChoiceSubmitItemAdapter(Context context, List<QuestionChoice> list) {
        super(context);
        mChoiceList = list;
    }

    @Override
    protected int setCount() {
        return mChoiceList.size();
    }

    @Override
    public void initView(ViewHolder holder, int position) {
        QuestionChoice choice = mChoiceList.get(position);
        holder.tvId.setText("  "+choice.idChoice + "  ");
        if (choice.selectedAnswer == null || TextUtils.isEmpty(choice.selectedAnswer)) {
            holder.item.setBackgroundResource(R.drawable.shape_submit_item_normal);
            holder.tvId.setTextColor(Color.BLACK);
        } else {
            holder.item.setBackgroundResource(R.drawable.shape_submit_item_selector);
            holder.tvId.setTextColor(Color.BLUE);
        }
    }
}
