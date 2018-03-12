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
 * Created by ibm on 27-02-2018.
 */

public class ProfileQuestionSaved extends Fragment {

    View mView;
    //post variables-------------
    RecyclerView recyclerView;
    DatabaseReference DatabaseRef;
    FirebaseAuth Auth;
    FirebaseAuth.AuthStateListener AuthStateListener;

    LinearLayoutManager mLayoutManager;
    FragmentActivity c;
    //post variables-------------


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.profile_question_saved, container, false);


        c = getActivity();

        DatabaseRef = FirebaseDatabase.getInstance().getReference().child("AllQuestions");
        DatabaseRef.keepSynced(true);
        Auth = FirebaseAuth.getInstance();

        recyclerView = mView.findViewById(R.id.profile_question_saved_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));

        AuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (Auth.getCurrentUser() == null) {
                    Intent intent = new Intent(getContext(), SignUpActivity.class);
                }
            }
        };


        //-----------------------------Displaying Posts-----------------------------------------------------

        return mView;
    }


    @Override
    public void onStart() {
        super.onStart();

        Auth.addAuthStateListener(AuthStateListener);
        Log.d("abhi", "onStart");


        FirebaseRecyclerAdapter<AllQuestionsDetails, ProfileQuestionSaved.PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AllQuestionsDetails, ProfileQuestionSaved.PostViewHolder>(
                AllQuestionsDetails.class,
                R.layout.question_single_view,
                ProfileQuestionSaved.PostViewHolder.class,
                DatabaseRef
        ) {
            @Override
            protected void populateViewHolder(final ProfileQuestionSaved.PostViewHolder viewHolder, final AllQuestionsDetails model, int position) {
                Log.d("abhi", "populateViewHolder");

                RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) viewHolder.mRelativeLayout.getLayoutParams();

                if (model.getStarred().equals("yes")) {
                    param.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    param.width = RelativeLayout.LayoutParams.MATCH_PARENT;

                    final String post_id = getRef(position).getKey().toString();

                    viewHolder.setPostMassage(model.getqPostMsg());
                    viewHolder.setThumbImage(model.getqPostThumbImage(), c);

                    viewHolder.mSattred.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String isSave = model.getStarred();
                            if (isSave.equals("no")) {
                                isSave = "yes";
                                Toast.makeText(c, "post added", Toast.LENGTH_SHORT).show();
                            } else {
                                isSave = "no";
                                Toast.makeText(c, "post removed", Toast.LENGTH_SHORT).show();
                            }
                            DatabaseRef.child(post_id).child("starred").setValue(isSave);
                        }
                    });}

                    else
                {

                    viewHolder.mRelativeLayout.setVisibility(View.GONE);
                    param.height = 0;
                    param.width = 0;
                }
                viewHolder.mRelativeLayout.setLayoutParams(param);



            }
        };


        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class PostViewHolder extends RecyclerView.ViewHolder {

        int i=0;
        View cView;
        TextView questionMsg;
        ImageView thumbImage,mSattred;
        RelativeLayout mRelativeLayout;

        public PostViewHolder(View itemView) {
            super(itemView);
            cView = itemView;

            questionMsg= cView.findViewById(R.id.question_queMsg);
            thumbImage=cView.findViewById(R.id.question_thumbImage);
            mSattred=cView.findViewById(R.id.question_starred);
            mRelativeLayout=cView.findViewById(R.id.question_grand);

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

