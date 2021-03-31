# Side-channel-attack
This project is divided into two parts:

### The first part
**Modify RSA implementation to use Montgomery modular multiplication** instead of normal modular multiplication.
That can seed up the performance in the decryption process, almost twice, after applying some changes in the way of using BigInteger class.
This part includes **crypto.java** and **montgomery.java** classes.

### The second part
**Side-channel timing attack** to verify that the second most significant bit of the private exponent given in **attack.java** is one.
Program experiments with different portions of the given private exponent, different exponent lengths.
For each of these lengths, the experiment is repeated for 20 times, and count how many times the result was consistent with the fact that the second most significant bit is one.
In each trial of the 20 repetitions, program collects execution times for 10,000 samples, and then proceeds to know whether the second most significant bit is 0 or 1, if the technique outputs 1, then it’s a success, otherwise, it’s not.
To estimate targeted bit, the used technique in **attack.java** class works as the following:

1. For each sample, program makes two assumptions about the targeted bit, one for zero and another for one.
2. For each assumption program performs Montgomery modular multiplication and classify this sample running time according to reduction step in one of the following four lists:
3. The assumption is 1 and there is reduction, average running time of this list is μ1
4. The assumption is 1 and there is no reduction, average running time of this list is μ2
5. The assumption is 0 and there is reduction, average running time of this list is μ3
6. The assumption is 0 and there is no reduction, average running time of this list is μ4
7. If the target bit is 1, it's expected that μ1 > μ2 and μ3 ≈ μ4
8. If the target bit is 0, it's expected that μ3 > μ4 and μ1 ≈ μ2
9. A simple check during the experiment is to check the relationship between μ1 - μ2 and μ3 - μ4 , then decide based on which is larger.
