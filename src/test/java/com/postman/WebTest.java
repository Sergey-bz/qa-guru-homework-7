package com.postman;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class RegistrationFormTest {

    @ValueSource(strings = {"supertest", "@gmail", "@gmail.com"})
    @ParameterizedTest(name = "Registration with email {0}")
    void incorrectEmailTest(String email) {
        open("https://www.postman.com/");
        $(byText("Sign Up for Free")).click();
        $("#email").setValue(email);
        $("#username").click();
        $("#input-error-email").shouldHave(text("Please enter a valid email address, like: yourname@email.com"));
    }

    @CsvSource(value = {
            "supertest@gmail.com, Тест",
            "supertest@yandex.ru, !Test"
    })
    @ParameterizedTest(name = "Registration with email {0} and username {1}")
    void incorrectUsernameTest(String email, String username) {
        open("https://www.postman.com/");
        $(byText("Sign Up for Free")).click();
        $("#email").setValue(email);
        $("#username").setValue(username);
        $("#password").click();
        $("#input-error-username").shouldHave(text("First character must be an alphabet or number"));
    }

    static Stream<Arguments> dataProviderForHeaderMenuTest() {
        return Stream.of(
                Arguments.of("Product", List.of("What is Postman?", "API repository", "Tools", "Intelligence", "Workspaces", "Integrations", "Get started free")),
                Arguments.of("Enterprise", List.of("Postman Enterprise", "Enterprise case studies", "Contact sales"))
        );
    }

    @MethodSource("dataProviderForHeaderMenuTest")
    @ParameterizedTest(name = "Button {0} contains items {1}")
    void headerMenuTest(String button, List<String> expectedItems) {
        open("https://www.postman.com/");
        $(".homepage-header-container").$(byText(button)).click();
        $$(".tippy-content a").shouldHave(texts(expectedItems));
    }
}
