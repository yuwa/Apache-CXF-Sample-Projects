package bookstore;

import java.math.BigDecimal;

import javax.jws.WebMethod;
import javax.jws.WebService;

//@formatter:off
@WebService(serviceName = "SupplierService",
			portName = "Supplier1PT",
			targetNamespace = "http://infosys.tuwien.ac.at/aic10/ass1/dto/supplier")
public interface Supplier1 {
//@formatter:on

	/** @return total price. */
	@WebMethod(operationName = "order")
	BigDecimal order(Product product, int amount);
	// TODO throws UnknownProductFault

}
