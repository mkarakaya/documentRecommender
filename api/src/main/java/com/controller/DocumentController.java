package com.controller;

import com.service.DocumentService;
import com.sree.textbytes.jtopia.Configuration;
import com.sree.textbytes.jtopia.TermDocument;
import com.sree.textbytes.jtopia.TermsExtractor;
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
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by 212457624 on 4/5/2016.
 */
@RestController
@RequestMapping("/document")
public class DocumentController {


    @Autowired
    private DocumentService documentService;

    @RequestMapping(value="/similarDocs",method = RequestMethod.POST)
    public  Map<String, ArrayList<Integer>> getSimilarDocs(@RequestParam(required = true) MultipartFile multiPartFile) throws IOException, ParseException {
        Map<String, ArrayList<Integer>> finalFilteredTerms = documentService.getTerms(multiPartFile);
        return finalFilteredTerms;

    }

    @RequestMapping(value="/modelAppend",method = RequestMethod.POST)
    public void modelAppend(@RequestParam(required = true) MultipartFile multiPartFile) throws IOException {
        documentService.modelAppend(multiPartFile);
    }

    @RequestMapping(value="/healthCheck",method = RequestMethod.GET)
    public String healthCheck() {
        return "don't worry be happy";
    }


}
