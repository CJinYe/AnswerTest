package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import activity.MainActivity;
import bean.ExercisesBean;
import bean.QuestionDB;
import conf.Constants;
import utils.DateTimeUtil;

import static db.DBConstants.DB_NAME;
import static db.DBConstants.ERROR_ADD_TIME;
import static db.DBConstants.ERROR_ANALYSIS;
import static db.DBConstants.ERROR_ANSWER;
import static db.DBConstants.ERROR_EXAM_PATH;
import static db.DBConstants.ERROR_SCORE;
import static db.DBConstants.ERROR_SOUND;
import static db.DBConstants.ERROR_SUBJECT;
import static db.DBConstants.ERROR_TYPE;
import static db.DBConstants.EXAMINATION_DATA_ANSWER_TIME;
import static db.DBConstants.EXAMINATION_DATA_NAME;
import static db.DBConstants.EXAMINATION_DATA_SUBMIT_TIME;
import static db.DBConstants.EXAMINATION_DATA_UNIT;
import static db.DBConstants.EXAMINATION_DATA_UNIT_NAME;
import static db.DBConstants.ExamintaionName;
import static db.DBConstants.QUESTION_ID;
import static db.DBConstants.SELECTOR_ANSWER;
import static db.DBConstants.TABLE_ERROR_EXAMINATION;
import static db.DBConstants.TABLE_EXAMINATION_DATA;
import static db.DBConstants.TABLE_USER_ANSWER_DATA;
import static db.DBConstants.UnitName;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-23 17:28
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class DBUtil {

    private final DBHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static String name;

    public DBUtil(Context context) {
        mDbHelper = new DBHelper(context, DB_NAME, null, 0);
    }

    /**
     * 插入用户的答案
     *  @param id
     * @param selectorAnswer
     */
    public long insertAnswerData(int id, String selectorAnswer) {
        mDb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.QUESTION_ID, id);
        values.put(SELECTOR_ANSWER, selectorAnswer);
        values.put(DBConstants.EXAMINATION_DATA_NAME, DBConstants.ExamintaionName);
        values.put(DBConstants.EXAMINATION_DATA_UNIT, DBConstants.UnitName);
        values.put(DBConstants.ERROR_EXAMINATION, "false");
        long mun = mDb.insert(DBConstants.TABLE_USER_ANSWER_DATA, null, values);
        mDb.close();
        return mun;
    }

    /**
     * 插入课本信息
     *
     * @param time       作答的总时间
     * @param submitTime 已经作答的时间
     */
    public long insertExaminationData(String time, String submitTime) {
        mDb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EXAMINATION_DATA_NAME, ExamintaionName);
        values.put(EXAMINATION_DATA_UNIT, UnitName);
        values.put(EXAMINATION_DATA_ANSWER_TIME, time);
        values.put(EXAMINATION_DATA_SUBMIT_TIME, submitTime);
        long db = mDb.insert(TABLE_EXAMINATION_DATA, null, values);
        mDb.close();
        return db;
    }

    /**
     * 查询课本信息是否存在
     *
     * @return
     */
    public boolean queryExaminationData() {
        mDb = mDbHelper.getWritableDatabase();
        Cursor cursor = mDb.query(DBConstants.TABLE_EXAMINATION_DATA, null,
                DBConstants.EXAMINATION_DATA_NAME + "=? and " + DBConstants.EXAMINATION_DATA_UNIT + "=?",
                new String[]{DBConstants.ExamintaionName, DBConstants.UnitName}, null, null, null);
        boolean isExist = false;
        if (cursor.moveToNext()) {
            isExist = true;
        }
        cursor.close();
        mDb.close();

        return isExist;
    }

    /**
     * 更新已经作答的时间
     */
    public void updateAnswerTime(String time, String submitTime) {
        mDb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstants.EXAMINATION_DATA_SUBMIT_TIME, submitTime);
        mDb.update(DBConstants.TABLE_EXAMINATION_DATA, values,
                EXAMINATION_DATA_NAME + "=? and " + EXAMINATION_DATA_UNIT + "=?",
                new String[]{ExamintaionName, UnitName});
        mDb.close();
    }


    /**
     * 根据Id查询用户的答案
     *
     * @param id
     */
    public QuestionDB queryAnswer(int id) {
        mDb = mDbHelper.getWritableDatabase();
        Cursor cursor = mDb.query(DBConstants.TABLE_USER_ANSWER_DATA, null,
                DBConstants.EXAMINATION_DATA_NAME + "=? and " + DBConstants.EXAMINATION_DATA_UNIT + "=? and " + DBConstants.QUESTION_ID + "=?",
                new String[]{DBConstants.ExamintaionName, DBConstants.UnitName, id + ""}, null, null, null);
        QuestionDB questionDB = null;
        if (cursor.moveToNext()) {
            questionDB = new QuestionDB();
            questionDB.userAnswer = cursor.getString(cursor.getColumnIndex(SELECTOR_ANSWER));
            String error = cursor.getString(cursor.getColumnIndex(DBConstants.ERROR_EXAMINATION));
            questionDB.errorExamination = !error.equals("false");
        }
        cursor.close();
        mDb.close();
        return questionDB;
    }

    /**
     * 查询错题库的答案
     *
     * @param
     */
    public QuestionDB queryErrorAnswer(int id) {
        mDb = mDbHelper.getWritableDatabase();
        Cursor cursor = mDb.query(DBConstants.TABLE_ERROR_EXAMINATION, null,
                "_id =?",
                new String[]{id + ""}, null, null, null);
        QuestionDB questionDB = null;
        if (cursor.moveToNext()) {
            questionDB = new QuestionDB();
            questionDB.userAnswer = cursor.getString(cursor.getColumnIndex(SELECTOR_ANSWER));
        }
        cursor.close();
        mDb.close();
        return questionDB;
    }

    /**
     * 根据Id查询是否加入了错题集
     *
     * @param id
     */
    public boolean queryIsError(int id) {
        mDb = mDbHelper.getWritableDatabase();
        //先从答案里面查
        Cursor cursor = mDb.query(DBConstants.TABLE_USER_ANSWER_DATA, null,
                DBConstants.EXAMINATION_DATA_NAME + "=? and " + DBConstants.EXAMINATION_DATA_UNIT + "=? and " + DBConstants.QUESTION_ID + "=?",
                new String[]{DBConstants.ExamintaionName, DBConstants.UnitName, id + ""}, null, null, null);
        boolean isError = false;
        if (cursor.moveToNext()) {
            String error = cursor.getString(cursor.getColumnIndex(DBConstants.ERROR_EXAMINATION));
            isError = !error.equals("false");
        }
        cursor.close();

        //再从错题库里面查
        if (!isError) {
            Cursor cursorError = mDb.query(DBConstants.TABLE_ERROR_EXAMINATION, null,
                    DBConstants.EXAMINATION_DATA_NAME + "=? and " + DBConstants.EXAMINATION_DATA_UNIT + "=? and " + DBConstants.QUESTION_ID + "=?",
                    new String[]{DBConstants.ExamintaionName, DBConstants.UnitName, id + ""}, null, null, null);
            if (cursorError.moveToNext()) {
                isError = true;
            }
            cursorError.close();
        }

        mDb.close();
        return isError;
    }

    /**
     * 查询所有错题库的题目
     *
     * @return
     */
    public ArrayList<ExercisesBean> queryAllErrorExamination() {
        mDb = mDbHelper.getWritableDatabase();
        //        Cursor cursor = mDb.query(TABLE_ERROR_EXAMINATION, null, null, null, null, null, null);
        Cursor cursor = mDb.rawQuery("select * from " + TABLE_ERROR_EXAMINATION, null);
        ArrayList<ExercisesBean> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            ExercisesBean bean = new ExercisesBean();
            bean.subject = cursor.getString(cursor.getColumnIndex(ERROR_SUBJECT));
            bean.analysis = cursor.getString(cursor.getColumnIndex(ERROR_ANALYSIS));
            bean.sound = cursor.getString(cursor.getColumnIndex(ERROR_SOUND));
            bean.score = cursor.getString(cursor.getColumnIndex(ERROR_SCORE));
            bean.type = cursor.getInt(cursor.getColumnIndex(ERROR_TYPE));
            bean.answer = cursor.getString(cursor.getColumnIndex(ERROR_ANSWER));
            bean.id = cursor.getInt(cursor.getColumnIndex(QUESTION_ID));
            bean.indexID = cursor.getInt(cursor.getColumnIndex("_id"));
            bean.selectedAnswer = cursor.getString(cursor.getColumnIndex(SELECTOR_ANSWER));
            bean.ExaminationName = cursor.getString(cursor.getColumnIndex(EXAMINATION_DATA_NAME));
            bean.unit = cursor.getString(cursor.getColumnIndex(EXAMINATION_DATA_UNIT));
            bean.examPath = cursor.getString(cursor.getColumnIndex(ERROR_EXAM_PATH));
            bean.addTime = cursor.getLong(cursor.getColumnIndex(ERROR_ADD_TIME));
            bean.name = cursor.getString(cursor.getColumnIndex(EXAMINATION_DATA_UNIT_NAME));
            list.add(bean);
        }
        cursor.close();
        mDb.close();
        return list;
    }

    /**
     * 查询试卷的路径
     *
     * @return
     */
    public String queryExamPath(int id) {
        mDb = mDbHelper.getWritableDatabase();
        Cursor cursor = mDb.query(DBConstants.TABLE_ERROR_EXAMINATION, null,
                "_id =?",
                new String[]{id + ""}, null, null, null);
        String path = "";
        while (cursor.moveToNext()) {
            path = cursor.getString(cursor.getColumnIndex(ERROR_EXAM_PATH));
        }
        cursor.close();
        mDb.close();
        return path;
    }

    /**
     * 查询已经作答的时间
     */
    public long queryAnswerTime() {
        mDb = mDbHelper.getWritableDatabase();
        Cursor cursor = mDb.query(DBConstants.TABLE_EXAMINATION_DATA, null,
                DBConstants.EXAMINATION_DATA_NAME + "=? and " + DBConstants.EXAMINATION_DATA_UNIT + "=?",
                new String[]{DBConstants.ExamintaionName, DBConstants.UnitName}, null, null, null);
        long timeLong = 0;
        if (cursor.moveToNext()) {
            String time = cursor.getString(cursor.getColumnIndex(DBConstants.EXAMINATION_DATA_SUBMIT_TIME));
            try {
                String format = "mm:ss";
                if (time.length() > 5) {
                    format = "HH:mm:ss";
                }
                timeLong = DateTimeUtil.stringToLong(time, format);
            } catch (ParseException e) {
                e.printStackTrace();
                Constants.log("e = " + e, -1);
            }
        }
        cursor.close();
        mDb.close();
        return timeLong;
    }

    /**
     * 查询已经作答的时间
     */
    public long queryAnswerTime(String time) {
        long timeLong = 0;
        try {
            String format = "mm:ss";
            if (time.length() > 5) {
                format = "HH:mm:ss";
            }
            timeLong = DateTimeUtil.stringToLong(time, format);
        } catch (ParseException e) {
            e.printStackTrace();
            Constants.log("e = " + e, -1);
        }
        return timeLong;
    }

    /**
     * 根据id更新答案
     *  @param id
     * @param selectorAnswer
     */
    public int updateAnswer(int id, String selectorAnswer) {
        mDb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SELECTOR_ANSWER, selectorAnswer);
        int num = mDb.update(DBConstants.TABLE_USER_ANSWER_DATA, values,
                DBConstants.QUESTION_ID + "=? and " + EXAMINATION_DATA_NAME + "=? and " + EXAMINATION_DATA_UNIT + "=?",
                new String[]{id + "", ExamintaionName, UnitName});
        mDb.close();
        return num;
    }

    /**
     * 根据id更新错题库答案
     *
     * @param id
     * @param selectorAnswer
     */
    public void updateErrorAnswer(int id, String selectorAnswer) {
        mDb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SELECTOR_ANSWER, selectorAnswer);
        mDb.update(DBConstants.TABLE_ERROR_EXAMINATION, values,
                "_id =?",
                new String[]{id + ""});
        mDb.close();
    }

    /**
     * 查询所有的答案
     *
     * @param
     */
    public List<QuestionDB> inquireAllAnswer() {
        mDb = mDbHelper.getWritableDatabase();
        Cursor cursor = mDb.query(DBConstants.TABLE_USER_ANSWER_DATA, null,
                DBConstants.EXAMINATION_DATA_NAME + "=? and " + DBConstants.EXAMINATION_DATA_UNIT + "=?",
                new String[]{DBConstants.ExamintaionName, DBConstants.UnitName}, null, null, null);
        List<QuestionDB> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            QuestionDB questionDB = new QuestionDB();
            questionDB.id = cursor.getInt(cursor.getColumnIndex(DBConstants.QUESTION_ID));
            questionDB.userAnswer = cursor.getString(cursor.getColumnIndex(SELECTOR_ANSWER));
            String error = cursor.getString(cursor.getColumnIndex(DBConstants.ERROR_EXAMINATION));
            questionDB.errorExamination = !error.equals("false");
            list.add(questionDB);
        }
        cursor.close();
        mDb.close();
        return list;
    }

    /**
     * 重置本单元的所有答案
     */
    public void resetData() {
        //        mDb = mDbHelper.getWritableDatabase();
        //        ContentValues values = new ContentValues();
        //        values.put(DBConstants.SELECTOR_ANSWER,"");
        //        mDb.update(DBConstants.TABLE_USER_ANSWER_DATA,values,
        //                EXAMINATION_DATA_NAME+"=? and "+EXAMINATION_DATA_UNIT+"=?",
        //                new String[]{ExamintaionName,UnitName});
        //        mDb.close();

        List<QuestionDB> questionDBList = inquireAllAnswer();
        for (QuestionDB questionDB : questionDBList) {
            if (questionDB.userAnswer != null) {
                String[] paths = questionDB.userAnswer.split("\\|\\|");
                for (String path : paths) {
                    if (path.contains(Constants.PadNoteAnswerPathTag)) {
                        path = path.replaceAll(Constants.PadNoteAnswerPathTag, "");
                        File file = new File(path);
                        file.delete();
                    }
                }
            }
        }

        mDb = mDbHelper.getWritableDatabase();
        int i = mDb.delete(TABLE_USER_ANSWER_DATA,
                EXAMINATION_DATA_NAME + "=? and " + EXAMINATION_DATA_UNIT + "=?",
                new String[]{ExamintaionName, UnitName});

        ContentValues values = new ContentValues();
        values.put(DBConstants.EXAMINATION_DATA_SUBMIT_TIME, "");
        mDb.update(DBConstants.TABLE_EXAMINATION_DATA, values,
                EXAMINATION_DATA_NAME + "=? and " + EXAMINATION_DATA_UNIT + "=?",
                new String[]{ExamintaionName, UnitName});
        mDb.close();
    }

    /**
     * 添加到错题库
     *
     * @param id
     * @param exercisesBean
     */
    public void addErrorExamination(int id, ExercisesBean exercisesBean) {
        mDb = mDbHelper.getWritableDatabase();

        mDb.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(DBConstants.ERROR_EXAMINATION, "true");
            mDb.update(DBConstants.TABLE_USER_ANSWER_DATA, values,
                    DBConstants.QUESTION_ID + "=? and " + EXAMINATION_DATA_NAME + "=? and " + EXAMINATION_DATA_UNIT + "=?",
                    new String[]{id + "", ExamintaionName, UnitName});

            ContentValues valuesError = new ContentValues();
            valuesError.put(DBConstants.QUESTION_ID, id);
            valuesError.put(DBConstants.EXAMINATION_DATA_NAME, DBConstants.ExamintaionName);
            valuesError.put(DBConstants.EXAMINATION_DATA_UNIT, DBConstants.UnitName);
            valuesError.put(DBConstants.ERROR_SUBJECT, exercisesBean.subject);
            valuesError.put(DBConstants.ERROR_ANALYSIS, exercisesBean.analysis);
            valuesError.put(DBConstants.ERROR_SCORE, exercisesBean.score);
            valuesError.put(DBConstants.ERROR_TYPE, exercisesBean.type);
            valuesError.put(ERROR_ANSWER, exercisesBean.answer);
            valuesError.put(DBConstants.ERROR_SOUND, exercisesBean.sound);
            valuesError.put(DBConstants.ERROR_EXAM_PATH, MainActivity.mFilePath);
            valuesError.put(DBConstants.ERROR_ADD_TIME, System.currentTimeMillis());
            valuesError.put(DBConstants.EXAMINATION_DATA_UNIT_NAME, exercisesBean.name);
            long code = mDb.insert(DBConstants.TABLE_ERROR_EXAMINATION, null, valuesError);
            mDb.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            Constants.log("添加到错题库 错误 = " + e, -1);
        } finally {
            mDb.endTransaction();
            mDb.close();
        }
    }

    /**
     * 移除错题库
     *
     * @param id
     */
    public void removeErrorExamination(int id) {
        mDb = mDbHelper.getWritableDatabase();

        mDb.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(DBConstants.ERROR_EXAMINATION, "false");
            mDb.update(TABLE_USER_ANSWER_DATA, values,
                    QUESTION_ID + "=? and " + EXAMINATION_DATA_NAME + "=? and " + EXAMINATION_DATA_UNIT + "=?",
                    new String[]{id + "", ExamintaionName, UnitName});

            int code = mDb.delete(TABLE_ERROR_EXAMINATION,
                    QUESTION_ID + "=? and " + EXAMINATION_DATA_NAME + "=? and " + EXAMINATION_DATA_UNIT + "=?",
                    new String[]{id + "", ExamintaionName, UnitName});
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
            mDb.close();
        }

    }

    /**
     * 错题库里移除选中的错题
     *
     * @param id
     */
    public void removeCheckErrorExamination(String examintaionName, String unit, int id) {
        mDb = mDbHelper.getWritableDatabase();

        mDb.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(DBConstants.ERROR_EXAMINATION, "false");
            mDb.update(TABLE_USER_ANSWER_DATA, values,
                    QUESTION_ID + "=? and " + EXAMINATION_DATA_NAME + "=? and " + EXAMINATION_DATA_UNIT + "=?",
                    new String[]{id + "", examintaionName, unit});

            int code = mDb.delete(TABLE_ERROR_EXAMINATION,
                    QUESTION_ID + "=? and " + EXAMINATION_DATA_NAME + "=? and " + EXAMINATION_DATA_UNIT + "=?",
                    new String[]{id + "", examintaionName, unit});
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
            mDb.close();
        }

    }

    /**
     * 删除所有的数据
     */
    public void deleteTableData() {
        mDb = mDbHelper.getWritableDatabase();
        mDb.delete(DBConstants.TABLE_USER_ANSWER_DATA, null, null);
        mDb.close();
    }
}
