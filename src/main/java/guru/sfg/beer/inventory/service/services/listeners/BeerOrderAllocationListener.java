package guru.sfg.beer.inventory.service.services.listeners;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.beer.inventory.service.services.AllocateService;
import guru.sfg.brewery.model.events.AllocateOrderRequest;
import guru.sfg.brewery.model.events.AllocationOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BeerOrderAllocationListener {
    private final AllocateService allocateService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(AllocateOrderRequest request) {
        AllocationOrderResult.AllocationOrderResultBuilder allocationOrderResultBuilder = AllocationOrderResult.builder();
        allocationOrderResultBuilder.beerOrderDto(request.getBeerOrderDto());
        try {
            final Boolean allocationResult = allocateService.allocateOrder(request.getBeerOrderDto());
            allocationOrderResultBuilder.pendingInventory(!allocationResult);
            allocationOrderResultBuilder.allocationError(false);
        } catch (Exception e) {
            log.error("Allocation failed for Order id: " + request.getBeerOrderDto().getId());
            allocationOrderResultBuilder.allocationError(true);
        }
        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESULT_QUEUE,
                allocationOrderResultBuilder.build());
    }
}
