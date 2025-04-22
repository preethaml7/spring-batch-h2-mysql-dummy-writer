package io.protopanda.partitioner;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class CustomPartitioner implements Partitioner {

    @Value("${batch.chunk-size:100}")
    private Integer chunkSize;

    @Value("${batch.total-records-count:1000}")
    private Integer totalRecordCount;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        Map<String, ExecutionContext> partitions = new HashMap<>();

        long numberOfPartitions = (totalRecordCount + chunkSize - 1) / chunkSize;

        for (int i = 0; i < numberOfPartitions; i++) {

            ExecutionContext context = new ExecutionContext();
            long start = (long) i * chunkSize;
            long end = Math.min((long) (i + 1) * chunkSize, totalRecordCount);

            context.putLong("startIndex", start);
            context.putLong("endIndex", end);
            partitions.put("partition-" + i, context);
        }

        return partitions;
    }
}
