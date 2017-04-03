package com.example.devesh.payroll.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.FloatRange;
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

public class giveLoanDialog extends DialogFragment {

    Context myContext;

    public interface DialogListener{
        public void OnPositiveClick(Bundle args);
    }

    DialogListener dialogListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
        dialogListener = (DialogListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.give_loan_diaolg,null);

        final EditText id,amount,rate;

        id = (EditText) view.findViewById(R.id.loanID);
        amount = (EditText) view.findViewById(R.id.loanAmount);
        rate = (EditText) view.findViewById(R.id.loanInterest);

        final AlertDialog.Builder builder = new AlertDialog.Builder(myContext);

        builder.setTitle("Enter Loan Details");
        builder.setCancelable(true);
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(id.getText().toString().trim().isEmpty())
                    Toast.makeText(getActivity().getApplicationContext(),"ENTER ID",Toast.LENGTH_SHORT).show();
                else{
                    if(amount.getText().toString().trim().isEmpty())
                        Toast.makeText(getActivity().getApplicationContext(),"ENTER AMOUNT",Toast.LENGTH_SHORT).show();
                    else{
                        if(rate.getText().toString().trim().isEmpty())
                            Toast.makeText(getActivity().getApplicationContext(),"ENTER RATE",Toast.LENGTH_SHORT).show();
                        else{
                            Integer eid = Integer.valueOf(id.getText().toString().trim());
                            Integer loan = Integer.valueOf(amount.getText().toString().trim());
                            Float rat = Float.valueOf(rate.getText().toString().trim());

                            Bundle bundle = new Bundle();

                            bundle.putInt("eid",eid);
                            bundle.putInt("loan",loan);
                            bundle.putFloat("rate",rat);

                            dialogListener.OnPositiveClick(bundle);
                        }
                    }
                }

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
