package com.firebase.rentme.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.rentme.MainActivity;
import com.firebase.rentme.R;
import com.firebase.rentme.dialogs.SelectBathroomsDialog;
import com.firebase.rentme.dialogs.SelectBedroomsDialog;
import com.firebase.rentme.models.PriceInputFilter;
import com.firebase.rentme.models.Property;

import com.firebase.rentme.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import java.util.Timer;
import java.util.TimerTask;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class CreatePropertyActivity extends AppCompatActivity implements SelectBathroomsDialog.SelectBathroomsDialogListener, SelectBedroomsDialog.SelectBedroomsDialogListener
{
    private static final String TAG = "CreatePropertyActivity";

    private final static int GALLERY = 1, ZIP_CODE_LENGTH = 5, PHONE_NUMBER_LENGTH = 13;
    private final static long DELAY = 1500L;

    private FirebaseFirestore database;
    private StorageReference storageReference;

    private Property newProperty;

    private Uri imgURI;
    private ArrayList<String> photoURLList = new ArrayList<>();
    private FirebaseAuth fAuth;

    //Test for upload multiple images
    int SELECT_PICTURES = 1;
    //array to store multiple photos
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();
    int upload_count = 0;
    int photoURLCounter = 0;
    int image = 0;


    private Button uploadImageButton;
    private EditText editTextPrice;
    private Spinner categorySpinner;
    private TextView textViewBedrooms;
    private TextView textViewBathrooms;
    private Button buttonSelectBedrooms;
    private int bedrooms = 0;
    private Button buttonSelectBathrooms;
    private double bathrooms = 1.0;
    private EditText editTextAddress;
    private EditText editTextCity;
    private EditText editTextZipCode;
    private Spinner stateSpinner;
    private CheckBox petsAllowedCheckBox;
    private CheckBox smokingAllowedCheckBox;
    private CheckBox parkingAvailableCheckBox;
    private CheckBox poolAvailableCheckBox;
    private CheckBox backyardAvailableCheckBox;
    private CheckBox laundryCheckBox;
    private CheckBox handicapAccessibleCheckBox;
    private EditText editTextBio;
    private CircularProgressButton addPropertyButton;

    @Override
    public void applyBedroomValues(String bedroomsText, int bedroomsValue)
    {
        bedrooms = bedroomsValue;
        textViewBedrooms.setText(bedroomsText);
    }

    @Override
    public void applyBathroomValues(String bathroomText, double bathroomValue)
    {
        bathrooms = bathroomValue;
        textViewBathrooms.setText(bathroomText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_property);

        Log.d(TAG, "onCreate: started");

        initAuthentication();
        initFirestore();
        initButtons();
        initTextViews();
        initSpinners();
        initEditTextFields();
        initCheckBoxes();
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

    private void initAuthentication()
    {
        fAuth = FirebaseAuth.getInstance();
    }

    private void initButtons()
    {
        uploadImageButton = findViewById(R.id.uploadImageButton);
        buttonSelectBedrooms = findViewById(R.id.bedroomsButton);
        buttonSelectBedrooms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openDialog(view);
            }
        });
        buttonSelectBathrooms = findViewById(R.id.bathroomsButton);
        buttonSelectBathrooms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                openDialog(view);
            }
        });
        addPropertyButton = findViewById(R.id.add_property_button);
    }

    public void openDialog(View view)
    {
        if (view.getId() == R.id.bedroomsButton)
        {
            buttonSelectBedrooms.setTextColor(Color.BLACK);
            buttonSelectBedrooms.setError(null);
            SelectBedroomsDialog bedroomsDialog = new SelectBedroomsDialog(bedrooms);
            bedroomsDialog.show(getSupportFragmentManager(), "bedrooms_dialog");
        }
        else if (view.getId() == R.id.bathroomsButton)
        {
            buttonSelectBathrooms.setTextColor(Color.BLACK);
            buttonSelectBathrooms.setError(null);
            SelectBathroomsDialog bathroomsDialog = new SelectBathroomsDialog(bathrooms);
            bathroomsDialog.show(getSupportFragmentManager(), "bathrooms_dialog");
        }
    }

    private void initTextViews()
    {
        textViewBedrooms = findViewById(R.id.bedroomsTextView);
        textViewBathrooms = findViewById(R.id.bathroomsTextView);
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
        editTextAddress = findViewById(R.id.edit_text_name);
        editTextCity = findViewById(R.id.edit_text_email);
        editTextZipCode = findViewById(R.id.edit_text_zip);
    }

    private void initCheckBoxes()
    {
        petsAllowedCheckBox = findViewById(R.id.petRulesCheckBox);
        smokingAllowedCheckBox = findViewById(R.id.smokingRulesCheckBox);
        parkingAvailableCheckBox = findViewById(R.id.parkingAmenityCheckBox);
        poolAvailableCheckBox = findViewById(R.id.poolAmenityCheckBox);
        backyardAvailableCheckBox = findViewById(R.id.backyardAmenityCheckBox);
        laundryCheckBox = findViewById(R.id.laundryAmenityCheckBox);
        handicapAccessibleCheckBox = findViewById(R.id.handicapAmenityCheckBox);
    }

    public void choosePhotoFromGallery(View v)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES);
    }

    //Results from choosePhotoFromGallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED)
        {
            imgURI = null;
            ImageList.clear();
            uploadImageButton.setBackgroundResource(R.drawable.border);
            uploadImageButton.setText(R.string.choose_images);
            return;
        }
        if (requestCode == GALLERY)
        {
            if (resultCode == MainActivity.RESULT_OK)
            {
                if (data.getClipData() != null)
                {
                    ImageList.clear();
                    int count = data.getClipData().getItemCount();
                    Log.i("count", String.valueOf(count));
                    int currentImageSelected = 0;
                    while (currentImageSelected < count)
                    {
                        imgURI = data.getClipData().getItemAt(currentImageSelected).getUri();
                        Log.i("uri", imgURI.toString());
                        ImageList.add(imgURI);
                        currentImageSelected = currentImageSelected + 1;
                    }
                    uploadImageButton.setText(getResources().getString(R.string.num_images_selected, count));
                    uploadImageButton.setBackgroundResource(R.drawable.success_background);
                    Log.i("listsize", String.valueOf(ImageList.size()));
                }
            }
        }
    }

    public void validatePropertyInformation(View v)
    {
        boolean isValid = true;
        boolean scrollToTop = false;

        if (ImageList.size() == 0)
        {
            isValid = false;
            scrollToTop = true;
            uploadImageButton.setBackgroundResource(R.drawable.error_background);
            uploadImageButton.setText(R.string.image_selection_error);
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

        if (textViewBedrooms.getText().toString().equals(""))
        {
            isValid = false;
            scrollToTop = true;
            displayError(buttonSelectBedrooms, "Must select number of bedrooms");
        }

        if (textViewBathrooms.getText().toString().equals(""))
        {
            isValid = false;
            scrollToTop = true;
            displayError(buttonSelectBathrooms, "Must select number of bathrooms");
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
        else if (viewObject instanceof Button)
        {
            buttonError((Button) viewObject, errorMessage);
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

    private void buttonError(Button button, String errorMessage)
    {
        button.setError(errorMessage);
        button.setTextColor(Color.RED);
    }

    //upload multiple images.
    private void uploadImage()
    {
        while (upload_count < ImageList.size())
        {
            storageReference.child(System.currentTimeMillis() + "." + getExtension(ImageList.get(image)))
                    .putFile(ImageList.get(image))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {
                            retrieveImageURL(taskSnapshot.getStorage().getDownloadUrl());
                            photoURLCounter = ImageList.size();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception exception)
                        {
                            Toast.makeText(CreatePropertyActivity.this, "Error Uploading Image", Toast.LENGTH_LONG).show();
                        }
                    });
            upload_count++;
            image++;
        }
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
                //Add to photo url array (*Charles *Use to iterate through photos)
                photoURLList.add(uri.toString());
                if (photoURLList.size() == photoURLCounter)
                {
                    initializeNewProperty();
                }

                initializeNewProperty();
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
        DocumentReference newPropertyDocReference = database.collection("properties").document(newProperty.getDocumentReferenceID());
        newPropertyDocReference.set(newProperty)
                .addOnSuccessListener(new OnSuccessListener<Void>()
                {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + newProperty.getDocumentReferenceID());
                        database.collection("users").document(fAuth.getUid()).update("ownerProperties", FieldValue.arrayUnion(newProperty.getDocumentReferenceID()));
                        addPropertyButton.doneLoadingAnimation(getColor(R.color.success), BitmapFactory.decodeResource(getResources(), R.drawable.house_checkmark));
                        exitAfterDelay();
                    }
                }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.w(TAG, "Error adding document", e);
                addPropertyButton.doneLoadingAnimation(getColor(R.color.error), BitmapFactory.decodeResource(getResources(), R.drawable.house_x));
                Toast.makeText(CreatePropertyActivity.this, "Failed to Create Listing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeNewProperty()
    {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userDocRef = database.collection("users").document(userID);

        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                User user = documentSnapshot.toObject(User.class);

                if (user != null)
                {
                    newProperty = Property.getPropertyInstance();
                    newProperty.setTimeOfCreation(System.currentTimeMillis());
                    newProperty.setHousingCategory(categorySpinner.getSelectedItem().toString());
                    newProperty.setPrice(Double.parseDouble(editTextPrice.getText().toString()));
                    newProperty.setPhotoURL(photoURLList.get(0));
                    newProperty.setPhotoURLList(photoURLList);
                    newProperty.setBio(editTextBio.getText().toString());
                    newProperty.setAddress(editTextAddress.getText().toString());
                    newProperty.setCity(editTextCity.getText().toString());
                    newProperty.setZipCode(editTextZipCode.getText().toString());
                    newProperty.setState(stateSpinner.getSelectedItem().toString());
                    newProperty.setOwnerName(user.getOwnerName());
                    newProperty.setOwnerPhoneNum(user.getOwnerPhoneNum());
                    newProperty.setOwnerEmail(user.getOwnerEmail());
                    newProperty.setBedrooms(bedrooms);
                    newProperty.setBathrooms(bathrooms);
                    newProperty.setPetsAllowed(petsAllowedCheckBox.isChecked());
                    newProperty.setSmokingAllowed(smokingAllowedCheckBox.isChecked());
                    newProperty.setParkingAvailable(parkingAvailableCheckBox.isChecked());
                    newProperty.setPoolAvailable(poolAvailableCheckBox.isChecked());
                    newProperty.setBackyardAvailable(backyardAvailableCheckBox.isChecked());
                    newProperty.setLaundryAvailable(laundryCheckBox.isChecked());
                    newProperty.setHandicapAccessible(handicapAccessibleCheckBox.isChecked());

                    uploadProperty();
                }
            }
        });
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
