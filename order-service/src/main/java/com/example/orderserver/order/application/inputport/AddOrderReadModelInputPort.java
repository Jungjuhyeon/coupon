package com.example.orderserver.order.application.inputport;

import com.example.orderserver.order.application.outputport.OrderReadModelOutputPort;
import com.example.orderserver.order.application.usecase.AddOrderReadModelUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddOrderReadModelInputPort implements AddOrderReadModelUseCase {
    private final OrderReadModelOutputPort orderReadModelOutputPort;

    @Override
    public void addOrderReadModel(com.example.orderserver.order.domain.model.document.OrderReadModel document) {
        orderReadModelOutputPort.save(document);
    }
}
