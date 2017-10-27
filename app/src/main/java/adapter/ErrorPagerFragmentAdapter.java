package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import bean.ExercisesBean;
import fragment.ErrorExam.AnswerFragmentError;
import fragment.ErrorExam.AnswerTextFragmentError;
import fragment.ErrorExam.ChoiceFragmentError;
import fragment.ErrorExam.JudgeFragmentError;
import fragment.ErrorExam.LigatureFragmentError;
import fragment.ErrorExam.MoreChoiceFragmentError;
import fragment.ErrorExam.PadFragmentNormalError;
import fragment.ErrorExam.PadNoteFragmentError;
import fragment.ErrorExam.SubmitFragmentError;
import fragment.Examination.PadFragmentSubjectivitylExam;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 13:56
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class ErrorPagerFragmentAdapter extends FragmentStatePagerAdapter {

    private List<ExercisesBean> exercisesBeanList;

    public ErrorPagerFragmentAdapter(FragmentManager supportFragmentManager,
                                     List<ExercisesBean> exercisesBeanList) {
        super(supportFragmentManager);
        this.exercisesBeanList = exercisesBeanList;
    }

    @Override
    public Fragment getItem(int position) {
        if (position-exercisesBeanList.size()==0){
            SubmitFragmentError fragment = new SubmitFragmentError();
            fragment.init(exercisesBeanList);
            return fragment;
        }

        ExercisesBean exercises =  exercisesBeanList.get(position);

        switch (exercises.type){
            case 0://选择题
                ChoiceFragmentError fragment = new ChoiceFragmentError();
                fragment.initData(exercises);
                return fragment;
            case 3://标准答案填空题(客观)
                PadFragmentNormalError fragment1 = new PadFragmentNormalError();
                fragment1.initData(exercises);
                return fragment1;
            case 31://图文混排填空题
                PadNoteFragmentError padNoteFragment = new PadNoteFragmentError();
                padNoteFragment.initData(exercises);
                return padNoteFragment;
            case 32://非标准答案填空题(主观)
                PadFragmentSubjectivitylExam padFragmentSubjectivity = new PadFragmentSubjectivitylExam();
                padFragmentSubjectivity.initData(exercises);
                return padFragmentSubjectivity;
            case 2://判断题
                JudgeFragmentError judgeFragment = new JudgeFragmentError();
                judgeFragment.initData(exercises);
                return judgeFragment;
            case 101://连线题
                LigatureFragmentError ligatureFragment = new LigatureFragmentError();
                ligatureFragment.initData(exercises);
                return ligatureFragment;
            case 102://作答题
            case 4://作答题
                AnswerFragmentError answerFragment = new AnswerFragmentError();
                answerFragment.initData(exercises);
                return answerFragment;
            case 41://纯文本作答题
                AnswerTextFragmentError textFragment = new AnswerTextFragmentError();
                textFragment.initData(exercises);
                return textFragment;
            case 1://多选题
                MoreChoiceFragmentError moreChoiceFragment = new MoreChoiceFragmentError();
                moreChoiceFragment.initData(exercises);
                return moreChoiceFragment;
        }


        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        Fragment fragment = (Fragment) super.instantiateItem(container,position);
//        String fragmentTag = fragment.getTag();

        return super.instantiateItem(container, position);
    }

    protected void submitItemsClick(int idChoice) {

    }

    @Override
    public int getCount() {
        if (exercisesBeanList == null) {
            return 0;
        }
        return exercisesBeanList.size()+ 1;
    }
}
