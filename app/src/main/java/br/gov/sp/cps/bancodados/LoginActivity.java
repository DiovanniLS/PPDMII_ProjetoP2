package br.gov.sp.cps.bancodados;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText textEmail, textSenha;
    Button btnLogin, btnIrCadastro;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        textEmail = findViewById(R.id.textEmail);
        textSenha = findViewById(R.id.textSenha);
        btnLogin = findViewById(R.id.btnLogin);
        btnIrCadastro = findViewById(R.id.btnIrCadastro);

        btnLogin.setOnClickListener(v -> {
            String email = textEmail.getText().toString().trim();
            String senha = textSenha.getText().toString().trim();

            if(email.isEmpty() || senha.isEmpty()){
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
                return;
            }

            // 🔐 valida UMA vez só
            if(dbHelper.validarLogin(email, senha)){
                Toast.makeText(this, "Login realizado!", Toast.LENGTH_LONG).show();

                // 🚀 vai pra tela do IMC
                Intent intent = new Intent(this, ImcActivity.class);
                intent.putExtra("EMAIL", email);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Email ou senha inválidos", Toast.LENGTH_LONG).show();
            }
        });

        btnIrCadastro.setOnClickListener(v -> {
            startActivity(new Intent(this, CadastroActivity.class));
        });
    }
}