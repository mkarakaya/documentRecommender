package com.model;

/**
 * Created by p.bell on 10.05.2016.
 */
public class SimilarDocumentDto {

    private long id;
    private String name;
    private double similarity;

    public SimilarDocumentDto(long id, String name, double similarity){
        this.id=id;
        this.name=name;
        this.similarity=similarity;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
}
