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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Adataim extends AppCompatActivity {

    EditText oldpwd;
    EditText newpwd;
    EditText newpwdagain;

    EditText merooragysz;

    private MeroOra meroOra;
    private CollectionReference collectionReference;
    private String email;
    private ArrayAdapter<String> adapter;
    private FirebaseFirestore mFirestore;

    private static final String LOG_TAG=Adataim.class.getName();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adataim);

        int secret_key=getIntent().getIntExtra("SECRET_KEY", 0);
        if (secret_key!=123){
            finish();
        }


        oldpwd=findViewById(R.id.regijelszo);
        newpwd=findViewById(R.id.jelszo);
        newpwdagain=findViewById(R.id.jelszoujra);
        merooragysz=findViewById(R.id.ujMeroOraGySz);
        email=user.getEmail();
        mFirestore = FirebaseFirestore.getInstance();
        collectionReference=mFirestore.collection("MeroOra");
        queryData();






    }

    public void changepwd(View view) {
        
       String oldpwdStr=oldpwd.getText().toString();
       String newpwdStr=newpwd.getText().toString();
       String newpwdagainStr=newpwdagain.getText().toString();
       
        


        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldpwdStr);


        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (newpwdStr.equals(newpwdagainStr)) {
                                user.updatePassword(newpwdStr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(LOG_TAG, "Jelstó frissült");
                                            Toast.makeText(Adataim.this, "Jelszó frissült", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Log.d(LOG_TAG, "Jelszó nem frissült");
                                            Toast.makeText(Adataim.this, "Jelszó nem frissült", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(Adataim.this, "A két jelszó nem egyezik meg", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(LOG_TAG, "Hibás jelszó");
                            Toast.makeText(Adataim.this, "Hibás jelszó", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void queryData() {
        List<String> mMeroOraGySz = new ArrayList<String>();



        collectionReference.whereEqualTo("email",email).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Log.d("documentid: ",document.getId());
                meroOra = document.toObject(MeroOra.class);
                mMeroOraGySz.add(meroOra.getMeroOraGyariSzama());
            }




            ListView listView = (ListView) findViewById(R.id.list);


            adapter = new ArrayAdapter<String>(this, R.layout.szamlak, R.id.szamla, mMeroOraGySz);
            listView.setAdapter(adapter);



        });

    }

    public void ujora(View view) {
        String meroora=merooragysz.getText().toString();
        MeroOra mMeroOra=new MeroOra(email,meroora,0);
        mFirestore.collection("MeroOra").add(mMeroOra)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Sikeresen hozzáadva: ", documentReference.getId());
                        Toast.makeText(Adataim.this, "Mérőóra sikeresen hozzáadva!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}