package com.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 212457624 on 5/2/2016.
 */
@Entity
public class DocumentTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long documentId;

    @ElementCollection
    private Map<String, Double> terms;

    public Long getId() {
        return id;
    }


    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Map<String, Double> getTerms() {
        return terms;
    }

    public void setTerms(Map<String, Double> terms) {
        this.terms = terms;
    }
}
