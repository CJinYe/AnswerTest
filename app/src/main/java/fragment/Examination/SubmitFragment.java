package fragment.Examination;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.icox.exercises.R;

import java.util.ArrayList;
import java.util.List;

import activity.MainActivity;
import adapter.submit.SubmitItemAdapter;
import bean.ExercisesBean;
import bean.QuestionDB;
import butterknife.ButterKnife;
import butterknife.InjectView;
import db.DBUtil;
import view.NoPreloadViewPager;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-24 16:05
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class SubmitFragment extends Fragment implements AdapterView.OnItemClickListener {
    @InjectView(R.id.fragment_submit_gv)
    GridView mFragmentSubmitGv;

//    @InjectView(R.id.fragment_submit_tv_title)
//    TextView mFragmentSubmitTvTitle;
//    @InjectView(R.id.fragment_submit_tv_title2)
//    TextView mFragmentSubmitTvTitle2;
//    @InjectView(R.id.fragment_submit_gv2)
//    view.MyGridView mFragmentSubmitGv2;
//    @InjectView(R.id.fragment_submit_tv_title3)
//    TextView mFragmentSubmitTvTitle3;
//    @InjectView(R.id.fragment_submit_gv3)
//    view.MyGridView mFragmentSubmitGv3;
//    @InjectView(R.id.fragment_submit_tv_title4)
//    TextView mFragmentSubmitTvTitle4;
//    @InjectView(R.id.fragment_submit_gv4)
//    view.MyGridView mFragmentSubmitGv4;
//    @InjectView(R.id.fragment_submit_tv_title5)
//    TextView mFragmentSubmitTvTitle5;
//    @InjectView(R.id.fragment_submit_gv5)
//    view.MyGridView mFragmentSubmitGv5;

    private List<ExercisesBean> exercisesBeen;
    private List<ExercisesBean> mChoice = new ArrayList<>(),
            mFilling = new ArrayList<>(),
            mJudge = new ArrayList<>(),
            mAnswer = new ArrayList<>(),
            mLigature = new ArrayList<>(),
            mPadNote = new ArrayList<>();
    private View mView;
    private DBUtil mDBUtil;
    private SubmitItemAdapter mAnswerSubmitItemAdapter;
    private boolean isExist;//当前页面是否已经创建
    private NoPreloadViewPager mViewPager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_submit, container, false);
        ButterKnife.inject(this, mView);
        isExist = true;
        mDBUtil = new DBUtil(getActivity());
        initView();
        initData();
        initListener();
        MainActivity mainActivity = (MainActivity) getActivity();
        mViewPager = mainActivity.getMainViewPager();
        return mView;
    }

    public void init(List<ExercisesBean> exercisesBeen) {
        this.exercisesBeen = exercisesBeen;
    }

    private void initListener() {
        mFragmentSubmitGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mViewPager.setCurrentItem(position);
            }
        });



    }

    private void initData() {
    }

    private void initView() {

        mChoice.clear();
        mFilling.clear();
        mJudge.clear();
        mAnswer.clear();
        mLigature.clear();
        mPadNote.clear();


        for (int i = 0; i < exercisesBeen.size(); i++) {
            ExercisesBean ex = exercisesBeen.get(i);

            if (mDBUtil != null && ex.id != 0) {
                QuestionDB questionDB = mDBUtil.queryAnswer(ex.id);
                if (questionDB != null) {
                    ex.selectedAnswer = questionDB.userAnswer;
                }
            }

            switch (ex.type) {
                case 0://选择题
                    mChoice.add(ex);
                    break;
                case 3://填空题
                    mFilling.add(ex);
                    break;
                case 31://图文填空题
                    mPadNote.add(ex);
                    break;
                case 2://判断题
                    mJudge.add(ex);
                    break;
                case 101://连线题
                    mLigature.add(ex);
                    break;
                case 102:
                case 4://作答题
                    mAnswer.add(ex);
                    break;
            }
        }

        SubmitItemAdapter choice = new SubmitItemAdapter(getActivity(), exercisesBeen);
        mFragmentSubmitGv.setAdapter(choice);



        if (exercisesBeen == null || exercisesBeen.size() == 0) {
            mFragmentSubmitGv.setVisibility(View.GONE);
        } else {
            mFragmentSubmitGv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isExist = false;
        ButterKnife.reset(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        //        initView();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int idChoice = exercisesBeen.get(position).id;
        mViewPager.setCurrentItem(idChoice - 1);
    }

    public void setGrideViewHeightBasedOnChildren(GridView grid) {
        ListAdapter adapter = grid.getAdapter();
        if (adapter == null) {
            return;
        }

        int totalHeight = 0;

        View listItem = adapter.getView(0, null, grid);
        listItem.measure(0, 0);
        if (adapter.getCount() - 1 < 0) {
            totalHeight = listItem.getMeasuredHeight();
        } else {
            int line = adapter.getCount() / 3;
            if (adapter.getCount() % 3 != 0)
                line = line + 1;
            totalHeight = (listItem.getMeasuredHeight() + 30) * line;
        }

        ViewGroup.LayoutParams params = grid.getLayoutParams();
        params.height = totalHeight + 30;
        //        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        grid.setLayoutParams(params);

    }

}
