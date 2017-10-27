package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.icox.exercises.R;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-8-21 16:48
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class SpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mSpinners;

    public SpinnerAdapter(Context context, String[] spinners) {
        mContext = context;
        mSpinners = spinners;
    }

    @Override
    public int getCount() {
        if (mSpinners != null)
            return mSpinners.length;
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holde;
        if (convertView == null) {
            holde = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_spinner, parent, false);
            holde.mTextView = (TextView) convertView.findViewById(R.id.item_spinner_tv_title);
            convertView.setTag(holde);
        } else {
            holde = (ViewHolder) convertView.getTag();
        }

        holde.mTextView.setText(mSpinners[position]);
        return convertView;
    }

    class ViewHolder {
        TextView mTextView;
    }
}
