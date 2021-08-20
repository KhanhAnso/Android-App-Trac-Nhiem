package com.example.apptracnghiem;

import android.provider.BaseColumns;

public final class Table {

    private Table(){}

    //class bảng các chuyên mục chủ đề câu hỏi
    public static class CategoriesTable implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME = "name";
    }
    //class bảng các câu hỏi
    public static class QuestionsTable implements BaseColumns {
        //Tên bảng
        public static final String TABLE_NAME = "questions";
        //câu hỏi
        public static final String COLUMN_QUESTION = "question";
        //Lựa chọn 1,2,3
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_OPTION4 = "option4";
        //đáp án
        public static final String COLUMN_ANSWER = "answer";
        //id chuyên mục
        public static final String COLUMN_CATEGORY_ID = "id_category";
    }
}
