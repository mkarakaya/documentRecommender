package com.service;

import com.model.Document;
import com.model.DocumentTerm;
import com.repository.DocumentRepository;
import com.repository.DocumentTermRepository;
import com.sree.textbytes.jtopia.Configuration;
import com.sree.textbytes.jtopia.TermDocument;
import com.sree.textbytes.jtopia.TermsExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.hibernate.Hibernate;
import org.hibernate.LobHelper;
import org.hibernate.engine.jdbc.LobCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 212457624 on 5/2/2016.
 */

@Service
public class DocumentService {

    @Autowired
    DocumentTermRepository documentTermRepository;

    @Autowired
    DocumentRepository documentRepository;

    public void modelAppend(MultipartFile multiPartFile) throws IOException {
        Map<String, ArrayList<Integer>> terms = getTerms(multiPartFile);
        Document document= new Document();
        document.setFile(multiPartFile.getBytes());
        documentRepository.save(document);
        DocumentTerm documentTerm= new DocumentTerm();
        documentTerm.setDocumentId(document.getId());
        documentTerm.setTerms(terms);
        documentTermRepository.save(documentTerm);

    }

    public Map<String, ArrayList<Integer>> getTerms(MultipartFile multiPartFile) throws IOException {
        PDDocument load = PDDocument.load(multiPartFile.getInputStream());
        PDFTextStripper stripper= new PDFTextStripper();
        String text = stripper.getText(load);
        text = text.replace(":", "");
        load.close();
        Configuration.setTaggerType("default");// "default" for lexicon POS
        // tagger and "openNLP" for
        // openNLP POS tagger
        Configuration.setSingleStrength(3);
        Configuration.setNoLimitStrength(2);
        // If tagger type is "default" , then set model location as
        // english-lexicon.txt"
        Configuration.setModelFileLocation("english-lexicon.txt");
        TermsExtractor termExtractor = new TermsExtractor();
        TermDocument termDocument = new TermDocument();
        termDocument = termExtractor.extractTerms(text);
        return termDocument.getFinalFilteredTerms();
    }
}
