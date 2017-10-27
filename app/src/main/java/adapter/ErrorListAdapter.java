package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icox.exercises.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import bean.ExercisesBean;
import db.DBUtil;
import listener.ErrorItemListener;
import utils.DateTimeUtil;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-24 14:23
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class ErrorListAdapter extends RecyclerView.Adapter<ErrorListAdapter.MyViewHolder> {

    private final DBUtil mDbUtil;
    private final ArrayList<ExercisesBean> mMainBeanList;
    private String mSelecotr;
    public ArrayList<ExercisesBean> mBeanList;
    private String[] spinnerDate;

    public ErrorListAdapter(ArrayList<ExercisesBean> beanList, DBUtil dbUtil, String selector) {
        mMainBeanList = beanList;
        mBeanList = beanList;
        mDbUtil = dbUtil;
        mSelecotr = selector;

        if (selector != null && !selector.contains("全部")) {
            ArrayList<ExercisesBean> list = new ArrayList<>();
            for (ExercisesBean bean : mMainBeanList) {
                if (bean.ExaminationName.contains(selector))
                    list.add(bean);
            }
            mBeanList = list;
        }
    }

    public void setSelector(String spinnerSelector, String selectorDate) {
        mSelecotr = spinnerSelector;
        if (spinnerSelector != null && !spinnerSelector.contains("全部")) {
            ArrayList<ExercisesBean> list = new ArrayList<>();
            for (ExercisesBean bean : mMainBeanList) {
                if (bean.ExaminationName.contains(spinnerSelector))
                    list.add(bean);
            }
            mBeanList = list;
        } else {
            mBeanList = mMainBeanList;
        }

        if (selectorDate != null && !selectorDate.contains("全部")) {
            ArrayList<ExercisesBean> list = new ArrayList<>();
            switch (selectorDate) {
                case "一天内":
                    for (ExercisesBean bean : mBeanList) {
                        Date date = new Date(bean.addTime);
                        if (DateTimeUtil.isToday(date))
                            list.add(bean);
                    }
                    break;
                case "一周内":
                    for (ExercisesBean bean : mBeanList) {
                        Date date = new Date(bean.addTime);
                        if (DateTimeUtil.isWeek(date))
                            list.add(bean);
                    }
                    break;
                case "一个月内":
                    for (ExercisesBean bean : mBeanList) {
                        Date date = new Date(bean.addTime);
                        if (DateTimeUtil.isMonth(date))
                            list.add(bean);
                    }
                    break;
                case "一年内":
                    for (ExercisesBean bean : mBeanList) {
                        Date date = new Date(bean.addTime);
                        if (DateTimeUtil.isYear(date))
                            list.add(bean);
                    }
                    break;

                default:
                    break;
            }
            mBeanList = list;

        }
        notifyDataSetChanged();
    }

    public void setBeanList(ArrayList<ExercisesBean> list) {
        if (list != null) {
            mBeanList = list;
        }
        notifyDataSetChanged();
    }

    public void deleteBean() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < mBeanList.size(); i++) {
            ExercisesBean bean = mBeanList.get(i);
            if (bean.isChecked) {
                mDbUtil.removeCheckErrorExamination(bean.ExaminationName, bean.unit, bean.id);
                list.add(i);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            mBeanList.remove(list.get(i));
        }
        notifyDataSetChanged();
    }

    public void checkAll() {
        for (ExercisesBean bean : mBeanList) {
            bean.isChecked = true;
        }
    }

    public void cancelCheckAll() {
        for (ExercisesBean bean : mBeanList) {
            bean.isChecked = false;
        }
    }

    public ArrayList<ExercisesBean> getBeanList() {
        return mBeanList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_error, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        if (mBeanList != null) {
            return mBeanList.size();
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ExercisesBean bean = mBeanList.get(position);
        holder.setData(bean);
        holder.mMainLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.isChecked = !bean.isChecked;
                if (mErrorItemListener != null)
                    mErrorItemListener.errorItemListener(position);
                notifyItemChanged(position);
            }
        });

        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.isChecked = !bean.isChecked;
                if (mErrorItemListener != null)
                    mErrorItemListener.errorItemListener(position);
                notifyItemChanged(position);
            }
        });

        holder.mMainLl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mErrorItemListener != null)
                    mErrorItemListener.errorItemLongListener(position);
                return true;
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTvNumber;
        TextView mTvUnit;
        TextView mTvTime;
        CheckBox mCheckBox;
        TextView mTvTitle;
        LinearLayout mMainLl;

        MyViewHolder(View itemView) {
            super(itemView);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.item_error_checkBox);
            mTvTitle = (TextView) itemView.findViewById(R.id.item_error_tv_title);
            mTvTime = (TextView) itemView.findViewById(R.id.item_error_tv_time);
            mTvUnit = (TextView) itemView.findViewById(R.id.item_error_tv_unit);
            mTvNumber = (TextView) itemView.findViewById(R.id.item_error_tv_number);
            mMainLl = (LinearLayout) itemView.findViewById(R.id.item_error_main_Ll);
        }

        public void setData(Object object) {
            ExercisesBean bean = (ExercisesBean) object;
            mCheckBox.setChecked(bean.isChecked);
            mTvTitle.setText(bean.ExaminationName);
            String time = "";
            try {
                time = DateTimeUtil.longToString(bean.addTime, "yyyy年MM月dd日HH:mm:ss");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mTvTime.setText("加入时间 : " + time);

            //            mTvUnit.setText("课本单元 : "+bean.unit.replaceAll(".xml",""));
            mTvUnit.setText("课本单元 : " + bean.name);
            mTvNumber.setText(" 第" + bean.id + "题");
        }
    }

    private ErrorItemListener mErrorItemListener;

    public void setOnItemListener(ErrorItemListener itemListener) {
        mErrorItemListener = itemListener;
    }
}
