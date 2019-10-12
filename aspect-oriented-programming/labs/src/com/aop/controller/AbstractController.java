package com.aop.controller;
import com.aop.aspect.Observer;

public abstract class AbstractController implements Observer
{
    abstract public void close();
}
