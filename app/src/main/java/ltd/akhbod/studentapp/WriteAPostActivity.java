package ltd.akhbod.studentapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class WriteAPostActivity extends AppCompatActivity {

    String lastKey;

    //------------------firebase variables-------------
    FirebaseAuth mAuth;
    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;
    DatabaseReference mlastKey;
    String UserId;
    //------------------firebase variables-------------


    //-----------------layout variables---------------
    EditText mPostMsg;
    ImageView mCancelImage;
    Spinner mSubjectSpinner,mTopicSpinner;
    Button mAskQuestion;
    TextView mSelectSub,mSelectTopic;

    LinearLayout mAddImageBtn;
    RelativeLayout mThumbViewLayout;
    ImageView mThumbImage,mRemoveImage;
    //-----------------layout variables---------------


    //posting variable--------------------------------
    Uri ImageUri;
    String ImageUrl="no",ThumbUrl="no";
    Boolean isImageUpload=false,isPostUpload=false;
    String PostUid=null;
    ProgressDialog mProgressDialog;
    //posting variable--------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_apost);


        //--------------------initialising firebase variables---------------------------------------
        mAuth=FirebaseAuth.getInstance();
        UserId=mAuth.getCurrentUser().getUid().toString();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("AllQuestions");
        mStorageRef= FirebaseStorage.getInstance().getReference().child("AllQuestions");
        mlastKey=FirebaseDatabase.getInstance().getReference().child("lastkey");

        mDatabaseRef.keepSynced(true);
        mlastKey.keepSynced(true);

        DatabaseReference pref = mDatabaseRef.push();
        PostUid = pref.getKey().toString();
        //--------------------initialising firebase variables---------------------------------------


        //---------------------initializing layout variables----------------------------------------
        mPostMsg=findViewById(R.id.writepost_postmsg);
        mSubjectSpinner=findViewById(R.id.writepost_subject_spinner);
        mTopicSpinner=findViewById(R.id.writepost_topic_spinner);
        mSelectSub=findViewById(R.id.writepost_selectsub);
        mSelectTopic=findViewById(R.id.writepost_selecttopic);

        mAddImageBtn=findViewById(R.id.writepost_addimagelayout);
        mThumbViewLayout=findViewById(R.id.writepost_thumbviewlayout);
        mThumbImage=findViewById(R.id.writepost_thumbImage);
        mRemoveImage=findViewById(R.id.writepost_removeImage);

        mAskQuestion=findViewById(R.id.writepost_askQuestion);


        mSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String SelectedSubject=parent.getSelectedItem().toString();
                mSelectSub.setText(SelectedSubject);}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        mTopicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String SelectedSubject=parent.getSelectedItem().toString();
                mSelectTopic.setText(SelectedSubject);}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        ArrayAdapter<CharSequence> subjectAdapter=ArrayAdapter.createFromResource(this,R.array.subjects,android.R.layout.simple_spinner_item);

        ArrayAdapter topicAdapter=ArrayAdapter.createFromResource(this,R.array.topics,android.R.layout.simple_spinner_item);

        mSubjectSpinner.setAdapter(subjectAdapter);
        mTopicSpinner.setAdapter(topicAdapter);
        //---------------------initializing layout variables----------------------------------------


        //---------------------initializing listeners-----------------------------------------------

        mAddImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }});

        mAskQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogPost();
            }});


        mRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletThumbView();
                mAddImageBtn.setVisibility(View.VISIBLE);
                mThumbViewLayout.setVisibility(View.GONE);
            }});

        mAskQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMassage();
            }});
        //---------------------initializing variables-----------------------------------------------

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("learn","onStart");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
       deletThumbView();
    }

    private void deletThumbView() {

        Log.d("learn",isImageUpload+"");
        Log.d("learn",isPostUpload+"");

        if(isImageUpload==true && isPostUpload==false) {

            StorageReference deleteRef = mStorageRef.child(PostUid + "/image.jpg");
            StorageReference deletRef2=mStorageRef.child(PostUid+"/thumbimage.jpg");
            deleteRef.delete();
            deletRef2.delete();
        }
    }

    private void addImage() {

        Intent intent = CropImage.activity().setMinCropResultSize(400,400)
                .setMaxCropResultSize(3500,3500)
                .getIntent(this);
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

    }

    private void showThumbImage() {

        if(ImageUri!=null) {

            mProgressDialog= new ProgressDialog(this);
            mProgressDialog.setTitle("Uploading Profile...");
            mProgressDialog.setMessage("Please wait while we upload and process the image");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            File thumb_path=new File((ImageUri).getPath());
            Bitmap thumb_bit = new Compressor(this).setMaxHeight(200)
                                   .setMaxWidth(200).setQuality(50)
                                   .compressToBitmap(thumb_path);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumb_bit.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            final byte[] thumb_byte = baos.toByteArray();


            Log.d("postuid1",PostUid);
            mStorageRef.child(PostUid).child("image"+".jpg").putFile(ImageUri)
                                      .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content

                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            ImageUrl = downloadUrl.toString();
                            Toast.makeText(getApplicationContext(),"Image uploading:Sucessfully",Toast.LENGTH_SHORT).show();

                            final StorageReference thumb_file_path=mStorageRef.child(PostUid).child("thumbimage"+".jpg");
                            UploadTask uploadTask = (UploadTask) thumb_file_path.putBytes(thumb_byte).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    if(task.isSuccessful()){
                                        isImageUpload=true;
                                        ThumbUrl=task.getResult().getDownloadUrl().toString();

                                        mAddImageBtn.setVisibility(View.GONE);
                                        mThumbViewLayout.setVisibility(View.VISIBLE);
                                        Picasso.with(getApplicationContext()).load(ThumbUrl).
                                                placeholder(R.drawable.unavailablepostimage).into(mThumbImage);

                                        mProgressDialog.dismiss();
                                    }}});}})

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(getApplicationContext(),"Image uploading:failed",Toast.LENGTH_SHORT).show();
                            Log.d("image", "Image uploading:failed");
                        }});

            isImageUpload=true;
        }

    }



    private void postMassage() {

        String PostMsg=mPostMsg.getText().toString();

        AllQuestionsDetails Details=new AllQuestionsDetails(PostMsg,ImageUrl,ThumbUrl,"no",mAuth.getCurrentUser().getUid().toString());
        mDatabaseRef.child(PostUid).setValue(Details).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    lastKey=PostUid;
                    LastKey key=new LastKey(lastKey);

                    mlastKey.setValue(lastKey);

                    isPostUpload=true;
                    mPostMsg.setText(null);
                    Toast.makeText(getApplicationContext(),"Massage Post:Successfull",Toast.LENGTH_SHORT).show();}
                else
                    Toast.makeText(getApplicationContext(),"Massage Post:Failed",Toast.LENGTH_SHORT).show();

                Intent intent=getIntent();
                finish();
            }});

    }


    public void alertDialogPost() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Post");
        builder.setMessage("Are you sure want to Post this Query?");

        builder.setNegativeButton("Post", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                postMassage();

            }});

        Dialog dialog = builder.create();
        dialog.show();
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






}
