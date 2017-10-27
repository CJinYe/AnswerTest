package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.icox.exercises.R;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-22 17:43
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class SlideLeftRightButton extends View {

    private int mBackgroundWidth;
    private Bitmap mBackgroundBitmap;
    private Bitmap mSlideButton;
    private int mSlideWidth;
    private int mButtonLeft;
    private Paint mPaint;
    private float mDownX;
    private SlideListener mSlideListener;
    private int mMaxLeft;

    public SlideLeftRightButton(Context context) {
        super(context);
        initView();
    }

    public SlideLeftRightButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SlideLeftRightButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mBackgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button_background);
        mSlideButton = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
        mBackgroundWidth = mBackgroundBitmap.getWidth();
        mSlideWidth = mSlideButton.getWidth();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mMaxLeft = mBackgroundWidth - mSlideWidth;
        mButtonLeft = (mBackgroundBitmap.getWidth() / 2) - (mSlideButton.getWidth() / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mBackgroundBitmap.getWidth(), mBackgroundBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBackgroundBitmap, 0, 0, mPaint);
        canvas.drawBitmap(mSlideButton, mButtonLeft, 0, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mButtonLeft = (int) event.getX()-mSlideWidth/2;
                break;
            case MotionEvent.ACTION_UP:
                if (mButtonLeft >= mMaxLeft) {
                    //滑到最右
                    mSlideListener.RightSlide();
                } else if (mButtonLeft <= 0) {
                    //滑到最左边
                    mSlideListener.LeftSlide();
                } else {
                    //回到原点
                    mButtonLeft = (mBackgroundWidth / 2) - (mSlideWidth / 2);
                }
                break;
        }
        mButtonLeft = (mButtonLeft > 0) ? mButtonLeft : 0;
        mButtonLeft = (mButtonLeft < mMaxLeft) ? mButtonLeft : mMaxLeft;
        invalidate();
        return true;
    }

    public void resetView(){
        mButtonLeft = (mBackgroundWidth / 2) - (mSlideWidth / 2);
        invalidate();
    }

    public void setSlideListener(SlideListener slideListener) {
        mSlideListener = slideListener;
    }

    public interface SlideListener {
        abstract void LeftSlide();

        abstract void RightSlide();
    }
}
