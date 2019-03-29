package com.terry.fireapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FetchActivity extends AppCompatActivity {
    @BindView(R.id.books_list)
    ListView books_list;

    BaseAdapter adapter;

    ArrayList<Book> books_array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);
        ButterKnife.bind(this);
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return books_array.size();
            }

            @Override
            public Object getItem(int position) {
                return books_array.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                {
                  convertView= LayoutInflater.from(getBaseContext()).inflate(R.layout.book_item,null);
                }
                TextView txtTitle = convertView.findViewById(R.id.textviewTitle);
                TextView txtAuthor = convertView.findViewById(R.id.textViewAuthor);
                TextView txtYear = convertView.findViewById(R.id.textViewYear);
                TextView txtCost = convertView.findViewById(R.id.textViewCost);

                ImageView imgDelete = convertView.findViewById(R.id.imageViewDelete);

            final     Book b = books_array.get(position);

                txtAuthor.setText(b.getAuthor());
                txtTitle.setText(b.getTitle());
                txtCost.setText(b.getCost());
                txtYear.setText(b.getYear());

                imgDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child(b.id);
                        db.setValue(null);

                        books_array.remove(b);
                        notifyDataSetChanged();
                    }
                });

                return convertView;
            }
        };
        books_list.setAdapter(adapter);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren())
                {
                    Book k = s.getValue(Book.class);
                    k.id = s.getKey();
                    books_array.add(k);
                }
                adapter.notifyDataSetChanged();//refresh adapter

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(FetchActivity.this, "Failed To Fetch", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
