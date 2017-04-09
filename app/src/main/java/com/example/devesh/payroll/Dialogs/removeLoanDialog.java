package com.example.devesh.payroll.Dialogs;

import android.app.Dialog;
import android.content.Context;
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

public class removeLoanDialog extends DialogFragment {


    DialogInterface dialogInterface;
    Context myContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
        dialogInterface = (DialogInterface) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.remove_loan_dialog, null);
        final EditText amountText = (EditText) view.findViewById(R.id.getAmount);
        final EditText editText = (EditText) view.findViewById(R.id.getId);

        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);

        builder.setView(view);
        builder.setCancelable(true);
        builder.setTitle("ENTER DETAILS");
        builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {


                if (editText.getText().toString().trim().isEmpty())
                    Toast.makeText(myContext, "ENTER ID", Toast.LENGTH_SHORT).show();
                else {
                    if (amountText.getText().toString().trim().isEmpty())
                        Toast.makeText(myContext, "ENTER AMOUNT", Toast.LENGTH_SHORT).show();
                    else {
                        Integer eid = Integer.valueOf(editText.getText().toString().trim());
                        Integer loan = Integer.valueOf(amountText.getText().toString().trim());

                        dialogInterface.onRemoveListener(eid, loan);
                    }

                }
            }
        }).setNegativeButton("CANCEL", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {

            }
        });


        return builder.create();
    }


    public interface DialogInterface {
        public void onRemoveListener(Integer arg1, Integer arg2);
    }
}
