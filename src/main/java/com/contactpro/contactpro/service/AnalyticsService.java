package com.contactpro.contactpro.service;

import com.contactpro.contactpro.repository.InteractionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {

    private final InteractionRepository interactionRepository;

    public AnalyticsService(InteractionRepository interactionRepository) {
        this.interactionRepository = interactionRepository;
    }

    public int calculateRelationshipScore(Long contactId) {

        int score = 0;

        // interaction frequency
        long interactionCount = interactionRepository.countByContactId(contactId);
        score += interactionCount * 5;

        // recency
        LocalDateTime lastInteraction =
                interactionRepository.findLastInteractionDate(contactId);

        if (lastInteraction != null) {

            long days =
                    ChronoUnit.DAYS.between(lastInteraction, LocalDateTime.now());

            if (days <= 7) score += 30;
            else if (days <= 30) score += 20;
            else if (days <= 90) score += 10;
        }

        // duration weight
        Integer totalDuration =
                interactionRepository.getTotalDuration(contactId);

        if (totalDuration != null) {
            if (totalDuration > 60) score += 20;
            else if (totalDuration > 30) score += 10;
        }

        return score;
    }
    // detects contacts that have not been interacted with in 30+ days
    public List<String> getFollowUpReminders() {

        List<Object[]> results =
                interactionRepository.findLastInteractionForAllContacts();

        List<String> reminders = new ArrayList<>();

        for (Object[] row : results) {

            Long contactId = (Long) row[0];
            LocalDateTime lastInteraction = (LocalDateTime) row[1];

            long days =
                    ChronoUnit.DAYS.between(lastInteraction, LocalDateTime.now());

            if (days > 30) {
                reminders.add(
                        "Contact ID " + contactId +
                                " has not been contacted for " + days + " days"
                );
            }
        }

        return reminders;
    }

    /*
     * Most contacted contacts
     */
    public List<Object[]> getMostContactedContacts() {
        return interactionRepository.findMostContactedContacts();
    }

    /*
     * Last interaction with contact
     */
    public LocalDateTime getLastInteraction(Long contactId) {
        return interactionRepository.findLastInteractionDate(contactId);
    }

    /*
     * Total interactions with contact
     */
    public long getInteractionCount(Long contactId) {
        return interactionRepository.countByContactId(contactId);
    }
}