package lab3_1;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.invoicing.BookKeeper;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Invoice;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceFactory;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Tax;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;
public class BookKeeperTest {

	private BookKeeper bookKeeper;
	private InvoiceFactory invoiceFactory;
	private TaxPolicy taxPolicy;
	private InvoiceRequest invoiceRequest;

	@Before
	public void start() {
		taxPolicy = Mockito.mock(TaxPolicy.class);
		invoiceFactory = new InvoiceFactory();
		bookKeeper = new BookKeeper(invoiceFactory);
		invoiceRequest = new InvoiceRequest(new ClientData(Id.generate(), "Client"));
		
		Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class)))
		   .thenReturn(new Tax(new Money(new BigDecimal(1000), Currency.getInstance("EUR")), "Podatek za cos tam."));
	}

	@Test
	public void firstTestCase() {
	
		ProductData productData = new ProductData(Id.generate(),new Money(new BigDecimal(1000), Currency.getInstance("EUR")), "Standard", ProductType.STANDARD,new Date());
		Money totalCost = new Money(new BigDecimal(10000), Currency.getInstance("EUR"));
		RequestItem item = new RequestItem(productData, 10, totalCost);
		invoiceRequest.add(item);

		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertThat(invoice.getItems().size(), Matchers.is(1));
		assertThat(invoice.getItems().get(0).getProduct(), Matchers.is(productData));
	}
	
	@Test
	public void secondTestCase() {
		
		ProductData productData = new ProductData(Id.generate(),new Money(new BigDecimal(1000), Currency.getInstance("EUR")), "Standard", ProductType.STANDARD,new Date());
		Money totalCost = new Money(new BigDecimal(10000), Currency.getInstance("EUR"));
		RequestItem item = new RequestItem(productData, 10, totalCost);
		
		ProductData productData2 = new ProductData(Id.generate(),new Money(new BigDecimal(2000), Currency.getInstance("EUR")), "Drug", ProductType.DRUG,new Date());
		Money totalCost2 = new Money(new BigDecimal(10000), Currency.getInstance("EUR"));
		RequestItem item2 = new RequestItem(productData2, 5, totalCost2);
		invoiceRequest.add(item);
		invoiceRequest.add(item2);

		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertThat(invoice.getItems().size(), Matchers.is(2));
		Mockito.verify(taxPolicy, Mockito.times(2)).calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class));
	}
	
	@Test
	public void thirdTestCase() {
		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertThat(invoice.getItems().size(), Matchers.is(0));
		Mockito.verify(taxPolicy, Mockito.never()).calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class));
	}
	
	@Test
	public void fourthTestCase() {
		
		ProductData productData = new ProductData(Id.generate(),new Money(new BigDecimal(1000), Currency.getInstance("EUR")), "Standard", ProductType.STANDARD,new Date());
		Money totalCost = new Money(new BigDecimal(10000), Currency.getInstance("EUR"));
		RequestItem item = new RequestItem(productData, 10, totalCost);
		invoiceRequest.add(item);

		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertThat(invoice.getGros(), Matchers.is(new Money(new BigDecimal(11000), Currency.getInstance("EUR"))));
		assertThat(invoice.getNet(), Matchers.is(new Money(new BigDecimal(10000), Currency.getInstance("EUR"))));
		assertThat(invoice.getItems().get(0).getGros(), Matchers.is(new Money(new BigDecimal(11000), Currency.getInstance("EUR"))));
		assertThat(invoice.getItems().get(0).getNet(), Matchers.is(new Money(new BigDecimal(10000), Currency.getInstance("EUR"))));
		assertThat(invoice.getItems().get(0).getTax().getAmount(), Matchers.is(new Money(new BigDecimal(1000), Currency.getInstance("EUR"))));
		Mockito.verify(taxPolicy, Mockito.times(1)).calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class));
	}


}
