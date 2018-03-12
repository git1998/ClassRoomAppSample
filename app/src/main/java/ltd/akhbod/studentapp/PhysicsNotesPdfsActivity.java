package ltd.akhbod.studentapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class PhysicsNotesPdfsActivity extends AppCompatActivity {

    FirebaseAuth.AuthStateListener AuthStateListener;
    StorageReference mStorageReference;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;

    EditText mPdfName;
    int PICK_PDF_CODE=1234;
    String pdfUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physics_notes_pdfs);


        Intent intent=getIntent();
        String topicname=intent.getStringExtra("topicname");

        databaseReference= FirebaseDatabase.getInstance().getReference().child("AllPdfs").child(topicname);
        mStorageReference= FirebaseStorage.getInstance().getReference().child("AllPdfs");

        //--------------------setting recycler view----------------------------
        recyclerView=findViewById(R.id.Physicsnotespdf_recyclerview);
        recyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        AuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(mAuth.getCurrentUser()==null){ Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);  }
            }};

        //--------------------setting recycler view----------------------------

        FloatingActionButton floatingActionButton=findViewById(R.id.Physicsnotespdf_floatingBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUnitPop();
            }});
    }


    private void showAddUnitPop() {

        final Dialog addPdfDialog;
        addPdfDialog = new Dialog(this);
        addPdfDialog.setContentView(R.layout.activity_physics_notes_addpdf_dialog);
        addPdfDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView popupNext = addPdfDialog.findViewById(R.id.addpdf_nextbtn);
        mPdfName=addPdfDialog.findViewById(R.id.addpdf_testname);
        LinearLayout mAddAttachment=addPdfDialog.findViewById(R.id.addpdf_attachpdf);

        mAddAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                    return;
                }

                //creating an intent for file chooser
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);

            }});

        popupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPdfName = addPdfDialog.findViewById(R.id.addpdf_testname);

                String PdfName=mPdfName.getText().toString();

                AllPdfsDetails obj = new AllPdfsDetails(mPdfName.getText().toString(),pdfUrl);
                DatabaseReference mRef = databaseReference.push();
                String PushId = mRef.getKey().toString();
                databaseReference.child(PushId).setValue(obj);

                databaseReference.child(PushId).setValue(obj);
                addPdfDialog.dismiss();
            }
        });
        addPdfDialog.show();
    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<AllPdfsDetails, PhysicsNotesPdfsActivity.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AllPdfsDetails, PhysicsNotesPdfsActivity.ViewHolder>(
                AllPdfsDetails.class,
                R.layout.activity_physics_notes_single_pdf_layout,
                PhysicsNotesPdfsActivity.ViewHolder.class,
                databaseReference
        ) {

            @Override
            protected void populateViewHolder(final PhysicsNotesPdfsActivity.ViewHolder viewHolder, final AllPdfsDetails model, int position) {
                Log.d("question", "populateViewHolder");


                viewHolder.setLayout(model.getTopicname());
                viewHolder.mTopicName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Opening the upload file in browser using the upload url
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(model.pdfurl));
                        startActivity(intent);
                    }});

            }};



        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTopicName;

        public ViewHolder(View itemView) {
            super(itemView);
            mTopicName=itemView.findViewById(R.id.singlepdf_topicname);
        }


        public void setLayout(String topicname) {
            mTopicName.setText(topicname);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFile(Uri data){

        StorageReference sRef = mStorageReference.child(mPdfName.getText().toString()).child(System.currentTimeMillis() + ".pdf");

        StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = sRef.putFile(data)

                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        pdfUrl=taskSnapshot.getDownloadUrl().toString();
                        Toast.makeText(getApplicationContext(), "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}
