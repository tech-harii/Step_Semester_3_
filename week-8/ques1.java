import java.util.*;

public class ques1 {

    // --- ABSTRACT QUESTION (Strategy / Open-Closed Principle) ---
    public static abstract class Question {
        private final String id;
        private final String prompt;
        private final String correctAnswer;

        public Question(String id, String prompt, String correctAnswer) {
            this.id = id;
            this.prompt = prompt;
            this.correctAnswer = correctAnswer;
        }

        public String getId() { return id; }
        public String getPrompt() { return prompt; }
        public String getCorrectAnswer() { return correctAnswer; }

        public abstract boolean evaluate(String studentAnswer);
    }

    public static class MultipleChoiceQuestion extends Question {
        private final List<String> options;

        public MultipleChoiceQuestion(String id, String prompt, String correctAnswer, List<String> options) {
            super(id, prompt, correctAnswer);
            this.options = options;
        }

        @Override
        public boolean evaluate(String studentAnswer) {
            return getCorrectAnswer().equalsIgnoreCase(studentAnswer != null ? studentAnswer.trim() : "");
        }
    }

    public static class TrueFalseQuestion extends Question {
        public TrueFalseQuestion(String id, String prompt, String correctAnswer) {
            super(id, prompt, correctAnswer);
        }

        @Override
        public boolean evaluate(String studentAnswer) {
            return getCorrectAnswer().equalsIgnoreCase(studentAnswer != null ? studentAnswer.trim() : "");
        }
    }

    // --- DOMAIN MODELS ---
    public static class Student {
        private final String studentId;
        private final String name;

        public Student(String studentId, String name) {
            this.studentId = studentId;
            this.name = name;
        }

        public String getName() { return name; }
    }

    public static class Examination {
        private final String examId;
        private final String title;
        private final List<Question> questions = new ArrayList<>();

        public Examination(String examId, String title) {
            this.examId = examId;
            this.title = title;
        }

        public void addQuestion(Question question) { questions.add(question); }
        public List<Question> getQuestions() { return Collections.unmodifiableList(questions); }
        public String getTitle() { return title; }
    }

    public enum AttemptStatus { IN_PROGRESS, SUBMITTED }

    public static class Attempt {
        private final String attemptId;
        private final Student student;
        private final Examination examination;
        private final Map<String, String> answers = new HashMap<>(); // QuestionID -> Answer
        private AttemptStatus status;
        private int score = 0;

        public Attempt(String attemptId, Student student, Examination examination) {
            this.attemptId = attemptId;
            this.student = student;
            this.examination = examination;
            this.status = AttemptStatus.IN_PROGRESS;
        }

        public void answerQuestion(String questionId, String answer) {
            if (status == AttemptStatus.SUBMITTED) {
                throw new IllegalStateException("Cannot change answers after submission.");
            }
            answers.put(questionId, answer);
        }

        public void submit() {
            if (status == AttemptStatus.SUBMITTED) {
                throw new IllegalStateException("Attempt has already been submitted.");
            }
            this.status = AttemptStatus.SUBMITTED;
            evaluate();
        }

        private void evaluate() {
            score = 0;
            for (Question question : examination.getQuestions()) {
                String givenAnswer = answers.get(question.getId());
                if (question.evaluate(givenAnswer)) {
                    score++;
                }
            }
        }

        public int getScore() { return score; }
        public int getTotalQuestions() { return examination.getQuestions().size(); }
        public Examination getExamination() { return examination; }
    }

    // --- MAIN TEST DRIVER ---
    public static void main(String[] args) {
        Student student = new Student("S101", "Student");
        Examination mathQuiz = new Examination("EXAM01", "Math Quiz");

        mathQuiz.addQuestion(new MultipleChoiceQuestion("Q1", "2 + 2 = ?", "A", Arrays.asList("A) 4", "B) 5", "C) 6")));
        mathQuiz.addQuestion(new MultipleChoiceQuestion("Q2", "5 * 3 = ?", "B", Arrays.asList("A) 10", "B) 15", "C) 20")));

        // Workflow execution
        Attempt attempt = new Attempt("ATT01", student, mathQuiz);
        System.out.println("Examination '" + mathQuiz.getTitle() + "' started by " + student.getName() + ".");

        attempt.answerQuestion("Q1", "A");
        System.out.println("Question 1 answered with 'A'.");

        attempt.answerQuestion("Q2", "C");
        System.out.println("Question 2 answered with 'C'.");

        attempt.submit();
        System.out.println("Examination '" + mathQuiz.getTitle() + "' submitted successfully.");
        System.out.println("Result for '" + mathQuiz.getTitle() + "' attempt: " 
                + attempt.getScore() + "/" + attempt.getTotalQuestions() + " correct.");
    }
}