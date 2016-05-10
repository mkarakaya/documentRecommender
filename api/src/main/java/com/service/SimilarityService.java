package com.service;

import edu.stanford.nlp.util.Sets;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * Created by p.bell on 10.05.2016.
 */
@Service
public class SimilarityService {
    public double getSimilarity(Map<String, Double> v1, Map<String, Double> v2) {
        Set<String> both = v1.keySet();
        both.retainAll(v2.keySet());
        double sclar = 0, norm1 = 0, norm2 = 0;
        for (String k : both) sclar += v1.get(k) * v2.get(k);
        for (String k : v1.keySet()) norm1 += v1.get(k) * v1.get(k);
        for (String k : v2.keySet()) norm2 += v2.get(k) * v2.get(k);
        return sclar / Math.sqrt(norm1 * norm2);
    }
}
