package com.firebase.rentme.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.EditText;

import com.firebase.rentme.R;
import com.firebase.rentme.models.Property;
import com.firebase.rentme.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EditPersonalInfo extends AppCompatActivity
{
    private FirebaseUser user;
    private User userInstance;
    private DocumentReference userDocRef;

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;

    private String name;
    private String email;
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);

        user = FirebaseAuth.getInstance().getCurrentUser();

        String userID = user.getUid();
        userDocRef = FirebaseFirestore.getInstance().collection("users").document(userID);

        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                userInstance = documentSnapshot.toObject(User.class);

                if (user != null)
                {
                    name = userInstance.getOwnerName();
                    email = userInstance.getOwnerEmail();
                    phoneNum = userInstance.getOwnerPhoneNum();
                    initEditTexts();
                }
            }
        });
    }

    private void initEditTexts()
    {
        nameEditText = findViewById(R.id.edit_text_name);
        emailEditText = findViewById(R.id.edit_text_email);
        emailEditText.setText(email);
        nameEditText.setText(name);
        phoneEditText = findViewById(R.id.edit_text_phone);
        phoneEditText.setText(phoneNum);
        phoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    public void updateUserInfo(View view)
    {
        boolean changeName = false;
        boolean changeEmail = false;
        boolean changePhone = false;

        if(!nameEditText.getText().toString().equals(name))
        {
            changeName = true;
            userDocRef.update("ownerName", nameEditText.getText().toString());
        }

        if(!emailEditText.getText().toString().equals(email))
        {
            changeEmail = true;
            userDocRef.update("ownerEmail", emailEditText.getText().toString());
        }

        if(!phoneEditText.getText().toString().equals(phoneNum))
        {
            changePhone = true;
            userDocRef.update("ownerPhoneNum", phoneEditText.getText().toString());
        }

//        if(changeName || changeEmail || changePhone)
//        {
//            ArrayList<String> targets = userInstance.getOwnerProperties();
//
//            while(targets.size() > 0)
//            {
//                FirebaseFirestore.getInstance().collection("properties").document(targets.remove(0)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot)
//                    {
//                        Property property = documentSnapshot.toObject(Property.class);
//                        if(property != null)
//                        {
//
//                        }
//                    }
//                });
//            }
//        }

        finish();
    }

    public void exit(View view)
    {
        finish();
    }
}
