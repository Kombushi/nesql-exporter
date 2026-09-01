package com.github.dcysteine.nesql.sql.gregtech.machineprops;

import com.github.dcysteine.nesql.sql.Identifiable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** A named mechanics constant read off GregTech code at export time. */
@Entity
@EqualsAndHashCode
@Getter
@ToString
public class GregTechConstant implements Identifiable<String> {
    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    private long value;

    /** The code member the value was read from. */
    @Column(nullable = false)
    private String source;

    /** Needed by Hibernate. */
    protected GregTechConstant() {}

    public GregTechConstant(String id, String name, long value, String source) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.source = source;
    }
}
