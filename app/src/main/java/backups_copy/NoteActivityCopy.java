//package activity;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.icox.exercises.R;
//import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
//import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
//import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
//
//import java.io.File;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import view.NoteSurfaceView;
//import view.TuyaView;
//
///**
// * @author Administrator
// * @version $Rev$
// * @time 2017-3-30 9:30
// * @des ${TODO}
// * @updateAuthor $Author$
// * @updateDate $Date$
// * @updateDes ${TODO}
// */
//public class NoteActivityCopy extends Activity implements View.OnClickListener {
//    @InjectView(R.id.note_write_tablet_view)
//    FrameLayout mNoteWriteTabletView;
//    @InjectView(R.id.note_write_but_ok)
//    LinearLayout mNoteWriteButOk;
//    @InjectView(R.id.note_write_but_clear)
//    Button mNoteWriteButClear;
//    @InjectView(R.id.note_write_but_repeal)
//    Button mNoteWriteButRepeal;
//    @InjectView(R.id.note_write_but_recover)
//    Button mNoteWriteButRecover;
//    @InjectView(R.id.note_write_but_cancel)
//    Button mNoteWriteButCancel;
//    @InjectView(R.id.note_write_btn_read)
//    Button mNoteWriteBtnRead;
//    @InjectView(R.id.note_write_btn_back)
//    Button mNoteWriteBtnBack;
//    @InjectView(R.id.note_write_edt_text)
//    EditText mNoteWriteEdtText;
//    @InjectView(R.id.note_write_but_type)
//    Button mNoteWriteButType;
//
//    private TuyaView mPaintView;
//    private String mImgPath;
//    private String mText;
//    private NoteSurfaceView mNoteSurface;
//    private boolean isType = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        //        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        setContentView(R.layout.activity_note);
//        ButterKnife.inject(this);
//        mImgPath = getIntent().getStringExtra("ImgPath");
//        mText = getIntent().getStringExtra("Text");
//        initCanvas();
//        initRightMenuBackground();
//    }
//
//    private void initCanvas() {
//        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
//        getWindow().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
//        int screenWidth = mDisplayMetrics.widthPixels;
//        int screenHeight = mDisplayMetrics.heightPixels;
//        //        GameViewSurface gameViewSurface = new GameViewSurface(this,screenWidth,screenHeight);
//        if (TextUtils.isEmpty(mImgPath)) {
//            // 获取屏幕尺寸
//            mPaintView = new TuyaView(this, screenWidth, screenHeight);
//            mNoteSurface = new NoteSurfaceView(this, screenWidth, screenHeight);
//        } else {
//            // 获取屏幕尺寸
//            Bitmap bitmapc = BitmapFactory.decodeFile(mImgPath);
//            //            mNoteSurface = new NoteSurfaceView(this, bitmapc, screenWidth, screenHeight);
//            mPaintView = new TuyaView(this, bitmapc, screenWidth, screenHeight);
//        }
//        mNoteWriteTabletView.addView(mPaintView);
//        mPaintView.requestFocus();
//
//        if (mText != null && !TextUtils.isEmpty(mText)) {
//            mNoteWriteEdtText.setVisibility(View.VISIBLE);
//            mNoteWriteEdtText.setEnabled(false);
//        }
//
//        mNoteWriteBtnBack.setOnClickListener(this);
//        mNoteWriteBtnRead.setOnClickListener(this);
//        mNoteWriteButCancel.setOnClickListener(this);
//        mNoteWriteButRecover.setOnClickListener(this);
//        mNoteWriteButRepeal.setOnClickListener(this);
//        mNoteWriteButClear.setOnClickListener(this);
//        mNoteWriteButOk.setOnClickListener(this);
//        mNoteWriteButType.setOnClickListener(this);
//    }
//
//    /**
//     * 右上角选择背景菜单
//     */
//    private void initRightMenuBackground() {
//        int redActionButtonSize =100;
//        int redActionButtonMargin =20;
//        int redActionButtonContentSize = 100;
//        int redActionButtonContentMargin = 20;
//
//        int redActionMenuRadius = 120;
//        int blueSubActionButtonSize = 70;
//        int blueSubActionButtonContentMargin = 20;
//
//        final ImageView fabIconStar  = new ImageView(this);
//        // 设置菜单按钮Button的图标
////        fabIconStar .setImageResource(R.drawable.ic_page_indicator_focused);
//
//        // 设置菜单按钮Button的宽、高，边距
//        FloatingActionButton.LayoutParams starParams = new FloatingActionButton.LayoutParams(
//                redActionButtonSize, redActionButtonSize);
//        starParams.setMargins(redActionButtonMargin, redActionButtonMargin,
//                redActionButtonMargin, redActionButtonMargin);
//        fabIconStar.setLayoutParams(starParams);
//
//
//        // 设置菜单按钮Button里面图案的宽、高，边距
//        FloatingActionButton.LayoutParams fabIconStarParams = new FloatingActionButton.LayoutParams(
//                redActionButtonContentSize, redActionButtonContentSize);
////        fabIconStarParams.setMargins(redActionButtonContentMargin,
////                redActionButtonContentMargin, redActionButtonContentMargin,
////                redActionButtonContentMargin);
//
//        final FloatingActionButton leftCenterButton = new FloatingActionButton.Builder(
//                this).setContentView(fabIconStar, fabIconStarParams)
//                .setBackgroundDrawable(R.drawable.ic_page_indicator_focused)
//                .setPosition(FloatingActionButton.POSITION_TOP_RIGHT)
//                .setLayoutParams(starParams).build();
//
//        SubActionButton.Builder lCSubBuilder = new SubActionButton.Builder(this);
////        lCSubBuilder.setBackgroundDrawable();
//
//        //设置菜单中图标的参数
//        FrameLayout.LayoutParams blueContentParams = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.WRAP_CONTENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
////        blueContentParams.setMargins(blueSubActionButtonContentMargin,
////                blueSubActionButtonContentMargin,
////                blueSubActionButtonContentMargin,
////                blueSubActionButtonContentMargin);
//
////        lCSubBuilder.setLayoutParams(blueContentParams);
//
//        //设置布局参数
//        FrameLayout.LayoutParams blueParams = new FrameLayout.LayoutParams(blueSubActionButtonSize,
//                blueSubActionButtonSize);
//        lCSubBuilder.setLayoutParams(blueParams);
//
//
//        ImageView ivGreen = new ImageView(this);
//        ImageView ivWhite = new ImageView(this);
//        ImageView ivBlack = new ImageView(this);
//        ImageView ivBlue = new ImageView(this);
//        ImageView ivYellow = new ImageView(this);
//
////        ivGreen.setBackgroundColor(Color.GREEN);
//        ivGreen.setImageResource(R.drawable.ic_page_indicator_focused);
//        ivWhite.setImageResource(R.drawable.ic_page_indicator_focused);
//        ivBlack.setImageResource(R.drawable.ic_page_indicator_focused);
//        ivBlue.setImageResource(R.drawable.ic_page_indicator_focused);
//        ivYellow.setImageResource(R.drawable.ic_page_indicator_focused);
//
//        //setStartAngle(70).setEndAngle(-70)设置扩展菜单的位置
//        final FloatingActionMenu leftCenterMenu=new FloatingActionMenu.Builder(this)
//                .addSubActionView(lCSubBuilder.setContentView(ivGreen).build())
//                .addSubActionView(lCSubBuilder.setContentView(ivBlack).build())
//                .addSubActionView(lCSubBuilder.setContentView(ivWhite).build())
//                .addSubActionView(lCSubBuilder.setContentView(ivBlue).build())
//                .addSubActionView(lCSubBuilder.setContentView(ivYellow).build())
//                .setRadius(redActionMenuRadius).setStartAngle(200).setEndAngle(70)
//                .attachTo(leftCenterButton).build();
//
//        ivGreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPaintView.selectorCanvasColor(Color.GREEN);
//            }
//        });
//
//        ivWhite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPaintView.selectorCanvasColor(Color.WHITE);
//            }
//        });
//
//        ivBlue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPaintView.selectorCanvasColor(Color.BLUE);
//            }
//        });
//
//        ivBlack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPaintView.selectorCanvasColor(Color.BLACK);
//            }
//        });
//
//        ivYellow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPaintView.selectorCanvasColor(Color.YELLOW);
//            }
//        });
//    }
//
////    /**
////     * 右上角选择背景菜单
////     */
////    private void initRightMenuBackground() {
////        final ImageView fabIconNew = new ImageView(this);
////        // 设置菜单按钮Button的图标
////        fabIconNew.setImageResource(R.drawable.ic_launcher);
////        final FloatingActionButton rightLowerButton = new FloatingActionButton.Builder(
////                this).setContentView(fabIconNew).build();
////
////        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);
////
////        ImageView ivGreen = new ImageView(this);
////        ImageView ivWhite = new ImageView(this);
////        ImageView ivBlack = new ImageView(this);
////        ImageView ivBlue = new ImageView(this);
////        ImageView ivYellow = new ImageView(this);
////
////        ivGreen.setBackgroundColor(Color.GREEN);
////        ivWhite.setBackgroundColor(Color.WHITE);
////        ivBlack.setBackgroundColor(Color.BLACK);
////        ivBlue.setBackgroundColor(Color.BLUE);
////        ivYellow.setBackgroundColor(Color.YELLOW);
////
////        final FloatingActionMenu rightLowerMenu = new FloatingActionMenu.Builder(
////                this)
////                .addSubActionView(rLSubBuilder.setContentView(ivGreen).build())
////                .addSubActionView(rLSubBuilder.setContentView(ivWhite).build())
////                .addSubActionView(rLSubBuilder.setContentView(ivBlack).build())
////                .addSubActionView(rLSubBuilder.setContentView(ivBlue).build())
////                .addSubActionView(rLSubBuilder.setContentView(ivYellow).build())
////                .attachTo(rightLowerButton).build();
////
////
////    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.note_write_btn_back://画笔黑色
//                mPaintView.selectPaintColor(4);
//                break;
//            case R.id.note_write_btn_read://画笔红色
//                mPaintView.selectPaintColor(0);
//                break;
//            case R.id.note_write_but_cancel://取消
//                finish();
//                break;
//            case R.id.note_write_but_ok://提交
//                if (!TextUtils.isEmpty(mImgPath)) {
//                    File file = new File(mImgPath);
//                    if (file.isFile() && file.exists()) {
//                        file.delete();
//                    }
//                }
//                String path = mPaintView.saveToSDCard();
//                finish();
//                break;
//            case R.id.note_write_but_clear://清屏
//                mPaintView.redo();
//                break;
//            case R.id.note_write_but_recover://恢复
//                mPaintView.recover();
//                break;
//            case R.id.note_write_but_repeal://撤销
//                mPaintView.undo();
//                break;
//            case R.id.note_write_but_type://文字
//                isType = !isType;
//                Toast.makeText(this,""+isType,Toast.LENGTH_SHORT).show();
//                if (isType) {
//                    mNoteWriteEdtText.setVisibility(View.VISIBLE);
//                    mNoteWriteEdtText.setEnabled(true);
//                }else {
//                    if (!TextUtils.isEmpty(mNoteWriteEdtText.getText().toString())){
//                    mNoteWriteEdtText.setVisibility(View.VISIBLE);
//                    mNoteWriteEdtText.setEnabled(false);
//                    }else {
//                    mNoteWriteEdtText.setVisibility(View.GONE);
//                    }
//                }
//                break;
//
//            default:
//                break;
//        }
//    }
//}
