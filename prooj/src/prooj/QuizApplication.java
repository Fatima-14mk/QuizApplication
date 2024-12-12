package prooj;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class QuizApplication extends JFrame implements ActionListener {
    private JLabel questionLabel, timerLabel;
    private JRadioButton[] options = new JRadioButton[4];
    private JButton nextButton, resultButton;
    private ButtonGroup optionGroup;
    private int score = 0, currentQuestion = 0, timeLimit = 10, remainingTime;
    private Timer timer;

    // Static leader score to track the highest score achieved
    private static int leaderScore = 0;

    // Questions and answers
    private final String[][] questions = {
        {"Which one among these is not a primitive datatype?", "int", "float", "boolean", "char"},
        {"Which class is the parent class of all classes in Java?", "Swing", "Applet", "Object", "ActionEvent"},
        {"Which package is directly available in Java?", "swing", "applet", "net", "lang"},
        {"Which one among these is not a valid component?", "JButton", "JList", "JButtonGroup", "JTextArea"},
        {"Which among these is not a valid method in the Applet class?", "init", "destroy", "paint", "main"}
    };

    private final String[] correctAnswers = {"float", "Object", "lang", "JButtonGroup", "main"};

    public QuizApplication(String title) {
        super(title);

        // Frame Configuration
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(10, 10, 10));
        JLabel headerLabel = new JLabel("Quiz Application");
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        centerPanel.add(questionLabel);

        timerLabel = new JLabel("Time left: 10s");
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        centerPanel.add(timerLabel);

        optionGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("Arial", Font.PLAIN, 16));
            optionGroup.add(options[i]);
            centerPanel.add(options[i]);
        }
        add(centerPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextButton.addActionListener(this);
        resultButton = new JButton("Show Result");
        resultButton.setFont(new Font("Arial", Font.BOLD, 14));
        resultButton.addActionListener(this);
        resultButton.setEnabled(false);
        buttonPanel.add(nextButton);
        buttonPanel.add(resultButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Display the first question
        setQuestion();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            if (isAnswerCorrect()) {
                score++;
            }
            currentQuestion++;

            if (currentQuestion < questions.length) {
                setQuestion();
            } else {
                nextButton.setEnabled(false);
                resultButton.setEnabled(true);
                timer.stop();
            }
        }

        if (e.getSource() == resultButton) {
            displayResults();
        }
    }

    private void setQuestion() {
        optionGroup.clearSelection();

        // Stop the existing timer if any
        if (timer != null) {
            timer.stop();
        }

        // Reset timer
        remainingTime = timeLimit;
        timerLabel.setText("Time left: " + remainingTime + "s");
        startTimer();

        // Load question and options
        questionLabel.setText("Q" + (currentQuestion + 1) + ": " + questions[currentQuestion][0]);
        for (int i = 0; i < 4; i++) {
            options[i].setText(questions[currentQuestion][i + 1]);
        }
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remainingTime--;
                timerLabel.setText("Time left: " + remainingTime + "s");

                if (remainingTime == 0) {
                    timer.stop();
                    JOptionPane.showMessageDialog(null, "Time is up! Moving to the next question.", "Time Over", JOptionPane.WARNING_MESSAGE);
                    currentQuestion++;

                    if (currentQuestion < questions.length) {
                        setQuestion();
                    } else {
                        nextButton.setEnabled(false);
                        resultButton.setEnabled(true);
                    }
                }
            }
        });
        timer.start();
    }

    private boolean isAnswerCorrect() {
        return options[getCorrectAnswerIndex()].isSelected();
    }

    private int getCorrectAnswerIndex() {
        for (int i = 0; i < 4; i++) {
            if (questions[currentQuestion][i + 1].equals(correctAnswers[currentQuestion])) {
                return i;
            }
        }
        return -1;
    }

    private void displayResults() {
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("Your Score: ").append(score).append("/").append(questions.length).append("\n\n");

        for (int i = 0; i < questions.length; i++) {
            resultMessage.append("Q").append(i + 1).append(": ").append(questions[i][0]).append("\n");
            resultMessage.append("Correct Answer: ").append(correctAnswers[i]).append("\n\n");
       
        }

        if (score > leaderScore) {
            leaderScore = score;
            resultMessage.append("Congratulations! You set a new high score of ").append(leaderScore).append("!\n");
            
        } else {
            resultMessage.append("Leader's High Score: ").append(leaderScore).append("\n");
            
        }

        resultMessage.append("Would you like to try again? Click Yes to restart or No to exit.");

        int option = JOptionPane.showConfirmDialog(this, resultMessage.toString(), "Quiz Results", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            restartQuiz();
        } else {
            System.exit(0);
        }
    }

    private void restartQuiz() {
        score = 0;
        currentQuestion = 0;
        nextButton.setEnabled(true);
        resultButton.setEnabled(false);
        setQuestion();
    }

    public static void main(String[] args) {
        new QuizApplication("Quiz Application");
    }
}

