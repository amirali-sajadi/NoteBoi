package com.example.noteboi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import dmax.dialog.SpotsDialog;

public class sign_up extends AppCompatActivity {

    EditText new_user, new_pass, new_email, confirm_pass;
    Button new_signup;
    android.app.AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        new_email = findViewById(R.id.ed_new_email);
        new_user = findViewById(R.id.ed_new_user);
        new_pass = findViewById(R.id.ed_new_pass);
        new_signup = findViewById(R.id.b_signup);
        confirm_pass = findViewById(R.id.ed_confirm_pass);
    }

    private boolean isNetworkAvailable(){

        ConnectivityManager manager = (ConnectivityManager) getSystemService(MainActivity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else return false;

    }

    public void sign_up(View view){

        if(isNetworkAvailable()){
            //show signing up dialog
            dialog = new SpotsDialog.Builder()
                    .setContext(this)
                    .setMessage("Signing up")
                    .setTheme(R.style.loading_dialog)
                    .setCancelable(false)
                    .build();
            dialog.show();

            if (new_user.getText().toString().length() <= 12
                    && new_pass.getText().toString().length() >= 4
                    && new_pass.getText().toString().length() <= 16
                    && isValidEmail(new_email.getText().toString().trim())
                    && new_pass.getText().toString().equals(confirm_pass.getText().toString())){

                ParseUser user = new ParseUser();
                user.setUsername(new_user.getText().toString().trim());
                user.setPassword(new_pass.getText().toString());
                user.setEmail(new_email.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(sign_up.this ,"signed up successfully", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(sign_up.this);
                            builder.setMessage("Please confirm your email\nAn email has been sent to you")
                                    .setTitle("Welcome to NoteBoi")
                                    .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent a = new Intent(sign_up.this, note_rows.class);
                                            startActivity(a);
                                        }
                                    })
                                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialogInterface) {
                                            Intent a = new Intent(sign_up.this, note_rows.class);
                                            startActivity(a);
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Toast.makeText(sign_up.this ,"sign up Failed", Toast.LENGTH_LONG).show();
                        }

                    }
                });dialog.dismiss();
            }

            else if(!isValidEmail(new_email.getText().toString())) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            else if(!new_pass.getText().toString().equals(confirm_pass.getText().toString())){
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            else if (new_user.getText().toString().length() > 12) {
                Toast.makeText(this, "Username must contain less than 13 characters", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
            else if (new_pass.getText().toString().length() < 4 || new_pass.getText().toString().length() > 12){
                Toast.makeText(this, "Password must contain 4-12 characters", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }

        }
        else Toast.makeText(this, "Check Your Network Connection", Toast.LENGTH_LONG).show();
    }

    //checking if the email format is valid
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}
