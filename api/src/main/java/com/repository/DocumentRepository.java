package com.repository;

import com.model.Document;
import com.model.DocumentTerm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by 212457624 on 5/2/2016.
 */
@Repository
public interface DocumentRepository extends CrudRepository<Document,Long> {
}
