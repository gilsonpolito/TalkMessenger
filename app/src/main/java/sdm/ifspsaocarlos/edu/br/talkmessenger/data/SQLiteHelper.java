package sdm.ifspsaocarlos.edu.br.talkmessenger.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TalkMessenger.db";

    static final String DATABASE_TABLE = "contatos";

    static final String KEY_ID = "id";
    static final String KEY_NAME = "nome";
    static final String KEY_APELIDO = "apelido";
    static final String KEY_PRINCIPAL = "principal";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "CREATE TABLE " + DATABASE_TABLE + " (" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            KEY_NAME + " TEXT NOT NULL, " +
            KEY_APELIDO + " TEXT NOT NULL, " +
            KEY_PRINCIPAL + " TEXT NOT NULL);";

    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }
}
