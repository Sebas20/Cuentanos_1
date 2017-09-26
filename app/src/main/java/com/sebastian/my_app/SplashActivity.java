package com.sebastian.my_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    String flag_loggin = "";
    String nombre_fa, correo_face, genero, img_url_face;
    String nombre_go, correo_go,img_url_go;
    String nombre_perfil,ciudad_perfil,genero_perfil,fecha_perfil,correo_perfil,flag_guardar="NA",direccion_foto;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView img1 = (ImageView) findViewById(R.id.gif_logo_view);
        img1.setBackgroundResource(R.drawable.gif_logo);
///
        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        ////////

        final AnimationDrawable frameAnimation = (AnimationDrawable) img1.getBackground();
        frameAnimation.start();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                AccessToken token = AccessToken.getCurrentAccessToken();
                OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
                if (opr.isDone()) {
                    GoogleSignInResult result = opr.get();
                    if (result.isSuccess()) {
                        flag_loggin = "RG";
                        handleSignInResult(result);

                    }

                }
                if (token != null) {
                    flag_loggin = "FA";
                    GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            if (response.getError() != null) {

                            } else {
                                correo_face = object.optString("email");
                                nombre_fa = object.optString("first_name");
                                genero = object.optString("gender");
                                try {
                                    img_url_face = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.d("correo", correo_face);
                                goActivity();
                                //getFacebookItems(object);

                            }
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email,gender,first_name,picture");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
                if (flag_loggin != "FA" && flag_loggin != "RG") {
                    goActivity();
                }


            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 3000);

    }


    private void handleSignInResult(GoogleSignInResult result) {
        GoogleSignInAccount account = result.getSignInAccount();
        nombre_go = account.getGivenName();
        correo_go = account.getEmail();
        if(account.getPhotoUrl() != null){
            img_url_go = account.getPhotoUrl().toString();

        }else{
            img_url_go = "vacia";
        }
        goActivity();
    }

    private void goActivity() {
        Intent intent;
        if (flag_loggin.equals("FA")) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("flag_loggin", flag_loggin);
            intent.putExtra("correo", correo_face);
            intent.putExtra("genero", genero);
            intent.putExtra("nombre", nombre_fa);
            intent.putExtra("url", img_url_face);
            intent.putExtra("flag_guardar",flag_guardar);
            if(flag_guardar.equals("AB")){
                intent.putExtra("corre_perfil",correo_perfil);
                intent.putExtra("nombre_perfil",nombre_perfil);
                intent.putExtra("genero_perfil",genero_perfil);
                intent.putExtra("ciudad_perfil",ciudad_perfil);
                intent.putExtra("fecha_perfil",fecha_perfil);
                intent.putExtra("flag_guardar",flag_guardar);
            }
            startActivity(intent);
            finish();

        } else if (flag_loggin.equals("RG")) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("correo", correo_go);
            intent.putExtra("nombre", nombre_go);
            intent.putExtra("url",img_url_go);
            intent.putExtra("flag_loggin", flag_loggin);
            intent.putExtra("flag_guardar",flag_guardar);
            if(flag_guardar.equals("AB")){
                intent.putExtra("corre_perfil",correo_perfil);
                intent.putExtra("nombre_perfil",nombre_perfil);
                intent.putExtra("genero_perfil",genero_perfil);
                intent.putExtra("ciudad_perfil",ciudad_perfil);
                intent.putExtra("fecha_perfil",fecha_perfil);
                intent.putExtra("flag_guardar",flag_guardar);
                intent.putExtra("direeccion_foto",direccion_foto);

            }
            startActivity(intent);
            finish();

        } else if (flag_loggin != "RG" && flag_loggin != "FA") {
            intent = new Intent(SplashActivity.this, loggin.class);
            intent.putExtra("flag_guardar",flag_guardar);
            intent.putExtra("flag_loggin", flag_loggin);
            if(flag_guardar.equals("AB")){
                intent.putExtra("corre_perfil",correo_perfil);
                intent.putExtra("nombre_perfil",nombre_perfil);
                intent.putExtra("genero_perfil",genero_perfil);
                intent.putExtra("ciudad_perfil",ciudad_perfil);
                intent.putExtra("fecha_perfil",fecha_perfil);
                intent.putExtra("flag_guardar",flag_guardar);
                intent.putExtra("direeccion_foto",direccion_foto);

            }
            startActivity(intent);
            finish();
        }



    }

}
