package pe.factos.billing.interfaces.rest.transform;

import pe.factos.billing.domain.model.commands.EmitCpeCommand;
import pe.factos.billing.domain.model.commands.ItemCommand;
import pe.factos.billing.interfaces.rest.resources.EmitCpeResource;

public final class EmitCpeCommandFromResourceAssembler {
    private EmitCpeCommandFromResourceAssembler() {
    }

    public static EmitCpeCommand toCommandFromResource(EmitCpeResource resource) {
        var itemCommands = resource.items().stream()
                .map(item -> new ItemCommand(
                        item.code(),
                        item.description(),
                        item.quantity(),
                        item.unitPrice(),
                        item.affectationType() != null ? item.affectationType() : "TAXABLE_ONEROUS"
                ))
                .toList();

        return new EmitCpeCommand(
                resource.series(),
                resource.correlative(),
                resource.cpeType(),
                resource.issueDate(),
                resource.issuerRuc(),
                resource.acquirerDocument(),
                resource.acquirerName(),
                itemCommands,
                resource.currency() != null ? resource.currency() : "PEN"
        );
    }
}
