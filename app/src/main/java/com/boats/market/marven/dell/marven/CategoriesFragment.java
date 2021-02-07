package com.boats.market.marven.dell.marven;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dell on 6/20/2019.
 */

public class CategoriesFragment extends Fragment {
    DatabaseReference databaseReference, databaseReference2;
    StorageReference storageReference;
    ArrayList<String> proimges = new ArrayList<>();
    ArrayList<String> proimges2 = new ArrayList<>();
    ArrayList<String> categoryNames = new ArrayList<>();
    ArrayList<String> subcategoryNames = new ArrayList<>();

    ProgressBar progressBar;
    int index;
    int j;

    Typeface typeface;
    VHolder2 vHolder2;
    RecyclerView recyclerView, recyclerView2;
    ArrayList<CategoryModel> arrayList = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public class VHolder extends RecyclerView.Adapter<VHolder.ViewHolder> {
        Context context;
        ArrayList<String> urls = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();

        public VHolder(Context context, ArrayList<String> urls, ArrayList<String> names) {
            this.context = context;
            this.urls = urls;
            this.names = names;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.category_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;


        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            Picasso.get().load(urls.get(position)).into(holder.img, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.logo.setVisibility(View.GONE);

                }

                @Override
                public void onError(Exception e) {

                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), EditCategory.class).putExtra("pic", proimges.get(position)).putExtra("name", categoryNames.get(position)));
                }
            });


            if (firebaseAuth.getCurrentUser() != null) {
                databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("admin")) {

                            holder.edit.setVisibility(View.VISIBLE);
                            if (!(names.get(position).equals("Men") || names.get(position).equals("Women") || names.get(position).equals("Kids")))
                                holder.remove.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Do you want to remove '" + categoryNames.get(position) + "' ?").setTitle("Warning").setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (subcategoryNames.size() != 0) {
                                databaseReference.child("categories").child(categoryNames.get(position)).child("subCat").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        dataSnapshot.child("products").getRef().addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                databaseReference2.child("Release").child("Products").child(dataSnapshot.getKey()).removeValue();
                                                databaseReference2.child("Sale").child("Products").child(dataSnapshot.getKey()).removeValue();
                                                databaseReference2.child("Monaspa").child("Products").child(dataSnapshot.getKey()).removeValue();
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
                                databaseReference.child("categories").child(categoryNames.get(position)).child("products").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        databaseReference2.child("Release").child("Products").child(dataSnapshot.getKey()).removeValue();
                                        databaseReference2.child("Sale").child("Products").child(dataSnapshot.getKey()).removeValue();
                                        databaseReference2.child("Monaspa").child("Products").child(dataSnapshot.getKey()).removeValue();
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
                            databaseReference.child("categories").child(categoryNames.get(position)).removeValue();
                            notifyDataSetChanged();


                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    index = position;
                    notifyDataSetChanged();
                    vHolder2.notifyDataSetChanged();
                    databaseReference.child("categories").child(categoryNames.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild("subCat")) {

                                j = position;
                                databaseReference.child("categories").child(categoryNames.get(position)).child("subCat").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        SubCategoryModel model = dataSnapshot.getValue(SubCategoryModel.class);
                                        subcategoryNames.add(model.getName());
                                        proimges2.add(model.getUrl());
                                        recyclerView.setVisibility(View.GONE);
                                        recyclerView2.setVisibility(View.VISIBLE);
                                        HomeActivity.categoryname.setText(categoryNames.get(position));
                                        if (!HomeActivity.currentFragment.equals("Fragment_uniqe"))
                                        HomeActivity.currentFragment = "Fragment_8";
                                        progressBar.setVisibility(View.GONE);
                                        vHolder2.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                                        subcategoryNames.remove(dataSnapshot.getValue(SubCategoryModel.class).getName());
                                        proimges2.remove(dataSnapshot.getValue(SubCategoryModel.class).getUrl());
                                        vHolder2.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                startActivity(new Intent(getActivity(), ProductsActivity.class).putExtra("catName", categoryNames.get(position)).putExtra("subName", "none").putExtra("index", -1));
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

        }

        @Override
        public int getItemCount() {
            return this.names.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img, logo;
            TextView remove, edit;
            ProgressBar progressBar;

            public ViewHolder(View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.pro_im);
                edit = itemView.findViewById(R.id.editCategory);
                remove = itemView.findViewById(R.id.remove_cat);
                logo = itemView.findViewById(R.id.logo);
                progressBar = itemView.findViewById(R.id.progbar);
            }

        }

    }


    public class VHolder2 extends RecyclerView.Adapter<VHolder2.ViewHolder> {
        Context context;
        ArrayList<String> urls = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();


        public VHolder2(Context context, ArrayList<String> urls, ArrayList<String> names) {
            this.context = context;
            this.urls = urls;
            this.names = names;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.subcategory_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            Picasso.get().load(urls.get(position)).into(holder.img, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    holder.progressBar.setVisibility(View.GONE);
                    holder.logo.setVisibility(View.GONE);

                }

                @Override
                public void onError(Exception e) {

                }
            });


            if (firebaseAuth.getCurrentUser() != null) {
                databaseReference.child("Users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("admin")) {
                            holder.edit.setVisibility(View.VISIBLE);
                            holder.remove.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), EditCategory.class).putExtra("pic", proimges2.get(position)).putExtra("index", String.valueOf(position)).putExtra("subname", subcategoryNames.get(position)).putExtra("name", categoryNames.get(index)));
                }
            });
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Warning").setMessage("Do you want to remove '" + subcategoryNames.get(position) + "' ?").setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            databaseReference.child("categories").child(categoryNames.get(index)).child("subCat").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    if (dataSnapshot.child("name").getValue().toString().equals(subcategoryNames.get(position))) {

                                        databaseReference.child("categories").child(categoryNames.get(index)).child("subCat").child(dataSnapshot.getKey()).child("products").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                databaseReference2.child("Release").child("Products").child(dataSnapshot.getKey()).removeValue();
                                                databaseReference2.child("Sale").child("Products").child(dataSnapshot.getKey()).removeValue();
                                                databaseReference2.child("Monaspa").child("Products").child(dataSnapshot.getKey()).removeValue();
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
                                        databaseReference.child("categories").child(categoryNames.get(index)).child("subCat").child(dataSnapshot.getKey()).removeValue();
                                        notifyDataSetChanged();

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
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progressBar.setVisibility(View.VISIBLE);
                    databaseReference.child("categories").child(categoryNames.get(j)).child("subCat").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            if (dataSnapshot.child("name").getValue().toString().equals(subcategoryNames.get(position))) {
                                startActivity(new Intent(getActivity(), ProductsActivity.class).putExtra("catName", categoryNames.get(j)).putExtra("subName", subcategoryNames.get(position)).putExtra("index", dataSnapshot.getKey()));
                                progressBar.setVisibility(View.GONE);
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
            });

        }

        @Override
        public int getItemCount() {
            return this.names.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img, logo;
            ProgressBar progressBar;
            TextView remove, edit;

            public ViewHolder(View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.pro_im);
                edit = itemView.findViewById(R.id.editCategory);
                remove = itemView.findViewById(R.id.remove_cat);
                logo = itemView.findViewById(R.id.logo);
                progressBar = itemView.findViewById(R.id.progbar);
            }

        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.categories_fragment, container, false);
        if (!HomeActivity.currentFragment.equals("Fragment_uniqe"))
            HomeActivity.currentFragment = "Fragment_2";
        HomeActivity.categoryname.setText("Categories");
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "gotham.ttf");

        final VHolder vHolderAdapter = new VHolder(getActivity(), proimges, categoryNames);
        recyclerView = v.findViewById(R.id.categories_recView);
        recyclerView.setAdapter(vHolderAdapter);
        vHolder2 = new VHolder2(getActivity(), proimges2, subcategoryNames);
        recyclerView2 = v.findViewById(R.id.subcategories_recView);
        recyclerView2.setAdapter(vHolder2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView2.setLayoutManager(linearLayoutManager);
        progressBar = v.findViewById(R.id.categories_progressbar);
        categoryNames.clear();
        subcategoryNames.clear();

        databaseReference.child("categories").orderByChild("order").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String key = dataSnapshot.getKey();
                if (dataSnapshot.hasChild("subCat")) {
                    CategoryModel2 model = dataSnapshot.getValue(CategoryModel2.class);
                    if (model.getUrlAndPublish().isPublish()) {
                        categoryNames.add(key);
                        proimges.add(model.getUrlAndPublish().getUrl());
                        vHolderAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        if (getArguments() == null || !getArguments().containsKey("catName"))
                            recyclerView.setVisibility(View.VISIBLE);
                        else {
                            if (getArguments().getString("catName").equals("Men")) {
                                index = categoryNames.indexOf("Men");
                                j = categoryNames.indexOf("Men");
                            }
                           else if (getArguments().getString("catName").equals("Kids")) {
                                index = categoryNames.indexOf("Kids");
                                j = categoryNames.indexOf("Kids");
                            } else {
                                index = categoryNames.indexOf("Women");
                                j = categoryNames.indexOf("Women");
                            }
                        }
                    }

                } else {
                    CategoryModel model = dataSnapshot.getValue(CategoryModel.class);
                    if (model.getUrlAndPublish().isPublish()) {
                        categoryNames.add(key);
                        proimges.add(model.getUrlAndPublish().getUrl());
                        vHolderAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        if (getArguments() == null || !getArguments().containsKey("catName"))
                            recyclerView.setVisibility(View.VISIBLE);


                    }
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                categoryNames.remove(dataSnapshot.getKey());
                proimges.remove(dataSnapshot.child("urlAndPublish").child("url").getValue());
                vHolderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (getArguments() != null && getArguments().containsKey("catName"))

        {
            progressBar.setVisibility(View.VISIBLE);
            vHolderAdapter.notifyDataSetChanged();
            vHolder2.notifyDataSetChanged();
            final String catName = getArguments().getString("catName");
            if (getArguments().getString("catName").equals("Men")){
                HomeActivity.categoryname.setText("Men");
            }
           else if (getArguments().getString("catName").equals("Kids")) {
                HomeActivity.categoryname.setText("Kids");
            } else {
                HomeActivity.categoryname.setText("Women");
            }
            databaseReference.child("categories").child(catName).child("subCat").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    SubCategoryModel model = dataSnapshot.getValue(SubCategoryModel.class);
                    subcategoryNames.add(model.getName());
                    proimges2.add(model.getUrl());
                    recyclerView.setVisibility(View.GONE);
                    recyclerView2.setVisibility(View.VISIBLE);
                    if (!HomeActivity.currentFragment.equals("Fragment_uniqe"))
                    HomeActivity.currentFragment = "Fragment_8";
                    progressBar.setVisibility(View.GONE);
                    vHolder2.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    subcategoryNames.remove(dataSnapshot.getValue(SubCategoryModel.class).getName());
                    proimges2.remove(dataSnapshot.getValue(SubCategoryModel.class).getUrl());
                    vHolder2.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return v;

    }


}
