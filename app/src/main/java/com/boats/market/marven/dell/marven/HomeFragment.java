package com.boats.market.marven.dell.marven;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

/**
 * Created by dell on 6/18/2019.
 */

public class HomeFragment extends Fragment {
    ImageView release_image, monaspa_image, sale_image, men_image, women_image, kids_image;
    LinearLayout monaspabtn_lay, releasebtn_lay, salebtn_lay, men_lay, women_lay, kids_lay;
    DatabaseReference databaseReference;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    String monaspaBeforeUrl = null, releaseBeforeUrl = null, saleBeforeUrl = null, menBeforeUrl = null,
            womenBeforeUrl = null, kidsBeforeUrl = null;
    String titleName;
    Fragment categoriesFragment = new CategoriesFragment();
    int adminOrDelive = 1;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, container, false);


        men_image = v.findViewById(R.id.menCategory_image);
        women_image = v.findViewById(R.id.womenCategory_image);
        kids_image = v.findViewById(R.id.kidsCategory_image);

        final RelativeLayout men_layout = v.findViewById(R.id.men_layout);
        final RelativeLayout women_layout = v.findViewById(R.id.women_layout);
        RelativeLayout kids_layout = v.findViewById(R.id.kids_layout);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final ImageView change_monaspat = v.findViewById(R.id.change_monaspa);
        final ImageView change_release = v.findViewById(R.id.change_release);
        final ImageView change_sale = v.findViewById(R.id.change_sale);
        final ImageView change_title = v.findViewById(R.id.change_title);

        final ImageView change_men = v.findViewById(R.id.change_men);
        final ImageView change_women = v.findViewById(R.id.change_women);
        final ImageView change_kids = v.findViewById(R.id.change_kids);

        HomeActivity.home_logo.setEnabled(false);
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("admin")) {
                        adminOrDelive = 1;
                        HomeActivity.home_logo.setEnabled(true);
                        change_monaspat.setVisibility(View.VISIBLE);
                        change_release.setVisibility(View.VISIBLE);
                        change_sale.setVisibility(View.VISIBLE);
                        change_title.setVisibility(View.VISIBLE);

                        change_men.setVisibility(View.VISIBLE);
                        change_women.setVisibility(View.VISIBLE);
                        change_kids.setVisibility(View.VISIBLE);
                    } else if (dataSnapshot.hasChild("deliver")) {
                        adminOrDelive = 2;
                        HomeActivity.home_logo.setEnabled(true);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        HomeActivity.home_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminOrDelive==1)
                    startActivity(new Intent(getActivity(), AdminActivity.class));
                else
                    startActivity(new Intent(getActivity(), Order.class));
            }
        });

        HomeActivity.currentFragment = "Fragment_1";
        final RelativeLayout monaspa_layout = v.findViewById(R.id.monaspa_layout);

        monaspa_image = v.findViewById(R.id.monaspa_image);

        release_image = v.findViewById(R.id.realese_image);
        sale_image = v.findViewById(R.id.sale_image);


        monaspabtn_lay = v.findViewById(R.id.monaspabtn_lay);
        releasebtn_lay = v.findViewById(R.id.releasebtn_lay);
        salebtn_lay = v.findViewById(R.id.salebtn_lay);
        men_lay = v.findViewById(R.id.men_lay);
        women_lay = v.findViewById(R.id.women_lay);
        kids_lay = v.findViewById(R.id.kids_lay);


        final ImageView logo1 = v.findViewById(R.id.logo);
        final ImageView logo2 = v.findViewById(R.id.logo2);
        final ImageView logo3 = v.findViewById(R.id.logo3);


        Button saveRelease = v.findViewById(R.id.save_releaseImage);
        Button saveSale = v.findViewById(R.id.save_saleImage);
        Button saveMonaspa = v.findViewById(R.id.save_monaspaImage);
        Button saveMen = v.findViewById(R.id.save_menImage);
        Button saveWomen = v.findViewById(R.id.save_womenImage);
        Button saveKids = v.findViewById(R.id.save_kidsImage);

        Button cancelRelease = v.findViewById(R.id.cancel_releaseImage);
        Button cancelSale = v.findViewById(R.id.cancel_saleImage);
        Button cancelMonaspa = v.findViewById(R.id.cancel_monaspaImage);
        Button cancelMen = v.findViewById(R.id.cancel_menImage);
        Button cancelWomen = v.findViewById(R.id.cancel_womenImage);
        Button cancelKids = v.findViewById(R.id.cancel_kidsImage);

        final ProgressBar pr1 = v.findViewById(R.id.progbar);
        final ProgressBar pr2 = v.findViewById(R.id.progbar2);
        final ProgressBar pr3 = v.findViewById(R.id.progbar3);

        try {
            databaseReference.child("Monaspa").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("Products")) {
                        monaspa_layout.setVisibility(View.VISIBLE);
                        Picasso.get().load(dataSnapshot.child("url").getValue().toString()).into(monaspa_image, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                pr1.setVisibility(View.GONE);
                                monaspaBeforeUrl = dataSnapshot.child("url").getValue().toString();
                                logo1.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        final RelativeLayout sale_layout = v.findViewById(R.id.sale_layout);

        try {
            databaseReference.child("Release").child("url").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    Picasso.get().load(dataSnapshot.getValue().toString()).into(release_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            releaseBeforeUrl = dataSnapshot.getValue().toString();
                            pr2.setVisibility(View.GONE);
                            logo2.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            databaseReference.child("Sale").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("Products")) {
                        sale_layout.setVisibility(View.VISIBLE);
                        Picasso.get().load(dataSnapshot.child("url").getValue().toString()).into(sale_image, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                saleBeforeUrl = dataSnapshot.child("url").getValue().toString();
                                pr3.setVisibility(View.GONE);
                                logo3.setVisibility(View.GONE);

                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        final ProgressBar men_progress = v.findViewById(R.id.progbar_men);
        final ProgressBar women_progress = v.findViewById(R.id.progbar_women);
        final ProgressBar kids_progress = v.findViewById(R.id.progbar_kids);
        final ImageView logo_men = v.findViewById(R.id.logo_men);
        final ImageView logo_women = v.findViewById(R.id.logo_women);
        final ImageView logo_kids = v.findViewById(R.id.logo_kids);
        try {
            databaseReference.child("categories").child("Men").child("urlAndPublish").child("url").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    Picasso.get().load(dataSnapshot.getValue().toString()).into(men_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            menBeforeUrl = dataSnapshot.getValue().toString();
                            men_progress.setVisibility(View.GONE);
                            logo_men.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            databaseReference.child("categories").child("Women").child("urlAndPublish").child("url").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    Picasso.get().load(dataSnapshot.getValue().toString()).into(women_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            womenBeforeUrl = dataSnapshot.getValue().toString();
                            women_progress.setVisibility(View.GONE);
                            logo_women.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            databaseReference.child("categories").child("Kids").child("urlAndPublish").child("url").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    Picasso.get().load(dataSnapshot.getValue().toString()).into(kids_image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            kidsBeforeUrl = dataSnapshot.getValue().toString();
                            kids_progress.setVisibility(View.GONE);
                            logo_kids.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        saveRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageReference.child("HomeImages").child("Release").putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        databaseReference.child("Release").child("url").setValue(url);
                        releasebtn_lay.setVisibility(View.GONE);
                    }
                });

            }
        });

        cancelRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releasebtn_lay.setVisibility(View.GONE);
                release_image.setImageURI(null);
                Picasso.get().load(releaseBeforeUrl).into(release_image);
            }
        });

        saveMonaspa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageReference.child("HomeImages").child("Monaspa").putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        databaseReference.child("Monaspa").child("url").setValue(url);
                        monaspabtn_lay.setVisibility(View.GONE);
                    }
                });

            }
        });

        saveMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageReference.child("categoryimages").child("Men").child("url").putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        databaseReference.child("categories").child("Men").child("urlAndPublish").child("url").setValue(url);
                        men_lay.setVisibility(View.GONE);
                    }
                });

            }
        });

        saveWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageReference.child("categoryimages").child("Women").child("url").putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        databaseReference.child("categories").child("Women").child("urlAndPublish").child("url").setValue(url);
                        women_lay.setVisibility(View.GONE);
                    }
                });

            }
        });


        saveKids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageReference.child("categoryimages").child("Kids").child("url").putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        databaseReference.child("categories").child("Kids").child("urlAndPublish").child("url").setValue(url);
                        kids_lay.setVisibility(View.GONE);
                    }
                });

            }
        });


        cancelMonaspa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monaspabtn_lay.setVisibility(View.GONE);
                monaspa_image.setImageURI(null);
                Picasso.get().load(monaspaBeforeUrl).into(monaspa_image);


            }
        });

        saveSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storageReference.child("HomeImages").child("Sale").putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getDownloadUrl().toString();
                        databaseReference.child("Sale").child("url").setValue(url);
                        salebtn_lay.setVisibility(View.GONE);
                    }
                });

            }
        });

        cancelSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salebtn_lay.setVisibility(View.GONE);
                sale_image.setImageURI(null);
                Picasso.get().load(saleBeforeUrl).into(sale_image);


            }
        });

        cancelMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                men_lay.setVisibility(View.GONE);
                men_image.setImageURI(null);
                Picasso.get().load(menBeforeUrl).into(men_image);
            }
        });

        cancelWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                women_lay.setVisibility(View.GONE);
                women_image.setImageURI(null);
                Picasso.get().load(womenBeforeUrl).into(women_image);
            }
        });


        cancelKids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kids_lay.setVisibility(View.GONE);
                kids_image.setImageURI(null);
                Picasso.get().load(kidsBeforeUrl).into(kids_image);
            }
        });


        change_monaspat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 0);
            }
        });

        change_release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 1);
            }
        });

        change_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 2);
            }
        });


        change_men.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 3);
            }
        });
        change_women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 4);
            }
        });
        change_kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Picture"), 5);
            }
        });


        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.changetitle_dialog);
        final EditText title = dialog.findViewById(R.id.title_edittxt);
        final TextView confirm = dialog.findViewById(R.id.confirm);
        final TextView cancel = dialog.findViewById(R.id.cancel);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        change_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        databaseReference.child("Monaspa").child("title").setValue(title.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Saved successfully", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Failed.try again..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }


                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });

        release_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProductsActivity.class).putExtra("catName", "Release").putExtra("subName", "none").putExtra("index", -1));
            }
        });

        sale_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProductsActivity.class).putExtra("catName", "Sale").putExtra("subName", "none").putExtra("index", -1));
            }
        });


        databaseReference.child("Monaspa").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                titleName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        monaspa_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), ProductsActivity.class).putExtra("catName", titleName).putExtra("subName", "none").putExtra("monaspa", true).putExtra("index", -1));

            }
        });

        men_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("categories").child("Men").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("subCat")) {
                            HomeActivity.home_icon.setColorFilter(getActivity().getResources().getColor(R.color.mycolor));
                            HomeActivity.list_icon.setColorFilter(getActivity().getResources().getColor(R.color.colorPrimary));
                            HomeActivity.favourite_icon.setColorFilter(getActivity().getResources().getColor(R.color.mycolor));
                            HomeActivity.about_icon.setColorFilter(getActivity().getResources().getColor(R.color.mycolor));
                            HomeActivity.account_icon.setColorFilter(getActivity().getResources().getColor(R.color.mycolor));
                            HomeActivity.toolbar2.setVisibility(View.GONE);
                            HomeActivity.toolbar.setVisibility(View.VISIBLE);
                            // getActivity().setActionBar(HomeActivity.toolbar);
                            Bundle data = new Bundle();
                            data.putString("catName", "Men");
                            categoriesFragment.setArguments(data);
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.placeholder, categoriesFragment);
                            fragmentTransaction.commit();
                            HomeActivity.currentFragment = "Fragment_uniqe";
                        } else {
                            startActivity(new Intent(getActivity(), ProductsActivity.class).putExtra("catName", "Men").putExtra("subName", "none").putExtra("index", -1));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });


        kids_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("categories").child("Kids").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("subCat")) {
                            HomeActivity.home_icon.setColorFilter(getActivity().getResources().getColor(R.color.mycolor));
                            HomeActivity.list_icon.setColorFilter(getActivity().getResources().getColor(R.color.colorPrimary));
                            HomeActivity.favourite_icon.setColorFilter(getActivity().getResources().getColor(R.color.mycolor));
                            HomeActivity.about_icon.setColorFilter(getActivity().getResources().getColor(R.color.mycolor));
                            HomeActivity.account_icon.setColorFilter(getActivity().getResources().getColor(R.color.mycolor));
                            HomeActivity.toolbar2.setVisibility(View.GONE);
                            HomeActivity.toolbar.setVisibility(View.VISIBLE);
                            // getActivity().setActionBar(HomeActivity.toolbar);
                            Bundle data = new Bundle();
                            data.putString("catName", "Kids");
                            categoriesFragment.setArguments(data);
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.placeholder, categoriesFragment);
                            fragmentTransaction.commit();
                            HomeActivity.currentFragment = "Fragment_uniqe";
                        } else {
                            startActivity(new Intent(getActivity(), ProductsActivity.class).putExtra("catName", "Kids").putExtra("subName", "none").putExtra("index", -1));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        women_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("categories").child("Women").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("subCat")) {
                            HomeActivity.home_icon.setColorFilter(getActivity().getResources().getColor(R.color.mycolor));
                            HomeActivity.list_icon.setColorFilter(getActivity().getResources().getColor(R.color.colorPrimary));
                            HomeActivity.favourite_icon.setColorFilter(getActivity().getResources().getColor(R.color.mycolor));
                            HomeActivity.about_icon.setColorFilter(getActivity().getResources().getColor(R.color.mycolor));
                            HomeActivity.account_icon.setColorFilter(getActivity().getResources().getColor(R.color.mycolor));
                            HomeActivity.toolbar2.setVisibility(View.GONE);
                            HomeActivity.toolbar.setVisibility(View.VISIBLE);
                            Bundle data = new Bundle();
                            data.putString("catName", "Women");
                            categoriesFragment.setArguments(data);
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.placeholder, categoriesFragment);
                            fragmentTransaction.commit();
                            HomeActivity.currentFragment = "Fragment_uniqe";
                        } else {
                            startActivity(new Intent(getActivity(), ProductsActivity.class).putExtra("catName", "Women").putExtra("subName", "none").putExtra("index", -1));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && data != null) {
            switch (requestCode) {
                case 0:
                    uri1 = data.getData();
                    monaspa_image.setImageURI(uri1);
                    monaspabtn_lay.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    uri1 = data.getData();
                    release_image.setImageURI(uri1);
                    releasebtn_lay.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    uri1 = data.getData();
                    sale_image.setImageURI(uri1);
                    salebtn_lay.setVisibility(View.VISIBLE);
                    break;

                case 3:
                    uri1 = data.getData();
                    men_image.setImageURI(uri1);
                    men_lay.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    uri1 = data.getData();
                    women_image.setImageURI(uri1);
                    women_lay.setVisibility(View.VISIBLE);
                    break;

                case 5:
                    uri1 = data.getData();
                    kids_image.setImageURI(uri1);
                    kids_lay.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }


    Uri uri1;
}

