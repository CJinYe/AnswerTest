package conf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-5-11 18:04
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class TextDemo extends TextView {

    private Paint mPaint;

    public TextDemo(Context context) {
        super(context);
    }

    public TextDemo(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Log.e("TextDemo", " text = " + getText());
        mPaint = new Paint();
        mPaint.setTextSize(30);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("TextDemo", " text = " + getText());
        String text = getText().toString();
//         text.substring(text.lastIndexOf("("),text.lastIndexOf(")"));
        canvas.drawText(getText().toString(),100,100,mPaint);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

}
