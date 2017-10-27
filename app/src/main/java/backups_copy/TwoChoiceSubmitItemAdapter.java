package backups_copy;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.icox.exercises.R;

import java.util.List;

import adapter.submit.BaseSubmitFragmentAdapter;
import bean.QuestionTwoChoice;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-25 14:52
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class TwoChoiceSubmitItemAdapter extends BaseSubmitFragmentAdapter {
    private List<QuestionTwoChoice> mChoiceList;

    public TwoChoiceSubmitItemAdapter(Context context, List<QuestionTwoChoice> list) {
        super(context);
        mChoiceList = list;
    }

    @Override
    protected int setCount() {
        return mChoiceList.size();
    }

    @Override
    public void initView(ViewHolder holder, int position) {
        QuestionTwoChoice choice = mChoiceList.get(position);
        holder.tvId.setText("  "+choice.idTwoChoice + "  ");
        if (choice.selectedTwoAnswer == null || TextUtils.isEmpty(choice.selectedTwoAnswer)) {
            holder.item.setBackgroundResource(R.drawable.shape_submit_item_normal);
            holder.tvId.setTextColor(Color.BLACK);
        } else {
            holder.item.setBackgroundResource(R.drawable.shape_submit_item_selector);
            holder.tvId.setTextColor(Color.BLUE);
        }
    }
}
