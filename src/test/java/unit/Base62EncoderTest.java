package unit;

import org.urlshortner.generator.Base62Encoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Base62EncoderTest {

    private final Base62Encoder encoder = new Base62Encoder();

    @Test
    void shouldGenerateDifferentCodesForDifferentIds() {
        String code1 = encoder.encode(1);
        String code2 = encoder.encode(2);

        assertNotEquals(code1, code2);
    }

    @Test
    void shouldGenerateSameCodeForSameId() {
        String code1 = encoder.encode(100);
        String code2 = encoder.encode(100);

        assertEquals(code1, code2);
    }

    @Test
    void shouldHandleZero() {
        assertEquals("a", encoder.encode(0));
    }
}