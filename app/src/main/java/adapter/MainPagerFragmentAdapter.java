package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import bean.ExercisesBean;
import fragment.Examination.AnswerFragmentExam;
import fragment.Examination.AnswerTextFragmentExam;
import fragment.Examination.ChoiceFragmentExam;
import fragment.Examination.JudgeFragmentExam;
import fragment.Examination.LigatureFragmentExam;
import fragment.Examination.MoreChoiceFragmentExam;
import fragment.Examination.PadFragmentNormalExam;
import fragment.Examination.PadFragmentSubjectivitylExam;
import fragment.Examination.PadNoteFragmentExam;
import fragment.Examination.SubmitFragment;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 13:56
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class MainPagerFragmentAdapter extends FragmentStatePagerAdapter {

    private List<ExercisesBean> exercisesBeanList;

    public MainPagerFragmentAdapter(FragmentManager supportFragmentManager,
                                    List<ExercisesBean> exercisesBeanList) {
        super(supportFragmentManager);
        this.exercisesBeanList = exercisesBeanList;
    }

    @Override
    public Fragment getItem(int position) {
        if (position-exercisesBeanList.size()==0){
            SubmitFragment fragment = new SubmitFragment();
            fragment.init(exercisesBeanList);
            return fragment;
        }

        ExercisesBean exercises =  exercisesBeanList.get(position);

        switch (exercises.type){
            case 0://选择题
                ChoiceFragmentExam choiceFragment = new ChoiceFragmentExam();
                choiceFragment.initData(exercises);
                return choiceFragment;
            case 3://标准答案填空题(客观)
                PadFragmentNormalExam padFragmentNormal = new PadFragmentNormalExam();
                padFragmentNormal.initData(exercises);
                return padFragmentNormal;
            case 31://图文混排填空题
                PadNoteFragmentExam padNoteFragment = new PadNoteFragmentExam();
                padNoteFragment.initData(exercises);
                return padNoteFragment;
            case 32://非标准答案填空题(主观)
                PadFragmentSubjectivitylExam padFragmentSubjectivity = new PadFragmentSubjectivitylExam();
                padFragmentSubjectivity.initData(exercises);
                return padFragmentSubjectivity;
            case 2://判断题
                JudgeFragmentExam judgeFragment = new JudgeFragmentExam();
                judgeFragment.initData(exercises);
                return judgeFragment;
            case 101://连线题
                LigatureFragmentExam ligatureFragment = new LigatureFragmentExam();
                ligatureFragment.initData(exercises);
                return ligatureFragment;
            case 102://作答题
            case 4://作答题
                AnswerFragmentExam answerFragment = new AnswerFragmentExam();
                answerFragment.initData(exercises);
                return answerFragment;
            case 41://纯文本作答题
                AnswerTextFragmentExam textFragment = new AnswerTextFragmentExam();
                textFragment.initData(exercises);
                return textFragment;
            case 1://多选题
                MoreChoiceFragmentExam moreChoiceFragment = new MoreChoiceFragmentExam();
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
