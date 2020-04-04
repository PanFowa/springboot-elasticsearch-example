package com.pan.service;

import com.pan.App;
import com.pan.service.es.QueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class TermQueryServiceImplV6Test {
    @Autowired
    QueryService termQueryService;
    @Test
    public void termQuery() {
        termQueryService.termQuery();
    }

    @Test
    public void termsQuery() {
        termQueryService.termsQuery();
    }

    @Test
    public void matchAllQuery() {
        termQueryService.matchAllQuery();
    }

    @Test
    public void matchQuery() {
        termQueryService.matchQuery();
    }

    @Test
    public void matchPhraseQuery() {
        termQueryService.matchPhraseQuery();
    }

    @Test
    public void matchMultiQuery() {
        termQueryService.matchMultiQuery();
    }

    @Test
    public void fuzzyQuery() {
        termQueryService.fuzzyQuery();
    }

    @Test
    public void rangeQuery() {
        termQueryService.rangeQuery();
    }

    @Test
    public void dateRangeQuery() {
        termQueryService.dateRangeQuery();
    }

    @Test
    public void wildcardQuery() {
        termQueryService.wildcardQuery();
    }

    @Test
    public void boolQuery() {
        termQueryService.boolQuery();
    }

    @Test
    public void aggregationStats() {
        termQueryService.aggregationStats();
    }

    @Test
    public void aggregationMin() {
        termQueryService.aggregationMin();
    }

    @Test
    public void aggregationMax() {
        termQueryService.aggregationMax();
    }

    @Test
    public void aggregationAvg() {
        termQueryService.aggregationAvg();
    }

    @Test
    public void aggregationSum() {
        termQueryService.aggregationSum();
    }

    @Test
    public void aggregationCount() {
        termQueryService.aggregationCount();
    }

    @Test
    public void aggregationPercentiles() {
        termQueryService.aggregationPercentiles();
    }

    @Test
    public void aggrBucketTerms() {
        termQueryService.aggrBucketTerms();
    }

    @Test
    public void aggrBucketRange() {
        termQueryService.aggrBucketRange();
    }

    @Test
    public void aggrBucketDateRange() {
        termQueryService.aggrBucketDateRange();
    }

    @Test
    public void aggrBucketHistogram() {
        termQueryService.aggrBucketHistogram();
    }

    @Test
    public void aggrBucketDateHistogram() {
        termQueryService.aggrBucketDateHistogram();
    }

    @Test
    public void aggregationTopHits() {
        termQueryService.aggregationTopHits();
    }
}
