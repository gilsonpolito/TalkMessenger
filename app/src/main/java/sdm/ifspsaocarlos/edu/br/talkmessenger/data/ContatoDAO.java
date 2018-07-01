package sdm.ifspsaocarlos.edu.br.talkmessenger.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import sdm.ifspsaocarlos.edu.br.talkmessenger.model.Contato;

public class ContatoDAO {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    public final static Integer CHAVE_CONTATO_PRINCIPAL = 1;
    public final static Integer CHAVE_CONTATOS = 0;

    public ContatoDAO(Context context) {
        this.dbHelper=new SQLiteHelper(context);
    }

    public List<Contato> buscaTodosContatos()
    {
        database=dbHelper.getReadableDatabase();
        List<Contato> contatos = new ArrayList<>();

        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID, SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_APELIDO, SQLiteHelper.KEY_PRINCIPAL};

        String where=SQLiteHelper.KEY_PRINCIPAL + " = ?";

        String[] argWhere=new String[]{CHAVE_CONTATOS.toString()};

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_NAME);

        while (cursor.moveToNext())
        {
            Contato contato = new Contato();
            contato.setId(cursor.getString(0));
            contato.setNomeCompleto(cursor.getString(1));
            contato.setApelido(cursor.getString(2));
            contato.setPrincipal(cursor.getInt(3));
            contatos.add(contato);
        }
        cursor.close();

        database.close();

        return contatos;
    }

    public  Contato buscaContatoPrincipal()
    {
        Contato contato = null;

        database=dbHelper.getReadableDatabase();

        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_APELIDO, SQLiteHelper.KEY_PRINCIPAL};

        String where=SQLiteHelper.KEY_PRINCIPAL + " = ?";

        String[] argWhere=new String[]{CHAVE_CONTATO_PRINCIPAL.toString()};

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_NAME);


        if (cursor.moveToNext())
        {
            contato = new Contato();
            contato.setId(cursor.getString(0));
            contato.setNomeCompleto(cursor.getString(1));
            contato.setApelido(cursor.getString(2));
            contato.setPrincipal(cursor.getInt(3));
        }
        cursor.close();

        database.close();

        return contato;
    }

    public Contato localizaContato(String id)
    {
        Contato contato = null;

        database=dbHelper.getReadableDatabase();

        Cursor cursor;

        String[] cols=new String[] {SQLiteHelper.KEY_ID,SQLiteHelper.KEY_NAME, SQLiteHelper.KEY_APELIDO, SQLiteHelper.KEY_PRINCIPAL};

        String where=SQLiteHelper.KEY_ID + " = ?";

        String[] argWhere=new String[]{id};

        cursor = database.query(SQLiteHelper.DATABASE_TABLE, cols, where , argWhere,
                null, null, SQLiteHelper.KEY_NAME);


        if (cursor.moveToNext())
        {
            contato = new Contato();
            contato.setId(cursor.getString(0));
            contato.setNomeCompleto(cursor.getString(1));
            contato.setApelido(cursor.getString(2));
            contato.setPrincipal(cursor.getInt(3));
        }
        cursor.close();

        database.close();

        return contato;
    }

    public void salvaContato(Contato contato) {

        Contato contatoJaExistente = localizaContato(contato.getId());

        database=dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SQLiteHelper.KEY_ID, Integer.parseInt(contato.getId()));
        values.put(SQLiteHelper.KEY_NAME, contato.getNomeCompleto());
        values.put(SQLiteHelper.KEY_APELIDO, contato.getApelido());
        values.put(SQLiteHelper.KEY_PRINCIPAL, contato.getPrincipal());

        if (contatoJaExistente != null) {
            String[] argWhere = new String[]{contato.getId()};
            database.update(SQLiteHelper.DATABASE_TABLE, values, SQLiteHelper.KEY_ID + " = ?", argWhere);
        }
        else
            database.insert(SQLiteHelper.DATABASE_TABLE, null, values);

        database.close();
    }

    /*public void apagaContato(Contato c)
    {
        database=dbHelper.getWritableDatabase();
        database.delete(SQLiteHelper.DATABASE_TABLE, SQLiteHelper.KEY_ID + "="
                + c.getId(), null);
        database.close();
    }*/
}