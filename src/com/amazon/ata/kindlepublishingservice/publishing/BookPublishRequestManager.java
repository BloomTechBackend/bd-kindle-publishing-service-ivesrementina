package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Queue;

public class BookPublishRequestManager {
    private CatalogDao catalogDao;
    Queue<BookPublishRequest> bookPublishRequestQueue = new LinkedList<>();

    @Inject
    public BookPublishRequestManager(CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }



    public BookPublishRequest addBookPublishRequest(BookPublishRequest bpr) {
        bookPublishRequestQueue.add(bpr);
        return bpr;
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
            return bookPublishRequestQueue.poll();
    }
}
