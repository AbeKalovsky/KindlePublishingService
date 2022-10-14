package com.amazon.ata.kindlepublishingservice.publishing;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BookPublishRequestManager {
   private ConcurrentLinkedQueue<BookPublishRequest> bookPublishRequestQueue;
    @Inject
    public  BookPublishRequestManager(ConcurrentLinkedQueue<BookPublishRequest> queue) {
        this.bookPublishRequestQueue = queue;
    }


    public void addBookPublishRequest(BookPublishRequest bookPublishRequest) {
            bookPublishRequestQueue.add(bookPublishRequest);
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
        return bookPublishRequestQueue.poll();
    }
}
