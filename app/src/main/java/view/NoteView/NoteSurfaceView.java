package view.NoteView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-3-30 11:04
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class NoteSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {


    /**
     * 控制游戏更新循环
     **/
    boolean mRunning = false;

    /**
     * 控制游戏循环
     **/
    boolean mIsRunning = false;

    /**
     * 每50帧刷新一次屏幕
     **/
    public static final int TIME_IN_FRAME = 50;

    private float paintWidth = 2f;//默认画笔宽度

    private Paint.Style paintStyle = Paint.Style.STROKE;//默认画笔风格

    private int paintAlph = 255;//默认不透明

    private Path mPath;//轨迹

    private Paint mPaint;//画笔
    private Paint mBitmapPaint;// 画布的画笔

    private float startX = 0.0f;//初始x

    private float startY = 0.0f;//初始Y

    private SurfaceHolder surfaceHolder;

    public Canvas mCanvas;

    public boolean first = true;

    private Bitmap mBitmap;
    private Bitmap mInBitmap;

    // 保存Path路径的集合,用List集合来模拟栈
    private static List<DrawPath> savePath;
    // 保存已删除Path路径的集合
    private static List<DrawPath> deletePath;
    private DrawPath dp;

    private int screenWidth, screenHeight;
    private int currentColor = BLACK;
    private int currentSize = 5;
    private int currentStyle = 1;
    private int[] paintColor;//颜色集合

    private class DrawPath {
        public Path path;// 路径
        public Paint paint;// 画笔
    }

    public NoteSurfaceView(Context context, Bitmap bitmapc, int w, int h) {
        super(context, null, 0);
        screenWidth = w;
        screenHeight = h;
        mInBitmap = bitmapc;
        paintColor = new int[]{
                Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, BLACK, Color.GRAY, Color.CYAN, Color.TRANSPARENT
        };
        setLayerType(LAYER_TYPE_SOFTWARE, null);//设置默认样式，去除dis-in的黑色方框以及clear模式的黑线效果
        initCanvas();
        //        mBitmap = bitmapc;
        mBitmap = Bitmap.createBitmap(bitmapc).copy(Bitmap.Config.ARGB_8888, true);

        //        mBitmap.eraseColor(Color.argb(0, 0, 0, 0));
        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
        //        mCanvas.drawColor(Color.WHITE);
        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();


        this.setFocusable(true);//设置当前view拥有触摸事件

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    public NoteSurfaceView(Context context, int w, int h) {
        super(context, null, 0);
        screenWidth = w;
        screenHeight = h;
        paintColor = new int[]{
                Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, BLACK, Color.GRAY, Color.CYAN, Color.TRANSPARENT
        };
        setLayerType(LAYER_TYPE_SOFTWARE, null);//设置默认样式，去除dis-in的黑色方框以及clear模式的黑线效果
        initCanvas();
        //        mBitmap = bitmapc;
        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(Color.argb(0, 0, 0, 0));

        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
        mCanvas.drawColor(Color.WHITE);
        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();


        this.setFocusable(true);//设置当前view拥有触摸事件

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    public void initCanvas() {
        setPaintStyle();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        //画布大小
    }

    //初始化画笔样式
    private void setPaintStyle() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        if (currentStyle == 1) {
            mPaint.setStrokeWidth(currentSize);
            mPaint.setColor(currentColor);
        } else {//橡皮擦
            mPaint.setAlpha(0);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mPaint.setColor(Color.TRANSPARENT);
            mPaint.setStrokeWidth(50);
        }
    }



    @Override
    public void onDraw(Canvas canvas) {
        //canvas.drawColor(0xFFAAAAAA);
        // 将前面已经画过得显示出来
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        if (mPath != null) {
            // 实时的显示
            canvas.drawPath(mPath, mPaint);
        }
    }

    public void doDraw(){
        mCanvas=surfaceHolder.lockCanvas();
        if (mPath!=null){
        mCanvas.drawPath(mPath, mPaint);//绘制
        }
        surfaceHolder.unlockCanvasAndPost(mCanvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //手接触屏幕时触发
                // 每次down下去重新new一个Path
                mPath = new Path();
                //每一次记录的路径对象是不一样的
                dp = new DrawPath();
                dp.path = mPath;
                dp.paint = mPaint;
                mPath.moveTo(x, y);
                startX = x;
                startY = y;
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                //手滑动时触发
                float endX = (startX + x) / 2;
                float endY = (startY + y) / 2;
                mPath.quadTo(startX, startY, endX, endY);
                startX = x;
                startY = y;
                postInvalidate();
                break;

            case MotionEvent.ACTION_UP:
                //手抬起时触发
                mCanvas.drawPath(mPath, mPaint);
                //将一条完整的路径保存下来(相当于入栈操作)
                savePath.add(dp);
                mPath = null;// 重新置空
                break;


            default:
                break;
        }
        return true;
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (mIsRunning) {

            /** 取得更新游戏之前的时间 **/
            long startTime = System.currentTimeMillis();
            /** 在这里加上线程安全锁 **/
            synchronized (surfaceHolder) {
                doDraw();
            }

            /** 取得更新游戏结束的时间 **/
            long endTime = System.currentTimeMillis();

            /** 计算出游戏一次更新的毫秒数 **/
            int diffTime = (int) (endTime - startTime);

            /** 确保每次更新时间为50帧 **/
            while (diffTime <= TIME_IN_FRAME) {
                diffTime = (int) (System.currentTimeMillis() - startTime);
                /** 线程等待 **/
                Thread.yield();
            }

        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        mCanvas = surfaceHolder.lockCanvas();
        //        mCanvas.drawBitmap(mBitmap, 0,0, null);
        surfaceHolder.unlockCanvasAndPost(mCanvas);

        mIsRunning = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        mIsRunning = false;
    }
}
