package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static db.DBConstants.CREATE_TABLE_ERROR_EXAMINATION;
import static db.DBConstants.CREATE_TABLE_EXAMINATION_DATA;
import static db.DBConstants.CREATE_TABLE_USER_ANSWER_DATA;

/**
 * @author Administrator
 * @version $Rev$
 * @time 2017-2-23 17:22
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, DBConstants.DB_VERSION);
//        super(context, "/storage/emulated/0/icoxedu/"+name, null, DBConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EXAMINATION_DATA);
        db.execSQL(CREATE_TABLE_USER_ANSWER_DATA);
        db.execSQL(CREATE_TABLE_ERROR_EXAMINATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
