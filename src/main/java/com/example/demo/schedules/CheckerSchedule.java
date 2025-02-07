package com.example.demo.schedules;

import com.example.demo.entity.Checker;
import com.example.demo.repotitory.CheckerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CheckerSchedule {
    private final CheckerRepository checkerRepository;

    @Scheduled(cron = "0 0 1 * * *")
    public void checkedOutDate() {
        List<Checker> checkers = checkerRepository.findAll();
        List<Checker> filteredChecker = checkers.stream().filter((p) -> p.getTimeout() == null).toList();
        LocalTime current = LocalTime.now();
        List<Checker> removeChecker = filteredChecker.stream()
                .filter(checker -> isWithinLast12Hours(checker.getTimeIn(), current))
                .collect(Collectors.toList());
        checkerRepository.deleteAll(removeChecker);
        log.info("Schedule Checker run task  : clear last 1 hours not checked");
    }

    public static boolean isWithinLast12Hours(LocalTime timeIn, LocalTime current) {
        Duration duration = Duration.between(timeIn, current);
        return duration.toHours() > 0.5;
    }
}
