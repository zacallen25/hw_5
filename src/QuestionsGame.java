import java.io.*;
import java.util.*;

/**
 *
 * The primary class of the program: QuestionsGame
 * This class handles all functionality related to the tree of questions.
 *Developed alongside Tyler Adcock
 */

public class QuestionsGame {

    private QuestionNode root;

    public QuestionsGame(String object){
        root = new QuestionNode(object);
    }

    /**
     * This constructor is used to initialize a question tree from a document of questions
     **/
    public QuestionsGame(Scanner input){
        root = new QuestionNode(createTree(input));
    }

    /**
     * This helper method recursively adds new nodes to a tree by reading data from a questions text document
     */
    private QuestionNode createTree(Scanner input){
        String type = input.nextLine();
        String value = input.nextLine();
        if(type.contains("Q"))
            return new QuestionNode(value, createTree(input), createTree(input));
        else
            return new QuestionNode(value);
    }

    /**
     * This method saves the new question tree to the questions document being used
     */
    public void saveQuestions(PrintStream output){
        if(output == null)
            throw new IllegalArgumentException("PrintStream is null.");

        saveQuestions(output, root);
    }

    /**
     * This helper method iterates through the tree using pre-order traversal
     * It writes the data of each node to the questions document with proper formatting
     */
    private void saveQuestions(PrintStream output, QuestionNode node){
        if(node != null){
            output.println(node.toString());
            saveQuestions(output, node.left);
            saveQuestions(output, node.right);
        }
    }

    /**
     * This method handles the mechanics of the game:
     *      text output, user input, win detection, adding new questions, and adding new objects
     */
    public void play(Scanner input){
        playHelper(input, root, null);
    }

    /**
     * This helper method handles the navigation through the question tree as the game progresses
     * It receives and checks user input after each prompt
     * If the computer fails, it adds a new question and new object provided by the user
     */
    private void playHelper(Scanner input, QuestionNode node, QuestionNode parent){
        if(node != null){
            if(node.isQuestion) {
                System.out.print(node.value + " (y/n)? ");
                String answer = input.nextLine();
                if (answer.trim().toLowerCase().startsWith("y"))
                    playHelper(input, node.left, node);
                else
                    playHelper(input, node.right, node);
            } else {
                System.out.println("I guess that your object is " + node.value + "!");
                System.out.print("Am I right? (y/n)? ");
                String answer = input.nextLine();
                if(answer.trim().toLowerCase().startsWith("y")){
                    System.out.println("Awesome! I win!");
                } else {
                    System.out.println("Boo! I Lose.  Please help me get better!");
                    System.out.print("What is your object? ");
                    String object = input.nextLine().trim();
                    System.out.println("Please give me a yes/no question that distinguishes between " + object + " and " + node.value + ".");
                    System.out.print("Q: ");
                    String question = input.nextLine().trim();
                    System.out.print("Is the answer \"yes\" for " + object + "? (y/n)? ");
                    answer = input.nextLine();

                    addQuestion(parent, question, node, object, answer.trim().toLowerCase().startsWith("y"));
                }
            }
        } else {
            System.out.println("There was some error.");
        }


    }

    /**
     * This helper method handles all actions involving adding new questions and objects
     */
    private void addQuestion(QuestionNode parent, String newParent, QuestionNode newChild, String newValue, boolean answerIsYes){
        if(parent != null) {
            if (answerIsYes) {
                if (parent.left == newChild)
                    parent.setLeft(new QuestionNode(newParent, new QuestionNode(newValue), newChild));
                else if (parent.right == newChild)
                    parent.setRight(new QuestionNode(newParent, new QuestionNode(newValue), newChild));
                else
                    System.out.println("There was an error with the tree.");
            } else {
                if (parent.left == newChild)
                    parent.setLeft(new QuestionNode(newParent, newChild, new QuestionNode(newValue)));
                else if (parent.right == newChild)
                    parent.setRight(new QuestionNode(newParent, newChild, new QuestionNode(newValue)));
                else
                    System.out.println("There was an error with the tree.");
            }
        } else {
            if (answerIsYes)
                root = new QuestionNode(newParent, new QuestionNode(newValue), newChild);
            else
                root = new QuestionNode(newParent, newChild, new QuestionNode(newValue));
        }
    }

    /**
     * The QuestionNode class is the core component of the question tree that is generated
     */
    private static class QuestionNode {
        public final String value;
        public QuestionNode left;
        public QuestionNode right;
        public final boolean isQuestion;

        /**
         * This constructor initializes a new node by copying the values of a different node to it
         * It is used to add new questions and objects to the question tree
         */
        public QuestionNode(QuestionNode other){
            this.value = other.value;
            this.left = other.left;
            this.right = other.right;
            this.isQuestion = other.isQuestion;
        }

        /**
         * This constructor initializes a new node from only a string value
         * It is exclusively used to create new object nodes, thus isQuestion is always set to false
         * value is a string containing the object the node will represent
         */
        public QuestionNode(String value) {
            this.value = value;
            this.left = null;
            this.right = null;
            this.isQuestion = false;
        }

        /**
         * This constructor initializes a new node from a string value, a left node, and a right node
         * It is exclusively used to create new question nodes, thus isQuestion is always set to true
         */
        public QuestionNode(String value, QuestionNode left, QuestionNode right) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.isQuestion = true;
        }

        /**
         * A setter function for the left node
         */
        public void setLeft(QuestionNode left) {
            this.left = left;
        }

        /**
         * A setter function for the right node]
         */
        public void setRight(QuestionNode right) {
            this.right = right;
        }

        /**
         * Overrides the toString method for QuestionNode]
         */
        @Override
        public String toString(){
            if(isQuestion)
                return "Q:\n" + value;
            else
                return "A:\n" + value;
        }
    }
}
