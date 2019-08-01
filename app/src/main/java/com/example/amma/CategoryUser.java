package com.example.amma;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryUser extends AppCompatActivity {

    ListView listView;
    String mTitle[] = {"Vegetable", "Spices", "Fish", "Sundry"};
    String mDescription[] = {"Includes Fruits", "Babas and many more", "Meats ,fish and chicken", "all dry items"};
    int images[] = {R.drawable.fruits, R.drawable.spice, R.drawable.fish, R.drawable.baby};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_user);
        listView = findViewById(R.id.listView);




       CategoryUser.MyAdapter adapter = new CategoryUser.MyAdapter(this, mTitle, mDescription, images);
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==  0) {
                    Intent intent=new Intent(CategoryUser.this,HomeActivity.class);
                    intent.putExtra("Category","Vegetable");
                    startActivity(intent);
                }
                if (position == 1 ) {
                    Intent intent=new Intent(CategoryUser.this,HomeActivity.class);
                    intent.putExtra("Category","Spices");
                    startActivity(intent);
                }
                if (position ==  2) {
                    Intent intent=new Intent(CategoryUser.this,HomeActivity.class);
                    intent.putExtra("Category","Fish");
                    startActivity(intent);                }
                if (position ==  3) {
                    Intent intent=new Intent(CategoryUser.this,AdminAdd.class);
                    intent.putExtra("Category","Sundry");
                    startActivity(intent);                }

            }
        });

    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rDescription[];
        int rImgs[];

        MyAdapter (Context c, String title[], String description[], int imgs[]) {
            super(c, R.layout.row_user, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_user, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.textView1);
            TextView myDescription = row.findViewById(R.id.textView2);


            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);




            return row;
        }
    }
}

