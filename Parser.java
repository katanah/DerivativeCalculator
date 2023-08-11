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
    private final String DEAFULT_COEFFICIENT = "1";

    private class Term {
        private String variable;
        private double coefficient;

        public Term(String variable, double coefficient) {
            this.variable = variable;
            this.coefficient = coefficient;
        }

        public String getVariable() {
            return variable;
        }

        public void setVariable(String variable) {
            this.variable = variable;
        }

        public double getCoefficient() {
            return coefficient;
        }

        public void setCoefficient(double coefficient) {
            this.coefficient = coefficient;
        }
    }

    public Parser(String expression, String variable) {
        this.expression = expression.replaceAll("\\s", "");
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

    private List<String> initTerms(String expression) {
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

    private Term parseTerm(String term, String sign) {
        if (term.indexOf(this.variable) != 0) {
            String[] coefficientAndVariable = term.split("(?=[" + this.variable + "])");
            String coefficient = sign + coefficientAndVariable[0];
            String variable = coefficientAndVariable[1];
            return new Term(variable, Double.parseDouble(coefficient));
        } else {
            String coefficient = sign + DEAFULT_COEFFICIENT;
            return new Term(term, Double.parseDouble(coefficient));
        }
    }

    private List<String> updateTerms(HashMap<String, Double> hmap, double constantSum, List<String> terms) {
        terms.clear();

        for (Map.Entry<String, Double> entry : hmap.entrySet()) {
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

    // TODO: this function elimates terms if it contains a variable other than
    // this.variable otherwise
    private List<String> combineLikeTerms(List<String> terms) {
        HashMap<String, Double> likeTerms = new HashMap<String, Double>(); // var, coeff
        double constantSum = 0;

        for (int i = 0; i < terms.size(); i++) {
            String term = terms.get(i);
            if (term.contains(this.variable)) {
                Term parsedTerm = parseTerm(term, checkSign(terms, term, i));
                addHashMapElement(likeTerms, parsedTerm);
            } else if (term.matches("\\d+")) {
                String currentConstant = checkSign(terms, term, i) + term;
                constantSum += Double.parseDouble(currentConstant);
            } else {
                continue;
            }
        }

        return updateTerms(likeTerms, constantSum, terms);
    }

    private void addHashMapElement(HashMap<String, Double> hmap, Term currentTerm) {
        String key = currentTerm.getVariable();
        double value = currentTerm.getCoefficient();

        hmap.merge(key, value, Double::sum);
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
