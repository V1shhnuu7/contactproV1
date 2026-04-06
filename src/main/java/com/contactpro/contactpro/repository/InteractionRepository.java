package com.contactpro.contactpro.repository;

import com.contactpro.contactpro.model.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/*
 * InteractionRepository
 *
 * Responsibility:
 * Communicates with the database for Interaction entity.
 *
 * JpaRepository automatically provides:
 * - save()
 * - findById()
 * - findAll()
 * - delete()
 * - update (via save)
 *
 * We only define extra queries if needed.
 */

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, Long> {

    /*
     * Find all interactions for a specific contact
     *
     * Spring Data JPA automatically converts this
     * method name into SQL:
     *
     * SELECT * FROM interactions WHERE contact_id = ?
     *
     * This allows us to view interaction history
     * of a contact.
     */
    List<Interaction> findByContactId(Long contactId);

    @Query("""
SELECT i.contact.id, COUNT(i)
FROM Interaction i
GROUP BY i.contact.id
ORDER BY COUNT(i) DESC
""")
    List<Object[]> findMostContactedContacts();

    @Query("""
SELECT MAX(i.interactionDate)
FROM Interaction i
WHERE i.contact.id = :contactId
""")
    LocalDateTime findLastInteractionDate(Long contactId);

    long countByContactId(Long contactId);

    @Query("""
SELECT SUM(i.duration)
FROM Interaction i
WHERE i.contact.id = :contactId
""")
    Integer getTotalDuration(Long contactId);

    // gets the latest interaction date for every contact
    @Query("""
SELECT i.contact.id, MAX(i.interactionDate)
FROM Interaction i
GROUP BY i.contact.id
""")
    List<Object[]> findLastInteractionForAllContacts();
}

