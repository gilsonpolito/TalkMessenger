package sdm.ifspsaocarlos.edu.br.talkmessenger.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sdm.ifspsaocarlos.edu.br.talkmessenger.R;
import sdm.ifspsaocarlos.edu.br.talkmessenger.adapter.ListaMensagemAdapter;
import sdm.ifspsaocarlos.edu.br.talkmessenger.api.MensagemAPI;
import sdm.ifspsaocarlos.edu.br.talkmessenger.data.MensagemDAO;
import sdm.ifspsaocarlos.edu.br.talkmessenger.model.Contato;
import sdm.ifspsaocarlos.edu.br.talkmessenger.model.Mensagem;

public class ListaMensagensActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private List<Mensagem> listaMensagens = new ArrayList<>();
    private boolean primeiraChamada = false;
    private boolean segundaChamada = false;
    private ListView mostrarMensagensLv;
    private ListaMensagemAdapter adapter;
    private EditText etMensagem;
    private Gson gson;
    private MensagemAPI api;
    private MensagemDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_mensagens);

        Intent intent = getIntent();
        final Contato contatoSelecionado = (Contato) intent.getSerializableExtra(getString(R.string.CONTATO_SELECIONADO));
        final Contato contatoPrincipal = (Contato) intent.getSerializableExtra(getString(R.string.CONTATO_PRINCIPAL));

        dao = new MensagemDAO(this);
        //dao.deletar();
        List<Mensagem> mensagensOrigem = dao.buscaTodasMensagens(contatoPrincipal.getId(), contatoSelecionado.getId());
        String maxIdMensagemOrigem = getMaxId(mensagensOrigem);
        listaMensagens.addAll(mensagensOrigem);

        List<Mensagem> mensagensDestino = dao.buscaTodasMensagens(contatoSelecionado.getId(), contatoPrincipal.getId());
        String maxIdMensagemDestino = getMaxId(mensagensDestino);
        listaMensagens.addAll(mensagensDestino);

        gson = new GsonBuilder().setLenient().create();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(getString(R.string.URL_BASE));
        builder.addConverterFactory(GsonConverterFactory.create(gson));

        retrofit = builder.build();
        api = retrofit.create(MensagemAPI.class);
        api.getMensagens(maxIdMensagemOrigem, contatoPrincipal.getId(), contatoSelecionado.getId())
                .enqueue(new Callback<List<Mensagem>>() {
                    @Override
                    public void onResponse(Call<List<Mensagem>> call, Response<List<Mensagem>> response) {
                        List<Mensagem> msg = response.body();
                        dao.salvar(msg);
                        listaMensagens.addAll(msg);
                        primeiraChamada = true;
                        if (chamarIntent()) {
                            updateUI();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Mensagem>> call, Throwable t) {
                        Toast.makeText(ListaMensagensActivity.this, "Erro na recuperação das mensagens!", Toast.LENGTH_SHORT).show();
                    }
                });

        api.getMensagens(maxIdMensagemDestino, contatoSelecionado.getId(), contatoPrincipal.getId())
                .enqueue(new Callback<List<Mensagem>>() {
                    @Override
                    public void onResponse(Call<List<Mensagem>> call, Response<List<Mensagem>> response) {
                        List<Mensagem> msg = response.body();
                        dao.salvar(msg);
                        listaMensagens.addAll(msg);
                        segundaChamada = true;
                        if (chamarIntent()) {
                            updateUI();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Mensagem>> call, Throwable t) {
                        Toast.makeText(ListaMensagensActivity.this, "Erro na recuperação das mensagens!", Toast.LENGTH_SHORT).show();
                    }
                });

        etMensagem = findViewById(R.id.etMensagem);
        Button btEnviarMensagem = findViewById(R.id.btEnviarMensagem);
        btEnviarMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mensagem mensagem = new Mensagem();
                mensagem.setAssunto("");
                mensagem.setCorpo(etMensagem.getText().toString());
                mensagem.setDestino(contatoSelecionado);
                mensagem.setDestinoId(contatoSelecionado.getId());
                mensagem.setOrigem(contatoPrincipal);
                mensagem.setOrigemId(contatoPrincipal.getId());

                RequestBody body = RequestBody.create(MediaType.parse(getString(R.string.APPLICATION_JSON)), gson.toJson(mensagem));
                Call<ResponseBody> call = api.create(body);

                AsyncTask<Call<ResponseBody>, Void, Mensagem> asyncTask = new AsyncTask<Call<ResponseBody>, Void, Mensagem>() {
                    @Override
                    protected Mensagem doInBackground(Call<ResponseBody>... calls) {
                        Call<ResponseBody> exec = calls[0];
                        Mensagem mensagem = null;
                        try {
                            Response<ResponseBody> resposta = exec.execute();
                            mensagem = gson.fromJson(resposta.body().string(), Mensagem.class);
                        } catch (IOException ioe) {
                            Toast.makeText(ListaMensagensActivity.this, getString(R.string.ERRO_ENVIAR_MENSAGEM), Toast.LENGTH_SHORT).show();
                        }
                        return mensagem;
                    }

                    @Override
                    protected void onPostExecute(Mensagem mensagem) {
                        super.onPostExecute(mensagem);
                        dao.salvar(mensagem);
                        listaMensagens.add(mensagem);
                        Toast.makeText(ListaMensagensActivity.this, getString(R.string.mensagem_cadastrada) + mensagem.getId(), Toast.LENGTH_SHORT).show();
                        updateUI();
                    }
                };
                asyncTask.execute(call);
            }
        });
    }

    private String getMaxId(List<Mensagem> mensagens) {
        Integer max = 1;
        for (Mensagem mensagem : mensagens) {
            if (Integer.valueOf(mensagem.getId()) > max) {
                max = Integer.valueOf(mensagem.getId());
            }
        }
        max += 1;
        return max.toString();
    }

    private void updateUI() {
        if(adapter==null){
            adapter = new ListaMensagemAdapter(this, R.layout.mensagem_celula, listaMensagens);
            mostrarMensagensLv = findViewById(R.id.lv_mostrar_mensagens);
            mostrarMensagensLv.setAdapter(adapter);
        }
        Collections.sort(listaMensagens, new Comparator<Mensagem>() {
            @Override
            public int compare(Mensagem m1, Mensagem m2) {
                return m1.getId().compareTo(m2.getId());
            }
        });
        adapter.notifyDataSetChanged();
        etMensagem.setText("");
    }

    private synchronized boolean chamarIntent() {
        return primeiraChamada && segundaChamada;
    }
}