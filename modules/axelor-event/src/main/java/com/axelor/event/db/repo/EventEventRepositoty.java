package com.axelor.event.db.repo;

import java.util.Map;
import com.axelor.event.db.Event;

public class EventEventRepositoty extends EventRepository {
  
  @Override
  public Map<String, Object> populate(Map<String, Object> json, Map<String, Object> context) {
    if (!context.containsKey("json-enhance")) {
      return json;
    }
    try {
      Long id = (Long) json.get("id");
    } catch (Exception e) {
      e.printStackTrace();
    }

    return json;
  }
}
