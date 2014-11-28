package jp.thotta.android.soundmemorygame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thotta on 14/11/28.
 */
public class ScoreRecordDBHelper extends SQLiteOpenHelper {
    public ScoreRecordDBHelper(Context context) {
        super(context, "score_record.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL = "create table score_record(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "score INTEGER" +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS score_record");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        // For debug
        //db.delete("score_record", null, null);
    }

    public void addGameScore(int score) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("score", score);
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        long pk = sqLiteDatabase.insert("score_record", null, contentValues);
        sqLiteDatabase.close();
    }

    public int getHighScore() {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {"score"};
        Cursor cursor = db.query("score_record", columns, null, null, null, null, "score DESC", "1");
        int score = 0;
        if(cursor != null) {
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                score = cursor.getInt(cursor.getColumnIndex("score"));
            }
            cursor.close();
        }
        db.close();
        return score;
    }
}
