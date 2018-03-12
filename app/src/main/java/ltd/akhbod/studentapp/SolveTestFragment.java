package ltd.akhbod.studentapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by ibm on 06-03-2018.
 */

public class SolveTestFragment extends Fragment implements SolveTestActivity.ObjSendListener {

    mcqtestdata ObjData=new mcqtestdata();

    public TextView editText1, editText2, editText3;
    public ImageView imageView1, imageView2, imageView3;             // image no: 1,2,3
    public String imageViewUrl1, imageViewUrl2, imageViewUrl3;



    LinearLayout mSolveTestOptionLayout1, mSolveTestOptionLayout2, mSolveTestOptionLayout3, mSolveTestOptionLayout4;
    RadioButton mRadioBtn1, mRadioBtn2, mRadioBtn3, mRadioBtn4;
    RelativeLayout mPostAnswerLayout,mNextQuestionLayout;
    Button mSubmitAnswer;
    ImageView mViewSolution;
    ScrollView mScrollView;
    String answer;
    TextView mAnsRightWrong;
    String ansRightWrong;

    TextView mEditTextOption1, mEditTextOption2, mEditTextOption3, mEditTextOption4;

    ImageView mEditImageOption1, mEditImageOption2, mEditImageOption3, mEditImageOption4;          // image no: 4,5,6
    String mEditImageOptionUrl1, mEditImageOptionUrl2, mEditImageOptionUrl3, mEditImageOptionUrl4;


    ImageView mOfficialAnswer;
    String mOfficialAnswerUrl;
    View mView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i("life","onCreateView()");

        mView=inflater.inflate(R.layout.activity_solve_test_single_fragment,container,false);

        editText1 = mView.findViewById(R.id.mtextView1);
        editText2 = mView.findViewById(R.id.mtextView2);
        editText3 = mView.findViewById(R.id.mtextView3);

        imageView1 = mView.findViewById(R.id.image1);
        imageView2 = mView.findViewById(R.id.image2);
        imageView3 = mView.findViewById(R.id.image3);

        mEditTextOption1 = mView.findViewById(R.id.solveTest_OptiontextView1);
        mEditTextOption2 = mView.findViewById(R.id.solveTest_OptiontextView2);
        mEditTextOption3 = mView.findViewById(R.id.solveTest_OptiontextView3);
        mEditTextOption4 = mView.findViewById(R.id.solveTest_OptiontextView4);

        mEditImageOption1 = mView.findViewById(R.id.solveTest_OptionImage1);
        mEditImageOption2 = mView.findViewById(R.id.solveTest_OptionImage2);
        mEditImageOption3 = mView.findViewById(R.id.solveTest_OptionImage3);
        mEditImageOption4 = mView.findViewById(R.id.solveTest_OptionImage4);

        mOfficialAnswer=mView.findViewById(R.id.solveTest_OfficialANswer);



        final ColorStateList colorStateList = new ColorStateList(
                new int[][]{

                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{

                        Color.BLACK //disabled
                        , Color.BLUE //enabled

                }
        );




        mSolveTestOptionLayout1 = mView.findViewById(R.id.solveTest_OptionLayout1);
        mSolveTestOptionLayout2 = mView.findViewById(R.id.solveTest_OptionLayout2);
        mSolveTestOptionLayout3 = mView.findViewById(R.id.solveTest_OptionLayout3);
        mSolveTestOptionLayout4 = mView.findViewById(R.id.solveTest_OptionLayout4);

        mRadioBtn1 = mView.findViewById(R.id.solveTest_RadioBtn1);
        mRadioBtn1.setButtonTintList(colorStateList);

        mRadioBtn2 = mView.findViewById(R.id.solveTest_RadioBtn2);
        mRadioBtn2.setButtonTintList(colorStateList);

        mRadioBtn3 = mView.findViewById(R.id.solveTest_RadioBtn3);
        mRadioBtn3.setButtonTintList(colorStateList);

        mRadioBtn4 = mView.findViewById(R.id.solveTest_RadioBtn4);
        mRadioBtn4.setButtonTintList(colorStateList);

        mPostAnswerLayout=mView.findViewById(R.id.solveTest_postAnswerLayout);      //submit answer layout
        mSubmitAnswer=mView.findViewById(R.id.solveTest_submitbtn);

        mNextQuestionLayout=mView.findViewById(R.id.solveTest_nextQuestionLayout);    //next button layout
        mViewSolution=mView.findViewById(R.id.solveTest_viewSolution);

        mScrollView=mView.findViewById(R.id.solveTest_ScrollView);

        mAnsRightWrong=mView.findViewById(R.id.solveTest_ansrightwrong);


        mSubmitAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                       //submit answer

                mNextQuestionLayout.setVisibility(View.VISIBLE);
                if(answer.equals(ObjData.getAnswer())){
                    ansRightWrong="Right!!";

                }else
                    ansRightWrong="Wrong!!";

                mAnsRightWrong.setText(ansRightWrong);
            }});


       mViewSolution.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

                   Picasso.with(getActivity()).load(ObjData.getOfficialanswer()).
                           placeholder(R.drawable.unavailablepostimage).into(mOfficialAnswer);

               int i=mScrollView.getMaxScrollAmount();
               mScrollView.scrollTo(0,i);
           }});



        mSolveTestOptionLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //option 1
                mRadioBtn1.setChecked(true);

                mRadioBtn2.setChecked(false);
                mRadioBtn3.setChecked(false);
                mRadioBtn4.setChecked(false);

                answer="A";
                mPostAnswerLayout.setVisibility(View.VISIBLE);

            }
        });


        mSolveTestOptionLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //option 2
                mRadioBtn2.setChecked(true);

                mRadioBtn1.setChecked(false);
                mRadioBtn3.setChecked(false);
                mRadioBtn4.setChecked(false);

                answer="B";
                mPostAnswerLayout.setVisibility(View.VISIBLE);

            }
        });


        mSolveTestOptionLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //option 3
                mRadioBtn3.setChecked(true);

                mRadioBtn1.setChecked(false);
                mRadioBtn2.setChecked(false);
                mRadioBtn4.setChecked(false);

                answer="C";
                mPostAnswerLayout.setVisibility(View.VISIBLE);

            }
        });


        mSolveTestOptionLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {           //option 4
                mRadioBtn4.setChecked(true);

                mRadioBtn1.setChecked(false);
                mRadioBtn2.setChecked(false);
                mRadioBtn3.setChecked(false);

                answer="D";
                mPostAnswerLayout.setVisibility(View.VISIBLE);

            }
        });


        return mView;
    }

    @Override
    public void sendListener(mcqtestdata obj) {
        Log.i("life","sendListener()");
        ObjData=obj;
        loadLayout();
        Log.d("loadLayout","ObjData"+ObjData);
    }

    @Override
    public void onStart() {
        Log.d("life","onStart()");
        super.onStart();

    }

    private void loadLayout() {
        Log.i("life","loadlayout()");
        if (ObjData != null) {
            Log.d("loadLayout","loadLayout");

            if (!ObjData.getQedittext1().equals("no")) {
                editText1.setText(ObjData.getQedittext1());
                Log.i("lifehack",""+editText1);
                editText1.setVisibility(View.VISIBLE);
            }
            if (!ObjData.getQedittext2().equals("no")) {
                Log.i("lifehack",""+editText2);
                editText2.setText(ObjData.getQedittext2());
                editText2.setVisibility(View.VISIBLE);
            }
            if (!ObjData.getQedittext3().equals("no")) {
                editText3.setText(ObjData.getQedittext3());
                editText3.setVisibility(View.VISIBLE);
            }

            if (!ObjData.getQeditimage1().equals("no")) {
                Log.e("objdata",""+ObjData.getQeditimage1());
                Picasso.with(getActivity()).load(ObjData.qeditimage1).
                        placeholder(R.drawable.unavailablepostimage).into(imageView1);
                imageView1.setVisibility(View.VISIBLE);
            }

            if (!ObjData.getQeditimage2().equals("no")) {
                Picasso.with(getActivity()).load(ObjData.qeditimage2).
                        placeholder(R.drawable.unavailablepostimage).into(imageView2);
                imageView2.setVisibility(View.VISIBLE);
            }

            if (!ObjData.getQeditimage3().equals("no")) {
                Picasso.with(getActivity()).load(ObjData.qeditimage3).
                        placeholder(R.drawable.unavailablepostimage).into(imageView3);
                imageView3.setVisibility(View.VISIBLE);
            }

            if (!ObjData.getAedittext1().equals("no")) {
                mEditTextOption1.setText(ObjData.getAedittext1());
                mEditTextOption1.setVisibility(View.VISIBLE);
            }
            if (!ObjData.getAedittext2().equals("no")) {
                mEditTextOption2.setText(ObjData.getAedittext2());
                mEditTextOption2.setVisibility(View.VISIBLE);
            }
            if (!ObjData.getAedittext3().equals("no")) {
                mEditTextOption3.setText(ObjData.getAedittext3());
                mEditTextOption3.setVisibility(View.VISIBLE);
            }
            if (!ObjData.getAedittext4().equals("no")) {
                mEditTextOption4.setText(ObjData.getAedittext4());
                mEditTextOption4.setVisibility(View.VISIBLE);
            }

            if (!ObjData.getAeditimage1().equals("no")) {
                Picasso.with(getActivity()).load(ObjData.getAeditimage1()).
                        placeholder(R.drawable.unavailablepostimage).into(mEditImageOption1);
                mEditImageOption1.setVisibility(View.VISIBLE);
            }

            if (!ObjData.getAeditimage2().equals("no")) {
                Picasso.with(getActivity()).load(ObjData.getAeditimage2()).
                        placeholder(R.drawable.unavailablepostimage).into(mEditImageOption2);
                mEditImageOption2.setVisibility(View.VISIBLE);
            }

            if (!ObjData.getAeditimage3().equals("no")) {
                Picasso.with(getActivity()).load(ObjData.getAeditimage3()).
                        placeholder(R.drawable.unavailablepostimage).into(mEditImageOption3);
                mEditImageOption3.setVisibility(View.VISIBLE);
            }

            if (!ObjData.getAeditimage4().equals("no")) {
                Picasso.with(getActivity()).load(ObjData.getAeditimage4()).
                        placeholder(R.drawable.unavailablepostimage).into(mEditImageOption4);
                mEditImageOption4.setVisibility(View.VISIBLE);
            }


        }
    }
}
