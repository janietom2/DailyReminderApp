package edu.utep.cs.cs4330.dailyreminder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.utep.cs.cs4330.dailyreminder.MainActivity;
import edu.utep.cs.cs4330.dailyreminder.Models.Utilities;
import edu.utep.cs.cs4330.dailyreminder.R;

public class AddDialog extends DialogFragment {

    Spinner priority;
    EditText itemSource;
    ProgressBar pb;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View DialogView = inflater.inflate(R.layout.edit_deialog,null);

        itemSource = DialogView.findViewById(R.id.editTextSource);

        // Spinner
        priority = DialogView.findViewById(R.id.priority);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.prioritynums));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        priority.setAdapter(adapter);




        String url = getArguments() != null ? getArguments().getString("url") : null;
        String tpriority = getArguments() != null ? getArguments().getString("priority") : null;


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add New Task");
        builder.setView(DialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = itemSource.getText().toString();
                        String tpriority = String.valueOf(Utilities.priorityConverter(priority.getSelectedItem().toString()));
//                        String tpriority = priority.getText().toString();
//                        ((MainActivity) Objects.requireNonNull(getActivity())).doAsync(url);
                        ((MainActivity)getActivity()).addItem(url, tpriority);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }});
        return builder.create();
    }
}