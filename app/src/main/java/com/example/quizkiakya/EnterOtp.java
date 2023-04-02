package com.example.quizkiakya;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterOtp extends AppCompatActivity {
    EditText received_otp_editText;
    TextView resend_otp_text ;
    TextView show_number_text ;
    Button verify_otp_btn ;
    TextView error_in_otp;
    String getBackendOtp;
    String enteredOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        received_otp_editText = findViewById(R.id.otp);
        resend_otp_text = findViewById(R.id.resend_opt_text);
        show_number_text = findViewById(R.id.show_no_in_otp_page);
        verify_otp_btn = findViewById(R.id.verify_otp_btn);
        error_in_otp = findViewById(R.id.otp_error_text);
        TextView change_number = findViewById(R.id.change_no_in_otpPage);
        ProgressBar progressBar = findViewById(R.id.progressbar_verify_otp);

        //Change number code.....///////
        change_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginWindow.class);
                startActivity(intent);
            }
        });

        //show enters number format code ...

        show_number_text.setText(String.format(
                "+91-%s",getIntent().getStringExtra("mobile")
        ));

        //get otp with intent with prev. screen....code......

        getBackendOtp = getIntent().getStringExtra("backendOtp");

        //on verify button click code......................

        verify_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // checked that line of otp is not empty code.....
                if(!received_otp_editText.getText().toString().trim().isEmpty()){
                    //check that length of otp in 6 size of number code...............
                    if((received_otp_editText.getText().toString().trim()).length() == 6){

                        //convert enter otp into string to compare............
                        enteredOtp = received_otp_editText.getText().toString();
                        //checked that backend is not null......
                        if(getBackendOtp != NULL){
                            //create compare otp in fire base credential....
                            progressBar.setVisibility(View.VISIBLE);
                            verify_otp_btn.setVisibility(View.INVISIBLE);
                            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                    getBackendOtp,enteredOtp
                            );
                            //firebase code for signIn
                            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                progressBar.setVisibility(View.GONE);
                                                verify_otp_btn.setVisibility(View.VISIBLE);
                                                Intent intent = new Intent(getApplicationContext(),DashboardPage.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }else{
                                                error_in_otp.setText("Please enter correct OTP");
                                            }
                                        }
                                    });

                        }else{
                            Toast.makeText(EnterOtp.this,"Check your internet connection ",Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        received_otp_editText.setText("");
                        error_in_otp.setText("Please enter 6 digit OTP");
                        received_otp_editText.setHint("Enter 6 digit OTP");
                    }
                }else{
                    error_in_otp.setText("Please enter OTP");
                }
            }
        });
        //resend otp code............
        resend_otp_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        EnterOtp.this,

                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                Toast.makeText(EnterOtp.this," Verification completed ",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(EnterOtp.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String newBackend, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                getBackendOtp = newBackend;
                                Toast.makeText(EnterOtp.this,"OTP send successfully ",Toast.LENGTH_SHORT).show();
                            }
                        }
                );

            }
        });


    }
}