import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Parser {
    private String expression; // TODO: *maybe make this a list to support other variables when combining like
                               // terms
    private List<String> termsAndOperators;
    private SyntaxTree syntaxTree;
    private String variable;

    public Parser(String expression, String variable) {
        this.expression = expression;
        this.variable = variable;
        this.termsAndOperators = initTerms(this.expression);
        // initTerms(this.expression);

        System.out.println(termsAndOperators.toString());

        setTermsAndOperators(combineLikeTerms(termsAndOperators));

        System.out.println(termsAndOperators.toString());

    }

    private List<String> initSyntaxTree(String expression) {
        return new ArrayList<>();
    }

    public List<String> initTerms(String expression) {
        String[] terms = expression.split("(?<=[+-/*])");
        List<String> splitExpression = new ArrayList<String>();

        splitTerms(terms, splitExpression);

        return splitExpression;
    }

    private void splitTerms(String[] terms, List<String> splitExpression) {
        for (String term : terms) {
            String[] splitTerms = term.split("(?=[+-/*])");

            for (String subterm : splitTerms) {
                splitExpression.add(subterm);
            }
        }

        if (splitExpression.get(0) == "+") {
            splitExpression.remove(0);
        }
    }

    private List<String> splitTerms(List<String> terms) {
        List<String> splitTerms = new ArrayList<>();

        for (String term : terms) {
            String[] operatorAndTerms = term.split("(?<=[+\\-/*])");

            for (String split : operatorAndTerms) {
                splitTerms.add(split);
            }
        }
        return splitTerms;
    }

    // TODO: this function elimates terms if it contains a variable other than
    // this.variable otherwise
    private List<String> combineLikeTerms(List<String> terms) {
        HashMap<String, String> likeTerms = new HashMap<String, String>(); // var, coeff
        double constantSum = 0;

        for (int i = 0; i < terms.size(); i++) {
            String term = terms.get(i);
            String coefficient = "";
            String variable = "";

            if (term.contains(this.variable)) {
                if (term.indexOf(this.variable) != 0) {
                    String[] coeffAndVar = term.split("(?=[" + this.variable + "])");
                    coefficient = checkSign(terms, term, i) + coeffAndVar[0];
                    variable = coeffAndVar[1];
                    addHashMapElement(likeTerms, variable, coefficient);
                } else {
                    coefficient = checkSign(terms, term, i) + "1";
                    variable = term;
                    addHashMapElement(likeTerms, variable, coefficient);
                }
            } else if (term.matches("\\d+")) {
                String currentTerm = checkSign(terms, term, i) + term;
                constantSum += Double.parseDouble(currentTerm);
            }

        }

        terms.clear();

        for (Map.Entry<String, String> entry : likeTerms.entrySet()) {
            String term = entry.getValue() + entry.getKey();

            if (Character.isLetterOrDigit(term.charAt(0))) {
                term = "+" + term;
            }
            terms.add(term);
        }

        if (constantSum != 0) {
            String constant = "";
            if (constantSum >= 1)
                constant = "+" + constantSum;
            else
                constant = "" + constantSum;

            terms.add(constant);
        }

        return splitTerms(terms);
    }

    private void parseTerm(String term) {

    }

    private void addHashMapElement(HashMap<String, String> hmap, String key, String value) {
        if (!hmap.containsKey(key)) {
            hmap.put(key, value);
        } else {
            double currentValue = Double.parseDouble(hmap.get(key));
            currentValue += Double.parseDouble(value);

            hmap.replace(key, Double.toString(currentValue));
        }
    }

    private void addHashMapElement(HashMap<String, Double> hmap, List<Object> coefficientAndVariable) {
        String key = coefficientAndVariable.get(1);
        double value = Double.parscoefficientAndVariable.get(0);

        if (!hmap.containsKey(key)) {
            hmap.put(key, value);
        } else {

        }
    }

    private String checkSign(List<String> terms, String term, int index) {
        if (index == 0) {
            return "+";
        } else {
            String previousTerm = terms.get(index - 1);
            if (previousTerm.equals("+")) {
                return "+";
            } else if (previousTerm.equals("-")) {
                return "-";
            }
        }

        return "Logic error";
    }

    public void setTermsAndOperators(List<String> termsAndOperators) {
        this.termsAndOperators = termsAndOperators;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter an expression: ");
        String expression = sc.nextLine();

        sc.close();

        Parser p = new Parser(expression, "x");
    }

}
