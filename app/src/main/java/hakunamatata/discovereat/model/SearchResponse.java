package hakunamatata.discovereat.model;

import java.util.List;

public class SearchResponse {

    private String status;
    private int total;
    private List<Business> businesses;

    public SearchResponse(String status, int total, List<Business> businesses) {
        this.status = status;
        this.total = total;
        this.businesses = businesses;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter and setter methods for total
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    // Getter and setter methods for businesses
    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    // Inner class to represent a Business
    public static class Business {
        private String name;
        private String id;
        private String phone;
        private Location location;
        private double rating;
        private String price;
        private String image_url; // Aggiungi questo campo

        // Getter and setter methods

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImageUrl() {
            return image_url;
        }

        public void setImageUrl(String image_url) {
            this.image_url = image_url;
        }

        // Inner class to represent Location
        public static class Location {
            private String address1;
            private String city;
            private String zip_code;
            private String country;
            private String state;

            public String getAddress1() {
                return address1;
            }

            public void setAddress1(String address1) {
                this.address1 = address1;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getZip_code() {
                return zip_code;
            }

            public void setZip_code(String zip_code) {
                this.zip_code = zip_code;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }
        }
    }
}
