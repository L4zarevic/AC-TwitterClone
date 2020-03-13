package com.example.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;
import com.rilixtech.materialfancybutton.MaterialFancyButton;

public class StartScreen extends AppCompatActivity implements View.OnClickListener {

    private TextView txtLogIn;
    private MaterialFancyButton btnCreateAcc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        txtLogIn = findViewById(R.id.txtLogIn);
        btnCreateAcc = findViewById(R.id.btnCreateAcc);

        btnCreateAcc.setOnClickListener(this);
        txtLogIn.setOnClickListener(this);

        //Token session check
        if (ParseUser.getCurrentUser() != null) {
            Intent intentLogIn = new Intent(StartScreen.this, Twitter.class);
            startActivity(intentLogIn);
        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.txtLogIn:
                Intent intentLogIn = new Intent(StartScreen.this, LoginActivity.class);
                startActivity(intentLogIn);

                break;

            case R.id.btnCreateAcc:
                Intent intentSignUp = new Intent(StartScreen.this, SignUp.class);
                startActivity(intentSignUp);
                finish();
                break;
        }
    }
}
