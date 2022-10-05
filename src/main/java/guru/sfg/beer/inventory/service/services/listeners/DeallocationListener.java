package guru.sfg.beer.inventory.service.services.listeners;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.beer.inventory.service.services.AllocateService;
import guru.sfg.brewery.model.events.DeallocateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeallocationListener {

    private final AllocateService allocateService;

    @JmsListener(destination = JmsConfig.DEALLOCATE_ORDER_QUEUE)
    private void listen(DeallocateOrderRequest deallocateOrderRequest) {
        allocateService.deallocateOrder(deallocateOrderRequest.getBeerOrderDto());
    }
}
