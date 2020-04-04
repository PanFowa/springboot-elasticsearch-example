package com.pan.service;


import com.pan.App;
import com.pan.service.es.IndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class IndexServiceV7Test {
    @Autowired
    @Qualifier("IndexServiceImplV7")
    IndexService indexService;

    @Test
    public void createIndex() {
        indexService.createIndex();
    }

    @Test
    public void deleteIndex() {
        indexService.deleteIndex();
    }
}