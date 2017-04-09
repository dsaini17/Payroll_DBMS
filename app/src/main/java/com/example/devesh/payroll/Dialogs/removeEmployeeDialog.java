package com.example.devesh.payroll.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.devesh.payroll.R;

/**
 * Created by devesh on 4/4/17.
 */

public class removeEmployeeDialog extends DialogFragment {

    Context myContext;
    removeEmployeeListener removeEmployeeListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
        removeEmployeeListener = (removeEmployeeDialog.removeEmployeeListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.remove_emloyee_dialog, null);
        final EditText eid = (EditText) view.findViewById(R.id.employeeID);
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setView(view);
        builder.setTitle("REMOVE EMPLOYEE");
        builder.setCancelable(true);
        builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String get = eid.getText().toString().trim();

                if (get.isEmpty())
                    Toast.makeText(myContext, "EMPLOYEE ID EMPTY", Toast.LENGTH_SHORT).show();
                else {
                    Integer sendData = Integer.valueOf(get);
                    removeEmployeeListener.OnRemoveEmployee(sendData);
                }
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    public interface removeEmployeeListener {
        public void OnRemoveEmployee(Integer id);
    }
}
