package com.example.appcompra;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.appcompra.utils.Notificacion;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class NotificationService extends Service {
    public static final int NOTIFICATION_ID = 234;
    public static final String CHANNEL_1_ID = "channel1";

    private Socket socketComunicacion=null;
    private PedirNotificacionesThread p3;
    private NotificationManager notificationManager;


    @Override
    public void onCreate() {
        super.onCreate();
            Log.e("service","Servicio creado");
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            createNotificationChannels();
            p3=new PedirNotificacionesThread(socketComunicacion);
            p3.setDaemon(true);
            p3.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("service","Servicio ya estaba creado");
        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    class PedirNotificacionesThread extends Thread{
        Socket socket;
        BufferedReader in;
        PrintWriter out;
        boolean working;
        LinkedList<Notificacion> notificaciones;
        PedirNotificacionesThread(Socket socket){
            this.socket=socket;
            this.working=true;
            this.notificaciones=new LinkedList<>();
        }
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            /*
            while(working){
                try {
                    Thread.sleep(2500);
                    mostrarNotificacion("Se ha añadido a lista nuevaLista","Pepito a añadadido a nueva lista, manuel ha añadido a lista 4");
                    Log.e("service","log de notificaion");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }*/

            Log.e("service","dentro del hilo");
            try {
                socketComunicacion=new Socket(InetAddress.getByName("192.168.1.132"),Constants.PORTSERVICE);
                Log.e("service","socketAceptado");
                in = new BufferedReader(new InputStreamReader(socketComunicacion.getInputStream()));
                out = new PrintWriter(socketComunicacion.getOutputStream(), true);

                while (working) {
                    Log.e("service","Esperando notificacion");
                    String entrada=in.readLine();
                    if(entrada!=null){
                        Log.e("servidor",entrada);
                        notificaciones=procesarNotificacionJSON(entrada);
                        if(!notificaciones.isEmpty()){
                            String mensaje=mensajeNotificaciones(notificaciones);
                            mostrarNotificacion("AppCompra",mensaje);
                        }
                    }
                }
                }catch (IOException e) {
                Log.e("error io",e.getMessage());
                    e.printStackTrace();
                }

            }
        }
    public String mensajeNotificaciones(LinkedList<Notificacion> not){
        String mensaje="";
        for(Notificacion n:not){
            mensaje+=n.getAutor()+" ha ";
            switch (n.getOperacion()){
                case "add":
                    mensaje+="añadido ";
                    break;
                case "delete":
                    mensaje+="borrado ";
                    break;
                case "mark":
                    mensaje+="comprado ";
                    break;
                case "unmark":
                    mensaje+="reañadido ";
                    break;
            }
            mensaje+="productos a la lista "+n.getNombreLista();
        }
        return mensaje;
    }

    public LinkedList<Notificacion> procesarNotificacionJSON(String json) {
        LinkedList<Notificacion> notificaciones =new LinkedList<>();
        String autor="";
        String tipoNotificacion;
        String operacion;
        String rol="";
        String nombreLista;

        try{
            JSONObject raiz=new JSONObject(json);
            JSONArray data=raiz.getJSONArray("notificaciones");
            JSONObject notificacionActual;
            for (int i=0;i<data.length();i++){
                notificacionActual=data.getJSONObject(i);
                if(notificacionActual.has("autor"))
                    autor=notificacionActual.getString("autor");
                else
                    autor=notificacionActual.getString("usuario");
                operacion=notificacionActual.getString("operacion");

                if(notificacionActual.has("rol")){
                    rol=notificacionActual.getString("rol");
                    tipoNotificacion="usuarios";
                }else{
                    tipoNotificacion="listas";
                }

                nombreLista=notificacionActual.getString("nombreLista");
                Notificacion n=new Notificacion(autor,tipoNotificacion,operacion,rol,nombreLista);
                if(!autor.equals(QueryUtils.getUsuario().getNombre()))
                    notificaciones.add(n);
            }

        }catch (JSONException e){
            Log.e("JSONException ","JSON mal formado "+e.getMessage());
        }

        return notificaciones;
    }

    public void backgroundToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void mostrarNotificacion(String titulo,String contenido){
        Intent intent = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setColor(Color.WHITE)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(contenido))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setContentIntent(pendingIntent)
                .setChannelId(CHANNEL_1_ID)
                .setGroupSummary(true)
                .build();
        notificationManager.notify(1, notification);
    }


    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is Channel 1");
            channel1.enableLights(true);

            channel1.setLightColor(Color.RED);

            channel1.enableVibration(true);
            notificationManager.createNotificationChannel(channel1);
        }
    }

}


