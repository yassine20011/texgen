package com.texgen.model;
import com.texgen.types.DocumentType;
import jakarta.persistence.*;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private int userId;

    public Document(int id, String title, DocumentType type, int userId) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.userId = userId;
    }

    public int getId() {return id;}
    public String getTitle() {return title;}
    public DocumentType getType() { return type; }



}
