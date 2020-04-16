package com.firebase.rentme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.firebase.rentme.models.PriceInputFilter;
import com.firebase.rentme.models.ResultsFilter;
import com.jaygoo.widget.RangeSeekBar;

public class CreatePropertyFilterActivity extends AppCompatActivity
{
    private ResultsFilter resultsFilter;

    private double minPrice;
    private double maxPrice;

    private EditText minPriceEditText;
    private EditText maxPriceEditText;
    private RangeSeekBar bedroomsRangeBar;
    private RangeSeekBar bathroomsRangeBar;
    private Spinner categorySpinner;
    private Spinner petsAllowedSpinner;
    private Spinner smokingAllowedSpinner;
    private CheckBox parkingAvailableCheckBox;
    private CheckBox poolAvailableCheckBox;
    private CheckBox backyardAvailableCheckBox;
    private CheckBox laundryCheckBox;
    private CheckBox handicapAccessibleCheckBox;
    private Button resetFiltersButton;
    private Button applyFiltersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_properties);

        Intent intent = getIntent();
        resultsFilter = intent.getParcelableExtra(ResultsFilter.PARCELABLE_FILTER);

        initEditTexts();
        initRangeSeekBars();
        initSpinners();
        initCheckBoxes();
        initFilters();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    private void initEditTexts()
    {
        minPriceEditText = findViewById(R.id.minEditText);
        minPriceEditText.setFilters(new InputFilter[]{new PriceInputFilter(8, 2)});
        setFocusListener(minPriceEditText);

        maxPriceEditText = findViewById(R.id.maxEditText);
        maxPriceEditText.setFilters(new InputFilter[]{new PriceInputFilter(8, 2)});
        setFocusListener(maxPriceEditText);
    }

    private void setFocusListener(EditText editText)
    {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean hasFocus)
            {
                validateMinMaxPrice();
            }
        });
    }

    private boolean validateMinMaxPrice()
    {
        boolean isValidRange = true;

        if (!minPriceEditText.getText().toString().equals(""))
        {
            minPrice = Double.parseDouble(minPriceEditText.getText().toString());
        }

        if (!maxPriceEditText.getText().toString().equals(""))
        {
            maxPrice = Double.parseDouble(maxPriceEditText.getText().toString());
        }

        if (maxPrice < minPrice)
        {
            isValidRange = false;
            minPriceEditText.setError("Min cannot be greater than Max");
            minPriceEditText.setBackgroundResource(R.drawable.error_background);
            maxPriceEditText.setError("Max cannot be less than Min");
            maxPriceEditText.setBackgroundResource(R.drawable.error_background);
        }
        else
        {
            minPriceEditText.setError(null);
            minPriceEditText.setBackgroundResource(R.drawable.multi_state_background);
            maxPriceEditText.setError(null);
            maxPriceEditText.setBackgroundResource(R.drawable.multi_state_background);
        }

        return isValidRange;
    }

    private void initRangeSeekBars()
    {
        bedroomsRangeBar = findViewById(R.id.bedroomRangeSeek);
        bathroomsRangeBar = findViewById(R.id.bathroomsRangeSeek);
    }

    private void initSpinners()
    {
        categorySpinner = findViewById(R.id.propertyTypeSpinner);
        populateSpinner(categorySpinner, R.array.filterCategories);
        petsAllowedSpinner = findViewById(R.id.petRulesSpinner);
        populateSpinner(petsAllowedSpinner, R.array.rules);
        smokingAllowedSpinner = findViewById(R.id.smokingRulesSpinner);
        populateSpinner(smokingAllowedSpinner, R.array.rules);
    }

    private void populateSpinner(Spinner spinner, int stringArrayResource)
    {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, stringArrayResource, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void initCheckBoxes()
    {
        parkingAvailableCheckBox = findViewById(R.id.parkingCheckBox);
        poolAvailableCheckBox = findViewById(R.id.poolCheckBox);
        backyardAvailableCheckBox = findViewById(R.id.backyardCheckBox);
        laundryCheckBox = findViewById(R.id.laundryCheckBox);
        handicapAccessibleCheckBox = findViewById(R.id.handicapCheckBox);
    }

    private void initFilters()
    {
        minPrice = resultsFilter.getMinPrice();
        maxPrice = resultsFilter.getMaxPrice();
        minPriceEditText.setText(String.format("%8.2f", minPrice));
        maxPriceEditText.setText(String.format("%8.2f", maxPrice));
        bedroomsRangeBar.setProgress(resultsFilter.getBedroomsMinValueProgress(), resultsFilter.getBedroomsMaxValueProgress());
        bathroomsRangeBar.setProgress(resultsFilter.getBathroomsMinValueProgress(), resultsFilter.getBathroomsMaxValueProgress());
        categorySpinner.setSelection(getSpinnerPosition(resultsFilter.getHousingCategory(), R.array.filterCategories));
        petsAllowedSpinner.setSelection(resultsFilter.getPetsAllowed());
        smokingAllowedSpinner.setSelection(resultsFilter.getSmokingAllowed());
        parkingAvailableCheckBox.setChecked(resultsFilter.hasParkingAvailable());
        poolAvailableCheckBox.setChecked(resultsFilter.hasPool());
        backyardAvailableCheckBox.setChecked(resultsFilter.hasBackyard());
        laundryCheckBox.setChecked(resultsFilter.hasLaundry());
        handicapAccessibleCheckBox.setChecked(resultsFilter.isHandicapAccessible());
    }

    private int getSpinnerPosition(String spinnerValue, int stringArrayId)
    {
        int index = 0;
        String[] spinnerArray = getResources().getStringArray(stringArrayId);
        for (int i = 0; i < spinnerArray.length; ++i)
        {
            if(spinnerValue.equals(spinnerArray[i]))
            {
                index = i;
            }
        }
        return index;
    }

    public void setDefaults(View view)
    {
        resultsFilter.setDefaultFilter();
        minPrice = 0.0;
        maxPrice = Double.MAX_VALUE;
        minPriceEditText.setText("");
        maxPriceEditText.setText("");
        bedroomsRangeBar.setProgress(0f, 100f);
        bathroomsRangeBar.setProgress(0f, 100f);
        categorySpinner.setSelection(0);
        petsAllowedSpinner.setSelection(0);
        smokingAllowedSpinner.setSelection(0);
        parkingAvailableCheckBox.setChecked(false);
        poolAvailableCheckBox.setChecked(false);
        backyardAvailableCheckBox.setChecked(false);
        laundryCheckBox.setChecked(false);
        handicapAccessibleCheckBox.setChecked(false);
    }

    public void applyFilters(View view)
    {
        if(validateMinMaxPrice())
        {
            resultsFilter.setMinPrice(Double.parseDouble(minPriceEditText.getText().toString()));
            resultsFilter.setMaxPrice(Double.parseDouble(maxPriceEditText.getText().toString()));
            resultsFilter.setBedroomsMinValue(bedroomsRangeBar.getLeftSeekBar());
            resultsFilter.setBedroomsMaxValue(bedroomsRangeBar.getRightSeekBar());
            resultsFilter.setBathroomsMinValue(bathroomsRangeBar.getLeftSeekBar());
            resultsFilter.setBathroomsMaxValue(bathroomsRangeBar.getRightSeekBar());
            resultsFilter.setHousingCategoryFilter(categorySpinner.getSelectedItem().toString());
            resultsFilter.setPetsAllowedFilter(petsAllowedSpinner.getSelectedItemPosition());
            resultsFilter.setSmokingAllowedFilter(smokingAllowedSpinner.getSelectedItemPosition());
            resultsFilter.setParkingAvailableFilter(parkingAvailableCheckBox.isChecked());
            resultsFilter.setPoolFilter(poolAvailableCheckBox.isChecked());
            resultsFilter.setBackyardFilter(backyardAvailableCheckBox.isChecked());
            resultsFilter.setLaundryFilter(laundryCheckBox.isChecked());
            resultsFilter.setHandicapAccessible(handicapAccessibleCheckBox.isChecked());
        }
        else
        {
            scrollToTop();
        }
    }

    public void scrollToTop()
    {
        final ScrollView scrollView = findViewById(R.id.filterScrollView);

        scrollView.post(new Runnable()
        {
            public void run()
            {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    public void exitActivity(View view)
    {
        finish();
    }
}
