package sdm.ifspsaocarlos.edu.br.talkmessenger.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sdm.ifspsaocarlos.edu.br.talkmessenger.R;
import sdm.ifspsaocarlos.edu.br.talkmessenger.adapter.ContatoWebServiceAdapter;
import sdm.ifspsaocarlos.edu.br.talkmessenger.api.ContatoAPI;
import sdm.ifspsaocarlos.edu.br.talkmessenger.data.ContatoDAO;
import sdm.ifspsaocarlos.edu.br.talkmessenger.model.Contato;
import sdm.ifspsaocarlos.edu.br.talkmessenger.util.Utilitaria;

public class ListaContatosWebServiceActivity extends AppCompatActivity {

    private SearchView searchView;
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private ContatoWebServiceAdapter adapter;
    private List<Contato> contatos = new ArrayList<>();
    private List<Contato> contatosWebService;
    private TextView empty;
    private ContatoDAO dao;
    private List<Contato> contatosDB = new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent resultIntent = new Intent();
        setResult(0, resultIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contatos_web_service);

        Intent intent = getIntent();
        contatosDB = (ArrayList<Contato>) intent.getSerializableExtra(getString(R.string.lista_contato_db));

        empty = findViewById(R.id.empty_view_ws);

        dao = new ContatoDAO(this);

        Toolbar toolbar = findViewById(R.id.toolbar_ws);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv_ws);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);

        adapter = new ContatoWebServiceAdapter(contatos, this);
        recyclerView.setAdapter(adapter);

        setupRecyclerView();

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(getString(R.string.URL_BASE));
        builder.addConverterFactory(GsonConverterFactory.create(gson));

        retrofit = builder.build();
        ContatoAPI api = retrofit.create(ContatoAPI.class);
        api.getContatos()
                .enqueue(new Callback<List<Contato>>() {
                    @Override
                    public void onResponse(Call<List<Contato>> call, Response<List<Contato>> response) {
                        contatosWebService = response.body();
                        contatos.addAll(contatosWebService);
                        removerContatosCadastradosBanco();
                    }

                    @Override
                    public void onFailure(Call<List<Contato>> call, Throwable t) {
                        Toast.makeText(ListaContatosWebServiceActivity.this, getString(R.string.ERRO_BUSCAR_CONTATOS), Toast.LENGTH_SHORT).show();
                    }
                });
        updateUI();
    }

    private void removerContatosCadastradosBanco() {
        contatos.removeAll(contatosDB);
        Collections.sort(contatos, new Comparator<Contato>() {
            @Override
            public int compare(Contato contato, Contato contatoComparado) {
                return contato.getNomeCompleto().compareTo(contatoComparado.getNomeCompleto());
            }
        });
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pesquisa, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.pesqContato).getActionView();
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.search_src_text);
                if (et.getText().toString().isEmpty()) {
                    searchView.onActionViewCollapsed();
                    contatos.clear();
                    contatos.addAll(contatosWebService);
                    removerContatosCadastradosBanco();
                }

                searchView.setQuery("", false);
            }
        });

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);

        return true;
    }

    private void updateUI() {
        recyclerView.getAdapter().notifyDataSetChanged();
        if (recyclerView.getAdapter().getItemCount() == 0)
            empty.setVisibility(View.VISIBLE);
        else
            empty.setVisibility(View.GONE);
    }

    private void updateUI(String query) {
        List<Contato> filtro = new ArrayList<>();
        for (Contato contato : contatos) {
            if (contato.getNomeCompleto().startsWith(query)) {
                filtro.add(contato);
            }
        }
        contatos.clear();
        contatos.addAll(filtro);
        updateUI();
    }

    private void setupRecyclerView() {
        adapter.setClickListener(new ContatoWebServiceAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.RIGHT) {
                    Contato contato = contatos.get(viewHolder.getAdapterPosition());
                    contato.setPrincipal(ContatoDAO.CHAVE_CONTATOS);
                    dao.salvar(contato);
                    contatosDB.add(contato);
                    contatos.remove(viewHolder.getAdapterPosition());
                    recyclerView.getAdapter().notifyDataSetChanged();
                    showSnackBar(getResources().getString(R.string.contato_adicionado));
                    updateUI();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                Paint p = new Paint();
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorInserir));
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_plus);

                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), p);
                        c.drawBitmap(icon, (float) itemView.getLeft() + Utilitaria.convertDpToPx(ListaContatosWebServiceActivity.this, 16), (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void showSnackBar(String msg) {
        CoordinatorLayout coordinatorlayout = (CoordinatorLayout) findViewById(R.id.coordlayout);
        Snackbar.make(coordinatorlayout, msg,
                Snackbar.LENGTH_LONG)
                .show();
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.clearFocus();
            updateUI(query);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

}