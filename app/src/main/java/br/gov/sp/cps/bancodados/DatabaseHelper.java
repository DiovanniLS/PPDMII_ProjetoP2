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

        public static final String COL_2 = "NOME";
        public static final String COL_3 = "EMAIL";
        public static final String COL_4 = "SENHA";

        public DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, 4);
        }

        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NOME TEXT,"+
                    "EMAIL TEXT UNIQUE, " +
                    "SENHA TEXT, " +
                    "ALTURA REAL, " +
                    "PESO REAL, " +
                    "IDADE INTEGER)");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        public boolean cadastrarUsuario(String nome,String email, String senha, double altura, double peso, int idade){
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COL_2, nome);
            values.put(COL_3, email);
            values.put(COL_4, hashSenha(senha));
            values.put("ALTURA", altura);
            values.put("PESO", peso);
            values.put("IDADE", idade);

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

            String senhaHash = hashSenha(senha);

            Cursor cursor = db.rawQuery(
                    "SELECT * FROM " + TABLE_NAME + " WHERE EMAIL=? AND SENHA=?",
                    new String[]{email, senhaHash}
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


        public Cursor buscarUsuario(String email){
            SQLiteDatabase db = this.getReadableDatabase();

            return db.rawQuery(
                    "SELECT * FROM " + TABLE_NAME + " WHERE EMAIL=?",
                    new String[]{email}
            );
        }


    }
