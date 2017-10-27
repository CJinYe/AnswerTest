package fragment;

import android.support.v4.app.Fragment;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 14:10
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class TwoChoiceFragment extends Fragment  {
//
//    @InjectView(R.id.fragment_choice_mv_question)
//    TextView mChoiceMvQuestion;
//    @InjectView(R.id.fragment_choice_mv_A_answer)
//    TextView mChoiceMvAAnswer;
//    @InjectView(R.id.fragment_choice_ll_A)
//    LinearLayout mChoiceLlA;
//    @InjectView(R.id.fragment_choice_mv_B_answer)
//    TextView mChoiceMvBAnswer;
//    @InjectView(R.id.fragment_choice_ll_B)
//    LinearLayout mChoiceLlB;
//    @InjectView(R.id.fragment_choice_mv_C_answer)
//    TextView mChoiceMvCAnswer;
//    @InjectView(R.id.fragment_choice_ll_C)
//    LinearLayout mChoiceLlC;
//    @InjectView(R.id.fragment_choice_mv_D_answer)
//    TextView mChoiceMvDAnswer;
//    @InjectView(R.id.fragment_choice_ll_D)
//    LinearLayout mChoiceLlD;
//    @InjectView(R.id.fragment_choice_mv_result)
//    TextView mChoiceMvResult;
//    @InjectView(R.id.fragment_choice_Tv_A)
//    TextView mChoiceTvA;
//    @InjectView(R.id.fragment_choice_Tv_A2)
//    TextView mChoiceTvA2;
//    @InjectView(R.id.fragment_choice_Tv_B)
//    TextView mChoiceTvB;
//    @InjectView(R.id.fragment_choice_Tv_B2)
//    TextView mChoiceTvB2;
//    @InjectView(R.id.fragment_choice_Tv_C)
//    TextView mChoiceTvC;
//    @InjectView(R.id.fragment_choice_Tv_C2)
//    TextView mChoiceTvC2;
//    @InjectView(R.id.fragment_choice_Tv_D)
//    TextView mChoiceTvD;
//    @InjectView(R.id.fragment_choice_Tv_D2)
//    TextView mChoiceTvD2;
//    @InjectView(R.id.fragment_choice_tv_title)
//    TextView mFragmentChoiceTvTitle;
//    private View mView;
//    private QuestionTwoChoice mChoice;
//    private DBUtil mDbUtil;
//    private QuestionDB mQuestionDB;
//    private SpUtils mSpUtils;
//    private boolean mIsExplain;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mView = inflater.inflate(R.layout.fragment_choice, container, false);
//        ButterKnife.inject(this, mView);
//        mDbUtil = new DBUtil(getActivity());
//        mSpUtils = new SpUtils(getActivity());
//        initView();
//        mIsExplain = mSpUtils.getBoolean(Constants.isExplain, false);
//        if (mIsExplain) {
//            initExplainView();
//        }
//        initListener();
//        return mView;
//    }
//
//    private void initExplainView() {
//        mChoiceMvResult.setVisibility(View.VISIBLE);
//        if (mQuestionDB != null) {
//            char[] chars = mQuestionDB.userAnswer.toCharArray();
//            for (int i = 0; i < chars.length; i++) {
//                String str = String.valueOf(chars[i]);
//                if ("A".equals(str)) {
//                    initExplainError(mChoiceTvA, mChoiceLlA, mChoiceTvA2);
//                } else if ("B".equals(str)) {
//                    initExplainError(mChoiceTvB, mChoiceLlB, mChoiceTvB2);
//                } else if ("C".equals(str)) {
//                    initExplainError(mChoiceTvC, mChoiceLlC, mChoiceTvC2);
//                } else if ("D".equals(str)) {
//                    initExplainError(mChoiceTvD, mChoiceLlD, mChoiceTvD2);
//                }
//            }
//        }
//
//        char[] chars = mChoice.answerTwoChoice.toCharArray();
//        for (int i = 0; i < chars.length; i++) {
//            String str = String.valueOf(chars[i]);
//            if ("A".equals(str)) {
//                initExplain(mChoiceTvA, mChoiceLlA, mChoiceTvA2);
//            } else if ("B".equals(str)) {
//                initExplain(mChoiceTvB, mChoiceLlB, mChoiceTvB2);
//            } else if ("C".equals(str)) {
//                initExplain(mChoiceTvC, mChoiceLlC, mChoiceTvC2);
//            } else if ("D".equals(str)) {
//                initExplain(mChoiceTvD, mChoiceLlD, mChoiceTvD2);
//            }
//        }
//    }
//
//    private void initListener() {
//        mChoiceLlA.setOnClickListener(this);
//        mChoiceLlC.setOnClickListener(this);
//        mChoiceLlB.setOnClickListener(this);
//        mChoiceLlD.setOnClickListener(this);
//    }
//
//    private void initView() {
//        mFragmentChoiceTvTitle.setText("双选题：");
//        mChoiceMvQuestion.setText(Html.fromHtml(mChoice.questionTwoChoice, getImageGetterInstance(), new MyTagHandler()));
//        mChoiceMvAAnswer.setText(Html.fromHtml(mChoice.answerTwoA, getImageGetterInstance(), new MyTagHandler()));
//        mChoiceMvBAnswer.setText(Html.fromHtml(mChoice.answerTwoB, getImageGetterInstance(), new MyTagHandler()));
//        mChoiceMvCAnswer.setText(Html.fromHtml(mChoice.answerTwoC, getImageGetterInstance(), new MyTagHandler()));
//        mChoiceMvDAnswer.setText(Html.fromHtml(mChoice.answerTwoD, getImageGetterInstance(), new MyTagHandler()));
//        mChoiceMvResult.setText(Html.fromHtml("答案解析:<br/>&#8195;&#8195;" + mChoice.explainationTwoChoice, getImageGetterInstance(), new MyTagHandler()));
//        mChoiceMvQuestion.setMovementMethod(LinkMovementMethod.getInstance());
//        mChoiceMvAAnswer.setMovementMethod(LinkMovementMethod.getInstance());
//        mChoiceMvBAnswer.setMovementMethod(LinkMovementMethod.getInstance());
//        mChoiceMvCAnswer.setMovementMethod(LinkMovementMethod.getInstance());
//        mChoiceMvDAnswer.setMovementMethod(LinkMovementMethod.getInstance());
//        mChoiceMvResult.setMovementMethod(LinkMovementMethod.getInstance());
//
//        mChoiceMvResult.setVisibility(View.GONE);
//
//        mQuestionDB = mDbUtil.queryAnswer(mChoice.idTwoChoice);
//        if (mQuestionDB != null) {
//            char[] chars = mQuestionDB.userAnswer.toCharArray();
//            for (int i = 0; i < chars.length; i++) {
//                String str = String.valueOf(chars[i]);
//                if ("A".equals(str)) {
//                    updataSelector(mChoiceTvA, mChoiceLlA, mChoiceTvA2);
//                } else if ("B".equals(str)) {
//                    updataSelector(mChoiceTvB, mChoiceLlB, mChoiceTvB2);
//                } else if ("C".equals(str)) {
//                    updataSelector(mChoiceTvC, mChoiceLlC, mChoiceTvC2);
//                } else if ("D".equals(str)) {
//                    updataSelector(mChoiceTvD, mChoiceLlD, mChoiceTvD2);
//                }
//            }
//            mChoice.selectedTwoAnswer = mQuestionDB.userAnswer;
//        }
//    }
//
//    public void initData(QuestionTwoChoice choice) {
//        mChoice = choice;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.fragment_choice_ll_A:
//                if (mIsExplain) {
//                    return;
//                } else {
//
//                    if (mChoice.selectedTwoAnswer != null && mChoice.selectedTwoAnswer.contains("A")) {
//                        //设置为不选中
//                        mChoice.selectedTwoAnswer = mChoice.selectedTwoAnswer.replaceAll("A", "");
//                        updataNormal(mChoiceTvA, mChoiceLlA, mChoiceTvA2);
//                    } else {
//                        if (mChoice.selectedTwoAnswer == null) {
//                            mChoice.selectedTwoAnswer = "A";
//                        } else {
//                            mChoice.selectedTwoAnswer += "A";
//
//                        }
//                        updataSelector(mChoiceTvA, mChoiceLlA, mChoiceTvA2);
//                    }
//                    updataDB();
//                }
//                break;
//            case R.id.fragment_choice_ll_B:
//                if (mIsExplain) {
//                    return;
//                } else {
//                    if (mChoice.selectedTwoAnswer != null && mChoice.selectedTwoAnswer.contains("B")) {
//                        //设置为不选中
//                        mChoice.selectedTwoAnswer = mChoice.selectedTwoAnswer.replaceAll("B", "");
//                        updataNormal(mChoiceTvB, mChoiceLlB, mChoiceTvB2);
//                    } else {
//                        if (mChoice.selectedTwoAnswer == null) {
//                            mChoice.selectedTwoAnswer = "B";
//                        } else {
//                            mChoice.selectedTwoAnswer += "B";
//                        }
//                        updataSelector(mChoiceTvB, mChoiceLlB, mChoiceTvB2);
//                    }
//                    updataDB();
//                }
//                break;
//            case R.id.fragment_choice_ll_C:
//                if (mIsExplain) {
//                    return;
//                } else {
//                    if (mChoice.selectedTwoAnswer != null && mChoice.selectedTwoAnswer.contains("C")) {
//                        //设置为不选中
//                        mChoice.selectedTwoAnswer = mChoice.selectedTwoAnswer.replaceAll("C", "");
//                        updataNormal(mChoiceTvC, mChoiceLlC, mChoiceTvC2);
//                    } else {
//                        if (mChoice.selectedTwoAnswer == null) {
//                            mChoice.selectedTwoAnswer = "C";
//                        } else {
//                            mChoice.selectedTwoAnswer += "C";
//                        }
//                        updataSelector(mChoiceTvC, mChoiceLlC, mChoiceTvC2);
//                    }
//                    updataDB();
//                }
//                break;
//            case R.id.fragment_choice_ll_D:
//                if (mIsExplain) {
//                    return;
//                } else {
//                    if (mChoice.selectedTwoAnswer != null && mChoice.selectedTwoAnswer.contains("D")) {
//                        //设置为不选中
//                        mChoice.selectedTwoAnswer = mChoice.selectedTwoAnswer.replaceAll("D", "");
//                        updataNormal(mChoiceTvD, mChoiceLlD, mChoiceTvD2);
//                    } else {
//                        if (mChoice.selectedTwoAnswer == null) {
//                            mChoice.selectedTwoAnswer = "D";
//                        } else {
//                            mChoice.selectedTwoAnswer += "D";
//                        }
//                        updataSelector(mChoiceTvD, mChoiceLlD, mChoiceTvD2);
//                    }
//                    updataDB();
//                }
//                break;
//        }
//    }
//
//    /**
//     * 选中时的视图
//     *
//     * @param choiceTvA
//     * @param choiceLlA
//     * @param choiceTvA2
//     */
//    private void updataSelector(TextView choiceTvA, LinearLayout choiceLlA, TextView choiceTvA2) {
//        ColorStateList ChoiceABCDSelector = getResources().getColorStateList(R.color.ChoiceABCDSelector);
//        ColorStateList ChoiceABCDNormal = getResources().getColorStateList(R.color.ChoiceABCDNormal);
//        ColorStateList BackgroundABCDNormal = getResources().getColorStateList(R.color.BackgroundABCDNormal);
//        ColorStateList BackgroundNormal = getResources().getColorStateList(R.color.BackgroundNormal);
//        choiceLlA.setBackgroundResource(R.drawable.shape_answer_selector);
//        choiceTvA.setBackgroundResource(R.color.BackgroundABCDSelector);
//        choiceTvA.setTextColor(ChoiceABCDSelector);
//        choiceTvA2.setText("|");
//        choiceTvA2.setTextColor(BackgroundABCDNormal);
//        choiceTvA2.setBackgroundResource(R.color.BackgroundABCDSelector);
//    }
//
//    /**
//     * 匹配用户答题跟答案
//     *
//     * @param choiceTvA
//     * @param choiceLlA
//     * @param choiceTvA2
//     */
//    private void initExplainError(TextView choiceTvA, LinearLayout choiceLlA, TextView choiceTvA2) {
//        ColorStateList ChoiceABCDSelector = getResources().getColorStateList(R.color.ChoiceABCDSelector);
//        ColorStateList ChoiceABCDNormal = getResources().getColorStateList(R.color.ChoiceABCDNormal);
//        ColorStateList BackgroundABCDNormal = getResources().getColorStateList(R.color.BackgroundABCDNormal);
//        ColorStateList BackgroundNormal = getResources().getColorStateList(R.color.BackgroundNormal);
//        if (mQuestionDB.userAnswer.equals(mChoice.answerTwoChoice)) {
//            choiceLlA.setBackgroundResource(R.drawable.shape_answer_selector);
//            choiceTvA.setBackgroundResource(R.color.BackgroundABCDSelector);
//            choiceTvA.setTextColor(ChoiceABCDSelector);
//            choiceTvA2.setTextColor(BackgroundABCDNormal);
//            choiceTvA2.setBackgroundResource(R.color.BackgroundABCDSelector);
//            choiceTvA.setText("　√  ");
//        } else {
//            choiceLlA.setBackgroundResource(R.drawable.shape_answer_explain_error);
//            choiceTvA.setBackgroundResource(R.color.BackgroundABCDSelectorError);
//            choiceTvA.setTextColor(ChoiceABCDSelector);
//            choiceTvA2.setTextColor(BackgroundABCDNormal);
//            choiceTvA2.setBackgroundResource(R.color.BackgroundABCDSelectorError);
//            choiceTvA.setText("　X  ");
//        }
//
//    }
//
//    /**
//     * 显示正确答案
//     *
//     * @param choiceTvA
//     * @param choiceLlA
//     * @param choiceTvA2
//     */
//    private void initExplain(TextView choiceTvA, LinearLayout choiceLlA, TextView choiceTvA2) {
//        ColorStateList ChoiceABCDSelector = getResources().getColorStateList(R.color.ChoiceABCDSelector);
//        ColorStateList ChoiceABCDNormal = getResources().getColorStateList(R.color.ChoiceABCDNormal);
//        ColorStateList BackgroundABCDNormal = getResources().getColorStateList(R.color.BackgroundABCDNormal);
//        ColorStateList BackgroundNormal = getResources().getColorStateList(R.color.BackgroundNormal);
//        choiceLlA.setBackgroundResource(R.drawable.shape_answer_selector);
//        choiceTvA.setBackgroundResource(R.color.BackgroundABCDSelector);
//        choiceTvA.setTextColor(ChoiceABCDSelector);
//        choiceTvA2.setTextColor(BackgroundABCDNormal);
//        choiceTvA2.setBackgroundResource(R.color.BackgroundABCDSelector);
//        choiceTvA.setText("　√  ");
//    }
//
//    /**
//     * 默认时的视图
//     *
//     * @param choiceTvA
//     * @param choiceLlA
//     * @param choiceTvA2
//     */
//    private void updataNormal(TextView choiceTvA, LinearLayout choiceLlA, TextView choiceTvA2) {
//        ColorStateList ChoiceABCDSelector = getResources().getColorStateList(R.color.ChoiceABCDSelector);
//        ColorStateList ChoiceABCDNormal = getResources().getColorStateList(R.color.ChoiceABCDNormal);
//        ColorStateList BackgroundABCDNormal = getResources().getColorStateList(R.color.BackgroundABCDNormal);
//        ColorStateList BackgroundNormal = getResources().getColorStateList(R.color.BackgroundNormal);
//        choiceLlA.setBackgroundResource(R.drawable.shape_answer_normal);
//        choiceTvA.setBackgroundResource(R.color.BackgroundABCDNormal);
//        choiceTvA.setTextColor(ChoiceABCDNormal);
//        choiceTvA2.setText("|");
//        choiceTvA2.setTextColor(BackgroundNormal);
//        choiceTvA2.setBackgroundResource(R.color.BackgroundABCDNormal);
//    }
//
//
//    private void updataDB() {
//        QuestionDB questionDB = mDbUtil.queryAnswer(mChoice.idTwoChoice);
//        if (questionDB == null) {
//            mDbUtil.insertAnswerData(mChoice.idTwoChoice, mChoice.selectedTwoAnswer, null);
//        } else {
//            mDbUtil.updateAnswer(mChoice.idTwoChoice, mChoice.selectedTwoAnswer, null);
//        }
//    }
//
//    private void updateBackgroundABCD(LinearLayout choiceLlA, TextView choiceTvA, TextView choiceTvA2) {
//        mChoiceLlA.setBackgroundResource(R.drawable.shape_answer_normal);
//        mChoiceLlB.setBackgroundResource(R.drawable.shape_answer_normal);
//        mChoiceLlC.setBackgroundResource(R.drawable.shape_answer_normal);
//        mChoiceLlD.setBackgroundResource(R.drawable.shape_answer_normal);
//        choiceLlA.setBackgroundResource(R.drawable.shape_answer_selector);
//
//        ColorStateList ChoiceABCDSelector = getResources().getColorStateList(R.color.ChoiceABCDSelector);
//        ColorStateList ChoiceABCDNormal = getResources().getColorStateList(R.color.ChoiceABCDNormal);
//        ColorStateList BackgroundABCDNormal = getResources().getColorStateList(R.color.BackgroundABCDNormal);
//        ColorStateList BackgroundNormal = getResources().getColorStateList(R.color.BackgroundNormal);
//
//        mChoiceTvA.setBackgroundResource(R.color.BackgroundABCDNormal);
//        mChoiceTvA.setTextColor(ChoiceABCDNormal);
//        mChoiceTvB.setBackgroundResource(R.color.BackgroundABCDNormal);
//        mChoiceTvB.setTextColor(ChoiceABCDNormal);
//        mChoiceTvC.setBackgroundResource(R.color.BackgroundABCDNormal);
//        mChoiceTvC.setTextColor(ChoiceABCDNormal);
//        mChoiceTvD.setBackgroundResource(R.color.BackgroundABCDNormal);
//        mChoiceTvD.setTextColor(ChoiceABCDNormal);
//        choiceTvA.setBackgroundResource(R.color.BackgroundABCDSelector);
//        choiceTvA.setTextColor(ChoiceABCDSelector);
//
//        mChoiceTvA2.setText("|");
//        mChoiceTvA2.setTextColor(BackgroundNormal);
//        mChoiceTvA2.setBackgroundResource(R.color.BackgroundABCDNormal);
//        mChoiceTvB2.setText("|");
//        mChoiceTvB2.setTextColor(BackgroundNormal);
//        mChoiceTvB2.setBackgroundResource(R.color.BackgroundABCDNormal);
//        mChoiceTvC2.setText("|");
//        mChoiceTvC2.setTextColor(BackgroundNormal);
//        mChoiceTvC2.setBackgroundResource(R.color.BackgroundABCDNormal);
//        mChoiceTvD2.setText("|");
//        mChoiceTvD2.setTextColor(BackgroundNormal);
//        mChoiceTvD2.setBackgroundResource(R.color.BackgroundABCDNormal);
//        choiceTvA2.setText("|");
//        choiceTvA2.setTextColor(BackgroundABCDNormal);
//        choiceTvA2.setBackgroundResource(R.color.BackgroundABCDSelector);
//    }
//
//    public class MyTagHandler implements Html.TagHandler {
//
//        @Override
//        public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
//
//            // 处理标签<img>
//            if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
//                // 获取长度
//                int len = output.length();
//                // 获取图片地址
//                ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
//                String imgURL = images[0].getSource();
//                // 使图片可点击并监听点击事件
//                output.setSpan(new ClickableImage(getActivity(), imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }
//
//        private class ClickableImage extends ClickableSpan {
//
//            private String url;
//            private Context context;
//
//            public ClickableImage(Context context, String url) {
//                this.context = context;
//                this.url = url;
//            }
//
//            @Override
//            public void onClick(View widget) {
//                // 进行图片点击之后的处理
//                Drawable d = Drawable.createFromPath(url);
//                PicDiaLog picDiaLog = new PicDiaLog(getActivity(), d);
//                picDiaLog.show();
//            }
//        }
//    }
//
//    /**
//     * ImageGetter用于text图文混排
//     *
//     * @return
//     */
//    public Html.ImageGetter getImageGetterInstance() {
//        Html.ImageGetter imgGetter = new Html.ImageGetter() {
//            @Override
//            public Drawable getDrawable(String source) {
//                Drawable d = null;
//                int fontH = (int) (getResources().getDimension(
//                        R.dimen.textSizeMedium) * 1.8);
//                //                int id = Integer.parseInt(source);
//                //                Drawable d = getResources().getDrawable(id);
//                d = Drawable.createFromPath(source);
//
//                if (d != null) {
//                    int height = fontH;
//                    int width = (int) ((float) d.getIntrinsicWidth() * 1.5 / (float) d
//                            .getIntrinsicHeight()) * fontH;
//                    //                int width =d.getMinimumWidth();
//                    if (width == 0) {
//                        width = d.getIntrinsicWidth();
//                    }
//                    d.setBounds(0, 0, width, height);
//                }
//                return d;
//            }
//        };
//        return imgGetter;
//    }
}
