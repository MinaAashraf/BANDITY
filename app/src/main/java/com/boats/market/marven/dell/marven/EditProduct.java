package com.boats.market.marven.dell.marven;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EditProduct extends AppCompatActivity {
    int index, index2, i;
    ImageView extenralImage;
    ProductModel model;
    RelativeLayout relativeLayout;
    String subName;
    DatabaseReference databaseReference;
    StorageReference storageReference;

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
        setContentView(R.layout.activity_edit_product);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        ImageView cartimg = toolbar.findViewById(R.id.cart_icon);
        final ImageView backimg = toolbar.findViewById(R.id.back_icon);
        TextView title = toolbar.findViewById(R.id.customcategort_name);
        cart_counter_txt = toolbar.findViewById(R.id.cart_counter_txt);


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
                startActivity(new Intent(EditProduct.this, CartActivity.class));

            }
        });
        title.setTypeface(typeface);


        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProduct.this, SearchActivity.class));
                finish();
            }
        });

        model = (ProductModel) getIntent().getExtras().get("pro");
        final EditText proName = findViewById(R.id.pro_name);
        final EditText proPrice = findViewById(R.id.pro_price);
        final EditText proSale = findViewById(R.id.pro_sale);
        Spinner removeColor_Spinner = findViewById(R.id.remove_color_spinner);
        Button removeColor_btn = findViewById(R.id.remove_color_btn);
        final EditText addColor_edittext = findViewById(R.id.add_color_edittext);
        final Button addColor_btn = findViewById(R.id.add_color_btn);
        Spinner removeSize_Spinner = findViewById(R.id.remove_size_spinner);
        Button removeSize_btn = findViewById(R.id.remove_size_btn);
        final EditText addSize_edittext = findViewById(R.id.add_size_edittext);
        Button addSize_btn = findViewById(R.id.add_size_btn);
        extenralImage = findViewById(R.id.ext_pro_pic);
        ImageView edit = findViewById(R.id.edit);
        relativeLayout = findViewById(R.id.relativeLayout);
        final EditText desciption = findViewById(R.id.description_edittext);
        final Button saveAll = findViewById(R.id.finalSave);
        final Button clearAll = findViewById(R.id.finalClear_btn);

        proName.setTypeface(typeface);
        proPrice.setTypeface(typeface);
        proSale.setTypeface(typeface);
        removeColor_btn.setTypeface(typeface);
        addColor_edittext.setTypeface(typeface);
        addColor_btn.setTypeface(typeface);
        removeSize_btn.setTypeface(typeface);
        addSize_edittext.setTypeface(typeface);
        addSize_btn.setTypeface(typeface);

        desciption.setTypeface(typeface);
        saveAll.setTypeface(typeface);
        clearAll.setTypeface(typeface);

        proName.setText(model.getSort());
        proPrice.setText(model.getPrice());
        proSale.setText(model.getSale());
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, model.getColores());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        removeColor_Spinner.setAdapter(adapter);
        removeColor_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                index = 0;
            }
        });


        removeColor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProduct.this);
                builder.setMessage("Do You Want To Remove This Color '" + model.getColores().get(index) + "' ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                model.getColores().remove(index);
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });

        addColor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addColor_edittext.setError(null);
                if (addColor_edittext.getText().toString().isEmpty()) {
                    addColor_edittext.setError("Enter Color");
                    addColor_edittext.requestFocus();
                } else {
                    if (model.getColores().contains(addColor_edittext.getText().toString().trim())) {
                        addColor_edittext.setError("Already exist!!");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProduct.this);
                        builder.setMessage("Do You Want To Add This Color '" + addColor_edittext.getText().toString() + "' ?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        model.getColores().add(addColor_edittext.getText().toString());
                                        addColor_edittext.setText("");
                                        adapter.notifyDataSetChanged();
                                    }
                                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                    }
                }
            }
        });

        final ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, model.getSizes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        removeSize_Spinner.setAdapter(adapter2);
        removeSize_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                index2 = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                index2 = 0;
            }
        });


        removeSize_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProduct.this);
                builder.setMessage("Do You Want To Remove This Size '" + model.getSizes().get(index2) + "' ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                model.getSizes().remove(index2);
                                adapter2.notifyDataSetChanged();
                            }
                        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });

        addSize_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSize_edittext.setError(null);
                if (addSize_edittext.getText().toString().isEmpty()) {
                    addSize_edittext.setError("Enter Size");
                    addSize_edittext.requestFocus();
                } else {
                    if (model.getSizes().contains(addSize_edittext.getText().toString().trim())) {
                        addSize_edittext.setError("Already exist!!");
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProduct.this);
                        builder.setMessage("Do You Want To Add This Size '" + addSize_edittext.getText().toString() + "' ?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        model.getSizes().add(addSize_edittext.getText().toString());
                                        addSize_edittext.setText("");
                                        adapter2.notifyDataSetChanged();
                                    }
                                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                    }

                }
            }
        });

        desciption.setText(model.getDescription());


        Picasso.get().load(model.getUrl()).into(extenralImage);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 2);
            }
        });


        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(clearAll);
                model = (ProductModel) getIntent().getExtras().get("pro");
                adapter.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
                desciption.setText(model.getDescription());
                proName.setText(model.getSort());
                proSale.setText(model.getSale());
                proPrice.setText(model.getPrice());
                Picasso.get().load(model.getUrl()).into(extenralImage);
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        if (getIntent().hasExtra("subName")) {
            subName = getIntent().getExtras().getString("subName");
            storageReference = storageReference.child("products").child(model.getCategoryName()).child(subName).child(model.getSort());
        } else {
            subName = "none";
            storageReference = storageReference.child("products").child(model.getCategoryName()).child(model.getSort());
        }

        if (!model.getSubkey().equals("none")) {
            databaseReference = databaseReference.child("categories").child(model.getCategoryName()).child("subCat").child(model.getSubkey()).child("products").child(model.getSort());
        } else {
            databaseReference = databaseReference.child("categories").child(model.getCategoryName()).child("products").child(model.getSort());

        }

        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();

        saveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(saveAll);
                boolean b = true;
                if (proPrice.getText().toString().isEmpty()) {
                    proPrice.setError("Required");
                    proPrice.requestFocus();
                    b = false;
                }
                if (desciption.getText().toString().isEmpty()) {
                    desciption.setError("Required");
                    desciption.requestFocus();
                    b = false;
                }
                if (model.getSizes().isEmpty()) {
                    addSize_edittext.setError("Enter one size at least");
                    addSize_edittext.requestFocus();
                    b = false;
                }
                if (model.getColores().isEmpty()) {
                    addColor_edittext.setError("Enter one color at least");
                    addColor_edittext.requestFocus();
                    b = false;
                }


                if (b) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProduct.this);
                    builder.setTitle("Edit").setMessage("Do you want to save changes ?").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String price = proPrice.getText().toString();
                            model.setPrice(price);
                            model.price2 = Integer.valueOf(price);
                            model.negativeprice2 = -1 * Integer.valueOf(price);
                            model.price2 = Float.valueOf(proPrice.getText().toString());
                            model.setSale(proSale.getText().toString());
                            model.setDescription(desciption.getText().toString());
                            if (uri2 != null) {
                                storageReference.child("ext_img").putFile(uri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        model.setUrl(taskSnapshot.getDownloadUrl().toString());
                                        if (!model.getSubkey().equals("none")) {
                                            databaseReference1.child("categories").child(model.getCategoryName()).child("subCat").child(model.getSubkey()).child("products").child(model.getSort()).setValue(model);
                                        } else {
                                            databaseReference1.child("categories").child(model.getCategoryName()).child("products").child(model.getSort()).setValue(model);
                                        }


                                        databaseReference1.child("Release").child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild(proName.getText().toString())) {
                                                    dataSnapshot.child(proName.getText().toString()).getRef().setValue(model);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        databaseReference1.child("Sale").child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild(proName.getText().toString())) {
                                                    if (!proSale.getText().toString().isEmpty())
                                                        dataSnapshot.child(proName.getText().toString()).getRef().setValue(model);
                                                    else
                                                        dataSnapshot.child(proName.getText().toString()).getRef().removeValue();
                                                } else {
                                                    if (!proSale.getText().toString().isEmpty())
                                                        dataSnapshot.child(proName.getText().toString()).getRef().setValue(model);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        databaseReference1.child("Monaspa").child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild(proName.getText().toString())) {
                                                    dataSnapshot.child(proName.getText().toString()).getRef().setValue(model);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        Toast.makeText(EditProduct.this, "Changes applied successfully", Toast.LENGTH_SHORT).show();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditProduct.this, e.getMessage() + "Try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {

                                if (!model.getSubkey().equals("none")) {
                                    databaseReference1.child("categories").child(model.getCategoryName()).child("subCat").child(model.getSubkey()).child("products").child(model.getSort()).setValue(model);
                                } else {
                                    databaseReference1.child("categories").child(model.getCategoryName()).child("products").child(model.getSort()).setValue(model);
                                }


                                databaseReference1.child("Release").child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(proName.getText().toString())) {
                                            dataSnapshot.child(proName.getText().toString()).getRef().setValue(model);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                databaseReference1.child("Sale").child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(proName.getText().toString())) {
                                            if (!proSale.getText().toString().isEmpty())
                                                dataSnapshot.child(proName.getText().toString()).getRef().setValue(model);
                                            else
                                                dataSnapshot.child(proName.getText().toString()).getRef().removeValue();
                                        } else {
                                            if (!proSale.getText().toString().isEmpty())
                                                dataSnapshot.child(proName.getText().toString()).getRef().setValue(model);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                databaseReference1.child("Monaspa").child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild(proName.getText().toString())) {
                                            dataSnapshot.child(proName.getText().toString()).getRef().setValue(model);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Toast.makeText(EditProduct.this, "Changes is saved", Toast.LENGTH_SHORT).show();


                            }

                        }
                    }).show();

                }


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            uri2 = data.getData();
            extenralImage.setImageURI(uri2);
        }
    }

    Uri uri2;

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
        else {
           super.onBackPressed();
        }

    }
}
