package ru.netology.booking.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;

public class MainPage {

    private SelenideElement buyButton = $$(".button").findBy(Condition.exactText("Купить"));

    public BuyCheckout depositClick() {
        buyButton.click();
        return new BuyCheckout();
    }
}
