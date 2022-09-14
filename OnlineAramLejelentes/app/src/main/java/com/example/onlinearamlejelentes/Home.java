package com.example.onlinearamlejelentes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Home  extends AppCompatActivity {
    private static final String LOG_TAG=Home.class.getName();
    private static final int SECRET_KEY=123;
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private FelhasznaloiAdat felhadat;
    private String email;
    TextView welcomeTW;
    private ArrayList<FelhasznaloiAdat> mFelh=new ArrayList<>();
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcomeTW=findViewById(R.id.welcome);

        int secret_key=getIntent().getIntExtra("SECRET_KEY", 0);
        if (secret_key!=123){
            finish();
        }


        user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            Log.d(LOG_TAG,"Azonosított felhasználó");
        }
        else{
            Log.d(LOG_TAG,"Azonosítatlan felhasználó");
            finish();
        }

        mFirestore=FirebaseFirestore.getInstance();
        email=user.getEmail();

        collectionReference=mFirestore.collection("UserData");

        queryData();










    }

    public void diktalas(View view) {
        Intent intent=new Intent(this,Diktalas.class);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);

    }

    private void queryData() {
        mFelh.clear();
        collectionReference.whereEqualTo("email",email).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                felhadat = document.toObject(FelhasznaloiAdat.class);
                mFelh.add(felhadat);
            }


            if (mFelh.size() == 0) {
                Log.d("mFelh size: ","0");
                queryData();
            }
            else{
                welcomeTW.setText("Üdvözöllek "+mFelh.get(0).getKernev()+"!");
            }

        });
    }

    public void onExit(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void szamlaim(View view) {
        Intent intent=new Intent(this,Szamlaim.class);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
    }

    public void adataim(View view) {
        Intent intent=new Intent(this,Adataim.class);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
    }
}
