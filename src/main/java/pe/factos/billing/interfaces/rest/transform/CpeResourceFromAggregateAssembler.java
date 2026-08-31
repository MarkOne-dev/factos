package pe.factos.billing.interfaces.rest.transform;

import pe.factos.billing.domain.model.aggregates.Cpe;
import pe.factos.billing.interfaces.rest.resources.CpeResource;
import pe.factos.billing.interfaces.rest.resources.ItemResource;

public final class CpeResourceFromAggregateAssembler {
    private CpeResourceFromAggregateAssembler() {
    }

    public static CpeResource toResourceFromAggregate(Cpe aggregate) {
        var itemResources = aggregate.getItems().stream()
                .map(item -> new ItemResource(
                        item.getCode(),
                        item.getDescription(),
                        item.getQuantity(),
                        item.getUnitPrice().amount(),
                        item.getAffectationType().name()
                ))
                .toList();

        String pdfUrl = "/api/v1/rendering/pdf/" + aggregate.getSeries() + "/" + aggregate.getCorrelative();

        return new CpeResource(
                aggregate.getSeries(),
                aggregate.getCorrelative(),
                aggregate.getCpeType(),
                aggregate.getIssueDate(),
                aggregate.getIssuerRuc().value(),
                aggregate.getAcquirerDocument(),
                aggregate.getAcquirerName(),
                aggregate.getStatus().name(),
                aggregate.getTotals().totalTaxable().amount(),
                aggregate.getTotals().totalIgv().amount(),
                aggregate.getTotals().totalAmount().amount(),
                aggregate.getTotals().totalAmount().currency(),
                pdfUrl,
                itemResources
        );
    }
}
