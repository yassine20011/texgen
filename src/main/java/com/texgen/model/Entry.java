package com.texgen.model;
import jakarta.persistence.*;

@Entity
@Table(name = "entries")
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private int sectionId;

    public Entry(int id, String content, int sectionId) {
        this.id = id;
        this.content = content;
        this.sectionId = sectionId;
    }

    public int getId() { return id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
