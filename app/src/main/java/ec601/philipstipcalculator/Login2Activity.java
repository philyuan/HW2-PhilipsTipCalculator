package ec601.philipstipcalculator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//LOG IN ACTIVITY
public class Login2Activity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    private EditText et_email, et_password;
    private TextView tv_signup;

    private ProgressDialog pd_progress;

    private FirebaseAuth fb_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        //Initialize firebase authorization
        fb_auth = FirebaseAuth.getInstance();

        //If user is already logged in go to main activity
        if(fb_auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        //Get UI elements
        btn_login = (Button)findViewById(R.id.btn_login);
        et_email = (EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password);
        tv_signup = (TextView)findViewById(R.id.tv_signup);

        //Set listeners
        btn_login.setOnClickListener(this);
        tv_signup.setOnClickListener(this);

        //Create progress bar for registration
        pd_progress = new ProgressDialog(this);

    }

    //Login user function
    private void login_user(){
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        //Check if either email or password inputs are empty
        if(TextUtils.isEmpty(email)) {
            et_email.setError("Please input email");
            return;
        }
        if(TextUtils.isEmpty(password)) {
            et_password.setError("Please input password");
            return;
        }

        //Registration
        pd_progress.setMessage("Logging In...");
        pd_progress.show();

        fb_auth.signInWithEmailAndPassword(email,password)
                //Wait for completion
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd_progress.hide();

                        //After completion, check if the user successfully logs in
                        if(task.isSuccessful()) {
                            //Go to main activity if success
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else {
                            Toast.makeText(Login2Activity.this, "Can't log in...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    @Override
    public void onClick(View v) {
        //Login user
        if(v == btn_login){
            login_user();
        }

        //Go to registration activity
        if(v == tv_signup){
            startActivity(new Intent(this,LoginActivity.class));
        }

    }
}
