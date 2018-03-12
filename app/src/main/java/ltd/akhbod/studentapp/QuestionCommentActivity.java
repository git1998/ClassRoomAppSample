package ltd.akhbod.studentapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import id.zelory.compressor.Compressor;

public class QuestionCommentActivity extends AppCompatActivity {


    //post variables-------------
    RecyclerView recyclerView;
    DatabaseReference DatabaseRef, pDatabaseRef;
    FirebaseAuth Auth;
    FirebaseAuth.AuthStateListener AuthStateListener;

    LinearLayoutManager mLayoutManager;
    FragmentActivity c;
    //post variables-------------

    //posting variable--------------------------------
    Uri ImageUri;
    String ImageUrl = "no", ThumbUrl = "no";
    Boolean isImageUpload = false, isPostUpload = false;
    String PostUid1 = null, PostUid2 = null;
    ProgressDialog mProgressDialog;

    ImageView mAnswerThumb, mRemoveThumb;
    EditText mPostAnswer;


    String MainAnswer, pImageUrl;
    //posting variable--------------------------------

    StorageReference StorageRef;
    String userId;


    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_comment);


        //----------------------------initialising layout from intent--------------------------------------


        Intent intent = getIntent();
        PostUid1 = intent.getStringExtra("postid");
        MainAnswer = intent.getStringExtra("msg");
        pImageUrl = intent.getStringExtra("imageurl");



        final TextView mMainAnswer = findViewById(R.id.question_comment_mainMsg);
        ImageView mImageView = findViewById(R.id.question_comment_mainImage);

        mMainAnswer.setText(MainAnswer);
        Picasso.with(c).load(ImageUrl).placeholder(R.drawable.unavailablepostimage).into(mImageView);


        final LinearLayout.LayoutParams layoutparams;
        layoutparams = (LinearLayout.LayoutParams) mMainAnswer.getLayoutParams();

        mMainAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i == 0) {
                    layoutparams.height = 300;
                    i = 1;
                } else {
                    layoutparams.height = 200;
                    i = 0;
                }

                mMainAnswer.setLayoutParams(layoutparams);
            }
        });


        final ImageView mAddImage = findViewById(R.id.chat_addbtn);
        mAnswerThumb = findViewById(R.id.question_comment_answerThumb);
        mRemoveThumb = findViewById(R.id.question_comment_removeThumb);
        mPostAnswer = findViewById(R.id.chat_massageimg);
        ImageView mSendAnswer = findViewById(R.id.chat_sendbtn);

        mAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });

        mSendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMassage();
            }
        });

        mRemoveThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletThumbView();
            }
        });


        //----------------------------initialising layout from intent--------------------------------------

        //-----------------------------Displaying Posts-----------------------------------------------------
        DatabaseRef = FirebaseDatabase.getInstance().getReference().child("postanswers").child(PostUid1);
        DatabaseRef.keepSynced(true);
        Auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.question_comment_recyclerview);
    //    recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        AuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (Auth.getCurrentUser() == null) {
                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                }
            }
        };


        //-----------------------------Displaying Posts-------------------------------------------------


        Auth = FirebaseAuth.getInstance();
        userId = Auth.getCurrentUser().getUid().toString();
        StorageRef = FirebaseStorage.getInstance().getReference().child("postanswer").child(PostUid1);
        DatabaseReference tDatabaseRef = DatabaseRef.push();
        PostUid2 = tDatabaseRef.getKey().toString();


        //------------------------------sending selected posts------------------------------------------------------

        pDatabaseRef = FirebaseDatabase.getInstance().getReference().child("AllPosts");

        //------------------------------sending selected posts------------------------------------------------------

    }




    private void addImage() {

        Intent intent = CropImage.activity().setMinCropResultSize(400, 400)
                .setMaxCropResultSize(3500, 3500)
                .getIntent(this);
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    private void showThumbImage() {

        if (ImageUri != null) {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Uploading Profile...");
            mProgressDialog.setMessage("Please wait while we upload and process the image");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            File thumb_path = new File((ImageUri).getPath());
            Bitmap thumb_bit = new Compressor(this).setMaxHeight(200)
                    .setMaxWidth(200).setQuality(50)
                    .compressToBitmap(thumb_path);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumb_bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final byte[] thumb_byte = baos.toByteArray();


            StorageRef.child(PostUid2).child("image" + ".jpg").putFile(ImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content

                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            ImageUrl = downloadUrl.toString();
                            Toast.makeText(getApplicationContext(), "Image uploading:Sucessfully", Toast.LENGTH_SHORT).show();

                            final StorageReference thumb_file_path = StorageRef.child(PostUid2).child("thumbimage" + ".jpg");
                            UploadTask uploadTask = (UploadTask) thumb_file_path.putBytes(thumb_byte).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    if (task.isSuccessful()) {
                                        isImageUpload = true;
                                        ThumbUrl = task.getResult().getDownloadUrl().toString();

                                        mAnswerThumb.setVisibility(View.VISIBLE);
                                        mRemoveThumb.setVisibility(View.VISIBLE);
                                        Picasso.with(getApplicationContext()).load(ThumbUrl).
                                                placeholder(R.drawable.unavailablepostimage).into(mAnswerThumb);

                                        mProgressDialog.dismiss();
                                    }
                                }
                            });
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(getApplicationContext(), "Image uploading:failed", Toast.LENGTH_SHORT).show();
                            Log.d("image", "Image uploading:failed");
                        }
                    });

            isImageUpload = true;
        }

    }


    private void deletThumbView() {

        Log.d("learn", isImageUpload + "");
        Log.d("learn", isPostUpload + "");

        if (isImageUpload == true && isPostUpload == false) {

            StorageReference deleteRef = StorageRef.child(PostUid2 + "/image.jpg");
            StorageReference deletRef2 = StorageRef.child(PostUid2 + "/thumbimage.jpg");
            deleteRef.delete();
            deletRef2.delete();
            mAnswerThumb.setVisibility(View.GONE);
            mRemoveThumb.setVisibility(View.GONE);
        }
    }


    private void postMassage() {

        String PostMsg = mPostAnswer.getText().toString();

        AllAnswersDetails Details = new AllAnswersDetails(PostMsg,ImageUrl,ThumbUrl);
        DatabaseRef.child(PostUid2).setValue(Details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    DatabaseReference tDatabaseRef = DatabaseRef.push();
                    PostUid2 = tDatabaseRef.getKey().toString();
                    mAnswerThumb.setVisibility(View.GONE);
                    isPostUpload = true;
                    mPostAnswer.setText(null);
                    Toast.makeText(getApplicationContext(), "Massage Post:Successfull", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Massage Post:Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void sendPost(String Msg, String ImageUrl, String ThumbUrl) {

        Log.d("rushabh",MainAnswer);
        Log.d("rushabh",pImageUrl);

        AllPostDetails Details = new AllPostDetails(MainAnswer,pImageUrl,Msg,ImageUrl);
        DatabaseReference pushRef = pDatabaseRef.push();
        String pushId = pushRef.getKey().toString();

        pDatabaseRef.child(pushId).setValue(Details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Answer Posted:Successfull", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Answer Posted:Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        deletThumbView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("image", "onActivityResult");

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d("image", "onCropAcivity");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                ImageUri = result.getUri();
                showThumbImage();
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        Auth.addAuthStateListener(AuthStateListener);
        Log.d("question", "onStart");

        FirebaseRecyclerAdapter<AllAnswersDetails,QuestionViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<AllAnswersDetails, QuestionViewHolder>(
                AllAnswersDetails.class,
                R.layout.single_comment_view,
                QuestionViewHolder.class,
                DatabaseRef
        ) {
            @Override
            protected void populateViewHolder(QuestionViewHolder viewHolder, final AllAnswersDetails model, int position) {

                Log.d("rushabh","yess");
                viewHolder.setThumbImage(model.getAnswermsg(), model.getThumbimage(), c);

                viewHolder.postThisAnswer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendPost(model.getAnswermsg(),model.mainimage,model.getThumbimage());
                    }});
            }
        };


        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


  public static   class QuestionViewHolder extends RecyclerView.ViewHolder {

        View cView;
        TextView answerMsg;
        ImageView thumbImage;
        final RelativeLayout.LayoutParams layoutparams;
        Button postThisAnswer;


        public QuestionViewHolder(View itemView) {
            super(itemView);
            cView = itemView;

            answerMsg = cView.findViewById(R.id.single_comment_view_answerMsg);
            thumbImage = cView.findViewById(R.id.single_comment_view_thumbImage);
            postThisAnswer = cView.findViewById(R.id.single_comment_view_postThisAns);
            layoutparams = (RelativeLayout.LayoutParams) answerMsg.getLayoutParams();
        }


        public void setThumbImage(String postMassage, String image, Context c) {
            Log.d("abhi", "url=" + image);
            if (!image.equals("no"))
                Picasso.with(c).load(image).placeholder(R.drawable.unavailablepostimage).into(thumbImage);

            answerMsg.setText(postMassage);
        }
    }

}
