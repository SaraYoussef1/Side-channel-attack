# Side-channel-attack
This project is divided into two parts:

### The first part
**modifying RSA implementation to use Montgomery modular multiplication** instead of normal modular multiplication.  
That can seed up the performance in the decryption process, almost twice, after applying some changes in the way of using bigInteger class.  
This part includes **crypto.java** and **montgomery.java** classes.  

### The second part
**Side-channel timing attack** to verify that the second most significant bit of the private exponent given in **attack.java** is one.  
Program experiments with different portions of the given private exponent, different exponent lengths. 
For each of these lengths, the experiment is repeated for 20 times, and count how many times the result was consistent with the fact that the second most
significant bit is 1.  
In each trial of the 20 repetitions, program collects execution times for 10000 samples, and then proceeds to know whether the second most significant bit is 0 or 1. If the technique outputs 1, then it’s a success, otherwise, it’s not.  
To estimate targeted bit, the used technique in **attack.java** class is similar to **differential power analysis attack** algorithm as the following:  

If the target bit is 1, it's expected that μ 1 > μ 2 and μ 3 ≈ μ 4  
If the target bit is 0, it's expected that μ 3 > μ 4 and μ 1 ≈ μ 2  
A simple check during the experiment is to check the relationship between μ 1 - μ 2 and μ 3 - μ 4 , then decide based on which is larger.  
