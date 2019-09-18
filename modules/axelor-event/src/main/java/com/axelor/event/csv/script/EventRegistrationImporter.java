package com.axelor.event.csv.script;

import java.util.Map;
import com.axelor.event.db.EventRegistration;

public class EventRegistrationImporter {
  
  public Object checkValues(Object bean, Map<String, Object> values) { 
    
    assert bean instanceof EventRegistration;
    EventRegistration eventRegistration = (EventRegistration) bean;
    System.out.println(eventRegistration ); 

//    values.forEach(
//        (key, value) -> {
//          System.err.println("key :"+key + " value  :" + value);
//        });
    
    return eventRegistration;
    
  }
  
  
}
