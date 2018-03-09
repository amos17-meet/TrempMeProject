package com.example.student.trempmeproject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DBName";
    private static final int DATABASE_VERSION = 4;

    private static final String TREMPIST_TABLE = "tbl_trempist";

    private static final String ID_COLUMN = "id";

    private static final String USER_NAME_COLUMN="user_name";
    private static final String DRIVING_LICENCE_COLUMN="driving_licence";
    private static final String ADDRESS_COLUMN="address";
    private static final String PASSWORD_COLUMN="password";
    private static final String IMAGE_COLUMN="image";


    private static final String[] cols=new String[]{ID_COLUMN,USER_NAME_COLUMN,DRIVING_LICENCE_COLUMN,ADDRESS_COLUMN,PASSWORD_COLUMN,IMAGE_COLUMN};

    private static final String CREATE_TREMPIST_TABLE = "CREATE TABLE "
            + TREMPIST_TABLE + "(" + ID_COLUMN + " INTEGER PRIMARY KEY,"
            + USER_NAME_COLUMN + " TEXT, " + DRIVING_LICENCE_COLUMN + " INTEGER,"
            +ADDRESS_COLUMN+ " TEXT,"+PASSWORD_COLUMN+ " TEXT,"+IMAGE_COLUMN+" BLOB)";


    public SQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("Message", "in on create-sqlite helper");
        sqLiteDatabase.execSQL(CREATE_TREMPIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TREMPIST_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void deleteAll(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.delete(TREMPIST_TABLE, null, null);
    }

    public void insert(SQLiteDatabase sqLiteDatabase, String userName, int drivingLicence, String address, String password, byte[] image){
        ContentValues insertValues = new ContentValues();
        int newId=getNewId(getAll(sqLiteDatabase));
        insertValues.put(ID_COLUMN,newId);
        insertValues.put(USER_NAME_COLUMN,userName);
        insertValues.put(DRIVING_LICENCE_COLUMN,drivingLicence);
        insertValues.put(ADDRESS_COLUMN,address);
        insertValues.put(PASSWORD_COLUMN,password);
        insertValues.put(IMAGE_COLUMN,image);
        sqLiteDatabase.insert(TREMPIST_TABLE,null,insertValues);

    }

    public void deleteUser(SQLiteDatabase sqLiteDatabase,int id) {
        String[] value=new String[]{id+""};
        sqLiteDatabase.delete(TREMPIST_TABLE, ID_COLUMN+" LIKE ?",value );
    }

    public Cursor getAll(SQLiteDatabase sqLiteDatabase){
        Cursor c= sqLiteDatabase.query(true,TREMPIST_TABLE,cols,null
                , null, null, null, null, null);

        if(c.getCount()>0) {
            c.moveToFirst();
            return c;
        }

        return c;
    }

    private int getNewId(Cursor c){
        int max=0;
        if (c.getCount()==0){
            return max;
        }
        else {
            while (!c.isAfterLast()) {
                if(getRowId(c)>=max){
                    max=getRowId(c)+1;
                }
                c.moveToNext();
            }
        }
        return max;
    }

    public int getRowId(Cursor c){
        return c.getInt(c.getColumnIndex(ID_COLUMN));
    }

    public String getRowUserName(Cursor c){
        return c.getString(c.getColumnIndex(USER_NAME_COLUMN));
    }

    public int hasDrivingLicence(Cursor c){
        return c.getInt(c.getColumnIndex(DRIVING_LICENCE_COLUMN));
    }

    public String getRowAddress(Cursor c){
        return c.getString(c.getColumnIndex(ADDRESS_COLUMN));
    }

    public String getRowPassword(Cursor c){
        return c.getString(c.getColumnIndex(PASSWORD_COLUMN));
    }

    public byte[] getRowImage(Cursor c){
        return c.getBlob(c.getColumnIndex(IMAGE_COLUMN));
    }


    public Cursor getQueryById(SQLiteDatabase sqLiteDatabase,int id){
        Cursor c=sqLiteDatabase.query(true,TREMPIST_TABLE,cols,ID_COLUMN+" LIKE "+id,null,null,null,null,null);
        if(c.getCount()>0){
            c.moveToFirst();
            return c;
        }
        return c;
    }

    public Cursor getQueryByUserName(SQLiteDatabase sqLiteDatabase,String userName){
        String[] value=new String[]{userName};
        Cursor c=sqLiteDatabase.query(true,TREMPIST_TABLE,cols,USER_NAME_COLUMN+" LIKE ?",value,null,null,null,null);
        if(c.getCount()>0){
            c.moveToFirst();
            return c;
        }
        return c;
    }

    public Cursor getQueryByLicence(SQLiteDatabase sqLiteDatabase,boolean hasLicence){
        int licence;
        if(hasLicence)licence=1;
        else licence=0;
        Cursor c=sqLiteDatabase.query(true,TREMPIST_TABLE,cols,DRIVING_LICENCE_COLUMN+"="+licence,null,null,null,null,null);
        if(c.getCount()>0){
            c.moveToFirst();
            return c;
        }
        return c;
    }

    public Cursor getQueryByAddress(SQLiteDatabase sqLiteDatabase,String addresse){
        String[] value=new String[]{addresse};
        Cursor c=sqLiteDatabase.query(true,TREMPIST_TABLE,cols,ADDRESS_COLUMN+" LIKE ?",value,null,null,null,null);
        if(c.getCount()>0){
            c.moveToFirst();
            return c;
        }
        return c;
    }

    public Cursor getQueryByPassword(SQLiteDatabase sqLiteDatabase,String password){
        String[] value=new String[]{password};
        Cursor c=sqLiteDatabase.query(true,TREMPIST_TABLE,cols,PASSWORD_COLUMN+" LIKE ?",value,null,null,null,null);
        if(c.getCount()>0){
            c.moveToFirst();
            return c;
        }
        return c;
    }

    public int matchUsernameToPassword( SQLiteDatabase sqLiteDatabase, String username, String password){
        Cursor userCursor=getQueryByUserName(sqLiteDatabase,username);
        Cursor passwordCursor=getQueryByPassword(sqLiteDatabase, password);
        if(userCursor.getCount()==0||passwordCursor.getCount()==0) {
            Log.e("DEBAD", "match password to usernamr: no username or password that requested");
            return -1;
        }
        else{
            while (!userCursor.isAfterLast()){
                String userCursorPassword=getRowPassword(userCursor);
                String userCursorUsername=getRowUserName(userCursor);
                while (!passwordCursor.isAfterLast()){
                    String passwordCursorPassword=getRowPassword(passwordCursor);
                    String passwordCursorUsername=getRowUserName(passwordCursor);
                    if(passwordCursorPassword.compareTo(userCursorPassword)==0) {
                        if(userCursorUsername.compareTo(passwordCursorUsername)==0){
                            return getRowId(passwordCursor);
                        }
                    }
                    passwordCursor.moveToNext();
                }
                userCursor.moveToNext();
            }
        }
        return -1;
    }

    public void setUserNameColumn(SQLiteDatabase sqLiteDatabase, String username, int userId){
        ContentValues newValues = new ContentValues();
        newValues.put(USER_NAME_COLUMN, username);
        sqLiteDatabase.update(TREMPIST_TABLE, newValues, "id="+userId, null);
    }

    public void setAddressColumn(SQLiteDatabase sqLiteDatabase, String address, int userId){
        ContentValues newValues = new ContentValues();
        newValues.put(ADDRESS_COLUMN, address);

        sqLiteDatabase.update(TREMPIST_TABLE, newValues, "id="+userId, null);
    }

    public void setDrivingLicenceColumn(SQLiteDatabase sqLiteDatabase, int hasDrivingLicence, int userId){
        ContentValues newValues = new ContentValues();
        newValues.put(DRIVING_LICENCE_COLUMN, hasDrivingLicence);

        sqLiteDatabase.update(TREMPIST_TABLE, newValues, "id="+userId, null);
    }

    public void setPasswordColumn(SQLiteDatabase sqLiteDatabase, String password, int userId){
        ContentValues newValues = new ContentValues();
        newValues.put(PASSWORD_COLUMN, password);

        sqLiteDatabase.update(TREMPIST_TABLE, newValues, "id="+userId, null);
    }

    public void setImgColumn(SQLiteDatabase sqLiteDatabase, byte [] img, int userId){
        ContentValues newValues = new ContentValues();
        newValues.put(IMAGE_COLUMN, img);

        sqLiteDatabase.update(TREMPIST_TABLE, newValues, "id="+userId, null);
    }







}

