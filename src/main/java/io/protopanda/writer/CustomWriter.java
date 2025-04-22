package io.protopanda.writer;

import io.protopanda.model.DataIngestionPayload;
import io.protopanda.repository.DepartmentRepository;
import io.protopanda.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomWriter implements ItemWriter<DataIngestionPayload> {

    private final EmployeeRepository employeeRepository;

    private final DepartmentRepository departmentRepository;

    @Override
    public void write(Chunk<? extends DataIngestionPayload> chunks) {

        chunks.forEach(payload -> {

            employeeRepository.saveAll(payload.getEmployeeList());
            departmentRepository.saveAll(payload.getDepartmentList());

            // log.info(">>> Writing: {}", payload.getEmployeeList().toString());

        });
    }
}
