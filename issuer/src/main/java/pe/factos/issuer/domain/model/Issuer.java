package pe.factos.issuer.domain.model;

import pe.factos.shared.domain.BusinessException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Issuer {
    private final Ruc ruc;
    private String corporateName;
    private String address;
    private String ubigeo;
    private DigitalCertificate certificate;
    private final List<AuthorizedSeries> authorizedSeries;

    public Issuer(Ruc ruc, String corporateName, String address, String ubigeo, DigitalCertificate certificate) {
        if (ruc == null) {
            throw new BusinessException("Issuer RUC cannot be null");
        }
        if (corporateName == null || corporateName.isBlank()) {
            throw new BusinessException("Issuer corporate name cannot be empty");
        }
        this.ruc = ruc;
        this.corporateName = corporateName;
        this.address = address;
        this.ubigeo = ubigeo;
        this.certificate = certificate;
        this.authorizedSeries = new ArrayList<>();
    }

    public Ruc getRuc() {
        return ruc;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void updateCorporateName(String corporateName) {
        if (corporateName == null || corporateName.isBlank()) {
            throw new BusinessException("Corporate name cannot be empty");
        }
        this.corporateName = corporateName;
    }

    public String getAddress() {
        return address;
    }

    public void updateAddress(String address) {
        this.address = address;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void updateUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public DigitalCertificate getCertificate() {
        return certificate;
    }

    public void renewCertificate(DigitalCertificate certificate) {
        if (certificate == null) {
            throw new BusinessException("Certificate cannot be null");
        }
        this.certificate = certificate;
    }

    public List<AuthorizedSeries> getAuthorizedSeries() {
        return Collections.unmodifiableList(authorizedSeries);
    }

    public void addAuthorizedSeries(AuthorizedSeries series) {
        if (series == null) {
            throw new BusinessException("Authorized series cannot be null");
        }
        if (authorizedSeries.contains(series)) {
            throw new BusinessException("Series " + series.code() + " is already authorized for this issuer");
        }
        this.authorizedSeries.add(series);
    }

    public boolean isSeriesAuthorized(String seriesCode) {
        return authorizedSeries.stream()
                .anyMatch(s -> s.code().equalsIgnoreCase(seriesCode));
    }
}
