package com.axelor.event.module;

import com.axelor.app.AxelorModule;
import com.axelor.event.service.EventService;
import com.axelor.event.service.EventServiceImpl;

public class EventModule extends AxelorModule {

    @Override
    protected void configure() {
      
      bind(EventService.class).to(EventServiceImpl.class);
      
    }
}
