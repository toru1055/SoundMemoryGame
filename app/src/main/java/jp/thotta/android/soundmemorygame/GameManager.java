package jp.thotta.android.soundmemorygame;

import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

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
    private ScoreManager scoreManager;
    private LifeManager lifeManager;

    private GameManager(){}

    public static GameManager getInstance() {
        return instance;
    }

    public int getStatus() {
        return fStatus;
    }

    public void setActivity(GameActivity gameActivity) {
        this.fGameActivity = gameActivity;
        scoreManager = new ScoreManager(this.fGameActivity);
        lifeManager = new LifeManager(this.fGameActivity);
        this.fStartButton = fGameActivity.findViewById(R.id.button_start_stop);
        initGame();
    }

    public void nextQuestion() {
        addQuestion();
        setStatus(STATUS_QUESTIONING);
        fStartButton.startAnimation(fAnimation);
        scoreManager.startScoring();
    }

    public void repeatQuestion() {
        setStatus(STATUS_QUESTIONING);
        fStartButton.startAnimation(fAnimation);
    }

    public void finishQuestion() {
        setStatus(STATUS_WAIT_ANSWER);
    }

    public void answer(int buttonId) {
        if(fStatus == STATUS_WAIT_ANSWER) {
            int row = buttonId / fGameActivity.getRow();
            int column = buttonId % fGameActivity.getRow();
            Question answerForCurrentQuestion = new Question(row, column);
            Question currentQuestion = questionList.get(fCurrentQuestionPointer);
            if (currentQuestion.equals(answerForCurrentQuestion)) {
                fCurrentQuestionPointer++;
                if (fCurrentQuestionPointer >= questionList.size()) {
                    scoreManager.endScoring();
                    scoreManager.calcScore(questionList.size());
                    scoreManager.updateView();
                    fCurrentQuestionPointer = 0;
                    Toast.makeText(fGameActivity, "Correct answer!", Toast.LENGTH_SHORT).show();
                    nextQuestion();
                }
            } else {
                fCurrentQuestionPointer = 0;
                lifeManager.deleteLife();
                if(lifeManager.getLife() > 0) {
                    Toast.makeText(
                            fGameActivity,
                            "Incorrect! Repeat same question.",
                            Toast.LENGTH_SHORT
                    ).show();
                    repeatQuestion();
                } else {
                    Toast.makeText(fGameActivity, "Game Over!!", Toast.LENGTH_LONG).show();
                    initGame();
                }
            }
        } else {
            Toast.makeText(
                    fGameActivity,
                    "Question hasn't finished! Please wait.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void initGame() {
        questionList.clear();
        setStatus(STATUS_INITIALIZED);
        ((Button)fStartButton).setText("Start");
    }

    public void startGame() {
        lifeManager.initialize();
        scoreManager.initialize();
        nextQuestion();
    }

    private void setStatus(int status) {
        this.fStatus = status;
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
