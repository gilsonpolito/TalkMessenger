package sdm.ifspsaocarlos.edu.br.talkmessenger.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import sdm.ifspsaocarlos.edu.br.talkmessenger.R;
import sdm.ifspsaocarlos.edu.br.talkmessenger.model.Contato;

public class MainActivity extends AppCompatActivity {

    private Contato contatoPrincipal;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        contatoPrincipal = (Contato)intent.getSerializableExtra(getString(R.string.PARAMETRO_CONTATO_MAIN));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ListaContatosWebServiceActivity.class);
                startActivityForResult(i, 1);
            }
        });

        Toast.makeText(this, contatoPrincipal.getNomeCompleto(), Toast.LENGTH_SHORT).show();
    }

    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            /*if (requestCode == 1)
                if (resultCode == RESULT_OK) {
                    showSnackBar(getResources().getString(R.string.contato_adicionado));
                    updateUI(null);
                }


            if (requestCode == 2) {
                if (resultCode == RESULT_OK)
                    showSnackBar(getResources().getString(R.string.contato_alterado));
                if (resultCode == 3)
                    showSnackBar(getResources().getString(R.string.contato_apagado));

                updateUI(null);
            }*/
        }
}
