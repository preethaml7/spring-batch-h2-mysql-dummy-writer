package io.protopanda.reader;

import io.protopanda.model.DataIngestionPayload;
import io.protopanda.model.Department;
import io.protopanda.model.Employee;
import net.datafaker.Faker;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;

public class CustomReader implements ItemReader<DataIngestionPayload> {

    private final Faker faker = new Faker();
    private int recordCount = 0;

    @Value("${batch.total-records-count:1000}")
    private Integer totalRecordCount;

    @Override
    public DataIngestionPayload read() throws UnexpectedInputException, ParseException, NonTransientResourceException {

        if (recordCount >= totalRecordCount) {
            return null; // Return null to the signal end of reading
        }

        // System.out.println("Reading record number: " + (recordCount + 1));

        String cityName = faker.address().cityName();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        recordCount++;

        Employee employee = Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .cityName(cityName)
                .build();

        Department department = Department.builder()
                .name(faker.company().industry())
                .cityName(cityName)
                .build();

        return DataIngestionPayload.builder()
                .employee(employee)
                .department(department)
                .build();
    }

} 