package com.sori.medichat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sori.medichat.db.DbHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Button btn_cambiar_A_Cliente;
    ImageButton btn_RecargarIp;
    ImageButton btn_cultar_datos;
    ImageButton btn_mostrar_datos;
    ImageButton btn_cultar_Lista;
    ImageButton btn_mostrar_Lista;
    Button btn_cultar_añadir;
    ImageButton btn_limpiar;
    TextView areamsj;
    TextView tituloAgregarMedi;
    TextView tituloAgregarGrup;
    TextView getTitulo_ip_ingresar;
    TextView tituloArea;
    EditText mjs;
    EditText edtGrupo;
    EditText edtMdicamento;
    ImageButton btnEnviar;
    Button btnGuardarGrupo;
    Button btnGuardarMedicamento;
    TextView titulo_ip;
    TextView ipServidor;
    TextView tituloGrupo;
    TextView titulomedicamento;
    EditText ip;
    ListView listMedicamentos;
    private Spinner spinner;
    private String seleccionado="";
    boolean banderaTipoArea;
    boolean ocultarAñadir;
    String areaActual;
    String areaOpuesta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**Elementos graficos*/
        areamsj = findViewById(R.id.areaMensajes);
        mjs = findViewById(R.id.txtMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);
        btn_limpiar = findViewById(R.id.btnLimpiar);
        btn_cultar_datos = findViewById(R.id.btnOcultarDatos);
        btn_mostrar_datos = findViewById(R.id.btnMostrarDatos);
        btn_cultar_Lista = findViewById(R.id.btnOcultarLista);
        btn_mostrar_Lista = findViewById(R.id.btnMostrarLista);
        btn_cultar_añadir = findViewById(R.id.btnOcultarRegistros);
        btn_RecargarIp = findViewById(R.id.btnRecargarIp);
        ipServidor = findViewById(R.id.txV_textoIP);
        titulo_ip = findViewById(R.id.textViewIP);
        tituloAgregarMedi = findViewById(R.id.TxV_tituloAgregarMedicamento);
        tituloAgregarGrup = findViewById(R.id.TxV_tituloAgregarGrupo);
        getTitulo_ip_ingresar = findViewById(R.id.TxV_titulo);
        ip = findViewById(R.id.txtIP);
        btnGuardarGrupo = findViewById(R.id.btnGuardarGrupo);
        btnGuardarMedicamento = findViewById(R.id.btnGuardarMedicamento);
        btn_cambiar_A_Cliente = findViewById(R.id.btnCliente);
        edtGrupo = findViewById(R.id.eTxV_Grupo);
        edtMdicamento = findViewById(R.id.eTxV_Medicamento);
        listMedicamentos = findViewById(R.id.listMedicamentos);
        tituloGrupo = findViewById(R.id.TxV_tituloAgregarGrupo);
        titulomedicamento = findViewById(R.id.TxV_tituloAgregarMedicamento);
        tituloArea = findViewById(R.id.textViewArea);

        ocultarAñadir= false; //True -> Ocultar false -> Mostrar
        banderaTipoArea= false; //True -> CENDIS false -> MERS
        areaActual = "MERS";
        areaOpuesta = "CENDIS";
        tituloArea.setText("Área  de "+areaActual);
        btn_mostrar_datos.setVisibility(View.GONE);
        btn_mostrar_Lista.setVisibility(View.GONE);

        /**Añade scroll al EditText de respuesta*/
        areamsj.setMovementMethod(new ScrollingMovementMethod());

        /**Spinner*/
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        loadSpinner();

        /***Lista de medicamentos*/
        setListener();


        ipServidor.setText(getIP(false,this));


        btn_cultar_añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ocultarAñadir = !ocultarAñadir;

                if(ocultarAñadir) {
                    tituloAgregarGrup.setVisibility(View.GONE);
                    tituloAgregarMedi.setVisibility(View.GONE);
                    btnGuardarGrupo.setVisibility(View.GONE);
                    btnGuardarMedicamento.setVisibility(View.GONE);
                    edtGrupo.setVisibility(View.GONE);
                    edtMdicamento.setVisibility(View.GONE);
                    btn_cultar_añadir.setText("Mostrar Añadir");
                }else{
                    tituloAgregarGrup.setVisibility(View.VISIBLE);
                    tituloAgregarMedi.setVisibility(View.VISIBLE);
                    btnGuardarGrupo.setVisibility(View.VISIBLE);
                    btnGuardarMedicamento.setVisibility(View.VISIBLE);
                    edtGrupo.setVisibility(View.VISIBLE);
                    edtMdicamento.setVisibility(View.VISIBLE);
                    btn_cultar_añadir.setText("Ocultar Añadir");
                }

            }});

        btn_cultar_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    titulo_ip.setVisibility(View.GONE);
                    btn_cambiar_A_Cliente.setVisibility(View.GONE);
                    ipServidor.setVisibility(View.GONE);
                    getTitulo_ip_ingresar.setVisibility(View.GONE);
                    ip.setVisibility(View.GONE);
                    btn_RecargarIp.setVisibility(View.GONE);
                    btn_cultar_datos.setVisibility(View.GONE);
                    btn_mostrar_datos.setVisibility(View.VISIBLE);

            }});

        btn_mostrar_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titulo_ip.setVisibility(View.VISIBLE);
                btn_cambiar_A_Cliente.setVisibility(View.VISIBLE);
                ipServidor.setVisibility(View.VISIBLE);
                getTitulo_ip_ingresar.setVisibility(View.VISIBLE);
                ip.setVisibility(View.VISIBLE);
                btn_RecargarIp.setVisibility(View.VISIBLE);
                btn_mostrar_datos.setVisibility(View.GONE);
                btn_cultar_datos.setVisibility(View.VISIBLE);
            }});

        btn_cultar_Lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.GONE);
                listMedicamentos.setVisibility(View.GONE);
                btn_cultar_Lista.setVisibility(View.GONE);
                btn_mostrar_Lista.setVisibility(View.VISIBLE);

            }});

        btn_mostrar_Lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                listMedicamentos.setVisibility(View.VISIBLE);
                btn_mostrar_Lista.setVisibility(View.GONE);
                btn_cultar_Lista.setVisibility(View.VISIBLE);
            }});


        //Boton para ser cliente
        btn_cambiar_A_Cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                banderaTipoArea = !banderaTipoArea;

                if(banderaTipoArea) {
                    spinner.setVisibility(View.GONE);
                    listMedicamentos.setVisibility(View.GONE);
                    btnGuardarGrupo.setVisibility(View.GONE);
                    btnGuardarMedicamento.setVisibility(View.GONE);
                    edtMdicamento.setVisibility(View.GONE);
                    edtGrupo.setVisibility(View.GONE);
                    tituloGrupo.setVisibility(View.GONE);
                    btn_cultar_Lista.setVisibility(View.GONE);
                    btn_mostrar_Lista.setVisibility(View.GONE);
                    titulomedicamento.setVisibility(View.GONE);
                    btn_cultar_añadir.setVisibility(View.GONE);
                    areaOpuesta="MERS";
                    areaActual="CENDIS";
                }else{
                    spinner.setVisibility(View.VISIBLE);
                    listMedicamentos.setVisibility(View.VISIBLE);
                    btnGuardarGrupo.setVisibility(View.VISIBLE);
                    btnGuardarMedicamento.setVisibility(View.VISIBLE);
                    edtMdicamento.setVisibility(View.VISIBLE);
                    edtGrupo.setVisibility(View.VISIBLE);
                    tituloGrupo.setVisibility(View.VISIBLE);
                    titulomedicamento.setVisibility(View.VISIBLE);
                    btn_cultar_añadir.setVisibility(View.VISIBLE);
                    btn_cultar_Lista.setVisibility(View.VISIBLE);
                    btn_mostrar_Lista.setVisibility(View.GONE);
                    areaOpuesta="CENDIS";
                    areaActual="MERS";
                }
                btn_cambiar_A_Cliente.setText("Área " +areaOpuesta);
                tituloArea.setText("Área  de "+areaActual);
            }});


        btn_limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areamsj.setText("");
            }
        });

        btn_RecargarIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipServidor.setText(getIP(true,MainActivity.this));
            }
        });

        Thread myThread = new Thread(new Server());
        myThread.start();

        //Boton para enviar mensaje
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ip.getText().toString().length()>0) {
                    BackgroundTask back = new BackgroundTask();

                    String mensaje =
                            "-----------\n"+
                            areaActual+": "+mjs.getText().toString() +"\n";

                    back.execute(ip.getText().toString(), mensaje);

                    mjs.setText("");
                    String chat = areamsj.getText().toString()+"\n"+mensaje;

                    areamsj.setText(chat);
                }else
                    Toast.makeText(MainActivity.this,"Primero ingresa la IP de otro celular",Toast.LENGTH_LONG).show();
            }});


        btnGuardarGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom =  edtGrupo.getText().toString();

                Toast.makeText(getApplicationContext(),"Grupo creado: " +nom,Toast.LENGTH_LONG).show();

                if (nom.trim().length()>0){
                    DbHelper sqLiteHelper = new DbHelper(getApplicationContext());

                    sqLiteHelper.insertGrupo(nom,MainActivity.this);

                    edtGrupo.setText("");

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputMethodManager.hideSoftInputFromWindow(edtGrupo.getWindowToken(),0);
                    loadSpinnerDataNuevoGrupo();
                }else
                    Toast.makeText(getApplicationContext(),"Escribe un nuevo grupo antes de guardar",Toast.LENGTH_LONG).show();
            }
        });


        btnGuardarMedicamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nom =  edtMdicamento.getText().toString();
                String grupo = seleccionado;
                Toast.makeText(getApplicationContext(),"Medicamento creado: " +nom,Toast.LENGTH_LONG).show();

                if (nom.trim().length()>0){
                    DbHelper sqLiteHelper = new DbHelper(getApplicationContext());

                    sqLiteHelper.insertMedicamentos(nom,grupo,MainActivity.this);

                    edtMdicamento.setText("");

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(edtGrupo.getWindowToken(),0);
                    loadSpinnerData(seleccionado);
                }else
                    Toast.makeText(getApplicationContext(),"Escribe un nuevo medicamento antes de guardar",Toast.LENGTH_LONG).show();
            }
        });
    }



    /** Obtener  IP*/
    public static String getIP(Boolean v, Context c){
        List<InetAddress> addrs;
        String address = "";
        try{
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface intf : interfaces){
                addrs = Collections.list(intf.getInetAddresses());
                for(InetAddress addr : addrs){
                    if(!addr.isLoopbackAddress() && addr instanceof Inet4Address){
                        address = addr.getHostAddress().toUpperCase(new Locale("es", "MX"));
                        if (v) Toast.makeText(c,"IP Actualizada",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (Exception e){
            Log.w(null, "Ex getting IP value " + e.getMessage());
        }

        if (!(address.length() > 0)){
            address = "Sin Conexión";
            if (v) Toast.makeText(c,"Sin Conexión",Toast.LENGTH_SHORT).show();
        }

        return address;
    }


    /**Colocar Medicamentos*/
    public void listarMedicamentos (String grupo){
        DbHelper sqLiteHelper = new DbHelper(getApplicationContext());
        List<String> nombres = sqLiteHelper.getMedicamentosGrupo(grupo);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,nombres);

        listMedicamentos.setAdapter(arrayAdapter);
        listMedicamentos.getLayoutParams().height = nombres.size()*150 ;
    }

    private void setListener(){
        ListView lista = findViewById(R.id.listMedicamentos);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mjs.setText(mjs.getText().toString()+"\n"+adapterView.getItemAtPosition(position));

            }
        });
    }

    /**Metodos del spinner*/

    private void loadSpinnerData(String grupo) {
        DbHelper sqLiteHelper = new DbHelper(getApplicationContext());

        List<String> nombres = sqLiteHelper.getTodosGrupos();
        
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,nombres);

        
        spinner.setAdapter(arrayAdapter);
        int posicion=0;
        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(grupo)) {
                posicion = i;
            }
        }

        spinner.setSelection(posicion);

    }


    private void loadSpinner() {
        DbHelper sqLiteHelper = new DbHelper(getApplicationContext());

        List<String> nombres = sqLiteHelper.getTodosGrupos();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,nombres);


        spinner.setAdapter(arrayAdapter);
    }

    private void loadSpinnerDataNuevoGrupo() {
        DbHelper sqLiteHelper = new DbHelper(getApplicationContext());

        List<String> nombres = sqLiteHelper.getTodosGrupos();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,nombres);
    
        
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(nombres.size()-1);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String nombre = adapterView.getItemAtPosition(position).toString();
        listarMedicamentos(nombre);
        seleccionado = nombre;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**Metodos para Conexion de sockets*/
    class Server implements Runnable{
        ServerSocket ss;
        Socket mysocket;
        DataInputStream dis;
        String mensaje;
        Handler handler = new Handler();

        public void run() {
            try {
                ss = new ServerSocket(9700);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"Esperando cliente: ",Toast.LENGTH_LONG).show();
                    }
                });

                while (true){
                    mysocket = ss.accept();
                    dis = new DataInputStream((mysocket.getInputStream()));
                    mensaje = dis.readUTF();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                            String chat = areamsj.getText().toString()+"\n"+mensaje;
                            areamsj.setText(chat);
                        }
                    });
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    class BackgroundTask extends AsyncTask<String,Void,String> {

        Socket s;
        DataOutputStream dos;
        String ip,mensaje;


        @Override
        protected String doInBackground(String... params) {
            ip = params[0];
            mensaje = params[1];

            try {
                s = new Socket(ip,9700);
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(mensaje);
                dos.close();

            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }
    }


}