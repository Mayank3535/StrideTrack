package com.example.stridetrack;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class SignupActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passwordInput;
    private Button signupButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        signupButton = findViewById(R.id.signupButton);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();


                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }


                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, task -> {
                            if (task.isSuccessful()) {

                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {

                                    saveUserNameToFirestore(user.getUid(), name);
                                }
                            } else {

                                Toast.makeText(SignupActivity.this, "SignUp Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void saveUserNameToFirestore(String userId, String name) {

        User user = new User(name);


        DocumentReference documentReference = firestore.collection("users").document(userId);
        documentReference.set(user)
                .addOnSuccessListener(aVoid -> Toast.makeText(SignupActivity.this, "Successfully Signed Up", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(SignupActivity.this, "Failed to save user details", Toast.LENGTH_SHORT).show());
    }


    public static class User {
        String name;

        public User() { }

        public User(String name) {
            this.name = name;
        }
    }
}
