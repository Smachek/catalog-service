package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@JsonTest
public class BookJsonTests {
    @Autowired
    private JacksonTester<Book> json;

    @Test
    void testSerialize() throws IOException {
        var book = new Book((long) 1, "1234567890", "Title", "Author", 9.90,  Instant.now(), Instant.now(), 0);
        var jsonContent = json.write(book);

        assertThat(jsonContent).extractingJsonPathNumberValue("@.id"). isEqualTo(book.id().intValue());
        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn").isEqualTo(book.isbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title").isEqualTo(book.title());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author").isEqualTo(book.author());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price").isEqualTo(book.price());
        assertThat(jsonContent).extractingJsonPathStringValue("@.createdDate").isEqualTo(book.createdDate().toString());
        assertThat(jsonContent).extractingJsonPathStringValue("@.lastModifiedDate").isEqualTo(book.lastModifiedDate().toString());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.version").isEqualTo(book.version());
    }

    @Test
    void testDeserialize() throws Exception {
        var instant = Instant.parse("2022-03-08T17:30:40.135799Z");
        var content = """
                {
                    "id":1,
                    "isbn":"1234567890",
                    "title":"Title",
                    "author":"Author",
                    "price":9.90,
                    "createdDate":"2022-03-08T17:30:40.135799Z",
                    "lastModifiedDate":"2022-03-08T17:30:40.135799Z",
                    "version":0
                }
                """;
        assertThat(json.parse(content))
                .usingRecursiveComparison()
                .isEqualTo(new Book(1L, "1234567890", "Title", "Author", 9.90, instant, instant, 0));
    }
}
