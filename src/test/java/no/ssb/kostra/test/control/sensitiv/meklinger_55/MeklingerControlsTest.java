package no.ssb.kostra.test.control.sensitiv.meklinger_55;

import no.ssb.kostra.control.sensitiv.meklinger_55.*;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ojj on 20.06.2018.
 */
public class MeklingerControlsTest {
    //                     000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111122222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222223333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444455555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555555556
    //                     000000000111111111122222222223333333333444444444455555555556666666666777777777788888888889999999999000000000011111111112222222222333333333344444444445555555555666666666677777777778888888888999999999900000000001111111111222222222233333333334444444444555555555566666666667777777777888888888899999999990000000000111111111122222222223333333333444444444455555555556666666666777777777788888888889999999999000000000011111111112222222222333333333344444444445555555555666666666677777777778888888888999999999900000000001111111111222222222233333333334444444444555555555566666666667777777777888888888899999999990
    //                     123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890
    private String ok_1 = "01             Østfold01002003000600501002003000600501002003000600510203006005010020030006000500500100015003000250250350060025035006002503500602535060025035006001250175030000501001503000600050100150300060005010015030006005101530060005010015030006000250050007501500300050075012501002003000600100200300060010020030006010203006001002003000600050010001500300010020030400500150045010005006004501000500600450100050060451005060045010005006002250050002503000100050300045010005030004501000503000451005300450100050300045005000250150022505001000600500100060050010006050100600500100060025000500300";
    private String fail = "99             Østfold36500206501651065216510605620065612610610618506561500100260101000400200160051010100300001100500500210120610030000600970020300220052020006002601000600160102000060060012300610036009702001002000200520100100050010026005005004002001601005051003003600255010010000000002002950060097004002000603002000100200100040020052020005001002601000400056200160101010030061003000060097050040030005120530020001100001100220200520200050010026010004002001601010100300610030000600970200050002002006001005003001000500500200150040010020005050001004550145011050020007000710200029000000000002";

    @Test
    public void controlFylkesnummerTest(){
        ControlFylkesnummer c = new ControlFylkesnummer();
        assertFalse(c.doControl(ok_1, 1, "", ""));
        assertTrue(c.doControl(fail, 2, "", ""));
    }

    @Test
    public void controlDubletterTest() {
        ControlDubletter c = new ControlDubletter();
        assertFalse(c.doControl(ok_1, 1, "", ""));
        assertTrue(c.doControl(ok_1, 1, "", ""));
        assertTrue(c.doControl(ok_1, 1, "", ""));
    }

    @Test
    public void control5TestOK() {
        Control5 c = new Control5();
        assertFalse(c.doControl(ok_1, 1, "", ""));
    }

    @Test
    public void control5TestFail() {
        Control5 c = new Control5();
        assertTrue(c.doControl(fail, 2, "", ""));
    }

    @Test
    public void control6TestOK() {
        Control6 c = new Control6();
        assertFalse(c.doControl(ok_1, 1, "", ""));
    }

    @Test
    public void control6TestFail() {
        Control6 c = new Control6();
        assertTrue(c.doControl(fail, 2, "", ""));
    }

    @Test
    public void control7TestOK() {
        Control7 c = new Control7();
        assertFalse(c.doControl(ok_1, 1, "", ""));
    }

    @Test
    public void control7TestFail() {
        Control7 c = new Control7();
        assertTrue(c.doControl(fail, 2, "", ""));
    }

    @Test
    public void control8TestOK() {
        Control8 c = new Control8();
        assertFalse(c.doControl(ok_1, 1, "", ""));
    }

    @Test
    public void control8TestFail() {
        Control8 c = new Control8();
        assertTrue(c.doControl(fail, 2, "", ""));
    }

    @Test
    public void control9TestOK() {
        Control9 c = new Control9();
        assertFalse(c.doControl(ok_1, 1, "", ""));
    }

    @Test
    public void control9TestFail() {
        Control9 c = new Control9();
        assertTrue(c.doControl(fail, 2, "", ""));
    }

    @Test
    public void control10TestOK() {
        Control10 c = new Control10();
        assertFalse(c.doControl(ok_1, 1, "", ""));
    }

    @Test
    public void control10TestFail() {
        Control10 c = new Control10();
        assertTrue(c.doControl(fail, 2, "", ""));
    }

    @Test
    public void control11TestOK() {
        Control11 c = new Control11();
        assertFalse(c.doControl(ok_1, 1, "", ""));
    }

    @Test
    public void control11TestFail() {
        Control11 c = new Control11();
        assertTrue(c.doControl(fail, 2, "", ""));
    }

    @Test
    public void control12TestOK() {
        Control12 c = new Control12();
        assertFalse(c.doControl(ok_1, 1, "", ""));
    }

    @Test
    public void control12TestFail() {
        Control12 c = new Control12();
        assertTrue(c.doControl(fail, 2, "", ""));
    }

    @Test
    public void control13TestOK() {
        Control13 c = new Control13();
        assertFalse(c.doControl(ok_1, 1, "", ""));
    }

    @Test
    public void control13TestFail() {
        Control13 c = new Control13();
        assertTrue(c.doControl(fail, 2, "", ""));
    }

    @Test
    public void control14TestOK() {
        Control14 c = new Control14();
        assertFalse(c.doControl(ok_1, 1, "", ""));
    }

    @Test
    public void control14TestFail() {
        Control14 c = new Control14();
        assertTrue(c.doControl(fail, 2, "", ""));
    }
    @Test
    public void control15TestOK() {
        Control15 c = new Control15();
        assertFalse(c.doControl(ok_1, 1, "", ""));
    }

    @Test
    public void control15TestFail() {
        Control15 c = new Control15();
        assertTrue(c.doControl(fail, 2, "", ""));
    }
}
