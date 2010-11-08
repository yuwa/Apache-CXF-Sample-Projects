package bookstore.services;

import java.math.BigDecimal;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import bookstore.BookstoreLibrary;
import bookstore.Customer;
import bookstore.CustomerManagement;
import bookstore.InformationReporter;

@Path("/customerservice/")
public class CustomerManagementJaxRS implements CustomerManagement {

	private BookstoreLibrary database;
	private InformationReporter reporter;

	public CustomerManagementJaxRS(BookstoreLibrary database, InformationReporter reporter) {
		this.database = database;
		this.reporter = reporter;
	}

	@POST
	@Path("/customers/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Customer createCustomer(Customer customer) {
		database.addCustomer(customer);
		return customer;
	}

	@DELETE
	@Path("/customers/{id}/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void deleteCustomer(String id) {
		database.deleteCustomer(id);
	}

	@GET
	@Path("/customers/{id}/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Customer getCustomer(@PathParam("id") String id) {
		Customer customer = database.getCustomer(id);
		reporter.notifyGetCustomerRequest(id, customer);
		return customer;
	}

	@PUT
	@Path("/customers/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void updateCustomer(Customer customer) {
		database.updateCustomer(customer);
	}

	@PUT
	@Path("/customers/{id}/account")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void updateAccount(@PathParam("id") String id, BigDecimal balance) {
		// TODO Implement reporter notification for updateAccount.
		database.updateAccount(database.getCustomer(id), balance);
	}

	@PUT
	@Path("/customers/notification/{message}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public void notify(Customer customer, @PathParam("message") String message) {
		// TODO Implement reporter notification for notifyCustomer.
		Customer c = database.getCustomer(customer.getId());
		c.notify(message);
	}
}
