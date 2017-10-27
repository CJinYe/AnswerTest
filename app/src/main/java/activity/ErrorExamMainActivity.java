package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.icox.exercises.R;

import java.util.ArrayList;

import adapter.ErrorListAdapter;
import adapter.SpinnerAdapter;
import bean.ExercisesBean;
import butterknife.ButterKnife;
import butterknife.InjectView;
import db.DBUtil;
import listener.ErrorItemListener;
import view.DividerItemDecoration;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-24 11:40
 * @des ${错题库}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class ErrorExamMainActivity extends Activity implements View.OnClickListener {
    @InjectView(R.id.error_btn_go_answer)
    Button mErrorBtnGoAnswer;
    @InjectView(R.id.error_recycler)
    RecyclerView mRecyclerView;
    @InjectView(R.id.error_tv_check_number)
    TextView mErrorTvCheckNumber;
    @InjectView(R.id.error_btn_check_all)
    Button mErrorBtnCheckAll;
    @InjectView(R.id.error_btn_cancel)
    Button mErrorBtnCancel;
    @InjectView(R.id.error_btn_delete)
    Button mErrorBtnDelete;
    @InjectView(R.id.error_spinner_selector_type)
    Spinner mSpinnerSelector;
    @InjectView(R.id.error_spinner_selector_date)
    Spinner mSpinnerSelectorDate;
    private DBUtil mDbUtil;
    private ErrorListAdapter mErrorListAdapter;

    private int checkNumber = 0;
    private ArrayList<ExercisesBean> mExercisesBeenList;

    private String[] mSpinners;
    private String spinnerSelector = null;
    private String spinnerSelectorDate = null;
    private String[] mSpinnersDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_error_main);
        ButterKnife.inject(this);
        mDbUtil = new DBUtil(this);
        initView();
        initListener();
        initAdapter();
    }

    private void initAdapter() {
        String spinnerStr = "全部|";
        for (ExercisesBean bean : mExercisesBeenList) {
            if (bean.ExaminationName.contains("语文")) {
                if (!spinnerStr.contains("语文"))
                    spinnerStr = spinnerStr + "语文|";
            } else if (bean.ExaminationName.contains("数学")) {
                if (!spinnerStr.contains("数学"))
                    spinnerStr = spinnerStr + "数学|";
            } else if (bean.ExaminationName.contains("英语")) {
                if (!spinnerStr.contains("英语"))
                    spinnerStr = spinnerStr + "英语|";
            } else if (bean.ExaminationName.contains("化学")) {
                if (!spinnerStr.contains("化学"))
                    spinnerStr = spinnerStr + "化学|";
            } else if (bean.ExaminationName.contains("物理")) {
                if (!spinnerStr.contains("物理"))
                    spinnerStr = spinnerStr + "物理|";
            } else if (bean.ExaminationName.contains("生物")) {
                if (!spinnerStr.contains("生物"))
                    spinnerStr = spinnerStr + "生物|";
            } else if (bean.ExaminationName.contains("地理")) {
                if (!spinnerStr.contains("地理"))
                    spinnerStr = spinnerStr + "地理|";
            } else if (bean.ExaminationName.contains("历史")) {
                if (!spinnerStr.contains("历史"))
                    spinnerStr = spinnerStr + "历史|";
            } else if (bean.ExaminationName.contains("政治")) {
                if (!spinnerStr.contains("政治"))
                    spinnerStr = spinnerStr + "政治|";
            } else if (bean.ExaminationName.contains("文综")) {
                if (!spinnerStr.contains("文综"))
                    spinnerStr = spinnerStr + "文综|";
            } else if (bean.ExaminationName.contains("理综")) {
                if (!spinnerStr.contains("理综"))
                    spinnerStr = spinnerStr + "理综|";
            }
        }

        mSpinners = spinnerStr.split("\\|");
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, mSpinners);
        mSpinnerSelector.setAdapter(spinnerAdapter);

        mSpinnersDate = getResources().getStringArray(R.array.spinnerDate);
        SpinnerAdapter spinnerDateAdapter = new SpinnerAdapter(this, mSpinnersDate);
        mSpinnerSelectorDate.setAdapter(spinnerDateAdapter);

        if (mSpinners.length > 1) {
            mSpinnerSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSelector = mSpinners[position];
                    mErrorListAdapter.setSelector(spinnerSelector, spinnerSelectorDate);
                    checkNumber = 0;
                    for (ExercisesBean bean : mErrorListAdapter.mBeanList) {
                        if (bean.isChecked) {
                            checkNumber++;
                        }
                    }
                    mErrorTvCheckNumber.setText("已选择 : " + checkNumber);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        mSpinnerSelectorDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectorDate = mSpinnersDate[position];
                mErrorListAdapter.setSelector(spinnerSelector,spinnerSelectorDate);
                checkNumber = 0;
                for (ExercisesBean bean : mErrorListAdapter.mBeanList) {
                    if (bean.isChecked) {
                        checkNumber++;
                    }
                }
                mErrorTvCheckNumber.setText("已选择 : " + checkNumber);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initListener() {
        mErrorListAdapter.setOnItemListener(new ErrorItemListener() {
            @Override
            public void errorItemListener(int position) {
                if (mErrorListAdapter.mBeanList.get(position).isChecked) {
                    checkNumber += 1;
                } else {
                    checkNumber -= 1;
                }

                mErrorTvCheckNumber.setText("已选择 : " + checkNumber);
            }

            @Override
            public void errorItemLongListener(int position) {
                goAnswer(position);
            }
        });

    }

    private void initView() {
        mExercisesBeenList = mDbUtil.queryAllErrorExamination();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        mErrorListAdapter = new ErrorListAdapter(mExercisesBeenList, mDbUtil, spinnerSelector);
        mRecyclerView.setAdapter(mErrorListAdapter);

        mErrorBtnCancel.setOnClickListener(this);
        mErrorBtnCheckAll.setOnClickListener(this);
        mErrorBtnGoAnswer.setOnClickListener(this);
        mErrorBtnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.error_btn_cancel://取消全选
                //                for (ExercisesBean benn : mErrorListAdapter.mBeanList) {
                //                    benn.isChecked = false;
                //                }
                mErrorListAdapter.cancelCheckAll();
                checkNumber = 0;
                mErrorTvCheckNumber.setText("已选择 : " + checkNumber);
                mErrorListAdapter.notifyDataSetChanged();
                break;
            case R.id.error_btn_check_all://全选
                //                for (ExercisesBean benn : mErrorListAdapter.mBeanList) {
                //                    benn.isChecked = true;
                //                }
                mErrorListAdapter.checkAll();
                checkNumber = mErrorListAdapter.mBeanList.size();
                mErrorTvCheckNumber.setText("已选择 : " + checkNumber);
                mErrorListAdapter.notifyDataSetChanged();
                break;
            case R.id.error_btn_delete://删除
                if (checkNumber <= 0)
                    return;

                ArrayList<ExercisesBean> list = new ArrayList<>();
                for (int i = 0; i < mErrorListAdapter.getBeanList().size(); i++) {
                    ExercisesBean bean = mErrorListAdapter.getBeanList().get(i);
                    if (bean.isChecked) {
                        mDbUtil.removeCheckErrorExamination(bean.ExaminationName, bean.unit, bean.id);
                    } else {
                        list.add(bean);
                    }
                }

                mErrorListAdapter = new ErrorListAdapter(list, mDbUtil, spinnerSelector);
                mRecyclerView.setAdapter(mErrorListAdapter);
                initListener();

                checkNumber = 0;
                mErrorTvCheckNumber.setText("已选择 : " + checkNumber);
                break;
            case R.id.error_btn_go_answer://做题
                goAnswer();
                break;
            default:
                break;
        }
    }

    private void goAnswer() {
        ArrayList<ExercisesBean> list = new ArrayList<>();
        for (ExercisesBean bean : mErrorListAdapter.mBeanList) {
            if (bean.isChecked) {
                list.add(bean);
            }
        }

        if (list.size() < 1) {
            return;
        }

        Intent intent = new Intent(this, ErrorExamActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ErrorList", list);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void goAnswer(int position) {
        ArrayList<ExercisesBean> list = new ArrayList<>();
        list.add(mErrorListAdapter.mBeanList.get(position));
        Intent intent = new Intent(this, ErrorExamActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ErrorList", list);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void initWindow() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
