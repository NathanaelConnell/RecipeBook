public class Fraction {
    int numerator;
    int denominator = 1;

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        if(denominator > 1) {
            this.denominator = denominator;
            reduce();
        }
    }

    private int gcd(int a, int b) {
        int tmp;
        while (b != 0) {
            tmp = b;
            b = a % b;
            a = tmp;
        }
        return a;
    }

    private void reduce() {
        int divisor = gcd(numerator, denominator);
        numerator /= divisor;
        denominator /= divisor;
    }

    public String toString() {
        String frac = "";
        frac += (denominator <=1 ? numerator: numerator + "/" + denominator);
        return frac;
    }
}