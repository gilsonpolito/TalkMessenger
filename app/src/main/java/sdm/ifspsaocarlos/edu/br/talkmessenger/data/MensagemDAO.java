package sdm.ifspsaocarlos.edu.br.talkmessenger.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import sdm.ifspsaocarlos.edu.br.talkmessenger.model.Mensagem;

public class MensagemDAO {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public MensagemDAO(Context context) {
        this.dbHelper=new SQLiteHelper(context);
    }

    public List<Mensagem> buscaTodasMensagens(String origemId, String destinoId)
    {
        database=dbHelper.getReadableDatabase();
        List<Mensagem> mensagens = new ArrayList<>();

        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_MENSAGEM_ID, SQLiteHelper.KEY_MENSAGEM_ORIGEM_ID, SQLiteHelper.KEY_MENSAGEM_DESTINO_ID, SQLiteHelper.KEY_MENSAGEM_CORPO_MENSAGEM};

        String where=SQLiteHelper.KEY_MENSAGEM_ORIGEM_ID + " = ? AND " + SQLiteHelper.KEY_MENSAGEM_DESTINO_ID + " = ?";

        String[] argWhere=new String[]{origemId, destinoId};

        cursor = database.query(SQLiteHelper.TABLE_MENSAGENS, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_MENSAGEM_ID);

        while (cursor.moveToNext())
        {
            Mensagem mensagem = new Mensagem();
            mensagem.setId(cursor.getString(0));
            mensagem.setOrigemId(cursor.getString(1));
            mensagem.setDestinoId(cursor.getString(2));
            mensagem.setCorpo(cursor.getString(3));
            mensagens.add(mensagem);
        }
        cursor.close();

        database.close();

        return mensagens;
    }

    public void salvar(Mensagem mensagem) {
        saveOrUpdate(mensagem, true);
    }

    private boolean existeMensagem(Mensagem mensagem){
        Boolean retorno = false;
        database=dbHelper.getReadableDatabase();

        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_MENSAGEM_ID, SQLiteHelper.KEY_MENSAGEM_ORIGEM_ID, SQLiteHelper.KEY_MENSAGEM_DESTINO_ID, SQLiteHelper.KEY_MENSAGEM_CORPO_MENSAGEM};

        String where=SQLiteHelper.KEY_MENSAGEM_ID + " = ?";

        String[] argWhere=new String[]{mensagem.getId()};

        cursor = database.query(SQLiteHelper.TABLE_MENSAGENS, cols, where , argWhere,
                null, null, null);

        if (cursor.moveToNext())
        {
            retorno = true;
        }
        cursor.close();

        database.close();

        return retorno;
    }

    public void salvar(List<Mensagem> mensagens){
        for(Mensagem mensagem: mensagens){
            saveOrUpdate(mensagem, !existeMensagem(mensagem));
        }
    }

    /*public void deletar(){
        database=dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.TABLE_MENSAGENS, "id > 0", null);
        database.close();
    }*/

    private void saveOrUpdate(Mensagem mensagem, Boolean salvar){
        database=dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.KEY_MENSAGEM_ID, Integer.parseInt(mensagem.getId()));
        values.put(SQLiteHelper.KEY_MENSAGEM_ORIGEM_ID, Integer.parseInt(mensagem.getOrigemId()));
        values.put(SQLiteHelper.KEY_MENSAGEM_DESTINO_ID, Integer.parseInt(mensagem.getDestinoId()));
        values.put(SQLiteHelper.KEY_MENSAGEM_CORPO_MENSAGEM, mensagem.getCorpo());

        if(salvar) {
            database.insert(SQLiteHelper.TABLE_MENSAGENS, null, values);
        }
        else {
            String where=SQLiteHelper.KEY_MENSAGEM_ID + " = ?";
            String[] whereArgs = new String[]{mensagem.getId()};
            database.update(SQLiteHelper.TABLE_MENSAGENS, values, where, whereArgs);
        }

        database.close();
    }
}
