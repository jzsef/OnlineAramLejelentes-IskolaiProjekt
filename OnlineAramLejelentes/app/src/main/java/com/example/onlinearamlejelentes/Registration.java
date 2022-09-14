package com.example.onlinearamlejelentes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Registration extends AppCompatActivity {

    private static final String LOG_TAG=Registration.class.getName();
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;




    EditText vezeteknevET;
    EditText keresztnevET;
    EditText cimET;
    EditText merooraET;
    EditText emailET;
    EditText jelszoET;
    EditText jelszoujraET;

    FelhasznaloiAdat felhadat;
    MeroOra mMeroOra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        int secret_key=getIntent().getIntExtra("SECRET_KEY", 0);
        if (secret_key!=123){
            finish();
        }
        vezeteknevET=findViewById(R.id.vezeteknev);
        keresztnevET=findViewById(R.id.keresztnev);
        cimET=findViewById(R.id.cim);
        merooraET=findViewById(R.id.merooragysz);
        emailET=findViewById(R.id.email);
        jelszoET=findViewById(R.id.jelszo);
        jelszoujraET=findViewById(R.id.jelszoujra);

        mAuth=FirebaseAuth.getInstance();



        mFirestore=FirebaseFirestore.getInstance();







    }

    public void registration(View view) {
        String vezeteknev=vezeteknevET.getText().toString();
        String keresztnev=keresztnevET.getText().toString();
        String cim=cimET.getText().toString();
        String meroora=merooraET.getText().toString();
        String email=emailET.getText().toString();
        String jelszo=jelszoET.getText().toString();
        String jelszoujra=jelszoujraET.getText().toString();

        if (vezeteknevET.getText().toString().length()<1||keresztnevET.getText().toString().length()<1
                ||cimET.getText().toString().length()<1||merooraET.getText().toString().length()<1
                ||emailET.getText().toString().length()<1||jelszoET.getText().toString().length()<1
                ||jelszoujraET.getText().toString().length()<1) {
            Toast.makeText(this, "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
        }
        else {

            if (!jelszo.equals(jelszoujra)) {
                Toast.makeText(this, "A két jelszó nem egyezik meg!", Toast.LENGTH_SHORT).show();
            }

            felhadat = new FelhasznaloiAdat(email, vezeteknev, keresztnev, cim);
            mMeroOra = new MeroOra(email, meroora, 0);


            mAuth.createUserWithEmailAndPassword(email, jelszo).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        mFirestore.collection("UserData").add(felhadat)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("Sikeresen hozzáadva: ", documentReference.getId());

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Hiba: ", e.getMessage());
                                Toast.makeText(Registration.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        mFirestore.collection("MeroOra").add(mMeroOra)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("Sikeresen hozzáadva: ", documentReference.getId());
                                        finish();
                                    }
                                });
                    /*Log.d(LOG_TAG,"Sikeres regisztráció");
                    finish();*/
                    } else {
                        Log.d(LOG_TAG, "Sikertelen regisztráció");
                        Toast.makeText(Registration.this, "Sikertelen regisztráció: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            Log.i(LOG_TAG, "Regisztrált: " + " vezeteknev: " + vezeteknev + " keresztnev: " + keresztnev + " cim: " + cim + " meroora: " + meroora + " email: " + email + " jelszó: " + jelszo + " jelszoujra: " + jelszoujra);

        }

    }


    public void cancel(View view) {
        finish();
    }


}