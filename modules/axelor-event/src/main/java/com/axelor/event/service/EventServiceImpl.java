package com.axelor.event.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.mail.MessagingException;
import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.repo.TemplateRepository;
import com.axelor.apps.message.service.TemplateMessageService;
import com.axelor.data.Importer;
import com.axelor.data.Listener;
import com.axelor.data.csv.CSVImporter;
import com.axelor.db.Model;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;
import com.axelor.exception.AxelorException;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class EventServiceImpl implements EventService {

  protected TemplateMessageService templateMessageService;
  @Inject TemplateRepository templateRepository;
  
  @Inject public EventServiceImpl(TemplateMessageService templateMessageService) {
    this.templateMessageService = templateMessageService;
  }

  @Override
  public Event compute(Event event) {
    List<EventRegistration> eventRegistrationList = event.getEventRegistrationList();
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
    event.setAmountCollected(totalAmount);
    event.setTotalDiscount(totalDiscount);

    return event;
  }

  @Override
  public boolean importCsvFile(File file) {

    Importer importfile =
        new CSVImporter(
            "/home/axelor/Practical Test Data Import/input-config.xml", file.getParent());
    Listener listener =
        new Listener() {

          @Override
          public void imported(Integer total, Integer success) {
            // TODO Auto-generated method stub

          }

          @Override
          public void imported(Model bean) {
            // TODO Auto-generated method stub

          }

          @Override
          public void handle(Model bean, Exception e) {
            // TODO Auto-generated method stub

          }
        };

    importfile.addListener(listener);
    importfile.run();

    return true;
  }

  @Override @Transactional
  public Message sendConfirmationEmail(EventRegistration eventRegistration) {
    
    System.err.println(templateMessageService); 
    
    try {
//    templateMessageService.generateAndSendMessage(
//        event, templateRepository.findByName("Event"));
//    System.err.println("done" ); 
      return templateMessageService.generateAndSendMessage(
          eventRegistration, templateRepository.findByName("Event"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
