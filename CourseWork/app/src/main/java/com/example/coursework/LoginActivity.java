package com.example.coursework;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coursework.requestServices.RequestService;

public class LoginActivity extends AppCompatActivity  {

    private Messenger toServiceMessenger;
    private LoginServiceConnection serverConnection;
    private final Messenger messenger = new Messenger(new IncomingHandler());
    private String url = "http://192.168.1.74:8080/";


    private void deserializeAccessResponse(String json){

    }

    private void deserializeRegisterResponse(String json){

    }

    @SuppressLint("HandlerLeak")
    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg){
            String json = (String)msg.obj;
            switch (msg.arg1) {
                case 1:
                    deserializeAccessResponse(json);
                    break;
                case 2:
                    deserializeRegisterResponse(json);
                    break;
            }
        }
    }

    private void getAccessAsync(){

        sendRequest(1, 0);
    }

    private void registerAsync(){

        sendRequest(2, 1);
    }

    private class LoginServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            toServiceMessenger = new Messenger(service);
           // getRoutesAsync();
           // isServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {	}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try{
            runService();
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void sendRequest(int requestType, int getOrPost){
        Message msg = Message.obtain(null, requestType);
        msg.replyTo = messenger;
        msg.obj = url;
        msg.arg1 = requestType;
        msg.arg2 = getOrPost;
        try {
            toServiceMessenger.send(msg);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void runService(){
        bindService(new Intent(this, RequestService.class),
                (serverConnection = new LoginServiceConnection()),
                Context.BIND_AUTO_CREATE);
    }

    public void RegisterWindow(View view){
        setContentView(R.layout.activity_register);
    }

    public void Register(View view){
        EditText name = (EditText) findViewById(R.id.edit_user_reg);
        EditText password = (EditText) findViewById(R.id.edit_password_reg);

        String userName = name.getText().toString();
        String pas = password.getText().toString();

        if (userName.length() < 5){
            Toast.makeText(getApplicationContext(), "Имя пользователя должно содержать больше 5 символов", Toast.LENGTH_LONG).show();
            return;
        }
        if (pas.length() < 5){
            Toast.makeText(getApplicationContext(), "Пароль должен содержать больше 5 символов", Toast.LENGTH_LONG).show();
            return;
        }

        if (pas.matches("[A-Z]+") || pas.matches("[a-z]+")){
            Toast.makeText(getApplicationContext(), "Пароль должен содержать символы разного регистра", Toast.LENGTH_LONG).show();
            return;
        }

        /////// send to server
        //registerAsync();

        setContentView(R.layout.activity_login);
    }

    public void backToLogin(View view){
        setContentView(R.layout.activity_login);
        Login(view);
    }

    // Обрабатываем нажатие кнопки "Войти":
    public void Login(View view) {
        // Связываемся с элементами нашего интерфейса:
        // Объявляем об использовании следующих объектов:
        EditText username = (EditText) findViewById(R.id.edit_user);
        EditText password = (EditText) findViewById(R.id.edit_password);

/////////////send to server
        //getAccessAsync();


        if (username.getText().toString().equals("avtolmachev") &&
                password.getText().toString().equals("Qwerty1")) {
            Toast.makeText(getApplicationContext(), "Вход выполнен!",Toast.LENGTH_SHORT).show();

            // Выполняем переход на другой экран:
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        // В другом случае выдаем сообщение с ошибкой:
        else {
            Toast.makeText(getApplicationContext(), "Неправильные данные!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        unbindService(serverConnection);
    }
}
