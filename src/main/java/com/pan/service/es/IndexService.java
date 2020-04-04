package com.pan.service.es;

public interface IndexService {
    public void createIndex();

    public void deleteIndex();
    public void addDocument();
    public void getDocument();
    public void updateDocument();
    public void deleteDocument();
}
