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
        super(context, "score_record.db", null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createScoreRecordTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //For debug
        db.execSQL("DROP TABLE IF EXISTS score_record");
        createScoreRecordTable(db);
    }

    private void createScoreRecordTable(SQLiteDatabase db) {
        String SQL = "create table score_record(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "game_mode INTEGER," +
                "score INTEGER," +
                "question_size INTEGER," +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";
        db.execSQL(SQL);
    }

    public void addGameScore(int mode, int score, int question_size) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("game_mode", mode);
        contentValues.put("score", score);
        contentValues.put("question_size", question_size);
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        sqLiteDatabase.insert("score_record", null, contentValues);
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

    public ScoreRecordBean getHighScoreObject(int mode) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {"score", "question_size"};
        String selection = "game_mode = " + mode;
        Cursor cursor = db.query("score_record", columns, selection,
                null, null, null, "score DESC", "1");
        int score = 0;
        int questionSize = 0;
        if(cursor != null) {
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                score = cursor.getInt(cursor.getColumnIndex("score"));
                questionSize = cursor.getInt(cursor.getColumnIndex("question_size"));
            }
            cursor.close();
        }
        db.close();
        return new ScoreRecordBean(mode, score, questionSize);
    }
}
