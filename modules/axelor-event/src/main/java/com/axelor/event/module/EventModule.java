package com.axelor.event.module;

import com.axelor.app.AxelorModule;
import com.axelor.event.db.repo.EventRegistrationRepository;
import com.axelor.event.db.repo.EventRegistroyEventRepository;
import com.axelor.event.service.EventRegistrationService;
import com.axelor.event.service.EventRegistrationServiceImpl;
import com.axelor.event.service.EventService;
import com.axelor.event.service.EventServiceImpl;

public class EventModule extends AxelorModule {

    @Override
    protected void configure() {
      
      bind(EventService.class).to(EventServiceImpl.class);
      bind(EventRegistrationService.class).to(EventRegistrationServiceImpl.class);
      bind(EventRegistrationRepository.class).to(EventRegistroyEventRepository.class);
      
    }
}
