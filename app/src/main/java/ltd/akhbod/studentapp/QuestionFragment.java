package ltd.akhbod.studentapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by ibm on 21-02-2018.
 */

public class QuestionFragment extends Fragment {

        //post variables-------------
        RecyclerView recyclerView;
        DatabaseReference DatabaseRef;
        FirebaseAuth Auth;
        FirebaseAuth.AuthStateListener AuthStateListener;

        LinearLayoutManager mLayoutManager;
        FragmentActivity c;
        //post variables-------------


        View mView;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            mView= inflater.inflate(R.layout.question_fragment,container,false);



            //-----------------------------Displaying Posts-----------------------------------------------------

            c=getActivity();

            DatabaseRef= FirebaseDatabase.getInstance().getReference().child("AllQuestions");
            DatabaseRef.keepSynced(true);
            Auth=FirebaseAuth.getInstance();

            recyclerView=mView.findViewById(R.id.question_recyclerview_id);
            recyclerView.setHasFixedSize(true);

            mLayoutManager=new LinearLayoutManager(mView.getContext());

            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);

            recyclerView.setLayoutManager(mLayoutManager);
            AuthStateListener=new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if(Auth.getCurrentUser()==null){ Intent intent=new Intent(getContext(),SignUpActivity.class);  }
                }};


            //-----------------------------Displaying Posts-----------------------------------------------------






            return mView;
        }


        @Override
        public void onStart() {
            super.onStart();

            Auth.addAuthStateListener(AuthStateListener);
            Log.d("question","onStart");

            FirebaseRecyclerAdapter<AllQuestionsDetails,QuestionFragment.QuestionViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<AllQuestionsDetails,QuestionViewHolder>(
                    AllQuestionsDetails.class,
                    R.layout.question_single_view,
                    QuestionFragment.QuestionViewHolder.class,
                    DatabaseRef
            ) {

                @Override
                protected void populateViewHolder(final QuestionFragment.QuestionViewHolder viewHolder, final AllQuestionsDetails model, int position) {
                    Log.d("question","populateViewHolder");

                    final String post_id=getRef(position).getKey().toString();


                    viewHolder.setPostMassage(model.getqPostMsg());
                    viewHolder.setThumbImage(model.getqPostThumbImage(),c);

                    viewHolder.questionMsg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                    if(viewHolder.i==0){
                                     viewHolder.layoutparams.height=300; viewHolder.i=1;}
                                     else{
                                        viewHolder.layoutparams.height=200; viewHolder.i=0;}

                                     viewHolder.questionMsg.setLayoutParams(viewHolder.layoutparams);
                        }});

                    viewHolder.mAnswerNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                Intent intent=new Intent(c,QuestionCommentActivity.class);
                                intent.putExtra("msg",model.getqPostMsg());
                                intent.putExtra("imageurl",model.getqPostImage());
                                intent.putExtra("thumburl",model.getqPostThumbImage());
                                intent.putExtra("postid",post_id);
                                startActivity(intent);
                        }});


                    viewHolder.mSattred.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                                    String isStarred=model.getStarred();
                                    if(isStarred.equals("no")){ isStarred="yes";
                                        Toast.makeText(c,"question starred",Toast.LENGTH_SHORT).show();}
                                    else {
                                        isStarred = "no";
                                        Toast.makeText(c,"question unstarred",Toast.LENGTH_SHORT).show();
                                    }
                                    DatabaseRef.child(post_id).child("starred").setValue(isStarred);
                                }});
                }
            };


            recyclerView.setAdapter(firebaseRecyclerAdapter);

        }



        public static class QuestionViewHolder extends RecyclerView.ViewHolder {
            int i=0;
            View cView;
            TextView questionMsg;
            ImageView thumbImage,mSattred;
            RelativeLayout mAnswerNow;
            final RelativeLayout.LayoutParams layoutparams;



            public QuestionViewHolder(View itemView) {
                super(itemView);
                cView = itemView;
                questionMsg= cView.findViewById(R.id.question_queMsg);
                thumbImage=cView.findViewById(R.id.question_thumbImage);
                mSattred=cView.findViewById(R.id.question_starred);
                mAnswerNow=cView.findViewById(R.id.question_answer);
                layoutparams= (RelativeLayout.LayoutParams) questionMsg.getLayoutParams();
            }

            public void setPostMassage(String postMassage) {

                    questionMsg.setText(postMassage);
            }

            public void setThumbImage(String image, FragmentActivity c) {
                    if(image!="no")
                    Picasso.with(c).load(image).placeholder(R.drawable.unavailablepostimage).into(thumbImage);
            }
        }
}
