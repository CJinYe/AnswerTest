package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-3-30 11:47
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class GameViewSurface extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    /**
     * 是否处于绘制状态
     */
    private boolean mIsDrawing;
    /**
     * 帮助类
     */
    private SurfaceHolder mHolder;
    /**
     * 画布
     */
    private Canvas mCanvas;
    /**
     * 路径
     */
    private Path mPath;
    /**
     * 画笔
     */
    private Paint mPaint;
    private float startX;
    private float startY;
    private Bitmap mBitmap;

    public GameViewSurface(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(0,0);
    }

    public GameViewSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(0,0);
    }

    public GameViewSurface(Context context,int x,int y) {
        super(context);
        initView(x,y);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x=(int) event.getX();
        int y=(int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                startX = x;
                startY = y;
                break;
            case MotionEvent.ACTION_MOVE:
//                mPath.lineTo(x, y);

                float endX = (startX + x) / 2;
                float endY = (startY + y) / 2;
                mPath.quadTo(startX, startY, endX, endY);
                startX = x;
                startY = y;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }

        return true;
    }

    private void initView(int x, int y) {
        mBitmap = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.WHITE);
        mHolder=getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        mPath=new Path();

        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

//        mCanvas = new Canvas(mBitmap);
//        mCanvas.drawColor(Color.WHITE);

    }

    @Override
    public void run() {
        long start =System.currentTimeMillis();
        while(mIsDrawing){
            draw();
        }
        long end =System.currentTimeMillis();
        if (end-start<100) {
            try {
                Thread.sleep(100-(end-start));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {


    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        mIsDrawing=true;
        mCanvas =mHolder.lockCanvas();
        mCanvas.drawBitmap(mBitmap, 0,0, mPaint);
        mCanvas.drawColor(Color.WHITE);
        mHolder.unlockCanvasAndPost(mCanvas);
        new Thread(this).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        mIsDrawing=false;

    }
    private void draw(){
        try {
            mCanvas=mHolder.lockCanvas();
//            mCanvas.drawBitmap(mBitmap,0,0,null);
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if (mCanvas!=null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }
    /**
     * 清除内容
     */
    public void clean(){
        initView(0,0);
    }
}
