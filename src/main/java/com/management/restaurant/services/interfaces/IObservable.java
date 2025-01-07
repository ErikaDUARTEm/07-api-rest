package com.management.restaurant.services.interfaces;


public interface IObservable<T> {
  void addObserver(IObserver<T> IObserver);
  void removeObserver(IObserver<T> IObserver);
  void notifyObservers(T t);
}
