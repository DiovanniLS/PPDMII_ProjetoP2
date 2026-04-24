package br.gov.sp.cps.bancodados;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class CadastroActivity extends AppCompatActivity {

    TextInputEditText textEmail, textSenha;
    Button btnCadastrar;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        dbHelper = new DatabaseHelper(this);

        textEmail = findViewById(R.id.textEmail);
        textSenha = findViewById(R.id.textSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(v -> {
            String email = textEmail.getText().toString();
            String senha = textSenha.getText().toString();

            if(email.isEmpty() || senha.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
                return;
            }

            if(dbHelper.usuarioExiste(email)){
                Toast.makeText(this, "Usuário já existe!", Toast.LENGTH_LONG).show();
                return;
            }

            if(dbHelper.cadastrarUsuario(email, senha)){
                Toast.makeText(this, "Cadastro realizado!", Toast.LENGTH_LONG).show();
                finish(); // volta para login
            } else {
                Toast.makeText(this, "Erro ao cadastrar", Toast.LENGTH_LONG).show();
            }
        });
    }
}
