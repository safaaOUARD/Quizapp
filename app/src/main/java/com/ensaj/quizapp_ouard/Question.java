package com.ensaj.quizapp_ouard;

public class Question {
    private String questionText;
    private String imageDrawable;
    private String optionA;
    private String optionB;
    private String correctAnswer;

    public Question(String questionText, String imageDrawable,
                    String optionA, String optionB, String correctAnswer) {
        this.questionText  = questionText;
        this.imageDrawable = imageDrawable;
        this.optionA       = optionA;
        this.optionB       = optionB;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText()  { return questionText; }
    public String getImageDrawable() { return imageDrawable; }
    public String getOptionA()       { return optionA; }
    public String getOptionB()       { return optionB; }
    public String getCorrectAnswer() { return correctAnswer; }
}