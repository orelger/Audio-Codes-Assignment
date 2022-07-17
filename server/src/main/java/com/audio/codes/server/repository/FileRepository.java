package com.audio.codes.server.repository;

import com.audio.codes.server.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Orel Gershonovich
 * @since 15-July-2022
 */

@Repository
public interface FileRepository extends MongoRepository<File, Integer> {
    Optional<File> findByFileName(String name);
}
