package ltd.akhbod.studentapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PhysicsTestActivity extends AppCompatActivity {

    List<String> unitnames = new ArrayList<>();
    List<Integer> lessonno = new ArrayList<>();

    int TOTAL_LESSON_COUNT;

    UnitNameAdapter unitNameAdapter;
    RecyclerView mMassageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physics_test);

        loadData();
        unitNameAdapter = new UnitNameAdapter(unitnames, lessonno,this);

        mMassageList = findViewById(R.id.Physicstest_recyclerview);
        LinearLayoutManager mLinearlayout = new LinearLayoutManager(this);
        mMassageList.setHasFixedSize(true);
        mMassageList.setLayoutManager(mLinearlayout);


        mMassageList.setAdapter(unitNameAdapter);



        Button addUnitBtn = findViewById(R.id.Physicstest_addunit);
        addUnitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddUnitPop();
            }
        });
    }

    private void loadData() {
        int i=1;
        try {

            while (true) {

                TOTAL_LESSON_COUNT=i;
                String unitName="unit"+i+".tmp";

                File directory = getFilesDir(); //or getExternalFilesDir(null); for external storage
                File file = new File(directory, unitName);

                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);

                AllUnitLessonSerializeDetails obj = (AllUnitLessonSerializeDetails) ois.readObject();

                unitnames.add(obj.getUnitname());
                Integer no=new Integer(obj.getLessonnos());
                lessonno.add(no);


                Log.d("samiksha", obj.getUnitname());
                Toast.makeText(this, "unit=" + obj.getUnitname() + "\nlessonno=" + obj.getLessonnos(), Toast.LENGTH_SHORT).show();


                i++;
            }

            } catch(ClassNotFoundException e){
                Log.d("samiksha", e.getMessage());

            } catch(FileNotFoundException e){
                Log.d("samiksha", e.getMessage());
            } catch(IOException e){
                Log.d("samiksha", e.getMessage());
            }
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



                AllUnitLessonSerializeDetails obj = new AllUnitLessonSerializeDetails(mUnitText.getText().toString(),Integer.parseInt(mLessonNo.getText().toString()));
                writeInFile(obj);

                addUnitDialog.dismiss();
            }
        });
        addUnitDialog.show();
    }

    private void writeInFile(AllUnitLessonSerializeDetails obj) {

        String unitName = "unit"+TOTAL_LESSON_COUNT+".tmp";
        Log.d("samiksha",unitName);
        File directory = getFilesDir(); //or getExternalFilesDir(null); for external storage
        File file = new File(directory,unitName);

        try {


            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(obj);

            unitnames.add(obj.getUnitname());
            Integer no=new Integer(obj.getLessonnos());
            lessonno.add(no);

            mMassageList.scrollToPosition(unitnames.size());
            unitNameAdapter.notifyDataSetChanged();

            TOTAL_LESSON_COUNT++;
            oos.close();

        } catch (FileNotFoundException e) {
            Log.d("samiksha",e.getMessage());

        } catch (IOException e) {
            Log.d("samiksha",e.getMessage());
        }

    }


}
