package com.example.devesh.payroll.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.devesh.payroll.R;

/**
 * Created by devesh on 2/4/17.
 */

public class userDialog extends DialogFragment {

    Context myContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        TextView a,b,c,d,e,f;
        View view = getActivity().getLayoutInflater().inflate(R.layout.all_user_data,null);

        a = (TextView) view.findViewById(R.id.Address);
        b = (TextView) view.findViewById(R.id.Email);
        c = (TextView) view.findViewById(R.id.Contact);
        d = (TextView) view.findViewById(R.id.Department);
        e = (TextView) view.findViewById(R.id.Salary);
        f = (TextView) view.findViewById(R.id.Tax);

        a.setText(args.getString("address"));
        b.setText(args.getString("email"));
        c.setText(args.getString("contact"));
        d.setText(args.getString("department"));

        e.setText(String.valueOf(args.getInt("salary")));
        f.setText(String.valueOf(args.getFloat("tax")));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view).setCancelable(true);
        builder.setTitle(args.getString("name"));
        return builder.create();
    }
}
