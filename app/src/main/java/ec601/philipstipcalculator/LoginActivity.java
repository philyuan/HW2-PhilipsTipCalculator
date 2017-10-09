package ec601.philipstipcalculator;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextUtils;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//REGISTRATION ACTIVITY
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_register;
    private EditText et_email, et_password;
    private TextView tv_signin;

    private ProgressDialog pd_progress;

    private FirebaseAuth fb_auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize firebase authorization
        fb_auth = FirebaseAuth.getInstance();

        //Find UI elements
        btn_register = (Button)findViewById(R.id.btn_register);
        et_email = (EditText)findViewById(R.id.et_email);
        et_password = (EditText)findViewById(R.id.et_password);
        tv_signin = (TextView)findViewById(R.id.tv_signin);

        //Create progress bar for registration
        pd_progress = new ProgressDialog(this);

        //Set actions for click
        btn_register.setOnClickListener(this);
        tv_signin.setOnClickListener(this);
    }

    //Registration function
    private void register_user() {
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
        pd_progress.setMessage("Registration In Progress...");
        pd_progress.show();

        fb_auth.createUserWithEmailAndPassword(email,password)
                //Wait for completion
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd_progress.hide();

                        //After completion, check if the user successfully registers
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Can't register...", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    @Override
    public void onClick(View v) {
        //Register user
        if(v == btn_register) {
            register_user();
        }

        //Go to login activity
        if(v == tv_signin) {
            startActivity(new Intent(this, Login2Activity.class));
        }
    }

}
