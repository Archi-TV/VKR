package com.example.coursework.requestServices;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class RequestService extends Service {
    String str;

    IncomingHandler inHandler;

    Messenger messenger;
    Messenger toActivityMessenger;


    @Override
    public void onCreate(){
        super.onCreate();

        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        inHandler = new IncomingHandler(thread.getLooper());
        messenger = new Messenger(inHandler);
    }

    private void sendMsg(JSONObject response, Message msg){
        Message outMsg = Message.obtain(inHandler, 0);
        outMsg.obj = response;
        outMsg.arg1 = msg.arg1;
        outMsg.replyTo = messenger;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return messenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    //обработчик сообщений активити
    private class IncomingHandler extends Handler {
        public IncomingHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(final Message msg){
            //super.handleMessage(msg);

            toActivityMessenger = msg.replyTo;

            str = (String)msg.obj;



            String response = "";
//            RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());;
//            final JsonObjectRequest request;
//            try {
//                request = new JsonObjectRequest(Request.Method.GET, //GET - API-запрос для получение данных
//                        str, null, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//
////                            Message outMsg = Message.obtain(inHandler, 0);
////                            outMsg.obj = response;
////                            outMsg.arg1 = msg.arg1;
////                            outMsg.replyTo = messenger;
////                            Handler s = outMsg.getTarget();
////                            int x = 4;
//                            sendMsg(response, msg);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    } // в случае возникновеня ошибки
//                });
//                mRequestQueue.add(request); // добавляем запрос в очередь
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


            try{
                if(str.contains("http")){
                    if (str.contains("@")){

                        String[] ar = str.split("@");



                        URL obj = new URL(ar[0]);
                        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

                        if (msg.arg1 != 4){
                            connection.setRequestMethod("POST");
                        }else {
                            connection.setRequestMethod("PUT");
                        }
                        connection.setRequestProperty("Content-Type", "application/json; utf-8");
                        connection.setDoOutput(true);

                        try(OutputStream os = connection.getOutputStream()) {
                            byte[] input = ar[1].getBytes(StandardCharsets.UTF_8);
                            os.write(input, 0, input.length);
                        }
//!!!!!!!!!!!!!!!!
                        connection.getInputStream();
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response += inputLine;
                        }
                        in.close();
                    }
                    else{
                        URL obj = new URL(str);
                        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

                        connection.setRequestMethod("GET");
//!!!!!!!!!!!!!!!!
                        connection.getInputStream();
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response += inputLine;
                        }
                        in.close();
                    }
                }
                else {
                    URL obj = new URL("https://google-maps-geocoding.p.rapidapi.com/geocode/json?language=en&address=" + str);
                    HttpsURLConnection connection = (HttpsURLConnection) obj.openConnection();
                    connection.setRequestMethod("GET");
//!!!!!!!!!!!!!!!!
                    connection.setRequestProperty("x-rapidapi-host", "google-maps-geocoding.p.rapidapi.com");
                    connection.setRequestProperty("x-rapidapi-key", "4f299e6edbmsha315db9c7747175p18051djsn8196fb677317");

                    connection.getInputStream();
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response += inputLine;
                    }
                    in.close();
                    System.out.println("response: " + response);
                }

            }catch(Exception e) {
                response += e.toString();
            }

            Message outMsg = Message.obtain(inHandler, 0);
            outMsg.obj = response;
            outMsg.arg1 = msg.arg1;
            outMsg.replyTo = messenger;

            try {
                if( toActivityMessenger != null )
                    toActivityMessenger.send(outMsg);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
