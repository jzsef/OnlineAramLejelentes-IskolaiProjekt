package com.example.onlinearamlejelentes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Fizetes extends AppCompatActivity {

    TextView fizOsszeg;
    EditText cardnum;
    EditText cardname;
    EditText expDate;
    EditText cvv;

    private FirebaseFirestore mFirestore;
    String azon;
    Szamla szamla;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fizetes);


        int secret_key=getIntent().getIntExtra("SECRET_KEY", 0);
        if (secret_key!=123){
            finish();
        }


        azon=getIntent().getStringExtra("azon");
        mFirestore=FirebaseFirestore.getInstance();

        fizOsszeg=findViewById(R.id.fizetendo);
        cardnum=findViewById(R.id.kartyaszam);
        cardname=findViewById(R.id.cardname);
        expDate=findViewById(R.id.lejaratidatum);
        cvv=findViewById(R.id.cvv);
        queryData();
    }

    public void fizetes(View view) {
        Log.d("cardnum:",cardnum.getText().toString());
        if (cardnum.getText().toString().length()<1||cardname.getText().toString().length()<1||expDate.getText().toString().length()<1||cvv.getText().toString().length()<1) {
            Toast.makeText(this, "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
        }
        else{
            DocumentReference docRef = mFirestore.collection("Szamlak").document(azon);

            docRef.delete();
            finish();
        }



    }

    private void queryData() {


        DocumentReference docRef = mFirestore.collection("Szamlak").document(azon);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        szamla = document.toObject(Szamla.class);
                        fizOsszeg.setText("Fizetendő: " +String.valueOf(szamla.getOsszeg())+" Ft");
                    } else {
                        Log.d("Error: ", "No such document");
                    }
                } else {
                    Log.d("Error: ", "get failed with ", task.getException());
                }
            }
        });




        }


    public void cancel(View view) {
        finish();
    }
}