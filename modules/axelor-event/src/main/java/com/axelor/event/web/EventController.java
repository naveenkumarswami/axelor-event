package com.axelor.event.web;

import com.axelor.event.db.Event;
import com.axelor.event.exception.IExceptionMessage;
import com.axelor.i18n.I18n;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class EventController {

  public void validateRegistration(ActionRequest request, ActionResponse response) {

    Event event = request.getContext().asType(Event.class);
    
    if (event.getCapacity() < event.getEventRegistrations().size()) {
      response.setError(I18n.get(IExceptionMessage.REGISTRATION_EXVEEDS_CAPACITY));
    }
  }
}
