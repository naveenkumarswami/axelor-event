package com.axelor.event.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.repo.TemplateRepository;
import com.axelor.apps.message.service.TemplateMessageService;
import com.axelor.data.Importer;
import com.axelor.data.Listener;
import com.axelor.data.csv.CSVImporter;
import com.axelor.db.Model;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.db.repo.EventRegistrationRepository;
import com.axelor.event.db.repo.EventRepository;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EventServiceImpl implements EventService {

  protected TemplateMessageService templateMessageService;
  @Inject TemplateRepository templateRepository;
  @Inject EventRepository eventRepository;
  @Inject EventRegistrationService eventRegistrationService;
  @Inject EventRegistrationRepository eventRegistrationRepository;

  @Inject
  public EventServiceImpl(TemplateMessageService templateMessageService) {
    this.templateMessageService = templateMessageService;
  }

  @Override
  public Event compute(Event event) {
    List<EventRegistration> eventRegistrationList = event.getEventRegistrationList();
    System.err.println("test12334  :  " + eventRegistrationList);
    BigDecimal totalAmount = BigDecimal.ZERO;
    BigDecimal totalDiscount = BigDecimal.ZERO;

    if (eventRegistrationList != null) {

      totalAmount =
          eventRegistrationList
              .stream()
              .map(i -> i.getAmount())
              .reduce(BigDecimal.ZERO, BigDecimal::add);
      totalDiscount =
          event
              .getEventFees()
              .multiply(new BigDecimal(eventRegistrationList.size()))
              .subtract(totalAmount);
    }
    System.err.println("totalAmount :" + totalAmount);
    System.err.println("totalDiscount :" + totalDiscount);
    event.setTotalEntry(eventRegistrationList.size());
    event.setAmountCollected(totalAmount);
    event.setTotalDiscount(totalDiscount);

    return event;
  }

  @Override
  public Boolean importCsvFile(File file, Map<String, Object> importContext) {

    // List<EventRegistration> eventRegistrationList = event.getEventRegistrationList();

    Importer importfile =
        new CSVImporter(
            "/home/axelor/Projects/ADK Test/axelor-test/modules/axelor-event/src/main/resources/demo/input-config.xml",
            file.getParent());
    Listener listener =
        new Listener() {

          @Override
          public void imported(Integer total, Integer success) {}

          @Override
          public void imported(Model bean) {}

          @Override
          public void handle(Model bean, Exception e) {}
        };

    importfile.setContext(importContext);
    importfile.addListener(listener);
    importfile.run();
    return true;
  }

  @Override
  @Transactional
  public Message sendConfirmationEmail(EventRegistration eventRegistration) {

    System.err.println(templateMessageService);

    try {
      return templateMessageService.generateAndSendMessage(
          eventRegistration, templateRepository.findByName("Event"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
