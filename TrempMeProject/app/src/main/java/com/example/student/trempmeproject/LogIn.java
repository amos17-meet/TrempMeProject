package com.example.student.trempmeproject;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogIn extends AppCompatActivity implements View.OnClickListener {
    EditText tvUserName, tvPassword;
    Button btnLogin, btnGoToSignUp;

    SQLiteHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        onSubmitclicked();
        openDatabase();
    }

    private void openDatabase(){
        dbHelper=new SQLiteHelper(this);
        db=dbHelper.getWritableDatabase();
    }

    public void onSubmitclicked(){
        tvUserName=(EditText)findViewById(R.id.et_user_name);
        tvPassword=(EditText)findViewById(R.id.et_password);
        btnLogin=(Button)findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {
        if(view==btnGoToSignUp){
                Intent intent=new Intent(this,SignUp.class);
                startActivityForResult(intent,0);
        }
        if (view==btnLogin){

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.log_in_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sign_up:
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivityForResult(intent,0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
