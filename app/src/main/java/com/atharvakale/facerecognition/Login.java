package com.atharvakale.facerecognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Login extends AppCompatActivity {
    EditText email,password;
    Button loginBtn,gotoRegister,ecBtn,brBtn;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        gotoRegister = findViewById(R.id.gotoRegister);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        ecBtn = findViewById(R.id.ecBtn);
        brBtn = findViewById(R.id.brBtn);


       ecBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               browser1(v);
           }
       });
        brBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     browser2(v);
            }
        });
        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkField(email);
                checkField(password);

                if(valid){

                     fAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                         @Override
                         public void onSuccess(AuthResult authResult) {
                          Toast.makeText(Login.this,"Loggedin Successfully",Toast.LENGTH_LONG).show();
                          checkUserAccessLevel(authResult.getUser().getUid());
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_LONG).show();

                         }
                     });
                }
            }
        });

    }

    private void browser1(View view){
        Intent browser1Intent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://ec.tut.ac.za/ldapLogin.php?aPAGE=LoginB"));
         startActivity(browser1Intent);
    }
    private void browser2(View view){
        Intent browser2Intent= new Intent(Intent.ACTION_VIEW,Uri.parse("https://login.microsoftonline.com/3df74539-9453-4d03-bb9d-b9102cb9ce9c/saml2?SAMLRequest=jdHPS8MwFAfwu%2bD%2fUHJvkqZNu4V2MPQymJdNPXiRJL5thTapeamof72ZQ%2fTo7f3gCx%2fea9dzPLkdvM6AMdvcdgT1OIRL%2fwy1qQ%2bVNE1hbVUaWOqCG9ksGij5Yilqkj1CwN67jgjKSbZBnGHjMGoX04gLkfM6F8U9bxQvFK%2boKKq6KfkTydaIEGLK3niH8whhD%2bGtt%2fCw23bkFOOEirHxI87Rhxcx0FRQbemnZqljw8R0krPBH3vHzubtuaJpR7L3cXDYkTk45TX2qJweAVW0ar%2b%2b26pEVVPw0Vs%2fkNX1VZa13%2fDwn6D%2bYZPVD1LYw8JYbXKzKEVeGVvnpixkvpRSFhqklo2gEVw6ClIT%2buMp4qQtUOvHX3rLLogEatnfr6y%2bAA%3d%3d&RelayState=%2fd2l%2fhome"));
        startActivity(browser2Intent);
    }

    private void checkUserAccessLevel(String uid) {

        DocumentReference df = fStore.collection("Users").document(uid);
        //extract data from document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                //identify user
                if(documentSnapshot.getString("isAdmin")!=null){
                    //user is admin
                    startActivity(new Intent(getApplicationContext(),Menu.class));
                    finish();
                }
                if(documentSnapshot.getString("isUser")!=null){
                    startActivity(new Intent(getApplicationContext(),ApplicationForm.class));
                    finish();
                }
            }
        });
    }

    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }



}