package ru.netology.booking.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.booking.checkSQL.Check;
import ru.netology.booking.data.DataHelper;
import ru.netology.booking.pages.*;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingTest {

    @Test
    @DisplayName("Проверка успешного оформления")
    void checkOrderSuccessfully() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.notificationOk();
        var status = Check.checkStatus();
        assertEquals("APPROVED", status);
    }

    @Test
    //TODO
    void checkDeclineForInternalReasons() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getDeclinedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.notificationDeclinedOrder();
        var status = Check.checkStatus();
        assertEquals("DECLINED", status);
    }

    @Test
    @DisplayName("Отказ при неверном номере карты")
    void checkDeclineForInvalidCardNumber() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getRandomCard();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.notificationInvalidCard();
    }

    @Test
    @DisplayName("Проверка валидации короткого номера карты")
    void checkErrorForShortCardNumber() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getRandomCard();
        servicePage.order(DataHelper.getShortCardNumber(), info.getDateMonth(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.notificationInvalidDataInCardNumberField();
    }

    @Test
    @DisplayName("Проверка валидации латиницы в номере карты")
    void checkNotFillCardNumberWithLatinLetters() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getRandomCard();
        servicePage.order(DataHelper.getLatLetters(), info.getDateMonth(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.emptyCardNumberFieldNotification();
    }

    @Test
    @DisplayName("Проверка валидации кириллицы в номере карты")
    void checkNotFillCardNumberWithRussianLetters() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getRandomCard();
        servicePage.order(DataHelper.getRuLetters(), info.getDateMonth(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.emptyCardNumberFieldNotification();
    }

    @Test
    @DisplayName("Проверка валидации символов в номере карты")
    void checkNotFillCardNumberWithSymbols() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getRandomCard();
        servicePage.order(DataHelper.getSymbols(), info.getDateMonth(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.emptyCardNumberFieldNotification();
    }

    @Test
    @DisplayName("Проверка валидации пустого номера карты")
    void checkErrorForEmptyCardNumber() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getRandomCard();
        servicePage.order("", info.getDateMonth(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.emptyCardNumberFieldNotification();
    }

    @Test
    @DisplayName("Проверка валидации длинного номера карты")
    void checkDeclineForLongCardNumberNotBeingVerified() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getRandomCard();
        servicePage.order(DataHelper.getTooLongCardNumber(), info.getDateMonth(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.notificationInvalidCard();
    }

    @Test
    //TODO DisplayName
    void checkDeclineForInvalidPastMonth() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        DataHelper.Date date = DataHelper.getDateInRecentPast();
        servicePage.order(info.getCardNumber(), date.getMonth(), date.getYear(), info.getOwner(), info.getCvc());
        servicePage.notificationInvalidDate();
    }

    @Test
    //TODO DisplayName
    void checkDeclineForInvalidPastYear() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        DataHelper.Date date = DataHelper.getDateInAncientPast();
        servicePage.order(info.getCardNumber(), date.getMonth(), date.getYear(), info.getOwner(), info.getCvc());
        servicePage.notificationOverdueCardDate();
    }

    @Test
    //TODO DisplayName
    void checkDeclineForInvalidFutureYear() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        DataHelper.Date date = DataHelper.getDateInFuture();
        servicePage.order(info.getCardNumber(), date.getMonth(), date.getYear(), info.getOwner(), info.getCvc());
        servicePage.notificationInvalidDate();
    }

    @Test
    //TODO DisplayName
    void checkNotDeclineForTooLongFormatOfMonthDueToCut() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        DataHelper.Date date = DataHelper.getTooLongDate();
        servicePage.order(info.getCardNumber(), date.getMonth(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.notificationOk();
        var status = Check.checkStatus();
        assertEquals("APPROVED", status);
    }

    @Test
    //TODO DisplayName
    void checkNotDeclineForTooLongFormatOfYearDueToCut() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        DataHelper.Date date = DataHelper.getTooLongDate();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), date.getYear(), info.getOwner(), info.getCvc());
        servicePage.notificationOk();
        var status = Check.checkStatus();
        assertEquals("APPROVED", status);
    }

    @Test
    @DisplayName("Ошибка если в поле Год 00")
    void checkErrorForZerosInYear() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), "00", info.getOwner(), info.getCvc());
        servicePage.notificationOverdueCardDate();
    }

    @Test
    @DisplayName("Ошибка при использовании несуществующего месяца")
    void checkErrorForNotExistingMonth() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), DataHelper.getNotExistingMonth(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.notificationInvalidDataInMonthField();
    }

    @Test
    @DisplayName("Ошибка если в поле Месяц 00")
    void checkErrorForZerosInMonth() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), "00", info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.notificationInvalidDataInMonthField();
    }

    @Test
    @DisplayName("Проверка валидации латиницы в поле Год")
    void checkNotFillYearWithLatinLetters() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), DataHelper.getLatLetters(), info.getOwner(), info.getCvc());
        servicePage.emptyYearFieldNotification();
    }

    @Test
    @DisplayName("Проверка валидациии латиницы в поле Месяц")
    void checkNotFillMonthWithLatinLetters() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), DataHelper.getLatLetters(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.emptyMonthFieldNotification();
    }

    @Test
    @DisplayName("Проверка валидации кириллицы в поле Год")
    void checkNotFillYearWithRussianLetters() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), DataHelper.getRuLetters(), info.getOwner(), info.getCvc());
        servicePage.emptyYearFieldNotification();
    }

    @Test
    @DisplayName("Проверка валидации кириллицы в поле Месяц")
    void checkNotFillMonthWithRussianLetters() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), DataHelper.getRuLetters(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.emptyMonthFieldNotification();
    }

    @Test
    @DisplayName("Проврека валидации символов в поле Год")
    void checkNotFillYearWithSymbols() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), DataHelper.getSymbols(), info.getOwner(), info.getCvc());
        servicePage.emptyYearFieldNotification();
    }

    @Test
    @DisplayName("Проверка валидации символов в поле Месяц")
    void checkNotFillMonthWithSymbols() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), DataHelper.getSymbols(), info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.emptyMonthFieldNotification();
    }

    @Test
    @DisplayName("Ошибка если поле Месяц пустое")
    void checkErrorForEmptyMonth() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), "", info.getDateYear(), info.getOwner(), info.getCvc());
        servicePage.emptyMonthFieldNotification();
    }

    @Test
    @DisplayName("Ошибка если поле Год пустое")
    void checkErrorForEmptyYear() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), "", info.getOwner(), info.getCvc());
        servicePage.emptyYearFieldNotification();
    }

    @Test
    @DisplayName("Ошибка при 2-х символах в поле CVC")
    void checkErrorForShortCVC() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), info.getDateYear(), info.getOwner(), DataHelper.getShortCVC());
        servicePage.notificationInvalidDataInCVCField();
    }

    @Test
    @DisplayName("Проверка валидации 4-х символов в поле CVC")
    void checkNotFillCVCWithLongNumber() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), info.getDateYear(), info.getOwner(), DataHelper.getTooLongCVC());
        servicePage.notificationOk();
        var status = Check.checkStatus();
        assertEquals("APPROVED", status);
    }

    @Test
    @DisplayName("Проверка валидации латиницы в поле CVC")
    void checkNotFillCVCWithLatinLetters() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), info.getDateYear(), info.getOwner(), DataHelper.getLatLetters());
        servicePage.emptyCVCFieldNotification();
    }

    @Test
    @DisplayName("Проверка валидации кириллицы в поле CVC")
    void checkNotFillCVCWithRussianLetters() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), info.getDateYear(), info.getOwner(), DataHelper.getRuLetters());
        servicePage.emptyCVCFieldNotification();
    }

    @Test
    @DisplayName("Проверка валидации символов в поле CVC")
    void checkNotFillCVCWithSymbols() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), info.getDateYear(), info.getOwner(), DataHelper.getSymbols());
        servicePage.emptyCVCFieldNotification();
    }

    @Test
    @DisplayName("Ошибка если поле CVC пустое")
    void checkErrorForEmptyCVC() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), info.getDateYear(), info.getOwner(), "");
        servicePage.emptyCVCFieldNotification();
    }


    @Test
    @DisplayName("Ошибка если поле Владелец пустое")
    void checkErrorForEmptyOwner() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), info.getDateYear(), "", info.getCvc());
        servicePage.emptyOwnerFieldNotification();
    }

    @Test
    @DisplayName("Заказ если в поле владелец присутствуют цифры")
    void checkOrderWithNumbersInOwner() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), info.getDateYear(), info.getCardNumber(), info.getCvc());
        servicePage.notificationOk();
        var status = Check.checkStatus();
        assertEquals("APPROVED", status);
    }

    @Test
    @DisplayName("Ошибка если в поле Владелец присутствуют символы")
    void checkErrorWhenSymbolsInOwner() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), info.getDateYear(), DataHelper.getSymbols(), info.getCvc());
        servicePage.notificationInvalidDataInOwnerField();
    }

    @Test
    @DisplayName("Ошибка если в поле Владелец присутствуют русские буквы")
    void checkErrorWhenRussianLettersInOwner() {
        var choosePage = open("http://localhost:8080", MainPage.class);
        var servicePage = choosePage.depositClick();
        DataHelper.CardInfo info = DataHelper.getApprovedCardInfo();
        servicePage.order(info.getCardNumber(), info.getDateMonth(), info.getDateYear(), DataHelper.getRuLetters(), info.getCvc());
        servicePage.notificationInvalidDataInOwnerField();
    }

}
