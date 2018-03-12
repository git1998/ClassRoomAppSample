package ltd.akhbod.studentapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "sms";
    private BroadcastReceiver smsBroadcastReceiver;
    IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    public static final String SMS_BUNDLE = "pdus";




    Spinner spinner;

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mVarificatioCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        smsBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("smsBroadcastReceiver", "onReceive");
                Bundle pudsBundle = intent.getExtras();
                Object[] pdus = (Object[]) pudsBundle.get(SMS_BUNDLE);
                SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
                Log.i(TAG,  messages.getMessageBody());

                String firebaseVerificationCode = messages.getMessageBody().trim().split(" ")[0];//only a number code
                Toast.makeText(getApplicationContext(), firebaseVerificationCode,Toast.LENGTH_SHORT).show();
                EditText mCode=findViewById(R.id.reg_code_id);
                mCode.setText(firebaseVerificationCode);


            }
        };



        if (savedInstanceState != null) {
            Log.d("vow","savedInstaceState");
            onRestoreInstanceState(savedInstanceState);
        }

        setContentView(R.layout.activity_sign_up);

        spinner=findViewById(R.id.reg_spinner_id);
        ArrayAdapter adapter=ArrayAdapter.createFromResource(this,R.array.subjects,R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button send=findViewById(R.id.reg_sendbtn_id);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mphoneNo=findViewById(R.id.reg_mobno_id);
                String phoneNo=mphoneNo.getText().toString();

               startPhoneNumberVarification(phoneNo);
            }
        });

        Button reSend=findViewById(R.id.reg_resendbtn_id);
        reSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mphoneNo=findViewById(R.id.reg_mobno_id);
                String phoneNo=mphoneNo.getText().toString();

                reStartPhoneNumberVerification(phoneNo);
            }
        });



        mAuth=FirebaseAuth.getInstance();

        mVarificatioCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d("abhi", "onVerificationCompleted: ");
                mVerificationInProgress=false;
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            Log.d("vow","Verification failed")   ;
            mVerificationInProgress=false;
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                Log.d("abhi", "onCodeSent: ");
                Toast.makeText(getApplicationContext(),"code has been sent!!!",Toast.LENGTH_SHORT).show();
                mVerificationId=s;
                Log.d("varificationid",s);
                mVerificationInProgress=true;
                mResendToken=forceResendingToken;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);

                Log.d("timeout",s);
            }
        };


    }





    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        EditText mphoneNo=findViewById(R.id.reg_mobno_id);
        String phoneNo=mphoneNo.getText().toString();

        Log.d("shubham", ""+mVerificationInProgress);
        if(mVerificationInProgress==true){
            Log.d("shubham", "onStart:");
            startPhoneNumberVarification(phoneNo);
        }

    }

    public void onClickVerify(View view) {

        EditText mCode=findViewById(R.id.reg_code_id);
        String code=mCode.getText().toString();

        PhoneAuthCredential credential=new PhoneAuthCredential(mVerificationId,code);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Log.d("abhi","onSignIn");
                    String user= mAuth.getCurrentUser().getUid().toString();

                    mVerificationInProgress=false;
                    Toast.makeText(getApplicationContext(),"Sign in suceesfully"+"\n"+user,Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_LONG).show();
            }});

    }


    public void startPhoneNumberVarification(String phoneNo){
        Log.d("shubham", "onStartPhoneNumberAuth:");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,
                60,
                TimeUnit.SECONDS,
                this,
                mVarificatioCallbacks
        );

        mVerificationInProgress=true;


    }

    public void reStartPhoneNumberVerification(String phoneNo){
        Log.d("vow","onRestartVerificationNumber");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,
                60,
                TimeUnit.SECONDS,
                this,
                mVarificatioCallbacks,
                mResendToken
        );

        mVerificationInProgress=true;

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putBoolean("progress",mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mVerificationInProgress=savedInstanceState.getBoolean("progress");
    }
}
