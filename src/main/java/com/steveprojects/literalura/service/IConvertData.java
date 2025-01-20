package com.steveprojects.literalura.service;

public interface IConvertData {
    <T> T getData(String json, Class<T> clase);
}
