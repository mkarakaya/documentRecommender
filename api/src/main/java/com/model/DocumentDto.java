package com.model;

/**
 * Created by p.bell on 10.05.2016.
 */
public class DocumentDto {

    private long id;
    private String name;
    private double similarity;
    private String author;
    private String format;

    public DocumentDto(long id, String name, double similarity){
        this.id=id;
        this.name=name;
        this.similarity=similarity;
        this.format="PDF";
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
