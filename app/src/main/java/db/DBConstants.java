package db;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-23 17:12
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class DBConstants {
    public static String ExamintaionName = "test";
    public static String UnitName = "test";
    public static String ExamPath;

    public static final String DB_NAME = "examination.db";//数据库名
    public static final int DB_VERSION = 1;//数据库版本

    public static final String TABLE_USER_ANSWER_DATA = "answer";//用户的答案表名
    public static final String QUESTION_ID = "id";//id
    public static final String SELECTOR_ANSWER = "user_answer";//用户选择的答案
    public static final String ANSWER_IMG = "img_path";//用户作答的图片
    public static final String ERROR_EXAMINATION = "error_examination";//是否加入错题库

    //-----------------------------

    public static final String TABLE_EXAMINATION_DATA = "examination_data";//试卷信息的表名
    public static final String EXAMINATION_DATA_NAME = "examination_name";//试卷名字(唯一)
    public static final String EXAMINATION_DATA_UNIT = "unit";//单元
    public static final String EXAMINATION_DATA_UNIT_NAME = "unit_name";//单元名称
    public static final String EXAMINATION_DATA_ANSWER_TIME = "answer_time";//作答总时间
    public static final String EXAMINATION_DATA_USER_ANSWER_TIME = "user_answer_time";//用户已经作答时间
    public static final String EXAMINATION_DATA_SUBMIT_TIME = "submit_time";//作答提交时间

    //---------------------------

    public static final String TABLE_ERROR_EXAMINATION = "error_examination";
    public static final String ERROR_SUBJECT = "subject";
    public static final String ERROR_ANALYSIS = "analysis";
    public static final String ERROR_SCORE = "score";
    public static final String ERROR_TYPE = "type";
    public static final String ERROR_ANSWER = "answer";
    public static final String ERROR_SOUND = "sound";
    public static final String ERROR_EXAM_PATH = "exam_path";
    public static final String ERROR_ADD_TIME = "add_time";

    //用户的答案表
    public static final String CREATE_TABLE_USER_ANSWER_DATA = "create table " +
            DBConstants.TABLE_USER_ANSWER_DATA + "(" +
            "_id integer primary key autoincrement," +
            DBConstants.EXAMINATION_DATA_NAME + " varchar,"+
            DBConstants.EXAMINATION_DATA_UNIT + " varchar,"+
            DBConstants.QUESTION_ID + " integer," +
            DBConstants.SELECTOR_ANSWER + " varchar," +
            DBConstants.ERROR_EXAMINATION + " varchar," +
            DBConstants.ANSWER_IMG + " varchar);";

    //课本信息表
    public static final String CREATE_TABLE_EXAMINATION_DATA = "create table "+
            TABLE_EXAMINATION_DATA + "("+
            "_id integer primary key autoincrement,"+
            DBConstants.EXAMINATION_DATA_NAME + " varchar,"+
            DBConstants.EXAMINATION_DATA_UNIT + " varchar,"+
            DBConstants.EXAMINATION_DATA_ANSWER_TIME + " varchar,"+
            DBConstants.EXAMINATION_DATA_USER_ANSWER_TIME + " varchar,"+
            DBConstants.EXAMINATION_DATA_SUBMIT_TIME + " varchar);";

    //错题库表
    public static final String CREATE_TABLE_ERROR_EXAMINATION = "create table "+
            TABLE_ERROR_EXAMINATION + "("+
            "_id integer primary key autoincrement,"+
            DBConstants.EXAMINATION_DATA_NAME + " varchar,"+
            DBConstants.EXAMINATION_DATA_UNIT + " varchar,"+
            DBConstants.EXAMINATION_DATA_UNIT_NAME + " varchar,"+
            DBConstants.ERROR_SUBJECT + " varchar,"+
            DBConstants.ERROR_ANALYSIS + " varchar,"+
            DBConstants.ERROR_SOUND + " varchar,"+
            DBConstants.ERROR_SCORE + " varchar,"+
            DBConstants.ERROR_TYPE + " integer,"+
            DBConstants.ERROR_ANSWER + " varchar,"+
            DBConstants.ERROR_EXAM_PATH + " varchar,"+
            DBConstants.ERROR_ADD_TIME + " TIMESTAMP,"+
            DBConstants.QUESTION_ID + " integer," +
            DBConstants.SELECTOR_ANSWER + " varchar);";

}
