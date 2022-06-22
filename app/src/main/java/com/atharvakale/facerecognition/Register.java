package com.atharvakale.facerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity{

    EditText fullName,email,password,phone;
    Button registerBtn,goToLogin;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        fullName = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        phone = findViewById(R.id.registerPhone);
        registerBtn = findViewById(R.id.registerBtn);
        goToLogin = findViewById(R.id.gotoLogin);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

       registerBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               checkField(fullName);
               checkField(email);
               checkField(password);
               checkField(phone);

               if(valid){
                   //start user registration process
                   fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                       @Override
                       public void onSuccess(AuthResult authResult) {

                           FirebaseUser user = fAuth.getCurrentUser();
                           Toast.makeText(Register.this,"Account created successfully",Toast.LENGTH_LONG).show();
                           startActivity(new Intent(getApplicationContext(), Login.class));
                           DocumentReference df = fStore.collection("Users").document(user.getUid());
                           Map<String,Object> userInfo = new HashMap<>();
                           userInfo.put("FullName",fullName.getText().toString());
                           userInfo.put("UserEmail",email.getText().toString());
                           userInfo.put("PhoneNumber",phone.getText().toString());
                           //specify if the user is the admin
                           userInfo.put("isUser", "1");
                           df.set(userInfo);

                           finish();
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toast.makeText(Register.this,"Failed to create account",Toast.LENGTH_LONG).show();

                       }
                   });
               }

           }
       });

       goToLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(),Login.class));
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