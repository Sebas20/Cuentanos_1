package com.sebastian.my_app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RecActivity extends AppCompatActivity {


    String correo, contraseña;

    public String direccion, aleatorio, codigo;
    public EditText email;
    public Button recuperar;

    private final Properties properties = new Properties();
    private Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec);

        email = (EditText) findViewById(R.id.eEmail);
        recuperar = (Button) findViewById(R.id.bRecuperar);
        ////////////////////////Verificacion internet////////////////////////////////////////////////

        if (!verificaConexion(this)) {
            Toast.makeText(getBaseContext(),
                    "Comprueba tu conexión a Internet ", Toast.LENGTH_SHORT)
                    .show();
            this.finish();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////


        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rnd = new Random();
                aleatorio = rnd.toString();
                codigo = aleatorio.substring(17, 23);

                direccion = email.getText().toString();

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.socketFactory.port", "465");
                properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.port", "465");


                if (!validarEmail(direccion)) {
                    Toast toast1 =
                            Toast.makeText(getApplication(), "email invalido", Toast.LENGTH_SHORT);
                    toast1.show();
                    email.setText("");
                } else {

                    try {
                        session = Session.getDefaultInstance(properties, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication("e1036945597@gmail.com", "3217075592");
                            }
                        });

                        if (session != null) {
                            MimeMessage message = new MimeMessage(session);
                            message.setFrom(new InternetAddress("e1036945597@gmail.com"));
                            message.addRecipient(Message.RecipientType.TO, new InternetAddress(direccion));
                            message.setSubject("Nueva Contraseña App Cuentanos");
                            message.setContent(codigo, "text/html;charset=utf-8");


                            Transport.send(message);
                            Toast toast1 =
                                    Toast.makeText(getApplication(), "mensaje enviado", Toast.LENGTH_SHORT);
                            toast1.show();

                            ////////////////Finalizar actividad///////////////////
                            //Bundle parametros=new Bundle();
                            //parametros.putString("codigo",codigo);

                            Intent intent=new Intent();
                            intent.putExtra("codigo",codigo);//Enviar datos
                            setResult(RESULT_OK,intent);
                            finish();




                        }
                    } catch (Exception me) {


                        //me.printStackTrace();
                        //Aqui se deberia o mostrar un mensaje de error o en lugar
                        //de no hacer nada con la excepcion, lanzarla para que el modulo
                        //superior la capture y avise al usuario con un popup, por ejemplo.
                    }
                }
            }
        });
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    //////////////Comprobacion internet
    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan ñapa
        for (int i = 0; i < 2; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }
}