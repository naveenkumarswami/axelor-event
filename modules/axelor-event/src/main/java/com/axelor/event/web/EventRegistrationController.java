package com.axelor.event.web;

import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.exception.IExceptionMessage;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.i18n.I18n;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class EventRegistrationController {
  
  @Inject
  EventRegistrationService eventRegistrationService;

  public void validateRegistration(ActionRequest request, ActionResponse response) {


    EventRegistration eventRegistration = request.getContext().asType(EventRegistration.class);
    Event event;
        
    if(eventRegistration.getEvent()==null){
    event = request.getContext().getParent().asType(Event.class);
    }
    else {
    event = eventRegistration.getEvent();
    }

    if (event.getRegistrationOpenDate().isAfter(eventRegistration.getRegistrationDateT().toLocalDate())
        || event
            .getRegistrationCloseDate()
            .isBefore(eventRegistration.getRegistrationDateT().toLocalDate())) {
      response.setError(I18n.get(IExceptionMessage.REGISTRATION_DATE));
    }
    eventRegistration = eventRegistrationService.compute(event, eventRegistration);
    response.setValues(eventRegistration);
  }
}
