package com.sebastian.my_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class infoLugar extends AppCompatActivity {

    private String nombre,telefono,direccion,cuidad;
    private TextView tvNombre, tvTelefono, tvDireccion, tvCuidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_lugar);
        Bundle extras = getIntent().getExtras();
        nombre = extras.getString("nombre");
        telefono = extras.getString("telefono");
        direccion = extras.getString("direccion");
        cuidad = extras.getString("cuidad");
        tvNombre = (TextView) findViewById(R.id.tvNombre);
        tvTelefono = (TextView) findViewById(R.id.tvTelefono);
        tvDireccion = (TextView) findViewById(R.id.tvDireccion);
        tvCuidad = (TextView) findViewById(R.id.tvCuidad);

        tvNombre.setText(nombre);
        tvTelefono.setText(telefono);
        tvDireccion.setText(direccion);
        tvCuidad.setText(cuidad);
    }
}