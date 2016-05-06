package com.model;



import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum Status {
    NEW, IN_PROGRESS, REIMBURSED
}
