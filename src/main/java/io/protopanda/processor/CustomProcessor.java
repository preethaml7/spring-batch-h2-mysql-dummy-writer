package io.protopanda.processor;

import io.protopanda.model.DataIngestionPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class CustomProcessor implements ItemProcessor<DataIngestionPayload, DataIngestionPayload> {

    @Override
    public DataIngestionPayload process(DataIngestionPayload item) {

        // System.out.println("Processing item: " + item);

        return DataIngestionPayload.builder()
                .employee(item.getEmployee())
                .department(item.getDepartment())
                .build();
    }
}
