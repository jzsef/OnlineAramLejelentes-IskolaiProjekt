package com.example.onlinearamlejelentes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Szamlaim extends AppCompatActivity {

    private static final String LOG_TAG = Szamlaim.class.getName();
    private static final int SECRET_KEY=123;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private ArrayList<Szamla> mSzamla = new ArrayList<>();
    private CollectionReference collectionReference;
    private FirebaseFirestore mFirestore;
    private FirebaseUser user;
    private String email;
    private Szamla szamla;

    List<String> azon = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_szamlaim);


        int secret_key=getIntent().getIntExtra("SECRET_KEY", 0);
        if (secret_key!=123){
            finish();
        }


        mFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.d(LOG_TAG, "Azonosított felhasználó");
        } else {
            Log.d(LOG_TAG, "Azonosítatlan felhasználó");
            finish();
        }

        collectionReference = mFirestore.collection("Szamlak");
        email = user.getEmail();

    }

    @Override
    public void onResume(){
        super.onResume();
        queryData();

    }

    private void queryData() {
        List<String> mMeroOraGySz = new ArrayList<String>();
        arrayList = new ArrayList<String>();
        azon.clear();
        mSzamla.clear();

        collectionReference.whereEqualTo("email", email).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Log.d("documentid: ", document.getId());
                szamla = document.toObject(Szamla.class);
                mSzamla.add(szamla);
                Log.d("azon: ",szamla.getMeroOraGyariSzama());
                Log.d("osszeg: ",String.valueOf(szamla.getOsszeg()));
                String info = szamla.getMeroOraGyariSzama() + "    |    " + String.valueOf(Math.round(szamla.getOsszeg()))+" Ft";
                arrayList.add(info);
                azon.add(document.getId());


            }


            if (mSzamla.size() == 0) {
                Log.d("mFelh size: ", "0");
                queryData();
            } else {
                Log.d("mFelh size: ", String.valueOf(mSzamla.size()));

                ListView listView = (ListView) findViewById(R.id.list);


                adapter = new ArrayAdapter<String>(this, R.layout.szamlak, R.id.szamla, arrayList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent=new Intent(Szamlaim.this,Fizetes.class);
                        intent.putExtra("azon",azon.get(i));
                        intent.putExtra("SECRET_KEY",SECRET_KEY);
                        startActivity(intent);

                        Toast.makeText(Szamlaim.this, "Összeg: "+mSzamla.get(i).getOsszeg()+" Ft", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });
    }
}