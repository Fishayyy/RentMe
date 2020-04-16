package com.firebase.rentme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.rentme.models.PriceInputFilter;
import com.firebase.rentme.models.Property;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class CreateListingActivity extends AppCompatActivity
{
    private static final String TAG = "MainActivity";
    private final static int GALLERY = 1, ZIP_CODE_LENGTH = 5, PHONE_NUMBER_LENGTH = 13;
    private final static long DELAY = 1500L;

    private FirebaseFirestore database;
    private StorageReference storageReference;

    private Property newProperty;

    private Uri imgURI;
    private String photoURL = "";

    private ImageButton imageButton;
    private EditText editTextPrice;
    private Spinner categorySpinner;
    private EditText editTextOwnerName;
    private EditText editTextOwnerPhoneNum;
    private EditText editTextOwnerEmail;
    private EditText editTextAddress;
    private EditText editTextCity;
    private EditText editTextZipCode;
    private Spinner stateSpinner;
    private EditText editTextBio;
    private CircularProgressButton addPropertyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_property);

        initFirestore();
        initButtons();
        initSpinners();
        initEditTextFields();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            View v = getCurrentFocus();
            if (v instanceof EditText)
            {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()))
                {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void initFirestore()
    {
        FirebaseFirestore.setLoggingEnabled(true);
        database = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Images");
    }

    private void initButtons()
    {
        imageButton = findViewById(R.id.uploadImageButton);
        addPropertyButton = findViewById(R.id.add_property_button);
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
        editTextPrice.setFilters(new InputFilter[]{new PriceInputFilter(8, 2)});
        editTextBio = findViewById(R.id.edit_text_bio);
        editTextAddress = findViewById(R.id.edit_text_address);
        editTextCity = findViewById(R.id.edit_text_city);
        editTextZipCode = findViewById(R.id.edit_text_zip);
        editTextOwnerName = findViewById(R.id.edit_text_ownerName);
        editTextOwnerPhoneNum = findViewById(R.id.edit_text_ownerPhoneNum);
        editTextOwnerPhoneNum.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        editTextOwnerEmail = findViewById(R.id.edit_text_ownerEmail);
    }

    public void choosePhotoFromGallery(View v)
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    //Results from choosePhotoFromGallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED)
        {
            imgURI = null;
            imageButton.setImageResource(R.drawable.add_pic_icon);
            imageButton.setBackgroundResource(R.drawable.border);
            return;
        }
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                imgURI = data.getData();
                imageButton.setImageURI(imgURI);
                imageButton.setBackgroundResource(R.color.primary);
            }
        }
    }

    public void validatePropertyInformation(View v)
    {
        boolean isValid = true;
        boolean scrollToTop = false;

        if (imgURI == null)
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

        if (scrollToTop)
        {
            scrollToTop();
        }

        if (isValid)
        {
            addPropertyButton.startAnimation();
            uploadImage();
        }
    }

    public void scrollToTop()
    {
        final ScrollView scrollView = findViewById(R.id.createListingScrollView);

        scrollView.post(new Runnable()
        {
            public void run()
            {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    public boolean isValidEmail(String emailInput)
    {
        return !emailInput.equals("") && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches();
    }

    private void displayError(View viewObject, String errorMessage)
    {
        if (viewObject instanceof EditText)
        {
            editTextError((EditText) viewObject, errorMessage);
        }
        else if (viewObject instanceof Spinner)
        {
            spinnerError((Spinner) viewObject, errorMessage);
        }
    }

    private void editTextError(final EditText textEntry, String errorMessage)
    {
        textEntry.setError(errorMessage);
        textEntry.setBackgroundResource(R.drawable.error_background);
        textEntry.addTextChangedListener(new TextWatcher()
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
                textEntry.setError(null);
                textEntry.setBackgroundResource(R.drawable.multi_state_background);
            }
        });
    }

    private void spinnerError(Spinner spinner, String errorMessage)
    {
        TextView errorTextView = (TextView) spinner.getSelectedView();
        errorTextView.setError(errorMessage);
        errorTextView.setTextColor(getColor(R.color.error));
    }

    private void uploadImage()
    {
        storageReference.child(System.currentTimeMillis() + "." + getExtension(imgURI))
                .putFile(imgURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        retrieveImageURL(taskSnapshot.getStorage().getDownloadUrl());
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception exception)
                    {
                        Toast.makeText(CreateListingActivity.this, "Error Uploading Image", Toast.LENGTH_LONG).show();
                    }
                });
    }

    //Get the file extension so we can store the file with extension
    private String getExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void retrieveImageURL(final Task<Uri> urlTask)
    {
        urlTask.addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri uri)
            {
                photoURL = uri.toString();
                uploadProperty();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception exception)
            {
                retrieveImageURL(urlTask);
            }
        });
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
                        addPropertyButton.doneLoadingAnimation(getColor(R.color.success), BitmapFactory.decodeResource(getResources(), R.drawable.house_checkmark));
                        exitAfterDelay();
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.w(TAG, "Error adding document", e);
                        addPropertyButton.doneLoadingAnimation(getColor(R.color.error), BitmapFactory.decodeResource(getResources(), R.drawable.house_x));
                        Toast.makeText(CreateListingActivity.this, "Failed to Create Listing", Toast.LENGTH_SHORT).show();
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

    private void exitAfterDelay()
    {
        TimerTask task = new TimerTask()
        {
            public void run()
            {
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, DELAY);
    }

    public void exitWithNoDelay(View view)
    {
        finish();
    }
}
