package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AppModel implements IAppModel {

    private PropertyChangeSupport subscribers;

    public AppModel() {
        this.subscribers = new PropertyChangeSupport(this);
    }

    /*
     * Methods for observers of the model.
     */

    public void subscribe(PropertyChangeListener listener) {
        subscribers.addPropertyChangeListener(listener);
    }

    public void unsubscribe(PropertyChangeListener listener) {
        subscribers.removePropertyChangeListener(listener);
    }

    public void notifySubscribers(String property, String notification) {
        subscribers.firePropertyChange(property, "oldValue", notification);
    }
}
