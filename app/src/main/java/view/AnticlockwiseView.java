package view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Chronometer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-22 14:54
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
@SuppressLint({ "ViewConstructor", "SimpleDateFormat" })
public class AnticlockwiseView extends Chronometer {

    public AnticlockwiseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO 自动生成的构造函数存根
        mTimeFormat = new SimpleDateFormat("mm:ss");
        mTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
    }

    private long mTime;
    private long mNextTime;
    private OnTimeCompleteListener mListener;
    private SimpleDateFormat mTimeFormat;

    public AnticlockwiseView(Context context) {
        super(context);

    }

    /**
     * 重新启动计时
     */
    public void reStart(long _time_s) {
        if (_time_s == -1)
        {
            mNextTime = mTime;
        } else
        {
            mTime = mNextTime = _time_s;
        }
        this.start();
    }

    public void reStart() {
        reStart(-1);
    }

    /**
     * 继续计时
     */
    public void onResume() {
        this.start();
    }

    /**
     * 暂停计时
     */
    public void onPause() {
        this.stop();
    }

    /**
     * 设置时间格式
     *
     * @param pattern
     *            计时格式
     */
    public void setTimeFormat(String pattern) {
        mTimeFormat = new SimpleDateFormat(pattern);
        mTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
    }

    public void setOnTimeCompleteListener(OnTimeCompleteListener l) {
        this.setOnChronometerTickListener(listener);
        mListener = l;
    }

    OnChronometerTickListener listener = new OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            if (mNextTime <= 0) {
                if (mNextTime == 0) {
                    AnticlockwiseView.this.stop();
                    if (null != mListener)
                        mListener.onTimeComplete();
                }
                mNextTime = 0;
                updateTimeText();
                return;
            }

            mNextTime--;

            updateTimeText();
        }
    };

    /**
     * 初始化时间
     * @param _time_s
     */
    public void initTime(long _time_s) {
        mTime = mNextTime = _time_s;
        updateTimeText();
    }

    private void updateTimeText() {
        this.setText(mTimeFormat.format(new Date(mNextTime * 1000)));
//        this.setText(mTimeFormat.format(new Date(mNextTime)));
    }

    public interface OnTimeCompleteListener {
        void onTimeComplete();
    }

}

