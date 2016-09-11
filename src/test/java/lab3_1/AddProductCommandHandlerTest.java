package lab3_1;

import org.junit.Before;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.Id;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProductCommand;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandler;
import pl.com.bottega.ecommerce.sales.application.api.handler.AddProductCommandHandlerMock;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;


public class AddProductCommandHandlerTest
{

    private AddProductCommandHandler productCommandHandler;
    private AddProductCommand productCommand;
    private ProductRepository productRepository;
    private ReservationRepository reservationRepository;
    private SuggestionService suggestionService;
    private ClientRepository clientRepository;
    private SystemContext systemContext;

    @Before
    public void init()
    {
        reservationRepository = AddProductCommandHandlerMock.reservationRepositoryMock();
        productRepository = AddProductCommandHandlerMock.productRepositoryMock();
        suggestionService = AddProductCommandHandlerMock.suggestionServiceMock();
        clientRepository = AddProductCommandHandlerMock.clientRepositoryMock();
        systemContext = new SystemContext();

        productCommandHandler = new AddProductCommandHandler(reservationRepository, productRepository, suggestionService, clientRepository, systemContext);
        productCommand = new AddProductCommand(new Id("1"),new Id("1"),5);

        productCommandHandler.handle(productCommand);
    }


}
