package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.JsonUtils;

import javax.inject.Inject;

public final class BookPublishTask implements Runnable {
private final BookPublishRequestManager bookPublishRequestManager;
private final PublishingStatusDao publishingStatusDao;
private final CatalogDao catalogDao;

    @Inject
    public BookPublishTask(BookPublishRequestManager bookPublishRequestManager,
                           PublishingStatusDao publishingStatusDao,
                           CatalogDao catalogDao) {
        this.bookPublishRequestManager = bookPublishRequestManager;
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;
    }

    @Override
    public void run() {

      BookPublishRequest request = bookPublishRequestManager.getBookPublishRequestToProcess();

       if (request == null) return;

        publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(),
                PublishingRecordStatus.IN_PROGRESS,
                request.getBookId());

       KindleFormattedBook book = KindleFormatConverter.format(request);

       try {
          CatalogItemVersion catalogItemVersion = catalogDao.createOrUpdateBook(book);
           publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.SUCCESSFUL,
                   catalogItemVersion.getBookId());
       } catch (BookNotFoundException e) {
           publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(),
                   PublishingRecordStatus.FAILED,
                   request.getBookId(),
                   "Exited with a BookNotFoundException");
       }catch (Exception e) {
           publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(),
                   PublishingRecordStatus.FAILED,
                   request.getBookId(),
                   KindlePublishingUtils.generatePublishingStatusMessage(PublishingRecordStatus.FAILED));
       }




        }

    }

