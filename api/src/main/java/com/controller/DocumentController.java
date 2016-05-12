package com.controller;

import com.model.DocumentDto;
import com.service.DocumentService;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 212457624 on 4/5/2016.
 */
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/document")
public class DocumentController {


    @Autowired
    private DocumentService documentService;

    @CrossOrigin(origins = "http://localhost:5001")
    @RequestMapping(value="/terms",method = RequestMethod.POST)
    public Map<String, Double> getTerms(@RequestParam(required = true) MultipartFile multiPartFile) throws IOException, ParseException {
        Map<String, Double> finalFilteredTerms = documentService.getTerms(multiPartFile);
        LinkedHashMap<String, Double> sortedFinalFilteredTerms = finalFilteredTerms.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (x, y) -> {
                            throw new AssertionError();
                        },
                        LinkedHashMap::new
                ));
        return sortedFinalFilteredTerms;

    }
    @CrossOrigin(origins = "http://localhost:5001")
    @RequestMapping(value="/similarDocs",method = RequestMethod.POST)
    public List<DocumentDto> getSimilarDocs(@RequestParam(required = true) MultipartFile multiPartFile) throws IOException, ParseException {
        return documentService.getSimilarDocs(multiPartFile);
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @RequestMapping(value="/docs",method = RequestMethod.GET)
    public List<DocumentDto> getDocs() throws IOException, ParseException {
        return documentService.getDocs();
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public OutputStream getDocFile(@PathVariable Long id) throws IOException, ParseException {
        return documentService.getDocFile(id);
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @RequestMapping(value="/modelAppend",method = RequestMethod.POST)
    public void modelAppend(@RequestParam(required = true) MultipartFile multiPartFile) throws IOException {
        documentService.modelAppend(multiPartFile);
    }

    @CrossOrigin(origins = "http://localhost:5001")
    @RequestMapping(value="/healthCheck",method = RequestMethod.GET)
    public String healthCheck() {
        return "don't worry be happy";
    }


}
