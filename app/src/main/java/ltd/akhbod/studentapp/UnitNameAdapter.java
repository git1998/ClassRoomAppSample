package ltd.akhbod.studentapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

/**
 * Created by ibm on 04-03-2018.
 */

    public class UnitNameAdapter extends RecyclerView.Adapter<UnitNameAdapter.MassageViewHolder> {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        private List<String> mUnitList;
        private List<Integer> mLessonNo;
        Context context;

        public UnitNameAdapter(List<String> mUnitList, List<Integer> mLessonNo,Context context)  {

            this.mUnitList=mUnitList;
            this.mLessonNo=mLessonNo;
            this.context=context;
        }


        @Override
        public MassageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_physics_singleunitlayout, parent, false);


            return new MassageViewHolder(v);
        }


        public class MassageViewHolder extends RecyclerView.ViewHolder {

            TextView unittext,lessontext;
            ImageView nextBtn;


            public MassageViewHolder(View itemView) {
                super(itemView);

                unittext = itemView.findViewById(R.id.singleunit_unittext);
                lessontext = itemView.findViewById(R.id.singleunit_lessontext);

            }
        }


        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(MassageViewHolder holder, final int position) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,PhysicsTestMcqActivity.class);
                    intent.putExtra("topic",mUnitList.get(position).toString());
                    context.startActivity(intent);
                }});

            holder.unittext.setText(mUnitList.get(position));
            holder.lessontext.setText(mLessonNo.get(position).toString());

        }




        @Override
        public int getItemCount() {
            return mUnitList.size();
        }
    }


