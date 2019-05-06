package com.example.appcompra;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appcompra.clases.Usuario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class RegisterActivity extends AppCompatActivity implements Serializable,LoaderCallbacks<Cursor> {
    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mNombreView;
    private EditText mPasswordView;
    private EditText mFechaNacView;
    private EditText mDireccionView;
    private DatePickerDialog dpd;
    private View mProgressView;
    private View mLoginFormView;
    private Calendar c;
    public Socket socket=null;
    public Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mNombreView=(EditText) findViewById(R.id.nombre);
        mFechaNacView=(EditText)findViewById(R.id.edad);
        mDireccionView=(EditText)findViewById(R.id.direccion);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_register_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
        TextView textLogin=(TextView)findViewById(R.id.login_link);
        textLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });
        mFechaNacView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                c=Calendar.getInstance();
                final int day=c.get(Calendar.DAY_OF_MONTH);
                int month=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR);
                dpd=new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mFechaNacView.setText(day+"/"+month+"/"+year);
                    }
                },day,month,year);
                dpd.show();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reseteamos los errores.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Guardamos los valores de los edit text
        String nombre=mNombreView.getText().toString();
        String edad=mFechaNacView.getText().toString();
        String direccion=mDireccionView.getText().toString();
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
            mAuthTask = new UserLoginTask(nombre,email,direccion,edad,password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //Lo dejamos asi por ahora
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //Lo dejamos asi por ahora
        return password.length() > 4;
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
                new ArrayAdapter<>(RegisterActivity.this,
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
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mNombre;
        private final String mDireccion;
        private final String mEdad;
        private String respuesta;
        private BufferedReader in;
        private PrintWriter out;
        public boolean terminado;

        UserLoginTask(String nombre,String email,String direccion,String edad,String password) {
            mNombre=nombre;
            mDireccion=direccion;
            mEdad=edad;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {


            try {
                socket=new Socket(Constants.IP_SERVER,Constants.PORT);
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out=new PrintWriter(socket.getOutputStream());
                out.write(Constants.REGISTER_CHARACTERS_SEND +Constants.SEPARATOR+mNombre+Constants.SEPARATOR+mEmail+Constants.SEPARATOR+mDireccion+Constants.SEPARATOR+mEdad+Constants.SEPARATOR+mPassword);
                Thread.sleep(2000);
                respuesta=in.readLine();
                Log.e("xd",respuesta);
            } catch (InterruptedException e) {
                return false;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(respuesta.split("\\|")[0].equals("RC")) {
                try {
                    out.write(Constants.LOGIN_CHARACTERS_SEND +Constants.SEPARATOR+mEmail+Constants.SEPARATOR+mPassword);
                    respuesta=in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(respuesta.split("\\|")[0].equals("LC")) {
                    usuario=new Usuario(Integer.parseInt(respuesta.split("\\|")[1]),respuesta.split("\\|")[2],mEmail,respuesta.split("\\|")[3]);
                    try {
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }else
                    return false;
            }else
                return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);

            if (success) {
                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                intent.putExtra("Usuario", (Serializable) usuario);
                finish();
                startActivity(intent);
            } else {
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
}

