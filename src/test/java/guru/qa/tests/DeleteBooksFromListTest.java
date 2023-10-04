package guru.qa.tests;

import guru.qa.models.DeleteBookModel;
import guru.qa.models.IsbnModel;
import guru.qa.models.AddBooksListModel;
import guru.qa.models.LoginResponseModel;
import org.openqa.selenium.Cookie;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static guru.qa.tests.TestData.credentials;

public class DeleteBooksFromListTest extends TestBase {
    @Test
    void addBookToProfileTest() {
        LoginResponseModel loginResponse = authorizationApi.login(credentials);
        IsbnModel isbnModel = new IsbnModel();
        isbnModel.setIsbn("9781449325862");
        List<IsbnModel> isbnList = new ArrayList<>();
        isbnList.add(isbnModel);

        AddBooksListModel booksList = new AddBooksListModel();
        booksList.setUserId(loginResponse.getUserId());
        booksList.setCollectionOfIsbns(isbnList);

        DeleteBookModel deleteBookModel = new DeleteBookModel();
        deleteBookModel.setIsbn("9781449325862");
        deleteBookModel.setUserId(loginResponse.getUserId());

        booksApi.deleteAllBooks(loginResponse);

        booksApi.addBook(loginResponse, booksList);

        booksApi.deleteOneBook(loginResponse, deleteBookModel);


        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", loginResponse.getUserId()));
        getWebDriver().manage().addCookie(new Cookie("token", loginResponse.getToken()));
        getWebDriver().manage().addCookie(new Cookie("expires", loginResponse.getExpires()));

        open("/profile");
        $("[id='see-book-Git Pocket Guide']").shouldNotBe(visible);
    }

}
