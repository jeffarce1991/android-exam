package com.jeff.exam;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jeff.exam.database.DbAdapter;
import com.jeff.exam.models.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PersonListActivity extends AppCompatActivity {
    static ArrayList<Integer> ids = new ArrayList();
    static ArrayList<String> firstNames = new ArrayList();
    static ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        ListView listview = (ListView) findViewById(R.id.listview1);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), PersonDetailsActivity.class);
                intent.putExtra("personId", ids.get(i));
                startActivity(intent);
            }
        });

        arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, firstNames);
        listview.setAdapter(arrayAdapter);
        if (checkDataBase(this, "DB_sqllite")) {
            DbAdapter.init(PersonListActivity.this);
            // Reading all contacts
            Log.d("Reading: ", "Reading all contacts..");
            List<Person> data = DbAdapter.getAllPerson();
            for (Person dt : data) {
                String log = "Id: " + dt.getId() + " ,First Name: " + dt.getFirstName() + " ,Last Name: " + dt.getLastName() + ",Birthday: " + dt.getBirthday() + ",Age: " + dt.getAge() + ",Email: " + dt.getEmail() + ",Mobile #: " + dt.getMobileNumber() + ",Address: " + dt.getAddress() + ",Contact Person " + dt.getContactPerson() + ",Contact Person Mobile #: " + dt.getContactPersonMobileNumber();
                // Writing Contacts to log
                ids.add(dt.getId());
                firstNames.add(dt.getFirstName() + " " + dt.getLastName());
                arrayAdapter.notifyDataSetChanged();
                Log.d("DbAdapter: ", log);
            }
            Toast.makeText(this, "Cache Loaded", Toast.LENGTH_SHORT).show();
        } else {
            PersonListActivity.DownloadTask task = new PersonListActivity.DownloadTask();
            task.execute("http://www.jeffarce.x10.mx/test/api.php");
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                ArrayList<Person> persons = new ArrayList<Person>();
                //TODO STORE JSON DATAS IN ARRAY

                String personInfo = jsonObject.getString("persons");
                JSONArray arr = new JSONArray(personInfo);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String firstName = jsonPart.getString("firtname");
                    String lastName = jsonPart.getString("lastname");
                    String birthday = jsonPart.getString("birthday");
                    String age = jsonPart.getString("age");
                    String email = jsonPart.getString("email");
                    String mobilenumber = jsonPart.getString("mobilenumber");
                    String address = jsonPart.getString("address");
                    String contactPerson = jsonPart.getString("contactperson");
                    String contactPersonNumber = jsonPart.getString("contactpersonnumber");

                    persons.add(new Person(firstName, lastName, birthday, age, email, mobilenumber, address, contactPerson, contactPersonNumber));
                }
/******************* Intialize Database *************/
                DbAdapter.init(PersonListActivity.this);

                // Inserting Contacts
                for (int x = 0; x < persons.size(); x++) {
                    DbAdapter.addUserData(persons.get(x));
                }
                // Reading all contacts
                Log.d("Reading: ", "Reading all contacts..");
                List<Person> data = DbAdapter.getAllPerson();

                for (Person dt : data) {
                    String log = "Id: " + dt.getId() + " ,First Name: " + dt.getFirstName() + " ,Last Name: " + dt.getLastName() + ",Birthday: " + dt.getBirthday() + ",Age: " + dt.getAge() + ",Email: " + dt.getEmail() + ",Mobile #: " + dt.getMobileNumber() + ",Address: " + dt.getAddress() + ",Contact Person " + dt.getContactPerson() + ",Contact Person Mobile #: " + dt.getContactPersonMobileNumber();
                    // Writing Contacts to log
                    ids.add(dt.getId());
                    firstNames.add(dt.getFirstName() + " " + dt.getLastName());
                    arrayAdapter.notifyDataSetChanged();
                    Log.d("DbAdapter: ", log);
                }

                Toast.makeText(PersonListActivity.this, "Json is loaded and cached", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }



    private static boolean checkDataBase(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public boolean isConnectedInternet() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else{
            connected = false;
        }

        return connected;
    }
}
