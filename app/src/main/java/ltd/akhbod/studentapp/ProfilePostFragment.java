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
 * Created by ibm on 26-02-2018.
 */

public class ProfilePostFragment extends Fragment {

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
        mView = inflater.inflate(R.layout.profile_post_fragment, container, false);


        //-----------------------------Displaying Posts-----------------------------------------------------

        c = getActivity();

        DatabaseRef = FirebaseDatabase.getInstance().getReference().child("AllPosts");
        DatabaseRef.keepSynced(true);
        Auth = FirebaseAuth.getInstance();

        recyclerView = mView.findViewById(R.id.profile_post_recyclerview);
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


        final FirebaseRecyclerAdapter<AllPostDetails, ProfilePostFragment.PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AllPostDetails, ProfilePostFragment.PostViewHolder>(
                AllPostDetails.class,
                R.layout.post_single_view,
                ProfilePostFragment.PostViewHolder.class,
                DatabaseRef
        ) {
            @Override
            protected void populateViewHolder(final ProfilePostFragment.PostViewHolder viewHolder, final AllPostDetails model, int position) {

                RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) viewHolder.mRelativeLayout.getLayoutParams();

                if (model.getSavebyme().equals("yes")) {
                    param.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    param.width = RelativeLayout.LayoutParams.MATCH_PARENT;

                    final String post_id = getRef(position).getKey().toString();

                    viewHolder.setQuestion(model.getQPostmassage(), model.getQthumburl(), c);
                    viewHolder.setAnswer(model.getApostmassage(), model.getAimageurl(), c);

                    viewHolder.mSaveImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String isSave = model.getSavebyme();
                            if (isSave.equals("no")) {
                                isSave = "yes";
                                Toast.makeText(c, "post added", Toast.LENGTH_SHORT).show();
                            } else {
                                isSave = "no";
                                Toast.makeText(c, "post removed", Toast.LENGTH_SHORT).show();
                            }
                            DatabaseRef.child(post_id).child("savebyme").setValue(isSave);
                        }
                    });


                } else {
                    viewHolder.mRelativeLayout.setVisibility(View.GONE);
                    param.height = 0;
                    param.width = 0;
                }
                viewHolder.mRelativeLayout.setLayoutParams(param);


            }};


        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }



    public static class PostViewHolder extends RecyclerView.ViewHolder {

        View cView;
        TextView qPostmsg,aPostMsg;
        ImageView qThumbImage,mSaveImage,aAnswerImage;
        RelativeLayout mRelativeLayout;

        public PostViewHolder(View itemView) {
            super(itemView);
            cView = itemView;

            qPostmsg = cView.findViewById(R.id.question_queMsg);
            qThumbImage=cView.findViewById(R.id.question_thumbImage);
            aPostMsg=cView.findViewById(R.id.post_single_apostmsg);
            aAnswerImage=cView.findViewById(R.id.post_single_apostImage);

            mSaveImage=cView.findViewById(R.id.writepost_save);
            mRelativeLayout=cView.findViewById(R.id.post_single_grand);
        }



        public void setQuestion(String qPostmassage, String qthumburl, FragmentActivity c) {

            qPostmsg.setText(qPostmassage);
            if(!qthumburl.equals("no")){
                Picasso.with(c).load(qthumburl).placeholder(R.drawable.unavailablepostimage).into(qThumbImage);}
        }

        public void setAnswer(String apostmassage, String aimageurl, FragmentActivity c) {

            aPostMsg.setText(apostmassage);
            if(!aimageurl.equals("no")){
                Picasso.with(c).load(aimageurl).placeholder(R.drawable.unavailablepostimage).into(aAnswerImage);}
        }
    }




}
