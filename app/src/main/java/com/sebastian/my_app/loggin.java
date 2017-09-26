package com.sebastian.my_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Array;
import java.util.Arrays;
import java.util.Map;

public class loggin extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    String correoR = "", contrasenaR = "";
    String Correo = "", Contrasena = "";
    String Nombre = "";
    String nuevaContraseña;
    String nombre_perfil, ciudad_perfil, genero_perfil, fecha_perfil, correo_perfil, flag_guardar, direccion_foto;
    SharedPreferences preferencia;
    SharedPreferences.Editor editor;
    String nombre_go = "", correo_go = "", img_url_go = "";
    int flag_recuperar = 0;
    private String nombre_fa = "", correo_face = "", img_url_face = "", genero = "";

    EditText eCorreo, eContrasena;
    // Facebook
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Profile userProfile;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;

    //Google
    private SignInButton signInButton;
    private GoogleApiActivity googleApiActivity;
    private GoogleApiClient googleApiClient;
    //////
    ImageView iconoPassword;
    ImageView iconoCorreo;
    String flag_loggin = "";
    String[] Errores;
    int flag_face = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.sebastian.my_app",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eContrasena = (EditText) findViewById(R.id.eContrasena);
        iconoCorreo = (ImageView) findViewById(R.id.iIcono_correo);
        iconoPassword = (ImageView) findViewById(R.id.iIcono_password);
        eCorreo.setFocusable(true);
        eContrasena.setFocusable(true);

        //Facebook
        loginButton = (LoginButton) findViewById(R.id.login_button);
        //loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        loginButton.setReadPermissions("public_profile", "user_photos", "email", "user_birthday");
        //Google
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (response.getError() != null) {

                        } else {
                            getFacebookItems(object);
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,first_name,picture");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(loggin.this, "Loggin Cancelado", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(loggin.this, "Error en la Conexión", Toast.LENGTH_SHORT).show();
            }
        });


        Resources res = getResources();
        Errores = res.getStringArray(R.array.Errores);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            flag_loggin = extras.getString("flag_loggin");
            flag_guardar = extras.getString("flag_guardar");
            if (flag_loggin.equals("RE")) {
                correoR = extras.getString("correo");
                contrasenaR = extras.getString("contrasena");
            }
            if (flag_guardar.equals("AB") && flag_guardar != null) {
                nombre_perfil = extras.getString("nombre_perfil");
                correo_perfil = extras.getString("correo_perfil");
                fecha_perfil = extras.getString("fecha_perfil");
                genero_perfil = extras.getString("genero_perfil");
                ciudad_perfil = extras.getString("ciudad_perfil");
                direccion_foto = extras.getString("direeccion_foto");
            }

        }

        eContrasena.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    iconoPassword.setImageResource(R.drawable.ic_password_rojo);
                } else {
                    iconoPassword.setImageResource(R.drawable.ic_password);
                }
            }
        });

        eCorreo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    iconoCorreo.setImageResource(R.drawable.ic_email_rojo);
                } else {
                    iconoCorreo.setImageResource(R.drawable.ic_email);
                }
            }
        });


    }


    public void getFacebookItems(JSONObject object) {
        try {
            flag_loggin = "FA";
            nombre_fa = object.getString("first_name");
            genero = object.getString("gender");
            correo_face = object.getString("email");
            img_url_face = object.getJSONObject("picture").getJSONObject("data").getString("url");
            String userID = object.getString("id");

            goMainActivity();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void goMainActivity() {
        Intent intent = new Intent(loggin.this, MainActivity.class);
        if (flag_loggin.equals("FA")) {
            intent.putExtra("flag_loggin", flag_loggin);
            intent.putExtra("correo", correo_face);
            intent.putExtra("genero", genero);
            intent.putExtra("nombre", nombre_fa);
            intent.putExtra("url", img_url_face);
            intent.putExtra("flag_guardar", flag_guardar);
            Log.d("correo", correo_face);
            Log.d("Flag", flag_loggin);

        } else if (flag_loggin.equals("RE")) {
            intent.putExtra("correo", correoR);
            intent.putExtra("contrasena", contrasenaR);
            intent.putExtra("flag_loggin", flag_loggin);
            intent.putExtra("flag_guardar", flag_guardar);

        } else if (flag_loggin.equals("RG")) {
            intent.putExtra("correo", correo_go);
            intent.putExtra("nombre", nombre_go);
            intent.putExtra("url", img_url_go);
            intent.putExtra("flag_loggin", flag_loggin);
            intent.putExtra("flag_guardar", flag_guardar);

        }
        if (flag_guardar.equals("AB")) {
            intent.putExtra("corre_perfil", correo_perfil);
            intent.putExtra("nombre_perfil", nombre_perfil);
            intent.putExtra("genero_perfil", genero_perfil);
            intent.putExtra("ciudad_perfil", ciudad_perfil);
            intent.putExtra("fecha_perfil", fecha_perfil);
            intent.putExtra("flag_guardar", flag_guardar);
            intent.putExtra("direeccion_foto", direccion_foto);

        }
        Log.d("Flag", flag_loggin);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void Datos_perfil_Face(Profile perfil) {
        if (perfil != null) {
            Nombre = perfil.getFirstName();

            flag_face = 1;
        } else {
            flag_face = 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        Datos_perfil_Face(profile);
    }

    public void registrarse(View view) {
        flag_loggin = "RE";
        Intent intent = new Intent(loggin.this, RegistroActivity.class);
        startActivityForResult(intent, 1234);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1234 && resultCode == RESULT_OK) {
            correoR = data.getExtras().getString("correo");
            contrasenaR = data.getExtras().getString("contrasena");
            Log.d("correo", correoR);
            Log.d("contrasenaR", contrasenaR);
        } else if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode == 7890 && resultCode == RESULT_OK) {
            nuevaContraseña = data.getExtras().getString("codigo");
            flag_recuperar = 1;
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void iniciar(View view) {
        Correo = eCorreo.getText().toString();
        Contrasena = eContrasena.getText().toString();
        if (flag_recuperar == 1) {
            contrasenaR = nuevaContraseña;
        }
        if (Contrasena.isEmpty() && flag_face == 0) {
            eContrasena.setError(Errores[3]);
        }
        if (Correo.isEmpty() && flag_face == 0) {
            eCorreo.setError(Errores[3]);
        }
        if (flag_face == 1) {
            flag_loggin = "FA";
            goMainActivity();
            flag_recuperar = 0;
        }
        if (Correo.equals(correoR) && Correo.length() > 0 && flag_face == 0) {
            if (Contrasena.equals(contrasenaR)) {
                goMainActivity();
            } else {
                Toast.makeText(loggin.this, Errores[2], Toast.LENGTH_SHORT).show();
            }
        } else if (Correo.length() > 0 && flag_face == 0) {
            Toast.makeText(loggin.this, Errores[4], Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(loggin.this, "Error Loggin", Toast.LENGTH_SHORT).show();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            flag_loggin = "RG";
            nombre_go = account.getGivenName();
            correo_go = account.getEmail();
            if (account.getPhotoUrl() != null) {
                img_url_go = account.getPhotoUrl().toString();

            } else {
                img_url_go = "vacia";
            }
            goMainActivity();


        } else {
            Toast.makeText(loggin.this, "Login cancelado", Toast.LENGTH_SHORT).show();
        }
    }


    private void signIn() {
        Intent Intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(Intent, 9001);
    }


    public void recuperar_con(View view) {
        String correo_edit = eCorreo.getText().toString();
        if (correo_edit.isEmpty()) {
            Toast.makeText(loggin.this, "Campo de Correo Vacío", Toast.LENGTH_SHORT).show();
        } else {
            if (correoR.isEmpty()) {
                Toast.makeText(loggin.this, "Todavia No Estas Registrado", Toast.LENGTH_SHORT).show();
            } else {
                if (correo_edit.equals(correoR)) {
                    Intent intent = new Intent(loggin.this, RecActivity.class);
                    startActivityForResult(intent, 7890);
                } else {
                    Toast.makeText(loggin.this, "El Usuario No Existe", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}


