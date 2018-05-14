package com.jeff.exam.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.jeff.exam.MainActivity;
import com.jeff.exam.models.Person;

import java.util.ArrayList;
import java.util.List;

public class DbAdapter {

    /******** if debug is set true then it will show all Logcat message *******/
    public static final boolean DEBUG = true;

    /******************** Logcat TAG ************/
    public static final String LOG_TAG = "DBAdapter";

    /******************** Table Fields ************/
    public static final String KEY_ID = "_id";

    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_AGE = "age";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE_NUMBER = "mobile_number";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CONTACT_PERSON = "contact_person";
    public static final String KEY_CONTACT_PERSON_MOBILE_NUMBER = "contact_person_mobile_number";



    /******************** Database Name ************/
    public static final String DATABASE_NAME = "DB_sqllite";

    /**** Database Version (Increase one if want to also upgrade your database) ***/
    public static final int DATABASE_VERSION = 1;// started at 1

    /** Table names */
    public static final String PERSON_TABLE = "tbl_person";

    /******* Set all table with comma seperated like USER_TABLE,ABC_TABLE *******/
    private static final String[ ] ALL_TABLES = { PERSON_TABLE };

    /** Create table syntax */
    private static final String PERSON_CREATE = "create table tbl_person( _id integer primary key autoincrement,first_name text not null,last_name text not null,birthday text not null,age text not null,email text not null,mobile_number text not null ,address text not null ,contact_person text not null ,contact_person_mobile_number text not null);";

    /******************** Used to open database in syncronized way ************/
    private static DataBaseHelper DBHelper = null;

    protected DbAdapter() {
    }
    /*********** Initialize database *************/
    public static void init(Context context) {
        if (DBHelper == null) {
            if (DEBUG)
                Log.i("DBAdapter", context.toString());
            DBHelper = new DataBaseHelper(context);
        }
    }

    /********** Main Database creation INNER class ********/
    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            if (DEBUG)
                Log.i(LOG_TAG, "new create");
            try {
                db.execSQL(PERSON_CREATE);


            } catch (Exception exception) {
                if (DEBUG)
                    Log.i(LOG_TAG, "Exception onCreate() exception");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (DEBUG)
                Log.w(LOG_TAG, "Upgrading database from version" + oldVersion
                        + "to" + newVersion + "...");

            for (String table : ALL_TABLES) {
                db.execSQL("DROP TABLE IF EXISTS " + table);
            }
            onCreate(db);
        }

    } // Inner class closed


    /***** Open database for insert,update,delete in syncronized manner *****/
    private static synchronized SQLiteDatabase open() throws SQLException {
        return DBHelper.getWritableDatabase();
    }


    /****************** General functions*******************/


    /********** Escape string for single quotes (Insert,Update) *******/
    private static String sqlEscapeString(String aString) {
        String aReturn = "";

        if (null != aString) {
            //aReturn = aString.replace(", );
            aReturn = DatabaseUtils.sqlEscapeString(aString);
            // Remove the enclosing single quotes ...
            aReturn = aReturn.substring(1, aReturn.length() - 1);
        }

        return aReturn;
    }


    /********* UnEscape string for single quotes (show data) *******/
    private static String sqlUnEscapeString(String aString) {

        String aReturn = "";

        if (null != aString) {
            aReturn = aString.replace("", "");
        }

        return aReturn;
    }


    /********************************************************************/

    /********* User data functons *********/

    public static void addUserData(Person uData) {
        // Open database for Read / Write

        final SQLiteDatabase db = open();

        String firstName = sqlEscapeString(uData.getFirstName());
        String lastName = sqlEscapeString(uData.getLastName());
        String birthday = sqlEscapeString(uData.getBirthday());
        String age = sqlEscapeString(uData.getAge());
        String email = sqlEscapeString(uData.getEmail());
        String mobileNumber = sqlEscapeString(uData.getMobileNumber());
        String address = sqlEscapeString(uData.getAddress());
        String contactPerson = sqlEscapeString(uData.getContactPerson());
        String contactPersonMobileNumber = sqlEscapeString(uData.getContactPersonMobileNumber());

        ContentValues cVal = new ContentValues();
        cVal.put(KEY_FIRST_NAME, firstName);
        cVal.put(KEY_LAST_NAME, lastName);
        cVal.put(KEY_BIRTHDAY, birthday);
        cVal.put(KEY_AGE, age);
        cVal.put(KEY_EMAIL, email);
        cVal.put(KEY_MOBILE_NUMBER, mobileNumber);
        cVal.put(KEY_ADDRESS, address);
        cVal.put(KEY_CONTACT_PERSON, contactPerson);
        cVal.put(KEY_CONTACT_PERSON_MOBILE_NUMBER, contactPersonMobileNumber);
        // Insert user values in database
        db.insert(PERSON_TABLE, null, cVal);
        db.close(); // Closing database connection
    }


    // Updating single data
    public static int updateUserData(Person data) {

        final SQLiteDatabase db = open();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, data.getFirstName());
        values.put(KEY_LAST_NAME, data.getLastName());
        values.put(KEY_BIRTHDAY, data.getBirthday());
        values.put(KEY_AGE, data.getAge());
        values.put(KEY_EMAIL, data.getEmail());
        values.put(KEY_MOBILE_NUMBER, data.getMobileNumber());
        values.put(KEY_ADDRESS, data.getAddress());
        values.put(KEY_CONTACT_PERSON, data.getContactPerson());
        values.put(KEY_CONTACT_PERSON_MOBILE_NUMBER, data.getContactPersonMobileNumber());

        // updating row
        return db.update(PERSON_TABLE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) });
    }

    // Getting single contact
    public static Person getPerson(int id) {

        // Open database for Read / Write
        final SQLiteDatabase db = open();

        Cursor cursor = db.query(PERSON_TABLE, new String[] { KEY_ID,
                        KEY_FIRST_NAME, KEY_LAST_NAME, KEY_BIRTHDAY, KEY_AGE, KEY_EMAIL ,KEY_MOBILE_NUMBER ,KEY_ADDRESS ,KEY_CONTACT_PERSON, KEY_CONTACT_PERSON_MOBILE_NUMBER}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);


        if (cursor != null)
            cursor.moveToFirst();

        Person data = new Person(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));

        // return user data
        return data;
    }

    // Getting All User data
    public static List<Person> getAllPerson() {

        List<Person> contactList = new ArrayList<Person>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + PERSON_TABLE;


        // Open database for Read / Write
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Person data = new Person();
                data.setId(Integer.parseInt(cursor.getString(0)));
                data.setFirstName(cursor.getString(1));
                data.setLastName(cursor.getString(2));
                data.setBirthday(cursor.getString(3));
                data.setAge(cursor.getString(4));
                data.setEmail(cursor.getString(5));
                data.setMobileNumber(cursor.getString(6));
                data.setAddress(cursor.getString(7));
                data.setContactPerson(cursor.getString(8));
                data.setContactPersonMobileNumber(cursor.getString(9));

                // Adding contact to list
                contactList.add(data);
            } while (cursor.moveToNext());
        }

        // return user list
        return contactList;
    }



    // Deleting single contact
    public static void deletePerson(Person data) {
        final SQLiteDatabase db = open();
        db.delete(PERSON_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) });
        Log.d("Name: ", "id : "+data.getId()+" deleted.");
        db.close();
    }

    // Getting dataCount

    public static int getPersonCount() {

        final SQLiteDatabase db = open();

        String countQuery = "SELECT  * FROM " + PERSON_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}
