package com.terry.fireapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.inputTitle)
    EditText inputTitle;
    @BindView(R.id.inputName)
    EditText inputName;
    @BindView(R.id.inputYear)
    EditText inputYear;
    @BindView(R.id.inputCost)
    EditText inputCost;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Saving .....");

    }

    @OnClick(R.id.buttonSave)
    public void  save(){

        if (! new Network().isInternetAvailable())
        {
            Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        String title = inputTitle.getText().toString().trim();
        String author = inputName.getText().toString().trim();
        String cost = inputCost.getText().toString().trim();
        String year = inputYear.getText().toString().trim();
        if (title.isEmpty() || author.isEmpty() || cost.isEmpty() || year.isEmpty())
        {
            Toast.makeText(this, "Fill in all Values", Toast.LENGTH_SHORT).show();
            return;

        }
        Map<String,String>map = new HashMap<>();
        map.put("title",title);
        map.put("author",author);
        map.put("year",year);
        map.put("cost",cost);

        DatabaseReference db= FirebaseDatabase.getInstance().getReference();

        progressDialog.show();
        db.push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                inputName.setText("");
                inputTitle.setText("");
                inputYear.setText("");
                inputCost.setText("");
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Fail.Try Again", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @OnClick (R.id.buttonFetch)
    public  void fetch() {
        Intent x = new Intent(this, FetchActivity.class);
        startActivity(x);

    }

    }

