package view.NoteView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bean.DrawPath;
import conf.Constants;
import utils.SDcardUtil;


public class NoteViewCopy extends View implements View.OnTouchListener {

    private final String TAG = "TuyaView";
    private Context context;
    public Bitmap mBitmap = null;
    public Bitmap mInBitmap = null;
    public Bitmap mSaveBitmap = null;
    private Canvas mCanvas;
    private Canvas mSaveCanvas;
    private Path mPath;
    private Paint mBitmapPaint;// 画布的画笔
    public Paint mPaint;// 真实的画笔
    private float mX, mY;// 临时点坐标
    private final float TOUCH_TOLERANCE = 2;
    // 保存Path路径的集合,用List集合来模拟栈
    public List<DrawPath> savePath = new ArrayList<>();
    // 保存已删除Path路径的集合
    private List<DrawPath> deletePath = new ArrayList<>();
    // 记录Path路径的对象
    private DrawPath mDrawPath;
    private int screenWidth, screenHeight;
    public int currentColor = Color.BLACK;//画笔颜色
    public int backgroundColor = Color.WHITE;//画布颜色
    public int currentSize = 4;//画笔大小
    public int currentStyle = 1;//画笔类型,1为正常,0为橡皮擦
    private boolean isRedo = false;//是否重做

    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();
    ArrayList<Path> mPaths = new ArrayList<Path>();
    private RelativeLayout.LayoutParams mParams;
    private InputMethodManager mImm;
    private ImageView mEraserView;
    private Bitmap mEraser;
    private long mStartTime;
    private long mEndTime;
    private float startX, startY, endX, endY;
    private Path mPathUp;
    private Path mPathDown;
    private Paint mPaintUp;
    private Paint mPaintDown;
    private float downX;
    private float downY;
    private Activity mNoteActivity;

    //是否更换了背景颜色
    public boolean mIsChangerBackground = false;


    public NoteViewCopy(Context context, Bitmap bitmapc, int w, int h) {
        super(context);
        this.context = context;
        screenWidth = w;
        screenHeight = h;
        mInBitmap = bitmapc;

        setBackground(new BitmapDrawable(bitmapc));

        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_4444);
        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中

        mSaveBitmap = Bitmap.createBitmap(bitmapc).copy(Bitmap.Config.ARGB_4444, true);
        mSaveCanvas = new Canvas(mSaveBitmap);
        initCanvas();
    }

    public NoteViewCopy(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        initDraftCanvas();
    }

    public void setInBitmap(Bitmap bitmap) {
        mInBitmap = bitmap;
    }

    public void destroyBitmap() {
        if (mInBitmap != null && !mInBitmap.isRecycled()) {
            mInBitmap.recycle();
            mInBitmap = null;
        }
        //        if (null != mBitmap && !mBitmap.isRecycled()) {
        //            mBitmap.recycle();
        //            mBitmap = null;
        //            Log.i(TAG, "destroy mBitmap = " + mBitmap);
        //        }
        //        if (null != mSaveBitmap && !mSaveBitmap.isRecycled()) {
        //            mSaveBitmap.recycle();
        //            mSaveBitmap = null;
        //            Log.i(TAG, "destroy mSaveBitmap = " + mSaveBitmap);
        //        }
    }

    public NoteViewCopy(Context context, int w, int h) {
        super(context);
        this.context = context;
        screenWidth = w;
        screenHeight = h;

        mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_4444);
        mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
        mSaveBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_4444);
        mSaveCanvas = new Canvas(mSaveBitmap);
        mSaveCanvas.drawColor(backgroundColor);
        setBackgroundColor(backgroundColor);
        initCanvas();
    }

    public NoteViewCopy(Context context, Bitmap bitmap, Bitmap saveBitmap) {
        super(context);
        this.context = context;
        mBitmap = bitmap;
        mSaveBitmap = saveBitmap;
        mCanvas = new Canvas(mBitmap);
        mSaveCanvas = new Canvas(mSaveBitmap);
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mSaveCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mSaveCanvas.drawColor(backgroundColor);
        setBackgroundColor(backgroundColor);
        initCanvas();
    }

    public NoteViewCopy(Context context, Bitmap bitmap, Bitmap saveBitmap, Bitmap inBitmap) {
        super(context);
        this.context = context;
        mBitmap = bitmap;
        mInBitmap = inBitmap;
        mSaveBitmap = saveBitmap;
        mCanvas = new Canvas(mBitmap);
        mSaveCanvas = new Canvas(mSaveBitmap);
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mSaveCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mSaveCanvas.drawBitmap(inBitmap, 0, 0, null);
        setBackground(new BitmapDrawable(inBitmap));
        initCanvas();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        screenHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (mBitmap == null && mInBitmap != null) {
            setBackground(new BitmapDrawable(mInBitmap));
            mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_4444);
            mBitmap.eraseColor(Color.argb(0, 0, 0, 0));
            mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
            mSaveBitmap = Bitmap.createBitmap(mInBitmap).copy(Bitmap.Config.ARGB_4444, true);
            mSaveCanvas = new Canvas(mSaveBitmap);
        } else if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_4444);
            mBitmap.eraseColor(Color.argb(0, 0, 0, 0));
            mCanvas = new Canvas(mBitmap);  //所有mCanvas画的东西都被保存在了mBitmap中
        }
    }

    private void initDraftCanvas() {
        mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        setPaintStyle();

        this.setOnTouchListener(this);
    }

    public void initCanvas() {

        if (mInBitmap != null) {
            //如果屏幕旋转了
            if (mBitmap.getWidth() < mInBitmap.getWidth()) {
                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                mInBitmap = Bitmap.createBitmap(mInBitmap, 0, 0, mInBitmap.getWidth(), mInBitmap.getHeight(), matrix, false);
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                mSaveCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                mSaveCanvas.drawBitmap(mInBitmap, 0, 0, null);
                setBackground(new BitmapDrawable(mInBitmap));
            }
        }
        mPaint = new Paint();
        mPaintUp = new Paint();
        mPaintDown = new Paint();
        initDraftCanvas();
        mNoteActivity = (Activity) context;
        //        ViewGroup.MarginLayoutParams marginLayoutParams =
        //                new ViewGroup.MarginLayoutParams(mNoteActivity.mNoteWriteIvEraser.getLayoutParams());
        //        mParams = new RelativeLayout.LayoutParams(marginLayoutParams);
    }

    //初始化画笔样式
    private void setPaintStyle() {
        mPaint = getInitPaint(mPaint);
        if (currentStyle == 1) {
            mPaint.setStrokeWidth(currentSize);
            mPaint.setColor(currentColor);
        } else {//橡皮擦
            //            mPaint.setAlpha(0);
            //            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mPaint.setColor(backgroundColor);
            mPaint.setStrokeWidth(70);
        }

        //        // 设置光源的方向
        //        float[] direction = new float[]{1, 1, 1};
        //        //设置环境光亮度
        //        float light = 0.4f;
        //        // 选择要应用的反射等级
        //        float specular = 6;
        //        // 向mask应用一定级别的模糊
        //        float blur = 4f;
        //
        //
        //        EmbossMaskFilter maskFilter = new EmbossMaskFilter(direction,
        //                light, specular, blur);
        //        mPaint.setMaskFilter(maskFilter);

        mPaintUp = getInitPaint(mPaintUp);
        mPaintDown = getInitPaint(mPaintDown);
        mPaintUp.setStrokeWidth(currentSize);
        mPaintDown.setStrokeWidth(currentSize);
        //        mPaintUp.setColor(Color.RED);
        //        mPaintDown.setColor(Color.BLUE);
    }

    private Paint getInitPaint(Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
        paint.setStrokeCap(Paint.Cap.ROUND);// 形状
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setStrokeWidth(currentSize);
        paint.setColor(currentColor);
        return paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldw != 0 && w != oldw) {
            Matrix matrix = new Matrix();
            if (w > oldw) {
                matrix.setRotate(-90);
            } else {
                matrix.setRotate(90);
            }
            // 围绕原地进行旋转
            mSaveBitmap = Bitmap.createBitmap(mSaveBitmap, 0, 0, mSaveBitmap.getWidth(), mSaveBitmap.getHeight(), matrix, false);
            mSaveCanvas = new Canvas(mSaveBitmap);
            setBackground(new BitmapDrawable(mSaveBitmap));
        }
    }

    /**
     * 撤销
     * 撤销的核心思想就是将画布清空，
     * 将保存下来的Path路径最后一个移除掉，
     * 重新将路径画在画布上面。
     */
    public void undo() {
        if (savePath != null && savePath.size() > 0) {
            DrawPath drawPath = savePath.get(savePath.size() - 1);
            deletePath.add(drawPath);
            savePath.remove(savePath.size() - 1);
            if (mInBitmap != null && !isRedo) {
                setPaintStyle();
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                mSaveCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                mSaveCanvas.drawBitmap(mInBitmap, 0, 0, null);
                setBackground(new BitmapDrawable(mInBitmap));
                Iterator<DrawPath> iter = savePath.iterator();
                while (iter.hasNext()) {
                    DrawPath drawPath1 = iter.next();
                    recoverPath(drawPath1);
                }
            } else {
                redrawOnBitmap();
            }
            invalidate();// 刷新
        }
    }

    private void recoverPath(DrawPath drawPath) {
        mCanvas.drawPath(drawPath.path, drawPath.paint);
        mSaveCanvas.drawPath(drawPath.path, drawPath.paint);
        if (drawPath.paintUp != null) {
            mCanvas.drawPath(drawPath.pathUp, drawPath.paintUp);
            mSaveCanvas.drawPath(drawPath.pathUp, drawPath.paintUp);
            mCanvas.drawPath(drawPath.pathDown, drawPath.paintDown);
            mSaveCanvas.drawPath(drawPath.pathDown, drawPath.paintDown);
        }
    }

    /**
     * 重做
     */
    public void redo() {
        isRedo = true;
        if (savePath != null && savePath.size() > 0) {
            savePath.clear();
            redrawOnBitmap();
        }

        if (mInBitmap != null) {
            redrawOnBitmap();
        }

        setBackgroundColor(backgroundColor);
    }

    private void redrawOnBitmap() {
        setPaintStyle();

        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mSaveCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mSaveCanvas.drawColor(backgroundColor);

        Iterator<DrawPath> iter = savePath.iterator();
        while (iter.hasNext()) {
            DrawPath drawPath = iter.next();
            recoverPath(drawPath);
        }
        invalidate();// 刷新
    }

    private void refreshCanvas() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        options.inPurgeable = true;
        options.inInputShareable = true;
    }

    /**
     * 恢复，恢复的核心就是将删除的那条路径重新添加到savapath中重新绘画即可
     */
    public void recover() {
        if (deletePath.size() > 0) {
            //将删除的路径列表中的最后一个，也就是最顶端路径取出（栈）,并加入路径保存列表中
            DrawPath dp = deletePath.get(deletePath.size() - 1);
            savePath.add(dp);
            //将取出的路径重绘在画布上
            recoverPath(dp);
            //将该路径从删除的路径列表中去除
            deletePath.remove(deletePath.size() - 1);
            invalidate();
        }
    }

    private int mode = 0;
    private int longSize;
    private boolean isMulti = false;
    private boolean isWrite = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 将前面已经画过得显示出来
        canvas.drawBitmap(mBitmap, 0, 0, null);
        //                setBackground(new BitmapDrawable(mSaveBitmap));
        // 实时的显示
        if (mPathUp != null && currentStyle == 1) {
            canvas.drawPath(mPathUp, mPaintUp);
        }
        if (mPathDown != null && currentStyle == 1) {
            canvas.drawPath(mPathDown, mPaintDown);
        }
        if (mPath != null && currentStyle == 1) {
            canvas.drawPath(mPath, mPaint);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        if (!isMulti) {
            initEraser(x, y);
        }

        //得到历史的点
        //        int historySize = event.getHistorySize();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:

                //                resetDirtyRect(x, y);
                //画的时候如果有软键盘则隐藏起来
                if (mImm != null) {
                    mImm.hideSoftInputFromWindow(mNoteActivity.getWindow().getDecorView().getWindowToken(),
                            0);
                }

                mode = 1;
                mStartTime = System.currentTimeMillis();
                // 每次down下去重新new一个Path
                mPath = new Path();
                //每一次记录的路径对象是不一样的
                mDrawPath = new DrawPath();
                mDrawPath.path = mPath;
                mDrawPath.paint = mPaint;
                mPath.moveTo(x, y);
                mPath.lineTo(x + 1, y + 1);
                mX = x;
                mY = y;
                startX = x;
                startY = y;
                downX = x;
                downY = y;
                longSize = 0;
                break;
            case MotionEvent.ACTION_MOVE:

                //                for (int i = 0; i < historySize; i++) {
                //                    float historicalX = event.getHistoricalX(i);
                //                    float historicalY = event.getHistoricalY(i);
                //                    expandDirtyRect(historicalX, historicalY);
                //                }

                if (mode >= 2) {
                } else {
                    if (!isMulti) {
                    }
                }

                endX = x;
                endY = y;
                mEndTime = System.currentTimeMillis();

                if (Math.abs(x - mX) > 3 || Math.abs(y - mY) > 3) {
                    float endX = (mX + x) / 2;
                    float endY = (mY + y) / 2;
                    mPath.quadTo(mX, mY, endX, endY);
                    isWrite = true;
                    if (currentStyle == 0) {
                        mCanvas.drawPath(mPath, mPaint);
                    }
                }


                if (currentStyle == 1 && (Math.abs(x - mX) > 3 || Math.abs(y - mY) > 3)) {
                    calculate();
                }

                startX = x;
                startY = y;
                mStartTime = System.currentTimeMillis();

                if (longSize > 80 && currentStyle == 0) {
                    mPath.moveTo(mX, mY);
                    longSize = 0;
                }
                longSize += 2;


                mX = x;
                mY = y;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                mode = 0;
                //                for (int i = 0; i < historySize; i++) {
                //                    float historicalX = event.getHistoricalX(i);
                //                    float historicalY = event.getHistoricalY(i);
                //                    expandDirtyRect(historicalX, historicalY);
                //                }

                //                if (!isMulti) {//不是多点触控

                float endX2 = (mX + x) / 2;
                float endY2 = (mY + y) / 2;
                mPath.quadTo(mX, mY, endX2, endY2);
                //把路径画到图片中
                mCanvas.drawPath(mPath, mPaint);

                mEndTime = System.currentTimeMillis();
                endX = x;
                endY = y;
                if (currentStyle == 1) {//不是橡皮擦
                    mSaveCanvas.drawPath(mPath, mPaint);

                    calculate();
                    PathMeasure pathMeasure = new PathMeasure(mPath, false);
                    if (pathMeasure.getLength() < 60) {
                        mPath.addPath(mPathUp, 1.1f, 1.1f);
                        mPath.addPath(mPathDown, -1f, -1f);
                    }
                    mCanvas.drawPath(mPathUp, mPaintUp);
                    mCanvas.drawPath(mPathDown, mPaintDown);
                    mSaveCanvas.drawPath(mPathUp, mPaintUp);
                    mSaveCanvas.drawPath(mPathDown, mPaintDown);
                }else {
                    Paint paint = new Paint();
                    paint = getInitPaint(paint);
                    paint.setColor(Color.WHITE);
                    paint.setStrokeWidth(70);
                    mSaveCanvas.drawPath(mPath, paint);
                }

                //把路径保存到集合
                savePath.add(mDrawPath);

                //                }

                mPath = null;// 重新置空
                mPathUp = null;
                mPathDown = null;
                lastResult = 0;
                isMulti = false;//不是多点触控
                lastCalculateTime = 0;

                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //第二点触控抬起
                mode -= 1;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //第二点触控
                if (isWrite) {
                    //                    float endX2 = (mX + x) / 2;
                    //                    float endY2 = (mY + y) / 2;
                    //                    mPath.quadTo(mX, mY, endX2, endY2);
                    //                    mCanvas.drawPath(mPath, mPaint);
                    //                    mSaveCanvas.drawPath(mPath, mPaint);

                    //                    if (currentStyle == 1) {//不是橡皮擦
                    //                        calculate();
                    //                        PathMeasure pathMeasure = new PathMeasure(mPath, false);
                    //                        if (pathMeasure.getLength() < 60) {
                    //                            mPath.addPath(mPathUp, 1.1f, 1.1f);
                    //                            mPath.addPath(mPathDown, -1f, -1f);
                    //                        }
                    //                        mCanvas.drawPath(mPathUp, mPaintUp);
                    //                        mCanvas.drawPath(mPathDown, mPaintDown);
                    //                        mSaveCanvas.drawPath(mPathUp, mPaintUp);
                    //                        mSaveCanvas.drawPath(mPathDown, mPaintDown);
                    //                    }
                    //                    savePath.add(mDrawPath);
                    isWrite = false;
                }
                isMulti = true;
                mode += 1;
                break;
        }

        //                        invalidate((int) (dirtyRect.left - currentSize),
        //                                (int) (dirtyRect.top - currentSize),
        //                                (int) (dirtyRect.right - currentSize),
        //                                (int) (dirtyRect.bottom - currentSize));

        lastTouchX = x;
        lastTouchY = y;
        return true;
    }

    //上一次计算的时间,如果间隔太长就不执行
    private long lastCalculateTime = 0;

    private void calculate() {

        //如果两点相隔时间太长就返回
        if (lastCalculateTime != 0 && (System.currentTimeMillis() - lastCalculateTime) > 500) {
            lastCalculateTime = System.currentTimeMillis();
            mPathDown.moveTo(endX, endY);
            mPathUp.moveTo(endX, endY);
            return;
        }

        float velocity = velocityFrom2();
        float radius = (float) (controlPaint(velocity, currentSize) / 1.8);
        float ratio = (float) screenWidth / 1280;
        //        Log.d("BiFeng", "radius = " + radius + "   screenWidth = " + screenWidth + "    ,tatio = " + ratio);
        radius = radius * ratio;

        // 根据角度算出四边形的四个点
        float offsetX = (float) (radius * Math.sin(Math.atan((endY - startY) / (endX - startX))));
        float offsetY = (float) (radius * Math.cos(Math.atan((endY - startY) / (endX - startX))));

        offsetX = Math.abs(offsetX);
        offsetY = Math.abs(offsetY);
        if (Float.isNaN(offsetX) || Float.isInfinite(offsetX)) {
            offsetX = 1;
        }
        if (Float.isNaN(offsetY) || Float.isInfinite(offsetY)) {
            offsetY = 1;
        }


        float x1 = startX - offsetX;
        float y1 = startY + offsetY;

        float x2 = endX - offsetX;
        float y2 = endY + offsetY;

        float x3 = endX + offsetX;
        float y3 = endY - offsetY;

        float x4 = startX + offsetX;
        float y4 = startY - offsetY;

        if (mPathUp == null) {
            mPathUp = new Path();
            mPathUp.moveTo(x1, y1);
            //            mPathUp.lineTo(x2,y2);
            float endX1 = (x2 + x1) / 2;
            float endY1 = (y2 + y1) / 2;
            mPathUp.quadTo(x1, y1, endX1, endY1);

            mPathDown = new Path();
            mPathDown.moveTo(x4, y4);
            //            mPathDown.lineTo(x3,y3);
            float endX2 = (x3 + x4) / 2;
            float endY2 = (y4 + y3) / 2;
            mPathDown.quadTo(x4, y4, endX2, endY2);

            mDrawPath.paintUp = mPaintUp;
            mDrawPath.pathUp = mPathUp;
            mDrawPath.paintDown = mPaintDown;
            mDrawPath.pathDown = mPathDown;
        } else {
            float endX1 = (x2 + x1) / 2;
            float endY1 = (y2 + y1) / 2;
            mPathUp.quadTo(x1, y1, endX1, endY1);

            float endX2 = (x3 + x4) / 2;
            float endY2 = (y4 + y3) / 2;
            mPathDown.quadTo(x4, y4, endX2, endY2);
        }

        lastCalculateTime = System.currentTimeMillis();

    }

    private final float KEY_PAINT_WIDTH = 2.1f;
    private float lastResult = 0;

    private float controlPaint(double velocity, float paintSize) {
        //余弦函数
        //y=0.5*[cos(x*PI)+1]
        float result = KEY_PAINT_WIDTH * paintSize;
        if (velocity <= 0.2) {
            result = (float) ((float) velocity / 3);
        } else if (velocity < 0.5) {
            result = (float) ((float) velocity / 2);

        } else if (velocity < 0.7) {
            result = (float) ((float) velocity / 1.5);

        } else if (velocity < 0.9) {
            result = (float) ((float) velocity / 1);

        } else if (velocity < 1) {
            result = (float) ((float) velocity * 1.2);

        } else if (velocity <= 2) {
            result = (float) velocity * 1.5f;

        } else if (velocity > 2) {
            result = (float) ((float) velocity * 1.8);

        } else if (velocity > 3) {
            result = (float) (0.12 * paintSize * KEY_PAINT_WIDTH * (Math.cos(velocity * Math.PI) + 1));


        } else if (velocity > 4) {
            result = (float) (0.09 * paintSize * KEY_PAINT_WIDTH * (Math.cos(velocity * Math.PI) + 1));


        } else if (velocity > 5) {
            result = (float) (0.08 * paintSize * KEY_PAINT_WIDTH * (Math.cos(velocity * Math.PI) + 1));


        } else if (velocity > 6) {
            result = (float) (0.07 * paintSize * KEY_PAINT_WIDTH * (Math.cos(velocity * Math.PI) + 1));


        } else if (velocity > 7) {
            result = (float) (0.06 * paintSize * KEY_PAINT_WIDTH * (Math.cos(velocity * Math.PI) + 1));


        } else {
            result = (float) (0.05 * paintSize * KEY_PAINT_WIDTH * (Math.cos(velocity * Math.PI) + 1));

        }

        if (result > paintSize / 1.1) {
            result = (float) (paintSize / 1.1);
        }

        if (lastResult != 0) {
            if (lastResult > result * 3) {
                result = ((float) (lastResult / 3 > result * 2 ? lastResult / 3 : result * 2));
            } else if (lastResult < result / 3) {
                result = ((float) (lastResult * 3 < result / 2 ? result / 2 : lastResult * 3));
            }
        }
        lastResult = result;
        return result;
    }

    /**
     * 计算笔画的速度
     *
     * @return 速度
     */
    public float velocityFrom2() {
        float distanceTo = (float) Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startY - endY, 2));
        float velocity = distanceTo / (mEndTime - mStartTime);
        //        if (velocity != velocity)
        //            return 0f;
        return velocity;
    }

    /**
     * 更新橡皮擦的位置
     *
     * @param x
     * @param y
     */
    public void initEraser(float x, float y) {

        if (currentStyle == 0) {
            mOnTouchEraser.onTouchEraserListener(x, y);
            //            int x1 = (int) x;
            //            int y1 = (int) y;
            //            int width = mNoteActivity.mNoteWriteIvEraser.getWidth() / 2;
            //            int height = (int) (mNoteActivity.mNoteWriteIvEraser.getHeight() / 1.8);
            //            mParams.setMargins(x1 - width, y1 - height, width - x1 - width,
            //                    height - y1 - height);
            //            mNoteActivity.mNoteWriteIvEraser.setLayoutParams(mParams);
            //            int dx = (int) (x - mX);
            //            int dy = (int) (y - mY);
            //            int mL = mEraserView.getLeft() + dx;
            //            int mB = mEraserView.getBottom() + dy;
            //            int mR = mEraserView.getRight() + dx;
            //            int mT = mEraserView.getTop() + dy;
            //            mNoteActivity.mNoteWriteIvEraser.layout(mL,mT,mR,mB);
            //            mEraserView.layout(mL,mT,mR,mB);
        }

    }


    public interface onTouchEraser {
        void onTouchEraserListener(float x, float y);
    }

    private onTouchEraser mOnTouchEraser;

    public void setOnTouchEraserListener(onTouchEraser onTouchEraser) {
        mOnTouchEraser = onTouchEraser;
    }


    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }
        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    /**
     * Resets the dirty region when the motion event occurs.
     */
    private void resetDirtyRect(float eventX, float eventY) {
        // The lastTouchX and lastTouchY were set when the ACTION_DOWN
        // motion event occurred.
        dirtyRect.left = Math.min(lastTouchX, eventX);
        dirtyRect.right = Math.max(lastTouchX, eventX);
        dirtyRect.top = Math.min(lastTouchY, eventY);
        dirtyRect.bottom = Math.max(lastTouchY, eventY);
    }

    //保存到sd卡
    public String saveToSDCard(String time, String content) throws Exception {
        mPaint.setColor(backgroundColor);
        mPaint.setStrokeWidth(3);
        mSaveCanvas.drawPoint(0, 0, mPaint);
        String sdPath = SDcardUtil.getBookNoteSavaPath();
        File f = new File(sdPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        String path = sdPath + "/" + content + "__" + time + ".png";
        File file = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSaveBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        return file.getPath();
    }

    //保存到sd卡
    public File saveToSDCard(int count, String content) {
        mPaint.setColor(backgroundColor);
        mPaint.setStrokeWidth(3);
        mSaveCanvas.drawPoint(0, 0, mPaint);
        String sdPath = Environment.getExternalStorageDirectory() + "/ScrawlNote";
        File f = new File(sdPath);
        if (!f.exists()) {
            f.mkdirs();
        }
        String path = sdPath + "/" + content + "第" + count + "张.png";
        File file = new File(path);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSaveBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        return file;
    }

    /**
     * 选择画布的颜色
     *
     * @param color
     */
    public void selectorCanvasColor(int color) {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mSaveCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        if (!isPostil) {
            mSaveCanvas.drawColor(color);
            Iterator<DrawPath> iter = savePath.iterator();
            while (iter.hasNext()) {
                DrawPath drawPath = iter.next();
                if (drawPath.paint.getColor() == backgroundColor || drawPath.paint.getColor() == Color.TRANSPARENT) {
                    drawPath.paint.setColor(color);
                }
                recoverPath(drawPath);
            }
            invalidate();// 刷新

            backgroundColor = color;
            setBackgroundColor(color);
            mIsChangerBackground = true;
        }
    }


    //选择画笔大小
    public void selectPaintSize(int which) {
        if (which < 1)
            which = 1;
        currentSize = which;
        setPaintStyle();
        mPaint.setStrokeWidth(which);
        mPaintUp.setStrokeWidth(which);
        mPaintDown.setStrokeWidth(which);
    }


    //设置画笔颜色
    public void selectPaintColor(int which) {
        currentStyle = 1;
        currentColor = which;
        setPaintStyle();

    }

    private boolean isEraser = false;
    public boolean isPostil = false;

    //设置橡皮擦
    public void selectEraser() {

        isEraser = !isEraser;
        if (currentStyle == 1) {
            currentStyle = 0;
        } else {
            currentStyle = 1;
            mPaint = new Paint();
            mPaintUp = new Paint();
            mPaintDown = new Paint();
        }
        setPaintStyle();

        //// TODO: 2017-5-24 暂时考虑用背景颜色做橡皮擦
        //批注的时候用全透明作为橡皮擦色
        if (backgroundColor == Constants.NoteViewBackground && currentStyle == 0) {
            mPaint.setColor(Color.TRANSPARENT);
            mPaint.setAlpha(0);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

    }

    public void isPostil(boolean isPostil) {
        this.isPostil = isPostil;
    }

}
