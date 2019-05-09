package edu.utep.cs.cs4330.dailyreminder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class DeleteDialog extends DialogFragment {

    public interface DeleteDialogListener {
        void DeleteItemDialog(int position);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete");
        builder.setMessage("Are your sure you want to delete this Task?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        DeleteDialogListener activity = (DeleteDialogListener) getActivity();
//                        assert activity != null;
                        assert getArguments() != null;
                        Log.i("position", String.valueOf(getArguments().getInt("position")));
//                        activity.DeleteItemDialog(getArguments().getInt("position"));
                        ((MainActivity)getActivity()).DeleteItemDialog(getArguments().getInt("position"));
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