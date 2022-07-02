package com.example.camisaria_ads.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.camisaria_ads.Camisas.Camisa;
import com.example.camisaria_ads.R;

public class DatabaseHelper extends SQLiteOpenHelper {
  //CRIANDO AS VARIAVEIS
   private static final String DATABASE_NAME = "camisaria";
   private static final String TABLE_CAMISAS = "camisas";

   private static final String CREATE_TABLE_CAMISAS = "CREATE TABLE " + TABLE_CAMISAS +"("+
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "tipo VARCHAR (100),"+
            "tamanho VARCHAR(4),"+
            "cor VARCHAR(10));";

   private static final  String DROP_TABLE_CAMISAS = "DROP TABLE IF EXISTS "+ TABLE_CAMISAS;
    /*private static final android.R.attr R= ;*/
    /*private static final android.R;*/
    private Camisa c;

    public DatabaseHelper(Context context){
//o construtor que passará para a super classe as informações do local e versão do banco
        super(context, DATABASE_NAME, null, 1);
   }


    @Override // responsável por informar ao Java que o método em questão está sendo reescrito.
    public void onCreate(SQLiteDatabase db) {
    /*método chamado quando a aplicação cria o banco de dados pela primeira vez*/
        db.execSQL(CREATE_TABLE_CAMISAS);
    }
    @Override // responsável por informar ao Java que o método em questão está sendo reescrito.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    /*método responsável por atualizar o banco de dados. O objeto db é a instância da classe SQLiteDatabase
      Os dois outros parâmetros, inteiros, contém a versão antiga e nova da tabela para o qual o upgrade de ser executado*/
      db.execSQL(DROP_TABLE_CAMISAS); // apaga a tabela se ela existir
      onCreate(db); // recria a tabela com as alterações feitas
    }

    /* INÍCIO CRUD Camisas*/

    public long createCamisas (Camisa c){
        /*código responsável por inserir dados*/
        SQLiteDatabase db = this.getWritableDatabase(); // chamando banco de dados para escrita
        ContentValues cv = new ContentValues(); // classe que cria o map de dados a ser inserido no formato key/value
        cv.put("tipo", c.getTipo()); //populando o map
        cv.put("tamanho", c.getTamanho());
        cv.put("cor", c.getCor());
        long id= db.insert(TABLE_CAMISAS, null, cv);//tabela, parametro null, map
        db.close();//encerrando a operação
        return id;
    }

    public long updateCamisas (Camisa c){
        /*código responsável por alterar registros no banco de dados*/
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();// classe que cria o map de dados a ser alterado no formato key/value
        cv.put("tipo", c.getTipo());//populando o map
        cv.put("tamanho", c.getTamanho());
        cv.put("cor", c.getCor());
        long id= db.update(TABLE_CAMISAS,cv, "_id = ? ", new String[]{String.valueOf(c.getId())});
        db.close(); //encerrando a operação
        return id;
    }


    public long deleteCamisas (Camisa c){
        /*código responsável por deletar registros no banco de dados*/
        SQLiteDatabase db = this.getWritableDatabase();// chamando banco de dados para escrita
        long id= db.delete(TABLE_CAMISAS,"_id = ? ", new String[]{String.valueOf(c.getId())});//método delete. Recebe nome da tabela, clausula Where, argumentos. O valueOf converte diversos tipos para String.
        db.close();//encerrando a operação
        return id;
    }

    public void getAllCamisas(Context context,ListView lv){
        /*código responsável por obter os registros no banco de dados*/
        SQLiteDatabase db = this.getWritableDatabase();//metodo para obter acesso ao banco de dados pra leitura
        String[] columns = {"_id","tipo","tamanho","cor"};//array de strings que sao as colunas
        Cursor data = db.query(TABLE_CAMISAS, columns, null,null,null,null,"tipo"); // o cursor data recebe o retorno da query
        int[] to = {R.id.textViewIdListarCamisas, R.id.textViewTipoListarCamisas, R.id.textViewTamanhoListarCamisas,R.id.textViewCorListarCamisas}; //array de inteiros, que sã oreferências aos conteudos do textView's
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context,R.layout.camisas_item_list_view,data,columns, to, 0); // classe Adpter que tem a função de personalizar a apresentação de dados originados de um banco de dados em lista de acordo com o layout criado
        lv.setAdapter(simpleCursorAdapter);//método do ListView de atribuição
        db.close();//encerrando a operação
    }

    public Camisa getByIdCamisa (int id){
        SQLiteDatabase db = this.getReadableDatabase();// metodo para obter acesso ao banco de dados pra leitura
        String[] columns = {"_id","tipo","tamanho","cor"};//array tipo strings que sao as colunas
        String[] args = {String.valueOf(id)};//método estático que converte o id em uma string
        Cursor data = db.query(TABLE_CAMISAS, columns, "_id = ?",args, null, null, null);// o cursor data recebe o retorno da query
        data.moveToFirst();// testa o retono da consulta e move o cursor para o primeiro resultado
        Camisa c = new Camisa(); // nova instância da classe Camisa
        c.setId(data.getInt(0));// obtem o valor da coluna especificada na tupla corrente e passa o dado para o setter ID da classe camisa
        c.setTipo(data.getString(1)); // idem, porém aqui o Tipo e sendo ele string
        c.setTamanho(data.getString(2)); // idem, porém aqui o Tamanho sendo ele string
        c.setCor(data.getString(3)); //idem, porém a Cor e sendo ela string
        data.close();
        db.close(); //encerrando a operação com o db
        return c;
    }
    /* FIM CRUD Camisas*/

}