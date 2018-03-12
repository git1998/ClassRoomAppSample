package ltd.akhbod.studentapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();


    }

    public void onClickLogIn(View view) {


    }

    public void onClickSignUp(View view) {
            Intent intent=new Intent(this,SignUpActivity.class);
            startActivity(intent);
        }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    String user= mAuth.getCurrentUser().getUid().toString();

                    Toast.makeText(getApplicationContext(),"Sign in suceesfully"+"\n"+user,Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplication(), Activity.class);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_LONG).show();
            }});
    }


}
