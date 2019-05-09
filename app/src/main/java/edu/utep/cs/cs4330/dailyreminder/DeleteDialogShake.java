package edu.utep.cs.cs4330.dailyreminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class DeleteDialogShake extends DialogFragment {

    public interface DeleteDialogListener {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Dellete ALL completed tasks");
        builder.setMessage("You wish to delete **ALL** completed tasks?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        DeleteDialogListener activity = (DeleteDialogListener) getActivity();
//                        assert activity != null;
                        assert getArguments() != null;
//                        activity.DeleteItemDialog(getArguments().getInt("position"));
                        ((MainActivity)getActivity()).deleteDone();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
