package com.example.infinitepocket;

public interface Observable<T> {
    void fire(T source);
}
