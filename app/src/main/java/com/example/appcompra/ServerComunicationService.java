package com.example.appcompra;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;


public class ServerComunicationService extends Service {

    private static final String CHANNEL_ID ="example" ;
    protected boolean working=true ;
    @Override
    public void onCreate() {
        super.onCreate();
        new ServerThread().start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    protected class ServerThread extends Thread{

        ServerThread(){

        }

        @Override
        public void run() {
            while (working) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("servicio","conectando");
            }
        }
    }
}
