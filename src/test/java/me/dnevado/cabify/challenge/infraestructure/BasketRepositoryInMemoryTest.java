package me.dnevado.cabify.challenge.infraestructure;

import io.quarkus.test.junit.QuarkusTest;
import me.dnevado.cabify.challenge.domain.model.ReturnMessage;
import me.dnevado.cabify.challenge.infrastructure.CabifyRepositoryInMemory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Optional;

@QuarkusTest
public class BasketRepositoryInMemoryTest {

    @Inject
    CabifyRepositoryInMemory cabifyRepository;
    /* 
    @Test
    public void testcreateAvailableCars() {
        Optional<ReturnMessage> cars = createAvailableCars();
        cars.ifPresentOrElse(b1 -> {
                    this.basketRepository
                            .getBasketByCode(b1.getCode())
                            .ifPresentOrElse(b2 -> {
                                Assertions.assertEquals(b1.getCode(), b2.getCode());
                                Assertions.assertEquals(b1.getItems().size(), 0);
                                Assertions.assertEquals(b1.getPriceNumber(), BigDecimal.ZERO);
                                    },
                                    () -> Assertions.fail());
                },
                () -> Assertions.fail()
        );
    }

    @Test
    public void testDeleteBasket() {
        Optional<CarsOld> basket = createBasket();
        basket.ifPresentOrElse(b1 -> {
                    this.basketRepository
                            .deleteBasket(b1.getCode())
                            .ifPresentOrElse(b2 -> Assertions.assertEquals(b1.getCode(), b2.getCode()),
                                    () -> Assertions.fail());
                },
                () -> Assertions.fail()
        );
    }

    @Test
    public void testDeleteBasketNotExist() {
        Optional<CarsOld> basketDeleted = this.basketRepository
                .deleteBasket("not_exist_code");
        basketDeleted.ifPresentOrElse(
                b -> Assertions.fail(),
                () -> Assertions.assertTrue(true)
        );
    }

    @Test
    public void testGetBasketByCode() {
        Optional<CarsOld> basketCreated = createBasket();
        basketCreated.ifPresentOrElse(
                b1 -> this.basketRepository
                        .getBasketByCode(b1.getCode()).ifPresentOrElse(
                                b2 -> Assertions.assertEquals(b1.getCode(), b2.getCode()),
                                () -> Assertions.fail()
                        ),
                () -> Assertions.fail()
        );
    }

    @Test
    public void testGetBasketByCodeNotExist() {
        Optional<CarsOld> basketGot = this.basketRepository
                .getBasketByCode("not_exist_code");
        basketGot.ifPresentOrElse(
                b -> Assertions.fail(),
                () -> Assertions.assertTrue(true)
        );
    }

    @Test
    public void testAddItemToBasket() {
        Optional<CarsOld> basketCreated = createBasket();
        Optional<Item> item = this.itemAdapter.getItemByCode("TSHIRT");
        basketCreated.ifPresent(b -> {
            item.ifPresent(i -> {
                this.basketRepository.addItemToBasket(b.getCode(), i, Optional.empty())
                        .ifPresentOrElse(
                                b2 -> Assertions.assertTrue(b.getItems().containsKey(i)),
                                () -> Assertions.fail()
                        );
            });
        });

    }

    @Test
    public void testAddItemWithDiscountToBasket() {
        String itemCode = "TSHIRT";
        Optional<Journeys> discount = this.discountAdapter.getItemByItemCode(itemCode);
        Optional<CarsOld> basketCreated = createBasket();
        Optional<Item> item = this.itemAdapter.getItemByCode(itemCode);
        basketCreated.ifPresent(b -> {
            item.ifPresent(i -> {
                this.basketRepository.addItemToBasket(b.getCode(), i, discount)
                        .ifPresentOrElse(
                                b2 -> {
                                    Assertions.assertTrue(b.getItems().containsKey(i));
                                    Assertions.assertEquals(b.getDiscount().get(i).getItemCode(), itemCode);
                                },
                                () -> Assertions.fail()
                        );
            });
        });

    }

    @Test
    public void testAddItemWithNODiscountToBasket() {
        String itemCode = "CHELO";
        Optional<CarsOld> basketCreated = createBasket();
        Optional<Item> itemGot = this.itemAdapter.getItemByCode(itemCode);
        Optional<Journeys> discount = this.discountAdapter.getItemByItemCode(itemCode);
        basketCreated.ifPresent(basket -> {
            itemGot.ifPresent(item -> {
                this.basketRepository.addItemToBasket(basket.getCode(), item, discount)
                        .ifPresentOrElse(
                                b2 -> {
                                    //items exist
                                    Assertions.assertTrue(b2.getItems().containsKey(itemGot));
                                    //no disccounts
                                    Assertions.assertTrue(b2.getDiscount().size() == 0);
                                },
                                () -> Assertions.fail()
                        );
            });
        });
    }

    @Test
    public void testGetPriceOneItemToBasket() {
        Optional<CarsOld> basketCreated = createBasket();
        Optional<Item> item = this.itemAdapter.getItemByCode("TSHIRT");
        basketCreated.ifPresent(b -> {
            item.ifPresent(i -> {
                this.basketRepository.addItemToBasket(b.getCode(), i, Optional.empty())
                        .ifPresentOrElse(
                                b2 -> {
                                    Assertions.assertTrue(b.getItems().containsKey(i));
                                },
                                () -> Assertions.fail()
                        );
            });
        });
    }

    private Optional<ReturnMessage> createAvailableCars() {
        return this.cabifyRepository.createAvailableCars("This is my basket");
    }*/
}