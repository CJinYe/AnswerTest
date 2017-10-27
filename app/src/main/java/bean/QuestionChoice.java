package bean;

/**
 * 保存数据库数据
 * Created by LGL on 2016/6/4.
 */
public class QuestionChoice {

    /**
     * 对应的就是Filter1-7  还有一个选中答案
     */

    //编号
    public int idChoice;
    //问题
    public String questionChoice;
    //四个选项
    public String answerA;
    public String answerB;
    public String answerC;
    public String answerD;
    //答案
    public String answerChoice;
    //详情
    public String explainationChoice;

    //用户选中的答案
    public String selectedAnswer;

}
