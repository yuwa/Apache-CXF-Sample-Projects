package test.bookstore.services;

import static test.endtoend.bookstore.builder.CustomerBuilder.aCustomerWithAddressesAndOpenBalanceOfFive;
import static test.endtoend.bookstore.builder.ItemBuilder.anItem;
import static test.endtoend.bookstore.builder.ItemBuilder.anItemOfOneProduct;
import static test.endtoend.bookstore.builder.OrderBuilder.anOrder;
import static test.endtoend.bookstore.builder.ProductBuilder.aProduct;
import static test.endtoend.bookstore.builder.ProductBuilder.aProductWhichIsUnknown;

import java.math.BigDecimal;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import bookstore.Customer;
import bookstore.CustomerManagement;
import bookstore.Item;
import bookstore.Order;
import bookstore.Product;
import bookstore.ProductAvailability;
import bookstore.ShippingService;
import bookstore.Supplier;
import bookstore.UnknownProductFault;
import bookstore.Warehouse;
import bookstore.services.BookstoreJaxWS;

public class BookstoreJaxWSTest {
	private static final String MESSAGE = "message";
	private static final int NOT_IMPORTANT = 0;
	private static final String NOT_AVAILABLE_IN_WAREHOUSE = "xyz";
	private static final BigDecimal NEW_BALANCE_3 = new BigDecimal(3);
	private static final BigDecimal NEW_BALENCE_4 = new BigDecimal(4);
	private static final BigDecimal SINGLE_UNIT_PRICE = new BigDecimal(1);
	private static final BigDecimal TOTAL_PRICE = new BigDecimal(1);
	private static final int AMOUNT_1 = 1;
	private static final boolean IS_AVAILABLE = true;
	private static final int ESTIMATED_TIME_DELIVERY = 1;
	private static final boolean NOT_AVAILABLE = false;
	protected static final int ANY_AMOUNT = 1;
	protected static final String ERROR_MESSAGE = "Product not available";

	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();
	@Mock
	private CustomerManagement customerService;
	@Mock
	private Warehouse warehouse;
	@Mock
	private ShippingService shippingService;
	@Mock
	private Supplier supplier;

	private BookstoreJaxWS bookstoreService;

	@Before
	public void createBookStoreService() {
		bookstoreService = new BookstoreJaxWS(customerService, warehouse, shippingService, supplier);
	}

	@Test
	public void orderExactlyOneProductFormWarehouse() {
		final Product aProduct = aProduct().withSingleUnitPrice(SINGLE_UNIT_PRICE).build();
		final Item anItem = anItem().ofQuantity(1).ofProduct(aProduct).build();
		final Customer aCustomer = aCustomerWithAddressesAndOpenBalanceOfFive();
		final Order anOrder = anOrder().fromCustomer(aCustomer).withItem(anItem).build();

		//@formatter:off
		context.checking(new Expectations() {{
			oneOf(customerService).getCustomer(anOrder.getCustomerId()); will(returnValue(aCustomer));

			oneOf(warehouse).checkAvailability(aProduct, anItem.getQuantity());	will(returnValue(availableInWarehouse()));
			oneOf(warehouse).order(aProduct, anItem.getQuantity()); will(returnValue(TOTAL_PRICE));

			oneOf(shippingService).shipItems(new Item[] {anItem}, aCustomer.getShippingAddress());
			oneOf(customerService).updateAccount(aCustomer.getId(), NEW_BALENCE_4);
			oneOf(customerService).notify(aCustomer, MESSAGE);
        }});
		//@formatter:on

		bookstoreService.requestOrder(anOrder);
	}

	private ProductAvailability availableInWarehouse() {
		return new ProductAvailability(IS_AVAILABLE, ESTIMATED_TIME_DELIVERY);
	};

	@Test
	public void orderExactlyTwoProductsFormWarehouse() {
		final Customer aCustomer = aCustomerWithAddressesAndOpenBalanceOfFive();
		final Product aProduct = aProduct().withSingleUnitPrice(SINGLE_UNIT_PRICE).build();
		final Item anItem1 = anItem().ofQuantity(1).ofProduct(aProduct).build();
		final Item anItem2 = anItem().ofQuantity(1).ofProduct(aProduct).build();
		final Order anOrder = anOrder().fromCustomer(aCustomer).withItem(anItem1).withItem(anItem2).build();

		//@formatter:off
		context.checking(new Expectations() {{
			oneOf(customerService).getCustomer(anOrder.getCustomerId()); will(returnValue(aCustomer));

			allowing(warehouse).checkAvailability(aProduct, AMOUNT_1); will(returnValue(availableInWarehouse()));
			allowing(warehouse).order(aProduct, AMOUNT_1); will(returnValue(TOTAL_PRICE));

			oneOf(shippingService).shipItems(new Item[] {anItem1, anItem2}, aCustomer.getShippingAddress());
			oneOf(customerService).updateAccount(aCustomer.getId(), NEW_BALANCE_3);
			oneOf(customerService).notify(aCustomer, MESSAGE);
		}});
		//@formatter:on

		bookstoreService.requestOrder(anOrder);
	}

	@Test
	public void ordersOneProductNotAvailableInWarehouseButProvidedBySupplier() {
		final Customer aCustomer = aCustomerWithAddressesAndOpenBalanceOfFive();
		final Product aProduct = aProduct().withProductId(NOT_AVAILABLE_IN_WAREHOUSE).withSingleUnitPrice(SINGLE_UNIT_PRICE).build();
		final Item anItem = anItem().ofQuantity(1).ofProduct(aProduct).build();
		final Order anOrder = anOrder().fromCustomer(aCustomer).withItem(anItem).build();

		//@formatter:off
		context.checking(new Expectations() {{
			oneOf(customerService).getCustomer(anOrder.getCustomerId()); will(returnValue(aCustomer));

			oneOf(warehouse).checkAvailability(aProduct, AMOUNT_1); will(returnValue(notAvailableInWarehouse()));

			oneOf(supplier).order(aProduct, AMOUNT_1); will(returnValue(TOTAL_PRICE));

			oneOf(shippingService).shipItems(new Item[] {anItem}, aCustomer.getShippingAddress());
			oneOf(customerService).updateAccount(aCustomer.getId(), NEW_BALENCE_4);
			oneOf(customerService).notify(aCustomer, MESSAGE);
		}});
		//@formatter:on

		bookstoreService.requestOrder(anOrder);
	}

	private ProductAvailability notAvailableInWarehouse() {
		return new ProductAvailability(NOT_AVAILABLE, NOT_IMPORTANT);
	};

	@Test
	public void notifysCustomerThatProductWasUnknown() {
		//@formatter:off
		final Customer aCustomer = aCustomerWithAddressesAndOpenBalanceOfFive();
		final Product aProduct = aProductWhichIsUnknown();
		final Order anOrder = anOrder()
								.withItem(anItemOfOneProduct(aProduct))
								.fromCustomer(aCustomer).build();

		context.checking(new Expectations() {{
			oneOf(customerService).getCustomer(anOrder.getCustomerId()); will(returnValue(aCustomer));

			oneOf(warehouse).checkAvailability(aProduct, ANY_AMOUNT); will(returnValue(notAvailableInWarehouse()));

			oneOf(supplier).order(aProduct, ANY_AMOUNT); will(throwException(new UnknownProductFault(ERROR_MESSAGE)));

			oneOf(customerService).notify(anOrder.getCustomer(), ERROR_MESSAGE);
		}});
		//@formatter:on

		bookstoreService.requestOrder(anOrder);
	}
}
