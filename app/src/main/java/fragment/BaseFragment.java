package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import activity.MainActivity;
import bean.ExercisesBean;
import bean.QuestionDB;
import db.DBConstants;
import db.DBUtil;
import listener.BaseFragmentListener;
import utils.MyTagHandler;
import utils.SpUtils;
import utils.SpannedUtil;

import static android.text.Html.fromHtml;
import static utils.ImageGetterInstanceUtil.getImageGetterInstance;

/**
 * @author 陈锦业
 * @version $Rev$
 * @time 2017-6-23 17:34
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public abstract class BaseFragment extends Fragment {
    public DBUtil mDbUtil;
    public boolean mIsExplain;
    public boolean isExist;
    public ExercisesBean mAnswer;
    public SpUtils mSpUtils;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDbUtil = new DBUtil(getActivity());
        mSpUtils = new SpUtils(getActivity());
//        mIsExplain = mSpUtils.getBoolean(Constants.isExplain, false);
        mIsExplain = getSpUtilIsExplain();
        isExist = true;
        if (getActivity() instanceof MainActivity) {
            DBConstants.ExamPath = MainActivity.mFilePath;
            ((MainActivity) getActivity()).setBaseListener(new BaseFragmentListener() {
                @Override
                public void baseFragmentListener() {
                    QuestionDB questionDB = mDbUtil.queryAnswer(mAnswer.id);
                    if (questionDB == null) {
                        mDbUtil.insertAnswerData(mAnswer.id, mAnswer.selectedAnswer);
                    }
                }
            });
        } else {
            DBConstants.ExamPath = mDbUtil.queryExamPath(mAnswer.indexID);
        }
        View view = initBaseView(inflater, container);
        return view;
    }

    public abstract View initBaseView(LayoutInflater inflater, ViewGroup container);
    public abstract boolean getSpUtilIsExplain();

    public void initData(ExercisesBean answer) {
        mAnswer = answer;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isExist = false;
    }

    public Spanned processSpanned(String str){
        Spanned spanned = fromHtml(str.replaceAll("\n", "<br>"), getImageGetterInstance(getActivity()), new MyTagHandler(getActivity()));
        SpannedUtil.spannedUtil(spanned);
        return spanned;
    }

}
