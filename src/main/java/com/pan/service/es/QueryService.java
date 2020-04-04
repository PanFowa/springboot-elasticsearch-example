package com.pan.service.es;

public interface QueryService {
    public void termQuery();
    public void termsQuery();
    public Object matchAllQuery();
    public Object matchQuery();
    public Object matchPhraseQuery();
    public Object matchMultiQuery();
    public Object fuzzyQuery();
    public void rangeQuery();
    public void dateRangeQuery();
    public Object wildcardQuery();
    public Object boolQuery();
    public Object aggregationStats();
    public Object aggregationMin();
    public Object aggregationMax();
    public Object aggregationAvg();
    public Object aggregationSum();
    public Object aggregationCount();
    public Object aggregationPercentiles();
    public Object aggrBucketTerms();
    public Object aggrBucketRange();
    public Object aggrBucketDateRange();
    public Object aggrBucketHistogram();
    public Object aggrBucketDateHistogram();
    public Object aggregationTopHits();

}
