package com.axelor.event.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import com.axelor.data.Importer;
import com.axelor.data.Listener;
import com.axelor.data.csv.CSVImporter;
import com.axelor.db.Model;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public class EventServiceImpl implements EventService {

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
      totalDiscount = event.getEventFees().multiply(new BigDecimal(eventRegistrationList.size())).subtract(totalAmount);
      
    }
    event.setAmountCollected(totalAmount);
    event.setTotalDiscount(totalDiscount);
    
    return event;
  }

  @Override
  public boolean importCsvFile(File file) {

    Importer importfile =
        new CSVImporter(
            "/home/axelor/Practical Test Data Import/input-config.xml",
            file.getAbsolutePath());
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
}
