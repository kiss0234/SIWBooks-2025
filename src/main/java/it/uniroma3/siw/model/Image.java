package it.uniroma3.siw.model;

import java.util.Base64;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;

@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @JdbcTypeCode(SqlTypes.VARBINARY)
    private byte[] data;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	
	@Transient
	private String base64Image;

	public String getBase64Image() {
	    if (this.data != null) {
	        return Base64.getEncoder().encodeToString(this.data);
	    }
	    return null;
	}

    
}