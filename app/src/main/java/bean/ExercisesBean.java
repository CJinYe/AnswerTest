package bean;

import java.io.Serializable;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-3-8 18:02
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class ExercisesBean implements Serializable {
    public String name;
    public int size;
    public int note;
    public int id;
    public String subject;
    public String analysis;
    public String score;
    public int type;
    public String answer;
    public String sound;

    public String selectedAnswer;
    public String soundPath;

    //-----------------------------------------------------

    /**
     * 试卷多出来的字段
     */
    public String h1;
    public String h2;
    public String h3;
    public String h4;
    public String h5;
    public String alysis1;
    //-----------------------------------------------------

    //是否是选中的
    public boolean isChecked = false;
    //课本信息
    public String ExaminationName;
    //单元
    public String unit;
    //试卷的路径
    public String examPath;
    //唯一索引ID
    public int indexID;
    //加入错题集的时间
    public long addTime;

}
