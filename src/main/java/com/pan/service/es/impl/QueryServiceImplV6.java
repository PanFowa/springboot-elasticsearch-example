package com.pan.service.es.impl;

import com.alibaba.fastjson.JSON;
import com.pan.entity.UserInfo;
import com.pan.service.es.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.max.ParsedMax;
import org.elasticsearch.search.aggregations.metrics.min.ParsedMin;
import org.elasticsearch.search.aggregations.metrics.percentiles.ParsedPercentiles;
import org.elasticsearch.search.aggregations.metrics.percentiles.Percentile;
import org.elasticsearch.search.aggregations.metrics.stats.ParsedStats;
import org.elasticsearch.search.aggregations.metrics.sum.ParsedSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.ParsedTopHits;
import org.elasticsearch.search.aggregations.metrics.valuecount.ParsedValueCount;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Slf4j
@Service
public class QueryServiceImplV6 implements QueryService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    /**
     * 精确查询（查询条件不会进行分词，但是查询内容可能会分词，导致查询不到）
     */
    public void termQuery() {
        try {
            // 构建查询条件（注意：termQuery 支持多种格式查询，如 boolean、int、double、string 等，这里使用的是 string 的查询）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.termQuery("address.keyword", "北京市通州区"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().totalHits > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(), UserInfo.class);
                    // 输出查询信息
                    log.info(userInfo.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 多个内容在一个字段中进行查询
     */
    public void termsQuery() {
        try {
            // 构建查询条件（注意：termsQuery 支持多种格式查询，如 boolean、int、double、string 等，这里使用的是 string 的查询）
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.termsQuery("address.keyword", "北京市丰台区", "北京市昌平区", "北京市大兴区"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().totalHits > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(), UserInfo.class);
                    // 输出查询信息
                    log.info(userInfo.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 匹配查询符合条件的所有数据，并设置分页
     * @return
     */
    public Object matchAllQuery() {
        try {
            // 构建查询条件
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
            // 创建查询源构造器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(matchAllQueryBuilder);
            // 设置分页
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(3);
            // 设置排序
            searchSourceBuilder.sort("salary", SortOrder.ASC);
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().totalHits > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(), UserInfo.class);
                    // 输出查询信息
                    log.info(userInfo.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 匹配查询数据
     * @return
     */
    public Object matchQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery("address", "*通州区"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().totalHits > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(), UserInfo.class);
                    // 输出查询信息
                    log.info(userInfo.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 词语匹配查询
     * @return
     */
    public Object matchPhraseQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("address", "北京市通州区"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().totalHits > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(), UserInfo.class);
                    // 输出查询信息
                    log.info(userInfo.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 内容在多字段中进行查询
     * @return
     */
    public Object matchMultiQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery("北京市", "address", "remark"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().totalHits > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(), UserInfo.class);
                    // 输出查询信息
                    log.info(userInfo.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 模糊查询所有以 “三” 结尾的姓名
     * @return
     */
    public Object fuzzyQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.fuzzyQuery("name", "三").fuzziness(Fuzziness.AUTO));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().totalHits > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(), UserInfo.class);
                    // 输出查询信息
                    log.info(userInfo.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 查询岁数 ≥ 30 岁的员工数据
     */
    public void rangeQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.rangeQuery("age").gte(30));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().totalHits > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(), UserInfo.class);
                    // 输出查询信息
                    log.info(userInfo.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 查询距离现在 30 年间的员工数据
     * [年(y)、月(M)、星期(w)、天(d)、小时(h)、分钟(m)、秒(s)]
     * 例如：
     * now-1h 查询一小时内范围
     * now-1d 查询一天内时间范围
     * now-1y 查询最近一年内的时间范围
     */
    public void dateRangeQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            // includeLower（是否包含下边界）、includeUpper（是否包含上边界）
            searchSourceBuilder.query(QueryBuilders.rangeQuery("birthDate")
                    .gte("now-30y").includeLower(true).includeUpper(true));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().totalHits > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(), UserInfo.class);
                    // 输出查询信息
                    log.info(userInfo.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
    }

    /**
     * 查询所有以 “三” 结尾的姓名
     *
     * *：表示多个字符（0个或多个字符）
     * ?：表示单个字符
     * @return
     */
    public Object wildcardQuery() {
        try {
            // 构建查询条件
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.wildcardQuery("name.keyword", "*三"));
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().totalHits > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(), UserInfo.class);
                    // 输出查询信息
                    log.info(userInfo.toString());
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    public Object boolQuery() {
        try {
            // 创建 Bool 查询构建器
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            // 构建查询条件
            boolQueryBuilder.must(QueryBuilders.termsQuery("address.keyword", "北京市昌平区", "北京市大兴区", "北京市房山区"))
                    .filter().add(QueryBuilders.rangeQuery("birthDate").format("yyyy").gte("1990").lte("1995"));
            // 构建查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(boolQueryBuilder);
            // 创建查询请求对象，将查询对象配置到其中
            SearchRequest searchRequest = new SearchRequest("mydlq-user");
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 根据状态和数据条数验证是否返回了数据
            if (RestStatus.OK.equals(searchResponse.status()) && searchResponse.getHits().totalHits > 0) {
                SearchHits hits = searchResponse.getHits();
                for (SearchHit hit : hits) {
                    // 将 JSON 转换成对象
                    UserInfo userInfo = JSON.parseObject(hit.getSourceAsString(), UserInfo.class);
                    // 输出查询信息
                    log.info(userInfo.toString());
                }
            }
        }catch (IOException e){
            log.error("",e);
        }
        return null;
    }

    /**
     * stats 统计员工总数、员工工资最高值、员工工资最低值、员工平均工资、员工工资总和
     * @return
     */
    public Object aggregationStats() {
        String responseResult = "";
        try {
            // 设置聚合条件
            AggregationBuilder aggr = AggregationBuilders.stats("salary_stats").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            // 设置查询结果不返回，只返回聚合结果
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 Stats 对象
                ParsedStats aggregation = aggregations.get("salary_stats");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                log.info("count：{}", aggregation.getCount());
                log.info("avg：{}", aggregation.getAvg());
                log.info("max：{}", aggregation.getMax());
                log.info("min：{}", aggregation.getMin());
                log.info("sum：{}", aggregation.getSum());
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }

    /**
     * min 统计员工工资最低值
     * @return
     */
    public Object aggregationMin() {
        String responseResult = "";
        try {
            // 设置聚合条件
            AggregationBuilder aggr = AggregationBuilders.min("salary_min").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 Min 对象
                ParsedMin aggregation = aggregations.get("salary_min");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                log.info("min：{}", aggregation.getValue());
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }

    /**
     * max 统计员工工资最高值
     * @return
     */
    public Object aggregationMax() {
        String responseResult = "";
        try {
            // 设置聚合条件
            AggregationBuilder aggr = AggregationBuilders.max("salary_max").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 Max 对象
                ParsedMax aggregation = aggregations.get("salary_max");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                log.info("max：{}", aggregation.getValue());
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }

    /**
     * avg 统计员工工资平均值
     * @return
     */
    public Object aggregationAvg() {
        String responseResult = "";
        try {
            // 设置聚合条件
            AggregationBuilder aggr = AggregationBuilders.avg("salary_avg").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 Avg 对象
                ParsedAvg aggregation = aggregations.get("salary_avg");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                log.info("avg：{}", aggregation.getValue());
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }

    /**
     * sum 统计员工工资总值
     * @return
     */
    public Object aggregationSum() {
        String responseResult = "";
        try {
            // 设置聚合条件
            SumAggregationBuilder aggr = AggregationBuilders.sum("salary_sum").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 Sum 对象
                ParsedSum aggregation = aggregations.get("salary_sum");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                log.info("sum：{}", String.valueOf((aggregation.getValue())));
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }

    /**
     * count 统计员工总数
     * @return
     */
    public Object aggregationCount() {
        String responseResult = "";
        try {
            // 设置聚合条件
            AggregationBuilder aggr = AggregationBuilders.count("employee_count").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 ValueCount 对象
                ParsedValueCount aggregation = aggregations.get("employee_count");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                log.info("count：{}", aggregation.getValue());
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }

    /**
     * percentiles 统计员工工资百分位
     * @return
     */
    public Object aggregationPercentiles() {
        String responseResult = "";
        try {
            // 设置聚合条件
            AggregationBuilder aggr = AggregationBuilders.percentiles("salary_percentiles").field("salary");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.aggregation(aggr);
            searchSourceBuilder.size(0);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status()) || aggregations != null) {
                // 转换为 Percentiles 对象
                ParsedPercentiles aggregation = aggregations.get("salary_percentiles");
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Percentile percentile : aggregation) {
                    log.info("百分位：{}：{}", percentile.getPercent(), percentile.getValue());
                }
                log.info("-------------------------------------------");
            }
            // 根据具体业务逻辑返回不同结果，这里为了方便直接将返回响应对象Json串
            responseResult = response.toString();
        } catch (IOException e) {
            log.error("", e);
        }
        return responseResult;
    }

    /**
     * 按岁数进行聚合分桶
     * @return
     */
    public Object aggrBucketTerms() {
        try {
            AggregationBuilder aggr = AggregationBuilders.terms("age_bucket").field("age");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(10);
            searchSourceBuilder.aggregation(aggr);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status())) {
                // 分桶
                Terms byCompanyAggregation = aggregations.get("age_bucket");
                List<? extends Terms.Bucket> buckets = byCompanyAggregation.getBuckets();
                // 输出各个桶的内容
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Terms.Bucket bucket : buckets) {
                    log.info("桶名：{} | 总数：{}", bucket.getKeyAsString(), bucket.getDocCount());
                }
                log.info("-------------------------------------------");
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 按工资范围进行聚合分桶
     * @return
     */
    public Object aggrBucketRange() {
        try {
            AggregationBuilder aggr = AggregationBuilders.range("salary_range_bucket")
                    .field("salary")
                    .addUnboundedTo("低级员工", 3000)
                    .addRange("中级员工", 5000, 9000)
                    .addUnboundedFrom("高级员工", 9000);
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.aggregation(aggr);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status())) {
                // 分桶
                Range byCompanyAggregation = aggregations.get("salary_range_bucket");
                List<? extends Range.Bucket> buckets = byCompanyAggregation.getBuckets();
                // 输出各个桶的内容
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Range.Bucket bucket : buckets) {
                    log.info("桶名：{} | 总数：{}", bucket.getKeyAsString(), bucket.getDocCount());
                }
                log.info("-------------------------------------------");
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 按照时间范围进行分桶
     * @return
     */
    public Object aggrBucketDateRange() {
        try {
            AggregationBuilder aggr = AggregationBuilders.dateRange("date_range_bucket")
                    .field("birthDate")
                    .format("yyyy")
                    .addRange("1985-1990", "1985", "1990")
                    .addRange("1990-1995", "1990", "1995");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.aggregation(aggr);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status())) {
                // 分桶
                Range byCompanyAggregation = aggregations.get("date_range_bucket");
                List<? extends Range.Bucket> buckets = byCompanyAggregation.getBuckets();
                // 输出各个桶的内容
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Range.Bucket bucket : buckets) {
                    log.info("桶名：{} | 总数：{}", bucket.getKeyAsString(), bucket.getDocCount());
                }
                log.info("-------------------------------------------");
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 按工资多少进行聚合分桶
     * @return
     */
    public Object aggrBucketHistogram() {
        try {
            AggregationBuilder aggr = AggregationBuilders.histogram("salary_histogram")
                    .field("salary")
                    .extendedBounds(0, 12000)
                    .interval(3000);
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.aggregation(aggr);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status())) {
                // 分桶
                Histogram byCompanyAggregation = aggregations.get("salary_histogram");
                List<? extends Histogram.Bucket> buckets = byCompanyAggregation.getBuckets();
                // 输出各个桶的内容
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Histogram.Bucket bucket : buckets) {
                    log.info("桶名：{} | 总数：{}", bucket.getKeyAsString(), bucket.getDocCount());
                }
                log.info("-------------------------------------------");
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 按出生日期进行分桶
     * @return
     */
    public Object aggrBucketDateHistogram() {
        try {
            AggregationBuilder aggr = AggregationBuilders.dateHistogram("birthday_histogram")
                    .field("birthDate")
                    .interval(1)
                    .dateHistogramInterval(DateHistogramInterval.YEAR)
                    .format("yyyy");
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.aggregation(aggr);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status())) {
                // 分桶
                Histogram byCompanyAggregation = aggregations.get("birthday_histogram");

                List<? extends Histogram.Bucket> buckets = byCompanyAggregation.getBuckets();
                // 输出各个桶的内容
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Histogram.Bucket bucket : buckets) {
                    log.info("桶名：{} | 总数：{}", bucket.getKeyAsString(), bucket.getDocCount());
                }
                log.info("-------------------------------------------");
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * topHits 按岁数分桶、然后统计每个员工工资最高值
     * @return
     */
    public Object aggregationTopHits() {
        try {
            AggregationBuilder testTop = AggregationBuilders.topHits("salary_max_user")
                    .size(1)
                    .sort("salary", SortOrder.DESC);
            AggregationBuilder salaryBucket = AggregationBuilders.terms("salary_bucket")
                    .field("age")
                    .size(10);
            salaryBucket.subAggregation(testTop);
            // 查询源构建器
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.size(0);
            searchSourceBuilder.aggregation(salaryBucket);
            // 创建查询请求对象，将查询条件配置到其中
            SearchRequest request = new SearchRequest("mydlq-user");
            request.source(searchSourceBuilder);
            // 执行请求
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            // 获取响应中的聚合信息
            Aggregations aggregations = response.getAggregations();
            // 输出内容
            if (RestStatus.OK.equals(response.status())) {
                // 分桶
                Terms byCompanyAggregation = aggregations.get("salary_bucket");
                List<? extends Terms.Bucket> buckets = byCompanyAggregation.getBuckets();
                // 输出各个桶的内容
                log.info("-------------------------------------------");
                log.info("聚合信息:");
                for (Terms.Bucket bucket : buckets) {
                    log.info("桶名：{}", bucket.getKeyAsString());
                    ParsedTopHits topHits = bucket.getAggregations().get("salary_max_user");
                    for (SearchHit hit:topHits.getHits()){
                        log.info(hit.getSourceAsString());
                    }
                }
                log.info("-------------------------------------------");
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }
}
