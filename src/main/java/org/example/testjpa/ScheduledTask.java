package org.example.testjpa;

import org.example.testjpa.db.SalesTransaction;
import org.example.testjpa.db.SalesTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ScheduledTask {

    @Autowired
    SalesTransactionRepository salesTransactionRepository;

    private static final List<String> statuses = Arrays.asList("A", "B", "C", "D", "E", "F", "G");

    @Scheduled(fixedDelay = 10 * 1000)
    @Transactional
    public void runJob() {
        System.out.println("gm");
        long start = System.currentTimeMillis();

        List<SalesTransaction> transactions = IntStream.range(0, 5_000).mapToObj(x -> {
            SalesTransaction tx = new SalesTransaction();
            tx.setCreatedAt(LocalDateTime.now());
            tx.setUpdatedAt(LocalDateTime.now());
            tx.setPostedDate(LocalDateTime.now());
            tx.setDescription(UUID.randomUUID().toString());
            tx.setAmountCents(new Random().nextInt(1000_000));
            tx.setStatus(getRandomStatus());
            return tx;
        }).collect(Collectors.toList());

        salesTransactionRepository.saveAll(transactions);
        salesTransactionRepository.flush();

        long duration = System.currentTimeMillis() - start;

        System.out.println(String.format("Inserted %s rows in %sms", transactions.size(), duration));
    }

    @Scheduled(fixedDelay = 5 * 1000)
    @Transactional
    public void updateTx() {
        List<SalesTransaction> transactions = salesTransactionRepository.findByStatus(getRandomStatus());
        System.out.println(String.format("Found %s items to update", transactions.size()));

        for (SalesTransaction transaction : transactions) {
            transaction.setDescription(UUID.randomUUID().toString());
            transaction.setUpdatedAt(LocalDateTime.now());
        }

        long start = System.currentTimeMillis();
        salesTransactionRepository.saveAll(transactions);
        salesTransactionRepository.flush();
        long duration = System.currentTimeMillis() - start;

        System.out.println(String.format("Updated %s rows in %sms", transactions.size(), duration));
    }

    private String getRandomStatus() {
        return statuses.get(new Random().nextInt(statuses.size()));
    }
}
