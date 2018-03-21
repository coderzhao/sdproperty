package com.quadrant.fr;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class NTechFace
{
    private long id;
    private List<String> galleries;
    private String meta;
    private String photo;
    private String photo_hash;
    private Timestamp timestamp;
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private String normalized;
    private String thumbnail;
    private String person_id;

    private Integer age;
    private List<String> emotions = new ArrayList<>();
    private String gender;

    public int getLeft()
    {
        return x1;
    }

    public int getTop()
    {
        return y1;
    }

    public int getWidth()
    {
        return x2-x1;
    }

    public int getHeight()
    {
        return y2-y1;
    }
    
    public long getId()
    {
        return id;
    }
    public void setId(long id)
    {
        this.id = id;
    }
    public List<String> getGalleries()
    {
        return galleries;
    }
    public void setGalleries(List<String> galleries)
    {
        this.galleries = galleries;
    }
    public String getMeta()
    {
        return meta;
    }
    public void setMeta(String meta)
    {
        this.meta = meta;
    }
    public String getPhoto()
    {
        return photo;
    }
    public void setPhoto(String photo)
    {
        this.photo = photo;
    }
    public String getPhoto_hash()
    {
        return photo_hash;
    }
    public void setPhoto_hash(String photo_hash)
    {
        this.photo_hash = photo_hash;
    }
    public Timestamp getTimestamp()
    {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }
    public int getX1()
    {
        return x1;
    }
    public void setX1(int x1)
    {
        this.x1 = x1;
    }
    public int getX2()
    {
        return x2;
    }
    public void setX2(int x2)
    {
        this.x2 = x2;
    }
    public int getY1()
    {
        return y1;
    }
    public void setY1(int y1)
    {
        this.y1 = y1;
    }
    public int getY2()
    {
        return y2;
    }
    public void setY2(int y2)
    {
        this.y2 = y2;
    }

	public String getNormalized() {
		return normalized;
	}

	public void setNormalized(String normalized) {
		this.normalized = normalized;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getPerson_id() {
		return person_id;
	}

	public void setPerson_id(String person_id) {
		this.person_id = person_id;
	}

	
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public List<String> getEmotions() {
		return emotions;
	}

	public void setEmotions(List<String> emotions) {
		this.emotions = emotions;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

    @Override
    public String toString() {
        return "NTechFace{" +
                "id=" + id +
                ", galleries=" + galleries +
                ", meta='" + meta + '\'' +
                ", photo='" + photo + '\'' +
                ", photo_hash='" + photo_hash + '\'' +
                ", timestamp=" + timestamp +
                ", x1=" + x1 +
                ", x2=" + x2 +
                ", y1=" + y1 +
                ", y2=" + y2 +
                ", normalized='" + normalized + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", person_id='" + person_id + '\'' +
                ", age=" + age +
                ", emotions=" + emotions +
                ", gender='" + gender + '\'' +
                '}';
    }
}
