package io.protopanda.reader;

import io.protopanda.model.Employee;
import net.datafaker.Faker;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;

public class CustomReader implements ItemReader<Employee> {

    private final Faker faker = new Faker();
    private int recordCount = 0;

    @Value("${batch.total-records-count:1000}")
    private Integer totalRecordCount;

    @Override
    public Employee read() throws UnexpectedInputException, ParseException, NonTransientResourceException {

        if (recordCount >= totalRecordCount) {
            return null; // Return null to the signal end of reading
        }

        // System.out.println("Reading record number: " + (recordCount + 1));

        String cityName = faker.address().cityName();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        recordCount++;

        return Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .cityName(cityName)
                .build();
    }
} 