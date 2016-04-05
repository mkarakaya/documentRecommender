package com.controller;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 212457624 on 4/5/2016.
 */
@RestController
@RequestMapping("/document")
public class DocumentController {

    @RequestMapping(value="/",method = RequestMethod.GET)
    public String hello() {
        IndexReader reader= DirectoryReader.open()
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        TopDocs topDocs = indexSearcher.search(query,10);
        return "hello";
    }
}
