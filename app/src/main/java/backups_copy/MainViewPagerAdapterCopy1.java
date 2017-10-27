package backups_copy;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import bean.QuestionAnswer;
import bean.QuestionChoice;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-21 11:45
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class MainViewPagerAdapterCopy1 extends PagerAdapter {
    private List<QuestionAnswer> mAnswerList;
    private List<QuestionChoice> mChoiceList;

    public MainViewPagerAdapterCopy1(List<QuestionChoice> choicesList, List<QuestionAnswer> answerList) {
        mAnswerList = answerList;
        mChoiceList = choicesList;
    }

    @Override
    public int getCount() {
        if (mChoiceList == null && mAnswerList ==null){
            return 0;
        }
        return mAnswerList.size() + mChoiceList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position < mChoiceList.size()) {
        }
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}
