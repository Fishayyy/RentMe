package com.firebase.rentme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Locale;

public class CreateListingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private static final String TAG = "MainActivity";

    private FirebaseFirestore db;
    private StorageReference storageReference;

    private Uri imguri;
    private String photoURL = "";

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

    private final int GALLERY = 1, PHONE_LENGTH = 13;

    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_property);

        initFirestore();

        imageButton = findViewById(R.id.uploadImage);

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
        editTextOwnerPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        editTextOwnerEmail = findViewById(R.id.edit_text_ownerEmail);
    }

    private void initFirestore()
    {
        FirebaseFirestore.setLoggingEnabled(true);

        db = FirebaseFirestore.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference("Images");
    }

    public void validateProperty(View v)
    {
        if (imguri == null)
        {
            Toast.makeText(getApplicationContext(), "Select a Photo", Toast.LENGTH_SHORT).show();
        }
        else if (editTextPrice.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Fill in Price", Toast.LENGTH_SHORT).show();
        }
        else if (housingCategory.equals(getString(R.string.selectCategory)))
        {
            Toast.makeText(getApplicationContext(), "Select a Housing Category", Toast.LENGTH_SHORT).show();
        }
        else if (editTextOwnerName.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Fill in Name", Toast.LENGTH_SHORT).show();
        }
        else if (editTextOwnerPhoneNum.getText().toString().length() != PHONE_LENGTH)
        {
            Toast.makeText(getApplicationContext(), "Invalid Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (!isValidEmail(editTextOwnerEmail.getText().toString().trim()))
        {
            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
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
        else if (editTextBio.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Fill in Bio", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Upload picture to Firebase Storage
            fileUploader();
        }
    }

    public void uploadProperty()
    {
        Button button = findViewById(R.id.add_property_button);

        button.setEnabled(false);
        button.setBackgroundColor(Color.GREEN);
        button.setText(R.string.propertyAdded);
        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkmark, 0);

        Property newProperty = new Property(
                housingCategory,
                Double.parseDouble(editTextPrice.getText().toString()),
                photoURL,
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
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast toast = Toast.makeText(CreateListingActivity.this, "Failed to Create Listing", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public static boolean isValidEmail(String emailInput)
    {
        return !emailInput.equals("") && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches();
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

    public void choosePhotoFromGallery(View v)
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED)
        {
            return;
        }
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                imguri = data.getData();
                imageButton.setImageURI(imguri);
            }
        }
    }

    //Get the file extension so we can store the file with extension
    private String getExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void fileUploader()
    {
        //giving our file a name with time and extension to avoid redundancy and duplication
        StorageReference Ref = storageReference.child(System.currentTimeMillis() + "." + getExtension(imguri));

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        Ref.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                // Get a URL to the uploaded content
                Task uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri downloadUrl = (Uri) uriTask.getResult();
                photoURL = downloadUrl.toString();

                uploadProperty();

                pd.dismiss();
            }
        })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception exception)
                    {
                        // Handle unsuccessful uploads
                        Toast.makeText(CreateListingActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
