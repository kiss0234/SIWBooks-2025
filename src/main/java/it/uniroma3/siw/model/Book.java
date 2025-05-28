package it.uniroma3.siw.model;

import java.util.Base64;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String title;
	
	private String description; 
	
	private int publicationDate;
	
	@Lob
	@JdbcTypeCode(SqlTypes.VARBINARY)
	private byte[] cover;
	
	@ManyToMany(mappedBy="books")
	private List<Author> authors;
	
	@OneToMany(mappedBy="book", cascade = CascadeType.ALL)
	private List<BookImage> immagini;

	@OneToMany(mappedBy="bookReviewed")
	private List<Review> reviews;
	
	@Transient
	private String base64Image;

	public String getBase64Image() {
	    if (this.cover != null) {
	        return Base64.getEncoder().encodeToString(this.cover);
	    }
	    return null;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getCover() {
		return cover;
	}

	public void setCover(byte[] cover) {
		this.cover = cover;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(int publicationDate) {
		this.publicationDate = publicationDate;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<BookImage> getImmagini() {
		return immagini;
	}

	public void setImmagini(List<BookImage> immagini) {
		this.immagini = immagini;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	@Override
	public int hashCode() {
		return Objects.hash(publicationDate, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return publicationDate == other.publicationDate && Objects.equals(title, other.title);
	}
}
