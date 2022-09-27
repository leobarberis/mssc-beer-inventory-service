package guru.sfg.beer.inventory.service.services;

import guru.sfg.brewery.model.BeerOrderDto;

public interface AllocateService {
    Boolean allocateOrder(BeerOrderDto beerOrderDto);
}
