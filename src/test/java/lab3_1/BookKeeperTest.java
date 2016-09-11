package lab3_1;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.com.bottega.ecommerce.application.api.builder.BookKeeperBuilder;
import pl.com.bottega.ecommerce.application.api.builder.InvoiceRequestBuilder;
import pl.com.bottega.ecommerce.application.api.builder.ProductDataBuilder;
import pl.com.bottega.ecommerce.application.api.builder.RequestItemBuilder;
import pl.com.bottega.ecommerce.application.api.builder.TaxBuilder;
import pl.com.bottega.ecommerce.sales.domain.invoicing.BookKeeper;
import pl.com.bottega.ecommerce.sales.domain.invoicing.Invoice;
import pl.com.bottega.ecommerce.sales.domain.invoicing.InvoiceRequest;
import pl.com.bottega.ecommerce.sales.domain.invoicing.TaxPolicy;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductData;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sharedkernel.Money;
import pl.com.bottega.ecommerce.sales.domain.invoicing.RequestItem;

public class BookKeeperTest {

	private BookKeeper bookKeeper;
	private TaxPolicy taxPolicy;
	private InvoiceRequest invoiceRequest;

	@Before
	public void start() {
		taxPolicy = Mockito.mock(TaxPolicy.class);
		bookKeeper = new BookKeeperBuilder().build();
		invoiceRequest = new InvoiceRequestBuilder().build();
		
		Mockito.when(taxPolicy.calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class))).thenReturn(new TaxBuilder().build());
	}

	@Test
	public void firstTestCase() {
	
		ProductData productData = new ProductDataBuilder().build();
		RequestItem item = new RequestItemBuilder().build();
		invoiceRequest.add(item);

		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertThat(invoice.getItems().size(), Matchers.is(1));
		//assertThat(invoice.getItems().get(0).getProduct(), Matchers.is(productData));
	}
	
	@Test
	public void secondTestCase() {
		
		ProductData productData = new ProductDataBuilder().build();
		RequestItem item = new RequestItemBuilder().withProductData(productData).build();
		invoiceRequest.add(item);
		
		ProductData productData2 = new ProductDataBuilder().build();
		RequestItem item2 = new RequestItemBuilder().withProductData(productData2).build();
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
		
		RequestItem item = new RequestItemBuilder().build();
		invoiceRequest.add(item);

		Invoice invoice = bookKeeper.issuance(invoiceRequest, taxPolicy);
		assertThat(invoice.getGros(), Matchers.is(new Money(new BigDecimal(200), Currency.getInstance("USD"))));
		assertThat(invoice.getNet(), Matchers.is(new Money(new BigDecimal(100), Currency.getInstance("USD"))));
		assertThat(invoice.getItems().get(0).getGros(), Matchers.is(new Money(new BigDecimal(200), Currency.getInstance("USD"))));
		assertThat(invoice.getItems().get(0).getNet(), Matchers.is(new Money(new BigDecimal(100), Currency.getInstance("USD"))));
		assertThat(invoice.getItems().get(0).getTax().getAmount(), Matchers.is(new Money(new BigDecimal(100), Currency.getInstance("USD"))));
		Mockito.verify(taxPolicy, Mockito.times(1)).calculateTax(Mockito.any(ProductType.class), Mockito.any(Money.class));
	}


}
