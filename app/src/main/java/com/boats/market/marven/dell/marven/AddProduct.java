package com.boats.market.marven.dell.marven;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class AddProduct extends AppCompatActivity {
    RelativeLayout relativeLayout;
    boolean b;

    ArrayList<Uri> images = new ArrayList<>();
    Adapter adapter;
    int i;
    ImageView ext_img;
    boolean save1, save2;
    String categoryName, subCategoryName;
    StorageReference storageReference;
    ProgressBar progressBar;
    String url;
    int j;
    NestedScrollView scrollView;
    Typeface typeface;

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

    class Adapter extends ArrayAdapter {

        public Adapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.image_item, parent, false);
            ImageView imageView = view.findViewById(R.id.image);
            imageView.setImageURI(images.get(position));
            ImageView close = view.findViewById(R.id.close);
            ImageView edit = view.findViewById(R.id.edit);


            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    images.remove(position);
                    notifyDataSetChanged();
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i = position;
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 1);
                    notifyDataSetChanged();
                }
            });

            return view;
        }
    }

    int colorsnumbers, sizenumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");

        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);

        ImageView cartimg = toolbar.findViewById(R.id.cart_icon);
        ImageView backimg = toolbar.findViewById(R.id.back_icon);
        TextView title = toolbar.findViewById(R.id.customcategort_name);
        title.setText("ADD PRODUCT");

        cart_counter_txt = toolbar.findViewById(R.id.cart_counter_txt);


        cartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddProduct.this, CartActivity.class));
            }
        });
        title.setTypeface(typeface);


        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ImageView searchIcon = toolbar.findViewById(R.id.search_icon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddProduct.this, SearchActivity.class));
                finish();
            }
        });
        final EditText proName = findViewById(R.id.pro_name);
        final EditText proPrice = findViewById(R.id.pro_price);
        final EditText proSale = findViewById(R.id.pro_sale);
        final Button addPicture = findViewById(R.id.addPro_picture);
        final ListView imagesList = findViewById(R.id.images_list);
        final Button add_external_img = findViewById(R.id.add_external_image);
        relativeLayout = findViewById(R.id.relativeLayout);
        ext_img = findViewById(R.id.ext_pro_pic);
        final ImageView edit = findViewById(R.id.edit);
        final EditText colorNum = findViewById(R.id.colornum_edit);
        final EditText colorName = findViewById(R.id.colorname_edit);
        final Button numSave = findViewById(R.id.numSave);
        final Button nameSave = findViewById(R.id.nameSave);
        final ArrayList<String> colors = new ArrayList<>();
        final Button saveColors = findViewById(R.id.saveColors);
        Button clearColors = findViewById(R.id.clearColors);
        scrollView = findViewById(R.id.scrollview);

        final EditText sizeNum = findViewById(R.id.sizenum_edit);
        final EditText sizeName = findViewById(R.id.sizename_edit);
        final Button numSaveSize = findViewById(R.id.numSaveSize);
        final Button nameSaveSize = findViewById(R.id.nameSaveSize);
        final Button saveSizes = findViewById(R.id.saveSizes);
        Button clearSizes = findViewById(R.id.clearSizes);
        final EditText desciption = findViewById(R.id.description_edittext);
        final Button saveToltal = findViewById(R.id.finalSave);
        final Button clearToltal = findViewById(R.id.finalClear_btn);

        proName.setTypeface(typeface);
        proPrice.setTypeface(typeface);
        proSale.setTypeface(typeface);
        addPicture.setTypeface(typeface);
        add_external_img.setTypeface(typeface);
        colorNum.setTypeface(typeface);
        colorName.setTypeface(typeface);
        numSave.setTypeface(typeface);
        nameSave.setTypeface(typeface);
        saveColors.setTypeface(typeface);
        clearColors.setTypeface(typeface);
        sizeNum.setTypeface(typeface);
        sizeName.setTypeface(typeface);
        numSaveSize.setTypeface(typeface);
        nameSaveSize.setTypeface(typeface);
        saveSizes.setTypeface(typeface);
        clearSizes.setTypeface(typeface);
        desciption.setTypeface(typeface);
        saveToltal.setTypeface(typeface);
        clearToltal.setTypeface(typeface);


        final Spinner categorySpinner = findViewById(R.id.category_spinner);
        final Spinner subSpinner = findViewById(R.id.sub_spinner);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final ArrayList<String> categories = new ArrayList<>();
        final ArrayList<String> subcategories = new ArrayList<>();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressBar = findViewById(R.id.progbar);


        numSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (colorNum.getText().toString().isEmpty()) {
                    colorNum.setError("Required");
                    colorNum.requestFocus();
                } else if (colorNum.getText().toString().contains(",") || Integer.valueOf(colorNum.getText().toString()) <= 0) {
                    colorNum.setError("Invalid Number");
                    colorNum.requestFocus();
                } else {
                    colorNum.setEnabled(false);
                    numSave.setEnabled(false);
                    colorName.setEnabled(true);
                    nameSave.setEnabled(true);
                    colorName.requestFocus();
                    colorsnumbers = Integer.valueOf(colorNum.getText().toString());
                    Toast.makeText(AddProduct.this, "Enter " + colorNum.getText().toString() + " Color(s) Please", Toast.LENGTH_SHORT).show();

                }
            }
        });
        nameSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (colorName.getText().toString().isEmpty()) {
                    colorName.setError("Required");
                    colorName.requestFocus();

                } else if (colors.contains(colorName.getText().toString())) {
                    colorName.setError("Repeated color !!");
                    colorName.requestFocus();
                } else {
                    colors.add(colorName.getText().toString());
                    colorName.setText("");
                    Toast.makeText(AddProduct.this, "Saved", Toast.LENGTH_SHORT).show();
                    if (colors.size() == colorsnumbers) {
                        colorName.setEnabled(false);
                        nameSave.setEnabled(false);
                        saveColors.setEnabled(true);
                        saveColors.setAlpha(1);
                    }

                }
            }
        });

        clearColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorNum.setText("");
                colorNum.setEnabled(true);
                numSave.setEnabled(true);
                colorName.setEnabled(false);
                nameSave.setEnabled(false);
                colorName.setText("");
                colors.clear();
                saveColors.setEnabled(false);
                saveColors.setAlpha(0.5f);
                save1 = false;
                Toast.makeText(AddProduct.this, "Sizes Is Cleared", Toast.LENGTH_SHORT).show();

            }
        });

        saveColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorNum.setEnabled(false);
                numSave.setEnabled(false);
                colorName.setEnabled(false);
                nameSave.setEnabled(false);
                Toast.makeText(AddProduct.this, "Colors Is Saved", Toast.LENGTH_SHORT).show();
                saveColors.setEnabled(false);
                saveColors.setAlpha(0.5f);
                save1 = true;
            }
        });


        numSaveSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sizeNum.getText().toString().isEmpty()) {
                    sizeNum.setError("Required");
                    sizeNum.requestFocus();
                } else if (sizeNum.getText().toString().contains(",") || Integer.valueOf(sizeNum.getText().toString()) <= 0) {
                    sizeNum.setError("Invalid Number");
                    sizeNum.requestFocus();
                } else {
                    sizeNum.setEnabled(false);
                    numSaveSize.setEnabled(false);
                    sizeName.setEnabled(true);
                    nameSaveSize.setEnabled(true);

                    sizeName.requestFocus();
                    sizenumbers = Integer.valueOf(sizeNum.getText().toString());
                    Toast.makeText(AddProduct.this, "Enter " + sizeNum.getText().toString() + " Size(s) Please", Toast.LENGTH_SHORT).show();

                }
            }
        });

        final ArrayList<String> sizes = new ArrayList<>();

        nameSaveSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sizeName.getText().toString().isEmpty()) {
                    sizeName.setError("Required");
                    sizeName.requestFocus();
                } else if (sizes.contains(sizeName.getText().toString())) {
                    sizeName.setError("Repeated size !!");
                    sizeName.requestFocus();
                } else {
                    sizes.add(sizeName.getText().toString());
                    sizeName.setText("");
                    Toast.makeText(AddProduct.this, "Saved", Toast.LENGTH_SHORT).show();
                    if (sizes.size() == sizenumbers) {
                        sizeName.setEnabled(false);
                        nameSaveSize.setEnabled(false);
                        saveSizes.setEnabled(true);
                        saveSizes.setAlpha(1);
                    }

                }
            }
        });

        clearSizes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeNum.setText("");
                sizeNum.setEnabled(true);
                numSaveSize.setEnabled(true);
                sizeName.setEnabled(false);
                nameSaveSize.setEnabled(false);
                sizeName.setText("");
                sizes.clear();
                saveSizes.setEnabled(false);
                saveSizes.setAlpha(0.5f);
                Toast.makeText(AddProduct.this, "Sizes Is Cleared", Toast.LENGTH_SHORT).show();
                save2 = false;

            }
        });

        saveSizes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeNum.setEnabled(false);
                numSaveSize.setEnabled(false);
                sizeName.setEnabled(false);
                nameSaveSize.setEnabled(false);
                saveSizes.setAlpha(0.5f);
                Toast.makeText(AddProduct.this, "Sizes Is Saved", Toast.LENGTH_SHORT).show();
                save2 = true;

            }
        });


        databaseReference.child("categories").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                categories.add(dataSnapshot.getKey());
                ArrayAdapter categoryAdapter = new ArrayAdapter(AddProduct.this, android.R.layout.simple_spinner_item, categories);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(categoryAdapter);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subcategories.clear();
                categoryName = categories.get(i);
                subSpinner.setEnabled(true);
                databaseReference.child("categories").child(categoryName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild("subCat")) {
                            databaseReference.child("categories").child(categoryName).child("subCat").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    SubCategoryModel model = dataSnapshot.getValue(SubCategoryModel.class);
                                    subcategories.add(model.getName());
                                    ArrayAdapter subAdapter = new ArrayAdapter(AddProduct.this, android.R.layout.simple_spinner_item, subcategories);
                                    subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    subSpinner.setAdapter(subAdapter);


                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            subSpinner.setEnabled(false);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                categoryName = categories.get(0);
            }
        });


        subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subCategoryName = subcategories.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                subCategoryName = subcategories.get(0);

            }
        });


        saveToltal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(saveToltal);
                boolean save = true;
                saveColors.setError(null);
                saveSizes.setError(null);
                add_external_img.setError(null);
                addPicture.setError(null);

                if (proPrice.getText().toString().isEmpty()) {
                    proPrice.setError("Required");
                    proPrice.requestFocus();
                    save = false;
                } else if (Float.valueOf(proPrice.getText().toString()) <= 0) {
                    proPrice.setError("Invalid Number");
                    proPrice.requestFocus();
                    save = false;
                }

                if (!proSale.getText().toString().isEmpty() && Float.valueOf(proSale.getText().toString()) <= 0) {
                    proSale.setError("Invalid Number");
                    proSale.requestFocus();
                    save = false;
                }

                if (ext_img.getVisibility() == View.GONE) {
                    add_external_img.setError("Add Picture!");
                    add_external_img.requestFocus();
                    save = false;
                }
                if (images.size() == 0) {
                    addPicture.setError("Add Pictures!");
                    addPicture.requestFocus();
                    save = false;
                }
                if (save1 == false) {
                    saveColors.setError("Save Colors!");
                    saveColors.requestFocus();
                    save = false;
                }
                if (save2 == false) {
                    saveSizes.setError("Save Sizes!");
                    saveSizes.requestFocus();
                    save = false;
                }

                if (desciption.getText().toString().isEmpty()) {
                    desciption.setError("Enter Product Description");
                    desciption.requestFocus();
                    save = false;
                }
                if (proName.getText().toString().isEmpty()) {
                    proName.setError("Required");
                    proName.requestFocus();
                    save = false;
                }


                if (save) {
                    saveToltal.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                    if (subcategories.size() == 0 || subSpinner.isEnabled() == false) {

                        storageReference.child("products").child(categoryName).child(proName.getText().toString()).child("ext_img").putFile(uri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                url = taskSnapshot.getDownloadUrl().toString();
                                final ArrayList<String> urlArr = new ArrayList<>();
                                j = -1;
                                for (final Uri myUrl : images) {
                                    j++;
                                    storageReference.child("products").child(categoryName).child(proName.getText().toString()).child("int_imgs").child(String.valueOf(j)).putFile(myUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            urlArr.add(taskSnapshot.getDownloadUrl().toString());
                                            if (myUrl.equals(images.get(images.size() - 1))) {
                                                final ProductModel model = new ProductModel(url, proName.getText().toString(), proSale.getText().toString(), proPrice.getText().toString(), urlArr, sizes, colors, desciption.getText().toString(), System.currentTimeMillis(), -1 * System.currentTimeMillis(), Float.valueOf(proPrice.getText().toString()), -1 * Float.valueOf(proPrice.getText().toString()), categoryName, "none", 0, 0);
                                                databaseReference.child("categories").child(categoryName).child("products").child(proName.getText().toString()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            Toast.makeText(AddProduct.this, "Product Is Added Successfully", Toast.LENGTH_SHORT).show();
                                                            //Clear All
                                                            progressBar.setVisibility(View.GONE);
                                                            databaseReference.child("Release").child("Products").child(proName.getText().toString()).setValue(model);
                                                            if (!model.getSale().equals(""))
                                                                databaseReference.child("Sale").child("Products").child(proName.getText().toString()).setValue(model);

                                                            uri2 = null;
                                                            ext_img.setImageURI(null);
                                                            relativeLayout.setVisibility(View.GONE);
                                                            images.clear();
                                                            adapter.notifyDataSetChanged();

                                                            desciption.setText("");
                                                            proPrice.setText("");
                                                            proName.setText("");
                                                            proSale.setText("");

                                                            colorNum.setText("");
                                                            colorNum.setEnabled(true);
                                                            numSave.setEnabled(true);
                                                            colorName.setEnabled(false);
                                                            nameSave.setEnabled(false);
                                                            colorName.setText("");
                                                            colors.clear();
                                                            saveColors.setEnabled(false);
                                                            saveColors.setAlpha(0.5f);
                                                            save1 = false;

                                                            sizeNum.setText("");
                                                            sizeNum.setEnabled(true);
                                                            numSaveSize.setEnabled(true);
                                                            sizeName.setEnabled(false);
                                                            nameSaveSize.setEnabled(false);
                                                            sizeName.setText("");
                                                            sizes.clear();
                                                            saveSizes.setEnabled(false);
                                                            saveSizes.setAlpha(0.5f);
                                                            save2 = false;
                                                            saveToltal.setEnabled(true);

                                                        } else {
                                                            Toast.makeText(AddProduct.this, "Failed Try Again", Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                            saveToltal.setEnabled(true);
                                                            progressBar.setVisibility(View.GONE);

                                                        }
                                                    }
                                                });
                                            }


                                        }
                                    });

                                }

                            }
                        });

                    } else {
                        storageReference.child("products").child(categoryName).child(subCategoryName).child(proName.getText().toString()).child("ext_img").putFile(uri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                url = taskSnapshot.getDownloadUrl().toString();
                                final ArrayList<String> urlArr = new ArrayList<>();
                                j = -1;
                                for (final Uri myUrl : images) {
                                    j++;
                                    storageReference.child("products").child(categoryName).child(subCategoryName).child(proName.getText().toString()).child("int_imgs").child(String.valueOf(j)).putFile(myUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            urlArr.add(taskSnapshot.getDownloadUrl().toString());

                                            if (myUrl.equals(images.get(images.size() - 1))) {
                                                databaseReference.child("categories").child(categoryName).child("subCat").addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                        if (dataSnapshot.child("name").getValue().toString().equals(subCategoryName)) {
                                                            final ProductModel model = new ProductModel(url, proName.getText().toString(), proSale.getText().toString(), proPrice.getText().toString(), urlArr, sizes, colors, desciption.getText().toString(), System.currentTimeMillis(), -1 * System.currentTimeMillis(), Float.valueOf(proPrice.getText().toString()), -1 * Float.valueOf(proPrice.getText().toString()), categoryName, dataSnapshot.getKey(), 0, 0);
                                                            databaseReference.child("categories").child(categoryName).child("subCat").child(dataSnapshot.getKey()).child("products").child(proName.getText().toString()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(AddProduct.this, "Product Is Added Successfully", Toast.LENGTH_SHORT).show();
                                                                        //Clear All:


                                                                        progressBar.setVisibility(View.GONE);


                                                                        databaseReference.child("Release").child("Products").child(proName.getText().toString()).setValue(model);
                                                                        if (!model.getSale().equals(""))
                                                                            databaseReference.child("Sale").child("Products").child(proName.getText().toString()).setValue(model);


                                                                        uri2 = null;
                                                                        ext_img.setImageURI(null);
                                                                        relativeLayout.setVisibility(View.GONE);
                                                                        images.clear();
                                                                        adapter.notifyDataSetChanged();

                                                                        desciption.setText("");
                                                                        proPrice.setText("");
                                                                        proName.setText("");
                                                                        proSale.setText("");

                                                                        colorNum.setText("");
                                                                        colorNum.setEnabled(true);
                                                                        numSave.setEnabled(true);
                                                                        colorName.setEnabled(false);
                                                                        nameSave.setEnabled(false);
                                                                        colorName.setText("");
                                                                        colors.clear();
                                                                        saveColors.setEnabled(false);
                                                                        saveColors.setAlpha(0.5f);
                                                                        save1 = false;

                                                                        sizeNum.setText("");
                                                                        sizeNum.setEnabled(true);
                                                                        numSaveSize.setEnabled(true);
                                                                        sizeName.setEnabled(false);
                                                                        nameSaveSize.setEnabled(false);
                                                                        sizeName.setText("");
                                                                        sizes.clear();
                                                                        saveSizes.setEnabled(false);
                                                                        saveSizes.setAlpha(0.5f);
                                                                        save2 = false;
                                                                        saveToltal.setEnabled(true);

                                                                    } else {
                                                                        Toast.makeText(AddProduct.this, "Failed Try Again", Toast.LENGTH_SHORT).show();
                                                                        progressBar.setVisibility(View.GONE);
                                                                        saveToltal.setEnabled(true);

                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                                    }

                                                    @Override
                                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                                    }

                                                    @Override
                                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                            }
                                        }
                                    });
                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddProduct.this, "Failed.Try again", Toast.LENGTH_SHORT).show();
                                saveToltal.setEnabled(true);
                                progressBar.setVisibility(View.GONE);

                            }
                        });
                    }


                }


            }
        });

        clearToltal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                hideKeyboard(clearToltal);
                saveColors.setError(null);
                saveSizes.setError(null);
                add_external_img.setError(null);
                addPicture.setError(null);
                proName.setError(null);
                proPrice.setError(null);
                desciption.setError(null);

                uri2 = null;
                ext_img.setImageURI(null);
                relativeLayout.setVisibility(View.GONE);
                images.clear();
                adapter.notifyDataSetChanged();

                desciption.setText("");
                proPrice.setText("");
                proName.setText("");
                proSale.setText("");

                colorNum.setText("");
                colorNum.setEnabled(true);
                numSave.setEnabled(true);
                colorName.setEnabled(false);
                nameSave.setEnabled(false);
                colorName.setText("");
                colors.clear();
                saveColors.setEnabled(false);
                saveColors.setAlpha(0.5f);
                save1 = false;

                sizeNum.setText("");
                sizeNum.setEnabled(true);
                numSaveSize.setEnabled(true);
                sizeName.setEnabled(false);
                nameSaveSize.setEnabled(false);
                sizeName.setText("");
                sizes.clear();
                saveSizes.setEnabled(false);
                saveSizes.setAlpha(0.5f);
                save2 = false;

                Toast.makeText(AddProduct.this, "Clear All", Toast.LENGTH_SHORT).show();

            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 2);
            }
        });


        add_external_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 2);
            }
        });


        adapter = new Adapter(this, android.R.layout.simple_list_item_1, images);
        imagesList.setAdapter(adapter);

        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b = false;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    images.add(clipData.getItemAt(i).getUri());
                }
            } else {
                uri = data.getData();
                images.add(uri);}
            adapter.notifyDataSetChanged();

        } else if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

              images.set(i,data.getData());
            adapter.notifyDataSetChanged();
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            uri2 = data.getData();
            ext_img.setImageURI(uri2);
            relativeLayout.setVisibility(View.VISIBLE);
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
