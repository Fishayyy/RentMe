package com.firebase.rentme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.rentme.models.Property;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreatePropertyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private static final String TAG = "MainActivity";
    private static final String IMAGE_DIRECTORY = "/demonuts";

    private FirebaseFirestore db;

    private String housingCategory;
    private EditText editTextPrice;
    private EditText editTextBio;
    private EditText editTextAddress;
    private EditText editTextCity;
    private EditText editTextZipCode;
    private String state;
    private EditText editTextOwnerName;
    private EditText editTextOwnerPhoneNum;
    private EditText editTextOwnerEmail;

    private int GALLERY = 1, CAMERA = 2;

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_property);

        requestMultiplePermissions();

        imageButton = findViewById(R.id.addPicButton);

        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(this);

        Spinner stateSpinner = findViewById(R.id.stateSpinner);
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);
        stateSpinner.setOnItemSelectedListener(this);

        editTextPrice = findViewById(R.id.edit_text_price);
        editTextBio = findViewById(R.id.edit_text_bio);
        editTextAddress = findViewById(R.id.edit_text_address);
        editTextCity = findViewById(R.id.edit_text_city);
        editTextZipCode = findViewById(R.id.edit_text_zip);
        editTextOwnerName = findViewById(R.id.edit_text_ownerName);
        editTextOwnerPhoneNum = findViewById(R.id.edit_text_ownerPhoneNum);
        editTextOwnerEmail = findViewById(R.id.edit_text_ownerEmail);

        FirebaseFirestore.setLoggingEnabled(true);

        initFirestore();
    }

    private void initFirestore()
    {
        db = FirebaseFirestore.getInstance();
    }

    public void addProperty(View v)
    {
        if (housingCategory.equals(getString(R.string.selectCategory)))
        {
            Toast.makeText(getApplicationContext(), "Select a Housing Category", Toast.LENGTH_SHORT).show();
        }
        else if (editTextPrice.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Fill in Price", Toast.LENGTH_SHORT).show();
        }
        else if (editTextBio.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Fill in Bio", Toast.LENGTH_SHORT).show();
        }
        else if (editTextAddress.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Fill in Address", Toast.LENGTH_SHORT).show();
        }
        else if (editTextCity.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Fill in City", Toast.LENGTH_SHORT).show();
        }
        else if (editTextZipCode.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Fill in ZIP", Toast.LENGTH_SHORT).show();
        }
        else if (state.equals(getString(R.string.selectState)))
        {
            Toast.makeText(getApplicationContext(), "Select a State", Toast.LENGTH_SHORT).show();
        }
        else if (editTextOwnerName.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Fill in Name", Toast.LENGTH_SHORT).show();
        }
        else if (editTextOwnerPhoneNum.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Fill in Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (editTextOwnerEmail.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Fill in Email", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Button button = findViewById(R.id.add_property_button);

            button.setEnabled(false);
            button.setBackgroundColor(Color.GREEN);
            button.setText(R.string.propertyAdded);
            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkmark, 0);

            Property newProperty = new Property(
                    housingCategory,
                    Double.parseDouble(editTextPrice.getText().toString()),
                    "URL",
                    editTextBio.getText().toString(),
                    editTextAddress.getText().toString(),
                    editTextCity.getText().toString(),
                    editTextZipCode.getText().toString(),
                    state,
                    editTextOwnerName.getText().toString(),
                    PhoneNumberUtils.formatNumber(editTextOwnerPhoneNum.getText().toString(), Locale.getDefault().getCountry()),
                    editTextOwnerEmail.getText().toString()
            );

            // Get a reference to the properties collection
            CollectionReference properties = db.collection("properties");

            properties.add(newProperty).addOnSuccessListener(new OnSuccessListener<DocumentReference>()
            {
                @Override
                public void onSuccess(DocumentReference documentReference)
                {
                    Toast toast = Toast.makeText(CreatePropertyActivity.this, "Created Listing", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                }
            })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast toast = Toast.makeText(CreatePropertyActivity.this, "Failed to Create Listing", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 0);
                            toast.show();
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (parent.getId() == R.id.categorySpinner)
            housingCategory = parent.getSelectedItem().toString();
        if (parent.getId() == R.id.stateSpinner)
            state = parent.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public void showPictureDialog(View v)
    {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Browse Gallery",
                "Take Photo"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        switch (which)
                        {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera()
    {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED)
        {
            return;
        }
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                Uri contentURI = data.getData();
                try
                {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imageButton.setImageBitmap(bitmap);

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }
        else if (requestCode == CAMERA)
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            imageButton.setImageBitmap(thumbnail);
        }
    }

    private void requestMultiplePermissions()
    {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener()
                {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report)
                    {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted())
                        {
                            //Optional Toast Message
                            //Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied())
                        {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
                    {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener()
                {
                    @Override
                    public void onError(DexterError error)
                    {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
}
