package com.example.devesh.payroll.Dialogs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.devesh.payroll.R;

/**
 * Created by devesh on 2/4/17.
 */

public class developerDialog extends DialogFragment {

    Context myCcontext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myCcontext = context;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View convertView = layoutInflater.inflate(R.layout.developer_dialog, null);

        final TextView getcode = (TextView) convertView.findViewById(R.id.githubCode);

        getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getcode.getText().toString().trim())));
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(myCcontext);
        builder.setTitle("Developers");
        builder.setCancelable(true);
        builder.setView(convertView);

        return builder.create();
    }
}
