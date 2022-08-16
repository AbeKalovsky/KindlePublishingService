package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;

import java.util.ArrayList;
import java.util.List;

public class PublishingStatusItemConverter {
    private PublishingStatusItemConverter() {}




    public static PublishingStatusRecord toRecord(PublishingStatusItem publishingStatusItems) {
        return PublishingStatusRecord.builder()
                .withStatus(publishingStatusItems.getStatus().toString())
                .withStatusMessage(publishingStatusItems.getStatusMessage())
                .withBookId(publishingStatusItems.getBookId())
                .build();
    }

    public static List<PublishingStatusRecord> toRecordList(List<PublishingStatusItem> publishingStatusItems) {
       List<PublishingStatusRecord> publishingStatusRecords = new ArrayList<>();
        for (PublishingStatusItem e: publishingStatusItems) {
            publishingStatusRecords.add(toRecord(e));
        }
        return publishingStatusRecords;
    }

}
