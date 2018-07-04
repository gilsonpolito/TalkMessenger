package sdm.ifspsaocarlos.edu.br.talkmessenger.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import sdm.ifspsaocarlos.edu.br.talkmessenger.R;
import sdm.ifspsaocarlos.edu.br.talkmessenger.api.ContatoAPI;
import sdm.ifspsaocarlos.edu.br.talkmessenger.data.ContatoDAO;
import sdm.ifspsaocarlos.edu.br.talkmessenger.model.Contato;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNomeCompleto;
    private EditText etApelido;

    private Retrofit retrofit;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ContatoDAO dao = new ContatoDAO(this);
        Contato contatoPrincipal = dao.getPrincipal();

        if (contatoPrincipal != null) {
            irParaTelaPrincipal(contatoPrincipal);
        } else {

            setContentView(R.layout.activity_cadastro);

            etNomeCompleto = findViewById(R.id.etNome);
            etApelido = findViewById(R.id.etApelido);

            gson = new Gson();
            retrofit = new Retrofit.Builder().baseUrl(getString(R.string.URL_BASE)).build();

            findViewById(R.id.btcadastrar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Contato contato = new Contato();
                    contato.setApelido(etApelido.getText().toString());
                    contato.setNomeCompleto(etNomeCompleto.getText().toString());

                    RequestBody body = RequestBody.create(MediaType.parse(getString(R.string.APPLICATION_JSON)), gson.toJson(contato));

                    ContatoAPI api = retrofit.create(ContatoAPI.class);

                    Call<ResponseBody> call = api.create(body);

                    AsyncTask<Call<ResponseBody>, Void, Contato> asyncTask = new AsyncTask<Call<ResponseBody>, Void, Contato>() {
                        @Override
                        protected Contato doInBackground(Call<ResponseBody>... calls) {
                            Call<ResponseBody> exec = calls[0];
                            Contato cadastro = null;
                            try {
                                Response<ResponseBody> resposta = exec.execute();
                                cadastro = gson.fromJson(resposta.body().string(), Contato.class);
                            } catch (IOException ioe) {
                                Toast.makeText(CadastroActivity.this, getString(R.string.ERRO_CADASTRO_CONTATO), Toast.LENGTH_SHORT).show();
                            }
                            return cadastro;
                        }

                        @Override
                        protected void onPostExecute(Contato contato) {
                            super.onPostExecute(contato);

                            contato.setPrincipal(ContatoDAO.CHAVE_CONTATO_PRINCIPAL);
                            dao.salvar(contato);

                            Toast.makeText(CadastroActivity.this, getString(R.string.contato_cadastrado) + contato.getId(), Toast.LENGTH_SHORT).show();

                            irParaTelaPrincipal(contato);
                        }
                    };
                    asyncTask.execute(call);
                }
            });
        }
    }

    private void irParaTelaPrincipal(Contato contato) {
        Intent main = new Intent(this, MainActivity.class);
        main.putExtra(getString(R.string.PARAMETRO_CONTATO_MAIN), contato);
        startActivity(main);
        finish();
    }
}
