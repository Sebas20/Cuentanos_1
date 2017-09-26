package com.sebastian.my_app;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListaLugares extends AppCompatActivity {
    private String consulta;
    private List<objLugar> lugares;
    private List<String> nombres;
    private ExpandableListView listview;
    //private ImageView img;
    private int posConsulta;
    private String[] padres = new String[50];;
    private String[][] hijos = new String[50][50];
    private int[][] idHijos = new int[50][50];
    private int[] cntHijos = new int[50];
    private int[] idPadres = new int[50];

    public static final String TABLE_NAME = "Cuentenos";
    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_CUIDAD = "cuidad";
    public static final String COLUMN_NAME_NOMBRE = "nombre";
    public static final String COLUMN_NAME_TELEFONO = "telefono";
    public static final String COLUMN_NAME_DIRECCION = "direccion";
    public static final String COLUMN_NAME_TIPO = "tipo";


    /*public class objLugar{
        private String cuidad, nombre, telefono, direccion, tipo;
        public objLugar(String cuidad, String nombre, String telefono, String direccion, String tipo){
            this.cuidad = cuidad;
            this.nombre = nombre;
            this.telefono = telefono;
            this.direccion = direccion;
            this.tipo = tipo;
        }
    }*/

    public boolean buscar(String compare){
        posConsulta = 0;
        for(String nombre:nombres){
            if(compare.equals(nombre)){
                return true;
            }
            posConsulta++;
        }
        return false;
    }

    public class FeedReaderDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Cuentenos.db";

        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        public void onCreate(SQLiteDatabase db) {
            //db.execSQL(query);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            //db.execSQL(query);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public static String[] cleanArray(final String[] v) {
        List<String> list = new ArrayList<String>(Arrays.asList(v));
        list.removeAll(Collections.singleton(null));
        return list.toArray(new String[list.size()]);
    }

    public static String[][] cleanMatriz(final String[][] s) {
        String[][] s1 = new String[s.length][];
        int k = 0;

        for (int i = 0; i < s.length; i++) {
            ArrayList<Object> list = new ArrayList<Object>();
            for (int j = 0; j < s[i].length; j++) {
                if (s[i][j] != null) {
                    list.add(s[i][j]);
                }
            }
            s1[k++] = list.toArray(new String[list.size()]);
        }
        return s1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_lugares);
        Bundle extras = getIntent().getExtras();
        consulta = extras.getString("consulta");
        lugares = new ArrayList<objLugar>();
        nombres = new ArrayList<String>();
        listview = (ExpandableListView) findViewById(R.id.listView);
        /*img = (ImageView) findViewById(R.id.imagen);
        Picasso.with(ListaLugares.this).load("http://i.imgur.com/DvpvklR.png").into(img);*/

        Log.i("consulta",consulta);

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplication());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String[] projection = {
                COLUMN_NAME_ID ,
                COLUMN_NAME_CUIDAD,
                COLUMN_NAME_NOMBRE,
                COLUMN_NAME_TELEFONO,
                COLUMN_NAME_DIRECCION,
                COLUMN_NAME_CUIDAD,
                COLUMN_NAME_TIPO
        };


        String selection = COLUMN_NAME_TIPO + " = ?";
        String[] selectionArgs = {consulta};

        String sortOrder =
                COLUMN_NAME_CUIDAD + " DESC";

        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );


        int i = 0;
        int j = 0;
        while(cursor.moveToNext()) {
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            String cuidad = cursor.getString(cursor.getColumnIndexOrThrow("cuidad"));
            String telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"));
            String direccion = cursor.getString(cursor.getColumnIndexOrThrow("direccion"));
            String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
            lugares.add(new objLugar(cuidad, nombre, telefono, direccion,tipo));
            if(buscar(nombre)){
                hijos[idPadres[posConsulta]][cntHijos[idPadres[posConsulta]]] = nombre;
                idHijos[idPadres[posConsulta]][cntHijos[idPadres[posConsulta]]] = j;
                idPadres[j] = idPadres[posConsulta];
                cntHijos[idPadres[posConsulta]]++;
            }else{
                padres[i] = nombre;
                hijos[i][0] = nombre;
                idHijos[i][0] = j;
                idPadres[j] = i;
                System.out.println(j);
                cntHijos[i] = 1;
                i++;
            }
            nombres.add(nombre);
            j++;
        }
        cursor.close();
        ExpandibleListViewAdapter adapter = new ExpandibleListViewAdapter(ListaLugares.this, cleanArray(padres), cleanMatriz(hijos),lugares,idHijos);
        listview.setAdapter(adapter);
        /*listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nombre = lugares.get(idHijos[i][0]).nombre;
                String cuidad = lugares.get(idHijos[i][0]).cuidad;
                String telefono = lugares.get(idHijos[i][0]).telefono;
                String direccion = lugares.get(idHijos[i][0]).direccion;
                Intent intent = new Intent(ListaLugares.this,infoLugar.class);
                intent.putExtra("nombre",nombre);
                intent.putExtra("cuidad",cuidad);
                intent.putExtra("telefono",telefono);
                intent.putExtra("direccion",direccion);
                startActivity(intent);fd
            }
        });*/
    }


}
