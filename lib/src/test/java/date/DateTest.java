package date;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

public class DateTest {


    @Test
    void ibkrDateFormatTest(){
        DateTimeFormatter ZONE_DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss VV");
        String zdtString = "20210101 00:00:00 America/Chicago";
        ZonedDateTime zdt = ZonedDateTime.parse(zdtString, ZONE_DATE_TIME_FORMAT);
        assertEquals(ZONE_DATE_TIME_FORMAT.format(zdt), zdtString);

        

    }
    
}
