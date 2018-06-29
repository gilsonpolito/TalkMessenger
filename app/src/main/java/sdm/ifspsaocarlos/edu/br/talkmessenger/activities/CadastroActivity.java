package sdm.ifspsaocarlos.edu.br.talkmessenger.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import sdm.ifspsaocarlos.edu.br.talkmessenger.R;

public class CadastroActivity extends AppCompatActivity {

    private Button btCadastrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        btCadastrar = findViewById(R.id.btcadastrar);
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CadastroActivity.this, "Funfo!!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
