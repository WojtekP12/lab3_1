package pl.com.bottega.ecommerce.sales.application.api.handler;

import org.mockito.Mockito;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductType;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.sharedkernel.Money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;


public class AddProductCommandHandlerMock
{

    private static Product product = new Product(new Id("1"), new Money(new BigDecimal(1000), Currency.getInstance(Locale.getDefault())), "Test", ProductType.DRUG);
    private static Id id = new Id("1");
    private static Client client = new Client();

    public static ReservationRepository reservationRepositoryMock()
    {
        ReservationRepository reservationRepository = Mockito.mock(ReservationRepository.class);
        Mockito.when(reservationRepository.load(new Id("1"))).thenReturn(new Reservation(id, Reservation.ReservationStatus.OPENED, new ClientData(id, "Client1"), new Date()));

        return reservationRepository;
    }

    public static ProductRepository productRepositoryMock()
    {
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        Mockito.when(productRepository.load(id)).thenReturn(product);

        return productRepository;
    }

    public static ClientRepository clientRepositoryMock()
    {
        ClientRepository clientRepository = Mockito.mock(ClientRepository.class);
        Mockito.when(clientRepository.load(id)).thenReturn(client);

        return clientRepository;
    }

    public static SuggestionService suggestionServiceMock()
    {
        SuggestionService suggestionService = Mockito.mock(SuggestionService.class);
        Mockito.when(suggestionService.suggestEquivalent(product, client)).thenReturn(product);

        return suggestionService;
    }

}
