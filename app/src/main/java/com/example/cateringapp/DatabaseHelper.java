package com.example.cateringapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static DatabaseHelper _instance;
    private String TAG = "Catering App DatabaseHelper";
    private ArrayList<User> userList;
    private List<Product> productList;
    private List<Product> cartList;

    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseProducts;
    private DatabaseReference mDatabaseCart;

    StorageReference storageRef;
    StorageReference imagesRef;
    FirebaseStorage storage;

    private static Context context;
    private static DatabaseOperationCallback callback;
    static boolean initialising = true;



    public DatabaseHelper()
    {
        userList = new ArrayList<>();
        productList = new ArrayList<>();
        cartList = new ArrayList<>();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users/");
        mDatabaseProducts = FirebaseDatabase.getInstance().getReference("products/");
        mDatabaseCart = FirebaseDatabase.getInstance().getReference("users/"+MainApplication.currentUser.getUid()+"/cart/");
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        imagesRef = storageRef.child("images/products/test.png");
        // Get the data from an ImageView as bytes
        RegisterListeners();


    }

    public static DatabaseHelper getInstance(Context _context)
    {
        context = _context;
        if(_instance == null)
            _instance = new DatabaseHelper();
        return _instance;
    }
    public static DatabaseHelper getInstance(Context _context, DatabaseOperationCallback _callback)
    {
        if(initialising)
            callback = _callback;
        else if(_callback != null)
            _callback.OnOperationComplete();

        return getInstance(_context);
    }
    void RegisterListeners()
    {
        ValueEventListener postListenerProducts = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initialising = false;
                if (callback != null)
                    callback.OnOperationComplete();
                callback = null;

                productList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.child("productID").getValue().toString();
                    String name = ds.child("name").getValue().toString();
                    double price = Double.parseDouble(ds.child("price").getValue().toString());

                    Product product = new Product(id,name,price);
                    productList.add(product);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadIncome:onCancelled", databaseError.toException());
                // ...
            }
        };
        ValueEventListener postListenerCart = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initialising = false;
                if (callback != null)
                    callback.OnOperationComplete();
                callback = null;

                cartList= new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id = ds.child("productID").getValue().toString();
                    String name = ds.child("name").getValue().toString();
                    double price = Double.parseDouble(ds.child("price").getValue().toString());

                    Product product = new Product(id,name,price);
                    cartList.add(product);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadExpense:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabaseProducts.addValueEventListener(postListenerProducts);
        mDatabaseCart.addValueEventListener(postListenerCart);
    }
    public void AddUser(User user)
    {
        mDatabaseUsers.child(user.getUserID()).setValue(user);
        Toast.makeText(context, "Added: " + user.getUserID(), Toast.LENGTH_SHORT).show();
    }
    public void AddProduct(Product product, final Bitmap bitmap, final DatabaseOperationCallback _callback)
    {
        final String key = mDatabaseProducts.push().getKey();
        imagesRef = imagesRef.getParent().child(key + ".png");
        product.setProductID(key);
        mDatabaseProducts.child(key).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                _callback.OnOperationComplete();
                uploadData(bitmap, key, _callback);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Failed to add Book",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void AddToCart(Product product, final DatabaseOperationCallback _callback)
    {
        mDatabaseCart.child(product.getProductID()).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                _callback.OnOperationComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Failed to add Expense",Toast.LENGTH_SHORT).show();
            }
        });
    }




    public List<Product> GetProducts()
    {
        return productList;
    }
    public List<Product> GetCart()
    {
        return cartList;
    }


    void uploadData(final Bitmap bitmap, final String id, final DatabaseOperationCallback _callback)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                _callback.OnImageOperationComplete(bitmap);
            }
        });
    }



}
