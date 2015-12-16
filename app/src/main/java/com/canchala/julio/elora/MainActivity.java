package com.canchala.julio.elora;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Query;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {

    private Firebase mRef;
    private TextView textView;
    private Button buttonorte;
    private Button buttonsur;
    private Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView) findViewById(R.id.textView);
        buttonorte=(Button)findViewById(R.id.buttonnorte);
        buttonsur=(Button)findViewById(R.id.buttonsur);
        delete=(Button)findViewById(R.id.buttondelete);


        Firebase.setAndroidContext(this);


        //Mi base de Datos
        mRef=new Firebase("https://elora.firebaseio.com/");

        //Muestra Tiempo Real
        final Firebase ejemplo1 = mRef.child("Condición");

        ejemplo1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = (String) dataSnapshot.getValue();
                textView.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        buttonorte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejemplo1.setValue("Norte");
            }
        });

        buttonsur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ejemplo1.setValue("Sur");

            }
        });

        //Guardar Informacion Metodo1
        String nom="Carlos Lopez";
        Firebase ejemplo2 = mRef.child("Usuarios").child(nom);
        User Carlos = new User(nom,23,1256664895);
        ejemplo2.setValue(Carlos);

        //Guardar Informacion Metodo2
        final Firebase ejemplo3 = mRef.child("Usuarios");
        nom="Juan Sanchez";
        ejemplo3.child(nom).child("nombre").setValue(nom);
        ejemplo3.child(nom).child("edad").setValue(35);
        ejemplo3.child(nom).child("cedula").setValue(1659885694);

        //Eliminar informacion
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ejemplo3.child("Carlos Lopez").removeValue();
            }
        });

        //Guardar Informacion Metodo3
        nom="Jhon Salazar";
        Firebase ejemplo4 = mRef.child("Usuarios");
        Map<String, Object> nuevousuario = new HashMap<String, Object>();
        nuevousuario.put("nombre",nom);
        nuevousuario.put("edad", "23");
        nuevousuario.put("cedula", "1256669874");
        Map<String, Object> usuarios = new HashMap<String, Object>();
        usuarios.put(nom, nuevousuario);
        ejemplo4.updateChildren(usuarios, new Firebase.CompletionListener() { //Comprobacion del guardado
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {

                    Toast.makeText(MainActivity.this, "Los datos no se pudieron guardar" + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*

        ////Guardar Informacion Metodo4-Identificador
        Firebase ejemplo5 = mRef.child("Libros");
        Map<String, String> libro1 = new HashMap<String, String>();
        libro1.put("Autor", "Carlo Salvador");
        libro1.put("Titulo", "Programacion Android");
        ejemplo5.push().setValue(libro1);
        Map<String, String> libro2 = new HashMap<String, String>();
        libro2.put("Autor", "Andrea Pirlo");
        libro2.put("Titulo", "Programacion Java");
        ejemplo5.push().setValue(libro2);


        //Guardar Informacion Metodo5-Informacion Simultanea
        Firebase ejemplo6 = mRef.child("Contador");
        ejemplo6.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                if (currentData.getValue() == null) {
                    currentData.setValue(1);
                } else {
                    currentData.setValue((Long) currentData.getValue() + 1);
                }
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean committed, DataSnapshot currentData) {
            }
        });

        //Lectura de informacion Metodo1
        Firebase ejemplo7 = mRef.child("Usuarios").child("Carlos Lopez").child("nombre");
        ejemplo7.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Toast.makeText(MainActivity.this, snapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(MainActivity.this, "Lectura fallida: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Lectura de informacion Metodo2 - Recorremos todos los usuarios
        Firebase ejemplo8 = mRef.child("Usuarios");

        ejemplo8.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Toast.makeText(MainActivity.this,"Hay "+snapshot.getChildrenCount()+" Usuarios", Toast.LENGTH_SHORT).show();

                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    User2 user2= postSnapshot.getValue(User2.class);
                    Toast.makeText(MainActivity.this,user2.getnombre(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(MainActivity.this,"Lectura fallida", Toast.LENGTH_SHORT).show();
            }
        });
        */

        //Lectura, cambio y eliminacion de informacion - Se activa para cada usuario
        Firebase ejemplo9 = mRef.child("Usuarios");
        Query query=ejemplo9.orderByChild("edad");//Consulta ordenada por la edad

        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Clientes clientes=dataSnapshot.getValue(Clientes.class);
                Toast.makeText(MainActivity.this,clientes.getnombre()+" Fue agregado", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Clientes clientes = dataSnapshot.getValue(Clientes.class);
                Toast.makeText(MainActivity.this,clientes.getnombre()+" Cambio", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String nombre = (String) dataSnapshot.child("nombre").getValue();
                Toast.makeText(MainActivity.this,"El cliente "+nombre+ " fue borrado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}
