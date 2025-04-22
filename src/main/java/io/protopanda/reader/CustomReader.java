package io.protopanda.reader;

import io.protopanda.model.DataIngestionPayload;
import io.protopanda.model.Department;
import io.protopanda.model.Employee;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Scope("step")
public class CustomReader implements ItemReader<DataIngestionPayload>, ItemStream {

    private final long startIndex;
    private final long endIndex;
    private boolean readAlready = false;

    public CustomReader(@Value("#{stepExecutionContext[startIndex]}") long startIndex,
                        @Value("#{stepExecutionContext[endIndex]}") long endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public DataIngestionPayload read() throws Exception {

        if (readAlready) return null;

        int count = (int) (endIndex - startIndex);
        readAlready = true;

        Faker faker = new Faker();
        List<Employee> employeeList = new ArrayList<>();
        List<Department> departmentList = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            String cityName = faker.address().cityName();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();

            Employee employee = Employee.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .cityName(cityName).build();

            Department department = Department.builder()
                    .name(faker.company().industry())
                    .cityName(cityName).build();

            employeeList.add(employee);
            departmentList.add(department);
        }

        return DataIngestionPayload.builder()
                .employeeList(employeeList)
                .departmentList(departmentList)
                .build();
    }

    @Override
    public void open(ExecutionContext executionContext) {
    }

    @Override
    public void update(ExecutionContext executionContext) {
    }

    @Override
    public void close() {
    }
}
