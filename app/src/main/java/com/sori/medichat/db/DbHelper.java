package com.sori.medichat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.sori.medichat.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "DBMedicamentos";

    private static final String TABLE_MEDICAMENTOS = "Medicamentos";
    private static final String COLUMN_IDMEDICAMENTO = "IdMedicamento";
    private static final String COLUMN_NOM_MEDICAMENTO = "Nombre";

    private static final String TABLE_GRUPOS = "Grupos";
    private static final String COLUMN_IDGRUPO = "IdGrupo";
    private static final String COLUMN_NOM_GRUPO= "Nombre";


    String sqlTablaMedicamento = "CREATE TABLE "+TABLE_MEDICAMENTOS+"("+
            COLUMN_IDMEDICAMENTO + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COLUMN_NOM_MEDICAMENTO+" TEXT," +
            COLUMN_IDGRUPO+" INTEGER NOT NULL REFERENCES "+TABLE_GRUPOS+"("+COLUMN_IDGRUPO+")" +
            ")";


    String sqlTablaGrupo = "CREATE TABLE "+TABLE_GRUPOS+"("+
            COLUMN_IDGRUPO + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            COLUMN_NOM_GRUPO+" TEXT)";

    public DbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sqlTablaMedicamento);
        sqLiteDatabase.execSQL(sqlTablaGrupo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int versionAnterior, int versionNueva) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Medicamentos"+TABLE_MEDICAMENTOS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Grupos"+TABLE_GRUPOS);
        onCreate(sqLiteDatabase);
    }

    public void insertMedicamentos (String nombre,String grupo,Context context){
        String Query = "SELECT "+COLUMN_IDGRUPO+" FROM " + TABLE_GRUPOS +" where "+ COLUMN_NOM_GRUPO+"='"+grupo+"'";
        SQLiteDatabase sqLiteOpenHelper = this.getWritableDatabase();
        Cursor cursor = sqLiteOpenHelper.rawQuery(Query,null);

        if(cursor.moveToFirst()){
            int idGrupo = cursor.getInt(0);
            Query = "INSERT INTO "+TABLE_MEDICAMENTOS+" ("+COLUMN_NOM_MEDICAMENTO+","+COLUMN_IDGRUPO+") VALUES ('" + nombre +"',"+idGrupo+")";
            sqLiteOpenHelper.execSQL(Query);
        }else{
            System.out.println("Error al insertar -------------------------------------------");
            Toast.makeText(context,"Grupo repetido",Toast.LENGTH_LONG).show();
        }
    }

    public void insertGrupo (String nombre,Context context){
        String Query = "SELECT * FROM " + TABLE_GRUPOS +" where "+ COLUMN_NOM_GRUPO+"='"+nombre+"'";
        SQLiteDatabase sqLiteOpenHelper = this.getWritableDatabase();
        Cursor cursor = sqLiteOpenHelper.rawQuery(Query,null);

        if(!cursor.moveToFirst()){
            String Query2 = "INSERT INTO "+TABLE_GRUPOS+" ("+COLUMN_NOM_GRUPO+") VALUES ('"+ nombre + "')";
            sqLiteOpenHelper.execSQL(Query2);
            Toast.makeText(context,"Se inserto grupo",Toast.LENGTH_SHORT).show();
        }else{
            System.out.println("Error al insertar -------------------------------------------");
            Toast.makeText(context,"Grupo repetido",Toast.LENGTH_LONG).show();
        }

    }


    public void borrarMedicamento (String nombre){
        String Query = "DELETE FROM " + TABLE_MEDICAMENTOS +" where "+ COLUMN_NOM_MEDICAMENTO+"="+"'"+nombre+"'";

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Query,null);

        if(cursor.moveToFirst()){
            ContentValues contentValues = new ContentValues();
            contentValues.remove(cursor.getColumnName(0));
        }
    }

    public List<String> getMedicamentosGrupo (String grupo){
        String Query = "SELECT "+COLUMN_IDGRUPO+" FROM " + TABLE_GRUPOS +" where "+ COLUMN_NOM_GRUPO+"='"+grupo+"'";
        SQLiteDatabase sqLiteOpenHelper = this.getWritableDatabase();
        Cursor cursor = sqLiteOpenHelper.rawQuery(Query,null);
        int idGrupo;
        if(cursor.moveToFirst()) {
             idGrupo= cursor.getInt(0);


        List <String> list = new ArrayList();
        Query = "Select "+COLUMN_NOM_MEDICAMENTO+" From " + TABLE_MEDICAMENTOS + " WHERE "+COLUMN_IDGRUPO+"="+idGrupo;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        cursor = sqLiteDatabase.rawQuery(Query,null);

        if(cursor.moveToFirst()){
            do {
                list.add(cursor.getString(0));
            }while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();
        return list;
        }else {
            System.out.println("Error al recuperar medicamentos");
            return null;
        }
    }

    public List<String> getTodosGrupos (){
        List <String> list = new ArrayList();

        String Query = "SELECT *  FROM " + TABLE_GRUPOS;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(Query,null);

        if(cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(1));
                }while (cursor.moveToNext());

            cursor.close();
            sqLiteDatabase.close();
            return list;
        }else {
            System.out.println("Error al recuperar Grupo de medicamentos");
            return list;
        }
    }
}