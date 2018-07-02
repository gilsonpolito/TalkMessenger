package sdm.ifspsaocarlos.edu.br.talkmessenger.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TalkMessenger.db";

    static final String TABLE_CONTATOS = "contatos";
    static final String TABLE_MENSAGENS = "mensagens";

    static final String KEY_CONTATO_ID = "id";
    static final String KEY_CONTATO_NAME = "nome";
    static final String KEY_CONTATO_APELIDO = "apelido";
    static final String KEY_CONTATO_PRINCIPAL = "principal";

    static final String KEY_MENSAGEM_ID = "id";
    static final String KEY_MENSAGEM_ORIGEM_ID = "id_origem";
    static final String KEY_MENSAGEM_DESTINO_ID = "id_destino";
    static final String KEY_MENSAGEM_CORPO_MENSAGEM = "corpo_mensagem";

    private static final int DATABASE_VERSION = 2;
    private static final String CREATE_TABLE_CONTATOS = "CREATE TABLE " + TABLE_CONTATOS + " (" +
            KEY_CONTATO_ID + " INTEGER PRIMARY KEY, " +
            KEY_CONTATO_NAME + " TEXT NOT NULL, " +
            KEY_CONTATO_APELIDO + " TEXT NOT NULL, " +
            KEY_CONTATO_PRINCIPAL + " INTEGER NOT NULL DEFAULT 0);";

    private static final String CREATE_TABLE_MENSAGENS = "CREATE TABLE " + TABLE_MENSAGENS + " (" +
            KEY_MENSAGEM_ID + " INTEGER PRIMARY KEY, " +
            KEY_MENSAGEM_ORIGEM_ID + " INTEGER NOT NULL, " +
            KEY_MENSAGEM_DESTINO_ID + " INTEGER NOT NULL, " +
            KEY_MENSAGEM_CORPO_MENSAGEM + " TEXT NOT NULL, " +
            "FOREIGN KEY(" + KEY_MENSAGEM_ORIGEM_ID + ") REFERENCES " + TABLE_CONTATOS + "(" + KEY_CONTATO_ID + "), " +
            "FOREIGN KEY(" + KEY_MENSAGEM_DESTINO_ID + ") REFERENCES " + TABLE_CONTATOS + "(" + KEY_CONTATO_ID + "));";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_CONTATOS);
        database.execSQL(CREATE_TABLE_MENSAGENS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                database.execSQL(CREATE_TABLE_MENSAGENS);
        }
    }
}
