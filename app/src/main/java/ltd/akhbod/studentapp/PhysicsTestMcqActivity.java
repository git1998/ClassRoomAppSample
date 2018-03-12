package ltd.akhbod.studentapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PhysicsTestMcqActivity extends AppCompatActivity {


    //post variables-------------
    RecyclerView recyclerView;
    DatabaseReference DatabaseRef;
    FirebaseAuth Auth;
    FirebaseAuth.AuthStateListener AuthStateListener;

    LinearLayoutManager mLayoutManager;
    //post variables-------------

    String topic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physics_test_mcq);

        Intent intent=getIntent();
        topic=intent.getStringExtra("topic");
        //-----------------------------Displaying Posts-----------------------------------------------------
        DatabaseRef= FirebaseDatabase.getInstance().getReference().child("alltopics").child(topic);
        DatabaseRef.keepSynced(true);
        Auth=FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.physicstestmcq_recyclerview);
        recyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(this);

        recyclerView.setLayoutManager(mLayoutManager);

        FloatingActionButton floatingActionButton=findViewById(R.id.physicstestmcq_floatingActionBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddtestPop();
            }});

        AuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(Auth.getCurrentUser()==null){ Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);  }
            }};

        //-----------------------------Displaying Posts-----------------------------------------------------



    }

    @Override
    public void onStart() {
        super.onStart();

        Auth.addAuthStateListener(AuthStateListener);
        Log.d("question","onStart");

        FirebaseRecyclerAdapter<AllSingleUnitDetails,PhysicsTestMcqActivity.PostViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<AllSingleUnitDetails,PhysicsTestMcqActivity.PostViewHolder>(
                AllSingleUnitDetails.class,
                R.layout.activity_physics_singleunit2layout,
                PhysicsTestMcqActivity.PostViewHolder.class,
                DatabaseRef
        ) {

            @Override
            protected void populateViewHolder(final PhysicsTestMcqActivity.PostViewHolder viewHolder, final AllSingleUnitDetails model, int position) {
                Log.d("question","populateViewHolder");

                final String post_id=getRef(position).getKey().toString();


                viewHolder.setTestName(model.getTestname());
                viewHolder.setNos(model.getNosq());


                viewHolder.mNextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(getApplicationContext(),SolveTestActivity.class);
                        intent.putExtra("testname",model.getTestname());
                        intent.putExtra("nos",model.getNosq());
                        intent.putExtra("topic",topic);
                        startActivity(intent);
                    }});

                viewHolder.mSaveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*String isStarred=model.getStarred();
                        if(isStarred.equals("no")){ isStarred="yes";
                            Toast.makeText(c,"question starred",Toast.LENGTH_SHORT).show();}
                        else {
                            isStarred = "no";
                            Toast.makeText(c,"question unstarred",Toast.LENGTH_SHORT).show();
                        }
                        DatabaseRef.child(post_id).child("starred").setValue(isStarred);
                    */}
                });


        }};


        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }



    public static class PostViewHolder extends RecyclerView.ViewHolder {
        int i=0;
        View mView;
        TextView mTestName,mNos;
        ImageView mNextBtn,mSaveBtn;


        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mTestName=mView.findViewById(R.id.singleunit2_testname);
            mNos=mView.findViewById(R.id.singleunit2_nos);
            mNextBtn=mView.findViewById(R.id.singleunit2_nextbtn);
            mSaveBtn=mView.findViewById(R.id.singleunit2_save);
        }

        public void setTestName(String text) {

            mTestName.setText(text);
        }

        public void setNos(int temp) {
            mNos.setText(temp+"lessons");
        }
    }



    private void showAddtestPop() {
        final Dialog addUnitDialog;
        addUnitDialog = new Dialog(this);
        addUnitDialog.setContentView(R.layout.activity_physics_test_addtest_dialog);
        addUnitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView popupNext = addUnitDialog.findViewById(R.id.addtest_nextbtn);
        popupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mUnitText = addUnitDialog.findViewById(R.id.addtest_testname);
                EditText mNos = addUnitDialog.findViewById(R.id.addtest_numberofquestions);

                Intent intent=new Intent(getApplicationContext(),CreateMcqQuizActivity.class);
                intent.putExtra("testname",mUnitText.getText().toString());
                intent.putExtra("questions",mNos.getText().toString());
                intent.putExtra("topic",topic);
                addUnitDialog.dismiss();

                startActivity(intent);


            }
        });
        addUnitDialog.show();
    }

}
