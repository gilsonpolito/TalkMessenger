package sdm.ifspsaocarlos.edu.br.talkmessenger.activities;


import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sdm.ifspsaocarlos.edu.br.talkmessenger.R;
import sdm.ifspsaocarlos.edu.br.talkmessenger.adapter.ContatoWebServiceAdapter;
import sdm.ifspsaocarlos.edu.br.talkmessenger.api.ContatoAPI;
import sdm.ifspsaocarlos.edu.br.talkmessenger.model.Contato;

public class ListaContatosWebServiceActivity extends AppCompatActivity {

    private SearchView searchView;
    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private ContatoWebServiceAdapter adapter;
    private List<Contato> contatos = new ArrayList<>();
    private TextView empty;

    private void setupRecyclerView() {
        adapter.setClickListener(new ContatoWebServiceAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        });

        /*ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                if (swipeDir == ItemTouchHelper.RIGHT) {
                    Contato contato = contatos.get(viewHolder.getAdapterPosition());
                    cDAO.apagaContato(contato);
                    contatos.remove(viewHolder.getAdapterPosition());
                    recyclerView.getAdapter().notifyDataSetChanged();
                    showSnackBar(getResources().getString(R.string.contato_apagado));
                    updateUI(null);
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
                        p.setColor(ContextCompat.getColor(getBaseContext(), R.color.colorDelete));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_remove);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contatos_web_service);

        empty = findViewById(R.id.empty_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
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
                        contatos.addAll(response.body());
                        updateUI();
                    }

                    @Override
                    public void onFailure(Call<List<Contato>> call, Throwable t) {
                        Toast.makeText(ListaContatosWebServiceActivity.this, getString(R.string.ERRO_BUSCAR_CONTATOS), Toast.LENGTH_SHORT).show();
                    }
                });
        updateUI();
    }

    private void updateUI(){
        recyclerView.getAdapter().notifyDataSetChanged();
        if (recyclerView.getAdapter().getItemCount() == 0)
            empty.setVisibility(View.VISIBLE);
        else
            empty.setVisibility(View.GONE);
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
                if (et.getText().toString().isEmpty())
                    searchView.onActionViewCollapsed();

                searchView.setQuery("", false);
            }
        });

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setIconifiedByDefault(true);

        return true;
    }
}
