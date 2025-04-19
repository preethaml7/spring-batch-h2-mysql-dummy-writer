package io.protopanda.processor;

import io.protopanda.model.Employee;
import org.springframework.batch.item.ItemProcessor;

public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {

    @Override
    public Employee process(final Employee employee) {

        return Employee.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .cityName(employee.getCityName())
                .build();
    }
}
