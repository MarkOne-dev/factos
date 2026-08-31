package pe.factos.catalog.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.factos.catalog.domain.model.aggregates.CatalogItem;
import pe.factos.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;

@Entity
@Table(name = "catalog_items")
@Getter
@Setter
@NoArgsConstructor
public class CatalogItemJpaEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "catalog_code", nullable = false)
    private String catalogCode;

    @Column(name = "item_code", nullable = false)
    private String itemCode;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public CatalogItemJpaEntity(CatalogItem catalogItem) {
        this.catalogCode = catalogItem.getCatalogCode();
        this.itemCode = catalogItem.getItemCode();
        this.description = catalogItem.getDescription();
        this.active = catalogItem.isActive();
    }

    public CatalogItem toDomain() {
        return new CatalogItem(catalogCode, itemCode, description, active);
    }
}
