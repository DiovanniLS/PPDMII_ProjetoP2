    package br.gov.sp.cps.bancodados;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    import java.security.MessageDigest;

    public class DatabaseHelper extends SQLiteOpenHelper {

        // Constantes que representam os dados do DB e da Tabela:
        public static final String DATABASE_NAME = "dados.db";
        public static final String TABLE_NAME = "usuarios";
        public static final String COL_1 = "ID";
        public static final String COL_2 = "EMAIL";
        public static final String COL_3 = "SENHA";

        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL("CREATE TABLE " + TABLE_NAME +
                    " (ID INTEGER PRIMARY KEY AUTOINCREMENT, EMAIL TEXT UNIQUE, SENHA TEXT)");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        // METODO GRAVAR:
        public boolean inserirDados(String nome, int idade){
            // Obter o DB no modo escrita:
            SQLiteDatabase db = this.getWritableDatabase();

            // Armazena os valores para serem inseridos:
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_2, nome);
            contentValues.put(COL_3, idade);

            // Insere os dados e armezena o resultado na tabela:
            long resultado = db.insert(TABLE_NAME, null, contentValues);

            // Fecha o DB:
            db.close();

            // Retorna true se a inserção foi bem-sucedida:
            return resultado != -1;
        }

        public boolean cadastrarUsuario(String email, String senha){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COL_2, email);
            values.put(COL_3, senha);

            long resultado = db.insert(TABLE_NAME, null, values);

            db.close();

            return resultado != -1;
        }

        public boolean usuarioExiste(String email){
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(
                    "SELECT * FROM " + TABLE_NAME + " WHERE EMAIL=?",
                    new String[]{email}
            );

            boolean existe = cursor.moveToFirst();
            cursor.close();

            return existe;
        }


        public boolean validarLogin(String email, String senha){
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(
                    "SELECT * FROM " + TABLE_NAME + " WHERE EMAIL=? AND SENHA=?",
                    new String[]{email, senha}
            );

            boolean loginValido = cursor.moveToFirst();
            cursor.close();

            return loginValido;
        }

        public String hashSenha(String senha){
            try{
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(senha.getBytes());
                StringBuilder hex = new StringBuilder();

                for(byte b : hash){
                    hex.append(String.format("%02x", b));
                }

                return hex.toString();
            }catch(Exception e){
                return senha;
            }
        }



    }
