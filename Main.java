public class Main {
    public static void main(String argsp[]) {


        String term = "2x^2";

        String[] coeffAndVar = term.split("(?=[" + "x" + "])");
        String coeff = coeffAndVar[0];
        String var = coeffAndVar[1];
        
        System.out.println(coeff + " " + var);
    }
}

//// x^2+2x-2x^2