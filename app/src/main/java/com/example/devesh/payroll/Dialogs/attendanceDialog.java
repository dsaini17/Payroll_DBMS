package com.example.devesh.payroll.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.devesh.payroll.R;

/**
 * Created by devesh on 4/4/17.
 */

public class attendanceDialog extends DialogFragment {


    public interface attendanceListener{
        public void onSelectDepartment(Integer index);
    }
    Context myContext;

    attendanceListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
        listener = (attendanceListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setTitle("SELECT DEPARTMENT").setItems(R.array.department_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("attendanceFragment",which+"");
                listener.onSelectDepartment(which);
            }
        }).setCancelable(true);

        return builder.create();
    }
}
