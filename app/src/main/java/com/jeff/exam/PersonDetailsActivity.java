package com.jeff.exam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.jeff.exam.database.DbAdapter;
import com.jeff.exam.models.Person;

import java.util.List;

public class PersonDetailsActivity extends AppCompatActivity {
int personId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        Intent intent = getIntent();
        personId = intent.getIntExtra("personId",-1);
        DbAdapter.init(this);
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        Person person = DbAdapter.getPerson(personId);

        TextView txtName = (TextView)findViewById(R.id.txtName);
        txtName.setText(person.getFirstName() + " " + person.getLastName());

        TextView txtBirthday = (TextView)findViewById(R.id.txtBirthday);
        txtBirthday.setText(person.getBirthday());

        TextView txtAge = (TextView)findViewById(R.id.txtAge);
        txtAge.setText(person.getAge() + " Years old");

        TextView txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtEmail.setText(person.getEmail());

        TextView txtMobileNumber = (TextView)findViewById(R.id.txtMobileNumber);
        txtMobileNumber.setText(person.getMobileNumber());

        TextView txtAddress = (TextView)findViewById(R.id.txtAddress);
        txtAddress.setText(person.getAddress());

        TextView txtContactPerson = (TextView)findViewById(R.id.txtContactPerson);
        txtContactPerson.setText(person.getContactPerson());

        TextView txtContactPersonMobileNumber = (TextView)findViewById(R.id.txtContactPersonMobileNumber);
        txtContactPersonMobileNumber.setText(person.getContactPersonMobileNumber());
    }
}
