package dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.icox.exercises.R;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.xw.repo.BubbleSeekBar;

import view.NoteView.NoteView;


/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-4-28 14:22
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class ColorPickerDialog extends Dialog {

    private String TAG = "ColorPickerDialog";
    private final NoteView mTuyaView;
    private ColorPicker mBackgroundPicker;
    private boolean mIsBackgroundColorChange = false;
    private boolean isPaintColorChange = false;
    private boolean isPaintSizeChange = false;
    private ColorPicker mPaintPicker;
    private BubbleSeekBar mSeekBar;

    public ColorPickerDialog(@NonNull Context context, NoteView tuyaView) {
        super(context, R.style.CustomDialog);
        mTuyaView = tuyaView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyCompat();
        setContentView(R.layout.dialog_color_picker);
        initBackgoundView();
        initPaintPicker();
        initView();
    }

    private void initView() {
        ImageView button = (ImageView) findViewById(R.id.dialog_color_picker_bt_sure);
        mSeekBar = (BubbleSeekBar) findViewById(R.id.dialog_color_picker_seek_bar);
        mSeekBar.setProgress(mTuyaView.mPaint.getStrokeWidth());
        mSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                isPaintSizeChange = true;
            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPaintColorChange) {
                    mTuyaView.selectPaintColor(mPaintPicker.getColor());
                    mTuyaView.currentColor = mPaintPicker.getColor();
                }

                if (mIsBackgroundColorChange) {
                    mTuyaView.selectorCanvasColor(mBackgroundPicker.getColor());
                    mTuyaView.backgroundColor = mBackgroundPicker.getColor();
                }

                if (isPaintSizeChange) {
                    mTuyaView.selectPaintSize(mSeekBar.getProgress());
                }

                dismiss();
            }
        });

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
    }


    private void initPaintPicker() {
        final TextView paintTv = (TextView) findViewById(R.id.dialog_color_paint_tv);
        mPaintPicker = (ColorPicker) findViewById(R.id.dialog_color_paint_picker);
        SVBar svBar = (SVBar) findViewById(R.id.dialog_color_paint_svbar);
        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.dialog_color_paint_saturationbar);

        mPaintPicker.addSVBar(svBar);
        mPaintPicker.addSaturationBar(saturationBar);

        mPaintPicker.getColor();

        mPaintPicker.setOldCenterColor(mPaintPicker.getColor());
        mPaintPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                paintTv.setTextColor(color);
                isPaintColorChange = true;
            }
        });

        //to turn of showing the old color
        mPaintPicker.setShowOldCenterColor(false);

    }

    private void initBackgoundView() {

        final TextView bgTv = (TextView) findViewById(R.id.dialog_color_background_tv);

        mBackgroundPicker = (ColorPicker) findViewById(R.id.dialog_color_background_picker);
        SVBar svBar = (SVBar) findViewById(R.id.dialog_color_background_svbar);
        SaturationBar saturationBar = (SaturationBar) findViewById(R.id.dialog_color_background_saturationbar);

        mBackgroundPicker.addSVBar(svBar);
        mBackgroundPicker.addSaturationBar(saturationBar);

        mBackgroundPicker.getColor();

        mBackgroundPicker.setOldCenterColor(mBackgroundPicker.getColor());
        mBackgroundPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                bgTv.setTextColor(color);
//                mTuyaView.setBackgroundColor(color);
                mIsBackgroundColorChange = true;
            }
        });

        //to turn of showing the old color
        mBackgroundPicker.setShowOldCenterColor(false);

        //        //adding onChangeListeners to bars
        //        opacitybar.setOnOpacityChangeListener(new OnOpacityChangeListener …)
        //        valuebar.setOnValueChangeListener(new OnValueChangeListener …)
        //        saturationBar.setOnSaturationChangeListener(new OnSaturationChangeListener …)
    }

    protected void applyCompat() {
        if (Build.VERSION.SDK_INT < 19) {
            return;
        }
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Window window = this.getWindow();
        if (window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }
    }

}
