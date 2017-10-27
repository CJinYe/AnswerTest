package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.icox.exercises.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import activity.PicActivity;
import conf.Constants;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-13 15:25
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class PadNoteAnswerAdapter extends BaseAdapter {
    private final Context mContext;
    private final String[] mNormAnswer;
    private String[] answers;

    public PadNoteAnswerAdapter(Context contexts, String[] answers, String[] answer) {
        mContext = contexts;
        mNormAnswer = answer;
        this.answers = answers;
    }

    @Override
    public int getCount() {
        if (mNormAnswer != null) {
            return mNormAnswer.length;
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_pad_note_answer, null);
            holder.ivAnswer = (ImageView) convertView.findViewById(R.id.item_pad_note_answer_iv);
            holder.tvAnswer = (TextView) convertView.findViewById(R.id.item_pad_note_answer_tv_edt);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.item_pad_note_answer_tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTitle.setText("第" + (position + 1) + "空：");

        if (answers != null && answers.length > position) {
            //如果答案是包含地址的
            if (answers[position].contains(Constants.PadNoteAnswerPathTag)) {
                holder.tvAnswer.setVisibility(View.GONE);
                holder.ivAnswer.setVisibility(View.VISIBLE);
                String path = answers[position].replaceAll(Constants.PadNoteAnswerPathTag, "");
                Picasso.with(mContext)
                        .load(new File(path))
                        .into(holder.ivAnswer);
            } else {
                if (!answers[position].equals("图图图")) {
                    holder.tvAnswer.setVisibility(View.VISIBLE);
                    holder.ivAnswer.setVisibility(View.GONE);
                    holder.tvAnswer.setText(answers[position]);
                }
            }

            holder.ivAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PicActivity.class);
                    intent.putExtra("path", answers[position].replaceAll(Constants.PadNoteAnswerPathTag, ""));
                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvAnswer;
        ImageView ivAnswer;
    }
}
