package com.texgen.model;
import com.texgen.types.SectionType;
import jakarta.persistence.*;

@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int id;

    @Enumerated(EnumType.STRING)
    private SectionType type;

    @ManyToOne
    @JoinColumn(name = "document_id", nullable = false)
    private int documentId;

    public Section(int id, SectionType type, int documentId) {
        this.id = id;
        this.type = type;
        this.documentId = documentId;
    }

    public int getId() { return id; }
    public SectionType getType() { return type; }
}
