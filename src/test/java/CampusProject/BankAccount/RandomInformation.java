package CampusProject.BankAccount;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomInformation {

    public static String randomName() {

        return RandomStringUtils.randomAlphabetic(5);
    }

    public static String randomNumber(int basamak) {
        return RandomStringUtils.randomNumeric(basamak);
    }



}
