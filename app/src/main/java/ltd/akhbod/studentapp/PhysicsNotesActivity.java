package ltd.akhbod.studentapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PhysicsNotesActivity extends AppCompatActivity {

    FirebaseAuth.AuthStateListener AuthStateListener;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physics_notes);

        //--------------------setting recycler view----------------------------
        recyclerView=findViewById(R.id.Physicsnotes_recyclerview);
        recyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);
        AuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(mAuth.getCurrentUser()==null){ Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);  }
            }};

        //--------------------setting recycler view----------------------------



        databaseReference= FirebaseDatabase.getInstance().getReference().child("AllUnits");
        mAuth=FirebaseAuth.getInstance();


        Button mAddUnit=findViewById(R.id.Physicsnotes_addunit);
        mAddUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUnitPop();
            }});
    }




    private void showAddUnitPop() {
        final Dialog addUnitDialog;
        addUnitDialog = new Dialog(this);
        addUnitDialog.setContentView(R.layout.activity_physics_test_addunit_dialog);
        addUnitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView popupNext = addUnitDialog.findViewById(R.id.addunit_nextbtn);
        popupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mUnitText = addUnitDialog.findViewById(R.id.addunit_unitname);
                EditText mLessonNo = addUnitDialog.findViewById(R.id.addunit_numberoflesson);

                String UnitText=mUnitText.getText().toString();
                int LessonNo=Integer.parseInt(mLessonNo.getText().toString());

                AllUnitLessonDetails obj=new AllUnitLessonDetails(UnitText,LessonNo);

                DatabaseReference mRef=databaseReference.push();
                String pushID=mRef.getKey().toString();

                databaseReference.child(pushID).setValue(obj);
                addUnitDialog.dismiss();
            }
        });
        addUnitDialog.show();
    }




    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<AllUnitLessonDetails, PhysicsNotesActivity.ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AllUnitLessonDetails, PhysicsNotesActivity.ViewHolder>(
                AllUnitLessonDetails.class,
                R.layout.activity_physics_singleunitlayout,
                PhysicsNotesActivity.ViewHolder.class,
                databaseReference
        ) {

            @Override
            protected void populateViewHolder(final PhysicsNotesActivity.ViewHolder viewHolder, final AllUnitLessonDetails model, int position) {
                Log.d("question", "populateViewHolder");


                viewHolder.topicName=model.getUnitname();
                viewHolder.setLayout(model.getUnitname(),model.getLessonnos());
                viewHolder.mNextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(),PhysicsNotesPdfsActivity.class);
                        intent.putExtra("topicname",viewHolder.topicName);
                        startActivity(intent);
                    }});

            }};



        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

            public static class ViewHolder extends RecyclerView.ViewHolder {

                TextView mUnitName,mLessonNo;
                ImageView mNextBtn;
                String topicName;

                public ViewHolder(View itemView) {
                    super(itemView);
                    mUnitName=itemView.findViewById(R.id.singleunit_unittext);
                    mLessonNo=itemView.findViewById(R.id.singleunit_lessontext);
                    mNextBtn=itemView.findViewById(R.id.singleunit_nextBtn);
                }

                public void setLayout(String UnitName,int LessonNo) {

                    mUnitName.setText(UnitName);
                    mLessonNo.setText(""+LessonNo);
                }

            }
        }
