package com.firebase.rentme.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.rentme.MainActivity;
import com.firebase.rentme.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity
{

    FirebaseAuth fAuth;
    String userId;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        final EditText input_OldPassword = findViewById(R.id.input_OldPassword);
        final EditText input_password = findViewById(R.id.input_password);
        final EditText input_reEnterPassword = findViewById(R.id.input_reEnterPassword);
        Button SaveChangesButton = findViewById(R.id.SaveChangesButton);

        input_OldPassword.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                input_OldPassword.setBackgroundResource(R.drawable.multi_state_background);
                input_OldPassword.setError(null);
            }
        });

        input_reEnterPassword.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(input_password.getText().toString().equals(input_reEnterPassword.getText().toString()))
                {
                    input_reEnterPassword.setBackgroundResource(R.drawable.success_background);
                    input_reEnterPassword.setError(null);
                    input_password.setBackgroundResource(R.drawable.success_background);
                    input_password.setError(null);
                }
                else
                {
                    input_reEnterPassword.setBackgroundResource(R.drawable.multi_state_background);
                    input_reEnterPassword.setError(null);
                    input_password.setBackgroundResource(R.drawable.multi_state_background);
                    input_password.setError(null);
                }
            }
        });


        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        SaveChangesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String oldPassword = input_OldPassword.getText().toString();
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

                if(!input_password.getText().toString().equals(input_reEnterPassword.getText().toString()))
                {
                    input_reEnterPassword.setError("Passwords do not match.");
                    input_reEnterPassword.setBackgroundResource(R.drawable.error_background);
                }

                user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Log.d("ChangePassword", "User re-authenticated.");
                        String newPassword = input_password.getText().toString();
                        String confirmNewPassword = input_reEnterPassword.getText().toString();

                        if (newPassword.equals(confirmNewPassword))
                        {
                            user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    Toast.makeText(ChangePassword.this, "Password Reset Successfully.", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Toast.makeText(ChangePassword.this, "Password Reset Failed.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        input_OldPassword.setBackgroundResource(R.drawable.error_background);
                        input_OldPassword.setError("Invalid Password");
                    }
                });
            }
        });
    }

    public void exitActivity(View view)
    {
        finish();
    }
}