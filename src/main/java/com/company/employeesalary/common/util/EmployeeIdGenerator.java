package com.company.employeesalary.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeIdGenerator {

    private static final int MAX_ATTEMPTS = 100;
    private static final int ID_MIN = 0;
    private static final int ID_MAX = 9999;
    private final SecureRandom random = new SecureRandom();

    /**
     * Generates a unique 4-digit employee ID
     * @param existsChecker Function to check if an ID already exists
     * @return Unique 4-digit employee ID as String
     * @throws IllegalStateException if unable to generate unique ID after MAX_ATTEMPTS
     */
    public String generateUniqueEmployeeId(Predicate<String> existsChecker) {
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            String employeeId = generateRandomId();

            if (!existsChecker.test(employeeId)) {
                log.debug("Generated unique employee ID: {} after {} attempts", employeeId, attempts + 1);
                return employeeId;
            }

            attempts++;
            log.trace("Employee ID {} already exists, retrying... (attempt {}/{})",
                    employeeId, attempts, MAX_ATTEMPTS);
        }

        throw new IllegalStateException(
                String.format("Failed to generate unique employee ID after %d attempts", MAX_ATTEMPTS));
    }

    /**
     * Generates a random 4-digit ID
     * @return 4-digit ID formatted as String with leading zeros if necessary
     */
    private String generateRandomId() {
        int number = random.nextInt(ID_MAX - ID_MIN + 1) + ID_MIN;
        return String.format("%04d", number);
    }
}