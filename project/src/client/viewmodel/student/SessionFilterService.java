package client.viewmodel.student;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.transformation.FilteredList;
import server.model.Session;

public class SessionFilterService
{
  private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private final StringProperty subjectFilter;
  private final StringProperty minPrice;
  private final StringProperty maxPrice;
  private final StringProperty fromDate;
  private final StringProperty toDate;

  private final FilteredList<Session> filteredSessions;

  public SessionFilterService(FilteredList<Session> filteredSessions)
  {
    this.filteredSessions = filteredSessions;

    subjectFilter = new SimpleStringProperty("All");
    minPrice = new SimpleStringProperty("");
    maxPrice = new SimpleStringProperty("");
    fromDate = new SimpleStringProperty("");
    toDate = new SimpleStringProperty("");

    subjectFilter.addListener((obs, o, n) -> applyFilter());
    minPrice.addListener((obs, o, n) -> applyFilter());
    maxPrice.addListener((obs, o, n) -> applyFilter());
    fromDate.addListener((obs, o, n) -> applyFilter());
    toDate.addListener((obs, o, n) -> applyFilter());
  }

  public void applyFilter()
  {
    filteredSessions.setPredicate(session -> {
      // Subject filter
      String subject = subjectFilter.get();
      if (subject != null && !subject.equals("All") && !subject.isBlank())
      {
        if (!subject.equals(session.getSubject())) return false;
      }

      // Min price filter
      String minP = minPrice.get();
      if (minP != null && !minP.isBlank())
      {
        try
        {
          if (session.getPrice() < Double.parseDouble(minP)) return false;
        }
        catch (NumberFormatException ignored) {}
      }

      // Max price filter
      String maxP = maxPrice.get();
      if (maxP != null && !maxP.isBlank())
      {
        try
        {
          if (session.getPrice() > Double.parseDouble(maxP)) return false;
        }
        catch (NumberFormatException ignored) {}
      }

      // From date filter
      String from = fromDate.get();
      if (from != null && !from.isBlank())
      {
        try
        {
          LocalDate fd = LocalDate.parse(from, DATE_FMT);
          if (session.getStart_date().toLocalDate().isBefore(fd)) return false;
        }
        catch (DateTimeParseException ignored) {}
      }

      // To date filter
      String to = toDate.get();
      if (to != null && !to.isBlank())
      {
        try
        {
          LocalDate td = LocalDate.parse(to, DATE_FMT);
          if (session.getStart_date().toLocalDate().isAfter(td)) return false;
        }
        catch (DateTimeParseException ignored) {}
      }

      return true;
    });
  }

  public void resetFilters()
  {
    subjectFilter.set("All");
    minPrice.set("");
    maxPrice.set("");
    fromDate.set("");
    toDate.set("");
  }

  public StringProperty subjectFilterProperty() { return subjectFilter; }
  public StringProperty minPriceProperty() { return minPrice; }
  public StringProperty maxPriceProperty() { return maxPrice; }
  public StringProperty fromDateProperty() { return fromDate; }
  public StringProperty toDateProperty() { return toDate; }
}
