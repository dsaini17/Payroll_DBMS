package com.example.devesh.payroll.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.devesh.payroll.R;

/**
 * Created by devesh on 1/4/17.
 */

public class passwordDialog extends DialogFragment {

    public interface DialogListener{
        public void Positive_Click(Bundle args);
    }

    public static final String TAG = "DialogFragment";
    Context myContext;
    DialogListener dialogListener;
    EditText oldPassword , newPassword ;
    String oldString , newString ;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dialogListener = (DialogListener) context;
        myContext = context;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.v(TAG,myContext+"");

        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View myView = layoutInflater.inflate(R.layout.password_change_layout,null);
        builder.setView(myView);

        oldPassword = (EditText) myView.findViewById(R.id.oldPassword);
        newPassword = (EditText) myView.findViewById(R.id.newPassword);

        builder.setCancelable(true);
        builder.setTitle("Password Change");
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                oldString = oldPassword.getText().toString().trim();
                newString = newPassword.getText().toString().trim();

                if(dialogListener!=null){
                    Bundle args = new Bundle();
                    args.putString("oldPassword",oldString);
                    args.putString("newPassword",newString);
                    dialogListener.Positive_Click(args);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();

    }
}
