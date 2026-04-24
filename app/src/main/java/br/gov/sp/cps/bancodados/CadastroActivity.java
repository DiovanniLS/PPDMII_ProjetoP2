package br.gov.sp.cps.bancodados;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class CadastroActivity extends AppCompatActivity {

    TextInputEditText textEmail, textSenha, textAltura, textDataNascimento, textPeso, textNome;
    Button btnCadastrar;
    DatabaseHelper dbHelper;

    int idadeCalculada = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        dbHelper = new DatabaseHelper(this);

        textNome = findViewById(R.id.textNome);
        textEmail = findViewById(R.id.textEmail);
        textSenha = findViewById(R.id.textSenha);
        textAltura = findViewById(R.id.textAltura);
        textPeso = findViewById(R.id.textPeso);
        textDataNascimento = findViewById(R.id.textDataNascimento);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        // 📅 abrir calendário
        textDataNascimento.setOnClickListener(v -> abrirCalendario());

        btnCadastrar.setOnClickListener(v -> cadastrar());
    }

    private void abrirCalendario() {
        Calendar calendar = Calendar.getInstance();

        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {

                    String data = dayOfMonth + "/" + (month + 1) + "/" + year;
                    textDataNascimento.setText(data);

                    calcularIdade(year, month, dayOfMonth);
                },
                ano, mes, dia
        );

        dialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // bloqueia futuro
        dialog.show();
    }

    private void calcularIdade(int year, int month, int day) {
        Calendar hoje = Calendar.getInstance();
        idadeCalculada = hoje.get(Calendar.YEAR) - year;

        if (hoje.get(Calendar.MONTH) < month ||
                (hoje.get(Calendar.MONTH) == month && hoje.get(Calendar.DAY_OF_MONTH) < day)) {
            idadeCalculada--;
        }
    }

    private void cadastrar() {
        String nome = textNome.getText().toString().trim();
        String email = textEmail.getText().toString().trim();
        String senha = textSenha.getText().toString().trim();
        String alturaStr = textAltura.getText().toString().trim();
        String pesoStr = textPeso.getText().toString().trim();
        String data = textDataNascimento.getText().toString().trim();

        if ( nome.isEmpty() || email.isEmpty() || senha.isEmpty() || alturaStr.isEmpty() || pesoStr.isEmpty() || data.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
            return;
        }

        if (dbHelper.usuarioExiste(email)) {
            Toast.makeText(this, "Usuário já existe!", Toast.LENGTH_LONG).show();
            return;
        }

        double altura;

        try {
            altura = Double.parseDouble(alturaStr);
        } catch (Exception e) {
            Toast.makeText(this, "Altura inválida", Toast.LENGTH_LONG).show();
            return;
        }

        double peso;

        try {
            peso = Double.parseDouble(pesoStr);
        } catch (Exception e) {
            Toast.makeText(this, "Peso inválido", Toast.LENGTH_LONG).show();
            return;
        }

        if (dbHelper.cadastrarUsuario(nome,email, senha, altura, peso, idadeCalculada)) {
            Toast.makeText(this, "Cadastro realizado!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao cadastrar", Toast.LENGTH_LONG).show();
        }
    }
}