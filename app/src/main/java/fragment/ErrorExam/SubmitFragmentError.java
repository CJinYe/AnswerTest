package fragment.ErrorExam;

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

import activity.ErrorExamActivity;
import adapter.submit.SubmitItemAdapterError;
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
public class SubmitFragmentError extends Fragment implements AdapterView.OnItemClickListener {
    @InjectView(R.id.fragment_submit_gv)
    GridView mFragmentSubmitGv;

    private List<ExercisesBean> exercisesBeen;
    private List<ExercisesBean> mChoice = new ArrayList<>(),
            mFilling = new ArrayList<>(),
            mJudge = new ArrayList<>(),
            mAnswer = new ArrayList<>(),
            mLigature = new ArrayList<>(),
            mPadNote = new ArrayList<>();
    private View mView;
    private DBUtil mDBUtil;
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
        ErrorExamActivity mainActivity = (ErrorExamActivity) getActivity();
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

//        mFragmentSubmitGv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mFilling.get(0).id == 1) {
//                    int idChoice = mChoice.get(position).id;
//                    mViewPager.setCurrentItem(idChoice - 1);
//                } else {
//                    int idChoice = mFilling.get(position).id;
//                    mViewPager.setCurrentItem(idChoice - 1);
//                }
//            }
//        });
//
//        mFragmentSubmitGv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int idChoice = mJudge.get(position).id;
//                mViewPager.setCurrentItem(idChoice - 1);
//            }
//        });
//        mFragmentSubmitGv4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int idChoice = mLigature.get(position).id;
//                mViewPager.setCurrentItem(idChoice - 1);
//            }
//        });
//        mFragmentSubmitGv5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int idChoice = mAnswer.get(position).id;
//                mViewPager.setCurrentItem(idChoice - 1);
//            }
//        });


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

            if (mDBUtil != null) {
                QuestionDB questionDB = mDBUtil.queryErrorAnswer(ex.indexID);
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

        SubmitItemAdapterError choice = new SubmitItemAdapterError(getActivity(), exercisesBeen);
        mFragmentSubmitGv.setAdapter(choice);

        //        if (mChoice.size() != 0) {
        //            if (mChoice.get(0).id == 1) {
        //                mFragmentSubmitTvTitle.setVisibility(View.VISIBLE);
        //                mFragmentSubmitTvTitle.setText("选择题");
        //                SubmitItemAdapter choice = new SubmitItemAdapter(getActivity(), mChoice);
        //                mFragmentSubmitGv.setAdapter(choice);
        //            } else {
        //                mFragmentSubmitTvTitle2.setVisibility(View.VISIBLE);
        //                mFragmentSubmitTvTitle2.setText("选择题");
        //                SubmitItemAdapter choice = new SubmitItemAdapter(getActivity(), mChoice);
        //                mFragmentSubmitGv2.setAdapter(choice);
        //            }
        //        }
        //
        //        if (mFilling.size() != 0) {
        //            if (mFilling.get(0).id == 1) {
        //                mFragmentSubmitTvTitle.setVisibility(View.VISIBLE);
        //                mFragmentSubmitTvTitle.setText("填空题");
        //                SubmitItemAdapter choice = new SubmitItemAdapter(getActivity(), mFilling);
        //                mFragmentSubmitGv.setAdapter(choice);
        //            } else {
        //                mFragmentSubmitTvTitle2.setVisibility(View.VISIBLE);
        //                mFragmentSubmitTvTitle2.setText("填空题");
        //                SubmitItemAdapter choice = new SubmitItemAdapter(getActivity(), mFilling);
        //                mFragmentSubmitGv2.setAdapter(choice);
        //            }
        //        }
        //
        //        if (mJudge.size() != 0) {
        //            mFragmentSubmitTvTitle3.setVisibility(View.VISIBLE);
        //            mFragmentSubmitTvTitle3.setText("判断题");
        //            SubmitItemAdapter choice = new SubmitItemAdapter(getActivity(), mJudge);
        //            mFragmentSubmitGv3.setAdapter(choice);
        //        }
        //        if (mLigature.size() != 0) {
        //            mFragmentSubmitTvTitle4.setVisibility(View.VISIBLE);
        //            mFragmentSubmitTvTitle4.setText("连线题");
        //            SubmitItemAdapter choice = new SubmitItemAdapter(getActivity(), mLigature);
        //            mFragmentSubmitGv4.setAdapter(choice);
        //        }
        //        if (mAnswer.size() != 0) {
        //            mFragmentSubmitTvTitle5.setVisibility(View.VISIBLE);
        //            mFragmentSubmitTvTitle5.setText("作答题");
        //            AnswerSubmitItemAdapter choice = new AnswerSubmitItemAdapter(getActivity(),mAnswer);
        //            mFragmentSubmitGv5.setAdapter(choice);
        //        }


        if (exercisesBeen == null || exercisesBeen.size() == 0) {
            mFragmentSubmitGv.setVisibility(View.GONE);
        } else {
            mFragmentSubmitGv.setVisibility(View.VISIBLE);
        }


        //        mFragmentSubmitGv.setFocusable(false);
        //        mFragmentSubmitGv2.setFocusable(false);
        //        mFragmentSubmitGv3.setFocusable(false);
        //        mFragmentSubmitGv4.setFocusable(false);
        //        mFragmentSubmitGv5.setFocusable(false);
        //        setGrideViewHeightBasedOnChildren(mFragmentSubmitGv);
        //        setGrideViewHeightBasedOnChildren(mFragmentSubmitGv2);
        //        setGrideViewHeightBasedOnChildren(mFragmentSubmitGv3);
        //        setGrideViewHeightBasedOnChildren(mFragmentSubmitGv4);
        //        setGrideViewHeightBasedOnChildren(mFragmentSubmitGv5);
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
