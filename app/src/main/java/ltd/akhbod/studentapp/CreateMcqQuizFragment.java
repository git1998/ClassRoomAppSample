package ltd.akhbod.studentapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ibm on 02-03-2018.
 */

public class CreateMcqQuizFragment  extends Fragment {

    View mView;

    FragmentActivity c;
    String imageChild;

    EditText editText1, editText2, editText3;
    ImageView imageView1, imageView2, imageView3;             // image no: 1,2,3
    String imageViewUrl1, imageViewUrl2, imageViewUrl3;

    Button createTextBtn, createImageBtn;

    ImageView crossImage;


    LinearLayout mCreateQuizOptionLayout1, mCreateQuizOptionLayout2, mCreateQuizOptionLayout3, mCreateQuizOptionLayout4;

    RadioButton mRadioBtn1, mRadioBtn2, mRadioBtn3, mRadioBtn4;

    Button mTextOptionBtn1, mTextOptionBtn2, mTextOptionBtn3, mTextOptionBtn4;

    Button mImageOptionBtn1, mImageOptionBtn2, mImageOptionBtn3, mImageOptionBtn4;

    EditText mEditTextOption1, mEditTextOption2, mEditTextOption3, mEditTextOption4;

    ImageView mEditImageOption1, mEditImageOption2, mEditImageOption3, mEditImageOption4;          // image no: 4,5,6
    String mEditImageOptionUrl1, mEditImageOptionUrl2, mEditImageOptionUrl3, mEditImageOptionUrl4;

    LinearLayout mUploadSol,mUploadedSol;
    ImageView mOfficialAnswer;
    String mOfficialAnswerUrl;

    Button mDoneBtn;

    TextView mAnswerNo;
    String answer;

    int i = 0;
    int imageNo;

    //firebase variable-------------------------------------
    DatabaseReference mDatabaseRef;
    StorageReference storageRef;
    //firebase variable-------------------------------------

    public interface OnFragmentInteractionListener {
        public void onFragmentCreated (int number);
    }

    private OnFragmentInteractionListener listener;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.activity_create_mcq_quiz_single_fragment, container, false);
        c=getActivity();

        //firebase initialising------------------------------------------------

        storageRef = FirebaseStorage.getInstance().getReference().child("mcqtest").child("test1");

        //firebase initialising------------------------------------------------


        //-----------------------question_layout---------------------------------

        crossImage = mView.findViewById(R.id.createQuizCrossImage);
        createTextBtn = mView.findViewById(R.id.createQuizTextBtn);
        createImageBtn = mView.findViewById(R.id.createQuizImageBtn);

        editText1 = mView.findViewById(R.id.editText1);
        editText2 = mView.findViewById(R.id.editText2);
        editText3 = mView.findViewById(R.id.editText3);

        imageView1 = mView.findViewById(R.id.image1);
        imageView2 = mView.findViewById(R.id.image2);
        imageView3 = mView.findViewById(R.id.image3);

        createTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i == 0) {
                    editText1.setVisibility(View.VISIBLE);
                    i++;
                } else if (i == 1) {
                    editText2.setVisibility(View.VISIBLE);
                    i++;
                } else if (i == 2) {
                    editText3.setVisibility(View.VISIBLE);

                }

            }
        });

        createImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i == 0) {
                    imageNo=1;
                    addImage();
                    imageView1.setVisibility(View.VISIBLE);
                    i++;

                } else if (i == 1) {
                    imageNo=2;
                    addImage();
                    imageView2.setVisibility(View.VISIBLE);
                    i++;

                } else if (i == 2) {
                    imageNo=3;
                    addImage();
                    imageView3.setVisibility(View.VISIBLE);
                }


            }
        });

        crossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createImageBtn.setVisibility(View.GONE);
                createTextBtn.setVisibility(View.GONE);
                crossImage.setVisibility(View.GONE);
            }
        });

        //-----------------------question_layout---------------------------------


        //-----------------------option_layout------------------------------------------------------


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


        mCreateQuizOptionLayout1 = mView.findViewById(R.id.createQuiz_OptionLayout1);
        mCreateQuizOptionLayout2 = mView.findViewById(R.id.createQuiz_OptionLayout2);
        mCreateQuizOptionLayout3 = mView.findViewById(R.id.createQuiz_OptionLayout3);
        mCreateQuizOptionLayout4 = mView.findViewById(R.id.createQuiz_OptionLayout4);

        mRadioBtn1 = mView.findViewById(R.id.createQuiz_RadioBtn1);
        mRadioBtn1.setButtonTintList(colorStateList);

        mRadioBtn2 = mView.findViewById(R.id.createQuiz_RadioBtn2);
        mRadioBtn2.setButtonTintList(colorStateList);

        mRadioBtn3 = mView.findViewById(R.id.createQuiz_RadioBtn3);
        mRadioBtn3.setButtonTintList(colorStateList);

        mRadioBtn4 = mView.findViewById(R.id.createQuiz_RadioBtn4);
        mRadioBtn4.setButtonTintList(colorStateList);


        mTextOptionBtn1 = mView.findViewById(R.id.createQuizOptionTextBtn1);
        mTextOptionBtn2 = mView.findViewById(R.id.createQuizOptionTextBtn2);
        mTextOptionBtn3 = mView.findViewById(R.id.createQuizOptionTextBtn3);
        mTextOptionBtn4 = mView.findViewById(R.id.createQuizOptionTextBtn4);

        mImageOptionBtn1 = mView.findViewById(R.id.createQuizOptionImageBtn1);
        mImageOptionBtn2 = mView.findViewById(R.id.createQuizOptionImageBtn2);
        mImageOptionBtn3 = mView.findViewById(R.id.createQuizOptionImageBtn3);
        mImageOptionBtn4 = mView.findViewById(R.id.createQuizOptionImageBtn4);

        mEditTextOption1 = mView.findViewById(R.id.createQuiz_OptionEditText1);
        mEditTextOption2 = mView.findViewById(R.id.createQuiz_OptionEditText2);
        mEditTextOption3 = mView.findViewById(R.id.createQuiz_OptionEditText3);
        mEditTextOption4 = mView.findViewById(R.id.createQuiz_OptionEditText4);

        mEditImageOption1 = mView.findViewById(R.id.createQuiz_OptionImage1);
        mEditImageOption2 = mView.findViewById(R.id.createQuiz_OptionImage2);
        mEditImageOption3 = mView.findViewById(R.id.createQuiz_OptionImage3);
        mEditImageOption4 = mView.findViewById(R.id.createQuiz_OptionImage4);


        mTextOptionBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                           //option 1
                mEditTextOption1.setVisibility(View.VISIBLE);
                mTextOptionBtn1.setVisibility(View.GONE);
                mImageOptionBtn1.setVisibility(View.GONE);

            }
        });

        mImageOptionBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageNo=4;
                addImage();

                mEditImageOption1.setVisibility(View.VISIBLE);
                mTextOptionBtn1.setVisibility(View.GONE);
                mImageOptionBtn1.setVisibility(View.GONE);
            }
        });

        mCreateQuizOptionLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioBtn1.setChecked(true);

                mRadioBtn2.setChecked(false);
                mRadioBtn3.setChecked(false);
                mRadioBtn4.setChecked(false);

                answer="A";

            }
        });


        mTextOptionBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                           //option 2
                mEditTextOption2.setVisibility(View.VISIBLE);
                mTextOptionBtn2.setVisibility(View.GONE);
                mImageOptionBtn2.setVisibility(View.GONE);


            }
        });

        mImageOptionBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageNo=5;
                addImage();

                mEditImageOption2.setVisibility(View.VISIBLE);
                mTextOptionBtn2.setVisibility(View.GONE);
                mImageOptionBtn2.setVisibility(View.GONE);
            }
        });

        mCreateQuizOptionLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioBtn2.setChecked(true);

                mRadioBtn1.setChecked(false);
                mRadioBtn3.setChecked(false);
                mRadioBtn4.setChecked(false);

                answer="B";
            }
        });


        mTextOptionBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                           //option 3
                mEditTextOption3.setVisibility(View.VISIBLE);
                mTextOptionBtn3.setVisibility(View.GONE);
                mImageOptionBtn3.setVisibility(View.GONE);
            }
        });

        mImageOptionBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageNo=6;
                addImage();

                mEditImageOption3.setVisibility(View.VISIBLE);
                mTextOptionBtn3.setVisibility(View.GONE);
                mImageOptionBtn3.setVisibility(View.GONE);
            }
        });

        mCreateQuizOptionLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioBtn3.setChecked(true);

                mRadioBtn1.setChecked(false);
                mRadioBtn2.setChecked(false);
                mRadioBtn4.setChecked(false);

                answer="C";
            }
        });


        mTextOptionBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                           //option 4
                mEditTextOption4.setVisibility(View.VISIBLE);
                mTextOptionBtn4.setVisibility(View.GONE);
                mImageOptionBtn4.setVisibility(View.GONE);
            }
        });

        mImageOptionBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageNo=7;
                addImage();

                mEditImageOption4.setVisibility(View.VISIBLE);
                mTextOptionBtn4.setVisibility(View.GONE);
                mImageOptionBtn4.setVisibility(View.GONE);
            }
        });

        mCreateQuizOptionLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioBtn4.setChecked(true);

                mRadioBtn1.setChecked(false);
                mRadioBtn2.setChecked(false);
                mRadioBtn3.setChecked(false);

                answer="D";
            }
        });

        //-----------------------question_layout----------------------------------------------------

//-----------------------upload solution layout---------------------------------------------

        mUploadSol = mView.findViewById(R.id.createQuiz_uploadSolutionLayout);
        mUploadedSol = mView.findViewById(R.id.createQuiz_uploadedSolutionLayout);
        mOfficialAnswer=mView.findViewById(R.id.createQuiz_OfficialANswer);
        mAnswerNo=mView.findViewById(R.id.createQuiz_answerNo);

        mUploadSol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUploadSol.setVisibility(View.GONE);
                imageNo=8;
                addImage();

                mAnswerNo.setText(""+answer+".");
                mUploadedSol.setVisibility(View.VISIBLE);
            }
        });

        //-----------------------upload solution layout---------------------------------------------


        mDoneBtn=mView.findViewById(R.id.createQuiz_doneBtn);
        mDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.d("done","done");
                   CreateMcqQuizActivity c=new CreateMcqQuizActivity();
                   listener.onFragmentCreated(10);
            }});



        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //--------------------question layout-------------------------------------------------------
        outState.putInt("oimageView1",imageView1.getVisibility());
        outState.putInt("oimageView2",imageView2.getVisibility());
        outState.putInt("oimageView3",imageView3.getVisibility());

        outState.putString("oimageViewUrl1",imageViewUrl1);
        outState.putString("oimageViewUrl2",imageViewUrl2);
        outState.putString("oimageViewUrl3",imageViewUrl3);

        outState.putInt("otextView1",editText1.getVisibility());
        outState.putInt("otextView2",editText2.getVisibility());
        outState.putInt("otextView3",editText3.getVisibility());

        outState.putInt("ocrossBtn",crossImage.getVisibility());
        //--------------------question layout-------------------------------------------------------


        //---------------------option layout--------------------------------------------------------
        outState.putBoolean("oradioBtn1",mRadioBtn1.isChecked());
        outState.putInt("oimageOptionBtn1",mImageOptionBtn1.getVisibility());
        outState.putInt("otextOptionBtn1",mTextOptionBtn1.getVisibility());
        outState.putInt("oeditTextOption1",mEditTextOption1.getVisibility());       //option 1
        outState.putInt("oeditImageOption1",mEditImageOption1.getVisibility());
        outState.putString("oeditImageOptionUrl1",mEditImageOptionUrl1);


        outState.putBoolean("oradioBtn2",mRadioBtn2.isChecked());
        outState.putInt("oimageOptionBtn2",mImageOptionBtn2.getVisibility());
        outState.putInt("otextOptionBtn2",mTextOptionBtn2.getVisibility());
        outState.putInt("oeditTextOption2",mEditTextOption2.getVisibility());       //option 2
        outState.putInt("oeditImageOption2",mEditImageOption2.getVisibility());
        outState.putString("oeditImageOptionUrl2",mEditImageOptionUrl2);


        outState.putBoolean("oradioBtn3",mRadioBtn3.isChecked());
        outState.putInt("oimageOptionBtn3",mImageOptionBtn3.getVisibility());
        outState.putInt("otextOptionBtn3",mTextOptionBtn3.getVisibility());
        outState.putInt("oeditTextOption3",mEditTextOption3.getVisibility());       //option 3
        outState.putInt("oeditImageOption3",mEditImageOption3.getVisibility());
        outState.putString("oeditImageOptionUrl3",mEditImageOptionUrl3);


        outState.putBoolean("oradioBtn4",mRadioBtn4.isChecked());
        outState.putInt("oimageOptionBtn4",mImageOptionBtn4.getVisibility());
        outState.putInt("otextOptionBtn4",mTextOptionBtn4.getVisibility());
        outState.putInt("oeditTextOption4",mEditTextOption4.getVisibility());       //option 1
        outState.putInt("oeditImageOption4",mEditImageOption4.getVisibility());
        outState.putString("oeditImageOptionUrl4",mEditImageOptionUrl4);
        //---------------------option layout--------------------------------------------------------


        //---------------------upload solution------------------------------------------------------
        outState.putInt("ouploadSol",mUploadSol.getVisibility());
        outState.putInt("ouploadedSol",mUploadedSol.getVisibility());
        outState.putInt("oofficialAnswer",mOfficialAnswer.getVisibility());
        outState.putString("oofficialAnswerUrl",mOfficialAnswerUrl);
        //---------------------upload solution------------------------------------------------------

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null) {


            //-------------------question layout----------------------------------------------------

            if(savedInstanceState.getInt("ocrossBtn")==View.GONE){
                crossImage.setVisibility(View.GONE);
                createImageBtn.setVisibility(View.GONE);
                createTextBtn.setVisibility(View.GONE);
            }

            editText1.setVisibility(savedInstanceState.getInt("otextView1"));
            editText2.setVisibility(savedInstanceState.getInt("otextView2"));
            editText3.setVisibility(savedInstanceState.getInt("otextView3"));

            imageView1.setVisibility(savedInstanceState.getInt("oimageView1"));
            imageView2.setVisibility(savedInstanceState.getInt("oimageView2"));
            imageView3.setVisibility(savedInstanceState.getInt("oimageView3"));

            imageViewUrl1=savedInstanceState.getString("oimageViewUrl1");
            if(imageViewUrl1!=null) {   Picasso.with(getActivity()).load(imageViewUrl1).
                    placeholder(R.drawable.unavailablepostimage).into(imageView1);     }

            imageViewUrl2=savedInstanceState.getString("oimageViewUrl2");
            if(imageViewUrl2!=null) {   Picasso.with(getActivity()).load(imageViewUrl2).
                    placeholder(R.drawable.unavailablepostimage).into(imageView2);     }

            imageViewUrl3=savedInstanceState.getString("oimageViewUrl3");
            if(imageViewUrl3!=null) {   Picasso.with(getActivity()).load(imageViewUrl3).
                    placeholder(R.drawable.unavailablepostimage).into(imageView3);     }

            if(savedInstanceState.getInt("ocrossBtn")==View.GONE) {
                createTextBtn.setVisibility(View.GONE);
                createImageBtn.setVisibility(View.GONE);
            }
            //-------------------question layout----------------------------------------------------


            //------------------option layout-------------------------------------------------------
            mRadioBtn1.setChecked(savedInstanceState.getBoolean("oradioBtn1"));
            mImageOptionBtn1.setVisibility(savedInstanceState.getInt("oimageOptionBtn1"));
            mTextOptionBtn1.setVisibility(savedInstanceState.getInt("otextOptionBtn1"));        //option 1
            mEditTextOption1.setVisibility(savedInstanceState.getInt("oeditTextOption1"));
            mEditImageOption1.setVisibility(savedInstanceState.getInt("oeditImageOption1"));

            mEditImageOptionUrl1=savedInstanceState.getString("oeditImageOptionUrl1");
            if(mEditImageOptionUrl1!=null){
                Picasso.with(getActivity()).load(mEditImageOptionUrl1).
                        placeholder(R.drawable.unavailablepostimage).into(mEditImageOption1);
            }


            mRadioBtn2.setChecked(savedInstanceState.getBoolean("oradioBtn2"));
            mImageOptionBtn2.setVisibility(savedInstanceState.getInt("oimageOptionBtn2"));
            mTextOptionBtn2.setVisibility(savedInstanceState.getInt("otextOptionBtn2"));        //option 1
            mEditTextOption2.setVisibility(savedInstanceState.getInt("oeditTextOption2"));
            mEditImageOption2.setVisibility(savedInstanceState.getInt("oeditImageOption2"));

            mEditImageOptionUrl2=savedInstanceState.getString("oeditImageOptionUrl2");
            if(mEditImageOptionUrl2!=null){
                Picasso.with(getActivity()).load(mEditImageOptionUrl2).
                        placeholder(R.drawable.unavailablepostimage).into(mEditImageOption2);
            }


            mRadioBtn3.setChecked(savedInstanceState.getBoolean("oradioBtn3"));
            mImageOptionBtn3.setVisibility(savedInstanceState.getInt("oimageOptionBtn3"));
            mTextOptionBtn3.setVisibility(savedInstanceState.getInt("otextOptionBtn3"));        //option 1
            mEditTextOption3.setVisibility(savedInstanceState.getInt("oeditTextOption3"));
            mEditImageOption3.setVisibility(savedInstanceState.getInt("oeditImageOption3"));

            mEditImageOptionUrl3=savedInstanceState.getString("oeditImageOptionUrl3");
            if(mEditImageOptionUrl3!=null){
                Picasso.with(getActivity()).load(mEditImageOptionUrl3).
                        placeholder(R.drawable.unavailablepostimage).into(mEditImageOption3);
            }


            mRadioBtn4.setChecked(savedInstanceState.getBoolean("oradioBtn4"));
            mImageOptionBtn4.setVisibility(savedInstanceState.getInt("oimageOptionBtn4"));
            mTextOptionBtn4.setVisibility(savedInstanceState.getInt("otextOptionBtn4"));        //option 1
            mEditTextOption4.setVisibility(savedInstanceState.getInt("oeditTextOption4"));
            mEditImageOption4.setVisibility(savedInstanceState.getInt("oeditImageOption4"));

            mEditImageOptionUrl4=savedInstanceState.getString("oeditImageOptionUrl4");
            if(mEditImageOptionUrl4!=null){
                Picasso.with(getActivity()).load(mEditImageOptionUrl4).
                        placeholder(R.drawable.unavailablepostimage).into(mEditImageOption4);
            }
            //------------------option layout-------------------------------------------------------


            //-------------------question layout----------------------------------------------------
            mUploadSol.setVisibility(savedInstanceState.getInt("ouploadSol"));
            mUploadedSol.setVisibility(savedInstanceState.getInt("ouploadedSol"));
            mOfficialAnswer.setVisibility(savedInstanceState.getInt("oofficialAnswer"));

            mOfficialAnswerUrl=savedInstanceState.getString("oofficialAnswerUrl");
            if(mOfficialAnswerUrl!=null){
                Picasso.with(getActivity()).load(mOfficialAnswerUrl).
                        placeholder(R.drawable.unavailablepostimage).into(mOfficialAnswer);
            }
            //-------------------question layout----------------------------------------------------



            }


        }

    @Override
    public void onAttach(Activity activity) {
        try {
            listener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
        super.onAttach(activity);
    }

    public void uploadToServer() {


    }

    private void addImage() {

        Intent intent = CropImage.activity().setMinCropResultSize(400, 400)
                .setMaxCropResultSize(3500, 3500)
                .getIntent(getActivity());
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("image", "onActivityResult");

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d("image", "onCropAcivity");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri ImageUri = result.getUri();
                postAndShowImage(ImageUri);
            }
        }
    }

    private void postAndShowImage(Uri imageUri) {

        switch (imageNo) {

            case 1:
                imageChild = "imageView1";
                break;
            case 2:
                imageChild = "imageView2";
                break;
            case 3:
                imageChild = "imageView3";
                break;
            case 4:
                imageChild = "imageOption1";
                break;
            case 5:
                imageChild = "imageOption2";
                break;
            case 6:
                imageChild = "imageOption3";
                break;
            case 7:
                imageChild = "imageOption4";
                break;
            case 8:
                imageChild="OfficialAnswer";
                break;
        }

        storageRef.child(imageChild+".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
             String Url=taskSnapshot.getDownloadUrl().toString();
              Log.d("test",Url);
             switch (imageChild){
                 case "imageView1":
                                    imageViewUrl1=Url;
                                    Picasso.with(getActivity()).load(Url).
                                    placeholder(R.drawable.unavailablepostimage).into(imageView1); break;

                 case "imageView2":
                                    imageViewUrl2=Url;
                                    Picasso.with(getActivity()).load(Url).
                                    placeholder(R.drawable.unavailablepostimage).into(imageView2);break;

                 case "imageView3":
                                    imageViewUrl3=Url;
                                    Picasso.with(getActivity()).load(Url).
                                    placeholder(R.drawable.unavailablepostimage).into(imageView3);break;

                 case "imageOption1":
                                    mEditImageOptionUrl1=Url;
                                    Picasso.with(getActivity()).load(Url).
                                    placeholder(R.drawable.unavailablepostimage).into(mEditImageOption1);break;

                 case "imageOption2":
                                    mEditImageOptionUrl2=Url;
                                    Picasso.with(getActivity()).load(Url).
                                    placeholder(R.drawable.unavailablepostimage).into(mEditImageOption2);break;

                 case "imageOption3":
                                    mEditImageOptionUrl3=Url;
                                    Picasso.with(getActivity()).load(Url).
                                    placeholder(R.drawable.unavailablepostimage).into(mEditImageOption3);break;

                 case "imageOption4":
                                    mEditImageOptionUrl4=Url;
                                    Picasso.with(getActivity()).load(Url).
                                    placeholder(R.drawable.unavailablepostimage).into(mEditImageOption4);break;

                 case "OfficialAnswer":
                                    mOfficialAnswerUrl=Url;
                                    Picasso.with(getActivity()).load(Url).
                                    placeholder(R.drawable.unavailablepostimage).into(mOfficialAnswer);break;
             }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }});

    }

    @Override
    public void onStart() {
        super.onStart();
        c=getActivity();
    }

    public void uploadDataToServer(final int queno, DatabaseReference mRef) {



        mcqtestdata m=new mcqtestdata(
                editText1.getText().toString().isEmpty() ? "no":editText1.getText().toString(),
                editText2.getText().toString().isEmpty() ? "no":editText2.getText().toString(),
                editText3.getText().toString().isEmpty() ? "no":editText3.getText().toString(),


                imageViewUrl1==null ? "no" : imageViewUrl1,
                imageViewUrl2==null ? "no" : imageViewUrl2,
                imageViewUrl3==null ? "no" : imageViewUrl3,

                mEditTextOption1.getText().toString().isEmpty() ? "no" : mEditTextOption1.getText().toString(),
                mEditTextOption1.getText().toString().isEmpty() ? "no" : mEditTextOption1.getText().toString(),
                mEditTextOption1.getText().toString().isEmpty() ? "no" : mEditTextOption1.getText().toString(),
                mEditTextOption1.getText().toString().isEmpty() ? "no" : mEditTextOption1.getText().toString(),

                mEditImageOptionUrl1==null ? "no" : mEditImageOptionUrl1,
                mEditImageOptionUrl2==null ? "no" : mEditImageOptionUrl2,
                mEditImageOptionUrl3==null ? "no" : mEditImageOptionUrl3,
                mEditImageOptionUrl4==null ? "no" : mEditImageOptionUrl4,

                mOfficialAnswerUrl==null ? "no" : mOfficialAnswerUrl,

                answer
        );

                String queNoChild = null;
                int k=queno;
                if (k==1){  queNoChild="que1";    }
                else if (k==2){  queNoChild="que2";    }
                else if (k==3){  queNoChild="que3";    }
                else if (k==4){  queNoChild="que4";    }
                else if (k==5){  queNoChild="que5";    }
                else if (k==6){  queNoChild="que6";    }
                else if (k==7){  queNoChild="que7";    }
                else if (k==8){  queNoChild="que8";    }
                else if (k==9){  queNoChild="que9";    }
                else if (k==10){  queNoChild="que10";    }

                DatabaseReference mref=mRef.child(queNoChild);

        final String finalQueNoChild = queNoChild;
        mref.setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(c, finalQueNoChild +" uploded",Toast.LENGTH_SHORT).show();
                        if(queno==10){
                        Toast.makeText(c,"Test uplodeded Successfully",Toast.LENGTH_SHORT).show();}
                    }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(c, finalQueNoChild +"uploading failed",Toast.LENGTH_SHORT).show();
            }});
    }



}

