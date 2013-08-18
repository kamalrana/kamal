package com.bebo.log4j.backend;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Logger;

public class LogData {

    private static final Logger LOG = Logger.getLogger(LogData.class.getName());
    private static final String NOT_RETRIEVED_YET = "<not retrieved yet>";
    private int id = 0;
    public static final String PROP_ID = "id";
    protected String date = NOT_RETRIEVED_YET;
    public static final String PROP_DATE = "date";
    public static final String PROP_LEVEL = "level";
    protected String level = NOT_RETRIEVED_YET;
    protected String message = NOT_RETRIEVED_YET;
    public static final String PROP_MESSAGE = "message";
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

   public LogData(int j) {
        this.id = j;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        String oldDate = this.date;
        this.date = date;
        propertyChangeSupport.firePropertyChange(PROP_DATE, oldDate, date);
    }
    public String getLevel() {
        return level;
    }

    public void setLevel( String level ) {
        String oldLevel = this.level;
        this.level = level;        
        propertyChangeSupport.firePropertyChange( PROP_LEVEL , oldLevel, level );
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        String oldMessage = this.message;
        this.message = message;
        if (propertyChangeSupport.hasListeners(PROP_MESSAGE)) {
//            LOG.info("notifying!!!");
        } else {
//            LOG.info("there is no listeners for the property");
        }

        propertyChangeSupport.firePropertyChange(PROP_MESSAGE, oldMessage, message);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
//        LOG.info("add listener to LogData " + this.id);
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return "id: " + id + " date: " + date;
    }
}