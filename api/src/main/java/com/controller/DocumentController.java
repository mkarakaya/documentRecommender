package com.controller;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 212457624 on 4/5/2016.
 */
@RestController
@RequestMapping("/document")
public class DocumentController {

    String[] documents= new String[]{"something bad happened","something good happened"};
    @RequestMapping(value="/similarDocs",method = RequestMethod.POST)
    public TopDocs getSimilarDocs(@RequestParam(required = false) MultipartFile multiPartFile) throws IOException, ParseException {
        PDDocument load = PDDocument.load(multiPartFile.getInputStream());
        PDFTextStripper stripper= new PDFTextStripper();
        String text = stripper.getText(load);
        text = text.replace(":", "");
        load.close();
        RAMDirectory ramDirectory = new RAMDirectory();
        StandardAnalyzer analyzer= createIndex(ramDirectory);
        IndexReader reader= DirectoryReader.open(ramDirectory); //don't write on disk)
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        Query query = new QueryParser(Version.LUCENE_4_9, "content", analyzer).parse(text);
        TopDocs topDocs = indexSearcher.search(query,10);
        return topDocs;
    }

    @RequestMapping(value="/healthCheck",method = RequestMethod.GET)
    public String healthCheck() {
        return "don't worry be happy";
    }

    @RequestMapping(value="/jtopia",method = RequestMethod.GET)
    public Map<String, ArrayList<Integer>> jtopia() {
        Configuration.setTaggerType("default");// "default" for lexicon POS
        // tagger and "openNLP" for
        // openNLP POS tagger
        Configuration.setSingleStrength(3);
        Configuration.setNoLimitStrength(2);
        // If tagger type is "default" , then set model location as
        // "model/default/english-lexicon.txt"
        // If tagger type is "openNLP" , then set model location as
        // "model/openNLP/en-pos-maxent.bin"
        Configuration.setModelFileLocation("model/default/english-lexicon.txt");
        TermsExtractor termExtractor = new TermsExtractor();
        TermDocument termDocument = new TermDocument();
        termDocument = termExtractor.extractTerms(
                "BEHR Premium Textured DECKOVER is an innovative solid color coating. It will bring your old, weathered wood or concrete back to life. The advanced 100% acrylic resin formula creates a durable coating for your tired and worn out deck, rejuvenating to a whole new look.  For the best results, be sure to properly prepare the surface using other applicable BEHR products displayed above.California residents: see&nbsp;Proposition 65 informationRevives wood and composite decks, railings, porches and boat docks, also great for concrete pool decks, patios and sidewalks100% acrylic solid color coatingResists cracking and peeling and conceals splinters and cracks up to 1/4 in.Provides a durable, mildew resistant finishCovers up to 75 sq. ft. in 2 coats per gallonCreates a textured, slip-resistant finishFor best results, prepare with the appropriate BEHR product for your wood or concrete surfaceActual paint colors may vary from on-screen and printer representationsColors available to be tinted in most storesOnline Price includes Paint Care fee in the following states: CA, CO, CT, ME, MN, OR, RI, VT"
                        + "Not only do angles make joints stronger, they also provide more consistent, straight corners. Simpson Strong-Tie offers a wide variety of angles in various sizes and thicknesses to handle light-duty jobs or projects where a structural connection is needed. Some can be bent (skewed) to match the project. For outdoor projects or those where moisture is present, use our ZMAX zinc-coated connectors, which provide extra resistance against corrosion (look for a Z at the end of the model number).Versatile connector for various 90 connections and home repair projectsStronger than angled nailing or screw fastening aloneHelp ensure joints are consistently straight and strongDimensions: 3 in. x 3 in. x 1-1/2 in.Made from 12-Gauge steelGalvanized for extra corrosion resistanceInstall with 10d common nails or #9 x 1-1/2 in. Strong-Drive SD screws");
        Map<String, ArrayList<Integer>> finalFilteredTerms = termDocument.getFinalFilteredTerms();
        return finalFilteredTerms;
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
