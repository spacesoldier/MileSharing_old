package com.soloway.city.milesharing.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.soloway.city.milesharing.R;

public class UserLoginDialog extends DialogFragment {

    private EditText mEditText;

    public UserLoginDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_login, container);
        mEditText = (EditText) view.findViewById(R.id.txt_your_login);
        getDialog().setTitle("User Login");

        return view;
    }
}
