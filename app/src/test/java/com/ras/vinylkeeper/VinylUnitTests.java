package com.ras.vinylkeeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ras.vinylkeeper.database.typeConverter.LocalDateTypeConverter;

import org.junit.Test;

import java.time.LocalDateTime;

public class VinylUnitTests {

    @Test
    public void DateToLongConversion() {
        LocalDateTime date = LocalDateTime.now();
        long convertedDateToLong = new LocalDateTypeConverter().convertDataToLong(date);
    }

    @Test
    public void LongToDateConversion() {
        LocalDateTime date = LocalDateTime.now();
        long convertedDateToLong = new LocalDateTypeConverter().convertDataToLong(date);
        LocalDateTime newDate = new LocalDateTypeConverter().convertLongToDate(convertedDateToLong);
    }
}