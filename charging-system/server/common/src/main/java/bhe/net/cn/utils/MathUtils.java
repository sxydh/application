package bhe.net.cn.utils;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum MathUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(MathUtils.class);

    public static void main(String[] args) {
        System.out.println(getRandomNum(6));
    }

    public static int getPrimeNum(int min) {
        int primeNum = 1;
        for (int divisor = min + 1;; divisor++) {
            boolean flag = true;
            for (int factor = 2; factor < divisor; factor++) {
                if (divisor % factor == 0) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                primeNum = divisor;
                break;
            }
        }
        return primeNum;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getRandomNum(int length) {
        String result = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            result += random.nextInt(10);
        }
        return result;
    }

}
