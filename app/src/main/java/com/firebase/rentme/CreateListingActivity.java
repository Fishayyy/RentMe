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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
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

public class CreateListingActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private final static int GALLERY = 1, ZIP_CODE_LENGTH = 5, PHONE_NUMBER_LENGTH = 13;

    private FirebaseFirestore database;
    private StorageReference storageReference;

    private Property newProperty;

    private Uri imguri;
    private String photoURL = "";

    private ImageButton imageButton;
    private Spinner categorySpinner;
    private EditText editTextPrice;
    private EditText editTextBio;
    private EditText editTextAddress;
    private EditText editTextCity;
    private EditText editTextZipCode;
    private Spinner stateSpinner;
    private EditText editTextOwnerName;
    private EditText editTextOwnerPhoneNum;
    private EditText editTextOwnerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_property);
        initFirestore();
        initImageUploadInterface();
        initSpinners();
        initEditTextFields();
    }

    private void initFirestore()
    {
        FirebaseFirestore.setLoggingEnabled(true);
        database = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Images");
    }

    private void initImageUploadInterface()
    {
        imageButton = findViewById(R.id.uploadImageButton);
    }

    private void initSpinners()
    {
        categorySpinner = findViewById(R.id.categorySpinner);
        stateSpinner = findViewById(R.id.stateSpinner);
        populateSpinner(categorySpinner, R.array.categories);
        populateSpinner(stateSpinner, R.array.states);
    }

    private void populateSpinner(Spinner spinner, int stringArrayResource)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, stringArrayResource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initEditTextFields()
    {
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

    public void validatePropertyInformation(View v)
    {
        boolean isValid = true;
        boolean scrollToTop = false;

        if (imguri == null)
        {
            isValid = false;
            scrollToTop = true;
            imageButton.setImageResource(R.drawable.error_missing_photo);
            imageButton.setBackgroundResource(R.drawable.error_background);
        }

        if (editTextPrice.getText().toString().isEmpty())
        {
            isValid = false;
            scrollToTop = true;
            displayError(editTextPrice, "Must declare a price");
        }

        if (categorySpinner.getSelectedItemPosition() == 0)
        {
            isValid = false;
            scrollToTop = true;
            displayError(categorySpinner, "Must select a category");
        }

        if (editTextOwnerName.getText().toString().isEmpty())
        {
            isValid = false;
            scrollToTop = true;
            displayError(editTextOwnerName, "Name is required");
        }

        if (editTextOwnerPhoneNum.getText().toString().length() != PHONE_NUMBER_LENGTH)
        {
            isValid = false;
            scrollToTop = true;
            String errorMessage = (editTextOwnerPhoneNum.getText().toString().length() == 0) ? "Phone number is required" : "Invalid Phone Number";
            displayError(editTextOwnerPhoneNum, errorMessage);
        }

        if (!isValidEmail(editTextOwnerEmail.getText().toString().trim()))
        {
            isValid = false;
            scrollToTop = true;
            String errorMessage = (editTextOwnerEmail.getText().toString().length() == 0) ? "Email is required" : "Invalid Email";
            displayError(editTextOwnerEmail, errorMessage);
        }

        if (editTextAddress.getText().toString().isEmpty())
        {
            isValid = false;
            displayError(editTextAddress, "Address is required");
        }

        if (editTextCity.getText().toString().isEmpty())
        {
            isValid = false;
            displayError(editTextCity, "City is required");
        }

        if (editTextZipCode.getText().toString().length() != ZIP_CODE_LENGTH)
        {
            isValid = false;
            String errorMessage = (editTextZipCode.getText().toString().length() == 0) ? "ZIP is required" : "Invalid ZIP";
            displayError(editTextZipCode, errorMessage);
        }

        if (stateSpinner.getSelectedItemPosition() == 0)
        {
            isValid = false;
            displayError(stateSpinner, "Must select a state");
        }

        if(scrollToTop)
        {
            scrollToTop();
        }

        if(isValid)
        {
            uploadImage();
        }
    }

    public void uploadProperty()
    {
        initializeNewProperty();

        database.collection("properties")
                .add(newProperty)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
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
                        Toast.makeText(CreateListingActivity.this, "Failed to Create Listing", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void initializeNewProperty()
    {
        newProperty = new Property();
        newProperty.setHousingCategory(categorySpinner.getSelectedItem().toString());
        newProperty.setPrice(Double.parseDouble(editTextPrice.getText().toString()));
        newProperty.setPhotoURL(photoURL);
        newProperty.setBio(editTextBio.getText().toString());
        newProperty.setAddress(editTextAddress.getText().toString());
        newProperty.setCity(editTextCity.getText().toString());
        newProperty.setZipCode(editTextZipCode.getText().toString());
        newProperty.setState(stateSpinner.getSelectedItem().toString());
        newProperty.setOwnerName(editTextOwnerName.getText().toString());
        newProperty.setOwnerPhoneNum(PhoneNumberUtils.formatNumber(editTextOwnerPhoneNum.getText().toString(), Locale.getDefault().getCountry()));
        newProperty.setOwnerEmail(editTextOwnerEmail.getText().toString());
    }

    private void displayError(View viewObject, String errorMessage)
    {
            if(viewObject instanceof EditText)
            {
                final EditText textEntry = (EditText) viewObject;
                textEntry.setError(errorMessage);
                textEntry.setBackgroundResource(R.drawable.error_background);
                textEntry.addTextChangedListener(new TextWatcher()  {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void afterTextChanged(Editable s)  {
                        textEntry.setError(null);
                        textEntry.setBackgroundResource(R.drawable.multi_state_background);
                    }
                });
            }
            else if(viewObject instanceof Spinner)
            {
                Spinner spinner = (Spinner) viewObject;
                TextView errorTextView = (TextView) spinner.getSelectedView();
                errorTextView.setError(errorMessage);
                errorTextView.setTextColor(getColor(R.color.error));
            }
    }

    public void scrollToTop()
    {
        final ScrollView scrollView = findViewById(R.id.scrollView);

        scrollView.post(new Runnable() {
            public void run() {
                scrollView.fullScroll(scrollView.FOCUS_UP);
            }
        });
    }

    public boolean isValidEmail(String emailInput)
    {
        return !emailInput.equals("") && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches();
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
            imageButton.setImageResource(R.drawable.add_pic_icon);
            imageButton.setBackgroundResource(R.drawable.border);
            return;
        }
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                imguri = data.getData();
                imageButton.setImageURI(imguri);
                imageButton.setBackgroundResource(R.color.colorPrimary);
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

    private void uploadImage()
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
                indicateUploadSuccess();
                createDelay(2000);
                pd.dismiss();
                //Closes the activity
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

    private void indicateUploadSuccess()
    {
        Button button = findViewById(R.id.add_property_button);

        button.setEnabled(false);
        button.setBackgroundColor(Color.GREEN);
        button.setText(R.string.propertyAdded);
        button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkmark, 0);
    }

    private void createDelay(int millis)
    {
        try
        {
            Thread.sleep(millis);
            finish();
        }
        catch (InterruptedException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
