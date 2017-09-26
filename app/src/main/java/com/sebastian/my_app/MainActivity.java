package com.sebastian.my_app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String correoR, contrasenaR;
    String correo_go,correo_fa,nombre_go,nombre_fa,genero;
    String nombre_perfil,ciudad_perfil,genero_perfil,fecha_perfil,correo_perfil,flag_guardar,direccion_foto;
    String flag_loggin;
    String img_url_face,img_url_go;
    GoogleApiClient googleApiClient;
    //////
    private LinearLayout llRestaurantes, llHospitales, llBarberias, llBares, llUniversidades, llVeterinarias, llCompras, llCines, llHoteles;
    private ListView listview;
    private String consulta = "";
    //////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
       Bundle extras = getIntent().getExtras();
        if(extras != null){
            flag_loggin = extras.getString("flag_loggin");
            flag_guardar = extras.getString("flag_guardar");
            if(flag_loggin.equals("FA")){
                correo_fa = extras.getString("correo");
                nombre_fa = extras.getString("nombre");
                genero = extras.getString("gebero");
                img_url_face = extras.getString("url");
            }else if(flag_loggin.equals("RE")){
                correoR = extras.getString("correo");
                contrasenaR = extras.getString("contrasena");
            }
            else if(flag_loggin.equals("RG")){
                correo_go = extras.getString("correo");
                nombre_go = extras.getString("nombre");
                img_url_go = extras.getString("url");
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
        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(MainActivity.this, "Error Loggin", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        ////////////////////////////////
        llRestaurantes = (LinearLayout) findViewById(R.id.llRestaurante);
        llHospitales = (LinearLayout) findViewById(R.id.llHospitales);
        llBarberias = (LinearLayout) findViewById(R.id.llBarberias);
        llBares = (LinearLayout) findViewById(R.id.llBares);
        llVeterinarias = (LinearLayout) findViewById(R.id.llVeterinarias);
        llUniversidades = (LinearLayout)findViewById(R.id.llUniversidades);
        llCompras = (LinearLayout) findViewById(R.id.llCompras);
        llCines = (LinearLayout) findViewById(R.id.llCine);
        llHoteles = (LinearLayout) findViewById(R.id.llHoteles);
        ///////////////////////////////

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.mMiperfil:
                intent = new Intent(MainActivity.this,PerfilActivity.class);
                if(flag_loggin.equals("FA")){
                    intent.putExtra("flag_loggin",flag_loggin);
                    intent.putExtra("correo", correo_fa);
                    intent.putExtra("nombre",nombre_fa);
                    intent.putExtra("genero",genero);
                    intent.putExtra("url",img_url_face);
                    intent.putExtra("flag_guardar",flag_guardar);
                }else if(flag_loggin.equals("RE")){
                    intent.putExtra("correo", correoR);
                    intent.putExtra("contrasena", contrasenaR);
                    intent.putExtra("flag_loggin",flag_loggin);
                    intent.putExtra("flag_guardar",flag_guardar);
                }else if(flag_loggin.equals("RG")){
                    intent.putExtra("correo",correo_go);
                    intent.putExtra("nombre",nombre_go);
                    intent.putExtra("url",img_url_go);
                    intent.putExtra("flag_loggin",flag_loggin);
                    intent.putExtra("flag_guardar",flag_guardar);
                }
                if(flag_guardar.equals("AB")){
                    intent.putExtra("corre_perfil",correo_perfil);
                    intent.putExtra("nombre_perfil",nombre_perfil);
                    intent.putExtra("genero_perfil",genero_perfil);
                    intent.putExtra("ciudad_perfil",ciudad_perfil);
                    intent.putExtra("fecha_perfil",fecha_perfil);
                    intent.putExtra("direeccion_foto",direccion_foto);
                    intent.putExtra("flag_guardar",flag_guardar);
                }
                Log.d("Flag",flag_loggin);
                startActivity(intent);
                break;
            case R.id.mCerrar:
                intent = new Intent(MainActivity.this,loggin.class);
                if(flag_loggin.equals("FA")){
                    flag_guardar = "NA";
                    intent.putExtra("flag_loggin",flag_loggin);
                    intent.putExtra("flag_guardar",flag_guardar);
                    LoginManager.getInstance().logOut();

                }else if(flag_loggin.equals("RE")) {
                    flag_guardar = "NA";
                    intent.putExtra("correo", correoR);
                    intent.putExtra("contrasena", contrasenaR);
                    intent.putExtra("flag_loggin", flag_loggin);
                    intent.putExtra("flag_guardar",flag_guardar);
                }else if(flag_loggin.equals("RG")) {
                    flag_guardar = "NA";
                    intent.putExtra("flag_loggin", flag_loggin);
                    intent.putExtra("flag_guardar",flag_guardar);
                    signOut();
                }
                if(flag_guardar.equals("AB")){
                    flag_guardar = "NA";
                    intent.putExtra("corre_perfil",correo_perfil);
                    intent.putExtra("nombre_perfil",nombre_perfil);
                    intent.putExtra("genero_perfil",genero_perfil);
                    intent.putExtra("ciudad_perfil",ciudad_perfil);
                    intent.putExtra("fecha_perfil",fecha_perfil);
                    intent.putExtra("flag_guardar",flag_guardar);
                    intent.putExtra("direeccion_foto",direccion_foto);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////
    public void listarLugares(View view) {
        int id = view.getId();

        switch(id){
            case R.id.llRestaurante:
                consulta = "restaurantes";
                break;
            case R.id.llHospitales:
                consulta = "hospitales";
                break;
            case R.id.llBarberias:
                consulta = "barberias";
                break;
            case R.id.llBares:
                consulta = "bares";
                break;
            case R.id.llVeterinarias:
                consulta = "veterinarias";
                break;
            case R.id.llUniversidades:
                consulta = "universidades";
                break;
            case R.id.llCompras:
                consulta = "compras";
                break;
            case R.id.llCine:
                consulta = "cines";
                break;
            case R.id.llHoteles:
                consulta = "hoteles";
                break;
        }

        Intent intent = new Intent(MainActivity.this,ListaLugares.class);
        intent.putExtra("consulta",consulta);
        startActivity(intent);
    }
    //////////////////////////////////////////////////////7
}
