package no.ssb.kostra.test.control.sensitiv.meklinger_55;

import no.ssb.kostra.control.sensitiv.meklinger_55.ControlDubletter;
import no.ssb.kostra.control.sensitiv.meklinger_55.ControlFylkesnummer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ojj on 20.06.2018.
 */
public class MeklingerControlsTest {
    //                     00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000011111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111112222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333344444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444
    //                     00000000011111111112222222222333333333344444444445555555555666666666677777777778888888888999999999900000000001111111111222222222233333333334444444444555555555566666666667777777777888888888899999999990000000000111111111122222222223333333333444444444455555555556666666666777777777788888888889999999999000000000011111111112222222222333333333344444444445555555555666666666677777777778888888888999999999900000000001111111111222222222233333333334444444444555555555566666666667777777777888888888899
    //                     12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901
    private String ok_1 = "01Østfold             3002000200520052000500100260101000400200160051010100300006100300006009700203002200520200060026010006001601020030061003600970200100200020052010010005001002600500500400200160100505100300360025502950060097004002000603002000200520200050010026010004002001601010100300610030000600970500400300051205300200020052020005001002601000400200160101010030061003000060097020005005003001000500500200150040010020005050001004550145011007103002200520200060026010006001601020030061003600970";
    private String fail = "13Østfold             3002000200520052000500100260101000400200160051010100300006100300006009700203002200520200060026010006001601020030061003600970200100200020052010010005001002600500500400200160100505100300360025502950060097004002000603002000200520200050010026010004002001601010100300610030000600970500400300051205300200020052020005001002601000400200160101010030061003000060097020005005003001000500500200150040010020005050001004550145011007103002200520200060026010006001601020030061003600970";

    @Test
    public void controlFylkesnummerTest(){
        ControlFylkesnummer c = new ControlFylkesnummer();
        assertFalse(c.doControl(ok_1, 1, "", ""));
        assertTrue(c.doControl(fail, 1, "", ""));
    }

    @Test
    public void controlDubletterTest() {
        ControlDubletter c = new ControlDubletter();
        assertFalse(c.doControl(ok_1, 1, "", ""));
        assertTrue(c.doControl(ok_1, 1, "", ""));
        assertTrue(c.doControl(ok_1, 1, "", ""));
    }
}
