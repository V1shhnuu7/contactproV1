package com.contactpro.contactpro.controller;

import com.contactpro.contactpro.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /*
     * Get most contacted contacts
     */
    @GetMapping("/most-contacted")
    public List<Object[]> mostContacted() {
        return analyticsService.getMostContactedContacts();
    }

    /*
     * Get last interaction of contact
     */
    @GetMapping("/last-interaction/{contactId}")
    public LocalDateTime lastInteraction(@PathVariable Long contactId) {
        return analyticsService.getLastInteraction(contactId);
    }

    /*
     * Get interaction count
     */
    @GetMapping("/count/{contactId}")
    public long interactionCount(@PathVariable Long contactId) {
        return analyticsService.getInteractionCount(contactId);
    }

    @GetMapping("/relationship-score/{contactId}")
    public int relationshipScore(@PathVariable Long contactId) {
        return analyticsService.calculateRelationshipScore(contactId);
    }
    // returns list of contacts needing follow-up
    @GetMapping("/followups")
    public List<String> followUps() {
        return analyticsService.getFollowUpReminders();
    }
}