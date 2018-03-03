package com.example.student.trempmeproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
    EditText tvUserName, tvPassword;
    Button btnSubmit, btnGoToSignUp;

    private final int RESULT_LOG_IN=0;
    private final int RESULT_SIGN_UP=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        onSubmitclicked();
        goToSignUp();

    }

    public void onSubmitclicked(){
        tvUserName=(EditText)findViewById(R.id.et_user_name);
        tvPassword=(EditText)findViewById(R.id.et_address);
        btnSubmit=(Button)findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("Username", tvUserName.getText().toString());
                intent.putExtra("Password", tvPassword.getText().toString());
                setResult(RESULT_LOG_IN, intent);
                finish();
            }
        });
    }

    public void goToSignUp(){
        btnGoToSignUp=(Button) findViewById(R.id.btn_to_sign_up);
        btnGoToSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(this,SignUp.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0){
            if(resultCode==RESULT_OK){
                Intent intent =new Intent();
                intent.putExtra("newUsername", data.getStringExtra("newUsername"));
                intent.putExtra("newPassword", data.getStringExtra("newPassword"));
                intent.putExtra("newAddress", data.getStringExtra("newAddress"));
                intent.putExtra("newStringHasLicence", data.getStringExtra("newStringHasLicence"));
                intent.putExtra("newImg",data.getByteArrayExtra("newImg"));
                setResult(RESULT_SIGN_UP,intent);
                finish();
            }
        }

    }
}
