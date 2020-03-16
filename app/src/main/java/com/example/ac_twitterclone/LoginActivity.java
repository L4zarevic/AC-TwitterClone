package com.example.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.rilixtech.materialfancybutton.MaterialFancyButton;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    private EditText edtEmail, edtPassword;
    private TextView txtSignUp;
    private MaterialFancyButton btnLogIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialization
        edtEmail = findViewById(R.id.edtEmail_LogInActivity);
        edtPassword = findViewById(R.id.edtPassword_LogInActivity);
        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnLogIn);
                }
                return false;
            }
        });
        txtSignUp = findViewById(R.id.txtSignUp_LogInActivity);
        btnLogIn = findViewById(R.id.btnLogIn_LogInActivity);

        btnLogIn.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);

        //Token session check
        if (ParseUser.getCurrentUser() != null) {
            ParseUser.getCurrentUser().logOut();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogIn_LogInActivity:
                if (edtEmail.getText().toString().trim().equals("") ||
                        edtPassword.getText().toString().trim().equals("")) {

                    FancyToast.makeText(LoginActivity.this, "Email,Password is required!", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();

                } else {
                    ParseUser.logInInBackground(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (user != null && e == null) {
                                FancyToast.makeText(LoginActivity.this, user.getUsername() + " is Logged in successfully ", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                transitionToSocialMediaActivity();
                            }
                        }
                    });
                }
                break;

            case R.id.txtSignUp_LogInActivity:
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
                break;
        }

    }

    //A method that allows us to hide the keyboard
    public void rootLoginLayout(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transitionToSocialMediaActivity() {
        Intent intent = new Intent(LoginActivity.this, Twitter.class);
        startActivity(intent);
        finish();

    }
}
