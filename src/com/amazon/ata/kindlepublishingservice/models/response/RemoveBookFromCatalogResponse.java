package com.amazon.ata.kindlepublishingservice.models.response;

public class RemoveBookFromCatalogResponse {

    private String bookId;
    public RemoveBookFromCatalogResponse(String bookId) {
        this.bookId = bookId;
    }

    public String getBookId() {
        return bookId;
    }
}
