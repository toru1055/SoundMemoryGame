package jp.thotta.android.soundmemorygame;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by thotta on 14/11/23.
 */
public class GameManager {
    private static GameManager instance = new GameManager();
    public static final int STATUS_INITIALIZED = 0;
    public static final int STATUS_QUESTIONING = 1;
    public static final int STATUS_WAIT_ANSWER = 2;

    private GameActivity fGameActivity;
    private int fStatus = STATUS_INITIALIZED;
    private List<Question> questionList = new LinkedList<Question>();
    private Animation fAnimation = null;
    private int fCurrentQuestionPointer = 0;
    private View fStartButton;

    private GameManager(){}

    public static GameManager getInstance() {
        return instance;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.fGameActivity = gameActivity;
        this.fStartButton = fGameActivity.findViewById(R.id.button_start_stop);
        resetQuestionList();
    }

    public void nextQuestion() {
        addQuestion();
        fStartButton.startAnimation(fAnimation);
    }

    public void repeatQuestion() {
        fStartButton.startAnimation(fAnimation);
    }

    public void answer(int buttonId) {
        int row = (int) buttonId / fGameActivity.getRow();
        int column = buttonId % fGameActivity.getRow();
        Question answerForCurrentQuestion = new Question(row, column);
        Question currentQuestion = questionList.get(fCurrentQuestionPointer);
        if(currentQuestion.equals(answerForCurrentQuestion)) {
            fCurrentQuestionPointer++;
            if(fCurrentQuestionPointer >= questionList.size()) {
                fCurrentQuestionPointer = 0;
                Toast.makeText(fGameActivity, "Correct answer!", Toast.LENGTH_SHORT).show();
                nextQuestion();
            }
        } else {
            fCurrentQuestionPointer = 0;
            Toast.makeText(fGameActivity, "Incorrect! Repeat same question.", Toast.LENGTH_SHORT).show();
            repeatQuestion();
        }
    }

    private void addQuestion() {
        questionList.add(generateQuestion());
        fAnimation = null;
        for(ListIterator it = questionList.listIterator(questionList.size()); it.hasPrevious();) {
            Question question = (Question) it.previous();
            Button button = (Button) fGameActivity.findButtonByRowCol(
                    question.fRow,
                    question.fColumn
            );
            fAnimation = ButtonAnimationListener.pushAnimation(button, fAnimation);
        }
        // For first duration.
        fAnimation = ButtonAnimationListener.pushAnimation((Button)fStartButton, fAnimation);
    }

    private void resetQuestionList() {
        questionList.clear();
    }

    private Question generateQuestion() {
        Random random = new Random();
        int row = random.nextInt(fGameActivity.getRow());
        int col = random.nextInt(fGameActivity.getColumn());
        return new Question(row, col);
    }

    public class Question {
        public int fRow, fColumn;
        public Question(int row, int col) {
            this.fRow = row;
            this.fColumn = col;
        }

        public boolean equals(Question question) {
            if(this.fRow == question.fRow && this.fColumn == question.fColumn) {
                return true;
            } else {
                return false;
            }
        }
    }
}
