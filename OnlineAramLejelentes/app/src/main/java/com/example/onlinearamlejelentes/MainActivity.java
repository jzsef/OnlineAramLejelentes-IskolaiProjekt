package com.example.onlinearamlejelentes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG=MainActivity.class.getName();
    private static final int SECRET_KEY=123;
    private FirebaseAuth mAuth;

    EditText emailET;
    EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailET=findViewById(R.id.editTextEmail);
        passwordET=findViewById(R.id.editTextPassword);
        mAuth=FirebaseAuth.getInstance();
    }

    public void login(View view) {
        String email=emailET.getText().toString();
        String password=passwordET.getText().toString();

        if (emailET.getText().toString().length()<1||passwordET.getText().toString().length()<1) {
            Toast.makeText(this, "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
        }else {


            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        belep();
                    } else {
                        Log.d(LOG_TAG, "Sikertelen belépés");
                        Toast.makeText(MainActivity.this, "Sikertelen belépés: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }



    }

    public void belep(){
        // Log.i(LOG_TAG, "Bejelentkezett: "+email+" jelszó: "+password);
        Intent intent=new Intent(this,Home.class);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
    }
    public void register(View view) {
        Intent intent=new Intent(this,Registration.class);
        intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
    }
}