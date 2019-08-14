//package io.temco.guhada.blockchain.feature;
//
//import org.caverj.utils.HumanReadableAddressUtils;
//import org.junit.Test;
//
//import static junit.framework.TestCase.*;
//
//public class HumanReadableAddressTest {
//
//    @Test
//    public void testCommonRawAddress() {
//        String address = "0x90B3E9A3770481345A7F17f22f16D020Bccfd33e";
//        assertFalse(HumanReadableAddressUtils.isHumanReadableAddress(address));
//    }
//
//    @Test
//    public void testAbnormalRawAddress() {
//        String address = "0x90B3E9A37702f16D020Bccfd33";
//        assertFalse(HumanReadableAddressUtils.isHumanReadableAddress(address));
//    }
//
//    @Test
//    public void testNormalHumanReadableAddress() {
//        String address = "luman.klaytn";
//        assertTrue(HumanReadableAddressUtils.isHumanReadableAddress(address));
//    }
//
//    @Test
//    public void testNormalHumanReadableAddress2() {
//        String address5 = "lumanluman.klaytn";
//        assertTrue(HumanReadableAddressUtils.isHumanReadableAddress(address5));
//    }
//
//    @Test
//    public void testNoIdentifier() {
//        String address = "brandon";
//        assertFalse(HumanReadableAddressUtils.isHumanReadableAddress(address));
//    }
//
//    @Test
//    public void testNoIdentifier2() {
//        String address = "a.";
//        assertFalse(HumanReadableAddressUtils.isHumanReadableAddress(address));
//    }
//
//    @Test
//    public void testLengthExceed() {
//        String address = "lumanlumanlumanlumanluman.klaytn";
//        assertFalse(HumanReadableAddressUtils.isHumanReadableAddress(address));
//    }
//
//    @Test
//    public void testLengthTooShort() {
//        String address = "a.klaytn";
//        assertFalse(HumanReadableAddressUtils.isHumanReadableAddress(address));
//    }
//
//    @Test
//    public void testStartedWithNumber() {
//        String address = "5blahbl";
//        assertFalse(HumanReadableAddressUtils.isHumanReadableAddress(address));
//    }
//
//    @Test
//    public void testWithCapitalLetter() {
//        String address = "WAYNE.klaytn";
//        assertTrue(HumanReadableAddressUtils.isHumanReadableAddress(address));
//    }
//
//    @Test
//    public void testToRawAddress() {
//        String address = "luman.klaytn";
//        assertEquals("0x6c756d616e2e6b6c6179746e0000000000000000",
//                HumanReadableAddressUtils.toRawAddress(address));
//    }
//
//    @Test
//    public void testToRawAddress2() {
//        String address = "WAYNE.klaytn";
//        assertEquals("0x5741594e452e6b6c6179746e0000000000000000",
//                HumanReadableAddressUtils.toRawAddress(address));
//    }
//}
