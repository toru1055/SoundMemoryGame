package jp.thotta.android.soundmemorygame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;
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
    public static final int STATUS_BE_FINISHED = 3;

    private GameActivity fGameActivity;
    private int fStatus = STATUS_INITIALIZED;
    private List<Question> questionList = new LinkedList<Question>();
    private Animation fAnimation = null;
    private int fCurrentQuestionPointer = 0;
    private View fStartButton;
    private ScoreManager scoreManager;
    private LifeManager lifeManager;
    private ScoreRecordDBHelper scoreRecordDB;

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
        scoreRecordDB = new ScoreRecordDBHelper(fGameActivity);
        initGame();
    }

    public void nextQuestion() {
        addQuestion();
        setStatus(STATUS_QUESTIONING);
        fStartButton.startAnimation(fAnimation);
        scoreManager.startScoring();
        updateQuestionSizeView();
    }

    public void repeatQuestion() {
        showToastText("Incorrect Answer! Repeat same question.");
        setStatus(STATUS_QUESTIONING);
        fStartButton.startAnimation(fAnimation);
    }

    public void finishQuestion() {
        if(fStatus != STATUS_BE_FINISHED) {
            setStatus(STATUS_WAIT_ANSWER);
        }
    }

    public boolean answer(int buttonId) {
        boolean isCorrect = false;
        if(fStatus == STATUS_WAIT_ANSWER) {
            int row = buttonId / fGameActivity.getRow();
            int column = buttonId % fGameActivity.getRow();
            Question answerForCurrentQuestion = new Question(row, column);
            Question currentQuestion = questionList.get(fCurrentQuestionPointer);
            if (currentQuestion.equals(answerForCurrentQuestion)) {
                answeredCorrectly();
                isCorrect = true;
            } else {
                fCurrentQuestionPointer = 0;
                lifeManager.deleteLife();
                if(lifeManager.getLife() > 0) {
                    repeatQuestion();
                } else {
                    playGameOverMelody();
                    showToastText("Game Over!!");
                    finishGame();
                }
            }
        } else {
            showToastText("Question hasn't finished! Please wait.");
        }
        return isCorrect;
    }

    private void answeredCorrectly() {
        fCurrentQuestionPointer++;
        if (fCurrentQuestionPointer >= questionList.size()) {
            scoreManager.endScoring();
            scoreManager.calcScore(questionList.size());
            scoreManager.updateView();
            fCurrentQuestionPointer = 0;
            showToastText("Correct answer!");
            if(getGameMode() != ScoreRecordBean.GAME_MODE_SUPER_HARD &&
                    questionList.size() >= ScoreRecordBean.STAGE_QUESTION_SIZE) {
                playChimeMelody();
                showStageCompleteDialog();
                finishGame(questionList.size());
            } else {
                nextQuestion();
            }
        }
    }

    private void showStageCompleteDialog() {
        new AlertDialog.Builder(fGameActivity)
                .setTitle("Congratulations! You Have Completed This Stage!")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fGameActivity.finish();
                            }
                        }).show();
    }

    private void playChimeMelody() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SoundGenerator.getInstance().playChimeMelody();
            }
        };
        new Thread(r).start();
    }

    private void playGameOverMelody() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SoundGenerator.getInstance().playGameOverMelody();
            }
        };
        new Thread(r).start();
    }

    private void showToastText(String text) {
        Toast.makeText(fGameActivity, text, Toast.LENGTH_SHORT).show();
    }

    private int getGameMode() {
        return fGameActivity.getRow();
    }

    private void updateHighScoreView() {
        int highScore = scoreRecordDB.getHighScore();
        TextView textView = (TextView) fGameActivity.findViewById(R.id.text_high_score);
        textView.setText(String.valueOf(highScore));
    }

    private void updateQuestionSizeView() {
        TextView textView = (TextView) fGameActivity.findViewById(R.id.text_question);
        textView.setText(String.valueOf(questionList.size()));
    }

    private void initGame() {
        questionList.clear();
        setStatus(STATUS_INITIALIZED);
        ((Button)fStartButton).setText("Start");
        updateHighScoreView();
    }

    public void startGame() {
        lifeManager.initialize();
        scoreManager.initialize();
        nextQuestion();
    }

    public void finishGame() {
        finishGame(questionList.size() - 1);
    }
    public void finishGame(int correctAnswerNum) {
        if(scoreManager.getScore() > scoreRecordDB.getHighScore()) {
            showToastText("Congratulations! You Got High Score!");
        }
        insertGameScoreDbRecord(correctAnswerNum);
        initGame();
        setStatus(STATUS_BE_FINISHED);

    }

    private void insertGameScoreDbRecord(int correctAnswerNum) {
        scoreRecordDB.addGameScore(getGameMode(), scoreManager.getScore(), correctAnswerNum);
        updateHighScoreView();
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
            return this.fRow == question.fRow && this.fColumn == question.fColumn;
        }
    }
}
