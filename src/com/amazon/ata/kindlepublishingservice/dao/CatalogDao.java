package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;


import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequest;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;


import java.util.List;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */

    public CatalogItemVersion getBookFromCatalog(BookPublishRequest bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null ) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    public CatalogItemVersion getBookFromCatalog(String bookId) {

        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null) {
            throw new BookNotFoundException("No book found for id: " + bookId);
        }

        return book;
    }

    private CatalogItemVersion getLatestVersionOfBook(BookPublishRequest bookPublishRequest) {

        if (bookPublishRequest == null) {
            return null;
        }

        CatalogItemVersion book = new CatalogItemVersion();

        book.setBookId(bookPublishRequest.getBookId());
        book.setTitle(bookPublishRequest.getTitle());
        book.setAuthor(bookPublishRequest.getAuthor());
        book.setGenre(bookPublishRequest.getGenre());
        book.setText(bookPublishRequest.getText());

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
                .withHashKeyValues(book)
                .withScanIndexForward(false)
                .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);

        if (results.isEmpty()) {

            throw new BookNotFoundException("Book not found");

        }

        return results.get(0);
    }
    private CatalogItemVersion getLatestVersionOfBook(String bookPublishRequest) {

        if (bookPublishRequest == null) {
            return null;
        }

        CatalogItemVersion book = new CatalogItemVersion();


        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression()
                .withHashKeyValues(book)
                .withScanIndexForward(false)
                .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);

        if (results.isEmpty()) {

            throw new BookNotFoundException("Book not found");

        }
        return results.get(0);
    }
    public void removeBook(String bookId) {
       CatalogItemVersion book = dynamoDbMapper.load(getLatestVersionOfBook(bookId));
        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }
       book.setInactive(true);
       dynamoDbMapper.save(book);
    }
}

