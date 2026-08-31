package pe.factos.issuer.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.factos.issuer.domain.model.aggregates.Issuer;
import pe.factos.issuer.domain.model.commands.CreateIssuerCommand;
import pe.factos.issuer.domain.model.valueobjects.Ruc;
import pe.factos.issuer.domain.repositories.IssuerRepository;
import pe.factos.shared.application.result.ApplicationError;
import pe.factos.shared.application.result.Result;

@Service
public class IssuerCommandServiceImpl implements IssuerCommandService {
    private final IssuerRepository issuerRepository;

    public IssuerCommandServiceImpl(IssuerRepository issuerRepository) {
        this.issuerRepository = issuerRepository;
    }

    @Override
    @Transactional
    public Result<Issuer, ApplicationError> handle(CreateIssuerCommand command) {
        var ruc = new Ruc(command.ruc());
        if (issuerRepository.existsByRuc(ruc)) {
            return Result.failure(ApplicationError.conflict("issuer", "Issuer with RUC " + command.ruc() + " already exists"));
        }
        var issuer = new Issuer(ruc, command.corporateName(), command.address(), command.ubigeo());
        var savedIssuer = issuerRepository.save(issuer);
        return Result.success(savedIssuer);
    }
}
