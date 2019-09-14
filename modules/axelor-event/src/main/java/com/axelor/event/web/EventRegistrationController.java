package com.axelor.event.web;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.exception.IExceptionMessage;
import com.axelor.i18n.I18n;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class EventRegistrationController {
  
  
  public void validateRegistration(ActionRequest request, ActionResponse response) {

    EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
    Event event = request.getContext().getParent().asType(Event.class);
    
    System.err.println(event.getRegistrationClose().compareTo(event.getRegistrationOpen()) ); 
    
    if (event.getRegistrationOpen().isAfter(eventRegistration.getRegistration().toLocalDate())
        || event.getRegistrationClose().isBefore(eventRegistration.getRegistration().toLocalDate())) {
      response.setError(I18n.get(IExceptionMessage.REGISTRATION_DATE));
    }
  }
  
}
