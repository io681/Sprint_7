package ru.praktikum_services.qa_scooter.utils;

import java.util.Random;

public class RandomUtil {

        public static String randomString(int length) {
            java.util.Random random = new java.util.Random();
            int leftLimit = 97;
            int rightLimit = 122;
            StringBuilder buffer = new StringBuilder(length);

            for(int i = 0; i < length; ++i) {
                int randomLimitedInt = leftLimit + (int)(random.nextFloat() * (float)(rightLimit - leftLimit + 1));
                buffer.append(Character.toChars(randomLimitedInt));
            }

            return buffer.toString();
        }
    public static int randomNumber(){
        Random random = new Random();
        return random.nextInt(11);
    }

}
