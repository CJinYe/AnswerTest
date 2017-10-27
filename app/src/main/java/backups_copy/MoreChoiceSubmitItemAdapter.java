package backups_copy;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.icox.exercises.R;

import java.util.List;

import adapter.submit.BaseSubmitFragmentAdapter;
import bean.QuestionMoreChoice;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-25 14:52
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class MoreChoiceSubmitItemAdapter extends BaseSubmitFragmentAdapter {
    private List<QuestionMoreChoice> mChoiceList;

    public MoreChoiceSubmitItemAdapter(Context context, List<QuestionMoreChoice> list) {
        super(context);
        mChoiceList = list;
    }

    @Override
    protected int setCount() {
        return mChoiceList.size();
    }

    @Override
    public void initView(ViewHolder holder, int position) {
        QuestionMoreChoice choice = mChoiceList.get(position);
        holder.tvId.setText("  "+choice.idMoreChoice + "  ");
        if (choice.selectedMoreAnswer == null || TextUtils.isEmpty(choice.selectedMoreAnswer)) {
            holder.item.setBackgroundResource(R.drawable.shape_submit_item_normal);
            holder.tvId.setTextColor(Color.BLACK);
        } else {
            holder.item.setBackgroundResource(R.drawable.shape_submit_item_selector);
            holder.tvId.setTextColor(Color.BLUE);
        }
    }
}
