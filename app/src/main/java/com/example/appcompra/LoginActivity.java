package com.example.appcompra;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcompra.clases.Singleton;
import com.example.appcompra.clases.Usuario;
import com.example.appcompra.utils.Peticion;
import com.example.appcompra.utils.QueryUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements Serializable,LoaderCallbacks<Cursor> {
    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    public Socket socket=null;
    public Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Singleton.getInstance().setSharedPreferences(getPreferences(MODE_PRIVATE));
        Singleton.getInstance().setEditor(Singleton.getInstance().getSharedPreferences().edit());

        /*
        editor.clear();
        editor.apply();
        */

        setContentView(R.layout.login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        QueryUtils.setIP("192.168.1.132");

        if(QueryUtils.getIP()==null)
            pedirIpPopup();
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button registerButton= (Button) findViewById(R.id.email_register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        if(comprobarSharedPreferences()) {
            mAuthTask=new UserLoginTask(Singleton.getInstance().getSharedPreferences().getString("email",""),Singleton.getInstance().getSharedPreferences().getString("pass",""));
            mAuthTask.execute();
            showProgress(true);
        }

    }

    private boolean comprobarSharedPreferences() {
        int id=Singleton.getInstance().getSharedPreferences().getInt("id",0);
        Log.e("xd",id+"");
        if (id==0) return false;
            return true;

    }


    public void pedirIpPopup(){
        View view = LayoutInflater.from(this).inflate(R.layout.popup_pedir_ip, null);
        final AutoCompleteTextView editText=view.findViewById(R.id.editText);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.color.backgroundColor);
        Button botonAceptarPopUp=view.findViewById(R.id.botonAceptarPopup);

        botonAceptarPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().isEmpty())
                    QueryUtils.setIP(editText.getText().toString());
                else
                    QueryUtils.setIP("127.0.0.1");
                dialog.dismiss();
            }
        });
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reseteamos los errores.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Guardamos los valores de los edit text
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Comprobamos si es valida segun nuestros criterios la contraseña, si es que ha puesto una
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Lo mismo con el email
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // Hay algun error; no empezar el login y focus el primer view
            focusView.requestFocus();
        } else {
            // Muestra el spinner del progreso, crea el async task y lo ejecutamos
            showProgress(true);
            String pass=encryptPassword(mPasswordView.getText().toString());
            mAuthTask=new UserLoginTask(mEmailView.getText().toString(),pass);
            mAuthTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        //Lo dejamos asi por ahora
        return true;
    }

    private boolean isPasswordValid(String password) {
        //Lo dejamos asi por ahora
        return password.length() > 1;
    }

    /**
     * Mostramos el UI de proceso y ocultamos el login form
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    //Los cursores estos son para autocompletar los emails
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Async task para conectar a los sockets
     */

    public class ComprobarLoginTask extends  AsyncTask<Void, Void, Boolean>{
        private BufferedReader in;
        private PrintWriter out;
        private String entrada;
        private boolean respuesta;
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                if(QueryUtils.getSocket()==null)
                    socket=new Socket(InetAddress.getByName(QueryUtils.getIP()),Constants.PORT);
                else
                    socket=QueryUtils.getSocket();

                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.COMPROBAR_LOGUEO);
                entrada=in.readLine();

                if(entrada.split(Constants.SEPARATOR)[0].equals(Constants.COMPROBAR_LOGUEO_CORRECTA)){
                    respuesta=true;
                }else{
                    respuesta=false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(Boolean respuesta) {
            if(respuesta){
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Usuario", getUsuarioFromSharedPreferences());
                intent.putExtras(bundle);
                finish();
                startActivity(intent);
            }else{
                attemptLogin();
            }
        }

    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private String respuesta;
        private BufferedReader in;
        private PrintWriter out;
        public boolean terminado;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            try {
                if(QueryUtils.getUsuario()!=null)
                    logout();

                if(QueryUtils.getSocket()==null)
                    socket=new Socket(InetAddress.getByName(QueryUtils.getIP()),Constants.PORT);
                else
                    socket=QueryUtils.getSocket();
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream(),true);
                out.println(Constants.LOGIN_PETICION +Constants.SEPARATOR+mEmail+Constants.SEPARATOR+mPassword);
                respuesta=in.readLine();
                if(respuesta.split(Constants.SEPARATOR)[0].equals(Constants.LOGIN_RESPUESTA_CORRECTA)) {
                    QueryUtils.setSocket(socket);
                    usuario=new Usuario(Integer.parseInt(respuesta.split(Constants.SEPARATOR)[1]),respuesta.split(Constants.SEPARATOR)[2],mEmail);
                    terminado=true;
                }else
                    terminado=false;
                Log.e("xd",respuesta);
            } catch (UnknownHostException e) {
               e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return terminado;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                saveSharedPreferences(usuario,mPassword);
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Usuario", usuario);
                intent.putExtras(bundle);
                //stopService(new Intent(getApplicationContext(), NotificationService.class));
                startService(new Intent(getApplicationContext(), NotificationService.class));
                finish();
                startActivity(intent);
            } else {
                finish();
                startActivity(getIntent());
                mPasswordView.setError("Email o contraseña incorrectos");
                mPasswordView.requestFocus();
                Singleton.getInstance().getEditor().clear();
                Singleton.getInstance().getEditor().apply();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class UserLoginTaskTest extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private String respuesta;
        public boolean terminado;

        UserLoginTaskTest(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            respuesta = Constants.DUMMY_LOGIN;
            Log.e("xd", respuesta);

            usuario = new Usuario(Integer.parseInt(respuesta.split(Constants.SEPARATOR)[1]), respuesta.split(Constants.SEPARATOR)[2], mEmail);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Usuario", usuario);
                intent.putExtras(bundle);
                finish();
                startActivity(intent);
            } else {
                finish();
                startActivity(getIntent());
                mPasswordView.setError("Email o contraseña incorrectos");
                mPasswordView.requestFocus();

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public void saveSharedPreferences(Usuario usuario,String pass){
        Singleton.getInstance().getEditor().putInt("id",usuario.getId());
        Singleton.getInstance().getEditor().putString("nombre",usuario.getNombre());
        Singleton.getInstance().getEditor().putString("email",usuario.getEmail());
        Singleton.getInstance().getEditor().putString("pass",pass);
        Singleton.getInstance().getEditor().apply();
    }

    public Usuario getUsuarioFromSharedPreferences(){
        int id=Singleton.getInstance().getSharedPreferences().getInt("id",0);
        String nombre=Singleton.getInstance().getSharedPreferences().getString("nombre","");
        String email=Singleton.getInstance().getSharedPreferences().getString("email","");
        return new Usuario(id,nombre,email);
    }

    public String encryptPassword(String pass){
        String hash=null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hash = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return hash;
    }

    public void logout(){
        Singleton.getInstance().enviarPeticion(new Peticion(Constants.LOGOUT,QueryUtils.getUsuario().getId(),20));
    }
}

