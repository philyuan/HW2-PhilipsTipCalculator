package ec601.philipstipcalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;   //Widget libraries
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextUtils;  //Function to check if input string is empty

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.NumberFormat;  //Number formatting

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth fb_auth;

    //Declare buttons, text views, edit texts
    private Button button_10,button_15,button_20,button_custom,button_logout;
    private TextView tv_title,tv_result;
    private EditText et_num1, et_num2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize firebase authorization
        fb_auth = FirebaseAuth.getInstance();

        //If not logged in, go to login page
        if(fb_auth.getCurrentUser() == null){
            startActivity(new Intent(this, Login2Activity.class));
        }

        //Find buttons, text views, edit texts
        button_10 = (Button)findViewById(R.id.button_10);
        button_15 = (Button)findViewById(R.id.button_15);
        button_20 = (Button)findViewById(R.id.button_20);
        button_custom = (Button)findViewById(R.id.button_custom);
        button_logout = (Button)findViewById(R.id.button_logout);

        et_num1 = (EditText)findViewById(R.id.et_num1);
        et_num2 = (EditText)findViewById(R.id.et_num2);

        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_result = (TextView)findViewById(R.id.tv_result);

        button_10.setOnClickListener(this);
        button_15.setOnClickListener(this);
        button_20.setOnClickListener(this);
        button_custom.setOnClickListener(this);
        button_logout.setOnClickListener(this);

        //Show user name in title
        FirebaseUser fb_user = fb_auth.getCurrentUser();
        tv_title.setText("User: "+fb_user.getEmail());

    }

    @Override
    public void onClick(View v) {
        //Declare and initialize percentages
        float p_10 = 0.10f;
        float p_15 = 0.15f;
        float p_20 = 0.20f;
        String result_total = "$0.00";

        //Get the total and custom tip as strings.
        String s_total = et_num1.getText().toString();
        String s_customtip = et_num2.getText().toString();

        //If logout button pressed, go to sign in activity
        if (v.getId() == R.id.button_logout) {
            fb_auth.signOut();
            startActivity(new Intent(this, Login2Activity.class));
        }

        else {
            //Check that user inputs a total
            if (TextUtils.isEmpty(s_total)) {
                et_num1.setError("Please input a total");
            } else {
                //Convert input strings to floats
                float total = Float.parseFloat(s_total);

                NumberFormat format_result = NumberFormat.getCurrencyInstance();

                //Depending on which button pressed...
                switch (v.getId()) {

                    //Calculate and display result on text view
                    case R.id.button_10:
                        float result_10 = total * (1.00f + p_10);
                        result_total = format_result.format(result_10);
                        break;

                    case R.id.button_15:
                        float result_15 = total * (1.00f + p_15);
                        result_total = format_result.format(result_15);
                        break;

                    case R.id.button_20:
                        float result_20 = total * (1.00f + p_20);
                        result_total = format_result.format(result_20);
                        break;

                    case R.id.button_custom:
                        //Check that user inputs a tip percent
                        if (TextUtils.isEmpty(s_customtip)) {
                            et_num2.setError("Please input a custom tip (%)");
                        } else {
                            float customtip = Float.parseFloat(s_customtip) / 100.00f;

                            //If something went wrong with custom tips (probably not), catch it!
                            try {
                                float result_custom = total * (1.00f + customtip);
                                result_total = format_result.format(result_custom);
                            } catch (Exception error_custom) {
                                tv_result.setText("Something went wrong!");
                            }
                        }
                        break;
                }

                tv_result.setText(String.valueOf(result_total));
            }
        }
    }
}
