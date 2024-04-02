import org.example.searadar.mr231_3.convert.Mr231_3Converter;
import org.example.searadar.mr231_3.station.Mr231_3StationType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ru.oogis.searadar.api.message.InvalidMessage;
import ru.oogis.searadar.api.message.RadarSystemDataMessage;
import ru.oogis.searadar.api.message.SearadarStationMessage;
import ru.oogis.searadar.api.message.TrackedTargetMessage;

import java.util.List;

public class TestMr231_3Converter {

    /**
     * Необходимые переменные и значения для тестовых методов.
     */
    Mr231_3StationType mr231_3 = new Mr231_3StationType();
    Mr231_3Converter mr231_3Converter = mr231_3.createConverter();

    String CorrectTTM = "$RATTM,23,13.88,137.2,T,63.8,094.3,T,9.2,79.4,N,b,T,,783344,А*42";
    String CorrectRSD = "$RARSD,36.5,331.4,8.4,320.6,,,,,11.6,185.3,96.0,N,N,S*33";

    String IncorrectMessage = "RAR";
    String IncorrectDistanceScaleRSD = "$RARSD,36.5,331.4,8.4,320.6,,,,,11.6,185.3,95.0,N,N,S*33";

    /**
     * Тестовый метод для проверки корректности преобразования TTM (Tracked Target Message) из MR231-3 формата в формат станции.
     * Этот метод проверяет, что преобразованный объект является экземпляром класса TrackedTargetMessage,
     * и затем проверяет поля объекта на соответствие ожидаемым значениям.
     */
    @Test
    public void testCorrectTTM(){
        List<SearadarStationMessage> stationMessages = mr231_3Converter.convert(CorrectTTM);
        assertTrue(stationMessages.get(0) instanceof TrackedTargetMessage, "Incorrect message type! Expected TrackedTargetMessage but get " + stationMessages.get(0).getClass());

        TrackedTargetMessage ttm = (TrackedTargetMessage) stationMessages.get(0);
        assertEquals(23, ttm.getTargetNumber(), "Incorrect target number!");
        assertEquals(13.88, ttm.getDistance(), "Incorrect distance!");
        assertEquals(137.2, ttm.getBearing(), "Incorrect bearing!");
        assertEquals(94.3, ttm.getCourse(), "Incorrect course!");
        assertEquals(63.8, ttm.getSpeed(), "Incorrect speed!");
        assertEquals("UNKNOWN", ttm.getType().name(), "Incorrect type!");
        assertEquals("TRACKED", ttm.getStatus().name(), "Incorrect status!");
        assertEquals("FRIEND", ttm.getIff().name(), "Incorrect iff!");
    }


    /**

     * Тестовый метод для проверки корректности преобразования RSD (Radar System Data) из MR231-3 формата в формат станции.
     * Этот метод проверяет, что преобразованный объект является экземпляром класса RadarSystemDataMessage,
     * и затем проверяет поля объекта на соответствие ожидаемым значениям.
     */
    @Test
    public void testCorrectRSD(){
        List<SearadarStationMessage> stationMessages = mr231_3Converter.convert(CorrectRSD);
        assertTrue(stationMessages.get(0) instanceof RadarSystemDataMessage, "Incorrect message type! Expected RadarSystemDataMessage but get " + stationMessages.get(0).getClass());

        RadarSystemDataMessage rsd = (RadarSystemDataMessage) stationMessages.get(0);
        assertEquals(36.5, rsd.getInitialDistance(), "Incorrect initial distance!");
        assertEquals(331.4, rsd.getInitialBearing(), "Incorrect initial bearing!");
        assertEquals(8.4, rsd.getMovingCircleOfDistance(), "Incorrect moving circle of distance!");
        assertEquals(320.6, rsd.getBearing(), "Incorrect bearing!");
        assertEquals(11.6, rsd.getDistanceFromShip(), "Incorrect distance from ship!");
        assertEquals(185.3, rsd.getBearing2(), "Incorrect bearing 2!");
        assertEquals(96.0, rsd.getDistanceScale(), "Incorrect distance scale!");
        assertEquals("N", rsd.getDistanceUnit(), "Incorrect distance unit!");
        assertEquals("N", rsd.getDisplayOrientation(), "Incorrect display orientation!");
        assertEquals("S", rsd.getWorkingMode(), "Incorrect working mode!");
    }



    /**
     * Тестовый метод для проверки того, что конвертер корректно отрабатывает исключение
     * StringIndexOutOfBoundsException при попытке преобразования неправильного сообщения.
     */
    @Test
    public void testIncorrectMessage(){
        assertThrows(StringIndexOutOfBoundsException.class, () -> {
           mr231_3Converter.convert(IncorrectMessage);
        }, "Converter must throw StringIndexOutOfBoundsException!");
    }

    /**
     * Тестовый метод для проверки обработки сообщения RSD с неверным значением масштаба расстояния.
     * Этот метод проверяет, что конвертер корректно определяет сообщение как невалидное и возвращает соответствующий объект InvalidMessage.
     */
    @Test
    public void testIncorrectDistanceScaleRSD(){
        List<SearadarStationMessage> stationMessages = mr231_3Converter.convert(IncorrectDistanceScaleRSD);
        assertTrue(stationMessages.get(0) instanceof InvalidMessage, "Incorrect message type! Expected InvalidMessage but get " + stationMessages.get(0).getClass());

        InvalidMessage invalidMessage = (InvalidMessage) stationMessages.get(0);
        assertEquals("RSD message. Wrong distance scale value: 95.0", invalidMessage.getInfoMsg(), "Incorrect invalid message!");
    }
}
