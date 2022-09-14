package com.example.onlinearamlejelentes;

import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Diktalas  extends AppCompatActivity {

    private static final String LOG_TAG=Diktalas.class.getName();
    private FirebaseUser user;
    private FirebaseFirestore mFirestore;
    private MeroOra meroOra;
    private String email;

    Szamla szamla;

    private ArrayList<MeroOra> mMeroOra=new ArrayList<>();
    List<String> azon = new ArrayList<String>();
    private CollectionReference collectionReference;
    Spinner spinner;
    EditText meroallasET;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diktalas);

        int secret_key=getIntent().getIntExtra("SECRET_KEY", 0);
        if (secret_key!=123){
            finish();
        }

        mAuth=FirebaseAuth.getInstance();
        user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            Log.d(LOG_TAG,"Azonosított felhasználó");
        }
        else{
            Log.d(LOG_TAG,"Azonosítatlan felhasználó");
            finish();
        }
        meroallasET=findViewById(R.id.oraallas);
        mFirestore=FirebaseFirestore.getInstance();
        email=user.getEmail();

        collectionReference=mFirestore.collection("MeroOra");

        spinner = findViewById(R.id.oragyariszama);

        queryData();


    }

    public void cancel(View view) {
        finish();
    }

    private void queryData() {
        List<String> mMeroOraGySz = new ArrayList<String>();
        mMeroOra.clear();
        azon.clear();
        collectionReference.whereEqualTo("email",email).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                Log.d("documentid: ",document.getId());
                meroOra = document.toObject(MeroOra.class);
                mMeroOra.add(meroOra);
                mMeroOraGySz.add(meroOra.getMeroOraGyariSzama());
                azon.add(document.getId());
            }


            if (mMeroOra.size() == 0) {
                Log.d("mFelh size: ","0");
                queryData();
            }
            else{
                Log.d("mFelh size: ",String.valueOf(mMeroOra.size()));
                ArrayAdapter<String> addressAdapter = new ArrayAdapter<String>(Diktalas.this, android.R.layout.simple_spinner_item,mMeroOraGySz);
                addressAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(addressAdapter);
            }

        });

    }

    public void diktal(View view) {




        String meroora = spinner.getSelectedItem().toString();
        Log.d("Selected id ",String.valueOf(spinner.getSelectedItemPosition()));
        String azonosito=azon.get(spinner.getSelectedItemPosition());
        String meroallas=meroallasET.getText().toString();
        int meroAllasKWh=Integer.parseInt(meroallas);
        double osszeg=(meroAllasKWh-mMeroOra.get(spinner.getSelectedItemPosition()).getMeroAllasKWh())*36.42;
        szamla=new Szamla(email,mMeroOra.get(spinner.getSelectedItemPosition()).getMeroOraGyariSzama(),osszeg);

        MeroOra diktalandomeroOra=new MeroOra(email,meroora,meroAllasKWh);


        if (mMeroOra.get(spinner.getSelectedItemPosition()).getMeroAllasKWh()<diktalandomeroOra.getMeroAllasKWh()) {


            DocumentReference reference = mFirestore.collection("MeroOra").document(azonosito);

            reference.update("meroAllasKWh", meroAllasKWh).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {


                        mFirestore.collection("Szamlak").add(szamla)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("Sikeresen hozzáadva: ", documentReference.getId());
                                        finish();
                                    }
                                });



                    }
                }
            });
        }
        else{
            Log.d("Hiba: ","A mérőállás értéke túl kicsi.");
            Toast.makeText(this, "A mérőállás értéke túl kicsi. Az előző érték: "+mMeroOra.get(spinner.getSelectedItemPosition()).getMeroAllasKWh()+" kWh", Toast.LENGTH_LONG).show();
        }
        /*mFirestore.collection("MeroOra").(diktalandomeroOra)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Sikeresen hozzáadva: ", documentReference.getId());
                        finish();
                    }
                });*/
    }
}
