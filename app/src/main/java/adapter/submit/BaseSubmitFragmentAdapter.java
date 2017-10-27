package adapter.submit;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icox.exercises.R;


/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-25 14:15
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class BaseSubmitFragmentAdapter extends BaseAdapter {
    public Context mContext;
    public BaseSubmitFragmentAdapter(Context context){
        mContext = context;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return setCount();
    }

    protected abstract int setCount();

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_submit,null);
            holder.item = (RelativeLayout) convertView.findViewById(R.id.item_submit);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.item_submit_tv_title);
            holder.tvId = (TextView) convertView.findViewById(R.id.item_submit_tv_id);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        initView(holder,position);
        return convertView;
    }

    protected abstract void initView(ViewHolder holder, int position);

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
    public class ViewHolder{
        public RelativeLayout item;
        public TextView tvTitle;
        public TextView tvId;
    }
}
