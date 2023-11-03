import static org.junit.Assert.assertEquals;

import org.junit.Test;

import polyu.learn.bbssoadfs.operation.cadn.CADN;
import polyu.learn.bbssoadfs.operation.cadn.CADNSISWrapper;


public class BbIntegratorTest {
	
	@Test
	public void testFirstAndLastName(){
		CADN cadn = new CADN();
		CADNSISWrapper candSISWrapper = new CADNSISWrapper(cadn);
		
		cadn.setPuUserFullName("CHAN Siu Ming");
		assertEquals("Siu Ming", candSISWrapper.getFirstName());
		assertEquals("CHAN", candSISWrapper.getLastName());
		
		cadn.setPuUserFullName("AU YEUNG Tai Ming");
		assertEquals("Tai Ming", candSISWrapper.getFirstName());
		assertEquals("AU YEUNG", candSISWrapper.getLastName());
		
		cadn.setPuUserFullName("AU-YEUNG Tai Ming");
		assertEquals("Tai Ming", candSISWrapper.getFirstName());
		assertEquals("AU-YEUNG", candSISWrapper.getLastName());
		
		cadn.setPuUserFullName("LASTNAMEONLY");
		assertEquals("LASTNAMEONLY", candSISWrapper.getFirstName());
		assertEquals("LASTNAMEONLY", candSISWrapper.getLastName());
		
		cadn.setPuUserFullName("Firstnameonly");
		assertEquals("Firstnameonly", candSISWrapper.getFirstName());
		assertEquals("Firstnameonly", candSISWrapper.getLastName());
	}
	
}
