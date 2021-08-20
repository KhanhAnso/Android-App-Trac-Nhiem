package com.example.apptracnghiem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apptracnghiem.database.Database;
import com.example.apptracnghiem.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuestionActivity extends AppCompatActivity {

    //Tạo các biến
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCategory;

    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;
    private ArrayList<Question> questionList;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private int questionCounter;
    private int questionSize;
    private Question currentQuestion;
    //biến điểm
    private int score;
    private boolean answered;

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //Ánh xạ id
        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCategory = findViewById(R.id.text_view_category);

        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);




        //nhận dữ liệu mức độ khó
        Intent intent = getIntent();
        int categoryID = intent.getIntExtra("idcategories", 0);
        String categoryName = intent.getStringExtra("categoriesname");
        //hiển thị chủ đề đã chọn
        textViewCategory.setText("Chủ đề: " + categoryName);

        Database dbHelper = new Database(this);
            //danh sách list chứa dữ liệu các câu hỏi,đáp án và câu trả lời
        questionList = dbHelper.getQuestions(categoryID);
        //kích cỡ của list = tổng số câu hỏi
        questionSize = questionList.size();

        //Hoán đổi vị trí các phần tử trong mảng
        Collections.shuffle(questionList);
            //show câu hỏi và đáp án
        showNextQuestion();
        //xác nhận câu trả lời



        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //nếu chưa trả lời thì nó đang là false
                if (!answered) {
                    //nếu đã chọn 1 trong 3 đáp án
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        //kiểm tra đáp án đúng chưa
                        //đúng hay sai thì cũng chạy sang câu tiếp
                        checkAnswer();
                    }
                    //Nếu chưa chọn đáp án
                    else {
                        Toast.makeText(QuestionActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                }
                //nếu đã trả lời thì ở checkAnswer() sẽ cho answered là true
                else {
                    //chuyển câu hỏi
                    showNextQuestion();
                }
            }
        });
    }


    //Hiển thị câu hỏi
    private void showNextQuestion() {
        //set màu cho đáp án
        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rb4.setTextColor(Color.BLACK);
        //xóa check
        rbGroup.clearCheck();
        //Nếu câu hỏi còn
        if (questionCounter < questionSize) {
            //Lấy dữ liệu ở vị trí questionCounter ra
            currentQuestion = questionList.get(questionCounter);
            //Gán giá trị lên màn hình
            textViewQuestion.setText(currentQuestion.getQuestion());

            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            //tăng counter
            questionCounter++;
            //hiển thị số câu còn lại vd: 2/10
            textViewQuestionCount.setText("Câu hỏi: " + questionCounter + "/" + questionSize);
            //Gán giá trị false nếu vì chưa trả lời, mới chỉ show
            answered = false;
            //gán tên cho button
            buttonConfirmNext.setText("Xác nhận");
            //gán giá trị = 30s
            timeLeftInMillis = 30000;
            //đếm ngược thơi gian trả lời
            startCountDown();
        }
        //Thoát nếu hết câu hỏi
        else {
            finishQuestion();
        }
    }




    //đếm time 30s
    private void startCountDown() {

        //chạy 30s tốc độ 1s
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //đang chạy time
                timeLeftInMillis = millisUntilFinished;
                //cập nhật time
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                //hết giờ
                timeLeftInMillis = 0;
                //cập nhật time
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }


    //update thoi gian
    private void updateCountDownText() {
        //tính phút
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        //tính giây
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        //định dạng kiểu hiển thị
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        //set lên texttime
        textViewCountDown.setText(timeFormatted);
        //kiểm tra thời gian nhỏ hơn 10s
        if (timeLeftInMillis < 10000) {
            //thời gian sẽ có màu đỏ
            textViewCountDown.setTextColor(Color.RED);
        } else {
            //không thì màu set ban đầu
            textViewCountDown.setTextColor(Color.BLACK);
        }
    }

    //Kiểm tra câu trả lời để thực hiện tăng điểm và đổi màu đáp án
    private void checkAnswer() {
        //gán true nếu đã trả lời câu hỏi này
        answered = true;
        //Kiểm tra
        //getCheckedRadioButtonId()trả về id của RadioButton được kiểm tra trong Radiogroup
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        //int	indexOfChild(View child) Trả về vị trí trong nhóm của dạng xem con đã chỉ định.
        //Cộng 1 vì đáp án là bắt đầu từ 1,2,3 còn indexOfChild() lại trả về bắt đầu từ 0,1,2..
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;
        //tăng điểm nếu trả lời đúng nếu vị trí câu trả lời giống nhau
        if (answerNr == currentQuestion.getAnswer()) {
            //Tăng điểm
            score= score + 10;
            //Hiển thị điểm
            textViewScore.setText("Điểm: " + score);
        }
        //kiểm tra đáp án
        showSolution();
    }

    
    //Hiển thị đáp án
    private void showSolution() {
        //mặc định màu đỏ
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);
        //kiểm tra đáp án
        switch (currentQuestion.getAnswer()) {
            //Nếu đáp án 1
            case 1:
                //màu xanh
                rb1.setTextColor(Color.GREEN);
                //set thông báo lên phần câu hỏi rằng đáp án là câu 1
                textViewQuestion.setText("Đáp án là A");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Đáp án là B");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Đáp án là C");
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                textViewQuestion.setText("Đáp án là D");
                break;
        }
        //Nếu còn câu hỏi
        if (questionCounter < questionSize) {
            //set button tiếp
            buttonConfirmNext.setText("Câu tiếp");
        } else {
            //set button đóng
            buttonConfirmNext.setText("Hoàn thành");
        }
        countDownTimer.cancel();
    }



    //Thoát
    private void finishQuestion() {
        // Tạo một Intent mới để chứa dữ liệu trả về
        Intent resultIntent = new Intent();
        // gửi dữ liệu điểm qua activity main
        resultIntent.putExtra("Score", score);
        // Đặt resultCode là Activity.RESULT_OK to
        // thể hiện đã thành công và có chứa kết quả trả về
        setResult(RESULT_OK, resultIntent);
        // gọi hàm finish() để đóng Activity hiện tại và trở về MainActivity.
        finish();
    }

    //back thì sẽ thoát khỏi màn hình trả lời câu hỏi
    @Override
    public void onBackPressed() {
        count++;
        //click 2 lần
        if (count >= 1) {
            //Thoát
            finishQuestion();
        }
        count = 0;
    }

}