package com.sebastian.my_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felipe on 25/09/17.
 */

public class ExpandibleListViewAdapter extends BaseExpandableListAdapter {

    String[] groupNames;
    String[][] childNames;
    Context context;
    List<objLugar> lugares;
    int[][] id;

    public ExpandibleListViewAdapter(Context context, String[] g, String[][] c, List<objLugar> lugares, int[][] id){
        this.context = context;
        groupNames = g;
        childNames = c;
        this.lugares = new ArrayList<objLugar>(lugares);
        this.id = id;
    }

    @Override
    public int getGroupCount() {
        return groupNames.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return childNames[i].length;
    }

    @Override
    public Object getGroup(int i) {
        return groupNames[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return childNames[i][i1];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        final int j = i;


        TextView txtView = new TextView(context);
        txtView.setText(groupNames[i]);
        txtView.setPadding(100,0,0,0);
        txtView.setTextColor(Color.BLACK);
        txtView.setTextSize(40);
        if(childNames[j].length==1) {
            txtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String nombre = lugares.get(id[j][0]).nombre;
                    String cuidad = lugares.get(id[j][0]).cuidad;
                    String telefono = lugares.get(id[j][0]).telefono;
                    String direccion = lugares.get(id[j][0]).direccion;
                    Intent intent = new Intent(context, infoLugar.class);
                    intent.putExtra("nombre", nombre);
                    intent.putExtra("cuidad", cuidad);
                    intent.putExtra("telefono", telefono);
                    intent.putExtra("direccion", direccion);
                    context.startActivity(intent);

                }
            });
        }
        return txtView;
    }
    //hasdf

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final int j = i;
        final int k = i1;

        final TextView txtView = new TextView(context);
        txtView.setText(lugares.get(id[i][i1]).direccion);
        txtView.setPadding(120,0,0,0);
        txtView.setTextColor(Color.BLACK);
        txtView.setTextSize(30);
        txtView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String nombre = lugares.get(id[j][k]).nombre;
                String cuidad = lugares.get(id[j][k]).cuidad;
                String telefono = lugares.get(id[j][k]).telefono;
                String direccion = lugares.get(id[j][k]).direccion;
                Intent intent = new Intent(context, infoLugar.class);
                intent.putExtra("nombre", nombre);
                intent.putExtra("cuidad", cuidad);
                intent.putExtra("telefono", telefono);
                intent.putExtra("direccion", direccion);
                context.startActivity(intent);
            }
        });

        return txtView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}