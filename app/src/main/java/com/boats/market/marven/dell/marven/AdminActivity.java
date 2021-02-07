package com.boats.market.marven.dell.marven;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    Button upload_sub_pic;
    int counter = 0;
    int num = 0;
    boolean b = true;
    LinearLayout subnames_layout, layout_parent, categories_layout, addcategory_layout;
    DatabaseReference databaseReference;
    ImageView category_pic;
    StorageReference storageReference;
    EditText categoryName;
    ProgressBar progressBar;
    ArrayList<Uri> images = new ArrayList<>();
    int i = 0;
    int j;
    Adapter adapter;
    TextView title;
    ArrayList<SubCategoryModel> modelArr = new ArrayList<>();
    NestedScrollView scrollView;

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
            ImageView edit = view.findViewById(R.id.edit);
            ImageView closeIcon = view.findViewById(R.id.close);
            closeIcon.setVisibility(View.GONE);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    i = position;
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 2);
                    notifyDataSetChanged();
                }
            });

            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "gotham.ttf");

        progressBar = findViewById(R.id.progressbar7);
        final Button categories = findViewById(R.id.categories);
        Button addCategory = findViewById(R.id.addcategory);
        subnames_layout = findViewById(R.id.subname_layout);
        layout_parent = findViewById(R.id.layout_parent);
        categories_layout = findViewById(R.id.categories_layout);
        addcategory_layout = findViewById(R.id.addcategory_layout);
        categoryName = findViewById(R.id.categoruname_edittxt);
        Button add_sub = findViewById(R.id.add_sub);
        final Button save = findViewById(R.id.save_btn), cancel = findViewById(R.id.cancel_btn);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        category_pic = findViewById(R.id.category_pic);
        Button voucherCode = findViewById(R.id.codes_btn);
        Button orders_btn = findViewById(R.id.orders_btn);

        addCategory.setTypeface(typeface);
        categories.setTypeface(typeface);

        categoryName.setTypeface(typeface);
        add_sub.setTypeface(typeface);
        save.setTypeface(typeface);
        cancel.setTypeface(typeface);
        voucherCode.setTypeface(typeface);
        orders_btn.setTypeface(typeface);

        voucherCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, VoucherCode.class));
            }
        });

        orders_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         startActivity(new Intent(AdminActivity.this,OrdersActivity.class));
            }
        });

        final Button uploadPic = findViewById(R.id.upload);
        Button usersBtn = findViewById(R.id.usersBtn);
        scrollView = findViewById(R.id.scrollview);
        usersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, UsersForAdmin.class));
            }
        });

        usersBtn.setTypeface(typeface);
        uploadPic.setTypeface(typeface);

        upload_sub_pic = findViewById(R.id.add_sub_pic);
        final ListView listView = findViewById(R.id.subImagesList);
        adapter = new Adapter(this, android.R.layout.simple_list_item_1, images);
        listView.setAdapter(adapter);

        upload_sub_pic.setTypeface(typeface);

        upload_sub_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 1);
                adapter.notifyDataSetChanged();
            }
        });

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));

        categoryName.requestFocus();
        Button addProductBtn = findViewById(R.id.add_product_btn);

        Button publish_btn = findViewById(R.id.publish_btn);
        publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, PublishingActivity.class));
            }
        });
        publish_btn.setTypeface(typeface);
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, AddProduct.class));
            }
        });
        toolbar = findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        cart_counter_txt = toolbar.findViewById(R.id.cart_counter_txt);

        ImageView search_icon = toolbar.findViewById(R.id.search_icon);
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, SearchActivity.class));
            }
        });
        addProductBtn.setTypeface(typeface);
        ImageView cartimg = toolbar.findViewById(R.id.cart_icon);
        ImageView backimg = toolbar.findViewById(R.id.back_icon);
        title = toolbar.findViewById(R.id.customcategort_name);
        title.setText("Admin Options");
        cartimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminActivity.this, CartActivity.class));
            }
        });

        title.setTypeface(typeface);


        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_parent.setVisibility(View.GONE);
                categories_layout.setVisibility(View.VISIBLE);
                title.setText("CATEGORIES");

                CheckConnection checkConnection = new CheckConnection(AdminActivity.this);
                if (!checkConnection.isConnecting()) {
                    startActivity(new Intent(AdminActivity.this, ConnectionActivity.class));
                }
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categories_layout.setVisibility(View.GONE);
                addcategory_layout.setVisibility(View.VISIBLE);
                title.setText("ADD CATEGORY");

                CheckConnection checkConnection = new CheckConnection(AdminActivity.this);
                if (!checkConnection.isConnecting()) {
                    startActivity(new Intent(AdminActivity.this, ConnectionActivity.class));
                }
            }
        });


        add_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                counter++;
                num++;
                LinearLayout linearLayout = new LinearLayout(AdminActivity.this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                linearLayout.setLayoutParams(params);
                linearLayout.setId(counter);

                EditText ed = new EditText(AdminActivity.this);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, (int) (50 * Resources.getSystem().getDisplayMetrics().density));
                p.weight = 3;

                ed.setLayoutParams(p);
                ed.setHint("SubCategory Name");
                ed.setTextSize(15);
                ed.setTag("ed" + counter);
                Button remove = new Button(AdminActivity.this);
                remove.setText("Remove");
                remove.setBackgroundResource(R.drawable.startshoping_style);
                remove.setTag("remove" + counter);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) (40 * Resources.getSystem().getDisplayMetrics().density));
                params1.leftMargin = (int) (10 * Resources.getSystem().getDisplayMetrics().density);
                remove.setOnClickListener(onClickListener);
                remove.setLayoutParams(params1);
                linearLayout.addView(ed);
                linearLayout.addView(remove);
                subnames_layout.addView(linearLayout);
                upload_sub_pic.setEnabled(true);

            }

        });

        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent1.setType("image/*");
                startActivityForResult(Intent.createChooser(intent1, "Choose Picture"), 0);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(save);
                b = true;
                if (categoryName.getText().toString().isEmpty()) {
                    categoryName.setError("Enter Caterory Name");
                    categoryName.requestFocus();
                    b = false;

                }

                if (uri == null) {
                    uploadPic.setError("Upload Picture");
                    uploadPic.requestFocus();
                    b = false;
                }

                if (adapter.getCount() != num) {
                    upload_sub_pic.setError("Upload " + num + "Pictures!!");
                    upload_sub_pic.requestFocus();
                    b = false;
                }

                for (int i = 0; i <= counter; i++) {
                    try {
                        EditText ed = findViewById(i).findViewWithTag("ed" + i);
                        if (ed.getText().toString().isEmpty()) {
                            ed.requestFocus();
                            ed.setError("Required!");
                            b = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (b) {
                    save.setEnabled(false);

                    progressBar.setVisibility(View.VISIBLE);
                    scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
                    if (counter != 0) {

                        for (j = 0; j < subnames_layout.getChildCount(); j++) {
                            LinearLayout parent = (LinearLayout) (subnames_layout.getChildAt(j));
                            final EditText editText = (EditText) parent.getChildAt(0);
                            storageReference.child("categoryimages").child(categoryName.getText().toString()).child(editText.getText().toString()).putFile(images.get(j)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    final SubCategoryModel model = new SubCategoryModel(editText.getText().toString(), taskSnapshot.getDownloadUrl().toString());
                                    modelArr.add(model);
                                }
                            });

                        }

                        storageReference.child("categoryimages").child(categoryName.getText().toString()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                final String url = taskSnapshot.getDownloadUrl().toString();
                                UrlAndPublish urlAndPublish = new UrlAndPublish(url);
                                newCatClass newCatClass = new newCatClass(modelArr, urlAndPublish);
                                databaseReference.child("categories").child(categoryName.getText().toString()).setValue(newCatClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {


                                            Toast.makeText(AdminActivity.this, "Category Is Successfully Created", Toast.LENGTH_SHORT).show();

                                            categoryName.setText("");
                                            uri = null;
                                            subnames_layout.removeAllViews();
                                            category_pic.setVisibility(View.GONE);
                                            counter = 0;
                                            progressBar.setVisibility(View.GONE);
                                            categoryName.requestFocus();
                                            images.clear();
                                            adapter.notifyDataSetChanged();
                                            num = 0;
                                            save.setEnabled(true);

                                        } else {
                                            Toast.makeText(AdminActivity.this, "Failed.Try Again", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            save.setEnabled(true);
                                        }
                                    }
                                });


                            }
                        });


                    } else {
                        storageReference.child("categoryimages").child(categoryName.getText().toString()).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                String url = taskSnapshot.getDownloadUrl().toString();
                                UrlAndPublish urlAndPublish = new UrlAndPublish(url);
                                databaseReference.child("categories").child(categoryName.getText().toString()).child("urlAndPublish").setValue(urlAndPublish).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {


                                            Toast.makeText(AdminActivity.this, "Category Is Created Successfully", Toast.LENGTH_SHORT).show();

                                            categoryName.setText("");
                                            uri = null;
                                            category_pic.setVisibility(View.GONE);
                                            counter = 0;
                                            progressBar.setVisibility(View.GONE);
                                            categoryName.requestFocus();
                                            num = 0;
                                            save.setEnabled(true);


                                        } else {
                                            Toast.makeText(AdminActivity.this, "Failed.Try Again", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            save.setEnabled(true);

                                        }
                                    }
                                });
                            }
                        });


                    }
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(cancel);
                onBackPressed();
                progressBar.setVisibility(View.GONE);


            }
        });


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for (int i = 0; i <= counter; i++) {
                if (view.getTag().equals("remove" + i)) {
                    subnames_layout.removeView(findViewById(i));
                    num--;
                    if (num == 0) {
                        upload_sub_pic.setEnabled(false);
                    }
                }
            }
        }
    };


    @Override
    public void onBackPressed() {

        CheckConnection checkConnection = new CheckConnection(this);
        if (!checkConnection.isConnecting())
            startActivity(new Intent(this, ConnectionActivity.class));
        else

        {
            if (addcategory_layout.getVisibility() == View.VISIBLE) {
                addcategory_layout.setVisibility(View.GONE);
                title.setText("CATEGORIES");
                categories_layout.setVisibility(View.VISIBLE);
                subnames_layout.removeAllViews();
                categoryName.setText("");
                counter = 0;
                uri = null;
                category_pic.setVisibility(View.GONE);
                images.clear();
                num = 0;
                adapter.notifyDataSetChanged();


            } else if (categories_layout.getVisibility() == View.VISIBLE) {
                categories_layout.setVisibility(View.GONE);
                title.setText("ADMIN OPTIONS");
                layout_parent.setVisibility(View.VISIBLE);
                subnames_layout.removeAllViews();
                categoryName.setText("");
                counter = 0;
                uri = null;
                category_pic.setVisibility(View.GONE);
                images.clear();
                num = 0;
                adapter.notifyDataSetChanged();


            } else
                super.onBackPressed();
        }


    }


    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 0) {
                uri = data.getData();
                category_pic.setImageURI(uri);
                category_pic.setVisibility(View.VISIBLE);
            } else if (requestCode == 1) {
                uri2 = data.getData();
                images.add(uri2);
                adapter.notifyDataSetChanged();
                if (adapter.getCount() == num) {
                    upload_sub_pic.setEnabled(false);
                }


            } else if (requestCode == 2) {
                uri = data.getData();
                images.set(i, uri);
                adapter.notifyDataSetChanged();
            }
        }
    }


    Uri uri, uri2;
}
