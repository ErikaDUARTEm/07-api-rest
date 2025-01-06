package com.management.restaurant.services.interfaces;


public interface Observable<T> {
  void addObserver(Observer<T>  observer);
  void removeObserver(Observer<T>  observer);
  void notifyObservers(T t);
}
