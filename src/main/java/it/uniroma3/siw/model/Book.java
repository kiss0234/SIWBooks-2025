package it.uniroma3.siw.model;


import java.util.List;
import java.util.Objects;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String title;
	
	@NotBlank
	@Size(max = 500, message = "Infos cannot exceed 80 characters")
	private String description; 
	
	@NotNull
	@Positive
	private int publicationDate;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Image cover;
	
	@ManyToMany(mappedBy="books")
	private List<Author> authors;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<Image> immagini;

	@OneToMany(mappedBy="bookReviewed")
	private List<Review> reviews;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Image getCover() {
		return cover;
	}

	public void setCover(Image cover) {
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

	public List<Image> getImmagini() {
		return immagini;
	}

	public void setImmagini(List<Image> immagini) {
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
