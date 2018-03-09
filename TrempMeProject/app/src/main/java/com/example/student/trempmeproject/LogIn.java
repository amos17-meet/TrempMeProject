package com.example.student.trempmeproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
    EditText tvUserName, tvPassword;
    Button btnSubmit, btnGoToSignUp;

    SQLiteHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        onSubmitclicked();
        goToSignUp();
        openDatabase();
    }

    private void openDatabase(){
        dbHelper=new SQLiteHelper(this);
        db=dbHelper.getWritableDatabase();
    }

    public void onSubmitclicked(){
        tvUserName=(EditText)findViewById(R.id.et_user_name);
        tvPassword=(EditText)findViewById(R.id.et_password);
        btnSubmit=(Button)findViewById(R.id.btn_submit);

        btnSubmit.setOnClickListener(this);
    }


    public void goToSignUp(){
        btnGoToSignUp=(Button) findViewById(R.id.btn_to_sign_up);
        btnGoToSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==btnGoToSignUp){
                Intent intent=new Intent(this,SignUp.class);
                startActivityForResult(intent,0);
        }
        if (view==btnSubmit){

            int id=dbHelper.matchUsernameToPassword(db,tvUserName.getText().toString(),tvPassword.getText().toString());
            if(id!=-1){
                Intent intent = new Intent();
                intent.putExtra("id", id+"");
                setResult(RESULT_OK, intent);
                finish();
            }else{
                Toast.makeText(this,"Uncurrect Username Or Password",Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0){
            if(resultCode==RESULT_OK){
                Intent intent =new Intent();
                intent.putExtra("id", data.getStringExtra("id"));
                setResult(RESULT_OK,intent);
                finish();
            }
        }

    }
}
