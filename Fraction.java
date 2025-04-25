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

    public Fraction(int numerator) {
        this.numerator = numerator;
    }

    public int getNumerator() {return numerator;}

    public void setNumerator(int numerator) {
        this.numerator = numerator;
        if(denominator > 1) {reduce();}
    }

    public int getDenominator() {return denominator;}

    public void setDenominator(int denominator) {
        this.denominator = denominator;
        if(denominator > 1) {reduce();}
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