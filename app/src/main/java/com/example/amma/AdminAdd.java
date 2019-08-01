package com.example.amma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAdd extends AppCompatActivity {

    private String CategoryN,Pname,Description,SaveCD,SaveT;
    private EditText ProductN,ProductD;
    private Button AddP;
    private ImageView ImageP;
    private final static int GalleryPick=1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        CategoryN=getIntent().getExtras().get("Category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        AddP=findViewById(R.id.add_new_product);
        ImageP=findViewById(R.id.select_product_image);
        ProductD=findViewById(R.id.product_description);
        ProductN=findViewById(R.id.product_name);
        loadingBar = new ProgressDialog(this);

        ImageP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        AddP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate();
            }
        });





    }

    private void Validate()
    {
     Pname=ProductN.getText().toString();
     Description=ProductD.getText().toString();

     if(ImageUri==null)
     {
         Toast.makeText(AdminAdd.this,"Product Image Is Required",Toast.LENGTH_SHORT).show();

     }
     else if (TextUtils.isEmpty(Pname))
     {
         Toast.makeText(AdminAdd.this,"Product Name Is Required",Toast.LENGTH_SHORT).show();
     }
     else if (TextUtils.isEmpty(Description))
     {
         Toast.makeText(AdminAdd.this,"Product Description Is Required",Toast.LENGTH_SHORT).show();
     }
     else
     {
         StoreProductInfo();
     }


    }

    private void StoreProductInfo()
    {
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar Cal=Calendar.getInstance();

        SimpleDateFormat CurrentD=new SimpleDateFormat("yyyy-MM-dd");
        SaveCD =CurrentD.format(Cal.getTime());

        SimpleDateFormat CurrentT=new SimpleDateFormat("HH:mm:ss a");
        SaveT =CurrentT.format(Cal.getTime());

        productRandomKey = SaveCD + SaveT;

        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AdminAdd.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminAdd.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();



                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", SaveT);
        productMap.put("time", SaveCD);
        productMap.put("description", Description);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", CategoryN);
        productMap.put("pname", Pname);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminAdd.this, AdminCategory.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAdd.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAdd.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void OpenGallery() {

        Intent Gallery=new Intent();
        Gallery.setAction(Intent.ACTION_GET_CONTENT);
        Gallery.setType("image/*");
        startActivityForResult(Gallery,GalleryPick);

    }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
   {
       super.onActivityResult(requestCode, resultCode, data);

       if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
           ImageUri=data.getData();
           ImageP.setImageURI(ImageUri);



       }
   }



}


