public class Fraction {
    int numerator;
    int denominator;

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        reduce();
    }

    public Fraction(int numerator) {
        this.numerator = numerator;
        denominator = 0;
    }

    public int getNumerator() {return numerator;}

    public int getDenominator() {return denominator;}

    public void setNumerator(int numerator) {
        this.numerator = numerator;
        reduce();
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
        reduce();
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
        frac += (denominator==0 ? numerator: numerator + "/" + denominator);
        return frac;
    }
}