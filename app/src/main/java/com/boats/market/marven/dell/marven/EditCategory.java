package com.boats.market.marven.dell.marven;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditCategory extends AppCompatActivity {
    ImageView proImage;
    Button saveImage;
    Button saveName;
    ImageView subImage;
    EditText subName;
    Button saveSub;
    String categoryName;

    int cart_counter;
    android.support.v7.widget.Toolbar toolbar;
    TextView cart_counter_txt;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("cart_counter", 0);
        cart_counter = sharedPreferences.getInt("count", 0);
        cart_counter_txt = toolbar.findViewById(R.id.cart_counter_txt);
        cart_counter_txt.setText("" + cart_counter);

        if (cart_counter > 0) {
            cart_counter_txt.setVisibility(View.VISIBLE);
        } else {
            cart_counter_txt.setVisibility(View.GONE);
        }
        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");
        cart_counter_txt.setTypeface(typeface);
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");
        categoryName = getIntent().getExtras().getString("name");
        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        ImageView cartimg = toolbar.findViewById(R.id.cart_icon);
        final ImageView backimg = toolbar.findViewById(R.id.back_icon);
        TextView title = toolbar.findViewById(R.id.customcategort_name);
        title.setText("EDIT");
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditCategory.this, CartActivity.class));

            }
        });
        title.setTypeface(typeface);

        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditCategory.this, SearchActivity.class));
                finish();
            }
        });


        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        ImageView camera = findViewById(R.id.camera_icon);
        proImage = findViewById(R.id.image);
        saveImage = findViewById(R.id.saveNewImage);

        Picasso.get().load(getIntent().getExtras().getString("pic")).placeholder(R.drawable.home_logo).into(proImage);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Picture.."), 0);
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditCategory.this);
                builder.setMessage("Do You Want To Save Change?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveImage.setEnabled(false);
                        saveImage.setAlpha(0.5f);
                        if (getIntent().hasExtra("index")) {
                            storageReference.child("categoryimages").child(categoryName).child("subCategory " + getIntent().getExtras().getString("index")).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    databaseReference.child("categories").child(categoryName).child("subCat").child(getIntent().getExtras().getString("index")).child("url").setValue(taskSnapshot.getDownloadUrl().toString());

                                }
                            });
                        } else {

                            storageReference.child("categoryimages").child(categoryName).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    databaseReference.child("categories").child(categoryName).child("urlAndPublish").child("url").setValue(taskSnapshot.getDownloadUrl().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {

                                                Toast.makeText(EditCategory.this, "Failed.Try Again..", Toast.LENGTH_SHORT).show();

                                            }


                                        }
                                    });
                                }
                            });


                        }
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Picasso.get().load(getIntent().getExtras().getString("pic")).placeholder(R.drawable.home_logo).into(proImage);
                        saveImage.setEnabled(false);
                        saveImage.setAlpha(0.5f);
                    }
                }).show();

            }
        });
        LinearLayout newSubLayout = findViewById(R.id.newsubLayout);
        subName = findViewById(R.id.subNameEdit);
        final Button uploadPic = findViewById(R.id.uploadPic);
        subImage = findViewById(R.id.subImage);
        saveSub = findViewById(R.id.saveNewSubCategory);
        subName.setTypeface(typeface);
        subName.setTypeface(typeface);
        uploadPic.setTypeface(typeface);
        saveSub.setTypeface(typeface);

        if (subName.getText().toString().isEmpty() || uri2 == null) {
            saveSub.setEnabled(false);
            saveSub.setAlpha(0.5f);
        } else {
            saveSub.setEnabled(true);
            saveSub.setAlpha(1);
        }




        subName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 if (subName.getText().toString().isEmpty() || uri2==null)
                 {
                     saveSub.setEnabled(false);
                     saveSub.setAlpha(0.5f);
                 }
                 else
                 {
                     saveSub.setEnabled(true);
                     saveSub.setAlpha(1);
                 }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Picture.."), 1);
            }
        });
        final ProgressBar progressBar = findViewById(R.id.progressPar);

        saveSub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                saveSub.setEnabled(false);
                saveSub.setAlpha(0.5f);
                storageReference.child("categoryimages").child(categoryName).child(subName.getText().toString()).putFile(uri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final String url = taskSnapshot.getDownloadUrl().toString();
                        databaseReference.child("categories").child(categoryName).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int key = 0;
                                if (dataSnapshot.hasChild("subCat")) {
                                    key = (int) dataSnapshot.child("subCat").getChildrenCount();
                                }
                                SubCategoryModel subCategoryModel = new SubCategoryModel(subName.getText().toString(), url);
                                dataSnapshot.child("subCat").child(String.valueOf(key)).getRef().setValue(subCategoryModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(EditCategory.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                                            subName.setText("");
                                            uri2 = null;
                                            subImage.setImageURI(null);
                                            subImage.setVisibility(View.GONE);

                                        } else {
                                            Toast.makeText(EditCategory.this, "Failed.Try again", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);

                                        }
                                    }
                                });
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditCategory.this, "Failed.Try again", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });


            }
        });
        final EditText nameEditText = findViewById(R.id.categoryname_edtext);
        saveName = findViewById(R.id.saveNewName);
        LinearLayout linearLayout = findViewById(R.id.subLayout);
        if (getIntent().hasExtra("index")) {
            linearLayout.setVisibility(View.VISIBLE);
            nameEditText.setText(getIntent().getExtras().getString("subname"));
            newSubLayout.setVisibility(View.GONE);
        }
        nameEditText.setTypeface(typeface);
        saveName.setTypeface(typeface);

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.equals("")) {
                    saveName.setEnabled(true);
                    saveName.setAlpha(1);
                } else {
                    saveName.setEnabled(false);
                    saveName.setAlpha(0.5f);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(saveName);
                AlertDialog.Builder builder = new AlertDialog.Builder(EditCategory.this);

                builder.setMessage("Do You Want To Save Change").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = nameEditText.getText().toString().trim();
                        databaseReference.child("categories").child(getIntent().getExtras().getString("name")).child("subCat").child(getIntent().getExtras().getString("index")).child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    saveName.setEnabled(false);
                                    saveName.setAlpha(0.5f);

                                } else {
                                    Toast.makeText(EditCategory.this, "Failed.Try Again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();


                saveName.setEnabled(false);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 0) {
                uri = data.getData();
                proImage.setImageURI(uri);
                saveImage.setEnabled(true);
                saveImage.setAlpha(1);
            } else if (requestCode == 1) {
                uri2 = data.getData();
                subImage.setImageURI(uri2);
                subImage.setVisibility(View.VISIBLE);
                if (!subName.getText().toString().isEmpty()) {
                    saveSub.setEnabled(true);
                    saveSub.setAlpha(1);

                } else {
                    saveSub.setEnabled(false);
                    saveSub.setAlpha(0.5f);
                }
            }
        }


    }

    Uri uri, uri2;

    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onBackPressed() {
        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));
        else
            super.onBackPressed();
    }

}
