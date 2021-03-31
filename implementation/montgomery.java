import java.math.BigInteger;

public class montgomery {
    private static BigInteger N, R, t, m, N2, RmodiN;
    static int Rexp, reduction;

    public montgomery(BigInteger N) {
        this.N = N;
        Rexp = N.bitLength();
        this.R = BigInteger.ONE.shiftLeft(Rexp);
        N2 = (N.modInverse(R)).negate();
        RmodiN = R.modInverse(N);
    }

    public  BigInteger multiply_mont(BigInteger a, BigInteger b, int attack) {
        reduction = 0;
        t = a.multiply(b);
        m = t.multiply(N2);
        m = m.subtract(m.shiftRight(Rexp).shiftLeft(Rexp));
        t = (t.add(m.multiply(N))).shiftRight(Rexp);
        if (t.compareTo(N) == 1){
            t =  t.subtract(N);
            if(attack == 1){
                reduction = 1;
                for(int i=0; i<9; i++)
                    t = t.subtract(N);
            }
        }
        return t;
    }

    public int getReduction(){
        return reduction;
    }

    private BigInteger modExp(BigInteger a, BigInteger exponent) {
        a = a.shiftLeft(Rexp).mod(N);
        BigInteger result = a;
        int expBitlength = exponent.bitLength();
        for (int i = expBitlength - 2; i >= 0; i--) {
            result = multiply_mont(result, result, 0);

            if (exponent.testBit(i)) {
                result = multiply_mont( a, result, 0);
            }
        }
        return result.multiply(RmodiN).mod(N);
    }

    public BigInteger encrypt(BigInteger m, BigInteger e) {
        return modExp(m, e);
    }

    public BigInteger decrypt(BigInteger c, BigInteger d) {

        return modExp(c, d);
    }
}
