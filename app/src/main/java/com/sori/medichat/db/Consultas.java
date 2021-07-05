package com.sori.medichat.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public class Consultas {

    //Clase para realizar las consultas

    private DbHelper dbHelper;
    private SQLiteDatabase db;


    /********************************************************
     Consultas() => Constructor para crear base de datos
     *********************************************************/
    public Consultas (Context contexto){
        dbHelper = new DbHelper(contexto); //Instancia para crear base de datos
        db = dbHelper.getWritableDatabase(); //Obtener la base de datos

        if(db != null){
            Toast.makeText(contexto, "BASE DE DATOS CREADA Y POBLADA",Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(contexto, "ERROR AL CREAR LA BASE DE DATOS",Toast.LENGTH_LONG).show();
        }
    }


    /********************************************************
     consultaCalendario() => Recupera los datos de Calendario
     *********************************************************/
    public String consultaMedicamento (String fechaBusqueda){
        /** Datos a recuperar*/
        int id;
        String nombre;
        String grupo;

        /**Consulta*/

        Cursor consulta = db.rawQuery(
                "SELECT id,nombre,dios,frase,representa " +
                        "FROM calendarioFechas  inner join calendarioDatos on calendarioFechas.idGrado = calendarioDatos.idGrado " +
                        "   where "+fechaBusqueda+" = calendarioFechas.id", null);

        try{
            consulta.moveToFirst();
            // do {
            id = consulta.getInt(consulta.getColumnIndex("id"));
            nombre = consulta.getString(consulta.getColumnIndex("nombre"));
            /*dios = consulta.getString(consulta.getColumnIndex("dios"));
            frase = consulta.getString(consulta.getColumnIndex("frase"));
            representa = consulta.getString(consulta.getColumnIndex("representa"));
            //}while(consulta.moveToNext());*/
        }catch (Exception e){
            return "No hay Calendario para esta fecha";
        }

        /*String mensaje =     nombre + "\n" +
                            "Dios regente: " + dios + "\n" +
                             frase + "\n" +
                             representa + "\n";*/

        return "";
    }
}

