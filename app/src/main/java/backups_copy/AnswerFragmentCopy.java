package backups_copy;

import android.support.v4.app.Fragment;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 15:28
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class AnswerFragmentCopy extends Fragment {

/*    @InjectView(R.id.fragment_answer_mv_question)
    MathView mMvQuestion;
    @InjectView(R.id.fragment_answer_mv_result)
    MathView mMvResult;
    @InjectView(R.id.fragment_answer_iv)
    ImageView mFragmentAnswerIv;
    private View mView;
    private QuestionAnswer mAnswer;
    private URL mUrl;
    private Bitmap pngBM;
    private boolean finishFlag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_answer_copy, container, false);
        ButterKnife.inject(this, mView);
        initView();
        init();
        return mView;
    }

    private void init() {
        try {
            String PicUrlCogs = "http://latex.codecogs.com/gif.latex?";
            mUrl = new URL(PicUrlCogs + mAnswer.questionAnswer);
            new MyDownloadTask().execute();         // 执行Http请求
            while (!finishFlag) {
            }              // 等待数据接收完毕
            mFragmentAnswerIv.setImageBitmap(pngBM);        // 显示图片
            // 标识回位
            finishFlag = false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    class MyDownloadTask extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            //display progress dialog.
        }

        protected Void doInBackground(Void... params) {
            try {
                URL picUrl = mUrl;              // 获取URL地址
                HttpURLConnection conn = (HttpURLConnection) picUrl.openConnection();
                //        conn.setConnectTimeout(1000);       // 建立连接
                //        conn.setReadTimeout(1000);
                conn.connect();               // 打开连接
                if (conn.getResponseCode() == 200) {     // 连接成功，返回数据
                    InputStream ins = conn.getInputStream(); // 将数据输入到数据流中
                    // 解析数据流
                    pngBM = BitmapFactory.decodeStream(picUrl.openStream());
                    finishFlag = true;            // 数据传输完毕标识
                    ins.close();               // 关闭数据流
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            // dismiss progress dialog and update ui
        }
    }

    private void initView() {
        mMvQuestion.setText(mAnswer.questionAnswer);
        mMvQuestion.getSettings().setTextSize(WebSettings.TextSize.LARGER);
        mMvResult.setText(mAnswer.explainationAnswer);
        mMvResult.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
    }

    public void initData(QuestionAnswer answer) {
        mAnswer = answer;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }*/
}
