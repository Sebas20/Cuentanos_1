package com.sebastian.my_app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class PerfilActivity extends AppCompatActivity {

    String correoR, contrasenaR;
    String nombre_perfil, ciudad_perfil, genero_perfil, fecha_perfil, correo_perfil,direccion_foto;
    String flag_loggin;
    String correo = "", nombre = "", correo_fa, nombre_fa, correo_go, nombre_go, genero;
    String img_url_face, img_url_go, foto, ciudad;
    ImageView iFoto, iFecha;
    EditText eCorreo, eNombre, eFecha;
    Spinner sGenero, sCiudad;
    Button bGuardar;
    GoogleApiClient googleApiClient;
    ImageButton bEditar;
    private int mes, dia, year, mdia, mmes, myear;
    String flag_guardar = "NA";
    int flag = 0;
    static final int DATE_ID = 0;
    Uri direccion;
    Calendar C = Calendar.getInstance();
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ArrayList<ItemData> list = new ArrayList<>();

        list.add(new ItemData(" Seleccione", R.drawable.genero));
        list.add(new ItemData(" Femenino", R.drawable.female));
        list.add(new ItemData(" Masculino", R.drawable.male));
        Bundle extras = getIntent().getExtras();
        iFoto = (ImageView) findViewById(R.id.iFoto);
        bGuardar = (Button) findViewById(R.id.bGuardar);
        eCorreo = (EditText) findViewById(R.id.eCorreo);
        sGenero = (Spinner) findViewById(R.id.sGenero);
        bEditar = (ImageButton) findViewById(R.id.bEditar);
        eNombre = (EditText) findViewById(R.id.eNombre);
        eFecha = (EditText) findViewById(R.id.eFecha);
        sCiudad = (Spinner) findViewById(R.id.sCiudad);
        iFecha = (ImageView) findViewById(R.id.iFecha);
        sGenero.setEnabled(false);
        sGenero.setSelection(2,true);
        sCiudad.setEnabled(false);
        iFecha.setEnabled(false);
        mes = C.get(Calendar.MONTH);
        dia = C.get(Calendar.DAY_OF_MONTH);
        year = C.get(Calendar.YEAR);
        if (extras != null) {
            flag_loggin = extras.getString("flag_loggin");
            flag_guardar = extras.getString("flag_guardar");
            if (flag_loggin.equals("FA")) {
                correo = extras.getString("correo");
                nombre = extras.getString("nombre");
                genero = extras.getString("genero");
                foto = extras.getString("url");
                img_url_face = foto;
                correo_fa = correo;
                nombre_fa = nombre;
            } else if (flag_loggin.equals("RE")) {
                correoR = extras.getString("correo");
                contrasenaR = extras.getString("contrasena");
                correo = correoR;
            } else if (flag_loggin.equals("RG")) {
                correo = extras.getString("correo");
                nombre = extras.getString("nombre");
                foto = extras.getString("url");
                correo_go = correo;
                nombre_go = nombre;
                img_url_go = foto;
            }
            if (flag_guardar.equals("AB") && flag_guardar != null) {
                nombre_perfil = extras.getString("nombre_perfil");
                correo_perfil = extras.getString("correo_perfil");
                fecha_perfil = extras.getString("fecha_perfil");
                genero_perfil = extras.getString("genero_perfil");
                ciudad_perfil = extras.getString("ciudad_perfil");
                direccion_foto = extras.getString("direeccion_foto");
                //Log.d("direeccion_foto",direccion_foto);
                if (nombre_perfil != null) {
                    eNombre.setText(nombre_perfil);
                }
                if (correo_perfil != null) {
                    eCorreo.setText(ciudad_perfil);
                }
                if (fecha_perfil != null) {
                    eFecha.setText(fecha_perfil);
                }if(direccion_foto != null){
                    Uri myURI = Uri.parse(direccion_foto);
                    iFoto.setImageURI(myURI);
                }

            }

            mostrar(nombre, correo, flag_loggin, foto);
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(PerfilActivity.this, "Error Loggin", Toast.LENGTH_SHORT).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_layout, R.id.txt, list);
        sGenero.setAdapter(adapter);

        sGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                genero_perfil = Integer.toString(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<CharSequence> adapter_1 = ArrayAdapter.createFromResource(this,
                R.array.ciudades, android.R.layout.simple_spinner_item);

        adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCiudad.setAdapter(adapter_1);
        sCiudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ciudad = adapterView.getItemAtPosition(i).toString();
                ciudad_perfil = Integer.toString(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sGenero.setEnabled(true);
                sCiudad.setEnabled(true);
                if(flag_loggin.equals("FA") || flag_loggin.equals("RG")){
                    eNombre.setEnabled(false);
                }
                else{
                    eNombre.setEnabled(true);
                }
                iFecha.setEnabled(true);
                eCorreo.setEnabled(false);
                flag = 1;
            }
        });

        bGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre_perfil = eNombre.getText().toString();
                ciudad_perfil = eCorreo.getText().toString();
                fecha_perfil = eFecha.getText().toString();
                sGenero.setEnabled(false);
                sCiudad.setEnabled(false);
                iFecha.setEnabled(false);
                eNombre.setEnabled(false);
                eCorreo.setEnabled(false);
                flag = 0;
                flag_guardar = "AB";


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
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
        switch (id) {
            case R.id.mPrincipal:
                intent = new Intent(PerfilActivity.this, MainActivity.class);
                if (flag_loggin.equals("FA")) {
                    intent.putExtra("flag_loggin", flag_loggin);
                    intent.putExtra("correo", correo_fa);
                    intent.putExtra("nombre", nombre_fa);
                    intent.putExtra("url", img_url_face);
                    intent.putExtra("flag_guardar", flag_guardar);
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
                if (flag_guardar.equals("AB") && flag_guardar != null) {
                    intent.putExtra("corre_perfil", correo_perfil);
                    intent.putExtra("nombre_perfil", nombre_perfil);
                    intent.putExtra("genero_perfil", genero_perfil);
                    intent.putExtra("ciudad_perfil", ciudad_perfil);
                    intent.putExtra("fecha_perfil", fecha_perfil);
                    intent.putExtra("flag_guardar", flag_guardar);
                    intent.putExtra("direeccion_foto",direccion_foto);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.mCerrar:
                intent = new Intent(PerfilActivity.this, loggin.class);
                if (flag_loggin.equals("FA")) {
                    flag_guardar = "NA";
                    intent.putExtra("flag_loggin", flag_loggin);
                    intent.putExtra("flag_guardar", flag_guardar);
                    LoginManager.getInstance().logOut();

                } else if (flag_loggin.equals("RE")) {
                    flag_guardar = "NA";
                    intent.putExtra("correo", correoR);
                    intent.putExtra("contrasena", contrasenaR);
                    intent.putExtra("flag_loggin", flag_loggin);
                    intent.putExtra("flag_guardar", flag_guardar);
                } else if (flag_loggin.equals("RG")) {
                    flag_guardar = "NA";
                    intent.putExtra("flag_loggin", flag_loggin);
                    intent.putExtra("flag_guardar", flag_guardar);
                    signOut();
                }
                if (flag_guardar.equals("AB")) {
                    flag_guardar = "NA";
                    intent.putExtra("corre_perfil", correo_perfil);
                    intent.putExtra("nombre_perfil", nombre_perfil);
                    intent.putExtra("genero_perfil", genero_perfil);
                    intent.putExtra("ciudad_perfil", ciudad_perfil);
                    intent.putExtra("fecha_perfil", fecha_perfil);
                    intent.putExtra("flag_guardar", flag_guardar);
                    intent.putExtra("direeccion_foto",direccion_foto);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void colocar_foto(View view) {
        final CharSequence[] opciones = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(PerfilActivity.this);
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i] == "Tomar Foto") {
                    abrir_camara();
                } else if (opciones[i] == "Elegir de Galeria") {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona un App"), 200);

                } else if (opciones[i] == "Cancelar") {
                    dialogInterface.dismiss();
                }

            }
        });
        if(flag == 1 && flag_loggin.equals("RE")){
            builder.show();
        }

    }

    private void abrir_camara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 201);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 201:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap bmp = (Bitmap) extras.get("data");
                    iFoto.setImageBitmap(bmp);
                }
                break;
            case 200:
                if (resultCode == RESULT_OK) {
                    direccion = data.getData();
                    direccion_foto = direccion.toString();
                    iFoto.setImageURI(Uri.parse(direccion_foto));
                }
                break;
        }
    }

    private void mostrar(String nombre, String correo, String flag_loggin, String img_value) {
        if (flag_loggin.equals("FA")) {
            eCorreo.setText(correo);
            eNombre.setText(nombre);
            mostrar_imagen(img_value);

        } else if (flag_loggin.equals("RE")) {
            eCorreo.setText(correo);
        } else if (flag_loggin.equals("RG")) {
            eCorreo.setText(correo);
            eNombre.setText(nombre);
            mostrar_imagen(img_value);

        }
    }


    private void mostrar_imagen(String url) {
        //Picasso.with(this).setIndicatorsEnabled(true);
        Picasso.with(getApplication()).load(url).placeholder(R.drawable.ic_user).error(R.drawable.ic_user).into(iFoto, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Toast.makeText(PerfilActivity.this, "Error Cargando la Imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fecha(View view) {
        showDialog(DATE_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID:
                return new DatePickerDialog(this, mDatePicker, year, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            myear = i;
            mmes = i1;
            mdia = i2;
            colocar_fecha();
        }
    };

    private void colocar_fecha() {
        String fecha = Integer.toString(mdia) + "/" + Integer.toString(mmes + 1) + "/" + Integer.toString(myear);
        eFecha.setText(fecha);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            intent = new Intent(PerfilActivity.this, MainActivity.class);
            if (flag_loggin.equals("FA")) {
                intent.putExtra("flag_loggin", flag_loggin);
                intent.putExtra("correo", correo_fa);
                intent.putExtra("nombre", nombre_fa);
                intent.putExtra("url", img_url_face);
                intent.putExtra("flag_guardar", flag_guardar);
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
                intent.putExtra("direeccion_foto",direccion_foto);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }


}
