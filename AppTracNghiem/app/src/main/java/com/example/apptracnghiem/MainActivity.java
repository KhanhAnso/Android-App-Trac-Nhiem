package com.example.apptracnghiem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.apptracnghiem.database.Database;
import com.example.apptracnghiem.model.Category;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textViewHighscore;
    private Spinner spinnerCategory;
    private Button buttonStartQuestion;
    private int highscore; //chưa

    private static final int REQUEST_CODE_QUESTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //xánh xạ id
        anhXa();
        //load chủ đề
        loadCategories();
        //load điểm lên
        loadHighscore();

        //button click bắt đầu
        buttonStartQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuestion();
            }
        });

    }
    //hàm ánh xạ
    private void anhXa(){
        textViewHighscore = findViewById(R.id.textview_high_score);
        spinnerCategory = findViewById(R.id.spinner_category);
        buttonStartQuestion = findViewById(R.id.button_start_question);
    }
    //load chủ đề
    private void loadCategories() {

        Database database = new Database(this);
        //Lấy dữ liệu danh sách chủ đề
        List<Category> categories = database.getDataCategories();
        //Tạo adapter
        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        //Bố cục hiển thị của danh sách chủ đề
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Gán chủ đề lên spinner
        spinnerCategory.setAdapter(adapterCategories);
    }

    //hàm bắt đầu câu hỏi
    private void startQuestion() {
        //Lấy id, name chủ đề đã chọn
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();

        //Chuyển qua activity Question
        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
        //Gửi dữ liệu id,name
        intent.putExtra("idcategories", categoryID);
        intent.putExtra("categoriesname", categoryName);
        //Sử dụng startActivityForResult để có thể nhận lại kết quả trả về thông qua phương thức onActivityResult
        startActivityForResult(intent, REQUEST_CODE_QUESTION);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //nếu requestcod = 1
        // Kiểm tra requestCode có trùng với REQUEST_CODE vừa dùng
        if (requestCode == REQUEST_CODE_QUESTION) {
            //neus resuiltcode = -1
            // resultCode được set bởi QuizActivity
            // RESULT_OK chỉ ra rằng kết quả này đã thành công
            if (resultCode == RESULT_OK) {
                //gán dữ liệu điểm ở màn hình câu hỏi sang
                // Nhận dữ liệu từ Intent trả về
                int score = data.getIntExtra("Score", 0);
                //nếu điểm lớn hơn điểm cao nhất đã có
                if (score > highscore) {
                    //update điểm
                    updateHighscore(score);
                }
            }
        }
    }


    //Load điểm ở màn câu hỏi sang
    private void loadHighscore() {
        //lưu trữ điểm
        SharedPreferences prefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        highscore = prefs.getInt("keyHighscore", 0);
        //hiển thị điểm
        textViewHighscore.setText("Điểm cao: " + highscore);
    }

    //update điểm
    private void updateHighscore(int highscoreNew) {
        //gán điểm mới
        highscore = highscoreNew;
        //hiển thị
        textViewHighscore.setText("Điểm cao: " + highscore);
        //điểm lưu trữ vào sharePreferences
        SharedPreferences prefs = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        //sửa đổi giá trị trong sharePreferences với editer
        SharedPreferences.Editor editor = prefs.edit();
        //gán lại giá trị cho điểm cao mới
        editor.putInt("keyHighscore", highscore);
        //gọi apply để hoàn tất
        editor.apply();
    }
}