package com.example.quizkiakya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginWindow extends AppCompatActivity {
    EditText mobile_no_text;
    Button get_otp_btn ;
    TextView errorText;
    LinearLayout editText_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_window);
        get_otp_btn = findViewById(R.id.Get_otp_btn);
        mobile_no_text = findViewById(R.id.mobile_number);
        errorText = findViewById(R.id.error_text);
        editText_layout = findViewById(R.id.editText_layout);
        ProgressBar progressBar = findViewById(R.id.progresbar_send_otp);


        get_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mobile_no_text.getText().toString().trim().isEmpty()){
                    if((mobile_no_text.getText().toString().trim()).length() == 10){
                        progressBar.setVisibility(View.VISIBLE);
                        get_otp_btn.setVisibility(View.INVISIBLE);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                "+91" + mobile_no_text.getText().toString(),
                                60,
                                TimeUnit.SECONDS,
                                LoginWindow.this,

                                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        progressBar.setVisibility(View.GONE);
                                        get_otp_btn.setVisibility(View.VISIBLE);
                                        Toast.makeText(LoginWindow.this," Verification completed ",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        progressBar.setVisibility(View.GONE);
                                        get_otp_btn.setVisibility(View.VISIBLE);
                                        Toast.makeText(LoginWindow.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String backend, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                        progressBar.setVisibility(View.GONE);
                                        get_otp_btn.setVisibility(View.VISIBLE);
                                        Intent intent = new Intent(getApplicationContext(),EnterOtp.class);
                                        intent.putExtra("mobile",mobile_no_text.getText().toString());
                                        intent.putExtra("backendOtp",backend);
                                        startActivity(intent);
                                    }
                                }
                        );

                    }else{
                        mobile_no_text.setText("");
                        errorText.setText("Please enter 10 digit number");
                        mobile_no_text.setHint("Enter correct number");
                    }
                }else{
                    errorText.setText("Please enter number");
                }
            }
        });


    }
}