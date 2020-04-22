package com.firebase.rentme.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.firebase.rentme.R;

public class SelectBathroomsDialog extends AppCompatDialogFragment
{
    private Button upButton;
    private Button downButton;
    private EditText valueEditText;
    SelectBathroomsDialogListener listener;
    private double values;

    public SelectBathroomsDialog(double values)
    {
        this.values = values;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        try {
            listener = (SelectBathroomsDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() +
                    "must implement SelectBathroomsDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.select_room_dialog, null);

        builder.setView(view)
                .setTitle("Select Bathrooms")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyBathroomValues(valueEditText.getText().toString(), values);
                    }
                });

        upButton = view.findViewById(R.id.upButton);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(values >= 3)
                {
                    values++;
                }
                else
                {
                    values += 0.5;
                }

                StringBuilder value = new StringBuilder(Integer.toString((int) values));

                if(values % 1.0 != 0.0)
                {
                    value.append(".5");
                }

                valueEditText.setText(value.toString());
            }
        });
        downButton = view.findViewById(R.id.downButton);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(values <= 3)
                {
                    if(values != 1)
                    {
                        values -= 0.5;
                    }
                }
                else
                {
                    values--;
                }

                StringBuilder value = new StringBuilder(Integer.toString((int) values));

                if(values % 1.0 != 0.0)
                {
                    value.append(".5");
                }

                valueEditText.setText(value.toString());
            }
        });
        valueEditText = view.findViewById(R.id.roomNumberEditText);

        StringBuilder value = new StringBuilder(Integer.toString((int) values));

        if(values % 1.0 != 0.0)
        {
            value.append(".5");
        }

        valueEditText.setText(value.toString());

        return builder.create();
    }

    public interface SelectBathroomsDialogListener{
        void applyBathroomValues(String bathroomsText, double bathroomsValue);
    }
}



