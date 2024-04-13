package com.tiwilli.bechosystem.gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Utils {

    public static Stage currentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static Integer tryParseToInt(String str) {
        try {
            return Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double tryParseToDouble(String str) {
        try {
            return Double.parseDouble(str);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
        tableColumn.setCellFactory(column -> {
            TableCell<T, Date> cell = new TableCell<T, Date>() {
                private SimpleDateFormat sdf = new SimpleDateFormat(format);
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(sdf.format(item));
                    }
                }
            };
            return cell;
        });
    }

    public static String formatLabelDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return date != null ? sdf.format(date) : "";
    }

    public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
        tableColumn.setCellFactory(column -> {
            TableCell<T, Double> cell = new TableCell<T, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        Locale.setDefault(Locale.US);
                        setText(String.format("%." + decimalPlaces + "f", item));
                    }
                }
            };
            return cell;
        });
    }

    public static void formatDatePicker(DatePicker datePicker, String format) {
        datePicker.setConverter(new StringConverter<LocalDate>() {

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
            {
                datePicker.setPromptText(format.toLowerCase());
            }
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    public static void trySetPreparedStatementToDouble(PreparedStatement st, int index, Double value) throws SQLException {
        st.setDouble(index, Objects.requireNonNullElse(value, 0.0));
    }

    public static void trySetPreparedStatementToDate(PreparedStatement st, int index, Date date) throws SQLException {
        if (date == null) {
            st.setNull(index, Types.DATE);
        }
        else {
            st.setDate(index, new java.sql.Date(date.getTime()));
        }
    }

    public static Date getDateOrNull(ResultSet rs, String columnLabel) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnLabel);
        return (timestamp != null) ? new java.util.Date(timestamp.getTime()) : null;
    }
}
