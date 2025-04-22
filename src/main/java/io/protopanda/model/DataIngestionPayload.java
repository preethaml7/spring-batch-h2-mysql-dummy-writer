package io.protopanda.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DataIngestionPayload {

    private List<Department> departmentList;

    private List<Employee> employeeList;

}
