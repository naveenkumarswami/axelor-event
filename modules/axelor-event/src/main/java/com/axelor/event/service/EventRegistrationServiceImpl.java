package com.axelor.event.service;

import java.util.Comparator;
import java.util.List;
import com.axelor.event.db.Discount;
import com.axelor.event.db.Event;
import com.axelor.event.db.EventRegistration;

public class EventRegistrationServiceImpl implements EventRegistrationService {

  @Override
  public EventRegistration compute(Event event, EventRegistration eventRegistration) {

    Integer diffCloseAndRegist =
        event
            .getRegistrationCloseDate()
            .compareTo(eventRegistration.getRegistrationDateT().toLocalDate());
    System.err.println(diffCloseAndRegist);
    List<Discount> discountList = event.getDiscountList();
    Discount discount = null;
    if (discountList != null) {
      discount =
          discountList
              .stream()
              .filter(a -> a.getBeforeDays() <= diffCloseAndRegist)
              .max(Comparator.comparing(Discount::getDiscountAmount))
              .orElse(null);
    }
    if (discount != null) {
      eventRegistration.setAmount(event.getEventFees().subtract(discount.getDiscountAmount()));
      System.err.println(discount);
    } else {
      eventRegistration.setAmount(event.getEventFees());
    }

    return eventRegistration;
  }
}
