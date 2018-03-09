package com.example.student.trempmeproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase db;
    SQLiteHelper dbHelper;
    private int USER_ID;

    final int RESULT_DELETE=-2;

    EditText etSearch;
    //Button btnSearch;
    RadioGroup rgSearchBy;
    String searchBy="username";

    ListView lvUsers;
    ArrayList<UserObject> userList=new ArrayList<UserObject>();
    UserAdapter userAdapter;
    UserObject userObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openDatabase();
        goToLogIn();
        maketUsersList(dbHelper.getAll(db));
        makeListView();
        onClickedRadioGroup();
        onEtSearchChange();
        //onSearchButtonClicked();



    }

    private void goToLogIn(){
        Intent intent=new Intent(this,LogIn.class);
        startActivityForResult(intent,0);

    }

    private void openDatabase(){
        dbHelper=new SQLiteHelper(this);
        db=dbHelper.getWritableDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            if(resultCode==RESULT_OK){
                USER_ID = Integer.parseInt(data.getStringExtra("id"));
                Toast.makeText(this, "Hellow " + dbHelper.getRowUserName(dbHelper.getQueryById(db, USER_ID)), Toast.LENGTH_LONG).show();
                maketUsersList(dbHelper.getAll(db));
                makeListView();
            }
        }
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                Toast.makeText(this,"Your ditails changed Successfully",Toast.LENGTH_LONG).show();
                maketUsersList(dbHelper.getAll(db));
                makeListView();
            }
            if(resultCode==RESULT_DELETE){
                USER_ID=-1;
                goToLogIn();
            }
        }
    }

    public void showUser(){
        Cursor user= dbHelper.getQueryById(db,USER_ID);
        Toast.makeText(this,"Username: "+dbHelper.getRowUserName(user)+
                "\n+Address: "+dbHelper.getRowAddress(user)+
                "\nHas Driving Licence: "+dbHelper.hasDrivingLicence(user),Toast.LENGTH_LONG).show();
    }

    public void maketUsersList(Cursor c){
        userList.clear();
        if(c.getCount()!=0) {
            while (!c.isAfterLast()) {
                Bitmap userBitmap = BitmapHelper.getImage(dbHelper.getRowImage(c));
                userObject = new UserObject(dbHelper.getRowId(c),
                        dbHelper.getRowUserName(c),
                        dbHelper.getRowAddress(c),
                        dbHelper.hasDrivingLicence(c),
                        userBitmap);
                userList.add(userObject);
                c.moveToNext();
            }
        }else{
        }
    }

    public void makeListView(){
        userAdapter = new UserAdapter(this, 0, 0, userList);
        lvUsers = (ListView) findViewById(R.id.lv_users);
        lvUsers.setAdapter(userAdapter);
    }

    public void onClickedRadioGroup(){
        rgSearchBy=(RadioGroup)findViewById(R.id.rg_search_by);
        rgSearchBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_username:
                        searchBy="username";
                        break;
                    case R.id.rb_address:
                        searchBy="address";
                        break;
                    case R.id.rb_driving_licence:
                        searchBy="licence";
                        break;
                    case R.id.rb_all:
                        Cursor c=dbHelper.getAll(db);
                        maketUsersList(c);
                        makeListView();
                }
            }
        });
    }

/*
    public void onSearchButtonClicked(){
        btnSearch=(Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);

    }
*/

    @Override
    public void onClick(View view) {
        /*
        if(view==btnSearch){
            etSearch=(EditText)findViewById(R.id.et_search);
            String filter=etSearch.getText().toString();

            Cursor c;

            switch (searchBy){
                case "username":
                    c=dbHelper.getQueryByUserName(db,filter);
                    maketUsersList(c);
                    break;
                case "address":
                    c=dbHelper.getQueryByAddress(db,filter);
                    maketUsersList(c);
                    break;
                case "licence":
                    c=dbHelper.getQueryByLicence(db,filter.equals("1"));
                    maketUsersList(c);
                    break;
                case "all":
                    Toast.makeText(this,"Choose search by option",Toast.LENGTH_LONG).show();

            }
            makeListView();
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("id", USER_ID+"");
                startActivityForResult(intent,1);
                return true;
            case R.id.log_out:
                USER_ID=-1;
                goToLogIn();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onEtSearchChange(){
        etSearch=(EditText)findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*
                Cursor c;

                switch (searchBy){
                    case "username":
                        c=dbHelper.getQueryByUserName(db,charSequence.toString());
                        maketUsersList(c);
                        break;
                    case "address":
                        c=dbHelper.getQueryByAddress(db,charSequence.toString());
                        maketUsersList(c);
                        break;
                    case "licence":
                        c=dbHelper.getQueryByLicence(db,charSequence.toString().equals("1"));
                        maketUsersList(c);
                        break;
                    case "all":
                        Toast.makeText(MainActivity.this,"Choose search by option",Toast.LENGTH_LONG).show();

                }
                makeListView();
                */
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Cursor c;
                if(editable.toString().equals("")){
                    c=dbHelper.getAll(db);
                    maketUsersList(c);
                }
                else {
                    switch (searchBy) {
                        case "username":
                            c = dbHelper.getQueryByUserName(db, editable.toString());
                            maketUsersList(c);
                            break;
                        case "address":
                            c = dbHelper.getQueryByAddress(db, editable.toString());
                            maketUsersList(c);
                            break;
                        case "licence":
                            c = dbHelper.getQueryByLicence(db, editable.toString().equals("1"));
                            maketUsersList(c);
                            break;
                        case "all":
                            Toast.makeText(MainActivity.this, "Choose search by option", Toast.LENGTH_LONG).show();

                    }

                }
                makeListView();
            }
        });
    }




}
