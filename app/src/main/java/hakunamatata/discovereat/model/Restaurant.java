package hakunamatata.discovereat.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Class to represent Restaurant Objects
 */
@Entity(indices = {@Index(value = {"id"},
        unique = true)})
public class Restaurant{
    @PrimaryKey (autoGenerate = true)
    private long idDb;
    private String id;
    private String name;
    @ColumnInfo(name = "locationSearch")
    private String locationString;
    private Location location;
    private String price;
    private double rating;
    @ColumnInfo(name = "url_to_image")
    @SerializedName("image_url")
    private String imageUrl;
    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;
    @ColumnInfo(name = "is_synchronized")
    private boolean isSynchronized;
    @SerializedName("phone")
    private String phone;
    @ColumnInfo(name = "display_phone")
    @SerializedName("display_phone")
    private String displayPhone;
    private double distance;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDisplayPhone() {
        return displayPhone;
    }

    public void setDisplayPhone(String displayPhone) {
        this.displayPhone = displayPhone;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Restaurant(String id, String name, String locationString, Location location, String price, double rating, String imageUrl, boolean isFavorite, boolean isSynchronized, String phone, String displayPhone, double distance) {
        this.id = id;
        this.name = name;
        this.locationString = locationString;
        this.location = location;
        this.price = price;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.isFavorite = isFavorite;
        this.isSynchronized = isSynchronized;
        this.phone = phone;
        this.displayPhone = displayPhone;
        this.distance = distance;
    }

    public Restaurant() {
    }

    public String getLocationString() {
        return locationString;
    }

    public void setLocationString(String locationString) {
        this.locationString = locationString;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getIdDb() {
        return idDb;
    }

    public void setIdDb(long idDb) {
        this.idDb = idDb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Exclude
    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void setSynchronized(boolean aSynchronized) {
        isSynchronized = aSynchronized;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "idDb=" + idDb +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", price='" + price + '\'' +
                ", rating=" + rating +
                ", imageUrl='" + imageUrl + '\'' +
                ", isFavorite=" + isFavorite +
                ", isSynchronized=" + isSynchronized +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return idDb == that.idDb && isFavorite == that.isFavorite &&
                id.equals(that.id) && Objects.equals(name, that.name) &&
                Objects.equals(imageUrl, that.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDb, id, name, imageUrl, isFavorite);
    }

}
