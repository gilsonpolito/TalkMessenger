package sdm.ifspsaocarlos.edu.br.talkmessenger.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sdm.ifspsaocarlos.edu.br.talkmessenger.R;
import sdm.ifspsaocarlos.edu.br.talkmessenger.adapter.ContatoAdapter;
import sdm.ifspsaocarlos.edu.br.talkmessenger.data.ContatoDAO;
import sdm.ifspsaocarlos.edu.br.talkmessenger.model.Contato;

public class MainActivity extends AppCompatActivity {

    private Contato contatoPrincipal;
    private FloatingActionButton fab;

    private ContatoDAO dao;
    private RecyclerView recyclerView;
    private ContatoAdapter adapter;
    private List<Contato> contatos = new ArrayList<>();

    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        empty = findViewById(R.id.empty_view_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv_main);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);

        adapter = new ContatoAdapter(contatos, this);
        recyclerView.setAdapter(adapter);

        setupRecyclerView();

        Intent intent = getIntent();
        contatoPrincipal = (Contato) intent.getSerializableExtra(getString(R.string.PARAMETRO_CONTATO_MAIN));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListaContatosWebServiceActivity.class);
                contatos.add(contatoPrincipal);
                intent.putExtra(getString(R.string.lista_contato_db), (ArrayList)contatos);
                startActivityForResult(intent, 0);
            }
        });

        Toast.makeText(this, contatoPrincipal.getNomeCompleto(), Toast.LENGTH_SHORT).show();

        dao = new ContatoDAO(this);
        atualizarListaDeContatos();
    }

    private void atualizarListaDeContatos() {
        contatos.clear();
        contatos.addAll(dao.buscaTodosContatos());
        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        atualizarListaDeContatos();
    }

    private void setupRecyclerView() {
        adapter.setClickListener(new ContatoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
            }
        });
    }

    private void showSnackBar(String msg) {
        CoordinatorLayout coordinatorlayout = (CoordinatorLayout) findViewById(R.id.coordlayout);
        Snackbar.make(coordinatorlayout, msg,
                Snackbar.LENGTH_LONG)
                .show();
    }

    private void updateUI() {
        recyclerView.getAdapter().notifyDataSetChanged();
        if (recyclerView.getAdapter().getItemCount() == 0)
            empty.setVisibility(View.VISIBLE);
        else
            empty.setVisibility(View.GONE);
    }
}
