import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Random;

public class attack {

    static BigInteger modulus;
    static BigInteger m [] = new BigInteger[20000];
    static BigInteger t [] = new BigInteger[20000];
    static LinkedList OneT  = new LinkedList<BigInteger>();
    static LinkedList OneF = new LinkedList<BigInteger>();
    static LinkedList ZeroT = new LinkedList<BigInteger>();
    static LinkedList ZeroF = new LinkedList<BigInteger>();

    public static void main(String[] args){
        modulus = new BigInteger(
                "a12360b5a6d58b1a7468ce7a7158f7a2562611bd163ae754996bc6a2421aa17d3cf6d4d46a06a9d437525571a2bfe9395d440d7b09e9912a2a1f2e6cb072da2d0534cd626acf8451c0f0f1dca1ac0c18017536ea314cf3d2fa5e27a13000c4542e4cf86b407b2255f9819a763797c221c8ed7e7050bc1c9e57c35d5bb0bddcdb98f4a1b58f6d8b8d6edb292fd0f7fa82dc5fdcd78b04ca09e7bc3f4164d901b119c4f427d054e7848fdf7110352c4e612d02489da801ec9ab978d98831fa7f872fa750b092967ff6bdd223199af209383bbce36799a5ed5856f587f7d420e8d76a58b398ef1f7b290bc5b75ef59182bfa02fafb7caeb504bd9f77348aea61ae9",
                16);

        BigInteger d = new BigInteger(
                "1801d152befc69b1134eda145bf6c94e224fa1acee36f06826436c609840a776a532911ae48101a460699fd9424a1d51329804fa23cbec98bf95cdb0dbc900c05c5a358f48228ab03372b25610b0354d0e4a8c57efe86b1b2fb9ff6580655cdabddb31d7a8cfaf99e7866ba0d93f7ee8d1aab07fc347836c03df537569ab9fcfca8ebf5662feafbdf196bb6c925dbc878f89985096fabd6430511c0ca9c4d99b6f9f5dd9aa3ddfac12f6c2d3194ab99c897ba25bf71e53cd33c1573e242d75c48cd2537d1766bbbf4f7235c40ce3f49b18e00c874932412743dc28b7d3d32e85c922c1d9a8e5bf4c7dd6fe4545dd699295d51945d1fc507c24a709e87561b001",
                16);

        montgomery mont = new montgomery(modulus);
        Random rnd = new Random();
        int portionBits = 10;
        BigInteger f = d.shiftRight(d.bitLength() - portionBits);

        int missed = 0;
            for(int i=0; i<20; i++) {
                System.out.println("trial number " + i);
                //clear used linked lists
                clear();
                missed = hypothesis(mont, rnd, f, missed);
            }
        System.out.println("missed is " + missed);

    }

    private static int hypothesis(montgomery mont, Random rnd, BigInteger d, int missed){
        for(int i=0; i<20000; i++){

            BigInteger startTime;
            BigInteger endTime;
            m[i] = new BigInteger(modulus.bitLength() - 1, rnd);
            startTime = BigInteger.valueOf(System.nanoTime());
            mont.decrypt(m[i], d);
            endTime = BigInteger.valueOf(System.nanoTime());
            t[i] = endTime.subtract(startTime);
            check(m[i], i, mont);
        }
        int expectedBit = comp_avr();
        System.out.println(expectedBit);
        if(expectedBit == 0)
            missed++;
        return missed;
    }

    private static void check(BigInteger m, int i, montgomery mont){
        m = m.shiftLeft(modulus.bitLength()).mod(modulus);
        BigInteger x = mont.multiply_mont(m, m, 0);
        Suppose_zero(x, mont, i);
        Suppose_one(x, m, mont , i);

    }
    private static void Suppose_zero(BigInteger x,montgomery mont, int i){
        x = mont.multiply_mont(x, x, 1);
        if(mont.getReduction() == 1)
            ZeroT.add(t[i]);
        else
            ZeroF.add(t[i]);

    }
    private static void Suppose_one(BigInteger x, BigInteger m, montgomery mont, int i){
        x = mont.multiply_mont(x, m, 0);
        x = mont.multiply_mont(x, x, 1);
        if(mont.getReduction() == 1)
            OneT.add(t[i]);
        else
            OneF.add(t[i]);
    }

    private static BigInteger average(LinkedList<BigInteger> time){
        BigInteger sum = BigInteger.ZERO;
        for(int i=0; i<time.size(); i++)
            sum = sum.add(time.get(i));
        return sum.divide(BigInteger.valueOf(time.size()));
    }

    private static int comp_avr(){
        BigInteger avrZ1 = average(ZeroT);
        BigInteger avrZ2 = average(ZeroF);
        BigInteger avrO1 = average(OneT);
        BigInteger avrO2 = average (OneF);
        return (((avrO1.subtract(avrO2).compareTo(avrZ1.subtract(avrZ2)))==1) ? 1 : 0);
    }

    private static void clear(){
        OneF.clear();
        OneT.clear();
        ZeroF.clear();
        ZeroT.clear();
    }
}

