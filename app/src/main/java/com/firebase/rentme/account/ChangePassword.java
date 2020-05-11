package com.firebase.rentme.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class ChangePassword extends AppCompatActivity {

    FirebaseAuth fAuth;
    String userId;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        TextView accountSettingsSectionTitle = findViewById(R.id.accountSettingsSectionTitle);
        final TextView EnterOldPassword = findViewById(R.id.EnterOldPassword);
        TextView EnterNewPassword = findViewById(R.id.EnterNewPassword);
        TextView ConfirmNewPassword = findViewById(R.id.ConfirmNewPassword);

        final EditText input_OldPassword = findViewById(R.id.input_OldPassword);
        final EditText input_password = findViewById(R.id.input_password);
        final EditText input_reEnterPassword = findViewById(R.id.input_reEnterPassword);
        Button SaveChangesButton = findViewById(R.id.SaveChangesButton);

        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        SaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = input_OldPassword.getText().toString();
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("ChangePassword", "User re-authenticated.");
                        String newPassword = input_password.getText().toString();
                        String confirmNewPassword = input_reEnterPassword.getText().toString();

                        if (newPassword.equals(confirmNewPassword))
                        {
                            user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ChangePassword.this, "Password Reset Successfully.", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChangePassword.this, "Password Reset Failed.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
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
