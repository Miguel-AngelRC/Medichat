package com.sori.medichat;

import androidx.appcompat.app.AppCompatActivity;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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

public class ClienteActivity extends AppCompatActivity {

        TextView areamsjCliente;
        EditText mjs;
        Button btnEnviar;
        TextView ipServidor;
        EditText ip;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cliente);

            /**Elementos graficos*/
            areamsjCliente = findViewById(R.id.areaMensajesCliente);
            mjs = findViewById(R.id.txtMensaje);
            btnEnviar = findViewById(R.id.btnEnviarC);
            ipServidor = findViewById(R.id.txV_textoIP);
            ip = findViewById(R.id.txtIP);


            /**Añade scroll al EditText de respuesta*/
            areamsjCliente.setMovementMethod(new ScrollingMovementMethod());
            areamsjCliente.setText("Si escribe");

            ipServidor.setText("IP: "+getIP());

            Thread myThreadCliente = new Thread(new ServerCliente());
            myThreadCliente.start();

            //Boton para enviar mensaje
            btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ip.getText().toString().length()>0) {
                        BackgroundTaskcliente back2 = new BackgroundTaskcliente();

                        String mensaje = "CENDIS: "+mjs.getText().toString();

                        back2.execute(ip.getText().toString(),mensaje ); //envia mensaje
                        mjs.setText("");
                        String chat = areamsjCliente.getText().toString()+"\n"+mensaje;
                        areamsjCliente.setText(chat);
                    }else
                        Toast.makeText(ClienteActivity.this,"Primero ingresa la IP de otro celular",Toast.LENGTH_LONG).show();
                }});
        }



        /** Obtener  IP*/
        public static String getIP(){
            List<InetAddress> addrs;
            String address = "";
            try{
                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                for(NetworkInterface intf : interfaces){
                    addrs = Collections.list(intf.getInetAddresses());
                    for(InetAddress addr : addrs){
                        if(!addr.isLoopbackAddress() && addr instanceof Inet4Address){
                            address = addr.getHostAddress().toUpperCase(new Locale("es", "MX"));
                        }
                    }
                }
            }catch (Exception e){
                Log.w(null, "Ex getting IP value " + e.getMessage());
            }

            if (!(address.length() > 0))
                address = "Por favor conectate a un red wifi";

            return address;
        }


        /**Metodos para Conexion de sockets*/
       class ServerCliente implements Runnable{
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
                            Toast.makeText(getApplicationContext(),"Esperando Servidor: ",Toast.LENGTH_LONG).show();
                        }
                    });

                    while (true){
                        mysocket = ss.accept();
                        dis = new DataInputStream((mysocket.getInputStream()));
                        mensaje = dis.readUTF();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String chat = areamsjCliente.getText().toString()+"\n"+mensaje;
                                areamsjCliente.setText(chat);
                                Toast.makeText(getApplicationContext(),"Mensaje(C): "+ mensaje,Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        class BackgroundTaskcliente extends AsyncTask<String,Void,String> {

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