package br.gov.sp.cps.bancodados;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ImcActivity extends AppCompatActivity {

    TextView txtResultado;
    DatabaseHelper dbHelper;

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // volta pra tela anterior (Login)
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Resultado IMC");
        }

        txtResultado = findViewById(R.id.txtResultado);
        dbHelper = new DatabaseHelper(this);

        String email = getIntent().getStringExtra("EMAIL");

        Cursor cursor = dbHelper.buscarUsuario(email);

        if (cursor != null && cursor.moveToFirst()) {
            String nome = cursor.getString(cursor.getColumnIndexOrThrow("NOME"));
            double altura = cursor.getDouble(cursor.getColumnIndexOrThrow("ALTURA"));
            double peso = cursor.getDouble(cursor.getColumnIndexOrThrow("PESO"));
            int idade = cursor.getInt(cursor.getColumnIndexOrThrow("IDADE"));

            double alturaMetros = altura / 100;
            double imc = peso / (alturaMetros * alturaMetros);

            String classificacao = classificarIMC(imc);

            txtResultado.setText(
                    "Nome: " + nome +
                    "\nIdade: " + idade +
                            "\nAltura: " + altura + " cm" +
                            "\nPeso: " + peso + " kg" +
                            "\nIMC: " + String.format("%.2f", imc) +
                            "\nClassificação: " + classificacao
            );
        } else {
            txtResultado.setText("Erro ao buscar dados do usuário");
        }

        if (cursor != null) cursor.close();
    }

    private String classificarIMC(double imc){
        if(imc < 18.5) return "Abaixo do peso";
        else if(imc < 25) return "Peso normal";
        else if(imc < 30) return "Sobrepeso";
        else return "Obesidade";
    }
}