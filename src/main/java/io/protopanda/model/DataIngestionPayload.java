package io.protopanda.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DataIngestionPayload {

    private Department department;

    private Employee employee;

}
