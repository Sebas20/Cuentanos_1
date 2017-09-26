package com.sebastian.my_app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    EditText eCorreo, eContrasena, eRep_contrasena;
    String correo = "", contrasena = "", rep_contrasena = "";
    ImageView  iconoCorreo,iconoPassword,iconoRep_password;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eContrasena = (EditText) findViewById(R.id.eContrasena);
        eRep_contrasena = (EditText) findViewById(R.id.eRepetir_contrasena);
        iconoCorreo = (ImageView)findViewById(R.id.iIcono_correo);
        iconoPassword = (ImageView)findViewById(R.id.iIcono_password);
        iconoRep_password = (ImageView)findViewById(R.id.iIcono_password_1);
        eContrasena.setFocusable(true);
        eCorreo.setFocusable(true);
        eRep_contrasena.setFocusable(true);

        eRep_contrasena.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    iconoRep_password.setImageResource(R.drawable.ic_password_rojo);
                }else {
                    iconoRep_password.setImageResource(R.drawable.ic_password);
                }
            }
        });
        eCorreo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    iconoCorreo.setImageResource(R.drawable.ic_email_rojo);
                }else {
                    iconoCorreo.setImageResource(R.drawable.ic_email);
                }
            }
        });

        eContrasena.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    iconoPassword.setImageResource(R.drawable.ic_password_rojo);
                }else {
                    iconoPassword.setImageResource(R.drawable.ic_password);
                }
            }
        });
    }

    public void registrar(View view) {
        flag = 0;
        correo = eCorreo.getText().toString();
        contrasena = eContrasena.getText().toString();
        rep_contrasena = eRep_contrasena.getText().toString();
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if (!pattern.matcher(correo).matches()) {
            eCorreo.setError("Correo Invalido");
            flag = 0;
        } else {
            flag++;
        }
        if (correo.isEmpty()) {
            eCorreo.setError("Ingrese un Correo");
            flag = 0;

        } else {
            flag++;
        }
        if(contrasena.length()<4){
            flag = 0;
            eRep_contrasena.setText("");
            eContrasena.setError("Contraseña Demasiado Corta");
        }else {
            flag ++;
        }
        if (contrasena.equals(rep_contrasena)) {
            flag++;

        } else {
            eRep_contrasena.setError("Contraseña Incorrecta");
            flag = 0;
        }
        if (contrasena.isEmpty()) {
            eContrasena.setError("Ingrese una Contraseña");
            flag = 0;
        }

        if (flag == 4) {
            flag = 0;
            Intent intent = new Intent();
            intent.putExtra("correo", correo);
            intent.putExtra("contrasena", contrasena);
            setResult(RESULT_OK, intent);
            finish();
        }

    }


}
