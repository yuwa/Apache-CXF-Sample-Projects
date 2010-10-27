package bookstore;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Customer {
	@XmlAttribute
	private String id;
	private String name;

	@XmlElementWrapper
	@XmlElements(@XmlElement(name = "address"))
	private List<Address> addresses = new ArrayList<Address>();

	@XmlElementWrapper
	@XmlElements(@XmlElement(name = "order"))
	private List<Order> orders = new ArrayList<Order>();

	@SuppressWarnings("unused")
	private Customer() {
		// Needed by Apache CXF.
	}

	public Customer(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public void addAddress(Address address) {
		addresses.add(address);
	}

	public void addOrder(Order order) {
		orders.add(order);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

}
