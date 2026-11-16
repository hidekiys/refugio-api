package br.com.fiap.refugio.model.vo;

/**
 * Value Object de geolocalizacao (latitude/longitude).
 * Mapeia as colunas latitude/longitude de CIDADAO e PONTO_REFRESCO.
 */
public class Coordenada {

    private Double latitude;
    private Double longitude;

    public Coordenada() {
    }

    public Coordenada(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
