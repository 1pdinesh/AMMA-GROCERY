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
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

public class AdminCategory extends AppCompatActivity {

    ListView listView;
    String mTitle[] = {"Vegetable", "Spices", "Fish", "Sundry"};
    String mDescription[] = {"Includes Fruits", "Babas and many more", "Meats ,fish and chicken", "all dry items"};
    int images[] = {R.drawable.fruits, R.drawable.spice, R.drawable.fish, R.drawable.baby};

    private Button LogoutBtn, maintainProductsBtn, CheckOrdersBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        listView = findViewById(R.id.listView);

        CheckOrdersBtn = (Button) findViewById(R.id.check_orders_btn);
        maintainProductsBtn = (Button) findViewById(R.id.maintain_btn);

        MyAdapter adapter = new MyAdapter(this, mTitle, mDescription, images);
        listView.setAdapter(adapter);


        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategory.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });



        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategory.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==  0) {
                    Intent intent=new Intent(AdminCategory.this,AdminAdd.class);
                    intent.putExtra("Category","Vegetable");
                    startActivity(intent);
                }
                if (position == 1 ) {
                    Intent intent=new Intent(AdminCategory.this,AdminAdd.class);
                    intent.putExtra("Category","Spices");
                    startActivity(intent);
                }
                if (position ==  2) {
                    Intent intent=new Intent(AdminCategory.this,AdminAdd.class);
                    intent.putExtra("Category","Fish");
                    startActivity(intent);                }
                if (position ==  3) {
                    Intent intent=new Intent(AdminCategory.this,AdminAdd.class);
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
            super(c, R.layout.row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
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
