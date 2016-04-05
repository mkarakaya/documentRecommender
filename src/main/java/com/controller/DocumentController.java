package com.controller;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by 212457624 on 4/5/2016.
 */
@RestController
@RequestMapping("/document")
public class DocumentController {

    String[] documents= new String[]{"something bad happened","something good happened"};
    @RequestMapping(value="/{document}",method = RequestMethod.GET)
    public TopDocs hello(@PathVariable String document) throws IOException, ParseException {
        RAMDirectory ramDirectory = new RAMDirectory();
        StandardAnalyzer analyzer= createIndex(ramDirectory);

        IndexReader reader= DirectoryReader.open(ramDirectory); //don't write on disk)
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        Query query = new QueryParser(Version.LUCENE_4_9, "content", analyzer).parse(document);
        TopDocs topDocs = indexSearcher.search(query,10);
        return topDocs;
    }

    private StandardAnalyzer createIndex(Directory ramDirectory) throws IOException {
        StandardAnalyzer  analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
        IndexWriterConfig   config = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
        IndexWriter indexWriter = new IndexWriter(ramDirectory, config);
        indexWriter.commit();

        for(int i=0;i<documents.length;i++){
            Document doc = createDocument(String.valueOf(i),documents[i]);
            indexWriter.addDocument(doc);
        }
        indexWriter.commit();
        indexWriter.close();
        return analyzer;
    }

    private Document createDocument(String id, String content) {
        FieldType type = new FieldType();
        type.setIndexed(true);
        type.setStored(true);
        type.setStoreTermVectors(true); //TermVectors are needed for MoreLikeThis

        Document doc = new Document();
        doc.add(new StringField("id", id, Field.Store.YES));
        doc.add(new Field("content", content, type));
        return doc;
    }
}
