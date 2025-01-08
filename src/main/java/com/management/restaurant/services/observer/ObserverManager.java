package com.management.restaurant.services.observer;
import com.management.restaurant.services.interfaces.IObservable;
import com.management.restaurant.services.interfaces.IObserver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class ObserverManager<T> implements IObservable<T> {
  private final List<IObserver<T>> IObservers = new ArrayList<>();

  @Override
  public void addObserver(IObserver<T> IObserver) {
    if (!IObservers.contains(IObserver)) {
      IObservers.add(IObserver);
    }
  }

  @Override
  public void removeObserver(IObserver<T> IObserver) {
    IObservers.remove(IObserver);
  }

  @Override
  public void notifyObservers(T data) {
    IObservers.forEach(IObserver -> {
      if (data instanceof IObserver) {
        IObserver.updateObserver(data);
      }
    });
  }
  public List<IObserver<T>> getObservers() {
    return new ArrayList<>(IObservers);
  }
}